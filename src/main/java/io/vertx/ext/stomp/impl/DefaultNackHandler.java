package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;

/**
 * STAMP compliant actions executed when receiving a {@code NACK} frame. It removes the acknowledges messages from the
 * list of messages waiting for acknowledgment and trigger an
 * {@link StompServerHandler#onNack(Subscription, List)} calls.
 * <p/>
 * If the {@code NACK} frame specifies a transaction id, the acknowledgment is delayed until the transaction commit.
 */
public class DefaultNackHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String id = frame.getId();
    if (id == null) {
      connection.write(Frames.createErrorFrame(
          "Id header missing",
          Headers.create(frame.getHeaders()), "Invalid NACK frame - the " +
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
            "Message delivery failed - unknown transaction id in NACK message");
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
      subscription.nack(id);
    }

    Frames.handleReceipt(frame, connection);
  }


}
