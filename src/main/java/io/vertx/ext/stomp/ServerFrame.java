package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Structure passed to server handler when receiving a frame. It provides a reference on the received {@link Frame}
 * but also on the {@link StompServerConnection}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface ServerFrame {

  /**
   * @return the received frame
   */
  Frame frame();

  /**
   * @return the connection
   */
  StompServerConnection connection();

}
