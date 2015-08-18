package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code SUBSCRIBE} frame. It builds a {@link Subscription}
 * instance and registers it.
 *
 * This handler is thread safe.
 */
public class DefaultSubscribeHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
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
      return;
    }

    Frames.handleReceipt(frame, connection);
  }
}
