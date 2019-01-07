/*
 *  Copyright (c) 2011-2015 The original author or authors
 *
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

package io.vertx.ext.stomp.integration;

import io.vertx.ext.stomp.StompClientOptions;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

/**
 * Checks that our clients can connect and interact with ActiveMQ. This test use the core port (61616).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ArtemisCoreDockerIT extends AbstractClientIT {

  @ClassRule
  public static final GenericContainer artemis
    = new GenericContainer("vromero/activemq-artemis:2.6.3-alpine")
    .withExposedPorts(61613)
    .withExposedPorts(5445);

  @Override
  public StompClientOptions getOptions() {
    return new StompClientOptions()
        .setHost(getDockerHost())
        .setPort(artemis.getMappedPort(61613))
        .setLogin("artemis")
        .setPasscode("simetraehcapa");
  }

  @Override
  public StompClientOptions getOptionsWithSSL() {
    return null;
  }

  /**
   * Artemis queue requires the "jms.queue" prefix.
   *
   * @return the destination.
   */
  @Override
  public String getDestination() {
    return "jms.queue.box-core";
  }
}
