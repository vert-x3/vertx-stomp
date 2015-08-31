package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;

/**
 * Interface implemented to customize the destination creation.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface DestinationFactory {

  /**
   * Creates a destination for the given <em>address</em>.
   *
   * @param vertx the vert.x instance used by the STOMP server.
   * @param name  the destination name.
   * @return the destination, {@code null} to reject the creation.
   * @see Destination#topic(Vertx, String)
   * @see Destination#queue(Vertx, String)
   */
  Destination create(Vertx vertx, String name);

}
