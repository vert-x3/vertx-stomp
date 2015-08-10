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
import io.vertx.ext.stomp.Frame;

/**
 * Represents a subscription in the STOMP server.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.Subscription original} non RX-ified interface using Vert.x codegen.
 */

public class Subscription {

  final io.vertx.ext.stomp.Subscription delegate;

  public Subscription(io.vertx.ext.stomp.Subscription delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Creates an instance of {@link io.vertx.rxjava.ext.stomp.Subscription} using the default implementation.
   * @param connection the connection (client)
   * @param destination the destination
   * @param ack the acknowledgment policy, <code>auto</code> by default.
   * @param id the subscription id
   * @return the created {@link io.vertx.rxjava.ext.stomp.Subscription}
   */
  public static Subscription create(StompServerConnection connection, String destination, String ack, String id) { 
    Subscription ret= Subscription.newInstance(io.vertx.ext.stomp.Subscription.create((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate(), destination, ack, id));
    return ret;
  }

  /**
   * @return the connection.
   * @return 
   */
  public StompServerConnection connection() { 
    StompServerConnection ret= StompServerConnection.newInstance(this.delegate.connection());
    return ret;
  }

  /**
   * @return the acknowledgment policy among <code>auto</code> (default), <code>client</code> (cumulative acknowledgment) and
   * <code>client-individual</code>.
   * @return 
   */
  public String ackMode() { 
    String ret = this.delegate.ackMode();
    return ret;
  }

  /**
   * @return the subscription id.
   * @return 
   */
  public String id() { 
    String ret = this.delegate.id();
    return ret;
  }

  /**
   * @return the destination.
   * @return 
   */
  public String destination() { 
    String ret = this.delegate.destination();
    return ret;
  }

  /**
   * Acknowledges the message with the given id.
   * @param messageId the message id
   * @return <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  public boolean ack(String messageId) { 
    boolean ret = this.delegate.ack(messageId);
    return ret;
  }

  /**
   * Not-acknowledges the message with the given id.
   * @param messageId the message id
   * @return <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  public boolean nack(String messageId) { 
    boolean ret = this.delegate.nack(messageId);
    return ret;
  }

  /**
   * Adds a message (identified by its id) to the list of message waiting for acknowledgment.
   * @param messageId the message id
   */
  public void enqueue(Frame messageId) { 
    this.delegate.enqueue(messageId);
  }

  /**
   * Checks whether or not the message identified by the given message id is waiting for an acknowledgment.
   * @param messageId the message id
   * @return <code>true</code> if the message requires an acknowledgment, <code>false</code> otherwise.
   */
  public boolean contains(String messageId) { 
    boolean ret = this.delegate.contains(messageId);
    return ret;
  }


  public static Subscription newInstance(io.vertx.ext.stomp.Subscription arg) {
    return arg != null ? new Subscription(arg) : null;
  }
}
