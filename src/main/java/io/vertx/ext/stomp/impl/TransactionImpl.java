package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Transaction} implementation.
 */
public class TransactionImpl implements Transaction {
  private final List<Frame> frames;
  private final String id;
  private final StompServerConnection connection;

  public TransactionImpl(StompServerConnection connection, String id) {
    this.connection = connection;
    this.id = id;
    this.frames = new ArrayList<>();
  }

  @Override
  public StompServerConnection connection() {
    return connection;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public synchronized Transaction addFrameToTransaction(Frame frame) {
    frames.add(frame);
    return this;
  }

  @Override
  public synchronized Transaction clear() {
    frames.clear();
    return this;
  }

  @Override
  public synchronized List<Frame> getFrames() {
    return new ArrayList<>(frames);
  }
}
