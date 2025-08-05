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

package io.vertx.ext.stomp.impl;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link io.vertx.ext.stomp.Destination} dispatching messages to all subscribers.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Topic implements Destination {

  protected final String destination;

  protected final List<Subscription> subscriptions = new ArrayList<>();
  protected final Vertx vertx;

  public Topic(Vertx vertx, String destination) {
    this.destination = destination;
    this.vertx = vertx;
  }

  /**
   * @return the destination address.
   */
  @Override
  public String destination() {
    return destination;
  }

  /**
   * Dispatches the given frame.
   *
   * @param connection   the connection
   * @param frame        the frame
   * @param payloadMode  only for websocket bridge, explicitely specify payload type or null
   * @return the current instance of {@link Destination}
   */
  private synchronized Destination dispatch(StompServerConnection connection, Frame frame, PayloadMode payloadMode) {
    for (Subscription subscription : subscriptions) {
      String messageId = UUID.randomUUID().toString();
      Frame message = transform(frame, subscription, messageId);
      if(payloadMode != null) {
        subscription.connection.write(message, payloadMode);
      } else {
        subscription.connection.write(message);
      }
    }
    return this;
  }

  @Override
  public Destination dispatch(StompServerConnection connection, Frame frame) {
    return dispatch(connection, frame, null);
  }

  @Override
  public Destination dispatchText(StompServerConnection connection, Frame frame) {
    return dispatch(connection, frame, PayloadMode.TEXT);
  }

  @Override
  public Destination dispatchBinary(StompServerConnection connection, Frame frame) {
    return dispatch(connection, frame, PayloadMode.BINARY);
  }

  public static Frame transform(Frame frame, Subscription subscription, String messageId) {
    final Headers headers = Headers.create(frame.getHeaders())
        // Destination already set in the input headers.
        .add(Frame.SUBSCRIPTION, subscription.id)
        .add(Frame.MESSAGE_ID, messageId);
    if (!subscription.ackMode.equals("auto")) {
      // We reuse the message Id as ack Id
      headers.add(Frame.ACK, messageId);
    }
    return new Frame(Command.MESSAGE,
        headers,
        frame.getBody());
  }

  /**
   * Handles a subscription request to the current {@link Destination}. All check about the frame format and unicity
   * of the id should have been done beforehand.
   *
   * @param connection the connection
   * @param frame      the {@code SUBSCRIBE} frame
   * @return the current instance of {@link Destination}
   */
  @Override
  public synchronized Destination subscribe(StompServerConnection connection, Frame frame) {
    Subscription subscription = new Subscription(connection, frame);
    subscriptions.add(subscription);
    return this;
  }

  /**
   * Handles a un-subscription request to the current {@link Destination}.
   *
   * @param connection the connection
   * @param frame      the {@code UNSUBSCRIBE} frame
   * @return {@code true} if the un-subscription has been handled, {@code false} otherwise.
   */
  @Override
  public synchronized boolean unsubscribe(StompServerConnection connection, Frame frame) {
    boolean r = false;
    for (Subscription subscription : subscriptions) {
      if (subscription.connection.equals(connection) && subscription.id.equals(frame.getId())) {
        r = subscriptions.remove(subscription);
        // Subscription id are unique for a connection.
        break;
      }
    }
    if (subscriptions.isEmpty()) {
      vertx.sharedData().getLocalMap("stomp.destinations").remove(this);
    }
    return r;
  }

  /**
   * Removes all subscriptions of the given connection
   *
   * @param connection the connection
   * @return the current instance of {@link Destination}
   */
  @Override
  public synchronized Destination unsubscribeConnection(StompServerConnection connection) {
    new ArrayList<>(subscriptions)
        .stream()
        .filter(subscription -> subscription.connection.equals(connection))
        .forEach(subscriptions::remove);

    if (subscriptions.isEmpty()) {
      vertx.sharedData().getLocalMap("stomp.destinations").remove(this);
    }
    return this;
  }

  /**
   * Handles a {@code ACK} frame.
   *
   * @param connection the connection
   * @param frame      the {@code ACK} frame
   * @return {@code true} if the destination has handled the frame (meaning it has sent the message with id)
   */
  @Override
  public synchronized boolean ack(StompServerConnection connection, Frame frame) {
    return false;
  }

  /**
   * Handles a {@code NACK} frame.
   *
   * @param connection the connection
   * @param frame      the {@code NACK} frame
   * @return {@code true} if the destination has handled the frame (meaning it has sent the message with id)
   */
  @Override
  public synchronized boolean nack(StompServerConnection connection, Frame frame) {
    return false;
  }

  /**
   * Gets all subscription ids for the given destination hold by the given client
   *
   * @param connection the connection (client)
   * @return the list of subscription id, empty if none
   */
  @Override
  public synchronized List<String> getSubscriptions(StompServerConnection connection) {
    return subscriptions.stream()
        .filter(subscription -> subscription.connection.equals(connection))
        .map(s -> s.id)
        .collect(Collectors.toList());
  }

  /**
   * Gets the number of subscriptions attached to the current {@link Destination}.
   *
   * @return the number of subscriptions.
   */
  @Override
  public synchronized int numberOfSubscriptions() {
    return subscriptions.size();
  }

  /**
   * Checks whether or not the given address matches with the current destination.
   *
   * @param address the address
   * @return {@code true} if it matches, {@code false} otherwise.
   */
  @Override
  public boolean matches(String address) {
    return this.destination.equals(address);
  }

  protected static class Subscription {
    final StompServerConnection connection;
    final String id;
    final String ackMode;
    final String destination;

    protected Subscription(StompServerConnection connection, Frame frame) {
      this.connection = connection;
      this.ackMode = frame.getAck() != null ? frame.getAck() : "auto";
      this.id = frame.getId();
      this.destination = frame.getDestination();
    }
  }

}
