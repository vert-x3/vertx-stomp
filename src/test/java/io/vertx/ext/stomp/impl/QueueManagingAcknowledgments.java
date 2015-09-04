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
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.utils.Headers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * An example of {@link Destination} implementation providing a 'queue'-semantic supporting ACK and NACK.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class QueueManagingAcknowledgments implements Destination {

  private final String destination;
  private final Vertx vertx;
  private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
  private int lastUsedSubscriptions = -1;

  public QueueManagingAcknowledgments(Vertx vertx, String destination) {
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
   * @param connection the connection
   * @param frame      the frame ({@code SEND} frame).
   * @return the current instance of {@link Destination}
   */
  @Override
  public synchronized Destination dispatch(StompServerConnection connection, Frame frame) {
    if (subscriptions.isEmpty()) {
      lastUsedSubscriptions = -1;
      return this;
    }
    Subscription subscription = getNextSubscription();
    String messageId = UUID.randomUUID().toString();
    Frame message = transform(frame, subscription, messageId);
    subscription.enqueue(message);
    subscription.connection().write(message.toBuffer());
    return this;
  }

  private Subscription getNextSubscription() {
    lastUsedSubscriptions = lastUsedSubscriptions + 1;
    if (lastUsedSubscriptions >= subscriptions.size()) {
      lastUsedSubscriptions = 0;
    }
    return subscriptions.get(lastUsedSubscriptions);
  }

  public static Frame transform(Frame frame, Subscription subscription, String messageId) {
    final Headers headers = Headers.create(frame.getHeaders())
        // Destination already set in the input headers.
        .add(Frame.SUBSCRIPTION, subscription.id())
        .add(Frame.MESSAGE_ID, messageId);
    if (!subscription.ackMode().equals("auto")) {
      // We reuse the message Id as ack Id
      headers.add(Frame.ACK, messageId);
    }
    return new Frame(Frame.Command.MESSAGE,
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
      if (subscription.connection().equals(connection) && subscription.id().equals(frame.getId())) {
        r = subscriptions.remove(subscription);
        // Subscription id are unique for a connection.
        break;
      }
    }
    if (subscriptions.isEmpty()) {
      vertx.sharedData().getLocalMap("stomp.destinations").remove(destination);
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
        .filter(subscription -> subscription.connection().equals(connection))
        .forEach(subscriptions::remove);

    if (subscriptions.isEmpty()) {
      vertx.sharedData().getLocalMap("stomp.destinations").remove(destination);
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
    String messageId = frame.getId();
    for (Subscription subscription : subscriptions) {
      if (subscription.connection().equals(connection) && subscription.contains(messageId)) {
        return !subscription.ack(messageId).isEmpty();
      }
    }
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
    String messageId = frame.getId();
    for (Subscription subscription : subscriptions) {
      if (subscription.connection().equals(connection) && subscription.contains(messageId)) {
        final List<Frame> frames = subscription.nack(messageId);
        // Try using the next subscriber.
        if (!frames.isEmpty() && subscriptions.size() > 1) {
          Subscription next = getNextSubscription();
          if (next == subscription) {
            // If the same subscriber is picked, try the next one.
            next = getNextSubscription();
          }
          for (Frame f : frames) {
            Frame message = transform(f, next, messageId);
            next.enqueue(message);
            next.connection().write(message.toBuffer());
          }
        }
        return true;
      }
    }
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
        .filter(subscription -> subscription.connection().equals(connection))
        .map(Subscription::id)
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

  public enum Ack {

    AUTO("auto"),

    CLIENT("client"),

    CLIENT_INDIVIDUAL("client-individual");

    String ack;

    Ack(String value) {
      this.ack = value;
    }

    public static Ack fromString(String s) {
      for (Ack ack : Ack.values()) {
        if (ack.ack.equals(s)) {
          return ack;
        }
      }

      return null;
    }
  }


  private class Subscription {
    private final StompServerConnection connection;
    private final Ack ack;
    private final String id;
    private final String destination;

    private final List<Frame> queue = new ArrayList<>();

    public Subscription(StompServerConnection connection, Frame frame) {
      this.connection = connection;
      this.ack = Ack.fromString(frame.getAck());
      this.id = frame.getId();
      this.destination = frame.getDestination();
    }


    public StompServerConnection connection() {
      return connection;
    }

    public String ackMode() {
      return ack.ack;
    }

    public String id() {
      return id;
    }

    public String destination() {
      return destination;
    }

    public List<Frame> ack(String messageId) {
      if (ack == Ack.AUTO) {
        return Collections.emptyList();
      }

      synchronized (this) {
        // The research / deletion here is a bit tricky.
        // In client mode we must collect all messages until the acknowledged messages, and when found, remove all
        // these messages. However, if not found, the collection must not be modified.
        List<Frame> collected = new ArrayList<>();
        for (Frame frame : new ArrayList<>(queue)) {
          if (messageId.equals(frame.getHeader(Frame.MESSAGE_ID))) {
            collected.add(frame);
            queue.removeAll(collected);
            connection.handler().onAck(connection, frame, collected);
            return collected;
          } else {
            if (ack == Ack.CLIENT) {
              collected.add(frame);
            }
          }
        }
      }
      return Collections.emptyList();
    }

    public List<Frame> nack(String messageId) {
      if (ack == Ack.AUTO) {
        return Collections.emptyList();
      }

      synchronized (this) {
        // The research / deletion here is a bit tricky.
        // In client mode we must collect all messages until the acknowledged messages, and when found, remove all
        // these messages. However, if not found, the collection must not be modified.
        List<Frame> collected = new ArrayList<>();
        for (Frame frame : new ArrayList<>(queue)) {
          if (messageId.equals(frame.getHeader(Frame.MESSAGE_ID))) {
            collected.add(frame);
            queue.removeAll(collected);
            connection.handler().onNack(connection, frame, collected);
            return collected;
          } else {
            if (ack == Ack.CLIENT) {
              collected.add(frame);
            }
          }
        }
      }
      return Collections.emptyList();
    }

    public void enqueue(Frame frame) {
      if (ack == Ack.AUTO) {
        return;
      }
      synchronized (this) {
        queue.add(frame);
      }
    }

    public synchronized boolean contains(String messageId) {
      for (Frame frame : queue) {
        if (messageId.equals(frame.getHeader(Frame.MESSAGE_ID))) {
          return true;
        }
      }
      return false;
    }
  }


}
