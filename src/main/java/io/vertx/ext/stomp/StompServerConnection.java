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

import javax.net.ssl.SSLSession;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * Class representing a connection between a STOMP client a the server. It keeps a references on the client socket,
 * so let write to this socket.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompServerConnection {

  /**
   * Writes the given frame to the socket.
   *
   * @param frame the frame, must not be {@code null}.
   * @return the current {@link StompServerConnection}
   */
  @Fluent
  StompServerConnection write(Frame frame);

  /**
   * Writes the given buffer to the socket. This is a low level API that should be used carefully.
   *
   * @param buffer the buffer
   * @return the current {@link StompServerConnection}
   */
  @Fluent
  StompServerConnection write(Buffer buffer);

  /**
   * @return the STOMP server serving this connection.
   */
  StompServer server();

  /**
   * @return SSLSession associated with the underlying socket. Returns null if connection is
   *         not SSL.
   */
  @GenIgnore({"permitted-type"})
  SSLSession sslSession();

  /**
   * @return the STOMP server handler dealing with this connection
   */
  StompServerHandler handler();

  /**
   * @return the STOMP session id computed when the client has established the connection to the server
   */
  String session();

  /**
   * Closes the connection with the client.
   */
  void close();

  /**
   * Sends a `PING` frame to the client. A `PING` frame is a frame containing only {@code EOL}.
   */
  void ping();

  /**
   * Notifies the connection about server activity (the server has sent a frame). This method is used to handle the
   * heartbeat.
   */
  void onServerActivity();

  /**
   * Configures the heartbeat.
   * @param ping ping time
   * @param pong pong time
   * @param pingHandler the ping handler
   */
  void configureHeartbeat(long ping, long pong, Handler<StompServerConnection> pingHandler);
}
