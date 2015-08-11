package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STAMP compliant actions executed when receiving a {@code ACK} frame. It removes the acknowledges messages from the
 * list of messages waiting for acknowledgment. If the {@code ACK} frame specifies a transaction id, the
 * acknowledgment is delayed until the transaction commit.
 *
 * This handler is thread safe.
 */
public class DefaultAckHandler implements ServerFrameHandler {

  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
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
      Transaction transaction = connection.handler().getTransaction(connection, txId);
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
        transaction.addFrameToTransaction(frame);
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
