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
import io.vertx.ext.stomp.StompServerConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a STOMP transaction.
 * This class is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Transaction {
  private final List<Frame> frames;
  private final String id;
  private final StompServerConnection connection;

  public Transaction(StompServerConnection connection, String id) {
    this.connection = connection;
    this.id = id;
    this.frames = new ArrayList<>();
  }

  /**
   * @return the connection
   */
  public StompServerConnection connection() {
    return connection;
  }

  /**
   * @return the transaction id
   */
  public String id() {
    return id;
  }

  /**
   * Adds a frame to the transaction. As stated in the STOMP specification, only {@code SEND, ACK and NACK} frames
   * can be in transactions.
   *
   * @param frame the frame to add
   * @return {@code true} if the frame was added to the transaction, {@code false otherwise}. Main failure reason is the number of
   * frames stored in the transaction that have exceed the number of allowed frames in transaction.
   */
  public synchronized boolean addFrameToTransaction(Frame frame) {
    return frames.size() < connection.server().options().getMaxFrameInTransaction() && frames.add(frame);
  }

  /**
   * Clears the list of frames added to the transaction.
   *
   * @return the current {@link Transaction}
   */
  public synchronized Transaction clear() {
    frames.clear();
    return this;
  }

  /**
   * @return the ordered list of frames added to the transaction. To avoid concurrency issue, a copy is returned.
   */
  public synchronized List<Frame> getFrames() {
    return new ArrayList<>(frames);
  }
}
