package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

import java.util.List;

/**
 * Structure passed to acknowledgement handler called when a {@code ACK} or {@code NACK} frame is received. The handler
 * receives an instance of {@link Acknowledgement} with the {@link Subscription} and the impacted messages. The list
 * of messages depends on the type of acknowledgment used by the subscription.
 * <p/>
 * Subscriptions using the {@code client} mode receives all messages that were waiting for acknowledgment that were
 * sent before the acknowledged messages. The list also contains the acknowledged message. This is a cumulative
 * acknowledgement. Subscriptions using the {@code client-individual} mode receives a singleton list containing only
 * the acknowledged message.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface Acknowledgement {

  /**
   * @return the subscription
   */
  Subscription subscription();

  /**
   * @return the list of frames that have been acknowledged / not-acknowledged. The content of the list depends on
   * the type of subscription.
   */
  List<Frame> frames();

}
