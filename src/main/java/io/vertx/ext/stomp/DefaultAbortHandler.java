package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code ABORT} frame.
 * This handler is thread safe.
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

    Transaction transaction = sf.connection().handler().getTransaction(sf.connection(), txId);
    if (transaction == null) {
      Frame error = Frames.createErrorFrame("Unknown transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "The transaction id is unknown.");
      sf.connection().write(error).close();
      return;
    }

    transaction.clear();
    sf.connection().handler().unregisterTransaction(transaction);

    Frames.handleReceipt(sf.frame(), sf.connection());
  }
}
