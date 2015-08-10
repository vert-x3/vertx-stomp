package io.vertx.ext.stomp.integration;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientOptions;

/**
 * A verticle subscribing to a STOMP destination.
 */
public class StompConsumer extends AbstractVerticle {

  private StompClient client;

  @Override
  public void start() throws Exception {
    System.out.println("Starting client");
    client = Stomp.createStompClient(vertx, new StompClientOptions(config()));
    client.connect(ar -> {
      if (ar.failed()) {
        System.err.println("Cannot connect to STOMP server");
        ar.cause().printStackTrace();
        return;
      }
      ar.result().subscribe("/queue/event", frame -> System.out.println("Frame received : " + frame.getBodyAsString()));
    });
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    client.close();
  }
}
