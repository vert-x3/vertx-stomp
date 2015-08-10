package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

import java.util.List;

/**
 * Handler called when a {@code ACK} or {@code NACK} frame is received. The handler receives the {@link Subscription}
 * and the impacted messages. This list of messages depends on the type of acknowledgment used by the subscription.
 * Subscriptions using {@code auto} do not call this handler (because there are no {@code ACK/NACK} in @{code auto}).
 * Subscriptions using the {@code client} mode receives all messages that were waiting for acknowledgment that were
 * sent before the acknowledged messages. The list also contains the acknowledged message. This is a cumulative
 * acknowledgement. Subscriptions using the {@code client-individual} mode receives a singleton list containing only
 * the acknowledged message.
 */
@VertxGen
public interface AcknowledgmentHandler {

  /**
   * Called when a {@code ACK / NACK} frame is received.
   *
   * @param subscription the subscription
   * @param frames       the impacted frames. If the subscription uses the {@code client} mode, it contains all impacted
   *                     messages (cumulative acknowledgment). In {@code client-individual} mode, the list contains only
   *                     the acknowledged frame.
   */
  void handle(Subscription subscription, List<Frame> frames);

}
