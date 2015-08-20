package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a subscription.
 * This class is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Subscription {

  //TODO ensure thread safety
  //TODO improve performances

  private final StompServerConnection connection;
  private final Ack ack;
  private final String id;
  private final String destination;
  private final Frame frame;

  private final List<Frame> queue = new ArrayList<>();

  public Subscription(StompServerConnection connection, Frame frame) {
    this.connection = connection;
    this.frame = frame;
    this.ack = Ack.fromString(frame.getAck());
    this.id = frame.getId();
    this.destination = frame.getDestination();
  }

  public enum Ack {

    AUTO("auto"),

    CLIENT("client"),

    //TODO Not used ???
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

  private synchronized List<Frame> find(String messageId) {
    //TODO Optimize this.
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
      connection.handler().onAck(connection, frame, messages);
      return true;
    }
  }

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
      connection.handler().onNack(connection, frame, messages);
      return true;
    }
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
