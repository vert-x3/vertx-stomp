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

/**
 * Checks that our clients can connect and interact with Cloud AMQP (RabbitMQ as a service).
 * We use a free instance for this test:
 * <p>
 * URL: moose.rmq.cloudamqp.com
 * Username and vhost: xvjvsrrc
 * Password: VbuL1atClKt7zVNQha0bnnScbNvGiqgb
 * Full AMQP url: amqp://xvjvsrrc:VbuL1atClKt7zVNQha0bnnScbNvGiqgb@moose.rmq.cloudamqp.com/xvjvsrrc
 * <p>
 * Do not abuse from this instance, it's pretty limited.
 * <p>
 * A `box` "queue" has been created on this instance (durable:true, auto-delete: false, no other options).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CloudAmqpIT extends AbstractClientIT {

  @Override
  public StompClientOptions getOptions() {
    return new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61613)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc");
  }

  @Override
  public StompClientOptions getOptionsWithSSL() {
    return new StompClientOptions()
        .setHost("moose.rmq.cloudamqp.com")
        .setPort(61614)
        .setLogin("xvjvsrrc").setPasscode("VbuL1atClKt7zVNQha0bnnScbNvGiqgb")
        .setVirtualHost("xvjvsrrc")
        .setSsl(true);
  }
}
