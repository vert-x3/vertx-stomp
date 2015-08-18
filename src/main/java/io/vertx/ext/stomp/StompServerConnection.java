package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
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
}
