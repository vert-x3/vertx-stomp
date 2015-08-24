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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the client subscriptions and message delivery.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class SubscriptionsUsingTopicTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx))
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
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/topic");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
  }

  @Test
  public void testThatCustomHeadersArePropagated() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Headers.create("foo", "bar", "toto", "titi"), Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/topic");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader("foo")).isEqualTo("bar");
    assertThat(frames.get(0).getHeader("toto")).isEqualTo("titi");
  }

  @Test
  public void testSubscriptionAndTwoReceptions() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", (frames::add));
    }));
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      return frames.size() == 2;
    });

    assertThat(frames).hasSize(2);
    assertThat(frames.get(0).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(0).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/topic");
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
    assertThat(frames.get(0).getHeader(Frame.CONTENT_LENGTH)).isEqualTo("5"); // Content length as string.
    assertThat(frames.get(1).getBodyAsString()).isEqualTo("Hello");
    assertThat(frames.get(1).getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frames.get(1).getHeader(Frame.MESSAGE_ID)).isNotNull().isNotEmpty();
    assertThat(frames.get(1).getHeader(Frame.SUBSCRIPTION)).isEqualTo("/topic");
    assertThat(frames.get(1).getHeader(Frame.DESTINATION)).isNotNull().isNotEmpty();
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

    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue2", (frames::add));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());

    assertThat(frames).hasSize(1);
    assertThat(frames.get(0).getCommand()).isEqualTo(Frame.Command.ERROR);
    assertThat(frames.get(0).getHeader(Frame.DESTINATION)).isEqualTo("/topic");
    assertThat(frames.get(0).getBodyAsString()).contains("no subscriptions");
  }

  @Test
  public void testMultipleSubscriptionsWithIds() {
    server.options().setSendErrorOnNoSubscriptions(true);

    Map<String, Frame> frames = new HashMap<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", Headers.create().add(Frame.ID, "0"), f -> frames.put("/topic", f));
      connection.subscribe("/queue2", Headers.create().add(Frame.ID, "1"), f -> frames.put("/queue2", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue2"));
    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
      connection.send("/queue2", Buffer.buffer("World"));
      connection.errorHandler(f -> frames.put("error", f));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 2);

    assertThat(frames).hasSize(2);
    assertThat(frames).doesNotContainKeys("error");

    Frame frame1 = frames.get("/topic");
    assertThat(frame1.getCommand()).isEqualTo(Frame.Command.MESSAGE);
    assertThat(frame1.getHeader(Frame.DESTINATION)).isEqualTo("/topic");
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

    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", frame -> {
        frames.add(frame);
        connection.unsubscribe("/topic");
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/topic", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frames::add);
      connection.send("/topic", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Frame.Command.ERROR);
  }

  @Test
  public void testUnsubscriptionWithCustomId() {
    server.options().setSendErrorOnNoSubscriptions(true);

    List<Frame> frames = new CopyOnWriteArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", Headers.create(Frame.ID, "0"), frame -> {
        frames.add(frame);
        connection.unsubscribe("/topic", Headers.create(Frame.ID, "0"));
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !Helper.hasDestination(server.stompHandler().getDestinations(), "/topic"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
      connection.errorHandler(frames::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2 && frames.get(1).getCommand() == Frame.Command.ERROR);
  }

  @Test
  public void testSubscriptionsUsingTheSameDefaultId(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", frame -> {
      });
      try {
        connection.subscribe("/topic", frame -> {
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
      connection.subscribe("/topic", Headers.create("id", "0"), frame -> {
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
        connection.subscribe("/topic", Headers.create(Frame.ID, "0"), frame -> {
        });
        connection.subscribe("/topic", Headers.create(Frame.ID, "1"), frame -> {
        });
        async.complete();
      } catch (IllegalArgumentException e) {
        context.fail("Exception unexpected");
      }
    }));
  }

  @Test
  public void testClosingConnection() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    StompClient client = Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/topic", (frames::add));
      connection.subscribe("/queue2", (frames::add));

    });
    clients.add(client);

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().size() == 2);

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/topic", Buffer.buffer("Hello"));
    }));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue2", Buffer.buffer("World"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2);

    client.close();

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> {
      return server.stompHandler().getDestinations().size() == 0;
    });
  }
}
