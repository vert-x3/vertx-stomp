/*
 *  Copyright (c) 2011-2015 The original author or authors
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp.impl;

import javax.net.ssl.SSLSession;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.stomp.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default implementation of the {@link StompServerConnection}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerWebSocketConnectionImpl extends StompServerTCPConnectionImpl implements StompServerConnection {

  private final ServerWebSocket socket;

  private final AtomicBoolean closed = new AtomicBoolean(false);

  public StompServerWebSocketConnectionImpl(ServerWebSocket socket, StompServer server, Handler<ServerFrame> writtenFrameHandler) {
    super(server, writtenFrameHandler);
    Objects.requireNonNull(socket);
    this.socket = socket;
  }

  @Override
  public SSLSession sslSession() { return this.socket.sslSession(); }

  @Override
  public StompServerConnection write(Frame frame) {
    return write(frame, server.options().getPayloadMode());
  }

  @Override
  public StompServerConnection write(Frame frame, PayloadMode payloadMode) {
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, this));
    }
    Buffer stompPayload = frame.toBuffer(server.options().isTrailingLine());
    if (payloadMode == PayloadMode.BINARY) {
      return write(stompPayload);
    } else if (payloadMode == PayloadMode.TEXT) {
      return writeText(stompPayload.toString());
    } else {
      return write(stompPayload); // Default
    }
  }

  @Override
  public StompServerConnection write(Buffer buffer) {
    socket.writeBinaryMessage(buffer);
    return this;
  }

  public StompServerConnection writeText(String message) {
    socket.writeTextMessage(message);
    return this;
  }

  @Override
  public void ping() {
    if (handler != null) {
      handler.handle(new ServerFrameImpl(Frames.PING, this));
    }
    socket.write(Buffer.buffer(FrameParser.EOL));
  }

  @Override
  public void close() {
    if (closed.compareAndSet(false, true)) {
      cancelHeartbeat();
      handler().onClose(this);
      socket.close();
    }
  }
}
