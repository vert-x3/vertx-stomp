/*
 *  Copyright (c) 2011-2015 The original author or authors
 *
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

package io.vertx.ext.stomp.integration;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientConnection;
import io.vertx.ext.stomp.StompClientOptions;
import io.vertx.ext.stomp.impl.AsyncLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

/**
 * Checks that our clients can connect and interact with Cloud AMQP (RabbitMQ as a service).
 * We use a free instance for this test:
 * <p>
 * URL: moose.rmq.cloudamqp.com
 * Username and vhost: xvjvsrrc
 * Password: VbuL1atClKt7zVNQha0bnnScbNvGiqgb
 * Full AMQP url: amqp://xvjvsrrc:VbuL1atClKt7zVNQha0bnnScbNvGiqgb@moose.rmq.cloudamqp.com/xvjvsrrc
 * <p>
 * Do not abuse from this instance, it's pretty limited.
 * <p>
 * A `box` "queue" has been created on this instance (durable:true, auto-delete: false, no other options).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CloudAmqpIT {

  private Vertx vertx;
  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    clients.stream().forEach(StompClient::close);
    AsyncLock<Void> lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  /**
   * The test is the following:
   * 1. Create a client subscribing to the "box" destination
   * 2. Create another client sending messages to the "box" destination
   * 3. Ensure the the client 1 receives the message.
   */
  @Test
  public void testRegularConnection() {
    AtomicReference<StompClientConnection> receiver = new AtomicReference<>();
    AtomicReference<StompClientConnection> sender = new AtomicReference<>();

    AtomicReference<Frame> frame = new AtomicReference<>();

    // Step 1.
    StompClient client1 = StompClient.create(vertx, new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61613)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc"))
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            receiver.set(connection.result());
            connection.result().subscribe("box", frame::set);
          }
        });
    clients.add(client1);

    await().atMost(10, TimeUnit.SECONDS).until(() -> receiver.get() != null);

    // Step 2.
    StompClient client2 = StompClient.create(vertx, new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61613)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc"))
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            sender.set(connection.result());
            connection.result().send("box", Buffer.buffer("hello from vert.x"));
          }
        });
    clients.add(client2);
    await().atMost(10, TimeUnit.SECONDS).until(() -> sender.get() != null);

    // Step 3.
    await().atMost(10, TimeUnit.SECONDS).until(() -> frame.get() != null);
    assertThat(frame.get().getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frame.get().getHeaders())
        .contains(entry("content-length", "17"))
        .containsKeys("destination", "message-id", "subscription");
    assertThat(frame.get().getBodyAsString()).isEqualToIgnoringCase("hello from vert.x");
  }

  /**
   * The test is the following:
   * 1. Create a client subscribing to the "box" destination
   * 2. Create another client sending messages to the "box" destination
   * 3. Ensure the the client 1 receives the message.
   */
  @Test
  public void testSSLConnection() {
    AtomicReference<StompClientConnection> receiver = new AtomicReference<>();
    AtomicReference<StompClientConnection> sender = new AtomicReference<>();

    AtomicReference<Frame> frame = new AtomicReference<>();

    // Step 1.
    StompClient client1 = StompClient.create(vertx, new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61614)
        .setSsl(true)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc"))
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            receiver.set(connection.result());
            connection.result().subscribe("box", frame::set);
          }
        });
    clients.add(client1);

    await().atMost(10, TimeUnit.SECONDS).until(() -> receiver.get() != null);

    // Step 2.
    StompClient client2 = StompClient.create(vertx, new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61614)
        .setSsl(true)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc"))
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            sender.set(connection.result());
            connection.result().send("box", Buffer.buffer("hello from vert.x"));
          }
        });
    clients.add(client2);
    await().atMost(10, TimeUnit.SECONDS).until(() -> sender.get() != null);

    // Step 3.
    await().atMost(10, TimeUnit.SECONDS).until(() -> frame.get() != null);
    assertThat(frame.get().getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frame.get().getHeaders())
        .contains(entry("content-length", "17"))
        .containsKeys("destination", "message-id", "subscription");
    assertThat(frame.get().getBodyAsString()).isEqualToIgnoringCase("hello from vert.x");
  }
}
