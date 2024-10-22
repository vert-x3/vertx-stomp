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

package io.vertx.stomp.tests.integration;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientOptions;

/**
 * A verticle sending messages to a STOMP destination.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompPublisher extends VerticleBase {

  private StompClient client;

  @Override
  public Future<?> start() throws Exception {
    System.out.println("Starting publisher");
    client = StompClient.create(vertx, new StompClientOptions(config()));
    return client
      .connect()
      .onSuccess(res -> {
        vertx.setPeriodic(5000, l -> res.send("/queue/event", Buffer.buffer("Hello")).onComplete(frame -> {
          System.out.println("Receipt received");
        }));
      });
  }

  @Override
  public Future<?> stop() throws Exception {
    return client.close();
  }
}
