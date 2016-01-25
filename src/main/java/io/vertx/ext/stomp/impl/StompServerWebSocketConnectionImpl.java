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

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of the {@link StompServerConnection}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerWebSocketConnectionImpl extends  StompServerTCPConnectionImpl implements StompServerConnection {

  private final ServerWebSocket socket;

  public StompServerWebSocketConnectionImpl(ServerWebSocket socket, StompServer server) {
    super(server);
    Objects.requireNonNull(socket);
    this.socket = socket;
  }

  @Override
  public StompServerConnection write(Buffer buffer) {
    socket.write(buffer);
    return this;
  }

  @Override
  public void close() {
    cancelHeartbeat();
    handler().onClose(this);
    try {
      socket.close();
    } catch (IllegalStateException e) {
      // Ignore it, the web socket has already been closed.
    }
  }
}
