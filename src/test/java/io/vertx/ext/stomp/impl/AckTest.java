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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks the {@code ACK} and {@code NACK} handling.
 */
@RunWith(VertxUnitRunner.class)
public class AckTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();
  private List<Frame> acked = new ArrayList<>();
  private List<Frame> nacked = new ArrayList<>();


  @Before
  public void setUp(TestContext context) throws InterruptedException {
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx)
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
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), connection::ack);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !acked.isEmpty());
  }

  @Test
  public void testSimpleNack() {
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      if (ar.failed()) {
        System.err.println("========> FAILURE <========");
        ar.cause().printStackTrace();
        return;
      }
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), connection::nack);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() ->
            server.stompHandler().getDestinations().contains("/queue")
    );

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !nacked.isEmpty());
    assertThat(acked).isEmpty();
  }

  @Test
  public void testCumulativeAck() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          connection.ack(frame.getAck());
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testCumulativeNack() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), (frame) -> {
        frames.add(frame);
        if (frames.size() == 3) {
          connection.nack(frame.getAck());
        }
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }


  @Test
  public void testIndividualAck() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
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

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testIndividualNack() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
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

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
      connection.send("/queue", Buffer.buffer("World"));
      connection.send("/queue", Buffer.buffer("!!!"));

    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }

  @Test
  public void testTimeoutNack() {
    server.options().setAckTimeout(10);
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), frame -> {
      });
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !nacked.isEmpty());
    assertThat(acked).isEmpty();
  }

  @Test
  public void testAckInTransaction() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> {
            if (frames.isEmpty()) {
              connection.beginTX("my-tx");
            }
            frames.add(frame);
            connection.ack(frame, "my-tx");
            if (frames.size() == 3) {
              connection.commit("my-tx");
            }
          }
      );
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(acked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(acked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> acked.size() == 3);
  }

  @Test
  public void testNackInTransaction() {
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> {
            if (frames.isEmpty()) {
              connection.beginTX("my-tx");
            }
            frames.add(frame);
            connection.nack(frame, "my-tx");
            if (frames.size() == 3) {
              connection.commit("my-tx");
            }
          }
      );
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }

  @Test
  public void testTimeoutNackOnTransactionAbort() {
    server.options().setAckTimeout(100);
    List<Frame> frames = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> {
            if (frames.isEmpty()) {
              connection.beginTX("my-tx");
            }
            frames.add(frame);
            connection.ack(frame, "my-tx");
            if (frames.size() == 3) {
              connection.abort("my-tx");
            }
          }
      );
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    assertThat(nacked.isEmpty());

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"));
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> nacked.size() == 3);
  }

  @Test
  public void testUnknownMessageInAck(TestContext context) {
    Async async = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> context.fail("unexpected error"));
      connection.ack("unknown", frame -> {
        async.complete();
      });
    }));

    Async async2 = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
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
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> async.complete());
      connection.ack(new Frame(Frame.Command.SEND,
              Headers.create("ack", "id"), null),
          "unknown", frame -> context.fail("unexpected receipt"));
    }));

    Async async2 = context.async();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> async2.complete());
      connection.ack(new Frame(Frame.Command.SEND,
              Headers.create("ack", "id"), null),
          "unknown", frame -> context.fail("unexpected receipt"));
    }));
  }
}
