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
