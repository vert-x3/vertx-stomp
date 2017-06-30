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

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.ext.stomp.impl.StompClientImpl;

/**
 * Defines a STOMP client.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompClient {

  /**
   * Creates a {@link StompClient} using the default implementation.
   *
   * @param vertx the vert.x instance to use
   * @return the created {@link StompClient}
   */
  static StompClient create(Vertx vertx) {
    return create(vertx, new StompClientOptions());
  }

  /**
   * Creates a {@link StompClient} using the default implementation.
   *
   * @param vertx   the vert.x instance to use
   * @param options the options
   * @return the created {@link StompClient}
   */
  static StompClient create(Vertx vertx, StompClientOptions options) {
    return new StompClientImpl(vertx, options);
  }

  /**
   * Connects to the server.
   *
   * @param port          the server port
   * @param host          the server host
   * @param resultHandler handler called with the connection result
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient connect(int port, String host, Handler<AsyncResult<StompClientConnection>> resultHandler);

  /**
   * Connects to the server.
   *
   * @param net           the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient connect(NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler);

  /**
   * Connects to the server.
   *
   * @param port          the server port
   * @param host          the server host
   * @param net           the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient connect(int port, String host, NetClient net,
                      Handler<AsyncResult<StompClientConnection>> resultHandler);

  /**
   * Connects to the server using the host and port configured in the client's options.
   *
   * @param resultHandler handler called with the connection result. A failure will be sent to the handler if a TCP
   *                      level issue happen before the `CONNECTED` frame is received. Afterwards, the
   *                      {@link #exceptionHandler(Handler)} is called.
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient connect(Handler<AsyncResult<StompClientConnection>> resultHandler);

  /**
   * Configures a received handler that gets notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   * <p>
   * When a connection is created, the handler is used as
   * {@link StompClientConnection#receivedFrameHandler(Handler)}.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient receivedFrameHandler(Handler<Frame> handler);

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
  @Fluent
  StompClient writingFrameHandler(Handler<Frame> handler);

  /**
   * A general error frame handler. It can be used to catch {@code ERROR} frame emitted during the connection process
   * (wrong authentication). This error handler will be pass to all {@link StompClientConnection} created from this
   * client. Obviously, the client can override it when the connection is established.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient errorFrameHandler(Handler<Frame> handler);

  /**
   * Sets an exception handler notified for TCP-level errors.
   *
   * @param handler the handler
   * @return the current {@link StompClient}
   */
  @Fluent
  StompClient exceptionHandler(Handler<Throwable> handler);

  /**
   * Closes the client.
   */
  void close();

  /**
   * @return the client's options.
   */
  StompClientOptions options();

  /**
   * @return the vert.x instance used by the client.
   */
  Vertx vertx();

  /**
   * @return whether or not the client is connected to the server.
   */
  boolean isClosed();
}
