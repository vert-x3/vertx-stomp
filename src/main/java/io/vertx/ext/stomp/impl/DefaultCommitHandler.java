package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;
import java.util.UUID;

/**
 * STAMP compliant actions executed when receiving a {@code COMMIT} frame. All frames that are part of the
 * transactions are processed ({@code ACK/NACK} and {@code SEND} frames). If the {@code COMMIT} frame defines a {@code
 * receipt}, the {@code RECEIPT} frame is sent once all frames have been replayed.
 *
 * This handler is thread safe.
 */
public class DefaultCommitHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String txId = frame.getHeader(Frame.TRANSACTION);
    if (txId == null) {
      Frame error = Frames.createErrorFrame("Missing transaction id", Headers.create(), "COMMIT frames " +
          "must contain the " +
          "'transaction' header.");
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

    replay(connection, transaction.getFrames());
    transaction.clear();
    connection.handler().unregisterTransaction(transaction);

    Frames.handleReceipt(frame, connection);
  }

  private void replay(StompServerConnection connection, List<Frame> frames) {
    for (Frame frame : frames) {
      switch (frame.getCommand()) {
        case SEND:
          // We are sure that the destination is set, as the check is made before enqueuing the frame.
          String destination = frame.getHeader(Frame.DESTINATION);
          List<Subscription> subscriptions = connection.handler().getSubscriptions(destination);
          subscriptions.stream().forEach(subscription -> {
                String messageId = UUID.randomUUID().toString();
                Frame message = DefaultSendHandler.sendToMessage(frame, subscription, messageId);
                subscription.enqueue(message);
                subscription.connection().write(message.toBuffer());
              }
          );
          break;
        case ACK:
          String id = frame.getId();
          Subscription subscription = connection.handler().getSubscription(connection, id);
          // Not found ignore, it may be too late...
          if (subscription != null) {
            subscription.ack(id);
          }
          break;
        case NACK:
          id = frame.getId();
          subscription = connection.handler().getSubscription(connection, id);
          // Not found ignore, it may be too late...
          if (subscription != null) {
            subscription.nack(id);
          }
          break;
      }
    }
  }
}