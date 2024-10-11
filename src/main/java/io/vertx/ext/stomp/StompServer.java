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
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.ServerWebSocketHandshake;
import io.vertx.core.net.NetServer;
import io.vertx.ext.stomp.impl.StompServerImpl;

/**
 * Defines a STOMP server. STOMP servers delegates to a {@link StompServerHandler} that let customize the behavior of
 * the server. By default, it uses a handler compliant with the STOMP specification, but let you change anything.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompServer {

  /**
   * Creates a {@link StompServer} based on the default Stomp Server implementation.
   *
   * @param vertx   the vert.x instance to use
   * @param options the server options
   * @return the created {@link StompServer}
   */
  static StompServer create(Vertx vertx, StompServerOptions options) {
    return new StompServerImpl(vertx, null, options);
  }

  /**
   * Creates a {@link StompServer} based on the default Stomp Server implementation.
   *
   * @param vertx     the vert.x instance to use
   * @param netServer the Net server used by the STOMP server
   * @return the created {@link StompServer}
   */
  static StompServer create(Vertx vertx, NetServer netServer) {
    return new StompServerImpl(vertx, netServer, new StompServerOptions());
  }

  /**
   * Creates a {@link StompServer} based on the default Stomp Server implementation.
   *
   * @param vertx   the vert.x instance to use
   * @param net     the Net server used by the STOMP server
   * @param options the server options
   * @return the created {@link StompServer}
   */
  static StompServer create(Vertx vertx, NetServer net, StompServerOptions options) {
    return new StompServerImpl(vertx, net, options);
  }

  /**
   * Creates a {@link StompServer} based on the default Stomp Server implementation, and use the default options.
   *
   * @param vertx the vert.x instance to use
   * @return the created {@link StompServer}
   */
  static StompServer create(Vertx vertx) {
    return create(vertx, new StompServerOptions());
  }

  /**
   * Configures the {@link StompServerHandler}. You must calls this method before calling the {@link #listen()} method.
   *
   * @param handler the handler
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer handler(StompServerHandler handler);

  /**
   * Connects the STOMP server to the given port. This method use the default host ({@code 0.0.0.0}). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   *
   * @param port the port
   * @return a future resolved with the listen result
   */
  Future<StompServer> listen(int port);

  /**
   * Connects the STOMP server to the given port / interface. Once the socket it bounds calls the given handler with
   * the result. The result may be a failure if the socket is already used.
   *
   * @param port the port
   * @param host the interface
   * @return a future resolved with the listen result
   */
  Future<StompServer> listen(int port, String host);

  /**
   * Connects the STOMP server default port (61613) and network interface ({@code 0.0.0.0}). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   *
   * @return a future resolved with the listen result
   */
  Future<StompServer> listen();

  /**
   * Closes the server.
   */
  Future<Void> close();

  /**
   * Checks whether or not the server is listening.
   *
   * @return {@code true} if the server is listening, {@code false} otherwise
   */
  boolean isListening();

  /**
   * Gets the port on which the server is listening.
   * <p/>
   * This is useful if you bound the server specifying 0 as port number signifying an ephemeral port.
   *
   * @return the port
   * @see NetServer#actualPort()
   */
  int actualPort();

  /**
   * @return the server options
   */
  StompServerOptions options();

  /**
   * @return the instance of vert.x used by the server.
   */
  Vertx vertx();

  /**
   * @return the {@link StompServerHandler} used by this server.
   */
  StompServerHandler stompHandler();

  /**
   * Gets the {@link Handler} able to manage web socket connection handshakes. If the web socket bridge is disabled, it returns
   * {@code null}.
   *
   * @return the handler that can be passed to {@link io.vertx.core.http.HttpServer#webSocketHandshakeHandler(Handler)}.
   */
  Handler<ServerWebSocketHandshake> webSocketHandshakeHandler();

  /**
   * Gets the {@link Handler} able to manage web socket connections. If the web socket bridge is disabled, it returns
   * {@code null}.
   *
   * @return the handler that can be passed to {@link io.vertx.core.http.HttpServer#webSocketHandler(Handler)}.
   */
  Handler<ServerWebSocket> webSocketHandler();

  /**
   * Configures the handler that is invoked every time a frame is going to be written to the "wire". It lets you log
   * the frames, but also adapt the frame if needed.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer writingFrameHandler(Handler<ServerFrame> handler);


}
