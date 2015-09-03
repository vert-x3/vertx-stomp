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
