package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Handles a client frame. This type of handler are called when the client receives a STOMP frame and let implement
 * the behavior.
 */
@VertxGen
public interface ClientFrameHandler {

  /**
   * Handler called when a client frame has been received.
   *
   * @param frame      the frame
   * @param connection the client connection that has received the frame
   */
  void onFrame(Frame frame, StompClientConnection connection);

}
