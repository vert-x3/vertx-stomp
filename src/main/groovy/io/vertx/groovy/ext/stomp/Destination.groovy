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
import io.vertx.ext.stomp.BridgeOptions
import java.util.List
import io.vertx.ext.stomp.Frame
import io.vertx.groovy.core.Vertx
import io.vertx.core.shareddata.Shareable
/**
 * Represents a STOMP destination.
 * Depending on the implementation, the message delivery is different. Queue are sending message to only one
 * subscribers, while topics are broadcasting the message to all subscribers.
 * <p/>
 * Implementations <strong>must</strong> be thread-safe.
*/
@CompileStatic
public class Destination {
  private final def io.vertx.ext.stomp.Destination delegate;
  public Destination(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Destination) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Destination topic(Vertx vertx, String destination) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.Destination.topic(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, destination), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  public static Destination queue(Vertx vertx, String destination) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.Destination.queue(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, destination), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  public static Destination bridge(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.Destination.bridge(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.stomp.BridgeOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
  /**
   * @return the destination address.
   * @return 
   */
  public String destination() {
    def ret = delegate.destination();
    return ret;
  }
  /**
   * Dispatches the given frame.
   * @param connection the connection
   * @param frame the frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current instance of {@link io.vertx.groovy.ext.stomp.Destination}
   */
  public Destination dispatch(StompServerConnection connection, Map<String, Object> frame) {
    delegate.dispatch(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return this;
  }
  /**
   * Handles a subscription request to the current {@link io.vertx.groovy.ext.stomp.Destination}.
   * @param connection the connection
   * @param frame the <code>SUBSCRIBE</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current instance of {@link io.vertx.groovy.ext.stomp.Destination}
   */
  public Destination subscribe(StompServerConnection connection, Map<String, Object> frame) {
    delegate.subscribe(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return this;
  }
  /**
   * Handles a un-subscription request to the current {@link io.vertx.groovy.ext.stomp.Destination}.
   * @param connection the connection
   * @param frame the <code>UNSUBSCRIBE</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return <code>true</code> if the un-subscription has been handled, <code>false</code> otherwise.
   */
  public boolean unsubscribe(StompServerConnection connection, Map<String, Object> frame) {
    def ret = delegate.unsubscribe(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return ret;
  }
  /**
   * Removes all subscriptions of the given connection
   * @param connection the connection
   * @return the current instance of {@link io.vertx.groovy.ext.stomp.Destination}
   */
  public Destination unsubscribeConnection(StompServerConnection connection) {
    delegate.unsubscribeConnection(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null);
    return this;
  }
  /**
   * Handles a <code>ACK</code> frame.
   * @param connection the connection
   * @param frame the <code>ACK</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
   */
  public boolean ack(StompServerConnection connection, Map<String, Object> frame) {
    def ret = delegate.ack(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return ret;
  }
  /**
   * Handles a <code>NACK</code> frame.
   * @param connection the connection
   * @param frame the <code>NACK</code> frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
   */
  public boolean nack(StompServerConnection connection, Map<String, Object> frame) {
    def ret = delegate.nack(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null, frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return ret;
  }
  /**
   * Gets all subscription ids for the given destination hold by the given client
   * @param connection the connection (client)
   * @return the list of subscription id, empty if none
   */
  public List<String> getSubscriptions(StompServerConnection connection) {
    def ret = delegate.getSubscriptions(connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null);
    return ret;
  }
  /**
   * Gets the number of subscriptions attached to the current {@link io.vertx.groovy.ext.stomp.Destination}.
   * @return the number of subscriptions.
   */
  public int numberOfSubscriptions() {
    def ret = delegate.numberOfSubscriptions();
    return ret;
  }
  /**
   * Checks whether or not the given address matches with the current destination.
   * @param address the address
   * @return <code>true</code> if it matches, <code>false</code> otherwise.
   */
  public boolean matches(String address) {
    def ret = delegate.matches(address);
    return ret;
  }
}
