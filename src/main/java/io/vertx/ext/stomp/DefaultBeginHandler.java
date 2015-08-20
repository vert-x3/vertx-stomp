package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.impl.Transactions;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code BEGIN} frame.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultBeginHandler implements Handler<ServerFrame> {

  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "BEGIN frames " +
          "must contain the 'transaction' header.");
      connection.write(error).close();
      return;
    }

    if (!Transactions.instance().registerTransaction(connection, txId)) {
      Frame error = Frames.createErrorFrame("Already existing transaction",
          Headers.create(Frame.TRANSACTION, txId),
          "A transaction using the same id is still active.");
      connection.write(error).close();
      return;
    }

    Frames.handleReceipt(frame, connection);
  }
}
