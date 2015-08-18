package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code NACK} sf.frame(). It removes the acknowledges messages
 * from the list of messages waiting for acknowledgment and trigger an
 * {@link StompServerHandler#onNack(Subscription, List)} calls.
 * <p/>
 * If the {@code NACK} frame specifies a transaction id, the acknowledgment is delayed until the transaction commit.
 *
 * This handler is thread safe.
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
      Transaction transaction = connection.handler().getTransaction(connection, txId);
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
          connection.handler().unregisterTransactionsFromConnection(connection);
          connection.write(errorFrame);
          connection.close();
          return;
        }
        Frames.handleReceipt(sf.frame(), connection);
        // Nothing else in transactions.
        return;
      }
    }

    Subscription subscription = connection.handler().getSubscription(connection, id);
    // Not found ignore, it may be too late...
    if (subscription != null) {
      subscription.nack(id);
    }

    Frames.handleReceipt(sf.frame(), connection);
  }


}
