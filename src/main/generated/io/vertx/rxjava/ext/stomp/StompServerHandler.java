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

package io.vertx.rxjava.ext.stomp;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.auth.AuthProvider;
import java.util.List;
import io.vertx.ext.stomp.Frame;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 * let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 * designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 * specification. In this default implementation, not acknowledge frames are dropped.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompServerHandler original} non RX-ified interface using Vert.x codegen.
 */

public class StompServerHandler implements Handler<ServerFrame> {

  final io.vertx.ext.stomp.StompServerHandler delegate;

  public StompServerHandler(io.vertx.ext.stomp.StompServerHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public void handle(ServerFrame arg0) { 
    this.delegate.handle((io.vertx.ext.stomp.ServerFrame) arg0.getDelegate());
  }

  /**
   * Creates an instance of {@link io.vertx.ext.stomp.StompServerHandler} using the default (compliant) implementation.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.ext.stomp.StompServerHandler}
   */
  public static StompServerHandler create(Vertx vertx) { 
    StompServerHandler ret= StompServerHandler.newInstance(io.vertx.ext.stomp.StompServerHandler.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * Configures the action to execute when a <code>CONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler connectHandler(Handler<ServerFrame> handler) { 
    this.delegate.connectHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>STOMP</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler stompHandler(Handler<ServerFrame> handler) { 
    this.delegate.stompHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler subscribeHandler(Handler<ServerFrame> handler) { 
    this.delegate.subscribeHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler) { 
    this.delegate.unsubscribeHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>SEND</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler sendHandler(Handler<ServerFrame> handler) { 
    this.delegate.sendHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a connection with the client is closed.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler closeHandler(Handler<StompServerConnection> handler) { 
    this.delegate.closeHandler(new Handler<io.vertx.ext.stomp.StompServerConnection>() {
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(new StompServerConnection(event));
      }
    });
    return this;
  }

  /**
   * Called when the connection is closed. This method executes a default behavior and must calls the configured
   * {@link io.vertx.rxjava.ext.stomp.StompServerHandler#closeHandler} if any.
   * @param connection the connection
   */
  public void onClose(StompServerConnection connection) { 
    this.delegate.onClose((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate());
  }

  /**
   * Configures the action to execute when a <code>COMMIT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.ext.stomp.StompServerHandler}
   */
  public StompServerHandler commitHandler(Handler<ServerFrame> handler) { 
    this.delegate.commitHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>ABORT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler abortHandler(Handler<ServerFrame> handler) { 
    this.delegate.abortHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>BEGIN</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler beginHandler(Handler<ServerFrame> handler) { 
    this.delegate.beginHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>DISCONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler disconnectHandler(Handler<ServerFrame> handler) { 
    this.delegate.disconnectHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>ACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler ackHandler(Handler<ServerFrame> handler) { 
    this.delegate.ackHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when a <code>NACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler nackHandler(Handler<ServerFrame> handler) { 
    this.delegate.nackHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new ServerFrame(event));
      }
    });
    return this;
  }

  /**
   * Called when the client connects to a server requiring authentication. It invokes the  configured
   * using {@link io.vertx.rxjava.ext.stomp.StompServerHandler#authProvider}.
   * @param server the STOMP server.
   * @param login the login
   * @param passcode the password
   * @param handler handler receiving the authentication result
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAuthenticationRequest(StompServer server, String login, String passcode, Handler<AsyncResult<Boolean>> handler) { 
    this.delegate.onAuthenticationRequest((io.vertx.ext.stomp.StompServer) server.getDelegate(), login, passcode, handler);
    return this;
  }

  /**
   * Called when the client connects to a server requiring authentication. It invokes the  configured
   * using {@link io.vertx.rxjava.ext.stomp.StompServerHandler#authProvider}.
   * @param server the STOMP server.
   * @param login the login
   * @param passcode the password
   * @return 
   */
  public Observable<Boolean> onAuthenticationRequestObservable(StompServer server, String login, String passcode) { 
    io.vertx.rx.java.ObservableFuture<Boolean> handler = io.vertx.rx.java.RxHelper.observableFuture();
    onAuthenticationRequest(server, login, passcode, handler.toHandler());
    return handler;
  }

  /**
   * Configures the  to be used to authenticate the user.
   * @param handler the handler
   * @return the current {@link io.vertx.ext.stomp.StompServerHandler}
   */
  public StompServerHandler authProvider(AuthProvider handler) { 
    this.delegate.authProvider((io.vertx.ext.auth.AuthProvider) handler.getDelegate());
    return this;
  }

  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   * opaque Strings.
   * @return 
   */
  public List<String> getDestinations() { 
    List<String> ret = this.delegate.getDestinations();
;
    return ret;
  }

  /**
   * Registers the given {@link io.vertx.ext.stomp.Subscription}.
   * @param subscription the subscription
   * @return <code>true</code> if the subscription has been registered correctly, <code>false</code> otherwise. The main reason to fail the registration is the non-uniqueness of the subscription id for a given client.
   */
  public boolean subscribe(Subscription subscription) { 
    boolean ret = this.delegate.subscribe((io.vertx.ext.stomp.Subscription) subscription.getDelegate());
    return ret;
  }

  /**
   * Unregisters the subscription 'id' from the given client.
   * @param connection the connection (client)
   * @param id the subscription id
   * @return <code>true</code> if the subscription removal succeed, <code>false</code> otherwise. The main reason to fail this removal is because the associated subscription cannot be found.
   */
  public boolean unsubscribe(StompServerConnection connection, String id) { 
    boolean ret = this.delegate.unsubscribe((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate(), id);
    return ret;
  }

  /**
   * Unregisters all subscriptions from a given client / connection.
   * @param connection the connection (client)
   * @return the current {@link io.vertx.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeConnection(StompServerConnection connection) { 
    this.delegate.unsubscribeConnection((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate());
    return this;
  }

  /**
   * Gets the current list of subscriptions for the given destination.
   * @param destination the destination
   * @return the list of subscription
   */
  public List<Subscription> getSubscriptions(String destination) { 
    List<Subscription> ret = this.delegate.getSubscriptions(destination).stream().map(Subscription::newInstance).collect(java.util.stream.Collectors.toList());
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
    Subscription ret= Subscription.newInstance(this.delegate.getSubscription((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate(), ackId));
    return ret;
  }

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   * Implementations must call the handler configured using {@link io.vertx.rxjava.ext.stomp.StompServerHandler#onAckHandler}.
   * @param subscription the subscription
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAck(Subscription subscription, List<Frame> messages) { 
    this.delegate.onAck((io.vertx.ext.stomp.Subscription) subscription.getDelegate(), messages);
    return this;
  }

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are
   * <storng>not</storng> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
   * <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
   * {@link io.vertx.rxjava.ext.stomp.StompServerHandler#onNackHandler}.
   * @param subscription the subscription
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNack(Subscription subscription, List<Frame> messages) { 
    this.delegate.onNack((io.vertx.ext.stomp.Subscription) subscription.getDelegate(), messages);
    return this;
  }

  /**
   * Configures the action to execute when messages are acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAckHandler(Handler<Acknowledgement> handler) { 
    this.delegate.onAckHandler(new Handler<io.vertx.ext.stomp.Acknowledgement>() {
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(new Acknowledgement(event));
      }
    });
    return this;
  }

  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNackHandler(Handler<Acknowledgement> handler) { 
    this.delegate.onNackHandler(new Handler<io.vertx.ext.stomp.Acknowledgement>() {
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(new Acknowledgement(event));
      }
    });
    return this;
  }

  /**
   * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   * frame containing <code>EOL</code> (specification). However, you can customize this and send another frame. However,
   * be aware that this may requires a custom client.
   * <p/>
   * The handler will only be called if the connection supports heartbeats.
   * @param handler the action to execute when a `PING` needs to be sent.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerHandler}
   */
  public StompServerHandler pingHandler(Handler<StompServerConnection> handler) { 
    this.delegate.pingHandler(new Handler<io.vertx.ext.stomp.StompServerConnection>() {
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(new StompServerConnection(event));
      }
    });
    return this;
  }


  public static StompServerHandler newInstance(io.vertx.ext.stomp.StompServerHandler arg) {
    return arg != null ? new StompServerHandler(arg) : null;
  }
}
