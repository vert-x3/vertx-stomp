package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Frames;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientConnection;
import io.vertx.ext.stomp.utils.Headers;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Represents a client connection to a STOMP server.
 */
public class StompClientConnectionImpl implements StompClientConnection, Handler<Frame> {
  private static final Logger log = LoggerFactory.getLogger(StompClientConnectionImpl.class);

  private final StompClient client;
  private final NetSocket socket;
  private final Handler<AsyncResult<StompClientConnection>> resultHandler;

  private volatile long lastServerActivity;

  private final Map<String, Handler<Void>> pendingReceipts = new HashMap<>();

  private String version;
  private String sessionId;
  private String server;

  private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();

  private volatile long pinger;
  private volatile long ponger;

  private Handler<StompClientConnection> pingHandler = connection -> connection.send(Frames.ping());
  private Handler<StompClientConnection> closeHandler;

  private class Subscription {
    final String destination;
    final String id;
    final Handler<Frame> handler;

    private Subscription(String destination, String id, Handler<Frame> handler) {
      this.destination = destination;
      this.id = id;
      this.handler = handler;
    }
  }


  private Handler<Frame> errorHandler;

  public StompClientConnectionImpl(NetSocket socket, StompClient client, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    this.socket = socket;
    this.client = client;
    this.resultHandler = resultHandler;

    FrameParser parser = new FrameParser();
    parser.handler(this);
    socket.handler(buffer -> {
      lastServerActivity = System.currentTimeMillis();
      parser.handle(buffer);
    })
        .closeHandler(v -> close());

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
  public synchronized void close() {

    if (closeHandler != null) {
      closeHandler.handle(this);
    }

    if (pinger != 0) {
      client.vertx().cancelTimer(pinger);
      pinger = 0;
    }

    if (ponger != 0) {
      client.vertx().cancelTimer(ponger);
      ponger = 0;
    }

    socket.close();
    client.close();
    pendingReceipts.clear();
    subscriptions.clear();
    server = null;
    sessionId = null;
    version = null;
  }

  @Override
  public synchronized String server() {
    return server;
  }

  @Override
  public StompClientConnection send(Map<String, String> headers, Buffer body) {
    return send(null, headers, body);
  }

  @Override
  public StompClientConnection send(Map<String, String> headers, Buffer body, Handler<Frame> receiptHandler) {
    return send(null, headers, body, receiptHandler);
  }

  @Override
  public StompClientConnection send(String destination, Buffer body) {
    return send(destination, null, body);
  }

  @Override
  public StompClientConnection send(String destination, Buffer body, Handler<Frame> receiptHandler) {
    return send(destination, null, body, receiptHandler);
  }

  @Override
  public StompClientConnection send(Frame frame) {
    return send(frame, null);
  }

  @Override
  public synchronized StompClientConnection send(Frame frame, Handler<Frame> receiptHandler) {
    if (receiptHandler != null) {
      String receiptId = UUID.randomUUID().toString();
      frame.addHeader(Frame.RECEIPT, receiptId);
      pendingReceipts.put(receiptId, f -> receiptHandler.handle(frame));
    }
    socket.write(frame.toBuffer());
    return this;
  }

  @Override
  public StompClientConnection send(String destination, Map<String, String> headers, Buffer body) {
    return send(destination, headers, body, null);
  }

  @Override
  public StompClientConnection send(String destination, Map<String, String> headers, Buffer body,
                                    Handler<Frame> receiptHandler) {
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
        && client.options().isAutoComputeContentLength()
        && !headers.containsKey(Frame.CONTENT_LENGTH)) {
      headers.put(Frame.CONTENT_LENGTH, Integer.toString(body.length()));
    }

    Frame frame = new Frame(Frame.Command.SEND, headers, body);
    return send(frame, receiptHandler);
  }

  @Override
  public String subscribe(String destination, Handler<Frame> handler) {
    return subscribe(destination, (Map<String, String>) null, handler);
  }

  @Override
  public String subscribe(String destination, Handler<Frame> handler, Handler<Frame> receiptHandler) {
    return subscribe(destination, null, handler, receiptHandler);
  }

  @Override
  public String subscribe(String destination, Map<String, String> headers, Handler<Frame> handler) {
    return subscribe(destination, headers, handler, null);
  }

