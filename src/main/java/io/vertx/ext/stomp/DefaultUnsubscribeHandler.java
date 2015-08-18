package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.utils.Headers;

/**
 * STOMP compliant actions executed when receiving a {@code UNSUBSCRIBE} frame. The associated
 * {@link io.vertx.ext.stomp.Subscription} is unregistered.
 * <p/>
 * This handler is thread safe.
 */
public class DefaultUnsubscribeHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
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
      return;
    }

    Frames.handleReceipt(frame, connection);
  }
}
