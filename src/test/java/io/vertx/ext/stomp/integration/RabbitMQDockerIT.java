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
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

/**
 * Checks that our clients can connect and interact with ActiveMQ.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RabbitMQDockerIT extends AbstractClientIT {

  ///etc/rabbitmq/enabled_plugins

  @ClassRule
  public static final GenericContainer rabbitmq
    = new GenericContainer("rabbitmq:latest")
            .withClasspathResourceMapping("integration/rabbitmq/enabled_plugins",
              "/etc/rabbitmq/enabled_plugins", BindMode.READ_ONLY)
            .withExposedPorts(61613);

  @Override
  public StompClientOptions getOptions() {
    return new StompClientOptions()
      .setHost(getDockerHost())
      .setPort(rabbitmq.getMappedPort(61613))
      .setBypassHostHeader(true);
  }

  @Override
  public StompClientOptions getOptionsWithSSL() {
    return null;
  }

}
