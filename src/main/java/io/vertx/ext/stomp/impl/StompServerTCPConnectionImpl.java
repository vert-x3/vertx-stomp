/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
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
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.*;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of the {@link StompServerConnection}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerTCPConnectionImpl implements StompServerConnection {

  private static final Logger log = LoggerFactory.getLogger(StompServerTCPConnectionImpl.class);

  private final StompServer server;
  private final NetSocket socket;
  private final String sessionId;
  protected final Handler<ServerFrame> handler;

  public volatile long lastClientActivity;
  private long pinger = -1;
  private long ponger = -1;

  private boolean closed;

  public StompServerTCPConnectionImpl(NetSocket socket, StompServer server, Handler<ServerFrame> writingFrameHandler) {
    Objects.requireNonNull(socket);
    Objects.requireNonNull(server);
    this.socket = socket;
    this.server = server;
    this.sessionId = UUID.randomUUID().toString();
    this.handler = writingFrameHandler;
  }

  public StompServerTCPConnectionImpl(StompServer server, Handler<ServerFrame> writingFrameHandler) {
    Objects.requireNonNull(server);
    this.socket = null;
    this.server = server;
    this.handler = writingFrameHandler;
    this.sessionId = UUID.randomUUID().toString();
  }

  @Override
  public StompServerConnection write(Frame frame) {
    if (handler != null) {
      handler.handle(new ServerFrameImpl(frame, this));
    }
    return write(frame.toBuffer(server.options().isTrailingLine()));
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
  public SSLSession sslSession() {
    return this.socket.sslSession();
  }

  @Override
  public void close() {
    cancelHeartbeat();
    synchronized (this) {
      if (!closed) {
        handler().onClose(this);
        closed = true;
      }
    }
    socket.close();
  }

  /**
   * Sends a `PING` frame to the client. A `PING` frame is a frame containing only {@code EOL}.
   */
  @Override
  public void ping() {
    if (handler != null) {
      handler.handle(new ServerFrameImpl(Frames.PING, this));
    }
    socket.write(Buffer.buffer(FrameParser.EOL));
  }

  public synchronized void cancelHeartbeat() {
    if (pinger >= 0) {
      server.vertx().cancelTimer(pinger);
      pinger = -1;
    }

    if (ponger >= 0) {
      server.vertx().cancelTimer(ponger);
      ponger = -1;
    }
  }

  @Override
  public void onServerActivity() {
    lastClientActivity = System.nanoTime();
  }

  @Override
  public synchronized void configureHeartbeat(long ping, long pong, Handler<StompServerConnection> pingHandler) {
    if (ping > 0) {
      pinger = server.vertx().setPeriodic(ping, l -> pingHandler.handle(this));
    }
    if (pong > 0) {
      ponger = server.vertx().setPeriodic(pong, l -> {
        long delta = System.nanoTime() - lastClientActivity;
        final long deltaInMs = TimeUnit.MILLISECONDS.convert(delta, TimeUnit.NANOSECONDS);
        if (deltaInMs > pong * 2) {
          log.warn("Disconnecting client " + this + " - no client activity in the last " + deltaInMs + " ms");
          close();
        }
      });
    }
  }

}
