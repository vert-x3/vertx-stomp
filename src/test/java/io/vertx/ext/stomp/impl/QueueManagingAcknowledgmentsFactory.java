package io.vertx.ext.stomp.impl;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.DestinationFactory;

/**
 * An implementation of destination factory managing acknowledgements.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class QueueManagingAcknowledgmentsFactory implements DestinationFactory {


  /**
   * Creates a destination for the given <em>address</em>.
   *
   * @param vertx the vert.x instance used by the STOMP server.
   * @param name  the destination
   * @return the destination, {@code null} to reject the creation.
   * @see Destination#topic(Vertx, String)
   * @see Destination#queue(Vertx, String)
   */
  @Override
  public Destination create(Vertx vertx, String name) {
    return new QueueManagingAcknowledgments(vertx, name);
  }
}
