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
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.auth.AuthProvider
import io.vertx.ext.stomp.BridgeOptions
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
public class StompServerHandler implements Handler<ServerFrame> {
  private final def io.vertx.ext.stomp.StompServerHandler delegate;
  public StompServerHandler(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.StompServerHandler) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public void handle(ServerFrame arg0) {
    ((io.vertx.core.Handler) delegate).handle(arg0 != null ? (io.vertx.ext.stomp.ServerFrame)arg0.getDelegate() : null);
  }
  /**
   * Creates an instance of {@link io.vertx.groovy.ext.stomp.StompServerHandler} using the default (compliant) implementation.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public static StompServerHandler create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.StompServerHandler.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.groovy.ext.stomp.StompServerHandler.class);
    return ret;
  }
  /**
   * Configures a handler that get notified when a STOMP frame is received by the server.
   * This handler can be used for logging, debugging or ad-hoc behavior.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler receivedFrameHandler(Handler<ServerFrame> handler) {
    delegate.receivedFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>CONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler connectHandler(Handler<ServerFrame> handler) {
    delegate.connectHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>STOMP</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler stompHandler(Handler<ServerFrame> handler) {
    delegate.stompHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler subscribeHandler(Handler<ServerFrame> handler) {
    delegate.subscribeHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler) {
    delegate.unsubscribeHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>SEND</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler sendHandler(Handler<ServerFrame> handler) {
    delegate.sendHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a connection with the client is closed.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler closeHandler(Handler<StompServerConnection> handler) {
    delegate.closeHandler(handler != null ? new Handler<io.vertx.ext.stomp.StompServerConnection>(){
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompServerConnection.class));
      }
    } : null);
    return this;
  }
  /**
   * Called when the connection is closed. This method executes a default behavior and must calls the configured
   * {@link io.vertx.groovy.ext.stomp.StompServerHandler#closeHandler} if any.
   * @param connection the connection
   */
  public void onClose(StompServerConnection connection) {
    delegate.onClose(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null);
  }
  /**
   * Configures the action to execute when a <code>COMMIT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler commitHandler(Handler<ServerFrame> handler) {
    delegate.commitHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>ABORT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler abortHandler(Handler<ServerFrame> handler) {
    delegate.abortHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>BEGIN</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler beginHandler(Handler<ServerFrame> handler) {
    delegate.beginHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>DISCONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler disconnectHandler(Handler<ServerFrame> handler) {
    delegate.disconnectHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>ACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler ackHandler(Handler<ServerFrame> handler) {
    delegate.ackHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when a <code>NACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler nackHandler(Handler<ServerFrame> handler) {
    delegate.nackHandler(handler != null ? new Handler<io.vertx.ext.stomp.ServerFrame>(){
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.ServerFrame.class));
      }
    } : null);
    return this;
  }
  /**
   * Called when the client connects to a server requiring authentication. It invokes the  configured
   * using {@link io.vertx.groovy.ext.stomp.StompServerHandler#authProvider}.
   * @param server the STOMP server.
   * @param login the login
   * @param passcode the password
   * @param handler handler receiving the authentication result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAuthenticationRequest(StompServer server, String login, String passcode, Handler<AsyncResult<Boolean>> handler) {
    delegate.onAuthenticationRequest(server != null ? (io.vertx.ext.stomp.StompServer)server.getDelegate() : null, login, passcode, handler);
    return this;
  }
  /**
   * Configures the  to be used to authenticate the user.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler authProvider(AuthProvider handler) {
    delegate.authProvider(handler != null ? (io.vertx.ext.auth.AuthProvider)handler.getDelegate() : null);
    return this;
  }
  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as opaque Strings.
   */
  public List<Destination> getDestinations() {
    def ret = (List)delegate.getDestinations()?.collect({InternalHelper.safeCreate(it, io.vertx.groovy.ext.stomp.Destination.class)});
    return ret;
  }
  /**
   * Gets the destination with the given name.
   * @param destination the destination
   * @return the {@link io.vertx.groovy.ext.stomp.Destination}, <code>null</code> if not existing.
   */
  public Destination getDestination(String destination) {
    def ret = InternalHelper.safeCreate(delegate.getDestination(destination), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   * Implementations must call the handler configured using {@link io.vertx.groovy.ext.stomp.StompServerHandler#onAckHandler}.
   * @param connection the connection
   * @param subscribe the <code>SUBSCRIBE</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAck(StompServerConnection connection, Map<String, Object> subscribe, List<Map<String, Object>> messages) {
    delegate.onAck(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, subscribe != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(subscribe)) : null, messages != null ? (List)messages.collect({new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(it))}) : null);
    return this;
  }
  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are
   * <strong>not</strong> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
   * <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
   * {@link io.vertx.groovy.ext.stomp.StompServerHandler#onNackHandler}.
   * @param connection the connection
   * @param subscribe the <code>SUBSCRIBE</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @param messages the acknowledge messages
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNack(StompServerConnection connection, Map<String, Object> subscribe, List<Map<String, Object>> messages) {
    delegate.onNack(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, subscribe != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(subscribe)) : null, messages != null ? (List)messages.collect({new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(it))}) : null);
    return this;
  }
  /**
   * Configures the action to execute when messages are acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAckHandler(Handler<Acknowledgement> handler) {
    delegate.onAckHandler(handler != null ? new Handler<io.vertx.ext.stomp.Acknowledgement>(){
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.Acknowledgement.class));
      }
    } : null);
    return this;
  }
  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNackHandler(Handler<Acknowledgement> handler) {
    delegate.onNackHandler(handler != null ? new Handler<io.vertx.ext.stomp.Acknowledgement>(){
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.Acknowledgement.class));
      }
    } : null);
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
    delegate.pingHandler(handler != null ? new Handler<io.vertx.ext.stomp.StompServerConnection>(){
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompServerConnection.class));
      }
    } : null);
    return this;
  }
  /**
   * Gets a {@link io.vertx.groovy.ext.stomp.Destination} object if existing, or create a new one. The creation is delegated to the
   * {@link io.vertx.groovy.ext.stomp.DestinationFactory}.
   * @param destination the destination
   * @return the {@link io.vertx.groovy.ext.stomp.Destination} instance, may have been created.
   */
  public Destination getOrCreateDestination(String destination) {
    def ret = InternalHelper.safeCreate(delegate.getOrCreateDestination(destination), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  /**
   * Configures the {@link io.vertx.groovy.ext.stomp.DestinationFactory} used to create {@link io.vertx.groovy.ext.stomp.Destination} objects.
   * @param factory the factory
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}.
   */
  public StompServerHandler destinationFactory(DestinationFactory factory) {
    delegate.destinationFactory(factory != null ? (io.vertx.ext.stomp.DestinationFactory)factory.getDelegate() : null);
    return this;
  }
  /**
   * Configures the STOMP server to act as a bridge with the Vert.x event bus.
   * @param options the configuration options (see <a href="../../../../../../../cheatsheet/BridgeOptions.html">BridgeOptions</a>)
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}.
   */
  public StompServerHandler bridge(Map<String, Object> options = [:]) {
    delegate.bridge(options != null ? new io.vertx.ext.stomp.BridgeOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null);
    return this;
  }
}
