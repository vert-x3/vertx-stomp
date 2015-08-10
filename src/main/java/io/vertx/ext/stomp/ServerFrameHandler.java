package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Handles a server frame. This type of handler are called when the server receives a STOMP frame and let implement
 * the behavior.
 */
@VertxGen
public interface ServerFrameHandler {

  /**
   * Handler called when a server frame has been received.
   *
   * @param frame      the frame
   * @param connection the server connection that has received the frame
   */
  void onFrame(Frame frame, StompServerConnection connection);

}
