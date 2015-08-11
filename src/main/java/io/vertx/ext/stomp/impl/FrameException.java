package io.vertx.ext.stomp.impl;

/**
 * Exception thrown when a STOMP frame is not structured correctly or does nto obey to the specification.
 *
 * This class is thread safe.
 */
public class FrameException extends RuntimeException {

  public FrameException(String message) {
    super(message);
  }


}
