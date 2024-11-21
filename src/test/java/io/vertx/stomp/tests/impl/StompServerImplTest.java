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

package io.vertx.stomp.tests.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.impl.StompServerImpl;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * test the {@link StompServerImpl}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class StompServerImplTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    vertx.close();
  }

  @Test
  public void testStartStop(TestContext context) {
    final Async async = context.async();
    StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen().onComplete(ar -> {
          ensureListening(context, ar);
          ar.result().close().onComplete(ar2 -> {
            ensureClosed(context, ar2, ar.result());
            async.complete();
          });
        });
  }


  @Test
  public void testStartReceiveStop(TestContext context) {
    final Async async = context.async();
    StompServer server = StompServer.create(vertx);
    server.handler(StompServerHandler.create(vertx)
        .connectHandler(
            sf -> {
              Frame frame = sf.frame();
              context.assertTrue(frame.getCommand() == Command.CONNECT);
              context.assertTrue(frame.getHeader("login").equals("system"));
              server.close().onComplete(ar2 -> {
                ensureClosed(context, ar2, server);
                async.complete();
              });
            }
        )
    ).listen().onComplete(ar -> {
      ensureListening(context, ar);
      writeMessage(vertx);
    });
  }

  @Test
  public void testStartReceiveStopWithTrailingSpaces(TestContext context) {
    final Async async = context.async();
    StompServer server = StompServer.create(vertx, new StompServerOptions().setTrailingLine(true));
    server.handler(StompServerHandler.create(vertx)
        .connectHandler(
            sf -> {
              Frame frame = sf.frame();
              context.assertTrue(frame.getCommand() == Command.CONNECT);
              context.assertTrue(frame.getHeader("login").equals("system"));
              server.close().onComplete(ar2 -> {
                ensureClosed(context, ar2, server);
                async.complete();
              });
            }
        )
    ).listen().onComplete(ar -> {
      ensureListening(context, ar);
      writeMessageWithTrailingLine(vertx);
    });
  }

  @Test
  public void testWithStompServerHandler(TestContext context) {
    final Async async = context.async();
    StompServer server = StompServer.create(vertx);
    server.handler(
        StompServerHandler.create(vertx).connectHandler(
            sf -> {
              Frame frame = sf.frame();
              context.assertTrue(frame.getCommand() == Command.CONNECT);
              context.assertTrue(frame.getHeader("login").equals("system"));
              server.close().onComplete(ar2 -> {
                ensureClosed(context, ar2, server);
                async.complete();
              });
            }
        )).listen().onComplete(ar -> {
      ensureListening(context, ar);
      writeMessage(vertx);
    });
  }

  @Test
  public void testWithStompServerHandlerWithTrailingLine(TestContext context) {
    final Async async = context.async();
    StompServer server = StompServer.create(vertx, new StompServerOptions().setTrailingLine(true));
    server.handler(
        StompServerHandler.create(vertx).connectHandler(
            sf -> {
              Frame frame = sf.frame();
              context.assertTrue(frame.getCommand() == Command.CONNECT);
              context.assertTrue(frame.getHeader("login").equals("system"));
              server.close().onComplete(ar2 -> {
                ensureClosed(context, ar2, server);
                async.complete();
              });
            }
        )).listen().onComplete(ar -> {
      ensureListening(context, ar);
      writeMessageWithTrailingLine(vertx);
    });
  }

  @Test
  public void testWhenPortIsSetToMinusOneInOptions(TestContext context) {
    final Async async = context.async();
    StompServer.create(vertx, new StompServerOptions().setPort(-1)).handler(StompServerHandler
        .create(vertx))
        .listen().onComplete(ar -> {
          if (!ar.failed()) {
            context.fail("Error expected");
          } else {
            // Create a client and check it cannot connect
            StompClient.create(vertx).connect(61613, "localhost").onComplete(x -> {
              if (!x.failed()) {
                context.fail("Error expected on the client side");
              }
              async.complete();
            });
          }
        });
  }

  @Test
  public void testWhenPortIsSetToMinusOneInListen(TestContext context) {
    final Async async = context.async();
    StompServer.create(vertx).handler(StompServerHandler
        .create(vertx))
        .listen(-1).onComplete(ar -> {
          if (!ar.failed()) {
            context.fail("Error expected");
          } else {
            // Create a client and check it cannot connect
            StompClient.create(vertx).connect(61613, "localhost").onComplete(x -> {
              if (!x.failed()) {
                context.fail("Error expected on the client side");
              }
              async.complete();
            });
          }
        });
  }

  @Test
  public void testConnectionCloseTwice(TestContext context) {
    final Async async = context.async();
    StompServer server = StompServer.create(vertx);
    AtomicInteger closeTimes = new AtomicInteger(0);
    server.handler(
      StompServerHandler.create(vertx).connectHandler(
        sf -> {
          Frame frame = sf.frame();
          context.assertTrue(frame.getCommand() == Command.CONNECT);
          context.assertTrue(frame.getHeader("login").equals("system"));
          sf.connection().close();
          vertx.setTimer(1000, id -> {
            server.close().onComplete(ar2 -> {
              context.assertEquals(1, closeTimes.get());
              ensureClosed(context, ar2, server);
              async.complete();
            });
          });
        }
      ).closeHandler(conn ->{
        closeTimes.incrementAndGet();
      })).listen().onComplete(ar -> {
      ensureListening(context, ar);
      writeMessage(vertx);
    });
  }

  private void writeMessage(Vertx vertx) {
    vertx.createNetClient().connect(StompServerOptions.DEFAULT_STOMP_PORT, "0.0.0.0")
      .onComplete(ar -> ar.result().write("CONNECT\n" + "login:system\n" + "passcode:manager\n\n" + FrameParser.NULL));
  }

  private void writeMessageWithTrailingLine(Vertx vertx) {
    vertx.createNetClient().connect(StompServerOptions.DEFAULT_STOMP_PORT, "0.0.0.0")
      .onComplete(ar -> ar.result().write("CONNECT\n" + "login:system\n" + "passcode:manager\n\n" + FrameParser.NULL + "\n"));
  }

  private void ensureClosed(TestContext context, AsyncResult<Void> ar, StompServer server) {
    context.assertTrue(ar.succeeded());
    context.assertFalse(server.isListening());
  }

  private void ensureListening(TestContext context, AsyncResult<StompServer> ar) {
    context.assertTrue(ar.succeeded());
    context.assertNotNull(ar.result());
    context.assertTrue(ar.result().isListening());
  }
}
