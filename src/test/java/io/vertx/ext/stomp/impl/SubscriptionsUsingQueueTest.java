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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the client subscriptions and message delivery.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class SubscriptionsUsingQueueTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx).destinationFactory(Destination::queue))
        .listen(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    clients.forEach(StompClient::close);
    clients.clear();
    server.close(context.asyncAssertSuccess());
    vertx.close(context.asyncAssertSuccess());
  }


  @Test
  public void testSubscriptionAndReception() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
  }

  @Test
  public void testThatCustomHeadersArePropagated() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Headers.create("foo", "bar", "toto", "titi"), Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader("foo")).isEqualTo("bar");
    assertThat(frames.get(0).getHeader("toto")).isEqualTo("titi");
  }

  @Test
  public void testSubscriptionAndTwoReceptions() {
    List<Frame> frames1 = new ArrayList<>();
    List<Frame> frames2 = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames1::add);
    }));
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames2::add);
    }));


    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestination("/queue").numberOfSubscriptions() == 2);

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
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
    assertThat(helloList.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(helloList.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(helloList.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(helloList.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(helloList.get(1).getBodyAsString()).isEqualTo("Hello");
    assertThat(helloList.get(1).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(helloList.get(1).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(helloList.get(1).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(helloList.get(1).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();

    assertThat(vertxList.get(0).getBodyAsString()).isEqualTo("vert.x");
    assertThat(vertxList.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(vertxList.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(vertxList.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(1).getBodyAsString()).isEqualTo("vert.x");
    assertThat(vertxList.get(1).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(vertxList.get(1).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(vertxList.get(1).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/queue");
    assertThat(vertxList.get(1).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
  }


  @Test
  public void testSubscriptionAndTwoReceptionsWithNack() {
    List<Frame> frames2 = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client-individual"), frame -> {
        connection.nack(frame.getAck());
      });
    }));
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client-individual"), frames2::add);
    }));


    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      final Destination destination = server.stompHandler().getDestination("/queue");
      return destination != null && destination.numberOfSubscriptions() == 2;
    });

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("vert.x"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            frames2.size() == 2
    );

  }

  @Test
  public void testSubscriptionAndTwoReceptionsWithNackInClientMode() {
    List<Frame> frames1 = new ArrayList<>();
    List<Frame> frames2 = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client"), frame -> {
        frames1.add(frame);
        if (frames1.size() == 2) {
          connection.nack(frame.getAck());
        }
      });
    }));
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("ack", "client"), frames2::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      final Destination destination = server.stompHandler().getDestination("/queue");
      return destination != null && destination.numberOfSubscriptions() == 2;
    });

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
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
  public void testSendingWithoutDestination(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      try {
        connection.send((String) null, Buffer.buffer("hello"));
        context.fail("Exception expected");
      } catch (IllegalArgumentException e) {
        async.complete();
      }
    }));
  }

  @Test
  public void testSendingWithHeadersButWithoutDestination(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      try {
        connection.send(Headers.create("foo", "bar"), Buffer.buffer("hello"));
        context.fail("Exception expected");
      } catch (IllegalArgumentException e) {
        async.complete();
      }
    }));
  }

  @Test
  public void testWhenNoSubscriptions() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue2", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.ERROR);
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isEqualTo("/queue");
    assertThat(frames.get(0).getBodyAsString()).contains("no subscriptions");
  }

  @Test
  public void testMultipleSubscriptionsWithIds() {
    server.options().setSendErrorOnNoSubscriptions(true);

    Map<String, Frame> frames = new HashMap<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create().add(Frame.ID, "0"), f -> frames.put("/queue", f));
      connection.subscribe("/queue2", Headers.create().add(Frame.ID, "1"), f -> frames.put("/queue2", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));
    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue2", Buffer.buffer("World"));
      connection.errorHandler(f -> frames.put("error", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 2);

    assertThat(frames).hasSize(2);
    assertThat(frames).doesNotContainKeys("error");

    Frame frame1 = frames.get("/queue");
    assertThat(frame1.getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frame1.getHeader(Frame.DESTINATION)).isEqualTo("/queue");
    assertThat(frame1.getHeader(Frame.SUBSCRIPTION)).isEqualTo("0");
    assertThat(frame1.getBodyAsString()).isEqualTo("Hello");

    frame1 = frames.get("/queue2");
    assertThat(frame1.getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frame1.getHeader(Frame.DESTINATION)).isEqualTo("/queue2");
    assertThat(frame1.getHeader(Frame.SUBSCRIPTION)).isEqualTo("1");
    assertThat(frame1.getBodyAsString()).isEqualTo("World");
  }

  @Test
  public void testUnsubscriptionWithDefaultId() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frame -> {
        frames.add(frame);
        connection.unsubscribe("/queue");
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Frame.Command.ERROR);
  }

  @Test
  public void testUnsubscriptionWithCustomId() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ID, "0"), frame -> {
        frames.add(frame);
        connection.unsubscribe("/queue", Headers.create(Frame.ID, "0"));
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Frame.Command.ERROR);
  }

  @Test
  public void testSubscriptionsUsingTheSameDefaultId(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frame -> {
      });
      try {
        connection.subscribe("/queue", frame -> {
        });
        context.fail("Exception expected");
      } catch (IllegalArgumentException e) {
        async.complete();
      }
    }));
  }

  @Test
  public void testSubscriptionsUsingTheSameCustomId(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create("id", "0"), frame -> {
      });
      try {
        connection.subscribe("/queue2", Headers.create("id", "0"), frame -> {
        });
        context.fail("Exception expected");
      } catch (IllegalArgumentException e) {
        async.complete();
      }
    }));
  }


  @Test
  public void testSubscriptionsUsingTheSameDestinationButDifferentId(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      try {
        connection.subscribe("/queue", Headers.create(Frame.ID, "0"), frame -> {
        });
        connection.subscribe("/queue", Headers.create(Frame.ID, "1"), frame -> {
        });
        async.complete();
      } catch (IllegalArgumentException e) {
        context.fail("Exception unexpected");
      }
    }));
  }

  @Test
  public void testClosingConnection() {
    List<Frame> frames = new ArrayList<>();
    StompClient client = Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", (frames::add));
      connection.subscribe("/queue2", (frames::add));

    });
    clients.add(client);

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() == 2);

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue2", Buffer.buffer("World"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2);

    client.close();

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() == 0);
  }
}
