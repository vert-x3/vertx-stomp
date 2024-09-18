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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientConnection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A STOMP client receiving messages.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReceiverStompClient extends AbstractVerticle {


  public static final List<Frame> FRAMES = new CopyOnWriteArrayList<>();

  @Override
  public void start(Promise<Void> future) throws Exception {
    StompClient.create(vertx).connect().onComplete(ar -> {
      if (ar.failed()) {
        future.fail(ar.cause());
        return;
      }
      final StompClientConnection connection = ar.result();
      connection
          .receivedFrameHandler(frame -> System.out.println("Client receiving:\n" + frame))
          .writingFrameHandler(frame -> System.out.println("Client sending:\n" + frame))
          .subscribe("/queue", FRAMES::add).onComplete(frame -> {
            future.tryComplete();
      });
    });

  }
}
