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
    ((io.vertx.core.Handler) this.delegate).handle((io.vertx.ext.stomp.ServerFrame)arg0.getDelegate());
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
  public StompServerHandler connectHandler(Handler<ServerFrame> handler) {
    this.delegate.connectHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>STOMP</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler stompHandler(Handler<ServerFrame> handler) {
    this.delegate.stompHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler subscribeHandler(Handler<ServerFrame> handler) {
    this.delegate.subscribeHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler) {
    this.delegate.unsubscribeHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>SEND</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler sendHandler(Handler<ServerFrame> handler) {
    this.delegate.sendHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
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
  public StompServerHandler commitHandler(Handler<ServerFrame> handler) {
    this.delegate.commitHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>ABORT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler abortHandler(Handler<ServerFrame> handler) {
    this.delegate.abortHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>BEGIN</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler beginHandler(Handler<ServerFrame> handler) {
    this.delegate.beginHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>DISCONNECT</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler disconnectHandler(Handler<ServerFrame> handler) {
    this.delegate.disconnectHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>ACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler ackHandler(Handler<ServerFrame> handler) {
    this.delegate.ackHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when a <code>NACK</code> frame is received.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler nackHandler(Handler<ServerFrame> handler) {
    this.delegate.nackHandler(new Handler<io.vertx.ext.stomp.ServerFrame>() {
      public void handle(io.vertx.ext.stomp.ServerFrame event) {
        handler.handle(new io.vertx.groovy.ext.stomp.ServerFrame(event));
      }
    });
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
    this.delegate.onAuthenticationRequest((io.vertx.ext.stomp.StompServer)server.getDelegate(), login, passcode, handler);
    return this;
  }
  /**
   * Configures the  to be used to authenticate the user.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler authProvider(AuthProvider handler) {
    this.delegate.authProvider((io.vertx.ext.auth.AuthProvider)handler.getDelegate());
    return this;
  }
  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   * opaque Strings.
   * @return 
   */
  public List<Destination> getDestinations() {
    def ret = this.delegate.getDestinations()?.collect({underpants -> new io.vertx.groovy.ext.stomp.Destination(underpants)});
      return ret;
  }
  /**
   * Gets the destination with the given name.
   * @param destination the destination
   * @return the {@link io.vertx.groovy.ext.stomp.Destination}, <code>null</code> if not existing.
   */
  public Destination getDestination(String destination) {
    def ret= InternalHelper.safeCreate(this.delegate.getDestination(destination), io.vertx.groovy.ext.stomp.Destination.class);
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
    this.delegate.onAck((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), subscribe != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(subscribe)) : null, messages.collect({underpants -> new Frame(new JsonObject(underpants))}));
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
    this.delegate.onNack((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), subscribe != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(subscribe)) : null, messages.collect({underpants -> new Frame(new JsonObject(underpants))}));
    return this;
  }
  /**
   * Configures the action to execute when messages are acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onAckHandler(Handler<Acknowledgement> handler) {
    this.delegate.onAckHandler(new Handler<io.vertx.ext.stomp.Acknowledgement>() {
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(new io.vertx.groovy.ext.stomp.Acknowledgement(event));
      }
    });
    return this;
  }
  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}
   */
  public StompServerHandler onNackHandler(Handler<Acknowledgement> handler) {
    this.delegate.onNackHandler(new Handler<io.vertx.ext.stomp.Acknowledgement>() {
      public void handle(io.vertx.ext.stomp.Acknowledgement event) {
        handler.handle(new io.vertx.groovy.ext.stomp.Acknowledgement(event));
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
  /**
   * Gets a {@link io.vertx.groovy.ext.stomp.Destination} object if existing, or create a new one. The creation is delegated to the
   * {@link io.vertx.groovy.ext.stomp.DestinationFactory}.
   * @param destination the destination
   * @return the {@link io.vertx.groovy.ext.stomp.Destination} instance, may have been created.
   */
  public Destination getOrCreateDestination(String destination) {
    def ret= InternalHelper.safeCreate(this.delegate.getOrCreateDestination(destination), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  /**
   * Configures the {@link io.vertx.groovy.ext.stomp.DestinationFactory} used to create {@link io.vertx.groovy.ext.stomp.Destination} objects.
   * @param factory the factory
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerHandler}.
   */
  public StompServerHandler destinationFactory(DestinationFactory factory) {
    this.delegate.destinationFactory((io.vertx.ext.stomp.DestinationFactory)factory.getDelegate());
    return this;
  }
}
