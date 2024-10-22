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
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A STOMP client receiving messages.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReceiverStompClient extends VerticleBase {

  public static final List<Frame> FRAMES = new CopyOnWriteArrayList<>();

  @Override
  public Future<?> start() throws Exception {
    return StompClient
      .create(vertx)
      .connect()
      .compose(connection -> connection
        .receivedFrameHandler(frame -> System.out.println("Client receiving:\n" + frame))
        .writingFrameHandler(frame -> System.out.println("Client sending:\n" + frame))
        .subscribe("/queue", FRAMES::add));
  }
}
