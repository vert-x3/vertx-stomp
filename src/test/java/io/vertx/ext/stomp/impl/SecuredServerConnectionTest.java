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

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.properties.PropertyFileAuthentication;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientOptions;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Tests STOMP server with security.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class SecuredServerConnectionTest {

  private Vertx vertx;
  private StompServer server;
  private HttpServer wsServer;
  private HttpClient wsClient;
  private StompClient client;

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Before
  public void setUp(TestContext context) {
    vertx = rule.vertx();
    AuthenticationProvider provider = PropertyFileAuthentication.create(vertx, "test-auth.properties");
    server = StompServer.create(vertx, new StompServerOptions()
        .setSecured(true)
        .setWebsocketBridge(true)
        .setWebsocketPath("/stomp"))
      .handler(StompServerHandler.create(vertx).authProvider(provider));
    server.listen(StompServerOptions.DEFAULT_STOMP_PORT).onComplete(context.asyncAssertSuccess());
    wsServer = vertx.createHttpServer(new HttpServerOptions().setWebSocketSubProtocols(Arrays.asList("v10.stomp", "v11.stomp")))
      .webSocketHandler(server.webSocketHandler());
    wsServer.listen(8080).onComplete(context.asyncAssertSuccess());
    wsClient = vertx.createHttpClient();
    client = StompClient.create(vertx, new StompClientOptions().setLogin("admin").setPasscode("admin"));
  }

  @After
  public void tearDown(TestContext context) {
    server.close().onComplete(context.asyncAssertSuccess());
    // Do not close the vert.x instance when using the RunTestOnContext rule.
  }

  static String extractSession(String data) {
    int start = data.indexOf(Frame.SESSION) + Frame.SESSION.length() + 1;
    int end = data.indexOf('\n', start);
    String ret = data.substring(start, end);
    return ret;
  }

  @Test
  public void testAuthenticatedConnection(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0").onComplete(result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        validate(context, buffer);

        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\npasscode:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testFailedAuthentication(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0").onComplete(result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("Authentication failed"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\npasscode:nope\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test(timeout = 5000)
  public void testFailedAuthenticationBecauseOfMissingHeaders(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0").onComplete(result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("Authentication failed"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testAuthenticatedConnectionWithStompFrame(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0").onComplete(result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        validate(context, buffer);
        async.complete();
      });
      socket.write("STOMP\n" + "accept-version:1.2\nlogin:admin\npasscode:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testFailedAuthenticationWithClient(TestContext context) {
    Async async = context.async();
    StompClient client = StompClient.create(vertx, new StompClientOptions()
        .setPort(server.actualPort()).setHost("0.0.0.0").setLogin("admin").setPasscode("nope"))
        .errorFrameHandler(frame -> {
          async.complete();
        });
    client.connect().onComplete(context.asyncAssertFailure());
  }

  @Test
  public void testClientConnectRejection(TestContext context) {
    StompClient.create(vertx).connect(server.actualPort(), "localhost").onComplete(context.asyncAssertFailure(err -> {
    }));
  }

  void validate(TestContext context, Buffer buffer) {
    context.assertTrue(buffer.toString().contains("CONNECTED"), "Was expected <" + buffer.toString() + "> to contain 'CONNECTED'");
    context.assertTrue(buffer.toString().contains("version:1.2"));

    User user = server.stompHandler().getUserBySession(extractSession(buffer.toString()));
    context.assertNotNull(user);
  }

  @Test
  public void testTCPClientMustBeConnected(TestContext context) {
    Async async = context.async();
    NetClient client = vertx.createNetClient();
    testClientMustBeConnected(context, v -> {
      client.connect(server.actualPort(), "0.0.0.0").onComplete(context.asyncAssertSuccess(so -> {
        Buffer received = Buffer.buffer();
        so.handler(received::appendBuffer);
        so.write(
          "SEND\n" +
            "destination:/test\n" +
            "\n" +
            "hello" +
            FrameParser.NULL);
        so.endHandler(v2 -> {
          context.assertTrue(received.toString().startsWith("ERROR\n"));
          async.complete();
        });
      }));
    });
  }

  @Test
  public void testWebSocketClientMustBeConnected(TestContext context) {
    Async async = context.async();
    testClientMustBeConnected(context, v -> {
      wsClient.webSocket(8080, "localhost", "/stomp").onComplete(context.asyncAssertSuccess(ws -> {
        Buffer received = Buffer.buffer();
        ws.binaryMessageHandler(received::appendBuffer);
        ws.writeBinaryMessage(
          Buffer.buffer("SEND\n" +
            "destination:/test\n" +
            "\n" +
            "hello" +
            FrameParser.NULL));
        ws.endHandler(v2 -> {
          context.assertTrue(received.toString().startsWith("ERROR\n"));
          async.complete();
        });
      }));
    });
  }

  private void testClientMustBeConnected(TestContext context, Handler<Void> cont) {
    client
      .connect(server.actualPort(), "localhost")
      .onComplete(context.asyncAssertSuccess(conn -> {
        Future<String> fut = conn.subscribe("/test", frame -> {
          context.fail("Should not receive a messsage");
        });
        fut.onComplete(context.asyncAssertSuccess(v2 -> {
          cont.handle(null);
        }));
      }));
  }
}
