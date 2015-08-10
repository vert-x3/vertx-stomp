package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Handler called by {@link io.vertx.ext.stomp.impl.FrameParser} when a STOMP frame has been parsed. STOMP client and
 * server use specialized versions passing the associated STOMP connection.
 *
 * @see ClientFrameHandler
 * @see ServerFrameHandler
 */
@VertxGen
public interface FrameHandler {
  void onFrame(Frame frame);
}
