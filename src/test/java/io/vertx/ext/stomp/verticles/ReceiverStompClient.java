package io.vertx.ext.stomp.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompClientConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * A STOMP client receiving messages.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReceiverStompClient extends AbstractVerticle {


  public static final List<Frame> FRAMES = new ArrayList<>();

  @Override
  public void start() throws Exception {
    Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", FRAMES::add);
    });
  }
}
