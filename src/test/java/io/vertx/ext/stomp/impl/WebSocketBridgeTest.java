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

package io.vertx.ext.stomp.impl;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocket;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that the server can handle web socket connection. These tests mimic the behavior of StompJS
 * (http://jmesnil.net/stomp-websocket/doc/).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class WebSocketBridgeTest {

  private Vertx vertx;
  private StompServer server;
  private HttpServer http;

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    AsyncLock<HttpServer> httpLock = new AsyncLock<>();
    AsyncLock<StompServer> stompLock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx, new StompServerOptions().setWebsocketBridge(true))
        .handler(StompServerHandler.create(vertx))
        .listen(stompLock.handler());
    stompLock.waitForSuccess();

    http = vertx.createHttpServer().websocketHandler(server.webSocketHandler()).listen(8080, httpLock.handler());
    httpLock.waitForSuccess();
  }

  @After
  public void tearDown() {
    clients.forEach(StompClient::close);
    clients.clear();

    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    http.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void testConnection() {
    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

    vertx.createHttpClient().websocket(8080, "localhost", "/stomp", MultiMap.caseInsensitiveMultiMap().add
        ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      socket.set(ws);
      ws.exceptionHandler(error::set)
          .handler(frame::set)
          .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "10000,10000"), null).toBuffer());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("CONNECTED")
        .contains("server:vertx-stomp", "heart-beat:", "session:", "version:1.2");
    socket.get().close();
  }

  @Test
  public void testReceivingAMessage() {
    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    clients.add(StompClient.create(vertx).connect(61613, "localhost", connection -> {
      client.set(connection.result());
    }));

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);

    vertx.createHttpClient().websocket(8080, "localhost", "/stomp", MultiMap.caseInsensitiveMultiMap().add
        ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      socket.set(ws);
      ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                  new Frame(Frame.Command.SUBSCRIBE, Headers.create("id", "sub-0", "destination", "foo"), null)
                      .toBuffer());
              return;
            }
            if (frame.get() == null) {
              frame.set(buffer);
            }
          })
          .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "10000,10000"), null).toBuffer());
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
    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<Frame> frame = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    clients.add(StompClient.create(vertx).connect(61613, "localhost", connection -> {
      connection.result().subscribe("foo", frame::set, r -> {
        client.set(connection.result());
      });
    }));

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);
    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);


    vertx.createHttpClient().websocket(8080, "localhost", "/stomp", MultiMap.caseInsensitiveMultiMap().add
        ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      socket.set(ws);
      ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                  new Frame(Frame.Command.SEND, Headers.create("header", "value", "destination", "foo"), Buffer
                      .buffer("hello")).toBuffer());
            }
          })
          .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "10000,10000"), null).toBuffer());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("MESSAGE")
        .contains("destination:foo", "header:value", "\nhello");
    socket.get().close();
  }

  @Test
  public void testPingFromServer() {
    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<WebSocket> socket = new AtomicReference<>();
    AtomicReference<Boolean> flag = new AtomicReference<>();

    AtomicReference<StompClientConnection> client = new AtomicReference<>();

    clients.add(StompClient.create(vertx).connect(61613, "localhost", connection -> {
      client.set(connection.result());
    }));

    await().atMost(10, TimeUnit.SECONDS).until(() -> client.get() != null);

    vertx.createHttpClient().websocket(8080, "localhost", "/stomp", MultiMap.caseInsensitiveMultiMap().add
            ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      socket.set(ws);
      ws.exceptionHandler(error::set)
              .handler(buffer -> {
                vertx.setTimer(1000, id -> {
                  flag.set(true);
                });
              })
              .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
                      "heart-beat", "100,0"), null).toBuffer());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && flag.get() != null);
    socket.get().close();
  }

  @Test
  public void testWebSocketsWhenTCPDisabled() {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    http.close(lock.handler());
    lock.waitForSuccess();

    server = StompServer.create(vertx, new StompServerOptions().setWebsocketBridge(true).setPort(-1)
        .setWebsocketPath("/something"))
        .handler(StompServerHandler.create(vertx));

    AsyncLock<HttpServer> httpLock = new AsyncLock<>();
    http = vertx.createHttpServer().websocketHandler(server.webSocketHandler()).listen(8080, httpLock.handler());
    httpLock.waitForSuccess();

    AtomicReference<Throwable> error = new AtomicReference<>();
    AtomicReference<WebSocket> sender = new AtomicReference<>();
    AtomicReference<WebSocket> receiver = new AtomicReference<>();
    AtomicReference<Buffer> frame = new AtomicReference<>();

    vertx.createHttpClient().websocket(8080, "localhost", "/something", MultiMap.caseInsensitiveMultiMap().add
        ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      receiver.set(ws);
      ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                  new Frame(Frame.Command.SUBSCRIBE, Headers.create("id", "sub-0", "destination", "foo"), null)
                      .toBuffer());
              return;
            }
            if (frame.get() == null) {
              frame.set(buffer);
            }
          })
          .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "10000,10000"), null).toBuffer());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("foo") != null);

    vertx.createHttpClient().websocket(8080, "localhost", "/something", MultiMap.caseInsensitiveMultiMap().add
        ("Sec-WebSocket-Protocol", "v10.stomp, v11.stomp, v12.stomp"), ws -> {
      sender.set(ws);
      ws.exceptionHandler(error::set)
          .handler(buffer -> {
            if (buffer.toString().startsWith("CONNECTED")) {
              ws.write(
                  new Frame(Frame.Command.SEND, Headers.create("header", "value", "destination", "foo"), Buffer
                      .buffer("hello")).toBuffer());
            }
          })
          .write(new Frame(Frame.Command.CONNECT, Headers.create("accept-version", "1.2,1.1,1.0",
              "heart-beat", "10000,10000"), null).toBuffer());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> error.get() == null && frame.get() != null);
    assertThat(frame.get().toString()).startsWith("MESSAGE")
        .contains("destination:foo", "header:value", "subscription:sub-0", "\nhello");
    receiver.get().close();
    sender.get().close();
  }


}
