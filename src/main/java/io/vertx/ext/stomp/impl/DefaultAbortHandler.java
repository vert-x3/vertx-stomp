package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STAMP compliant actions executed when receiving a {@code ABORT} frame.
 */
public class DefaultAbortHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "ABORT frames " +
          "must contain the 'transaction' header.");
      connection.write(error).close();
      return;
    }

    Transaction transaction = connection.handler().getTransaction(connection, txId);
    if (transaction == null) {
      Frame error = Frames.createErrorFrame("Unknown transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "The transaction id is unknown.");
      connection.write(error).close();
      return;
    }

    transaction.clear();
    connection.handler().unregisterTransaction(transaction);

    Frames.handleReceipt(frame, connection);
  }
}
