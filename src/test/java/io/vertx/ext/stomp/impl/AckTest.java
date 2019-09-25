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
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks the {@code ACK} and {@code NACK} handling.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class AckTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();
  private List<Frame> acked = new CopyOnWriteArrayList<>();
  private List<Frame> nacked = new CopyOnWriteArrayList<>();


  @Before
  public void setUp(TestContext context) throws InterruptedException {
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .destinationFactory(new QueueManagingAcknowledgmentsFactory())
            .onAckHandler(acknowledgement -> acked.addAll(acknowledgement.frames()))
            .onNackHandler(acknowledgement -> nacked.addAll(acknowledgement.frames())))
        .listen(lock.handler());

    lock.waitForSuccess();
  }

  @After
  public void tearDown(TestContext context) {
    clients.forEach(StompClient::close);
    clients.clear();
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }


  @Test
  public void testSimpleAck() {
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), frame -> connection.ack(frame.getAck()));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !acked.isEmpty());
  }

  @Test
  public void testSimpleNack() {
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), frame -> connection.nack(frame.getAck()));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            Helper.hasDestination(server.stompHandler().getDestinations(), "/queue")
    );

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !nacked.isEmpty());
    assertThat(acked).isEmpty();
  }

  @Test
  public void testCumulativeAck() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          connection.ack(frame.getAck());
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));
      connection.send("/queue", Buffer.buffer("not acknowledged"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testCumulativeNack() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          connection.nack(frame.getAck());
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));
      connection.send("/queue", Buffer.buffer("not acknowledged"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }


  @Test
  public void testIndividualAck() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client-individual"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          for (Frame f : frames) {
            connection.ack(f.getAck());
          }
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testIndividualNack() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client-individual"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          for (Frame f : frames) {
            connection.nack(f.getAck());
          }
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }


  @Test
  public void testAckInTransaction() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> {
            if (frames.isEmpty()) {
              connection.beginTX("my-tx");
            }
            frames.add(frame);
            connection.ack(frame.getAck(), "my-tx");
            if (frames.size() == 3) {
              connection.commit("my-tx");
            }
          }
      );
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(acked.isEmpty());

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(acked.isEmpty());

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testNackInTransaction() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> {
            if (frames.isEmpty()) {
              connection.beginTX("my-tx");
            }
            frames.add(frame);
            connection.nack(frame.getAck(), "my-tx");
            if (frames.size() == 3) {
              connection.commit("my-tx");
            }
          }
      );
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }

  @Test
  public void testUnknownMessageInAck(TestContext context) {
    Async async = context.async();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> context.fail("unexpected error"));
      connection.ack("unknown", frame -> {
        async.complete();
      });
    }));

    Async async2 = context.async();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> context.fail("unexpected error"));
      connection.nack("unknown", frame -> {
        async2.complete();
      });
    }));
  }

  @Test
  public void testWrongTransactionIdInAckAndNack(TestContext context) {
    Async async = context.async();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> async.complete());
      connection.ack("id",
          "unknown", frame -> context.assertTrue(frame.failed(), "unexpected receipt"));
    }));

    Async async2 = context.async();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> async2.complete());
      connection.ack("id",
          "unknown", frame -> context.assertTrue(frame.failed(), "unexpected receipt"));
    }));
  }

  @Test
  public void testSubscriptionAndTwoReceptionsWithNackInClientMode() {
    List<Frame> frames1 = new CopyOnWriteArrayList<>();
    List<Frame> frames2 = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client"), frame -> {
        frames1.add(frame);
        if (frames1.size() == 2) {
          connection.nack(frame.getAck());
        }
      });
    }));
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client"), frames2::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      final Destination destination = server.stompHandler().getDestination("/queue");
      return destination != null && destination.numberOfSubscriptions() == 2;
    });

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            frames2.size() == 4
    );
  }

  @Test
  public void testSubscriptionAndTwoReceptionsWithNack() {
    List<Frame> frames2 = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client-individual"), frame -> {
        connection.nack(frame.getAck());
      });
    }));
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client-individual"), frames2::add);
    }));


    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      final Destination destination = server.stompHandler().getDestination("/queue");
      return destination != null && destination.numberOfSubscriptions() == 2;
    });

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            frames2.size() == 2
    );

  }
}
