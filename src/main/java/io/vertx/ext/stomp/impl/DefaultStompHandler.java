package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.stomp.*;

import java.util.ArrayList;
import java.util.List;

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

  private static final Logger log = LoggerFactory.getLogger(DefaultStompHandler.class);
  private final Vertx vertx;

  private Handler<ServerFrame> connectHandler = new DefaultConnectHandler();

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

  private Handler<ServerFrame> disconnectHandler = ((sf) -> {
    StompServerConnection connection = sf.connection();
    Frames.handleReceipt(sf.frame(), connection);
    connection.close();
    onClose(connection);
  });

  private AuthProvider authProvider;

  private Handler<StompServerConnection> pingHandler = StompServerConnection::ping;

  private Handler<Acknowledgement> onAckHandler = (acknowledgement) -> log.info("Acknowledge messages - " +
      acknowledgement.frames());
  private Handler<Acknowledgement> onNackHandler = (acknowledgement) ->
      log.warn("Messages not acknowledge - " + acknowledgement.frames());

  private final LocalMap<String, Destination> destinations;

  private volatile long lastClientActivity;
  private volatile long pinger;
  private volatile long ponger;

  public DefaultStompHandler(Vertx vertx) {
    this.vertx = vertx;
    this.destinations = vertx.sharedData().getLocalMap("stomp.destinations");
  }

  public synchronized void onClose(StompServerConnection connection) {
    // Default behavior.
    if (pinger != 0) {
      vertx.cancelTimer(pinger);
      pinger = 0;
    }
    if (ponger != 0) {
      vertx.cancelTimer(ponger);
      ponger = 0;
    }

    getDestinations().stream().forEach((d) -> d.unsubscribeConnection(connection));
    Transactions.instance().unregisterTransactionsFromConnection(connection);

    if (closeHandler != null) {
      closeHandler.handle(connection);
    }
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

    lastClientActivity = System.currentTimeMillis();
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
    synchronized (this) {
      handler = connectHandler;
    }

    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, connection));
    }
    // Compute heartbeat, and register pinger and ponger
    long ping = Frame.Heartbeat.computePingPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(connection.server().options().getHeartbeat()));
    long pong = Frame.Heartbeat.computePongPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(connection.server().options().getHeartbeat()));
    if (ping > 0) {
      pinger = connection.server().vertx().setPeriodic(ping, (l) -> pingHandler.handle(connection));
    }
    if (pong > 0) {
      ponger = connection.server().vertx().setPeriodic(pong, l -> {
        long delta = System.currentTimeMillis() - lastClientActivity;
        if (delta > pong * 2) {
          log.warn("Disconnecting client " + connection + " - no client activity in the last " + delta + " ms");
          connection.close();
          onClose(connection);
          vertx.cancelTimer(ponger);
        }
      });
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
  public synchronized StompServerHandler authProvider(AuthProvider handler) {
    this.authProvider = handler;
    return this;
  }

  @Override
  public StompServerHandler onAuthenticationRequest(StompServer server,
                                                    String login, String passcode,
                                                    Handler<AsyncResult<Boolean>> handler) {
    final AuthProvider auth;
    synchronized (this) {
      // Stack contention.
      auth = authProvider;
    }

    if (!server.options().isSecured()) {
      if (auth != null) {
        log.warn("Authentication handler set while the server is not secured");
      }
      vertx.runOnContext(v -> handler.handle(Future.succeededFuture(true)));
      return this;
    }

    if (server.options().isSecured() && auth == null) {
      log.error("Cannot authenticate connection - no authentication provider");
      vertx.runOnContext(v -> handler.handle(Future.succeededFuture(false)));
      return this;
    }

    vertx.runOnContext(v ->
        auth.authenticate(new JsonObject().put("username", login).put("password", passcode), ar -> {
          if (ar.succeeded()) {
            vertx.runOnContext(v2 -> handler.handle(Future.succeededFuture(true)));
          } else {
            vertx.runOnContext(v2 -> handler.handle(Future.succeededFuture(false)));
          }
        }));
    return this;
  }

  @Override
  public List<Destination> getDestinations() {
    return new ArrayList<>(destinations.values());
  }

  /**
   * Gets the destination with the given name..
   *
   * @param destination the destination
   * @return the {@link Destination}, {@code null} if not found.
   */
  public Destination getDestination(String destination) {
    return destinations.get(destination);
  }

  public Destination getOrCreateDestination(String destination) {
    synchronized (vertx) {
      Destination d = destinations.get(destination);
      if (d == null) {
        // TODO Manage different types of destination.
        d = Destination.topic(vertx, destination);
        destinations.put(destination, d);
      }
      return d;
    }
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
