package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/**
 * Interface used to create STOMP server and clients.
 *
 * @see StompClient
 * @see StompServer
 */
@VertxGen
public interface Stomp {

  String UTF_8 = "utf-8";

  static StompServer createStompServer(Vertx vertx) {
    return StompServer.create(vertx);
  }

  static StompServer createStompServer(Vertx vertx, StompServerOptions options) {
    return StompServer.create(vertx, options);
  }

  static StompServer createStompServer(Vertx vertx, NetServer netServer) {
    return StompServer.create(vertx, netServer, new StompServerOptions());
  }

  static StompServer createStompServer(Vertx vertx, NetServer netServer, StompServerOptions options) {
    return StompServer.create(vertx, netServer, options);
  }

  static StompClient createStompClient(Vertx vertx) {
    return StompClient.create(vertx);
  }

  static StompClient createStompClient(Vertx vertx, StompClientOptions options) {
    return StompClient.create(vertx, options);
  }

}
