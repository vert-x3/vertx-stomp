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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Checks the {@code ACK} and {@code NACK} handling.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class TestSendFailure {

  private Vertx vertx;
  private StompServer server;
  private StompClient client;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .receivedFrameHandler(frame -> {
              if ("/queue".equalsIgnoreCase(frame.frame().getDestination())) {
                server.close();
              }
            })
            .destinationFactory(new QueueManagingAcknowledgmentsFactory()));
    server.listen().onComplete(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    if (client != null) {
      client.close();
    }
    server.close().onComplete(context.asyncAssertSuccess());
    vertx.close().onComplete(context.asyncAssertSuccess());
  }


  @Test
  public void testSimpleAck(TestContext ctx) {
    Handler<AsyncResult<Frame>> receiptHandler = ctx.asyncAssertFailure();
    client = StompClient.create(vertx);
    client.connect().onComplete(ctx.asyncAssertSuccess(conn -> {
      conn.send("/queue", Buffer.buffer("Hello")).onComplete(receiptHandler);
    }));
  }
}
