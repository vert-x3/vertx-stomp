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
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetClient;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * Default implementation of {@link StompClient}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompClientImpl implements StompClient {

  private static final Logger log = LoggerFactory.getLogger(StompClientImpl.class);

  private final Vertx vertx;
  private final StompClientOptions options;
  private NetClient client;
  private Handler<Frame> receivedFrameHandler;
  private Handler<Frame> writingFrameHandler;
  private Handler<Frame> errorFrameHandler;
  private Handler<Throwable> exceptionHandler;


  public StompClientImpl(Vertx vertx, StompClientOptions options) {
    this.vertx = vertx;
    this.options = options;
  }

  @Override
  public StompClient connect(int port, String host, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    return connect(port, host, vertx.createNetClient(options), resultHandler);
  }

  @Override
  public StompClient connect(Handler<AsyncResult<StompClientConnection>> resultHandler) {
    return connect(options.getPort(), options.getHost(), vertx.createNetClient(options), resultHandler);
  }

  @Override
  public Future<StompClientConnection> connect() {
    Promise<StompClientConnection> promise = Promise.promise();
    connect(promise);
    return promise.future();
  }

  /**
   * Configures a received handler that get notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can be modified by the handler.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Override
  public synchronized StompClient receivedFrameHandler(Handler<Frame> handler) {
    receivedFrameHandler = handler;
    return this;
  }

  /**
   * Configures a writing handler that gets notified when a STOMP frame is written on the wire.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   * <p>
   * When a connection is created, the handler is used as
   * {@link StompClientConnection#writingFrameHandler(Handler)}.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Override
  public synchronized StompClient writingFrameHandler(Handler<Frame> handler) {
    writingFrameHandler = handler;
    return this;
  }

  /**
   * A general error frame handler. It can be used to catch {@code ERROR} frame emitted during the connection process
   * (wrong authentication). This error handler will be pass to all {@link StompClientConnection} created from this
   * client. Obviously, the client can override it when the connection is established.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Override
  public synchronized StompClient errorFrameHandler(Handler<Frame> handler) {
    errorFrameHandler = handler;
    return this;
  }

  /**
   * A general exception handler called when the TCP connection failed.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Override
  public synchronized StompClient exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public StompClient connect(NetClient netClient, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    return connect(options.getPort(), options.getHost(), netClient, resultHandler);
  }

  @Override
  public Future<StompClientConnection> connect(NetClient net) {
    Promise<StompClientConnection> promise = Promise.promise();
    connect(net, promise);
    return promise.future();
  }

  @Override
  public Future<StompClientConnection> connect(int port, String host) {
    Promise<StompClientConnection> promise = Promise.promise();
    connect(port, host, promise);
    return promise.future();
  }

  @Override
  public synchronized void close() {
    if (client != null) {
      client.close();
      client = null;
    }
  }


  @Override
  public StompClientOptions options() {
    return options;
  }

  @Override
  public Vertx vertx() {
    return vertx;
  }

  @Override
  public synchronized boolean isClosed() {
    return client == null;
  }

  @Override
  public synchronized StompClient connect(int port, String host, NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    if (client != null) {
      client.close();
      client = null;
    }

    Handler<Frame> r = receivedFrameHandler;
    Handler<Frame> w = writingFrameHandler;
    Handler<Throwable> err = exceptionHandler;
    net.connect(port, host).onComplete(ar -> {
      synchronized (StompClientImpl.this) {
        client = ar.failed() ? null : net;
        if (client != null) {
          ar.result().exceptionHandler(t -> {
            if (resultHandler != null) {
              resultHandler.handle(Future.failedFuture(t));
            } else {
              log.error("Unable to connect to the server", t);
            }
          });
        }
      }

      if (ar.failed()) {
        if (resultHandler != null) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          log.error("Unable to connect to the server", ar.cause());
        }
      } else {
        // Create the connection, the connection attach a handler on the socket.
        StompClientConnection stompClientConnection = new StompClientConnectionImpl(vertx, ar.result(), this, resultHandler)
          .receivedFrameHandler(r)
          .writingFrameHandler(w)
          .exceptionHandler(err)
          .errorHandler(errorFrameHandler);
        // Socket connected - send "CONNECT" Frame
        Frame frame = getConnectFrame(host);

        // If we don't get the CONNECTED timeout in time, fail the connection.
        vertx.setTimer(options.getConnectTimeout(), l -> {
          if (!stompClientConnection.isConnected()) {
            resultHandler.handle(Future.failedFuture("CONNECTED frame not receive in time"));
            stompClientConnection.close();
          }
        });

        if (w != null) {
          w.handle(frame);
        }

        ar.result().write(frame.toBuffer(options.isTrailingLine()));
      }
    });
    return this;
  }

  @Override
  public Future<StompClientConnection> connect(int port, String host, NetClient net) {
    Promise<StompClientConnection> promise = Promise.promise();
    connect(port, host, net, promise);
    return promise.future();
  }

  private Frame getConnectFrame(String host) {
    Headers headers = Headers.create();
    String accepted = getAcceptedVersions();
    if (accepted != null) {
      headers.put(Frame.ACCEPT_VERSION, accepted);
    }
    if (!options.isBypassHostHeader()) {
      headers.put(Frame.HOST, host);
    }
    if (options.getVirtualHost() != null) {
      headers.put(Frame.HOST, options.getVirtualHost());
    }
    if (options.getLogin() != null) {
      headers.put(Frame.LOGIN, options.getLogin());
    }
    if (options.getPasscode() != null) {
      headers.put(Frame.PASSCODE, options.getPasscode());
    }
    headers.put(Frame.HEARTBEAT, Frame.Heartbeat.create(options.getHeartbeat()).toString());

    Command cmd = options.isUseStompFrame() ? Command.STOMP : Command.CONNECT;
    return new Frame(cmd, headers, null);
  }

  private String getAcceptedVersions() {
    if (options.getAcceptedVersions() == null || options.getAcceptedVersions().isEmpty()) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    options.getAcceptedVersions().forEach(
      version -> builder.append(builder.length() == 0 ? version : FrameParser.COMMA + version)
    );
    return builder.toString();
  }


}
