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
import io.vertx.ext.stomp.impl.Transaction;
import io.vertx.ext.stomp.impl.Transactions;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code NACK} sf.frame(). It removes the acknowledges messages
 * from the list of messages waiting for acknowledgment and trigger an
 * {@link StompServerHandler#onNack(Subscription, List)} calls.
 * <p/>
 * If the {@code NACK} frame specifies a transaction id, the acknowledgment is delayed until the transaction commit.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultNackHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame sf) {
    StompServerConnection connection = sf.connection();
    String id = sf.frame().getId();
    if (id == null) {
      connection.write(Frames.createErrorFrame(
          "Id header missing",
          Headers.create(sf.frame().getHeaders()), "Invalid NACK frame - the " +
              "'id' must be set"));
      connection.close();
      return;
    }

    // Handle transaction
    String txId = sf.frame().getHeader(Frame.TRANSACTION);
    if (txId != null) {
      Transaction transaction = Transactions.instance().getTransaction(connection, txId);

      if (transaction == null) {
        // No transaction.
        Frame errorFrame = Frames.createErrorFrame(
            "No transaction",
            Headers.create(Frame.ID, id, Frame.TRANSACTION, txId),
            "Message delivery failed - unknown transaction id in NACK message");
        connection.write(errorFrame);
        connection.close();
        return;
      } else {
        if (!transaction.addFrameToTransaction(sf.frame())) {
          // Frame not added to transaction
          Frame errorFrame = Frames.createErrorFrame("Frame not added to transaction",
              Headers.create(Frame.ID, id, Frame.TRANSACTION, txId),
              "Message delivery failed - the frame cannot be added to the transaction - the number of allowed thread " +
                  "may have been reached");
          Transactions.instance().unregisterTransactionsFromConnection(connection);
          connection.write(errorFrame);
          connection.close();
          return;
        }
        Frames.handleReceipt(sf.frame(), connection);
        // Nothing else in transactions.
        return;
      }
    }

    final List<Destination> destinations = connection.handler().getDestinations();
    for (Destination destination : destinations) {
      if (destination.nack(connection, sf.frame())) {
        break;
      }
    }

    Frames.handleReceipt(sf.frame(), connection);
  }


}