  @Override
  public synchronized String subscribe(String destination, Map<String, String> headers, Handler<Frame> handler, Handler<Frame>
      receiptHandler) {
    Objects.requireNonNull(destination);
    Objects.requireNonNull(handler);

    if (headers == null) {
      headers = Headers.create();
    }

    String id = headers.containsKey(Frame.ID) ? headers.get(Frame.ID) : destination;

    final Optional<Subscription> maybeSubscription = subscriptions.stream()
        .filter(s -> s.id.equals(id)).findFirst();

    if (maybeSubscription.isPresent()) {
      throw new IllegalArgumentException("The client is already registered  to " + destination);
    }

    subscriptions.add(new Subscription(destination, id, handler));

    headers.put(Frame.DESTINATION, destination);
    // Spec says that by default ack is 'auto'. The server should handle it as auto if none passed.
    if (!headers.containsKey(Frame.ACK)) {
      headers.put(Frame.ACK, "auto");
    }

    if (!headers.containsKey(Frame.ID)) {
      headers.put(Frame.ID, id);
    }

    Frame frame = new Frame(Frame.Command.SUBSCRIBE, headers, null);
    send(frame, receiptHandler);
    return id;
  }

  @Override
  public StompClientConnection unsubscribe(String destination) {
    return unsubscribe(destination, null, null);
  }

  @Override
  public StompClientConnection unsubscribe(String destination, Handler<Frame> receiptHandler) {
    return unsubscribe(destination, null, receiptHandler);
  }

  @Override
  public StompClientConnection unsubscribe(String destination, Map<String, String> headers) {
    return unsubscribe(destination, headers, null);
  }

  @Override
  public synchronized StompClientConnection unsubscribe(String destination, Map<String, String> headers, Handler<Frame>
      receiptHandler) {
    Objects.requireNonNull(destination);
    if (headers == null) {
      headers = Headers.create();
    }
    String id = headers.containsKey(Frame.ID) ? headers.get(Frame.ID) : destination;
    headers.put(Frame.ID, id);

    final Optional<Subscription> maybeSubscription = subscriptions.stream()
        .filter(s -> s.id.equals(id)).findFirst();

    if (!maybeSubscription.isPresent()) {
      throw new IllegalArgumentException("No subscription with id " + id);
    }
    final Subscription subscription = maybeSubscription.get();
    subscriptions.remove(subscription);

    send(new Frame(Frame.Command.UNSUBSCRIBE, headers, null), receiptHandler);
    return this;
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

  @Override
  public StompClientConnection beginTX(String id, Handler<Frame> receiptHandler) {
    return beginTX(id, new Headers(), receiptHandler);
  }

  @Override
  public StompClientConnection beginTX(String id) {
    return beginTX(id, new Headers());
  }

  @Override
  public StompClientConnection beginTX(String id, Map<String, String> headers) {
    return beginTX(id, headers, null);
  }

  @Override
  public StompClientConnection beginTX(String id, Map<String, String> headers, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);

    return send(new Frame().setCommand(Frame.Command.BEGIN).setTransaction(id), receiptHandler);
  }

  @Override
  public StompClientConnection commit(String id) {
    return commit(id, new Headers());
  }

  @Override
  public StompClientConnection commit(String id, Handler<Frame> receiptHandler) {
    return commit(id, new Headers(), receiptHandler);
  }

  @Override
  public StompClientConnection commit(String id, Map<String, String> headers) {
    return commit(id, headers, null);
  }

