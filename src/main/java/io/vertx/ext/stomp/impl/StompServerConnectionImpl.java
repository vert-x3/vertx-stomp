package io.vertx.ext.stomp.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;

import java.util.Objects;
import java.util.UUID;

/**
 * default implementation of the {@link StompServerConnection}.
 */
public class StompServerConnectionImpl implements StompServerConnection {
  private final StompServer server;
  private final NetSocket socket;
  private final String sessionId;

  public StompServerConnectionImpl(NetSocket socket, StompServer server) {
    Objects.requireNonNull(socket);
    Objects.requireNonNull(server);
    this.socket = socket;
    this.server = server;
    this.sessionId = UUID.randomUUID().toString();
  }

  @Override
  public StompServerConnection write(Frame frame) {
    return write(frame.toBuffer());
  }

  @Override
  public StompServerConnection write(Buffer buffer) {
    socket.write(buffer);
    return this;
  }

  @Override
  public StompServer server() {
    return server;
  }

  @Override
  public StompServerHandler handler() {
    return server.stompHandler();
  }

  @Override
  public String session() {
    return sessionId;
  }

  @Override
  public void close() {
    handler().onClose(this);
    socket.close();
  }

  /**
   * Sends a `PING` frame to the client. A `PING` frame is a frame containing only {@code EOL}.
   */
  @Override
  public void ping() {
    socket.write(Buffer.buffer(FrameParser.EOL));
  }


}
