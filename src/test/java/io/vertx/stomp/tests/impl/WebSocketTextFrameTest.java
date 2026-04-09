/*
 * Copyright (c) 2011-2026 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.stomp.tests.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test WebSocket TEXT frame mode for STOMP messages.
 * Verifies that when configured with WebSocketFrameType.TEXT, the server sends
 * STOMP frames as WebSocket text frames instead of binary frames.
 */
public class WebSocketTextFrameTest {

  private Vertx vertx;
  private StompServer server;
  private HttpServer http;
  private WebSocketConnectOptions options;
  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();

    // Configure server to use TEXT WebSocket frames
    server = StompServer.create(vertx, new StompServerOptions()
        .setWebsocketBridge(true)
        .setWebSocketFrameType(io.vertx.ext.stomp.WebSocketFrameType.TEXT))  // Use TEXT frames
      .handler(StompServerHandler.create(vertx));

    HttpServerOptions httpOptions = new HttpServerOptions()
      .setMaxWebSocketFrameSize(1024)
      .setMaxWebSocketMessageSize(2048)
      .setPort(8080);

    http = vertx.createHttpServer(httpOptions)
      .webSocketHandshakeHandler(server.webSocketHandshakeHandler())
      .webSocketHandler(server.webSocketHandler());

    options = new WebSocketConnectOptions()
      .setPort(8080)
      .setURI("/stomp")
      .addHeader("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp");
  }

  private void startServers() {
    if (server.options().getPort() != -1) {
      AsyncLock<StompServer> stompLock = new AsyncLock<>();
      server.listen().onComplete(stompLock.handler());
      stompLock.waitForSuccess();
    }
    AsyncLock<HttpServer> httpLock = new AsyncLock<>();
    http.listen().onComplete(httpLock.handler());
    httpLock.waitForSuccess();
  }

  @After
  public void tearDown() {
    clients.forEach(StompClient::close);
    clients.clear();

    AsyncLock<Void> lock = new AsyncLock<>();
    server.close().onComplete(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    http.close().onComplete(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close().onComplete(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void testTextFrameConnection() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicBoolean receivedBinaryFrame = new AtomicBoolean(false);

    WebSocketClient client = vertx.createWebSocketClient();
    client.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        socket.set(ws);
        ws.exceptionHandler(error::set)
          .textMessageHandler(text -> {
            // Should receive STOMP frames as text
            frame.set(Buffer.buffer(text));
          })
          .binaryMessageHandler(buffer -> {
            // Should NOT receive binary frames in TEXT mode
            receivedBinaryFrame.set(true);
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(receivedBinaryFrame.get()).isFalse();
    assertThat(frame.get().toString()).startsWith("CONNECTED")
      .contains("server:vertx-stomp", "heart-beat:", "session:", "version:1.2");
    socket.get().close();
  }

  @Test
  public void testTextFrameMessageExchange() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> messageFrame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicBoolean receivedBinaryFrame = new AtomicBoolean(false);

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    StompClient c = StompClient.create(vertx);
    c.connect(61613, "localhost").onComplete(connection -> {
      client.set(connection.result());
    });
    clients.add(c);

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);

    WebSocketClient wsClient = vertx.createWebSocketClient();
    wsClient.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        socket.set(ws);
        ws.exceptionHandler(error::set)
          .textMessageHandler(text -> {
            Buffer buffer = Buffer.buffer(text);
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SUBSCRIBE, Headers.create("id", "sub-0", "destination", "foo"), null)
                  .toBuffer());
              return;
            }
            if (messageFrame.get() == null) {
              messageFrame.set(buffer);
            }
          })
          .binaryMessageHandler(buffer -> {
            receivedBinaryFrame.set(true);
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);
    client.get().send("foo", Headers.create("header", "value"), Buffer.buffer("hello"));

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && messageFrame.get() != null);
    assertThat(receivedBinaryFrame.get()).isFalse();
    assertThat(messageFrame.get().toString()).startsWith("MESSAGE")
      .contains("destination:foo", "content-length:5", "header:value", "subscription:sub-0", "\nhello");
    socket.get().close();
  }

  @Test
  public void testTextFrameWithUTF8Content() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Frame> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicBoolean receivedBinaryFrame = new AtomicBoolean(false);

    String utf8Message = "Hello 世界 🌍";  // UTF-8 content with Chinese and emoji

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    StompClient c = StompClient.create(vertx);
    c.connect(61613, "localhost").onComplete(connection -> {
      connection.result().subscribe("utf8test", frame::set).onComplete(r -> {
        client.set(connection.result());
      });
    });
    clients.add(c);

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);
    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("utf8test") != null);

    WebSocketClient wsClient = vertx.createWebSocketClient();
    wsClient.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        socket.set(ws);
        ws.exceptionHandler(error::set)
          .textMessageHandler(text -> {
            if (text.startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SEND, Headers.create("header", "value", "destination", "utf8test"),
                  Buffer.buffer(utf8Message)).toBuffer());
            }
          })
          .binaryMessageHandler(buffer -> {
            receivedBinaryFrame.set(true);
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(receivedBinaryFrame.get()).isFalse();
    assertThat(frame.get().toString()).startsWith("MESSAGE")
      .contains("destination:utf8test", "header:value", "\n" + utf8Message);
    socket.get().close();
  }
}
