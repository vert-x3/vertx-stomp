package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.stomp.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A plug-able implementation of {@link StompServerHandler}. The default behavior is compliant with the STOMP
 * specification.
 * <p/>
 * By default {@code ACK/NACK} are managed as a dead messages. Not acknowledges messages are dropped from the list
 * and a warning is printed in the log.
 */
public class DefaultStompHandler implements StompServerHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultStompHandler.class);
  private final Vertx vertx;

  private ServerFrameHandler connectHandler = new DefaultConnectHandler();

  private ServerFrameHandler stompHandler;

  private ServerFrameHandler sendHandler = new DefaultSendHandler();

  private ServerFrameHandler subscribeHandler = new DefaultSubscribeHandler();

  private ServerFrameHandler unsubscribeHandler = new DefaultUnsubscribeHandler();

  private Handler<StompServerConnection> closeHandler;

  private ServerFrameHandler commitHandler = new DefaultCommitHandler();
  private ServerFrameHandler abortHandler = new DefaultAbortHandler();
  private ServerFrameHandler beginHandler = new DefaultBeginHandler();

  private ServerFrameHandler ackHandler = new DefaultAckHandler();

  private ServerFrameHandler nackHandler = new DefaultNackHandler();

  private ServerFrameHandler disconnectHandler = ((frame, connection) -> {
    Frames.handleReceipt(frame, connection);
    connection.close();
    onClose(connection);
  });

  private AuthenticationHandler authenticatedHandler;

  private Map<String, List<Subscription>> subscriptions = new HashMap<>();
  private List<Transaction> transactions = new ArrayList<>();
  private AcknowledgmentHandler onAckHandler = (subscription, messageIds) -> log.info("Acknowledge messages - " + messageIds);
  private AcknowledgmentHandler onNackHandler = (subscription, messageIds) ->
      log.warn("Messages not acknowledge - " + messageIds);

  private long lastClientActivity;
  private long pinger;
  private long ponger;
  private Handler<StompServerConnection> pingHandler = StompServerConnection::ping;

  public DefaultStompHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  public void onClose(StompServerConnection connection) {
    if (closeHandler != null) {
      closeHandler.handle(connection);
    }
    // Default behavior.
    if (pinger != 0) {
      vertx.cancelTimer(pinger);
      pinger = 0;
    }
    if (ponger != 0) {
      vertx.cancelTimer(ponger);
      ponger = 0;
    }
    unsubscribeConnection(connection);
    unregisterTransactionsFromConnection(connection);
  }

  @Override
  public StompServerHandler connectHandler(ServerFrameHandler handler) {
    this.connectHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler stompHandler(ServerFrameHandler handler) {
    this.stompHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler subscribeHandler(ServerFrameHandler handler) {
    this.subscribeHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler unsubscribeHandler(ServerFrameHandler handler) {
    this.unsubscribeHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler sendHandler(ServerFrameHandler handler) {
    this.sendHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler closeHandler(Handler<StompServerConnection> handler) {
    this.closeHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler commitHandler(ServerFrameHandler handler) {
    this.commitHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler abortHandler(ServerFrameHandler handler) {
    this.abortHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler beginHandler(ServerFrameHandler handler) {
    this.beginHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler disconnectHandler(ServerFrameHandler handler) {
    this.disconnectHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler ackHandler(ServerFrameHandler handler) {
    this.ackHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler nackHandler(ServerFrameHandler handler) {
    this.nackHandler = handler;
    return this;
  }

  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    lastClientActivity = System.currentTimeMillis();
    switch (frame.getCommand()) {
      case CONNECT:
        handleConnect(frame, connection);
        break;
      case STOMP:
        handleStomp(frame, connection);
        break;
      case SEND:
        handleSend(frame, connection);
        break;
      case SUBSCRIBE:
        handleSubscribe(frame, connection);
        break;
      case UNSUBSCRIBE:
        handleUnsubscribe(frame, connection);
        break;
      case BEGIN:
        handleBegin(frame, connection);
        break;
      case ABORT:
        handleAbort(frame, connection);
        break;
      case COMMIT:
        handleCommit(frame, connection);
        break;
      case ACK:
        handleAck(frame, connection);
        break;
      case NACK:
        handleNack(frame, connection);
        break;
      case DISCONNECT:
        handleDisconnect(frame, connection);
        break;
      case PING:
        // We received a ping frame, we do nothing.
        break;
    }
  }

  private void handleAck(Frame frame, StompServerConnection connection) {
    if (ackHandler != null) {
      ackHandler.onFrame(frame, connection);
    }
  }

  private void handleNack(Frame frame, StompServerConnection connection) {
    if (nackHandler != null) {
      nackHandler.onFrame(frame, connection);
    }
  }

  private void handleBegin(Frame frame, StompServerConnection connection) {
    if (beginHandler != null) {
      beginHandler.onFrame(frame, connection);
    }
  }

  private void handleAbort(Frame frame, StompServerConnection connection) {
    if (abortHandler != null) {
      abortHandler.onFrame(frame, connection);
    }
  }

  private void handleCommit(Frame frame, StompServerConnection connection) {
    if (commitHandler != null) {
      commitHandler.onFrame(frame, connection);
    }
  }

  private void handleSubscribe(Frame frame, StompServerConnection connection) {
    if (subscribeHandler != null) {
      subscribeHandler.onFrame(frame, connection);
    }
  }

  private void handleUnsubscribe(Frame frame, StompServerConnection connection) {
    if (unsubscribeHandler != null) {
      unsubscribeHandler.onFrame(frame, connection);
    }
  }

  private void handleSend(Frame frame, StompServerConnection connection) {
    if (sendHandler != null) {
      sendHandler.onFrame(frame, connection);
    }
  }

  private void handleConnect(Frame frame, StompServerConnection connection) {
    if (connectHandler != null) {
      connectHandler.onFrame(frame, connection);
    }
    // Compute heartbeat, and register pinger and ponger
    long ping = Frame.Heartbeat.computePingPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(connection.server().getOptions().getHeartbeat()));
    long pong = Frame.Heartbeat.computePongPeriod(
        Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)),
        Frame.Heartbeat.create(connection.server().getOptions().getHeartbeat()));
    if (ping > 0) {
      pinger = connection.server().vertx().setPeriodic(ping, (l) -> pingHandler.handle(connection));
    }
    if (pong > 0) {
      ponger = connection.server().vertx().setPeriodic(pong, l -> {
        long delta = System.currentTimeMillis() - lastClientActivity;
        if (delta > pong * 2) {
          log.warn("Disconnecting client " + connection + " - no client activity in the last " + delta + " ms");
          connection.close();
          onClose(connection);
        }
      });
    }
  }

  private void handleDisconnect(Frame frame, StompServerConnection connection) {
    if (disconnectHandler != null) {
      disconnectHandler.onFrame(frame, connection);
    }
  }

  private void handleStomp(Frame frame, StompServerConnection connection) {
    if (stompHandler == null) {
      // Per spec, STOMP frame must be handled as CONNECT
      handleConnect(frame, connection);
      return;
    }
    stompHandler.onFrame(frame, connection);
  }

  @Override
  public StompServerHandler authenticationHandler(AuthenticationHandler handler) {
    this.authenticatedHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler onAuthenticationRequest(StompServer server,
                                                    String login, String passcode,
                                                    Handler<AsyncResult<Boolean>> handler) {
    if (!server.getOptions().isSecured()) {
      if (authenticatedHandler != null) {
        log.warn("Authentication handler set while the server is not secured");
      }
      vertx.runOnContext(v -> handler.handle(Future.succeededFuture(true)));
      return this;
    }

    if (server.getOptions().isSecured() && authenticatedHandler == null) {
      log.error("Cannot authenticate connection - no authentication handler");
      vertx.runOnContext(v -> handler.handle(Future.succeededFuture(false)));
    }

    vertx.runOnContext(v ->
        authenticatedHandler.authenticate(login, passcode, handler));
    return this;
  }

  @Override
  public List<String> getDestinations() {
    return new ArrayList<>(subscriptions.keySet());
  }

  @Override
  public boolean subscribe(Subscription subscription) {
    if (isIdAlreadyUsedByConnection(subscription)) {
      return false;
    }
    addSubscription(subscription.destination(), subscription);
    return true;
  }

  private boolean isIdAlreadyUsedByConnection(Subscription subscription) {
    final Optional<Subscription> first
        = getSubscriptions(subscription.connection()).stream().filter(s -> s.id().equals(subscription.id())).findFirst();
    return first.isPresent();
  }

  @Override
  public boolean unsubscribe(StompServerConnection connection, String id) {
    return removeSubscription(id, connection);
  }

  @Override
  public StompServerHandler unsubscribeConnection(StompServerConnection connection) {
    //TODO Check against concurrent modification exception.
    getSubscriptions(connection).stream().forEach(
        s -> removeSubscription(s.id(), s.connection())
    );
    return this;
  }

  @Override
  public List<Subscription> getSubscriptions(String destination) {
    List<Subscription> list = subscriptions.get(destination);
    if (list == null) {
      return Collections.emptyList();
    }
    return list;
  }

  @Override
  public boolean registerTransaction(Transaction transaction) {
    if (getTransaction(transaction.connection(), transaction.id()) != null) {
      return false;
    }
    transactions.add(transaction);
    return true;
  }

  @Override
  public Transaction getTransaction(StompServerConnection connection, String id) {
    return transactions.stream().filter(transaction -> transaction.connection().equals(connection) && transaction.id()
        .equals(id)).findFirst().orElse(null);
  }

  @Override
  public boolean unregisterTransaction(Transaction transaction) {
    return transaction != null && transactions.remove(transaction);
  }

  @Override
  public StompServerHandler unregisterTransactionsFromConnection(StompServerConnection connection) {
    transactions.stream()
        .filter(transaction -> transaction.connection().equals(connection))
        .sorted() // Avoid using baking up collection. TODO Test we dont have concurrent modification exception.
        .forEach(transactions::remove);
    return this;
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }

  @Override
  public Subscription getSubscription(StompServerConnection connection, String ackId) {
    return subscriptions.values().stream().flatMap(List::stream).filter(subscription ->
        subscription.connection().equals(connection) && subscription.contains(ackId)).findFirst().orElse(null);
  }

  @Override
  public StompServerHandler onAck(Subscription subscription, List<Frame> messages) {
    if (onAckHandler != null) {
      onAckHandler.handle(subscription, messages);
    }
    return this;
  }

  @Override
  public StompServerHandler onNack(Subscription subscription, List<Frame> messages) {
    if (onNackHandler != null) {
      onNackHandler.handle(subscription, messages);
    }
    return this;
  }

  @Override
  public StompServerHandler onAckHandler(AcknowledgmentHandler handler) {
    this.onAckHandler = handler;
    return this;
  }

  @Override
  public StompServerHandler onNackHandler(AcknowledgmentHandler handler) {
    this.onNackHandler = handler;
    return this;
  }

  /**
   * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   * frame containing {@code EOL} (specification). However, you can customize this and send another frame. However,
   * be aware that this may requires a custom client.
   * <p/>
   * The handler will only be called if the connection supports heartbeats.
   *
   * @param handler the action to execute when a `PING` needs to be sent.
   * @return the current {@link StompServerHandler}
   */
  @Override
  public StompServerHandler pingHandler(Handler<StompServerConnection> handler) {
    this.pingHandler = handler;
    return this;
  }


  private void addSubscription(String destination, Subscription subscription) {
    List<Subscription> list = subscriptions.get(destination);
    if (list == null) {
      list = new ArrayList<>();
      subscriptions.put(destination, list);
    }
    list.add(subscription);
  }

  private boolean removeSubscription(String id, StompServerConnection connection) {
    boolean r = false;
    Subscription subscription = getSubscription(id, connection);
    if (subscription != null) {
      List<Subscription> list = subscriptions.get(subscription.destination());
      if (list != null) {
        r = list.remove(subscription);
        if (list.isEmpty()) {
          subscriptions.remove(subscription.destination());
        }
      }
    }
    return r;
  }

  private Subscription getSubscription(String id, StompServerConnection connection) {
    for (List<Subscription> list : subscriptions.values()) {
      for (Subscription s : list) {
        if (s.connection().equals(connection) && s.id().equals(id)) {
          return s;
        }
      }
    }
    return null;
  }

  private List<Subscription> getSubscriptions(StompServerConnection connection) {
    List<Subscription> result = new ArrayList<>();
    for (List<Subscription> list : subscriptions.values()) {
      result.addAll(list.stream()
          .filter(s -> s.connection().equals(connection)).collect(Collectors.toList()));
    }
    return result;
  }
}
