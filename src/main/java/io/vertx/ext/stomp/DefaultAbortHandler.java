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

package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.impl.Transactions;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code ABORT} frame.
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultAbortHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame sf) {
    String txId = sf.frame().getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "ABORT frames " +
          "must contain the 'transaction' header.");
      sf.connection().write(error).close();
      return;
    }

    if (! Transactions.instance().unregisterTransaction(sf.connection(), txId)) {
      Frame error = Frames.createErrorFrame("Unknown transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "The transaction id is unknown.");
      sf.connection().write(error).close();
      return;
    }

    Frames.handleReceipt(sf.frame(), sf.connection());
  }
}
