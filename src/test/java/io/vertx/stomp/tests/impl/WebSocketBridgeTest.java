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

package io.vertx.stomp.tests.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.apache.commons.lang.StringUtils;
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
import static org.junit.Assert.assertEquals;

/**
 * Checks that the server can handle web socket connection. These tests mimic the behavior of StompJS
 * (http://jmesnil.net/stomp-websocket/doc/).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class WebSocketBridgeTest {

  public static final int MAX_WEBSOCKET_FRAME_SIZE = 1024;
  private Vertx vertx;
  private StompServer server;
  private HttpServer http;
  private WebSocketConnectOptions options = new WebSocketConnectOptions()
    .setPort(8080)
    .setURI("/stomp")
    .addHeader("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp");

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();

    vertx = Vertx.vertx();

    server = StompServer.create(vertx, new StompServerOptions().setWebsocketBridge(true))
        .handler(StompServerHandler.create(vertx)
         .bridge(new BridgeOptions()
          .addInboundPermitted(new PermittedOptions().setAddressRegex(".*"))
          .addOutboundPermitted(new PermittedOptions().setAddressRegex(".*")))
        );

    HttpServerOptions httpOptions = new HttpServerOptions()
      .setMaxWebSocketFrameSize(MAX_WEBSOCKET_FRAME_SIZE)
      .setMaxWebSocketMessageSize(2048)
      .setPort(8080);

    http = vertx.createHttpServer(httpOptions).webSocketHandler(server.webSocketHandler());
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
  public void testConnection() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

    WebSocketClient client = vertx.createWebSocketClient();
    client.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        socket.set(ws);
        ws.exceptionHandler(error::set)
          .handler(frame::set)
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("CONNECTED")
        .contains("server:vertx-stomp", "heart-beat:", "session:", "version:1.2");
    socket.get().close();
  }

  @Test
  public void testReceivingAMessage() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

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
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SUBSCRIBE, Headers.create("id", "sub-0", "destination", "foo"), null)
                  .toBuffer());
              return;
            }
            if (frame.get() == null) {
              frame.set(buffer);
            }
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);
    client.get().send("foo", Headers.create("header", "value"), Buffer.buffer("hello"));

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("MESSAGE")
        .contains("destination:foo", "content-length:5", "header:value", "subscription:sub-0", "\nhello");
    socket.get().close();
  }

  @Test
  public void testSendingAMessage() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Frame> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    StompClient c = StompClient.create(vertx);
    c.connect(61613, "localhost").onComplete(connection -> {
      connection.result().subscribe("foo", frame::set).onComplete(r -> {
        client.set(connection.result());
      });
    });
    clients.add(c);

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);
    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);


    WebSocketClient wsClient = vertx.createWebSocketClient();
    wsClient.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        socket.set(ws);
        ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SEND, Headers.create("header", "value", "destination", "foo"), Buffer
                  .buffer("hello")).toBuffer());
            }
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("MESSAGE")
        .contains("destination:foo", "header:value", "\nhello");
    socket.get().close();
  }

  @Test
  /*
      Constructs a message with size == 2*MAX_WEBSOCKET_FRAME_SIZE. The message is then sent via
      eventBus bridge. The test then reads the message via WebSocket and makes sure that the message
      is delivered in three WebSocketFrames.
      Regression for #35
   */
  public void testSendingAMessageBiggerThanSocketFrameSize() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    List<WebSocketFrame> wsBuffers = new ArrayList<>();
    List<Buffer> stompBuffers = new ArrayList<>();

    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    StompClient c = StompClient.create(vertx);
    c.connect(61613, "localhost").onComplete(connection -> {
      connection.result().subscribe("bigData", h -> {
      }).onComplete(r -> {
        client.set(connection.result());

      });
      connection.result().receivedFrameHandler(stompFrame -> {
        if (stompFrame.toBuffer().toString().startsWith("MESSAGE")) {
          stompBuffers.add(stompFrame.toBuffer());
        }
      });
    });
    clients.add(c);

    WebSocketClient wsClient = vertx.createWebSocketClient();
    wsClient.connect(options).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        AtomicBoolean inMsg = new AtomicBoolean();
        ws.exceptionHandler(error::set)
          .frameHandler(frame -> {
            if (!frame.isContinuation()) {
              if (frame.isBinary()) {
                String data = frame.binaryData().toString();
                if (data.startsWith("CONNECTED")) {
                  ws.write(
                    new Frame(Command.SUBSCRIBE, Headers.create("id", "myId", "destination", "bigData"), null)
                      .toBuffer());
                }
                if (data.startsWith("MESSAGE")) {
                  // Start collecting the frames once we see the first real payload message
                  inMsg.set(true);
                  wsBuffers.add(frame);
                } else {
                  inMsg.set(false);
                }
              }
            } else if (inMsg.get()) {
              wsBuffers.add(frame);
            }
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
        socket.set(ws);
      }
    });

    // Create content that is slightly bigger than the size of a single web socket frame
    String bufferContent = StringUtils.repeat("*",  2 * MAX_WEBSOCKET_FRAME_SIZE);

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);
    await().atMost(10, TimeUnit.SECONDS).until(() -> socket.get() != null);
    vertx.eventBus().publish("bigData",bufferContent);

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && stompBuffers.size() == 1);
    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && wsBuffers.size() == 3);

    // STOMP message has 2048 bytes of payload + headers => 2167 bytes
    assertEquals(2167, stompBuffers.get(0).getBytes().length);

    // We expect two complete frames + 1 with 116 bytes
    assertEquals(MAX_WEBSOCKET_FRAME_SIZE, wsBuffers.get(0).binaryData().getBytes().length);
    assertEquals(MAX_WEBSOCKET_FRAME_SIZE, wsBuffers.get(1).binaryData().getBytes().length);
    assertEquals(116, wsBuffers.get(2).binaryData().getBytes().length);
    socket.get().close();
  }

  @Test
  public void testPingFromServer() {
    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicReference<Boolean> flag = new AtomicReference<>();

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    StompClient c = StompClient.create(vertx);
    c.connect(61613, "localhost").onComplete(connection -> {
      client.set(connection.result());
    });
    clients.add(c);

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);

    WebSocketClient wsClient = vertx.createWebSocketClient();
    vertx.runOnContext(v -> {
      wsClient.connect(options).onComplete(ar -> {
        if (ar.succeeded()) {
          WebSocket ws = ar.result();
          socket.set(ws);
          ws.exceptionHandler(error::set)
            .handler(buffer -> {
              vertx.setTimer(1000, id -> {
                flag.set(true);
              });
            })
            .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "100,0"), null).toBuffer());
        }
      });
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && flag.get() != null);
    socket.get().close();
  }

  @Test
  public void testWebSocketsWhenTCPDisabled() {
    server = StompServer.create(vertx, new StompServerOptions().setWebsocketBridge(true).setPort(-1)
        .setWebsocketPath("/something"))
        .handler(StompServerHandler.create(vertx));

    http = vertx
      .createHttpServer(new HttpServerOptions().setPort(8080))
      .webSocketHandler(server.webSocketHandler());

    startServers();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<WebSocket> sender = new AtomicReference<>();
    AtomicReference<WebSocket> receiver = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();

    WebSocketClient wsClient = vertx.createWebSocketClient();
    wsClient.connect(new WebSocketConnectOptions(options).setURI("/something")).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        receiver.set(ws);
        ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SUBSCRIBE, Headers.create("id", "sub-0", "destination", "foo"), null)
                  .toBuffer());
              return;
            }
            if (frame.get() == null) {
              frame.set(buffer);
            }
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);

    wsClient.connect(new WebSocketConnectOptions(options).setURI("/something")).onComplete(ar -> {
      if (ar.succeeded()) {
        WebSocket ws = ar.result();
        sender.set(ws);
        ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                new Frame(Command.SEND, Headers.create("header", "value", "destination", "foo"), Buffer
                  .buffer("hello")).toBuffer());
            }
          })
          .write(new Frame(Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
            "heart-beat", "10000,10000"), null).toBuffer());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("MESSAGE")
        .contains("destination:foo", "header:value", "subscription:sub-0", "\nhello");
    receiver.get().close();
    sender.get().close();
  }
}
