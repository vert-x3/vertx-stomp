package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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
   * Connects the STOMP server to the given port.
   *
   * @param port the port
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen(int port);

  /**
   * Connects the STOMP server to the given port / interface.
   *
   * @param port the port
   * @param host the interface
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen(int port, String host);

  /**
   * Connects the STOMP server to the port / host configured in the server options.
   *
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen();


  /**
   * Connects the STOMP server default port (61613) and network interface ({@code 0.0.0.0}). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   *
   * @param handler the handler to call with the result
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen(Handler<AsyncResult<StompServer>> handler);

  /**
   * Connects the STOMP server to the given port. This method use the default host ({@code 0.0.0.0}). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   *
   * @param port    the port
   * @param handler the handler to call with the result
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen(int port, Handler<AsyncResult<StompServer>> handler);

  /**
   * Connects the STOMP server to the given port / interface. Once the socket it bounds calls the given handler with
   * the result. The result may be a failure if the socket is already used.
   *
   * @param port    the port
   * @param host    the host / interface
   * @param handler the handler to call with the result
   * @return the current {@link StompServer}
   */
  @Fluent
  StompServer listen(int port, String host, Handler<AsyncResult<StompServer>> handler);

  /**
   * Closes the server.
   *
   * @param completionHandler handler called once the server has been stopped
   */
  void close(Handler<AsyncResult<Void>> completionHandler);

  /**
   * Closes the server.
   */
  void close();

  /**
   * Checks whether or not the server is listening.
   *
   * @return {@code true} if the server is listening, {@code false} otherwise
   */
  boolean isListening();

  /**
   * Gets the port on which the server is listening. 0 is not listening.
   *
   * @return the port
   */
  int getPort();

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
}
