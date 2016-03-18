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

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.stomp.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests STOMP server with security.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class SecuredServerConnectionTest {
  private Vertx vertx;
  private StompServer server;

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Before
  public void setUp(TestContext context) {
    vertx = rule.vertx();
    JsonObject config = new JsonObject().put("properties_path", "classpath:test-auth.properties");
    AuthProvider provider = ShiroAuth.create(vertx, new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(config));
    server = StompServer.create(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx).authProvider(provider))
        .listen(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    server.close(context.asyncAssertSuccess());
    // Do not close the vert.x instance when using the RunTestOnContext rule.
  }

  @Test
  public void testAuthenticatedConnection(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\npasscode:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testFailedAuthentication(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
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
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
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
    vertx.createNetClient().connect(server.actualPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
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
    client.connect(connection -> {
      context.fail("Authentication issue expected");
    });
  }


}
