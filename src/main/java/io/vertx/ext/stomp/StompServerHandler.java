package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.impl.DefaultStompHandler;

import java.util.List;

/**
 * STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 * let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 * designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 * specification. In this default implementation, not acknowledge frames are dropped.
 */
@VertxGen
public interface StompServerHandler extends ServerFrameHandler {

  /**
   * Creates an instance of {@link StompServerHandler} using the default (compliant) implementation.
   *
   * @param vertx the vert.x instance to use
   * @return the created {@link StompServerHandler}
   */
  static StompServerHandler create(Vertx vertx) {
    return new DefaultStompHandler(vertx);
  }

  /**
   * Configures the action to execute when a {@code CONNECT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler connectHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code STOMP} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler stompHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code SUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler subscribeHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code UNSUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler unsubscribeHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code SEND} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler sendHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a connection with the client is closed.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler closeHandler(Handler<StompServerConnection> handler);

  /**
   * Called when the connection is closed. This method executes a default behavior and must calls the configured
   * {@link #closeHandler(Handler)} if any.
   *
   * @param connection the connection
   */
  void onClose(StompServerConnection connection);

  /**
   * Configures the action to execute when a {@code COMMIT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler commitHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code ABORT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler abortHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code BEGIN} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler beginHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code DISCONNECT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler disconnectHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code ACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler ackHandler(ServerFrameHandler handler);

  /**
   * Configures the action to execute when a {@code NACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler nackHandler(ServerFrameHandler handler);

  /**
   * Called when the client connects to a server requiring authentication. It should invokes the handler configured
   * using {@link #authenticationHandler(AuthenticationHandler)}.
   *
   * @param server   the STOMP server.
   * @param login    the login
   * @param passcode the password
   * @param handler  handler receiving the authentication result
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onAuthenticationRequest(StompServer server, String login, String passcode,
                                             Handler<AsyncResult<Boolean>> handler);

  /**
   * Configures the action to execute when a an authentication request is made.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler authenticationHandler(AuthenticationHandler handler);

  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   * opaque Strings.
   */
  List<String> getDestinations();

  /**
   * Registers the given {@link Subscription}.
   *
   * @param subscription the subscription
   * @return {@code true} if the subscription has been registered correctly, {@code false} otherwise. The main reason
   * to fail the registration is the non-uniqueness of the subscription id for a given client.
   */
  boolean subscribe(Subscription subscription);

  /**
   * Unregisters the subscription 'id' from the given client.
   *
   * @param connection the connection (client)
   * @param id         the subscription id
   * @return {@code true} if the subscription removal succeed, {@code false} otherwise. The main reason to fail this
   * removal is because the associated subscription cannot be found.
   */
  boolean unsubscribe(StompServerConnection connection, String id);

  /**
   * Unregisters all subscriptions from a given client / connection.
   *
   * @param connection the connection (client)
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler unsubscribeConnection(StompServerConnection connection);

  /**
   * Gets the current list of subscriptions for the given destination.
   *
   * @param destination the destination
   * @return the list of subscription
   */
  List<Subscription> getSubscriptions(String destination);

  /**
   * Registers a transaction.
   *
   * @param transaction the transaction
   * @return {@code true} if the registration succeed, {@code false} otherwise. The main reason of failure is the
   * non-uniqueness of the transaction id for a given client / connection
   */
  boolean registerTransaction(Transaction transaction);

  /**
   * Gets a transaction.
   *
   * @param connection the connection used by the transaction
   * @param id         the id of the transaction
   * @return the transaction, {@code null} if not found
   */
  Transaction getTransaction(StompServerConnection connection, String id);

  /**
   * Unregisters a transaction
   *
   * @param transaction the transaction to unregister
   * @return {@code true} if the transaction is unregistered correctly, {@code false} otherwise.
   */
  boolean unregisterTransaction(Transaction transaction);

  /**
   * Unregisters all transactions from the given connection / client.
   *
   * @param connection the connection
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler unregisterTransactionsFromConnection(StompServerConnection connection);

  /**
   * Gets the list of current transactions.
   *
   * @return the list of transactions, empty is none.
   */
  List<Transaction> getTransactions();

  /**
   * Gets a subscription for the given connection / client and use the given acknowledgment id. Acknowledgement id
   * is different from the subscription id as it point to a single message.
   *
   * @param connection the connection
   * @param ackId      the ack id
   * @return the subscription, {@code null} if not found
   */
  Subscription getSubscription(StompServerConnection connection, String ackId);

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   * Implementations must call the handler configured using {@link #onAckHandler(AcknowledgmentHandler)}.
   *
   * @param subscription the subscription
   * @param messages     the acknowledge messages
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onAck(Subscription subscription, List<Frame> messages);

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are
   * <storng>not</storng> acknowledged. Not acknowledgment can result from a {@code NACK} frame or from a timeout (no
   * {@code ACK} frame received in a given time. Implementations must call the handler configured using
   * {@link #onNackHandler(AcknowledgmentHandler)}.
   *
   * @param subscription the subscription
   * @param messages     the acknowledge messages
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onNack(Subscription subscription, List<Frame> messages);

  /**
   * Configures the action to execute when messages are acknowledged.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   * @see #onAck(Subscription, List)
   */
  @Fluent
  StompServerHandler onAckHandler(AcknowledgmentHandler handler);

  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   * @see #onNack(Subscription, List)
   */
  @Fluent
  StompServerHandler onNackHandler(AcknowledgmentHandler handler);


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
  @Fluent
  StompServerHandler pingHandler(Handler<StompServerConnection> handler);
}
