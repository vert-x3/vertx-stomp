package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Frames;
import io.vertx.ext.stomp.ServerFrameHandler;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STAMP compliant actions executed when receiving a {@code UNSUBSCRIBE} frame. The associated
 * {@link io.vertx.ext.stomp.Subscription} is unregistered.
 */
public class DefaultUnsubscribeHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    final String id = frame.getHeader(Frame.ID);

    if (id == null) {
      connection.write(
          Frames.createErrorFrame(
              "Invalid unsubscribe",
              Headers.create(frame.getHeaders()),
              "The 'id' header must be set"));
      connection.close();
      return;
    }

    if (!connection.handler().unsubscribe(connection, id)) {
      connection.write(Frames.createErrorFrame(
          "Invalid unsubscribe",
          Headers.create(frame.getHeaders()),
          "No subscription associated with the given 'id'"));
      connection.close();
    }

    Frames.handleReceipt(frame, connection);
  }
}
