package io.vertx.ext.stomp.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientConnection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A STOMP client receiving messages.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReceiverStompClient extends AbstractVerticle {


  public static final List<Frame> FRAMES = new CopyOnWriteArrayList<>();

  @Override
  public void start(Future<Void> future) throws Exception {
    StompClient.create(vertx).connect(ar -> {
      if (ar.failed()) {
        future.fail(ar.cause());
      }
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", FRAMES::add, frame -> {
        future.complete();
      });
    });
  }
}
