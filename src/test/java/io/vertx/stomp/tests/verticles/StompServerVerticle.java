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

package io.vertx.stomp.tests.verticles;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;

/**
 * A verticle starting a STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerVerticle extends VerticleBase {

  private StompServer server;

  @Override
  public Future<?> start() throws Exception {
    server = StompServer.create(vertx).handler(StompServerHandler.create(vertx)
        .destinationFactory((vertx, name) -> {
          if (config().getBoolean("useQueue", false)) {
            return Destination.queue(vertx, name);
          } else {
            return Destination.topic(vertx, name);
          }
        }));
    return server.listen();
  }

  @Override
  public Future<?> stop() throws Exception {
    return server.close();
  }
}
