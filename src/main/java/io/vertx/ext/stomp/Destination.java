package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Shareable;
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
}
