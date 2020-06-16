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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code COMMIT} frame. All frames that are part of the
 * transactions are processed ({@code ACK/NACK} and {@code SEND} frames). If the {@code COMMIT} frame defines a {@code
 * receipt}, the {@code RECEIPT} frame is sent once all frames have been replayed.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultCommitHandler implements Handler<ServerFrame> {

  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "COMMIT frames " +
          "must contain the " +
          "'transaction' header.");
      connection.write(error).close();
      return;
    }

    Transaction transaction = Transactions.instance().getTransaction(connection, txId);
    if (transaction == null) {
      Frame error = Frames.createErrorFrame("Unknown transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "The transaction id is unknown.");
      connection.write(error).close();
      return;
    }

    replay(connection, transaction.getFrames());
    transaction.clear();
    Transactions.instance().unregisterTransaction(connection, txId);

    Frames.handleReceipt(frame, connection);
  }

  private void replay(StompServerConnection connection, List<Frame> frames) {
    // To avoid blocking the event loop for too long, we replay the transaction chunk by chunk.
    Iterator<Frame> iterator = frames.iterator();
    while (iterator.hasNext()) {
      List<Frame> chunk = new ArrayList<>();
      while (iterator.hasNext() && chunk.size() < connection.server().options().getTransactionChunkSize()) {
        chunk.add(iterator.next());
      }
      connection.server().vertx().runOnContext(v -> replayChunk(connection, chunk));
    }
  }

  private void replayChunk(StompServerConnection connection, List<Frame> frames) {
    final List<Destination> destinations = connection.handler().getDestinations();
    for (Frame frame : frames) {
      switch (frame.getCommand()) {
        case SEND:
          // We are sure that the destination is set, as the check is made before enqueuing the frame.
          String destination = frame.getHeader(Frame.DESTINATION);
          Destination dest = connection.handler().getDestination(destination);
          if (dest != null) {
            dest.dispatch(connection, frame);
          }
          break;
        case ACK:
          for (Destination d : destinations) {
            if (d.ack(connection, frame)) {
              break;
            }
          }
          break;
        case NACK:
          for (Destination d : destinations) {
            if (d.nack(connection, frame)) {
              break;
            }
          }
          break;
      }
    }
  }
}
