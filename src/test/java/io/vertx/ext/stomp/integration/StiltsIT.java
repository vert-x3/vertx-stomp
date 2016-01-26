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

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.impl.AsyncLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.projectodd.stilts.stomp.*;
import org.projectodd.stilts.stomp.client.ClientSubscription;
import org.projectodd.stilts.stomp.client.StompClient;

import javax.net.ssl.SSLException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that the Stilts client can connect to our STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StiltsIT {

  protected Vertx vertx;
  protected StompServer server;

  @Before
  public void setUp() {
    AsyncLock<StompServer> lock = new AsyncLock<>();
    vertx = Vertx.vertx();
    server = StompServer.create(vertx).handler(StompServerHandler.create(vertx)).listen(lock.handler());
    lock.waitForSuccess();
  }

  @After
  public void tearDown() {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void test() throws URISyntaxException, InterruptedException, TimeoutException, StompException, SSLException {
    StompClient client1 = new StompClient("stomp://localhost:61613");
    StompClient client2 = new StompClient("stomp://localhost:61613");
    client1.connect();
    client2.connect();

    AtomicReference<StompMessage> frame = new AtomicReference<>();

    ClientSubscription subscription1 =
        client1.subscribe( "box" )
            .withMessageHandler(frame::set)
            .start();

    Headers headers = new DefaultHeaders();
    headers.put("header", "value");
    client2.send(StompMessages.createStompMessage("box", headers, "hello !"));

    await().atMost(10, TimeUnit.SECONDS).until(()-> frame.get() != null);

    assertThat(frame.get().getDestination()).isEqualTo("box");
    assertThat(frame.get().getContentAsString()).isEqualTo("hello !");
    assertThat(frame.get().getHeaders().get("header")).isEqualTo("value");
    assertThat(frame.get().getHeaders().get("message-id")).isNotNull();
    assertThat(frame.get().getHeaders().get("subscription")).isNotNull();

    subscription1.unsubscribe();
    client1.disconnect();
    client2.disconnect();
  }
}
