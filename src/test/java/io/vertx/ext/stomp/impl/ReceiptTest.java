package io.vertx.ext.stomp.impl;

import com.jayway.awaitility.Awaitility;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the {@code RECEIPT} frame.
 */
@RunWith(VertxUnitRunner.class)
public class ReceiptTest {

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
  public void testReceiptsOnSend() {
    List<Frame> frames = new ArrayList<>();
    List<Frame> receipts = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));
    assertThat(receipts).hasSize(1);
    assertThat(receipts.get(0).toString()).contains("SUBSCRIBE", "/queue");

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());
    assertThat(receipts).hasSize(2);
  }


  @Test
  public void testReceiptsOnSubscribeAndUnsubscribe() {
    List<Frame> frames = new ArrayList<>();
    List<Frame> receipts = new ArrayList<>();
    AtomicReference<StompClientConnection> client = new AtomicReference<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      client.set(connection);
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() >= 2);
    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 3);

    client.get().unsubscribe("/queue", receipts::add);
    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 4);
  }

  @Test
  public void testReceiptsWithAck() {
    List<Frame> receipts = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), connection::ack, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 2);
  }

  @Test
  public void testReceiptsWithNack() {
    List<Frame> receipts = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"), connection::nack, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> server.stompHandler().getDestinations().contains("/queue"));

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 2);
  }

  @Test
  public void testReceiptsInCommittedTransactions() {
    List<Frame> receipts = new ArrayList<>();
    List<Frame> frames = new ArrayList<>();
    List<Frame> errors = new ArrayList<>();
    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 1);

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(errors::add);
      connection.begin("my-tx", receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("Hello")), receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx").setBody(
          Buffer.buffer("World")), receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("!!!")), receipts::add);
      connection.commit("my-tx", receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 3 && errors.isEmpty()
        && receipts.size() == 6);
  }

  @Test
  public void testReceiptsInAbortedTransactions() throws InterruptedException {
    List<Frame> frames = new ArrayList<>();
    List<Frame> errors = new ArrayList<>();
    List<Frame> receipts = new ArrayList<>();

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 1);

    clients.add(Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(errors::add);
      connection.begin("my-tx", receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("Hello")), receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx").setBody(
          Buffer.buffer("World")), receipts::add);
      connection.send(new Frame().setCommand(Frame.Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("!!!")), receipts::add);
      connection.abort("my-tx", receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 0 && errors.isEmpty()
        && receipts.size() == 6);
  }

}
