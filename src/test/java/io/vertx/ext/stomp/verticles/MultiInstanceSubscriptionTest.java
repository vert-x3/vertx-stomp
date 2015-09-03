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

package io.vertx.ext.stomp.verticles;

import com.jayway.awaitility.Awaitility;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.impl.AsyncLock;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(io.vertx.ext.unit.junit.VertxUnitRunner.class)
public class MultiInstanceSubscriptionTest {

  private Vertx vertx;
  private String deploymentId;

  @Before
  public void setUp() {
    ReceiverStompClient.FRAMES.clear();
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    AsyncLock<Void> lock = new AsyncLock<>();
    if (deploymentId != null) {
      vertx.undeploy(deploymentId, lock.handler());
    }
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void testThatTopicSubscriptionsAreShared(TestContext context) {
    vertx.deployVerticle("io.vertx.ext.stomp.verticles.StompServerVerticle", new DeploymentOptions().setInstances(3),
        ar -> {
          if (ar.failed()) {
            context.fail(ar.cause());
          } else {
            deploymentId = ar.result();
            // Deploy the clients.
            vertx.deployVerticle("io.vertx.ext.stomp.verticles.ReceiverStompClient",
                new DeploymentOptions().setInstances(3), ar2 -> {
              if (ar.failed()) {
                context.fail(ar.cause());
              } else {
                vertx.deployVerticle("io.vertx.ext.stomp.verticles.TxSenderStompClient", new DeploymentOptions()
                    .setInstances(2));
              }
            });
          }
        });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> ReceiverStompClient.FRAMES.size() == 5 * 3 * 2);
  }

  @Test
  public void testThatQueueSubscriptionsAreShared(TestContext context) {
    vertx.deployVerticle("io.vertx.ext.stomp.verticles.StompServerVerticle", new DeploymentOptions()
            .setConfig(new JsonObject().put("useQueue", true))
            .setInstances(3),
        ar -> {
          if (ar.failed()) {
            context.fail(ar.cause());
          } else {
            deploymentId = ar.result();
            // Deploy the clients.
            vertx.deployVerticle("io.vertx.ext.stomp.verticles.ReceiverStompClient",
                new DeploymentOptions().setInstances(3), ar2 -> {
                  if (ar.failed()) {
                    context.fail(ar.cause());
                  } else {
                    vertx.deployVerticle("io.vertx.ext.stomp.verticles.TxSenderStompClient", new DeploymentOptions()
                        .setInstances(2));
                  }
                });
          }
        });

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> ReceiverStompClient.FRAMES.size() == 5 * 2);
  }
}
