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
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.utils.Headers;

/**
 * A verticle connecting to a STOMP server and sending message in a transaction.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class TxSenderStompClient extends VerticleBase {


  @Override
  public Future<?> start() throws Exception {
    return StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        connection.errorHandler(frame -> System.err.println("Tx Sender has received an ERROR frame : \n" + frame));
        connection.beginTX("my-transaction");
        connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Hello"));
        connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("My"));
        connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Name"));
        connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Is"));
        connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Vert.x"));
        connection.commit("my-transaction");
        connection.disconnect();
      });
  }
}
