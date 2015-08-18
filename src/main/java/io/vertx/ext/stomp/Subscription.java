package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.stomp.impl.SubscriptionImpl;

/**
 * Represents a subscription in the STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface Subscription {

  /**
   * Creates an instance of {@link Subscription} using the default implementation.
   *
   * @param connection  the connection (client)
   * @param destination the destination
   * @param ack         the acknowledgment policy, {@code auto} by default.
   * @param id          the subscription id
   * @return the created {@link Subscription}
   */
  static Subscription create(StompServerConnection connection, String destination, String ack, String id) {
    return new SubscriptionImpl(connection, destination, ack, id);
  }

  /**
   * @return the connection.
   */
  StompServerConnection connection();

  /**
   * @return the acknowledgment policy among {@code auto} (default), {@code client} (cumulative acknowledgment) and
   * {@code client-individual}.
   */
  String ackMode();

  /**
   * @return the subscription id.
   */
  String id();

  /**
   * @return the destination.
   */
  String destination();

  /**
   * Acknowledges the message with the given id.
   *
   * @param messageId the message id
   * @return {@code true} if the message was waiting for acknowledgment, {@code false} otherwise.
   */
  boolean ack(String messageId);

  /**
   * Not-acknowledges the message with the given id.
   *
   * @param messageId the message id
   * @return {@code true} if the message was waiting for acknowledgment, {@code false} otherwise.
   */
  boolean nack(String messageId);

  /**
   * Adds a message (identified by its id) to the list of message waiting for acknowledgment.
   *
   * @param messageId the message id
   */
  void enqueue(Frame messageId);

  /**
   * Checks whether or not the message identified by the given message id is waiting for an acknowledgment.
   *
   * @param messageId the message id
   * @return {@code true} if the message requires an acknowledgment, {@code false} otherwise.
   */
  boolean contains(String messageId);
}
