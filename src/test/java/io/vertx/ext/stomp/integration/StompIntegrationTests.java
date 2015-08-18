package io.vertx.ext.stomp.integration;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Runs one of the support STOMP Broker, from 'src/test/resources/integration', configure the test, and check
 * messages are published and received.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompIntegrationTests {

  public static void main(String args[]) {
    String host = "192.168.59.103"; // Change to localhost on linux.
    int port = 61613;
    boolean useHostHeader = true;   // Set it to false for RabbitMQ

    JsonObject config = new JsonObject()
        .put("host", host)
        .put("port", port)
        .put("bypassHostHeader", !useHostHeader);

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(StompConsumer.class.getName(), new DeploymentOptions().setConfig(config).setInstances(2));
    vertx.deployVerticle(StompPublisher.class.getName(), new DeploymentOptions().setConfig(config));
  }


}