  @Override
  public StompClientConnection commit(String id, Map<String, String> headers, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);
    return send(new Frame().setCommand(Frame.Command.COMMIT).setTransaction(id), receiptHandler);
  }

  @Override
  public StompClientConnection abort(String id) {
    return abort(id, new Headers());
  }

  @Override
  public StompClientConnection abort(String id, Handler<Frame> receiptHandler) {
    return abort(id, new Headers(), receiptHandler);
  }

  @Override
  public StompClientConnection abort(String id, Map<String, String> headers) {
    return abort(id, headers, null);
  }

  @Override
  public StompClientConnection abort(String id, Map<String, String> headers, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(headers);
    return send(new Frame().setCommand(Frame.Command.ABORT).setTransaction(id), receiptHandler);
  }

  @Override
  public StompClientConnection disconnect() {
    return disconnect(new Frame().setCommand(Frame.Command.DISCONNECT), null);
  }

  @Override
  public StompClientConnection disconnect(Frame frame) {
    return disconnect(frame, null);
  }

  @Override
  public StompClientConnection disconnect(Handler<Frame> receiptHandler) {
    return disconnect(new Frame().setCommand(Frame.Command.DISCONNECT), receiptHandler);
  }

  @Override
  public StompClientConnection disconnect(Frame frame, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(frame);
    send(frame, f -> {
      if (receiptHandler != null) {
        receiptHandler.handle(f);
      }
      // Close once the receipt have been received.
      close();
    });
    return this;
  }

  @Override
  public StompClientConnection ack(Frame frame) {
    return ack(frame.getAck(), null);
  }

  @Override
  public StompClientConnection ack(Frame frame, Handler<Frame> receiptHandler) {
    return ack(frame.getAck(), receiptHandler);
  }

  @Override
  public StompClientConnection ack(String id) {
    return ack(id, null);
  }

  @Override
  public StompClientConnection ack(String id, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    send(new Frame(Frame.Command.ACK, Headers.create(Frame.ID, id), null), receiptHandler);
    return this;
  }

  @Override
  public StompClientConnection nack(Frame frame) {
    return nack(frame.getAck());
  }

  @Override
  public StompClientConnection nack(Frame frame, Handler<Frame> receiptHandler) {
    return nack(frame.getAck(), receiptHandler);
  }

  @Override
  public StompClientConnection nack(String id) {
    return nack(id, null);
  }

  @Override
  public StompClientConnection nack(String id, Handler<Frame> receiptHandler) {
    Objects.requireNonNull(id);
    send(new Frame(Frame.Command.NACK, Headers.create(Frame.ID, id), null), receiptHandler);
    return this;
  }

  @Override
  public StompClientConnection ack(Frame frame, String txId) {
    return ack(frame, txId, null);
  }

  @Override
  public StompClientConnection ack(Frame frame, String txId, Handler<Frame> receiptHandler) {
    final String id = frame.getAck();
    Objects.requireNonNull(id, "A ACK frame must contain the ACK id");
    Objects.requireNonNull(txId);

    send(new Frame(Frame.Command.ACK, Headers.create(Frame.ID, id, Frame.TRANSACTION, txId), null), receiptHandler);

    return this;
  }

  @Override
  public StompClientConnection nack(Frame frame, String txId) {
    return nack(frame, txId, null);
  }

  @Override
  public StompClientConnection nack(Frame frame, String txId, Handler<Frame> receiptHandler) {
    final String id = frame.getAck();
    Objects.requireNonNull(id, "A NACK frame must contain the ACK id");
    Objects.requireNonNull(txId);

    Frame toSend = new Frame(Frame.Command.NACK, Headers.create(Frame.ID, id, Frame.TRANSACTION, txId), null);
    send(toSend, receiptHandler);
    return this;
  }

  @Override
  public void handle(Frame frame) {
    switch (frame.getCommand()) {
      case CONNECTED:
        handleConnected(frame);
        break;
      case RECEIPT:
        handleReceipt(frame);
        break;
      case MESSAGE:
        String destination = frame.getHeader(Frame.DESTINATION);
        subscriptions.stream()
            .filter(s -> s.destination.equals(destination)).forEach(s -> s.handler.handle(frame));
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
      Handler<Void> receiptHandler = pendingReceipts.remove(receipt);
      if (receiptHandler == null) {
        throw new IllegalStateException("No receipt handler for receipt " + receipt);
      }
      receiptHandler.handle(null);
    }
  }

  private synchronized void handleConnected(Frame frame) {
    sessionId = frame.getHeader(Frame.SESSION);
    version = frame.getHeader(Frame.VERSION);
    server = frame.getHeader(Frame.SERVER);

    // Compute the heartbeat.
    long ping = Frame.Heartbeat.computePingPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(client.options().getHeartbeat()));
    long pong = Frame.Heartbeat.computePongPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(client.options().getHeartbeat()));

    if (ping > 0) {
      pinger = client.vertx().setPeriodic(ping, l -> pingHandler.handle(this));
    }
    if (pong > 0) {
      ponger = client.vertx().setPeriodic(pong, l -> {
        long delta = System.currentTimeMillis() - lastServerActivity;
        if (delta > pong * 2) {
          log.error("Disconnecting client " + client + " - no server activity detected in the last " + delta + " ms.");
          client.vertx().cancelTimer(l);
          disconnect();
        }
      });
    }

    resultHandler.handle(Future.succeededFuture(this));
  }
}
