package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STAMP compliant actions executed when receiving a {@code BEGIN} frame.
 */
public class DefaultBeginHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "BEGIN frames " +
          "must contain the 'transaction' header.");
      connection.write(error).close();
      return;
    }

    Transaction transaction = Transaction.create(connection, txId);
    if (!connection.handler().registerTransaction(transaction)) {
      Frame error = Frames.createErrorFrame("Already existing transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "A transaction using the same id is still active.");
      connection.write(error).close();
    }

    Frames.handleReceipt(frame, connection);
  }
}
