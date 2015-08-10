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
import io.vertx.ext.stomp.Frame
/**
 * Represents a subscription in the STOMP server.
*/
@CompileStatic
public class Subscription {
  private final def io.vertx.ext.stomp.Subscription delegate;
  public Subscription(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Subscription) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates an instance of {@link io.vertx.groovy.ext.stomp.Subscription} using the default implementation.
   * @param connection the connection (client)
   * @param destination the destination
   * @param ack the acknowledgment policy, <code>auto</code> by default.
   * @param id the subscription id
   * @return the created {@link io.vertx.groovy.ext.stomp.Subscription}
   */
  public static Subscription create(StompServerConnection connection, String destination, String ack, String id) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Subscription.create((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), destination, ack, id), io.vertx.groovy.ext.stomp.Subscription.class);
    return ret;
  }
  /**
   * @return the connection.
   * @return 
   */
  public StompServerConnection connection() {
    def ret= InternalHelper.safeCreate(this.delegate.connection(), io.vertx.groovy.ext.stomp.StompServerConnection.class);
    return ret;
  }
  /**
   * @return the acknowledgment policy among <code>auto</code> (default), <code>client</code> (cumulative acknowledgment) and
   * <code>client-individual</code>.
   * @return 
   */
  public String ackMode() {
    def ret = this.delegate.ackMode();
    return ret;
  }
  /**
   * @return the subscription id.
   * @return 
   */
  public String id() {
    def ret = this.delegate.id();
    return ret;
  }
  /**
   * @return the destination.
   * @return 
   */
  public String destination() {
    def ret = this.delegate.destination();
    return ret;
  }
  /**
   * Acknowledges the message with the given id.
   * @param messageId the message id
   * @return <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  public boolean ack(String messageId) {
    def ret = this.delegate.ack(messageId);
    return ret;
  }
  /**
   * Not-acknowledges the message with the given id.
   * @param messageId the message id
   * @return <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  public boolean nack(String messageId) {
    def ret = this.delegate.nack(messageId);
    return ret;
  }
  /**
   * Adds a message (identified by its id) to the list of message waiting for acknowledgment.
   * @param messageId the message id (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   */
  public void enqueue(Map<String, Object> messageId = [:]) {
    this.delegate.enqueue(messageId != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(messageId)) : null);
  }
  /**
   * Checks whether or not the message identified by the given message id is waiting for an acknowledgment.
   * @param messageId the message id
   * @return <code>true</code> if the message requires an acknowledgment, <code>false</code> otherwise.
   */
  public boolean contains(String messageId) {
    def ret = this.delegate.contains(messageId);
    return ret;
  }
}
