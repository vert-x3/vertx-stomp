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

import io.vertx.core.Completable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.internal.ContextInternal;
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Represents a client connection to a STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompClientConnectionImpl implements StompClientConnection, Handler<Frame> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StompClientConnectionImpl.class);

  private final StompClientOptions options;
  private final NetSocket socket;
  private final ContextInternal context;

  private volatile long lastServerActivity;

  private final Map<String, Promise<Void>> pendingReceipts = new HashMap<>();

  private String version;
  private String sessionId;
  private String server;

  private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();

  private volatile long pinger = -1L;
  private volatile long ponger = -1L;

  private Handler<StompClientConnection> pingHandler = connection -> connection.send(Frames.ping());
  private Handler<StompClientConnection> closeHandler;
  private Handler<StompClientConnection> droppedHandler = v -> {
    // Do nothing by default.
  };

  private Handler<Frame> receivedFrameHandler;
  private Handler<Frame> writingHandler;

  private Handler<Frame> errorHandler;
  private Status status;
  private Handler<Throwable> exceptionHandler;
  private Promise<Void> connectFuture;

  private static class Subscription {
    final String destination;
    final String id;

    final Handler<Frame> handler;

    private Subscription(String destination, String id, Handler<Frame> handler) {
      this.destination = destination;
      this.id = id;
      this.handler = handler;
    }
  }

  /**
   * Creates a {@link StompClientConnectionImpl} instance
   *
   * @param context         the vert.x context
   * @param socket        the underlying TCP socket
   * @param options        the client options
   */
  public StompClientConnectionImpl(ContextInternal context, NetSocket socket, StompClientOptions options) {
    this.socket = socket;
    this.options = options;
    this.context = context;
    this.connectFuture = context.promise();
    this.status = Status.CONNECTING;

    FrameParser parser = new FrameParser();
    parser.handler(this);
    socket
      .shutdownHandler(this::handleShutdown)
      .handler(buffer -> {
      lastServerActivity = System.nanoTime();
      parser.handle(buffer);
    })
      .exceptionHandler(this::handleException)
      .closeHandler(v -> {
        boolean dropped = status == Status.CONNECTED;
        status = Status.CLOSED;
        handleClose();
        Handler<StompClientConnection> handler = droppedHandler;
        if (dropped && handler != null) {
          context.emit(this, handler);
        }
      });
  }

  public Future<Void> connectFuture() {
    return connectFuture.future();
  }

  @Override
  public boolean isConnected() {
    return status == Status.CONNECTED;
  }

  @Override
  public synchronized String session() {
    return sessionId;
  }

  @Override
  public synchronized String version() {
    return version;
  }

  @Override
  public void close() {
    synchronized (this) {
      if (status == Status.CLOSING || status == Status.CLOSED) {
        return;
      }
      status = Status.CLOSING;
    }
    socket.close();
  }

  private void handleShutdown(Duration ignore) {
    synchronized (this) {
      if (status == Status.CONNECTED) {
        disconnect();
      }
      socket.close();
    }
  }

  private void handleException(Throwable ex) {
    Handler<Throwable> handler;
    synchronized (this) {
      if (status != Status.CONNECTED) {
        return;
      }
      handler = exceptionHandler;
    }
    if (handler != null) {
      handler.handle(ex);
    }
  }

  private void handleClose() {
    if (pinger != -1) {
      context.owner().cancelTimer(pinger);
      pinger = -1;
    }

    if (ponger != -1) {
      context.owner().cancelTimer(ponger);
      ponger = -1;
    }

    Collection<Promise<Void>> values = new ArrayList<>(pendingReceipts.values());
    pendingReceipts.clear();
    for (Promise<Void> promise : values) {
      promise.fail("Client closed");
    }

    subscriptions.clear();
    server = null;
    sessionId = null;
    version = null;

    connectFuture.tryFail("Connection closed");

    if (closeHandler != null) {
      context.emit(this, closeHandler);
    }
  }


  @Override
  public synchronized String server() {
    return server;
  }

  @Override
  public Future<Frame> send(Map<String, String> headers, Buffer body) {
    return send(null, headers, body);
  }

  @Override
  public Future<Frame> send(String destination, Buffer body) {
    return send(destination, null, body);
  }

  @Override
  public Future<Frame> send(Frame frame) {
    Promise<Frame> promise = Promise.promise();
    send(frame, promise);
    return promise.future();
  }

  public synchronized StompClientConnection send(Frame frame, Completable<Frame> receiptHandler) {
    if (receiptHandler != null && frame.getCommand() != Command.PING) {
      String receiptId = UUID.randomUUID().toString();
      frame.addHeader(Frame.RECEIPT, receiptId);
      Promise<Void> promise = Promise.promise();
      promise.future().onComplete(f -> {
        if (f.succeeded()) {
          receiptHandler.succeed(frame);
        } else {
          receiptHandler.fail(f.cause());
        }
      });
      pendingReceipts.put(receiptId, promise);
    }
    if (writingHandler != null) {
      writingHandler.handle(frame);
    }
    Future<Void> written = socket.write(frame.toBuffer(options.isTrailingLine()));
    if (receiptHandler != null && frame.getCommand() == Command.PING) {
      written
        .map(frame)
        .onComplete(receiptHandler);
    }
    return this;
  }

  @Override
  public Future<Frame> send(String destination, Map<String, String> headers, Buffer body) {
    Promise<Frame> promise = Promise.promise();
    send(destination, headers, body, promise);
    return promise.future();
  }

  public StompClientConnection send(String destination, Map<String, String> headers, Buffer body,
                                    Completable<Frame> receiptHandler) {
    // No need for synchronization, no field access, except client (final)
    if (headers == null) {
      headers = new Headers();
    }
    if (destination != null) {
      headers.put(Frame.DESTINATION, destination);
    }
    // At that point, the 'destination' header must be set.
    if (headers.get(Frame.DESTINATION) == null) {
      throw new IllegalArgumentException("The 'destination' header is mandatory : " + headers);
    }

    if (body != null
      && options.isAutoComputeContentLength()
      && !headers.containsKey(Frame.CONTENT_LENGTH)) {
      headers.put(Frame.CONTENT_LENGTH, Integer.toString(body.length()));
    }

    Frame frame = new Frame(Command.SEND, headers, body);
    return send(frame, receiptHandler);
  }

  @Override
  public Future<String> subscribe(String destination, Handler<Frame> handler) {
    Promise<String> promise = Promise.promise();
    subscribe(destination, (Map<String, String>) null, handler, promise);
    return promise.future();
  }

  public StompClientConnection subscribe(String destination, Handler<Frame> handler, Completable<String> receiptHandler) {
    return subscribe(destination, null, handler, receiptHandler);
  }

  @Override
  public Future<String> subscribe(String destination, Map<String, String> headers, Handler<Frame> handler) {
    Promise<String> promise = Promise.promise();
    subscribe(destination, headers, handler, promise);
    return promise.future();
  }

  public synchronized StompClientConnection subscribe(String destination, Map<String, String> headers, Handler<Frame> handler, Completable<String> receiptHandler) {
    Objects.requireNonNull(destination);
    Objects.requireNonNull(handler);

    if (headers == null) {
      headers = Headers.create();
    }

    String id = headers.getOrDefault(Frame.ID, destination);

    final Optional<Subscription> maybeSubscription = subscriptions.stream()
      .filter(s -> s.id.equals(id)).findFirst();

    if (maybeSubscription.isPresent()) {
      throw new IllegalArgumentException("The client is already registered  to " + destination);
    }

    subscriptions.add(new Subscription(destination, id, handler));

    headers.put(Frame.DESTINATION, destination);

    if (!headers.containsKey(Frame.ID)) {
      headers.put(Frame.ID, id);
    }

    Frame frame = new Frame(Command.SUBSCRIBE, headers, null);
    send(frame, (res, err) -> {
      if (receiptHandler != null) {
        if (err != null) {
          receiptHandler.fail(err);
        } else {
          receiptHandler.succeed(id);
        }
      }
    });

    return this;
  }

  @Override
  public Future<Frame> unsubscribe(String destination) {
    Promise<Frame> promise = Promise.promise();
    unsubscribe(destination, null, promise);
    return promise.future();
  }

  @Override
  public Future<Frame> unsubscribe(String destination, Map<String, String> headers) {
    Promise<Frame> promise = Promise.promise();
    unsubscribe(destination, headers, promise);
    return promise.future();
  }

  public synchronized StompClientConnection unsubscribe(String destination, Map<String, String> headers, Completable<Frame>
    receiptHandler) {
    Objects.requireNonNull(destination);
    if (headers == null) {
      headers = Headers.create();
    }
    String id = headers.containsKey(Frame.ID) ? headers.get(Frame.ID) : destination;
    headers.put(Frame.ID, id);

    final Optional<Subscription> maybeSubscription = subscriptions.stream()
      .filter(s -> s.id.equals(id)).findFirst();

    if (maybeSubscription.isPresent()) {
      final Subscription subscription = maybeSubscription.get();
      subscriptions.remove(subscription);
      send(new Frame(Command.UNSUBSCRIBE, headers, null), receiptHandler);
      return this;
    } else {
      throw new IllegalArgumentException("No subscription with id " + id);
    }
  }

  @Override
  public synchronized StompClientConnection errorHandler(Handler<Frame> handler) {
    this.errorHandler = handler;
    return this;
  }

  @Override
  public synchronized StompClientConnection closeHandler(Handler<StompClientConnection> handler) {
    this.closeHandler = handler;
    return this;
  }

  @Override
  public synchronized StompClientConnection pingHandler(Handler<StompClientConnection> handler) {
    this.pingHandler = handler;
    return this;
  }

  public StompClientConnection beginTX(String id, Completable<Frame> receiptHandler) {
    return beginTX(id, new Headers(), receiptHandler);
  }

  @Override
  public Future<Frame> beginTX(String id) {
    return beginTX(id, new Headers());
  }

  @Override
  public Future<Frame> beginTX(String id, Map<String, String> headers) {
    Promise<Frame> promise = Promise.promise();
    beginTX(id, headers, promise);
    return promise.future();
  }

  public StompClientConnection beginTX(String id, Map<String, String> headers, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);

    return send(new Frame().setCommand(Command.BEGIN).setTransaction(id), receiptHandler);
  }

  @Override
  public Future<Frame> commit(String id) {
    return commit(id, new Headers());
  }

  public StompClientConnection commit(String id, Completable<Frame> receiptHandler) {
    return commit(id, new Headers(), receiptHandler);
  }

  @Override
  public Future<Frame> commit(String id, Map<String, String> headers) {
    Promise<Frame> promise = Promise.promise();
    commit(id, headers, promise);
    return promise.future();
  }

  public StompClientConnection commit(String id, Map<String, String> headers, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);
    return send(new Frame().setCommand(Command.COMMIT).setTransaction(id), receiptHandler);
  }

  @Override
  public Future<Frame> abort(String id) {
    return abort(id, new Headers());
  }

  public StompClientConnection abort(String id, Completable<Frame> receiptHandler) {
    return abort(id, new Headers(), receiptHandler);
  }

  @Override
  public Future<Frame> abort(String id, Map<String, String> headers) {
    Promise<Frame> promise = Promise.promise();
    abort(id, headers, promise);
    return promise.future();
  }

  public StompClientConnection abort(String id, Map<String, String> headers, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);
    return send(new Frame().setCommand(Command.ABORT).setTransaction(id), receiptHandler);
  }

  @Override
  public Future<Frame> disconnect() {
    Promise<Frame> promise = Promise.promise();
    disconnect(new Frame().setCommand(Command.DISCONNECT), promise);
    return promise.future();
  }

  @Override
  public Future<Frame> disconnect(Frame frame) {
    Promise<Frame> promise = Promise.promise();
    disconnect(frame, promise);
    return promise.future();
  }

  public StompClientConnection disconnect(Completable<Frame> receiptHandler) {
    return disconnect(new Frame().setCommand(Command.DISCONNECT), receiptHandler);
  }

  public StompClientConnection disconnect(Frame frame, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(frame);
    synchronized (this) {
      if (status == Status.CONNECTED) {
        status = Status.CLOSING;
        send(frame, (res, err) -> {
          if (receiptHandler != null) {
            receiptHandler.complete(res, err);
          }
          // Close once the receipt have been received.
          socket.close();
        });
      } else {
        receiptHandler.fail("Not connected");
      }
    }
    return this;
  }

  @Override
  public Future<Frame> ack(String id) {
    Promise<Frame> promise = Promise.promise();
    ack(id, promise);
    return promise.future();
  }

  public StompClientConnection ack(String id, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    send(new Frame(Command.ACK, Headers.create(Frame.ID, id), null), receiptHandler);
    return this;
  }

  public Future<Frame> nack(String id) {
    Promise<Frame> promise = Promise.promise();
    nack(id, promise);
    return promise.future();
  }

  public StompClientConnection nack(String id, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    send(new Frame(Command.NACK, Headers.create(Frame.ID, id), null), receiptHandler);
    return this;
  }

  @Override
  public Future<Frame> ack(String id, String txId) {
    Promise<Frame> promise = Promise.promise();
    ack(id, txId, promise);
    return promise.future();
  }

  public StompClientConnection ack(String id, String txId, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id, "A ACK frame must contain the ACK id");
    Objects.requireNonNull(txId);

    send(new Frame(Command.ACK, Headers.create(Frame.ID, id, Frame.TRANSACTION, txId), null), receiptHandler);

    return this;
  }

  @Override
  public Future<Frame> nack(String id, String txId) {
    Promise<Frame> promise = Promise.promise();
    nack(id, txId, promise);
    return promise.future();
  }

  public StompClientConnection nack(String id, String txId, Completable<Frame> receiptHandler) {
    Objects.requireNonNull(id, "A NACK frame must contain the ACK id");
    Objects.requireNonNull(txId);

    Frame toSend = new Frame(Command.NACK, Headers.create(Frame.ID, id, Frame.TRANSACTION, txId), null);
    send(toSend, receiptHandler);
    return this;
  }

  @Override
  public synchronized StompClientConnection receivedFrameHandler(Handler<Frame> handler) {
    this.receivedFrameHandler = handler;
    return this;
  }

  @Override
  public synchronized StompClientConnection writingFrameHandler(Handler<Frame> handler) {
    this.writingHandler = handler;
    return this;
  }

  @Override
  public synchronized StompClientConnection exceptionHandler(Handler<Throwable> exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    return this;
  }

  @Override
  public synchronized StompClientConnection connectionDroppedHandler(Handler<StompClientConnection> handler) {
    this.droppedHandler = handler;
    return this;
  }

  @Override
  public void handle(Frame frame) {
    synchronized (this) {
      if (receivedFrameHandler != null) {
        receivedFrameHandler.handle(frame);
      }
    }
    switch (frame.getCommand()) {
      case CONNECTED:
        handleConnected(frame);
        break;
      case RECEIPT:
        handleReceipt(frame);
        break;
      case MESSAGE:
        String id = frame.getHeader(Frame.SUBSCRIPTION);
        subscriptions.stream()
          .filter(s -> s.id.equals(id)).forEach(s -> s.handler.handle(frame));
        break;
      case ERROR:
        if (errorHandler != null) {
          errorHandler.handle(frame);
        }
        break;
      case PING:
        // Do nothing.
        break;
    }
  }

  private synchronized void handleReceipt(Frame frame) {
    String receipt = frame.getHeader(Frame.RECEIPT_ID);
    if (receipt != null) {
      Promise<Void> receiptHandler = pendingReceipts.remove(receipt);
      if (receiptHandler == null) {
        throw new IllegalStateException("No receipt handler for receipt " + receipt);
      }
      receiptHandler.complete();
    }
  }

  private synchronized void handleConnected(Frame frame) {
    sessionId = frame.getHeader(Frame.SESSION);
    version = frame.getHeader(Frame.VERSION);
    server = frame.getHeader(Frame.SERVER);

    // Compute the heartbeat.
    // Stomp client acts as a client to call the computePingPeriod & computePongPeriod method
    long ping = Frame.Heartbeat.computePingPeriod(
      Frame.Heartbeat.create(options.getHeartbeat()),
      Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));
    long pong = Frame.Heartbeat.computePongPeriod(
      Frame.Heartbeat.create(options.getHeartbeat()),
      Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));

    if (ping > 0) {
      pinger = context.setPeriodic(ping, l -> pingHandler.handle(this));
    }
    if (pong > 0) {
      ponger = context.setPeriodic(pong, l -> {
        long delta = System.nanoTime() - lastServerActivity;
        final long deltaInMs = TimeUnit.MILLISECONDS.convert(delta, TimeUnit.NANOSECONDS);
        if (deltaInMs > pong * 2) {
          LOGGER.error("Disconnecting client - no server activity detected in the last " + deltaInMs + " ms.");
          context.owner().cancelTimer(ponger);

          // Do not send disconnect here, just close the connection.
          // The server will detect the disconnection using its own heartbeat.
          close();

          // Stack confinement, guarded by the parent class monitor lock.
          Handler<StompClientConnection> handler;
          synchronized (StompClientConnectionImpl.this) {
            handler = droppedHandler;
          }

          if (handler != null) {
            handler.handle(this);
          }
        }
      });
    }
    // Switch the exception handler.
    socket.exceptionHandler(this.exceptionHandler);
    status = Status.CONNECTED;
    connectFuture.tryComplete();
  }

  /**
   * Gets the underlying TCP socket.
   *
   * @return the socket
   */
  public NetSocket socket() {
    return socket;
  }

  private enum Status {
    CONNECTING,
    CONNECTED,
    CLOSING,
    CLOSED
  }

}
