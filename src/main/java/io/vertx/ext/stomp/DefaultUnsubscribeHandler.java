package io.vertx.ext.stomp;

import io.vertx.core.Handler;
import io.vertx.ext.stomp.utils.Headers;

import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code UNSUBSCRIBE} frame.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
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

    List<Destination> destinations = connection.handler().getDestinations();
    boolean handled = false;
    for (Destination destination : destinations) {
      if (destination.unsubscribe(connection, frame)) {
        handled = true;
        break;
      }
    }

    if (!handled) {
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
