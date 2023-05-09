/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.future.PromiseInternal;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.stomp.Acknowledgement;
import io.vertx.ext.stomp.BridgeOptions;
import io.vertx.ext.stomp.DefaultAbortHandler;
import io.vertx.ext.stomp.DefaultAckHandler;
import io.vertx.ext.stomp.DefaultBeginHandler;
import io.vertx.ext.stomp.DefaultCommitHandler;
import io.vertx.ext.stomp.DefaultConnectHandler;
import io.vertx.ext.stomp.DefaultNackHandler;
import io.vertx.ext.stomp.DefaultSendHandler;
import io.vertx.ext.stomp.DefaultSubscribeHandler;
import io.vertx.ext.stomp.DefaultUnsubscribeHandler;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.DestinationFactory;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Frames;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A plug-able implementation of {@link StompServerHandler}. The default behavior is compliant with the STOMP
 * specification.
 * <p/>
 * By default {@code ACK/NACK} are managed as a dead messages. Not acknowledges messages are dropped from the list
 * and a warning is printed in the log.
 * <p/>
 * This class is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultStompHandler implements StompServerHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStompHandler.class);
  private final Vertx vertx;
  private final Context context;

  private Handler<ServerFrame> connectHandler;

  private Handler<ServerFrame> stompHandler;

  private Handler<ServerFrame> sendHandler = new DefaultSendHandler();

  private Handler<ServerFrame> subscribeHandler = new DefaultSubscribeHandler();

  private Handler<ServerFrame> unsubscribeHandler = new DefaultUnsubscribeHandler();

  private Handler<StompServerConnection> closeHandler;

  private Handler<ServerFrame> commitHandler = new DefaultCommitHandler();
  private Handler<ServerFrame> abortHandler = new DefaultAbortHandler();
  private Handler<ServerFrame> beginHandler = new DefaultBeginHandler();

  private Handler<ServerFrame> ackHandler = new DefaultAckHandler();

  private Handler<ServerFrame> nackHandler = new DefaultNackHandler();

  private Handler<ServerFrame> disconnectHandler = (sf -> {
    StompServerConnection connection = sf.connection();
    Frames.handleReceipt(sf.frame(), connection);
    connection.close();
  });

  private AuthenticationProvider authProvider;

  private Handler<StompServerConnection> pingHandler = StompServerConnection::ping;

  private Handler<Acknowledgement> onAckHandler = (acknowledgement) -> LOGGER.info("Acknowledge messages - " +
      acknowledgement.frames());
  private Handler<Acknowledgement> onNackHandler = (acknowledgement) ->
      LOGGER.warn("Messages not acknowledge - " + acknowledgement.frames());

  private final LocalMap<Destination, String> destinations;

  // user is mutable and built from other modules so there's no guarantees
  // about thread safety so use w/ care..
  private final ConcurrentHashMap<String, User> users;

  private DestinationFactory factory = Destination::topic;

  private Handler<ServerFrame> receivedFrameHandler;

  /**
   * Creates a new instance of {@link DefaultStompHandler}.
   *
   * @param vertx the vert.x instance
   */
  public DefaultStompHandler(Vertx vertx) {
    this.vertx = vertx;
    this.context = Vertx.currentContext();
    this.destinations = vertx.sharedData().getLocalMap("stomp.destinations");
    this.users = new ConcurrentHashMap<>();
    this.connectHandler = new DefaultConnectHandler();
  }

  @Override
  public synchronized void onClose(StompServerConnection connection) {
    // Default behavior.
    getDestinations().stream().forEach((d) -> d.unsubscribeConnection(connection));
    Transactions.instance().unregisterTransactionsFromConnection(connection);

    // Remove user, if exists
    this.users.remove(connection.session());

    if (closeHandler != null) {
      closeHandler.handle(connection);
    }
  }

  @Override
  public synchronized StompServerHandler receivedFrameHandler(Handler<ServerFrame> handler) {
    this.receivedFrameHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler connectHandler(Handler<ServerFrame> handler) {
    this.connectHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler stompHandler(Handler<ServerFrame> handler) {
    this.stompHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler subscribeHandler(Handler<ServerFrame> handler) {
    this.subscribeHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler) {
    this.unsubscribeHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler sendHandler(Handler<ServerFrame> handler) {
    this.sendHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler closeHandler(Handler<StompServerConnection> handler) {
    this.closeHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler commitHandler(Handler<ServerFrame> handler) {
    this.commitHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler abortHandler(Handler<ServerFrame> handler) {
    this.abortHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler beginHandler(Handler<ServerFrame> handler) {
    this.beginHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler disconnectHandler(Handler<ServerFrame> handler) {
    this.disconnectHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler ackHandler(Handler<ServerFrame> handler) {
    this.ackHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler nackHandler(Handler<ServerFrame> handler) {
    this.nackHandler = handler;
    return this;
  }

  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
    connection.onServerActivity();

    synchronized (this) {
      if (receivedFrameHandler != null) {
        receivedFrameHandler.handle(serverFrame);
      }
    }

    switch (frame.getCommand()) {
      case CONNECT:
        handleConnect(frame, connection);
        break;
      case STOMP:
        handleStomp(frame, connection);
        break;
      case SEND:
        handleSend(frame, connection);
        break;
      case SUBSCRIBE:
        handleSubscribe(frame, connection);
        break;
      case UNSUBSCRIBE:
        handleUnsubscribe(frame, connection);
        break;
      case BEGIN:
        handleBegin(frame, connection);
        break;
      case ABORT:
        handleAbort(frame, connection);
        break;
      case COMMIT:
        handleCommit(frame, connection);
        break;
      case ACK:
        handleAck(frame, connection);
        break;
      case NACK:
        handleNack(frame, connection);
        break;
      case DISCONNECT:
        handleDisconnect(frame, connection);
        break;
      case PING:
        // We received a ping frame, we do nothing.
        break;
      default:
        // Unknown frames
        break;
    }
  }

  private void handleAck(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = ackHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleNack(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = nackHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleBegin(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = beginHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleAbort(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = abortHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleCommit(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = commitHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleSubscribe(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = subscribeHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleUnsubscribe(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = unsubscribeHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleSend(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = sendHandler;
    }

    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleConnect(Frame frame, StompServerConnection connection) {

    Handler<ServerFrame> handler;
    Handler<StompServerConnection> pingH;
    synchronized (this) {
      handler = connectHandler;
      pingH = pingHandler;
    }

    // Compute heartbeat, and register pinger and ponger
    // Stomp server acts as a client to call the computePingPeriod & computePongPeriod method
    long ping = Frame.Heartbeat.computePingPeriod(
      Frame.Heartbeat.create(connection.server().options().getHeartbeat()),
      Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));
    long pong = Frame.Heartbeat.computePongPeriod(
        Frame.Heartbeat.create(connection.server().options().getHeartbeat()),
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));

    connection.configureHeartbeat(ping, pong, pingH);

    // Then, handle the frame.
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleDisconnect(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = disconnectHandler;
    }
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
  }

  private void handleStomp(Frame frame, StompServerConnection connection) {
    Handler<ServerFrame> handler;
    synchronized (this) {
      handler = stompHandler;
    }
    if (handler == null) {
      // Per spec, STOMP frame must be handled as CONNECT
      handleConnect(frame, connection);
      return;
    }
    handler.handle(new ServerFrameImpl(frame, connection));
  }

  @Override
  public synchronized StompServerHandler authProvider(AuthenticationProvider handler) {
    this.authProvider = handler;
    return this;
  }

  @Override
  public Future<Boolean> onAuthenticationRequest(StompServerConnection connection, String login, String passcode) {
    PromiseInternal<Boolean> promise = ((ContextInternal) context).promise();
    onAuthenticationRequest(connection, login, passcode, promise);
    return promise.future();
  }

  public StompServerHandler onAuthenticationRequest(StompServerConnection connection,
                                                    String login, String passcode,
                                                    Handler<AsyncResult<Boolean>> handler) {
    final AuthenticationProvider auth;
    synchronized (this) {
      // Stack contention.
      auth = authProvider;
    }

    final StompServer server = connection.server();
    if (!server.options().isSecured()) {
      if (auth != null) {
        LOGGER.warn("Authentication handler set while the server is not secured");
      }
      context.runOnContext(v -> handler.handle(Future.succeededFuture(true)));
      return this;
    }

    if (server.options().isSecured() && auth == null) {
      LOGGER.error("Cannot authenticate connection - no authentication provider");
      context.runOnContext(v -> handler.handle(Future.succeededFuture(false)));
      return this;
    }

    context.runOnContext(v ->
        auth.authenticate(new UsernamePasswordCredentials(login, passcode))
          .onFailure(err -> context.runOnContext(v2 -> handler.handle(Future.succeededFuture(false))))
          .onSuccess(user -> {
            // make the user available
            users.put(connection.session(), user);
            context.runOnContext(v2 -> handler.handle(Future.succeededFuture(true)));
        }));
    return this;
  }

  /**
   * Return the authenticated user for this session.
   *
   * @param session session ID for the server connection.
   * @return the user provided by the {@link AuthenticationProvider} or null if not found.
   */
  @Override
  public User getUserBySession(String session) {
    return this.users.get(session);
  }

  @Override
  public List<Destination> getDestinations() {
    return new ArrayList<>(destinations.keySet());
  }

  /**
   * Gets the destination with the given name..
   *
   * @param destination the destination
   * @return the {@link Destination}, {@code null} if not found.
   */
  public Destination getDestination(String destination) {
    for (Destination d : destinations.keySet()) {
      if (d.matches(destination)) {
        return d;
      }
    }
    return null;
  }

  public Destination getOrCreateDestination(String destination) {
    DestinationFactory destinationFactory;
    synchronized (this) {
      destinationFactory = this.factory;
    }
    synchronized (vertx) {
      Destination d = getDestination(destination);
      if (d == null) {
        d = destinationFactory.create(vertx, destination);
        if (d != null) {
          // We use the local map as a set, the value is irrelevant.
          destinations.put(d, "");
        }
      }
      return d;
    }
  }

  @Override
  public synchronized StompServerHandler destinationFactory(DestinationFactory factory) {
    this.factory = factory;
    return this;
  }

  /**
   * Configures the STOMP server to act as a bridge with the Vert.x event bus.
   *
   * @param options the configuration options
   * @return the current {@link StompServerHandler}.
   * @see Vertx#eventBus()
   */
  @Override
  public synchronized StompServerHandler bridge(BridgeOptions options) {
    destinations.put(Destination.bridge(vertx, options), "");
    return this;
  }

  @Override
  public StompServerHandler onAck(StompServerConnection connection, Frame subscription, List<Frame> messages) {
    Handler<Acknowledgement> handler;
    synchronized (this) {
      handler = onAckHandler;
    }
    if (handler != null) {
      handler.handle(new AcknowledgementImpl(subscription, messages));
    }
    return this;
  }

  @Override
  public StompServerHandler onNack(StompServerConnection connection, Frame subscribe, List<Frame> messages) {
    Handler<Acknowledgement> handler;
    synchronized (this) {
      handler = onNackHandler;
    }
    if (handler != null) {
      handler.handle(new AcknowledgementImpl(subscribe, messages));
    }
    return this;
  }

  @Override
  public synchronized StompServerHandler onAckHandler(Handler<Acknowledgement> handler) {
    this.onAckHandler = handler;
    return this;
  }

  @Override
  public synchronized StompServerHandler onNackHandler(Handler<Acknowledgement> handler) {
    this.onNackHandler = handler;
    return this;
  }

  /**
   * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   * frame containing {@code EOL} (specification). However, you can customize this and send another frame. However,
   * be aware that this may requires a custom client.
   * <p/>
   * The handler will only be called if the connection supports heartbeats.
   *
   * @param handler the action to execute when a `PING` needs to be sent.
   * @return the current {@link StompServerHandler}
   */
  @Override
  public synchronized StompServerHandler pingHandler(Handler<StompServerConnection> handler) {
    this.pingHandler = handler;
    return this;
  }

}
