package io.vertx.ext.stomp.integration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientOptions;

/**
 * A verticle sending messages to a STOMP destination.
 */
public class StompPublisher extends AbstractVerticle {

  private StompClient client;

  @Override
  public void start() throws Exception {
    System.out.println("Starting publisher");
    client = Stomp.createStompClient(vertx, new StompClientOptions(config()));
    client.connect(ar -> {
      if (ar.failed()) {
        System.err.println("Cannot connect to STOMP server");
        ar.cause().printStackTrace();
        return;
      }

      vertx.setPeriodic(5000, l -> ar.result().send("/queue/event", Buffer.buffer("Hello"), frame -> {
        System.out.println("Receipt received");
      }));
    });
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    client.close();
  }
}