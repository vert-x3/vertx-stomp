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
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test STOMP server / client connection protocol.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(io.vertx.ext.unit.junit.VertxUnitRunner.class)
public class ServerConnectionTest {

  private Vertx vertx;
  private StompServer server;

  private NetClient client;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    AsyncLock<StompServer> lock = new AsyncLock<>();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(lock.handler());
    lock.waitForSuccess();
  }

  @After
  public void tearDown(TestContext context) {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();

    if (client != null) {
      client.close();
      client = null;
    }
  }

  @Test
  public void testConnection(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        // Optional headers:
        context.assertTrue(buffer.toString().contains("session:"));
        context.assertTrue(buffer.toString().contains("server:"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testConnectionWithSeveralVersions(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.1"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.0,1.1\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testConnectionWithoutVersionHeader(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.0"));
        async.complete();
      });
      socket.write("CONNECT\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testConnectionWithInvalidVersions(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        context.assertTrue(buffer.toString().contains("Supported protocol versions are 1.2"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:0.0\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testConnectionWithInvalidVersionLists(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        context.assertTrue(buffer.toString().contains("Supported protocol versions are 1.2"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:0.0,3.2\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testConnectionWithStompFrame(TestContext context) {
    Async async = context.async();
    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        // Optional headers:
        context.assertTrue(buffer.toString().contains("session:"));
        context.assertTrue(buffer.toString().contains("server:"));
        async.complete();
      });
      socket.write("STOMP\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testAConnectionReusingSubscriptionId(TestContext context) {
    List<Buffer> frames = new ArrayList<>();
    AtomicReference<NetSocket> reference = new AtomicReference<>();

    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      reference.set(socket);
      socket.handler(buffer -> {
        if (buffer.toString().contains("CONNECTED")) {
          // Send first subscription
          socket.write("SUBSCRIBE\n" + "destination:/queue\n" + "id:0\n\n" + FrameParser.NULL);
        } else {
          frames.add(buffer);
        }
      });
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() >= 1);

    // Send a second subscribe using the same ID
    reference.get().write("SUBSCRIBE\n" + "destination:/queue2\n" + "id:0\n\n" + FrameParser.NULL);

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 1);

    assertThat(frames.get(0).toString()).startsWith("ERROR");
  }

  @Test
  public void testInvalidUnsubscribe(TestContext context) {
    List<Buffer> frames = new ArrayList<>();
    AtomicReference<NetSocket> reference = new AtomicReference<>();

    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      reference.set(socket);
      socket.handler(buffer -> {
        if (buffer.toString().contains("CONNECTED")) {
          socket.write("UNSUBSCRIBE\n" + "id:0\n\n" + FrameParser.NULL);
        } else {
          frames.add(buffer);
        }
      });
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 1);

    assertThat(frames.get(0).toString()).startsWith("ERROR");
  }

  @Test
  public void testUnsubscribeWithoutId(TestContext context) {
    List<Buffer> frames = new ArrayList<>();
    AtomicReference<NetSocket> reference = new AtomicReference<>();

    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        System.err.println("Connection failed");
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      reference.set(socket);
      socket.handler(buffer -> {
        if (buffer.toString().contains("CONNECTED")) {
          socket.write("UNSUBSCRIBE\n" + "\n" + FrameParser.NULL);
        } else {
          frames.add(buffer);
        }
      });
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });

    Awaitility.await().atMost(10, TimeUnit.MINUTES).until(() -> frames.size() >= 1);

    assertThat(frames.get(0).toString()).startsWith("ERROR");
  }

  @Test
  public void testMalformedFrame(TestContext context) {
    List<Buffer> frames = new ArrayList<>();

    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(frames::add);
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + "illegal body" + FrameParser.NULL);
    });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 1);

    assertThat(frames.get(0).toString()).startsWith("ERROR");
  }

  @Test
  public void testNumberOfHeadersExceeded(TestContext context) {
    server.options().setMaxHeaders(2);

    List<Buffer> frames = new ArrayList<>();
    AtomicReference<NetSocket> reference = new AtomicReference<>();

    client = vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      reference.set(socket);
      socket.handler(buffer -> {
        if (buffer.toString().contains("CONNECTED")) {
          socket.write("SEND\n"
              + "header1:value1\n"
              + "header2:value2\n"
              + "destination:foo\n"
              + "\n" + FrameParser.NULL);
        } else {
          frames.add(buffer);
        }
      });
      socket.write("CONNECT\n" + "accept-version:1.2\n" + "\n" + FrameParser.NULL);
    });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 1);

    assertThat(frames.get(0).toString()).startsWith("ERROR")
        .containsIgnoringCase("number of headers exceeded");
  }


}
