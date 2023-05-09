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
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetServer;
import io.vertx.ext.stomp.*;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default implementation of the {@link StompServer}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerImpl implements StompServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(StompServerImpl.class);

  private final Vertx vertx;
  private final StompServerOptions options;
  private final NetServer server;

  private StompServerHandler handler;
  private volatile boolean listening;

  private Handler<ServerFrame> writingFrameHandler;

  /**
   * Creates a new instance of {@link StompServerImpl}.
   * @param vertx the vert.x instance
   * @param net the net server, may be {@code null}
   * @param options the options
   */
  public StompServerImpl(Vertx vertx, NetServer net, StompServerOptions options) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(options);
    this.options = options;
    this.vertx = vertx;
    if (net == null) {
      server = vertx.createNetServer(options);
    } else {
      server = net;
    }
  }

  @Override
  public synchronized StompServer handler(StompServerHandler handler) {
    Objects.requireNonNull(handler);
    this.handler = handler;
    return this;
  }

  @Override
  public Future<StompServer> listen() {
    Promise<StompServer> promise = Promise.promise();
    listen(promise);
    return promise.future();
  }

  public StompServer listen(Handler<AsyncResult<StompServer>> handler) {
    return listen(options.getPort(), options.getHost(), handler);
  }

  @Override
  public Future<StompServer> listen(int port) {
    return listen(port, StompServerOptions.DEFAULT_STOMP_HOST);
  }

  @Override
  public Future<StompServer> listen(int port, String host) {
    Promise<StompServer> promise = Promise.promise();
    listen(port, host, promise);
    return promise.future();
  }

  public StompServer listen(int port, String host, Handler<AsyncResult<StompServer>> handler) {
    if (port == -1) {
      handler.handle(Future.failedFuture("TCP server disabled. The port is set to '-1'."));
      return this;
    }
    StompServerHandler stomp;
    synchronized (this) {
      stomp = this.handler;
    }

    Objects.requireNonNull(stomp, "Cannot open STOMP server - no StompServerConnectionHandler attached to the " +
        "server.");
    server
        .connectHandler(socket -> {
          AtomicBoolean connected = new AtomicBoolean();
          AtomicBoolean firstFrame = new AtomicBoolean();
          StompServerConnection connection = new StompServerTCPConnectionImpl(socket, this, frame -> {
            if (frame.frame().getCommand() == Command.CONNECTED) {
              connected.set(true);
            }
            Handler<ServerFrame> h = writingFrameHandler;
            if (h != null) {
              h.handle(frame);
            }
          });
          FrameParser parser = new FrameParser(options);
          socket.exceptionHandler((exception) -> {
            LOGGER.error("The STOMP server caught a TCP socket error - closing connection", exception);
            connection.close();
          });
          socket.endHandler(v -> connection.close());
          parser
              .errorHandler((exception) -> {
                    connection.write(
                        Frames.createInvalidFrameErrorFrame(exception));
                    connection.close();
                  }
              )
              .handler(frame -> {
                if (frame.getCommand() == Command.CONNECT || frame.getCommand() == Command.STOMP) {
                  if (firstFrame.compareAndSet(false, true)) {
                    stomp.handle(new ServerFrameImpl(frame, connection));
                  } else {
                    connection.write(Frames.createErrorFrame("Already connected", Collections.emptyMap(), ""));
                    connection.close();
                  }
                } else if (connected.get()) {
                  stomp.handle(new ServerFrameImpl(frame, connection));
                } else {
                  connection.write(Frames.createErrorFrame("Not connected", Collections.emptyMap(), ""));
                  connection.close();
                }
              });
          socket.handler(parser);
        })
        .listen(port, host).onComplete(ar -> {
          if (ar.failed()) {
            if (handler != null) {
              vertx.runOnContext(v -> handler.handle(Future.failedFuture(ar.cause())));
            } else {
              LOGGER.error(ar.cause());
            }
          } else {
            listening = true;
            LOGGER.info("STOMP server listening on " + ar.result().actualPort());
            if (handler != null) {
              vertx.runOnContext(v -> handler.handle(Future.succeededFuture(this)));
            }
          }
        });
    return this;
  }

  @Override
  public Future<Void> close() {
    Promise<Void> promise = Promise.promise();
    close(promise);
    return promise.future();
  }

  @Override
  public boolean isListening() {
    return listening;
  }

  @Override
  public int actualPort() {
    return server.actualPort();
  }

  @Override
  public StompServerOptions options() {
    return options;
  }

  @Override
  public Vertx vertx() {
    return vertx;
  }

  @Override
  public synchronized StompServerHandler stompHandler() {
    return handler;
  }

  public void close(Handler<AsyncResult<Void>> done) {
    if (!listening) {
      if (done != null) {
        vertx.runOnContext((v) -> done.handle(Future.succeededFuture()));
      }
      return;
    }

    Handler<AsyncResult<Void>> listener = (v) -> {
      if (v.succeeded()) {
        LOGGER.info("STOMP Server stopped");
      } else {
        LOGGER.info("STOMP Server failed to stop", v.cause());
      }

      listening = false;
      if (done != null) {
        done.handle(v);
      }
    };

    server.close().onComplete(listener);
  }

  @Override
  public Handler<ServerWebSocket> webSocketHandler() {
    if (!options.isWebsocketBridge()) {
      return null;
    }

    StompServerHandler stomp;
    synchronized (this) {
      stomp = this.handler;
    }

    return socket -> {
      if (!socket.path().equals(options.getWebsocketPath())) {
        LOGGER.error("Receiving a web socket connection on an invalid path (" + socket.path() + "), the path is " +
            "configured to " + options.getWebsocketPath() + ". Rejecting connection");
        socket.reject();
        return;
      }
      AtomicBoolean connected = new AtomicBoolean();
      AtomicBoolean firstFrame = new AtomicBoolean();
      StompServerConnection connection = new StompServerWebSocketConnectionImpl(socket, this, frame -> {
        if (frame.frame().getCommand() == Command.CONNECTED  || frame.frame().getCommand() == Command.STOMP) {
          connected.set(true);
        }
        Handler<ServerFrame> h = writingFrameHandler;
        if (h != null) {
          h.handle(frame);
        }
      });
      FrameParser parser = new FrameParser(options);
      socket.exceptionHandler((exception) -> {
        LOGGER.error("The STOMP server caught a WebSocket error - closing connection", exception);
        connection.close();
      });
      socket.endHandler(v -> connection.close());
      parser
          .errorHandler((exception) -> {
                connection.write(
                    Frames.createInvalidFrameErrorFrame(exception));
                connection.close();
              }
          )
          .handler(frame -> {
            if (frame.getCommand() == Command.CONNECT) {
              if (firstFrame.compareAndSet(false, true)) {
                stomp.handle(new ServerFrameImpl(frame, connection));
              } else {
                connection.write(Frames.createErrorFrame("Already connected", Collections.emptyMap(), ""));
                connection.close();
              }
            } else if (connected.get()) {
              stomp.handle(new ServerFrameImpl(frame, connection));
            } else {
              connection.write(Frames.createErrorFrame("Not connected", Collections.emptyMap(), ""));
              connection.close();
            }
          });
      socket.handler(parser);
    };
  }

  @Override
  public StompServer writingFrameHandler(Handler<ServerFrame> handler) {
    synchronized (this) {
      this.writingFrameHandler = handler;
    }
    return this;
  }


}
