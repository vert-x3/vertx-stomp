package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.ext.stomp.StompServerConnection;

import java.util.Objects;

/**
 * A basic implementation of {@link ServerFrame}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServerFrameImpl implements ServerFrame {
  private final StompServerConnection connection;
  private final Frame frame;

  public ServerFrameImpl(Frame frame, StompServerConnection connection) {
    Objects.requireNonNull(connection);
    Objects.requireNonNull(frame);
    this.connection = connection;
    this.frame = frame;
  }


  /**
   * @return the received frame
   */
  @Override
  public Frame frame() {
    return frame;
  }

  /**
   * @return the connection
   */
  @Override
  public StompServerConnection connection() {
    return connection;
  }
}
