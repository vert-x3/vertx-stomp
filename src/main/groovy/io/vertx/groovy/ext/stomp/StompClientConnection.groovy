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
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.ext.stomp.Frame
import java.util.Map
import io.vertx.core.Handler
/**
 * Once a connection to the STOMP server has been made, client receives a {@link io.vertx.groovy.ext.stomp.StompClientConnection}, that let
 * send and receive STOMP frames.
*/
@CompileStatic
public class StompClientConnection {
  private final def io.vertx.ext.stomp.StompClientConnection delegate;
  public StompClientConnection(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.StompClientConnection) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the session id.
   * @return 
   */
  public String session() {
    def ret = delegate.session();
    return ret;
  }
  /**
   * @return the STOMP protocol version negotiated with the server.
   * @return 
   */
  public String version() {
    def ret = delegate.version();
    return ret;
  }
  /**
   * Closes the connection without sending the <code>DISCONNECT</code> frame.
   */
  public void close() {
    delegate.close();
  }
  /**
   * @return the server name.
   * @return 
   */
  public String server() {
    def ret = delegate.server();
    return ret;
  }
  /**
   * Sends a <code>SEND</code> frame to the server.
   * @param headers the headers, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String, String> headers, Buffer body) {
    delegate.send(headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null);
    return this;
  }
  /**
   * Sends a <code>SEND</code> frame to the server.
   * @param headers the headers, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String, String> headers, Buffer body, Handler<Map<String, Object>> receiptHandler) {
    delegate.send(headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends a <code>SEND</code> frame to the server to the given destination. The message does not have any other header.
   * @param destination the destination, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Buffer body) {
    delegate.send(destination, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null);
    return this;
  }
  /**
   * Sends a <code>SEND</code> frame to the server to the given destination. The message does not have any other header.
   * @param destination the destination, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Buffer body, Handler<Map<String, Object>> receiptHandler) {
    delegate.send(destination, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends the given frame to the server.
   * @param frame the frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String, Object> frame = [:]) {
    delegate.send(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null);
    return this;
  }
  /**
   * Sends the given frame to the server.
   * @param frame the frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String, Object> frame = [:], Handler<Map<String, Object>> receiptHandler) {
    delegate.send(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends a <code>SEND</code> frame to the server to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Map<String, String> headers, Buffer body) {
    delegate.send(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null);
    return this;
  }
  /**
   * Sends a <code>SEND</code> frame to the server to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Map<String, String> headers, Buffer body, Handler<Map<String, Object>> receiptHandler) {
    delegate.send(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, body != null ? (io.vertx.core.buffer.Buffer)body.getDelegate() : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   * @param destination the destination, must not be <code>null</code>
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @return the subscription id.
   */
  public String subscribe(String destination, Handler<Map<String, Object>> handler) {
    def ret = delegate.subscribe(destination, handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return ret;
  }
  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   * @param destination the destination, must not be <code>null</code>
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
   * @return the subscription id.
   */
  public String subscribe(String destination, Handler<Map<String, Object>> handler, Handler<Map<String, Object>> receiptHandler) {
    def ret = delegate.subscribe(destination, handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return ret;
  }
  /**
   * Subscribes to the given destination.
   * @param destination the destination, must not be <code>null</code>.
   * @param headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  public String subscribe(String destination, Map<String, String> headers, Handler<Map<String, Object>> handler) {
    def ret = delegate.subscribe(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return ret;
  }
  /**
   * Subscribes to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  public String subscribe(String destination, Map<String, String> headers, Handler<Map<String, Object>> handler, Handler<Map<String, Object>> receiptHandler) {
    def ret = delegate.subscribe(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return ret;
  }
  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the <code>id</code> header).
   * @param destination the destination
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination) {
    delegate.unsubscribe(destination);
    return this;
  }
  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the <code>id</code> header).
   * @param destination the destination
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the un-subscription has been received. The handler receives the sent frame (<code>UNSUBSCRIBE</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Handler<Map<String, Object>> receiptHandler) {
    delegate.unsubscribe(destination, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the <code>id</code> header, the header value is used. Otherwise the destination is used.
   * @param destination the destination
   * @param headers the headers
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Map<String, String> headers) {
    delegate.unsubscribe(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null);
    return this;
  }
  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the <code>id</code> header, the header value is used. Otherwise the destination is used.
   * @param destination the destination
   * @param headers the headers
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the un-subscription has been received. The handler receives the sent frame (<code>UNSUBSCRIBE</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Map<String, String> headers, Handler<Map<String, Object>> receiptHandler) {
    delegate.unsubscribe(destination, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sets a handler notified when an <code>ERROR</code> frame is received by the client. The handler receives the <code>ERROR</code> frame and a reference on the {@link io.vertx.groovy.ext.stomp.StompClientConnection}.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection errorHandler(Handler<Map<String, Object>> handler) {
    delegate.errorHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sets a handler notified when the STOMP connection is closed.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection closeHandler(Handler<StompClientConnection> handler) {
    delegate.closeHandler(handler != null ? new Handler<io.vertx.ext.stomp.StompClientConnection>(){
      public void handle(io.vertx.ext.stomp.StompClientConnection event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompClientConnection.class));
      }
    } : null);
    return this;
  }
  /**
   * Sets a handler notified when the server does not respond to a <code>ping</code> request in time. In other
   * words, this handler is invoked when the heartbeat has detected a connection failure with the server.
   * The handler can decide to reconnect to the server.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection} receiving the dropped connection.
   */
  public StompClientConnection connectionDroppedHandler(Handler<StompClientConnection> handler) {
    delegate.connectionDroppedHandler(handler != null ? new Handler<io.vertx.ext.stomp.StompClientConnection>(){
      public void handle(io.vertx.ext.stomp.StompClientConnection event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompClientConnection.class));
      }
    } : null);
    return this;
  }
  /**
   * Sets a handler that let customize the behavior when a ping needs to be sent to the server. Be aware that
   * changing the default behavior may break the compliance with the STOMP specification.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection pingHandler(Handler<StompClientConnection> handler) {
    delegate.pingHandler(handler != null ? new Handler<io.vertx.ext.stomp.StompClientConnection>(){
      public void handle(io.vertx.ext.stomp.StompClientConnection event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompClientConnection.class));
      }
    } : null);
    return this;
  }
  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction begin has been processed by the server. The handler receives the sent frame (<code>BEGIN</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection beginTX(String id, Handler<Map<String, Object>> receiptHandler) {
    delegate.beginTX(id, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection beginTX(String id) {
    delegate.beginTX(id);
    return this;
  }
  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection beginTX(String id, Map<String, String> headers) {
    delegate.beginTX(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null);
    return this;
  }
  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction begin has been processed by the server. The handler receives the sent frame (<code>BEGIN</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection beginTX(String id, Map<String, String> headers, Handler<Map<String, Object>> receiptHandler) {
    delegate.beginTX(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id) {
    delegate.commit(id);
    return this;
  }
  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction commit has been processed by the server. The handler receives the sent frame (<code>COMMIT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Handler<Map<String, Object>> receiptHandler) {
    delegate.commit(id, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Map<String, String> headers) {
    delegate.commit(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null);
    return this;
  }
  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction commit has been processed by the server. The handler receives the sent frame (<code>COMMIT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Map<String, String> headers, Handler<Map<String, Object>> receiptHandler) {
    delegate.commit(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id) {
    delegate.abort(id);
    return this;
  }
  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction cancellation has been processed by the server. The handler receives the sent frame (<code>ABORT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Handler<Map<String, Object>> receiptHandler) {
    delegate.abort(id, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Map<String, String> headers) {
    delegate.abort(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null);
    return this;
  }
  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction cancellation has been processed by the server. The handler receives the sent frame (<code>ABORT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Map<String, String> headers, Handler<Map<String, Object>> receiptHandler) {
    delegate.abort(id, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Disconnects the client. Unlike the {@link io.vertx.groovy.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server.
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect() {
    delegate.disconnect();
    return this;
  }
  /**
   * Disconnects the client. Unlike the {@link io.vertx.groovy.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the disconnection has been processed by the server. The handler receives the sent frame (<code>DISCONNECT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect(Handler<Map<String, Object>> receiptHandler) {
    delegate.disconnect(receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Disconnects the client. Unlike the {@link io.vertx.groovy.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server. This method lets you customize the <code>DISCONNECT</code> frame.
   * @param frame the <code>DISCONNECT</code> frame. (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect(Map<String, Object> frame) {
    delegate.disconnect(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null);
    return this;
  }
  /**
   * Disconnects the client. Unlike the {@link io.vertx.groovy.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server. This method lets you customize the <code>DISCONNECT</code> frame.
   * @param frame the <code>DISCONNECT</code> frame. (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the disconnection has been processed by the server. The handler receives the sent frame (<code>DISCONNECT</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect(Map<String, Object> frame, Handler<Map<String, Object>> receiptHandler) {
    delegate.disconnect(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id) {
    delegate.ack(id);
    return this;
  }
  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id, Handler<Map<String, Object>> receiptHandler) {
    delegate.ack(id, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id) {
    delegate.nack(id);
    return this;
  }
  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id, Handler<Map<String, Object>> receiptHandler) {
    delegate.nack(id, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   * @param id the message id of the message to acknowledge
   * @param txId the transaction id
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id, String txId) {
    delegate.ack(id, txId);
    return this;
  }
  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   * @param id the message id of the message to acknowledge
   * @param txId the transaction id
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id, String txId, Handler<Map<String, Object>> receiptHandler) {
    delegate.ack(id, txId, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   * @param id the message id of the message to acknowledge
   * @param txId the transaction id
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id, String txId) {
    delegate.nack(id, txId);
    return this;
  }
  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   * @param id the message id of the message to acknowledge
   * @param txId the transaction id
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id, String txId, Handler<Map<String, Object>> receiptHandler) {
    delegate.nack(id, txId, receiptHandler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        receiptHandler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Configures a received handler that get notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified by the handler.
   * <p>
   * Unlike {@link io.vertx.groovy.ext.stomp.StompClient#receivedFrameHandler}, the given handler won't receive the <code>CONNECTED</code> frame. If a received frame handler is set on the {@link io.vertx.groovy.ext.stomp.StompClient}, it will be used by all
   * clients connection, so calling this method is useless, except if you want to use a different handler.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection receivedFrameHandler(Handler<Map<String, Object>> handler) {
    delegate.receivedFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Configures a handler notified when a frame is going to be written on the wire. This handler can be used from
   * logging, debugging. The handler can modify the received frame.
   *
   * If a writing frame handler is set on the {@link io.vertx.groovy.ext.stomp.StompClient}, it will be used by all
   * clients connection, so calling this method is useless, except if you want to use a different handler.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClientConnection}
   */
  public StompClientConnection writingFrameHandler(Handler<Map<String, Object>> handler) {
    delegate.writingFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
}
