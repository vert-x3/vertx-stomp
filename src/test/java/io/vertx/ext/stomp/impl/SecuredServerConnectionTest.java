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
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
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
@RunWith(io.vertx.ext.unit.junit.VertxUnitRunner.class)
public class SecuredServerConnectionTest {
  private Vertx vertx;
  private StompServer server;

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    JsonObject config = new JsonObject().put("properties_path", "classpath:test-auth.properties");
    AuthProvider provider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, config);
    AsyncLock<StompServer> lock = new AsyncLock<>();
    server = StompServer.create(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx).authProvider(provider))
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


}
