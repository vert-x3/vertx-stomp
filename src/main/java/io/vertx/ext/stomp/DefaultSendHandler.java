package io.vertx.ext.stomp;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;
import java.util.UUID;

/**
 * STAMP compliant actions executed when receiving a {@code SEND} frame.
 * <p/>
 * If the {@code SEND} frame specifies a transaction, the message delivery is postponed until the transaction commit.
 * <p/>
 * The handler computes the {@code MESSAGE} frame from the {@code SEND} frame. It computes a {@code message-id} and
 * {@code ack} id if needed. If requested the {@code RECEIPT} frame is sent once the {@code MESSAGE} frame has been
 * sent to all matching subscriptions.
 * <p/>
 * If the {@code SEND} frame requires an acknowledgment, the {@code message-id} is added to the list of messages
 * waiting for acknowledgment. If the corresponding {@code ACK} frame does not arrives before
 * {@link StompServerOptions#getAckTimeout()} ms, the message is considered as non-acknowledged.
 *
 * This handler is thread safe.
 */
public class DefaultSendHandler implements ServerFrameHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultSendHandler.class);


  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String destination = frame.getHeader(Frame.DESTINATION);
    if (destination == null) {
      connection.write(Frames.createErrorFrame(
          "Destination header missing",
          Headers.create(frame.getHeaders()), "Invalid send frame - the " +
              "'destination' must be set"));
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
            Headers.create(Frame.DESTINATION, destination, Frame.TRANSACTION, txId),
            "Message delivery failed - unknown transaction id");
        connection.write(errorFrame);
        connection.close();
        return;
      } else {
        transaction.addFrameToTransaction(frame);
        Frames.handleReceipt(frame, connection);
        // No delivery in transactions.
        return;
      }
    }


    List<Subscription> subscriptions = connection.handler().getSubscriptions(destination);
    if (subscriptions.isEmpty() && connection.server().getOptions().isSendErrorOnNoSubscriptions()) {
      Frame errorFrame = Frames.createErrorFrame(
          "No subscriptions",
          Headers.create(Frame.DESTINATION, destination),
          "Message delivery failed - no subscriptions on this destination");
      connection.write(errorFrame);
      connection.close();
      return;
    }

    subscriptions.stream().forEach(subscription -> {
          String messageId = UUID.randomUUID().toString();
          Frame message = sendToMessage(frame, subscription, messageId);
          enqueue(connection, subscription, message);
          subscription.connection().write(message.toBuffer());
        }
    );

    Frames.handleReceipt(frame, connection);
  }

  private void enqueue(StompServerConnection connection, Subscription subscription, Frame frame) {
    subscription.enqueue(frame);
    if (connection.server().getOptions().getAckTimeout() != 0) {
      long time = connection.server().getOptions().getAckTimeout() * connection.server().getOptions().getTimeFactor();
      connection.server().vertx().setTimer(time, l -> {
        if (subscription.nack(frame.getHeader(Frame.MESSAGE_ID))) {
          log.warn("Frame not acknowledge in time (" + frame.getHeader(Frame.MESSAGE_ID) + ")");
        }
      });
    }
  }

  public static Frame sendToMessage(Frame frame, Subscription subscription, String messageId) {
    final Headers headers = Headers.create(frame.getHeaders())
        // Destination already set in the input headers.
        .add(Frame.SUBSCRIPTION, subscription.id())
        .add(Frame.MESSAGE_ID, messageId);
    if (!subscription.ackMode().equals("auto")) {
      // We reuse the message Id as ack Id
      headers.add(Frame.ACK, messageId);
    }
    return new Frame(Frame.Command.MESSAGE,
        headers,
        frame.getBody());
  }
}
