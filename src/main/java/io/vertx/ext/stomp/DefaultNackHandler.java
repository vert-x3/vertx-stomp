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
    String id = sf.frame().getId();
    if (id == null) {
      sf.connection().write(Frames.createErrorFrame(
          "Id header missing",
          Headers.create(sf.frame().getHeaders()), "Invalid NACK frame - the " +
              "'id' must be set"));
      sf.connection().close();
      return;
    }

    // Handle transaction
    String txId = sf.frame().getHeader(Frame.TRANSACTION);
    if (txId != null) {
      Transaction transaction = sf.connection().handler().getTransaction(sf.connection(), txId);
      if (transaction == null) {
        // No transaction.
        Frame errorFrame = Frames.createErrorFrame(
            "No transaction",
            Headers.create(Frame.ID, id, Frame.TRANSACTION, txId),
            "Message delivery failed - unknown transaction id in NACK message");
        sf.connection().write(errorFrame);
        sf.connection().close();
        return;
      } else {
        transaction.addFrameToTransaction(sf.frame());
        Frames.handleReceipt(sf.frame(), sf.connection());
        // Nothing else in transactions.
        return;
      }
    }

    Subscription subscription = sf.connection().handler().getSubscription(sf.connection(), id);
    // Not found ignore, it may be too late...
    if (subscription != null) {
      subscription.nack(id);
    }

    Frames.handleReceipt(sf.frame(), sf.connection());
  }


}
