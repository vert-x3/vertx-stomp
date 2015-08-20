package io.vertx.ext.stomp.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;

/**
 * A verticle starting a STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerVerticle extends AbstractVerticle {


  private StompServer server;

  @Override
  public void start(Future<Void> future) throws Exception {
    server = Stomp.createStompServer(vertx).handler(StompServerHandler.create(vertx).destinationFactory(
        (vertx, name) -> {
          if (config().getBoolean("useQueue", false)) {
            return Destination.queue(vertx, name);
          } else {
            return Destination.topic(vertx, name);
          }
        }))
        .listen(ar -> {
          if (ar.failed()) {
            future.fail(ar.cause());
          } else {
            future.complete(null);
          }
        });
  }
}
