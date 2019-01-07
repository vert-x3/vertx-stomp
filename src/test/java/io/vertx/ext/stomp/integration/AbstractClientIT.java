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
import org.assertj.core.api.Assertions;
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
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public abstract class AbstractClientIT {


  protected Vertx vertx;
  protected List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    clients.forEach(StompClient::close);
    AsyncLock<Void> lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  public abstract StompClientOptions getOptions();

  public abstract StompClientOptions getOptionsWithSSL();

  public String getDestination() {
    return "box";
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
    StompClient client1 = StompClient.create(vertx, getOptions())
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            receiver.set(connection.result());
            connection.result().subscribe(getDestination(), frame::set);
          }
        });
    clients.add(client1);

    await().atMost(10, TimeUnit.SECONDS).until(() -> receiver.get() != null);

    // Step 2.
    StompClient client2 = StompClient.create(vertx, getOptions())
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            sender.set(connection.result());
            connection.result().send(getDestination(), Buffer.buffer("hello from vert.x"));
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
    Assertions.assertThat(frame.get().getBodyAsString()).isEqualToIgnoringCase("hello from vert.x");
  }

  /**
   * Same test as the previous one, but using SSL.
   */
  @Test
  public void testSSLConnection() {
    if (getOptionsWithSSL() == null) {
      // SSL test disabled.
      return;
    }
    AtomicReference<StompClientConnection> receiver = new AtomicReference<>();
    AtomicReference<StompClientConnection> sender = new AtomicReference<>();

    AtomicReference<Frame> frame = new AtomicReference<>();

    // Step 1.
    StompClient client1 = StompClient.create(vertx, getOptionsWithSSL())
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            receiver.set(connection.result());
            connection.result().subscribe(getDestination(), frame::set);
          }
        });
    clients.add(client1);

    await().atMost(10, TimeUnit.SECONDS).until(() -> receiver.get() != null);

    // Step 2.
    StompClient client2 = StompClient.create(vertx, getOptionsWithSSL())
        .connect(connection -> {
          if (connection.failed()) {
            connection.cause().printStackTrace();
          } else {
            sender.set(connection.result());
            connection.result().send(getDestination(), Buffer.buffer("hello from vert.x"));
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
