package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a subscription.
 * This class is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Subscription {

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
