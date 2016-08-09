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
import rx.Observable;
import io.vertx.ext.stomp.BridgeOptions;
import java.util.List;
import io.vertx.ext.stomp.Frame;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.shareddata.Shareable;

/**
 * Represents a STOMP destination.
 * Depending on the implementation, the message delivery is different. Queue are sending message to only one
 * subscribers, while topics are broadcasting the message to all subscribers.
 * <p/>
 * Implementations <strong>must</strong> be thread-safe.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.Destination original} non RX-ified interface using Vert.x codegen.
 */

public class Destination {

  final io.vertx.ext.stomp.Destination delegate;

  public Destination(io.vertx.ext.stomp.Destination delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Destination topic(Vertx vertx, String destination) { 
    Destination ret = Destination.newInstance(io.vertx.ext.stomp.Destination.topic((io.vertx.core.Vertx)vertx.getDelegate(), destination));
    return ret;
  }

  public static Destination queue(Vertx vertx, String destination) { 
    Destination ret = Destination.newInstance(io.vertx.ext.stomp.Destination.queue((io.vertx.core.Vertx)vertx.getDelegate(), destination));
    return ret;
  }

  public static Destination bridge(Vertx vertx, BridgeOptions options) { 
    Destination ret = Destination.newInstance(io.vertx.ext.stomp.Destination.bridge((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * @return the destination address.
   */
  public String destination() { 
    String ret = delegate.destination();
    return ret;
  }

  /**
   * Dispatches the given frame.
   * @param connection the connection
   * @param frame the frame
   * @return the current instance of {@link io.vertx.rxjava.ext.stomp.Destination}
   */
  public Destination dispatch(StompServerConnection connection, Frame frame) { 
    delegate.dispatch((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), frame);
    return this;
  }

  /**
   * Handles a subscription request to the current {@link io.vertx.rxjava.ext.stomp.Destination}.
   * @param connection the connection
   * @param frame the <code>SUBSCRIBE</code> frame
   * @return the current instance of {@link io.vertx.rxjava.ext.stomp.Destination}
   */
  public Destination subscribe(StompServerConnection connection, Frame frame) { 
    delegate.subscribe((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), frame);
    return this;
  }

  /**
   * Handles a un-subscription request to the current {@link io.vertx.rxjava.ext.stomp.Destination}.
   * @param connection the connection
   * @param frame the <code>UNSUBSCRIBE</code> frame
   * @return <code>true</code> if the un-subscription has been handled, <code>false</code> otherwise.
   */
  public boolean unsubscribe(StompServerConnection connection, Frame frame) { 
    boolean ret = delegate.unsubscribe((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), frame);
    return ret;
  }

  /**
   * Removes all subscriptions of the given connection
   * @param connection the connection
   * @return the current instance of {@link io.vertx.rxjava.ext.stomp.Destination}
   */
  public Destination unsubscribeConnection(StompServerConnection connection) { 
    delegate.unsubscribeConnection((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate());
    return this;
  }

  /**
   * Handles a <code>ACK</code> frame.
   * @param connection the connection
   * @param frame the <code>ACK</code> frame
   * @return <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
   */
  public boolean ack(StompServerConnection connection, Frame frame) { 
    boolean ret = delegate.ack((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), frame);
    return ret;
  }

  /**
   * Handles a <code>NACK</code> frame.
   * @param connection the connection
   * @param frame the <code>NACK</code> frame
   * @return <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
   */
  public boolean nack(StompServerConnection connection, Frame frame) { 
    boolean ret = delegate.nack((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), frame);
    return ret;
  }

  /**
   * Gets all subscription ids for the given destination hold by the given client
   * @param connection the connection (client)
   * @return the list of subscription id, empty if none
   */
  public List<String> getSubscriptions(StompServerConnection connection) { 
    List<String> ret = delegate.getSubscriptions((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate());
    return ret;
  }

  /**
   * Gets the number of subscriptions attached to the current {@link io.vertx.rxjava.ext.stomp.Destination}.
   * @return the number of subscriptions.
   */
  public int numberOfSubscriptions() { 
    int ret = delegate.numberOfSubscriptions();
    return ret;
  }

  /**
   * Checks whether or not the given address matches with the current destination.
   * @param address the address
   * @return <code>true</code> if it matches, <code>false</code> otherwise.
   */
  public boolean matches(String address) { 
    boolean ret = delegate.matches(address);
    return ret;
  }


  public static Destination newInstance(io.vertx.ext.stomp.Destination arg) {
    return arg != null ? new Destination(arg) : null;
  }
}
