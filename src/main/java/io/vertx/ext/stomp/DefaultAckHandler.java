package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.impl.Transaction;
import io.vertx.ext.stomp.impl.Transactions;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code ACK} frame. It removes the acknowledges messages from the
 * list of messages waiting for acknowledgment. If the {@code ACK} frame specifies a transaction id, the
 * acknowledgment is delayed until the transaction commit.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultAckHandler implements Handler<ServerFrame> {

  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
    String id = frame.getId();
    if (id == null) {
      connection.write(Frames.createErrorFrame(
          "Id header missing",
          Headers.create(frame.getHeaders()), "Invalid ACK frame - the " +
              "'id' must be set"));
      connection.close();
      return;
    }

    // Handle transaction
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId != null) {
      Transaction transaction = Transactions.instance().getTransaction(connection, txId);
      if (transaction == null) {
        // No transaction.
        Frame errorFrame = Frames.createErrorFrame(
            "No transaction",
            Headers.create(Frame.ID, id, Frame.TRANSACTION, txId),
            "Message delivery failed - unknown transaction id in ACK message");
        connection.write(errorFrame);
        connection.close();
        return;
      } else {
        if (!transaction.addFrameToTransaction(frame)) {
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
        Frames.handleReceipt(frame, connection);
        // Nothing else in transactions.
        return;
      }
    }

    Subscription subscription = connection.handler().getSubscription(connection, id);
    // Not found ignore, it may be too late...
    if (subscription != null) {
      subscription.ack(id);
    }

    Frames.handleReceipt(frame, connection);
  }
}
