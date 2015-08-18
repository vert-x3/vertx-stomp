package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Transaction} implementation.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
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
  public synchronized boolean addFrameToTransaction(Frame frame) {
    return frames.size() < connection.server().options().getMaxFrameInTransaction() && frames.add(frame);
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
