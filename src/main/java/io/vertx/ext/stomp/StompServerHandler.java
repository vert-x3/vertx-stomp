package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.stomp.impl.DefaultStompHandler;

import java.util.List;

/**
 * STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 * let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 * designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 * specification. In this default implementation, not acknowledge frames are dropped.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompServerHandler extends Handler<ServerFrame> {

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
  StompServerHandler connectHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code STOMP} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler stompHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code SUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler subscribeHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code UNSUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code SEND} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler sendHandler(Handler<ServerFrame> handler);

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
  StompServerHandler commitHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code ABORT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler abortHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code BEGIN} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler beginHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code DISCONNECT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler disconnectHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code ACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler ackHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code NACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler nackHandler(Handler<ServerFrame> handler);

  /**
   * Called when the client connects to a server requiring authentication. It invokes the {@link AuthProvider} configured
   * using {@link #authProvider(AuthProvider)}.
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
   * Configures the {@link AuthProvider} to be used to authenticate the user.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler authProvider(AuthProvider handler);

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
   * Implementations must call the handler configured using {@link #onAckHandler(Handler)}.
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
   * {@link #onNackHandler(Handler)}.
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
  StompServerHandler onAckHandler(Handler<Acknowledgement> handler);

  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   * @see #onNack(Subscription, List)
   */
  @Fluent
  StompServerHandler onNackHandler(Handler<Acknowledgement> handler);


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
