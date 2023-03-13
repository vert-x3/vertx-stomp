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

import com.jayway.awaitility.Awaitility;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the client subscriptions and message delivery.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class SubscriptionsUsingQueueTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    AsyncLock<StompServer> lock = new AsyncLock<>();
    vertx = Vertx.vertx();
    server =StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx).destinationFactory(Destination::queue));
    server.listen().onComplete(lock.handler());
    lock.waitForSuccess();
  }

  @After
  public void tearDown() {
    AsyncLock<Void> lock = new AsyncLock<>();
    clients.forEach(StompClient::close);
    clients.clear();
    server.close().onComplete(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock<>();
    vertx.close().onComplete(lock.handler());
    lock.waitForSuccess();
  }

  private void client(Handler<AsyncResult<StompClientConnection>> handler) {
    StompClient client = StompClient.create(vertx);
    clients.add(client);
    client.connect().onComplete(handler);
  }

  @Test
  public void testSubscriptionAndReceptionUsingQueue() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
  }

  @Test
  public void testThatCustomHeadersArePropagatedWhenUsingQueue() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Headers.create("foo", "bar", "toto", "titi"), Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader("foo")).isEqualTo("bar");
    assertThat(frames.get(0).getHeader("toto")).isEqualTo("titi");
  }

  @Test
  public void testSubscriptionAndTwoReceptions() {
    List<Frame> frames1 = new CopyOnWriteArrayList<>();
    List<Frame> frames2 = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames1::add);
    }));
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames2::add);
    }));


    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("/queue").numberOfSubscriptions() == 2);

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            frames1.size() == 2 && frames2.size() == 2
    );

    List<Frame> helloList;
    List<Frame> vertxList;
    if (frames1.get(0).getBodyAsString().equalsIgnoreCase("Hello")) {
      helloList = frames1;
      vertxList = frames2;
    } else {
      helloList = frames2;
      vertxList = frames1;
    }

    assertThat(helloList.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(helloList.get(0).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(helloList.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(helloList.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(helloList.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(helloList.get(1).getBodyAsString()).isEqualTo("Hello");
    assertThat(helloList.get(1).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(helloList.get(1).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(helloList.get(1).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(helloList.get(1).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();

    assertThat(vertxList.get(0).getBodyAsString()).isEqualTo("vert.x");
    assertThat(vertxList.get(0).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(vertxList.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(vertxList.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(1).getBodyAsString()).isEqualTo("vert.x");
    assertThat(vertxList.get(1).getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(vertxList.get(1).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(1).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(vertxList.get(1).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
  }

  @Test
  public void testWhenNoSubscriptionsWhenUsingQueue() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue2", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getCommand()).isEqualTo(Command.ERROR);
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isEqualTo("/queue");
    assertThat(frames.get(0).getBodyAsString()).contains("no subscriptions");
  }

  @Test
  public void testMultipleSubscriptionsWithIdsOnQueues() {
    server.options().setSendErrorOnNoSubscriptions(true);

    Map<String, Frame> frames = new HashMap<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create().add(Frame.ID, "0"), f -> frames.put("/queue", f));
      connection.subscribe("/queue2", Headers.create().add(Frame.ID, "1"), f -> frames.put("/queue2", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));
    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue2", Buffer.buffer("World"));
      connection.errorHandler(f -> frames.put("error", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 2);

    assertThat(frames).hasSize(2);
    assertThat(frames).doesNotContainKeys("error");

    Frame frame1 = frames.get("/queue");
    assertThat(frame1.getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frame1.getHeader(Frame.DESTINATION)).isEqualTo("/queue");
    assertThat(frame1.getHeader(Frame.SUBSCRIPTION)).isEqualTo("0");
    assertThat(frame1.getBodyAsString()).isEqualTo("Hello");

    frame1 = frames.get("/queue2");
    assertThat(frame1.getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frame1.getHeader(Frame.DESTINATION)).isEqualTo("/queue2");
    assertThat(frame1.getHeader(Frame.SUBSCRIPTION)).isEqualTo("1");
    assertThat(frame1.getBodyAsString()).isEqualTo("World");
  }

  @Test
  public void testUnsubscriptionWithDefaultIdUsingQueue() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frame -> {
        frames.add(frame);
        connection.unsubscribe("/queue");
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Command.ERROR);
  }

  @Test
  public void testUnsubscriptionWithCustomId() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ID, "0"), frame -> {
        frames.add(frame);
        connection.unsubscribe("/queue", Headers.create(Frame.ID, "0"));
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Command.ERROR);
  }

  @Test
  public void testMultipleConnectionAndClosing() {
    for (int i = 0; i < 20; i++) {
      testClosingConnection();
    }
  }

  @Test
  public void testClosingConnection() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    StompClient client = StompClient.create(vertx);
    client.connect().onComplete(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
      connection.subscribe("/queue2", (frames::add));

    });
    clients.add(client);

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() == 2);

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue2", Buffer.buffer("World"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2);

    client.close();

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() == 0);
  }

  @Test
  public void testLeavingSubscriptions() {
    List<Frame> frames1 = new CopyOnWriteArrayList<>();
    List<Frame> frames2 = new CopyOnWriteArrayList<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames1::add);
    }));
    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frame -> {
        frames2.add(frame);
        if (frames2.size() == 2) {
          connection.unsubscribe("/queue");
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("/queue") != null  &&
        server.stompHandler().getDestination("/queue").numberOfSubscriptions() == 2);

    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    client((ar -> {
      final StompClientConnection connection = ar.result();
      reference.set(connection);
      connection.send("/queue", Buffer.buffer("1"));
      connection.send("/queue", Buffer.buffer("2"));
      connection.send("/queue", Buffer.buffer("3"));
      connection.send("/queue", Buffer.buffer("4"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("/queue") != null  &&
        server.stompHandler().getDestination("/queue").numberOfSubscriptions() == 1);

    vertx.runOnContext(v -> {
      reference.get().send("/queue", Buffer.buffer("5"));
      reference.get().send("/queue", Buffer.buffer("6"));
    });

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames1.size() == 4 && frames2.size() == 2);
  }
}
