package io.vertx.ext.stomp.impl;

import com.jayway.awaitility.Awaitility;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.*;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A couple of load tests that needs to be launched manually.
 */
@RunWith(VertxUnitRunner.class)
@Ignore("Manual launch only")
public class LoadTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();
  private List<Frame> acked = new ArrayList<>();
  private List<Frame> nacked = new ArrayList<>();


  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = Stomp.createStompServer(vertx, new StompServerOptions().setHeartbeat(
        new JsonObject().put("x", 0).put("y", 0)))
        .handler(StompServerHandler.create(vertx)
            .onAckHandler(acknowledgement -> acked.addAll(acknowledgement.frames()))
            .onNackHandler(acknowledgement -> nacked.addAll(acknowledgement.frames())))
        .listen(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    System.out.println("Closing clients");
    clients.forEach(StompClient::close);
    System.out.println("Closing server");
    server.close(context.asyncAssertSuccess());
    vertx.close(context.asyncAssertSuccess());
  }


  @Test
  public void testWithMultiplePublisherAndConsumerOnOneDestination() {
    int publisher = 100;
    int consumer = 100;
    String dest = "/queue";
    int numberOfMessagePerPublisher = 100;

    AtomicInteger received = new AtomicInteger();
    AtomicInteger started = new AtomicInteger();

    // Init consumers
    for (int i = 0; i < consumer; i++) {

      clients.add(Stomp.createStompClient(vertx)
          .connect(ar -> {
            if (ar.failed()) {
              System.err.println("Consumer connection error " + ar.cause().getMessage());
              ar.cause().printStackTrace();
              return;
            }
            ar.result()
                .errorHandler(frame -> System.err.println("Consumer Error : " + frame))
                .subscribe(dest, frame -> {
                      received.incrementAndGet();
                    },
                    frame -> started.incrementAndGet()
                );
          }));
    }

    Awaitility.waitAtMost(1, TimeUnit.MINUTES).until(() -> started.get() == consumer);

    long begin = System.currentTimeMillis();
    // Init producers
    AtomicInteger global = new AtomicInteger();
    for (int i = 0; i < publisher; i++) {
      String p = Integer.toString(i);
      clients.add(StompClient.create(vertx).connect(ar -> {
        final StompClientConnection connection = ar.result();
        connection.errorHandler(frame -> System.err.println("Producer Error : " + frame));
        AtomicInteger count = new AtomicInteger();
        vertx.setPeriodic(10, id -> {
          connection.send(dest, Buffer.buffer("Hello"));
          global.incrementAndGet();
          if (count.incrementAndGet() == numberOfMessagePerPublisher) {
            vertx.cancelTimer(id);
            connection.disconnect();
          }
        });
      }));
    }

    Awaitility.await().atMost(1, TimeUnit.MINUTES).until(() -> {
      int size = received.get();
      return size == publisher * numberOfMessagePerPublisher * consumer;
    });

    long end = System.currentTimeMillis();
    System.out.println(received.get() + " messages delivered in " + (end - begin) + " ms");
  }


  @Test
  public void testWithASinglePublisherAndMultipleConsumersOnOneDestination() {
    int publisher = 1;
    int consumer = 200;
    String dest = "/queue";
    int numberOfMessagePerPublisher = 800;

    AtomicInteger received = new AtomicInteger();
    AtomicInteger started = new AtomicInteger();

    // Init consumers
    for (int i = 0; i < consumer; i++) {

      clients.add(Stomp.createStompClient(vertx)
          .connect(ar -> {
            if (ar.failed()) {
              System.err.println("Consumer connection error " + ar.cause().getMessage());
              ar.cause().printStackTrace();
              return;
            }
            ar.result()
                .errorHandler(frame -> System.err.println("Consumer Error : " + frame))
                .subscribe(dest, frame -> {
                      received.incrementAndGet();
                    },
                    frame -> started.incrementAndGet()
                );
          }));
    }
    Awaitility.waitAtMost(1, TimeUnit.MINUTES).until(() -> started.get() == consumer);

    long begin = System.currentTimeMillis();
    // Init producers
    AtomicInteger global = new AtomicInteger();
    for (int i = 0; i < publisher; i++) {
      String p = Integer.toString(i);
      clients.add(StompClient.create(vertx).connect(ar -> {
        final StompClientConnection connection = ar.result();
        connection.errorHandler(frame -> System.err.println("Producer Error : " + frame));
        AtomicInteger count = new AtomicInteger();
        vertx.setPeriodic(10, id -> {
          connection.send(dest, Buffer.buffer("Hello"));
          global.incrementAndGet();
          if (count.incrementAndGet() == numberOfMessagePerPublisher) {
            vertx.cancelTimer(id);
          }
        });
      }));
    }

    Awaitility.await().atMost(1, TimeUnit.MINUTES).until(() -> {
      int size = received.get();
      return size == publisher * numberOfMessagePerPublisher * consumer;
    });

    long end = System.currentTimeMillis();
    System.out.println(received.get() + " messages delivered in " + (end - begin) + " ms");
  }
}
