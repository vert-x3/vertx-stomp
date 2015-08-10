package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STAMP compliant actions executed when receiving a {@code SUBSCRIBE} frame. It builds a {@link Subscription}
 * instance and registers it.
 */
public class DefaultSubscribeHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    String id = frame.getHeader(Frame.ID);
    String destination = frame.getHeader(Frame.DESTINATION);
    String ack = frame.getHeader(Frame.ACK);
    if (ack == null) {
      ack = "auto";
    }

    if (destination == null || id == null) {
      connection.write(Frames.createErrorFrame(
          "Invalid subscription",
          Headers.create(
              frame.getHeaders()), "The 'destination' and 'id' headers must be set"));
      connection.close();
      return;
    }

    boolean added = connection.handler().subscribe(Subscription.create(connection, destination, ack, id));
    if (!added) {
      connection.write(Frames.createErrorFrame(
          "Invalid subscription",
          Headers.create(frame.getHeaders()), "'id'" +
              " already used by this connection."));
      connection.close();
    }

    Frames.handleReceipt(frame, connection);
  }
}
