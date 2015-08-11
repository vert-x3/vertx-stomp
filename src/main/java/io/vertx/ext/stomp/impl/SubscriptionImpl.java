package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Subscription}.
 */
public class SubscriptionImpl implements Subscription {
  private final StompServerConnection connection;
  private final Ack ack;
  private final String id;
  private final String destination;

  private final List<Frame> queue = new ArrayList<>();

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

  public SubscriptionImpl(StompServerConnection connection, String destination, String ack, String id) {
    this.connection = connection;
    this.ack = Ack.fromString(ack);
    this.id = id;
    this.destination = destination;
  }

  @Override
  public StompServerConnection connection() {
    return connection;
  }

  @Override
  public String ackMode() {
    return ack.ack;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String destination() {
    return destination;
  }

  private synchronized List<Frame> find(String messageId) {
    List<Frame> result = new ArrayList<>();
    for (Frame frame : queue) {
      if (messageId.equals(frame.getHeader(Frame.MESSAGE_ID))) {
        result.add(frame);
        return result;
      } else {
        if (ack == Ack.CLIENT) {
          result.add(frame);
        }
      }
    }
    // Not found.
    return null;
  }

  @Override
  public boolean ack(String messageId) {
    if (ack == Ack.AUTO) {
      return false;
    }

    List<Frame> messages;
    synchronized (this) {
      messages = find(messageId);
      if (messages != null) {
        queue.removeAll(messages);
      }
    }

    if (messages == null) {
      return false;
    } else {
      connection.handler().onAck(this, messages);
      return true;
    }
  }

  @Override
  public boolean nack(String messageId) {
    if (ack == Ack.AUTO) {
      return false;
    }

    List<Frame> messages;
    synchronized (this) {
      messages = find(messageId);
      if (messages != null) {
        queue.removeAll(messages);
      }
    }


    if (messages == null) {
      return false;
    } else {
      connection.handler().onNack(this, messages);
      return true;
    }
  }

  @Override
  public void enqueue(Frame frame) {
    if (ack == Ack.AUTO) {
      return;
    }
    synchronized (this) {
      queue.add(frame);
    }
  }

  @Override
  public synchronized boolean contains(String messageId) {
    for (Frame frame : queue) {
      if (messageId.equals(frame.getHeader(Frame.MESSAGE_ID))) {
        return true;
      }
    }
    return false;
  }
}
