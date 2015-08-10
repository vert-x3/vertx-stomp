/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.groovy.ext.stomp;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.groovy.core.Vertx
import java.util.List
import io.vertx.ext.stomp.Frame
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 * let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 * designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 * specification. In this default implementation, not acknowledge frames are dropped.
*/
@CompileStatic
public class StompServerHandler extends ServerFrameHandler {
  private final def io.vertx.ext.stomp.StompServerHandler delegate;
  public StompServerHandler(Object delegate) {
    super((io.vertx.ext.stomp.StompServerHandler) delegate);
    this.delegate = (io.vertx.ext.stomp.StompServerHandler) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates an instance of {@link io.vertx.groovy.ext.stomp.StompServerHandler} using the default (compliant) implementation.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public static StompServerHandler create(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.StompServerHandler.create((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.stomp.StompServerHandler.class);
    return ret;
  }
  /**
   * Configures the action to execute when a <code>CONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler connectHandler(ServerFrameHandler handler) {
    this.delegate.connectHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>STOMP</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler stompHandler(ServerFrameHandler handler) {
    this.delegate.stompHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler subscribeHandler(ServerFrameHandler handler) {
    this.delegate.subscribeHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeHandler(ServerFrameHandler handler) {
    this.delegate.unsubscribeHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>SEND</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler sendHandler(ServerFrameHandler handler) {
    this.delegate.sendHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a connection with the client is closed.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler closeHandler(Handler<StompServerConnection> handler) {
    this.delegate.closeHandler(new Handler<io.vertx.ext.stomp.StompServerConnection>() {
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(new io.vertx.groovy.ext.stomp.StompServerConnection(event));
      }
    });
    return this;
  }
  /**
   * Called when the connection is closed. This method executes a default behavior and must calls the configured
   * {@link io.vertx.groovy.ext.stomp.StompServerHandler#closeHandler} if any.
   * @param connection the connection
   */
  public void onClose(StompServerConnection connection) {
    this.delegate.onClose((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate());
  }
  /**
   * Configures the action to execute when a <code>COMMIT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler commitHandler(ServerFrameHandler handler) {
    this.delegate.commitHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>ABORT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler abortHandler(ServerFrameHandler handler) {
    this.delegate.abortHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>BEGIN</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler beginHandler(ServerFrameHandler handler) {
    this.delegate.beginHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>DISCONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler disconnectHandler(ServerFrameHandler handler) {
    this.delegate.disconnectHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>ACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler ackHandler(ServerFrameHandler handler) {
    this.delegate.ackHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when a <code>NACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler nackHandler(ServerFrameHandler handler) {
    this.delegate.nackHandler((io.vertx.ext.stomp.ServerFrameHandler)handler.getDelegate());
    return this;
  }
  /**
   * Called when the client connects to a server requiring authentication. It should invokes the handler configured
   * using {@link io.vertx.groovy.ext.stomp.StompServerHandler#authenticationHandler}.
   * @param server the STOMP server.
   * @param login the login
   * @param passcode the password
   * @param handler handler receiving the authentication result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAuthenticationRequest(StompServer server, String login, String passcode, Handler<AsyncResult<Boolean>> handler) {
    this.delegate.onAuthenticationRequest((io.vertx.ext.stomp.StompServer)server.getDelegate(), login, passcode, handler);
    return this;
  }
  /**
   * Configures the action to execute when a an authentication request is made.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler authenticationHandler(AuthenticationHandler handler) {
    this.delegate.authenticationHandler((io.vertx.ext.stomp.AuthenticationHandler)handler.getDelegate());
    return this;
  }
  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   * opaque Strings.
   * @return 
   */
  public List<String> getDestinations() {
    def ret = this.delegate.getDestinations();
    return ret;
  }
  /**
   * Registers the given {@link io.vertx.groovy.ext.stomp.Subscription}.
   * @param subscription the subscription
   * @return <code>true</code> if the subscription has been registered correctly, <code>false</code> otherwise. The main reason to fail the registration is the non-uniqueness of the subscription id for a given client.
   */
  public boolean subscribe(Subscription subscription) {
    def ret = this.delegate.subscribe((io.vertx.ext.stomp.Subscription)subscription.getDelegate());
    return ret;
  }
  /**
   * Unregisters the subscription 'id' from the given client.
   * @param connection the connection (client)
   * @param id the subscription id
   * @return <code>true</code> if the subscription removal succeed, <code>false</code> otherwise. The main reason to fail this removal is because the associated subscription cannot be found.
   */
  public boolean unsubscribe(StompServerConnection connection, String id) {
    def ret = this.delegate.unsubscribe((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), id);
    return ret;
  }
  /**
   * Unregisters all subscriptions from a given client / connection.
   * @param connection the connection (client)
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeConnection(StompServerConnection connection) {
    this.delegate.unsubscribeConnection((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate());
    return this;
  }
  /**
   * Gets the current list of subscriptions for the given destination.
   * @param destination the destination
   * @return the list of subscription
   */
  public List<Subscription> getSubscriptions(String destination) {
    def ret = this.delegate.getSubscriptions(destination)?.collect({underpants -> new io.vertx.groovy.ext.stomp.Subscription(underpants)});
      return ret;
  }
  /**
   * Registers a transaction.
   * @param transaction the transaction
   * @return <code>true</code> if the registration succeed, <code>false</code> otherwise. The main reason of failure is the non-uniqueness of the transaction id for a given client / connection
   */
  public boolean registerTransaction(Transaction transaction) {
    def ret = this.delegate.registerTransaction((io.vertx.ext.stomp.Transaction)transaction.getDelegate());
    return ret;
  }
  /**
   * Gets a transaction.
   * @param connection the connection used by the transaction
   * @param id the id of the transaction
   * @return the transaction, <code>null</code> if not found
   */
  public Transaction getTransaction(StompServerConnection connection, String id) {
    def ret= InternalHelper.safeCreate(this.delegate.getTransaction((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), id), io.vertx.groovy.ext.stomp.Transaction.class);
    return ret;
  }
  /**
   * Unregisters a transaction
   * @param transaction the transaction to unregister
   * @return <code>true</code> if the transaction is unregistered correctly, <code>false</code> otherwise.
   */
  public boolean unregisterTransaction(Transaction transaction) {
    def ret = this.delegate.unregisterTransaction((io.vertx.ext.stomp.Transaction)transaction.getDelegate());
    return ret;
  }
  /**
   * Unregisters all transactions from the given connection / client.
   * @param connection the connection
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unregisterTransactionsFromConnection(StompServerConnection connection) {
    this.delegate.unregisterTransactionsFromConnection((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate());
    return this;
  }
  /**
   * Gets the list of current transactions.
   * @return the list of transactions, empty is none.
   */
  public List<Transaction> getTransactions() {
    def ret = this.delegate.getTransactions()?.collect({underpants -> new io.vertx.groovy.ext.stomp.Transaction(underpants)});
      return ret;
  }
  /**
   * Gets a subscription for the given connection / client and use the given acknowledgment id. Acknowledgement id
   * is different from the subscription id as it point to a single message.
   * @param connection the connection
   * @param ackId the ack id
   * @return the subscription, <code>null</code> if not found
   */
  public Subscription getSubscription(StompServerConnection connection, String ackId) {
    def ret= InternalHelper.safeCreate(this.delegate.getSubscription((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), ackId), io.vertx.groovy.ext.stomp.Subscription.class);
    return ret;
  }
  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   * Implementations must call the handler configured using {@link io.vertx.groovy.ext.stomp.StompServerHandler#onAckHandler}.
   * @param subscription the subscription
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAck(Subscription subscription, List<Map<String, Object>> messages) {
    this.delegate.onAck((io.vertx.ext.stomp.Subscription)subscription.getDelegate(), messages.collect({underpants -> new Frame(new JsonObject(underpants))}));
    return this;
  }
  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are
   * <storng>not</storng> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
   * <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
   * {@link io.vertx.groovy.ext.stomp.StompServerHandler#onNackHandler}.
   * @param subscription the subscription
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNack(Subscription subscription, List<Map<String, Object>> messages) {
    this.delegate.onNack((io.vertx.ext.stomp.Subscription)subscription.getDelegate(), messages.collect({underpants -> new Frame(new JsonObject(underpants))}));
    return this;
  }
  /**
   * Configures the action to execute when messages are acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAckHandler(AcknowledgmentHandler handler) {
    this.delegate.onAckHandler((io.vertx.ext.stomp.AcknowledgmentHandler)handler.getDelegate());
    return this;
  }
  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNackHandler(AcknowledgmentHandler handler) {
    this.delegate.onNackHandler((io.vertx.ext.stomp.AcknowledgmentHandler)handler.getDelegate());
    return this;
  }
  /**
   * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   * frame containing <code>EOL</code> (specification). However, you can customize this and send another frame. However,
   * be aware that this may requires a custom client.
   * <p/>
   * The handler will only be called if the connection supports heartbeats.
   * @param handler the action to execute when a `PING` needs to be sent.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler pingHandler(Handler<StompServerConnection> handler) {
    this.delegate.pingHandler(new Handler<io.vertx.ext.stomp.StompServerConnection>() {
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(new io.vertx.groovy.ext.stomp.StompServerConnection(event));
      }
    });
    return this;
  }
}
