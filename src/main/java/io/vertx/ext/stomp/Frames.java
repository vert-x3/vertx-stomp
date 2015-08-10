package io.vertx.ext.stomp;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.impl.FrameException;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;

import java.util.Map;
import java.util.Objects;

/**
 * Utility methods to build common {@link Frame}s. It defines a non-STOMP frame ({@code PING}) that is used for
 * heartbeats. When such frame is written on the wire it is just the {@code 0} byte.
 */
public class Frames {

  private static final Frame PING = new Frame(Frame.Command.PING, Headers.create(), null) {

    @Override
    public Buffer toBuffer() {
      return Buffer.buffer(FrameParser.EOL);
    }
  };

  private Frames() {
    // Avoid direct instantiation.
  }

  public static Frame createErrorFrame(String message, Map<String, String> headers, String body) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(headers);
    Objects.requireNonNull(body);
    return new Frame(Frame.Command.ERROR,
        Headers.create(headers)
            .add(Frame.MESSAGE, message)
            .add(Frame.CONTENT_LENGTH, Integer.toString(body.length()))
            .add(Frame.CONTENT_TYPE, "test/plain"),
        Buffer.buffer(body));
  }

  public static Frame createInvalidFrameErrorFrame(FrameException exception) {
    return createErrorFrame("Invalid frame received", Headers.create(), exception.getMessage());
  }

  public static Frame createReceiptFrame(String receiptId, Map<String, String> headers) {
    Objects.requireNonNull(receiptId);
    Objects.requireNonNull(headers);
    return new Frame(Frame.Command.RECEIPT,
        Headers.create(headers)
            .add(Frame.RECEIPT_ID, receiptId),
        null);
  }

  public static void handleReceipt(Frame frame, StompServerConnection connection) {
    String receipt = frame.getReceipt();
    if (receipt != null) {
      connection.write(createReceiptFrame(receipt, Headers.create()));
    }
  }

  public static Frame ping() {
    return PING;
  }
}
