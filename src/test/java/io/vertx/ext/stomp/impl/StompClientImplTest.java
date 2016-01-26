/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
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

package io.vertx.ext.stomp.impl;

import com.jayway.awaitility.Awaitility;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Test the STOMP client.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompClientImplTest {

  private Vertx vertx;
  private StompServer server;

  @Before
  public void setUp() {
    AsyncLock<StompServer> lock = new AsyncLock<>();
    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(lock.handler());

    lock.waitForSuccess();
  }

  @After
  public void tearDown() {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void testConnection() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    StompClient client = StompClient.create(vertx);
    client.connect(ar -> {
      if (ar.failed()) {
        reference.set(null);
      } else {
        reference.set(ar.result());
      }
      latch.countDown();
    });

    latch.await(1, TimeUnit.MINUTES);
    assertNotNull(reference.get());
    assertNotNull(reference.get().session());
    assertNotNull(reference.get().server());
    assertNotNull(reference.get().version());
  }


  @Test
  public void testConnectionWithStompFrame() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    StompClient client = StompClient.create(vertx, new StompClientOptions().setUseStompFrame(true));
    client.connect(ar -> {
      if (ar.failed()) {
        reference.set(null);
      } else {
        reference.set(ar.result());
      }
      latch.countDown();
    });

    latch.await(1, TimeUnit.MINUTES);
    assertNotNull(reference.get());
    assertNotNull(reference.get().session());
    assertNotNull(reference.get().server());
    assertNotNull(reference.get().version());
  }

  @Test
  public void testSendingMessages() {
    AtomicReference<Frame> ref = new AtomicReference<>();
    StompClient client = StompClient.create(vertx);
    client.connect(ar -> {
      if (ar.failed()) {
        return;
      }
      ar.result().send("/hello", Buffer.buffer("this is my content"), ref::set);
    });

    Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(ref, Matchers.notNullValue(Frame.class));
    assertThat(ref.get().getDestination()).isEqualTo("/hello");
  }

  @Test
  public void testConnectionAndDisconnect() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Frame> reference = new AtomicReference<>();
    StompClient client = StompClient.create(vertx, new StompClientOptions().setUseStompFrame(true));
    client.connect(ar -> {
      if (ar.failed()) {
        reference.set(null);
        latch.countDown();
      } else {
        ar.result().disconnect(
            frame -> {
              reference.set(frame);
              latch.countDown();
            });
      }
    });
    latch.await(1, TimeUnit.MINUTES);
    assertNotNull(reference.get());
  }

  @Test
  public void testConnectionAndDisconnectWithCustomFrame() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Frame> reference = new AtomicReference<>();
    StompClient client = StompClient.create(vertx, new StompClientOptions().setUseStompFrame(true));
    client.connect(ar -> {
      if (ar.failed()) {
        reference.set(null);
        latch.countDown();
      } else {
        ar.result().disconnect(new Frame(Frame.Command.DISCONNECT, Headers.create("message", "bye bye"), null),
            frame -> {
              reference.set(frame);
              latch.countDown();
            });
      }
    });
    latch.await(1, TimeUnit.MINUTES);
    assertNotNull(reference.get());
    assertThat(reference.get().getHeader("message")).contains("bye bye");
  }

  @Test
  public void testClientHeartbeatWhenNoServerActivity() {
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    AsyncLock<Void> closeLock = new AsyncLock<>();
    server.close(closeLock.handler());
    closeLock.waitForSuccess();

    AsyncLock<StompServer> lock = new AsyncLock<>();
    server = StompServer.create(vertx,
        new StompServerOptions().setHeartbeat(new JsonObject().put("x", 100).put("y", 100)))
        // Disable ping frame:
        .handler(StompServerHandler.create(vertx).pingHandler(v -> {
        }))
        .listen(lock.handler());
    lock.waitForSuccess();

    StompClient client = StompClient.create(vertx, new StompClientOptions().setHeartbeat(new JsonObject().put
        ("x", 100).put("y", 100)));
    client.connect(ar -> reference.set(ar.result()));

    // Wait until inactivity is detected.
    Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS).until(
        () -> reference.get().session() == null
    );
  }

  @Test
  public void testClientHeartbeatWithServerActivity() throws InterruptedException {
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    AsyncLock lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock();
    server = StompServer.create(vertx,
        new StompServerOptions().setHeartbeat(new JsonObject().put("x", 100).put("y", 100)))
        .handler(StompServerHandler.create(vertx))
        .listen(lock.handler());
    lock.waitForSuccess();

    StompClient client = StompClient.create(vertx, new StompClientOptions().setHeartbeat(new JsonObject()
        .put("x", 100).put("y", 100)));
    client.connect(ar -> reference.set(ar.result()));

    Thread.sleep(1000);
    assertThat(reference.get().server()).isNotNull();
  }

  @Test
  public void testServerHeartbeatWhenNoClientActivity() {
    AsyncLock<Void> closeLock = new AsyncLock<>();
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    server.close(closeLock.handler());
    closeLock.waitForSuccess();

    AsyncLock<StompServer> lock = new AsyncLock<>();
    server = StompServer.create(vertx,
        new StompServerOptions().setHeartbeat(new JsonObject().put("x", 100).put("y", 100)))
        .handler(StompServerHandler.create(vertx))
        .listen(lock.handler());
    lock.waitForSuccess();

    StompClient client = StompClient.create(vertx, new StompClientOptions().setHeartbeat(new JsonObject()
        .put("x", 100).put("y", 100)));
    client.connect(ar -> {
      reference.set(ar.result());
      // Disable ping frame:
      ar.result().pingHandler(connection -> {
      });
    });

    // Wait until inactivity is detected.
    Awaitility.await().atMost(1, TimeUnit.SECONDS).until(
        () -> reference.get().session() == null
    );
  }

  @Test
  public void testConnectionDroppedHandler() throws InterruptedException {
    AtomicBoolean flag = new AtomicBoolean(true);
    AtomicBoolean dropped = new AtomicBoolean(false);
    AsyncLock lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock();
    server = StompServer.create(vertx,
        new StompServerOptions().setHeartbeat(new JsonObject().put("x", 100).put("y", 100)))
        .handler(StompServerHandler.create(vertx).pingHandler(connection -> {
          if (flag.get()) {
            connection.ping();
          }
          // When the flag is set to false, the ping are not sent anymore. We use this mechanism to mimic a
          // server not sending ping anymore.
        }))
        .listen(lock.handler());
    lock.waitForSuccess();

    StompClient client = StompClient.create(vertx, new StompClientOptions().setHeartbeat(new JsonObject()
        .put("x", 100).put("y", 100)));
    client.connect(ar -> {
      ar.result().connectionDroppedHandler(v -> {
        dropped.set(true);
      });
      flag.set(false); // Disable the ping.
    });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(dropped::get);
  }

}