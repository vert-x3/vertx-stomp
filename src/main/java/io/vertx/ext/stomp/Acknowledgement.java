/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;

import java.util.List;

/**
 * Structure passed to acknowledgement handler called when a {@code ACK} or {@code NACK} frame is received. The handler
 * receives an instance of {@link Acknowledgement} with the subscription {@link Frame} and the impacted messages. The
 * list of messages depends on the type of acknowledgment used by the subscription.
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
   * @return the subscription frame
   */
  Frame subscription();

  /**
   * @return the list of frames that have been acknowledged / not-acknowledged. The content of the list depends on
   * the type of subscription.
   */
  List<Frame> frames();

}
