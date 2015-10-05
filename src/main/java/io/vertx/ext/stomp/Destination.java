/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Shareable;
import io.vertx.ext.stomp.impl.EventBusBridge;
import io.vertx.ext.stomp.impl.Queue;
import io.vertx.ext.stomp.impl.Topic;

import java.util.List;

/**
 * Represents a STOMP destination.
 * Depending on the implementation, the message delivery is different. Queue are sending message to only one
 * subscribers, while topics are broadcasting the message to all subscribers.
 * <p/>
 * Implementations <strong>must</strong> be thread-safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface Destination extends Shareable {

  static Destination topic(Vertx vertx, String destination) {
    return new Topic(vertx, destination);
  }

  static Destination queue(Vertx vertx, String destination) {
    return new Queue(vertx, destination);
  }

  static Destination bridge(Vertx vertx, BridgeOptions options) {
    return new EventBusBridge(vertx, options);
  }

  /**
   * @return the destination address.
   */
  String destination();

  /**
   * Dispatches the given frame.
   *
   * @param connection the connection
   * @param frame      the frame
   * @return the current instance of {@link Destination}
   */
  @Fluent
  Destination dispatch(StompServerConnection connection, Frame frame);

  /**
   * Handles a subscription request to the current {@link Destination}.
   *
   * @param connection the connection
   * @param frame      the {@code SUBSCRIBE} frame
   * @return the current instance of {@link Destination}
   */
  @Fluent
  Destination subscribe(StompServerConnection connection, Frame frame);

  /**
   * Handles a un-subscription request to the current {@link Destination}.
   *
   * @param connection the connection
   * @param frame      the {@code UNSUBSCRIBE} frame
   * @return {@code true} if the un-subscription has been handled, {@code false} otherwise.
   */
  boolean unsubscribe(StompServerConnection connection, Frame frame);

  /**
   * Removes all subscriptions of the given connection
   *
   * @param connection the connection
   * @return the current instance of {@link Destination}
   */
  @Fluent
  Destination unsubscribeConnection(StompServerConnection connection);

  /**
   * Handles a {@code ACK} frame.
   *
   * @param connection the connection
   * @param frame      the {@code ACK} frame
   * @return {@code true} if the destination has handled the frame (meaning it has sent the message with id)
   */
  boolean ack(StompServerConnection connection, Frame frame);

  /**
   * Handles a {@code NACK} frame.
   *
   * @param connection the connection
   * @param frame      the {@code NACK} frame
   * @return {@code true} if the destination has handled the frame (meaning it has sent the message with id)
   */
  boolean nack(StompServerConnection connection, Frame frame);


  /**
   * Gets all subscription ids for the given destination hold by the given client
   *
   * @param connection the connection (client)
   * @return the list of subscription id, empty if none
   */
  List<String> getSubscriptions(StompServerConnection connection);

  /**
   * Gets the number of subscriptions attached to the current {@link Destination}.
   *
   * @return the number of subscriptions.
   */
  int numberOfSubscriptions();

  /**
   * Checks whether or not the given address matches with the current destination.
   *
   * @param address the address
   * @return {@code true} if it matches, {@code false} otherwise.
   */
  boolean matches(String address);
}
