package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.impl.Transaction;
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
