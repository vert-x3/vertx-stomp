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
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the {@code RECEIPT} frame.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReceiptTest {

  private Vertx vertx;
  private StompServer server;

  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    AsyncLock<StompServer> lock = new AsyncLock<>();
    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(lock.handler());
    lock.waitForSuccess();
  }

  @After
  public void tearDown() {
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
  public void testReceiptsOnSend() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));
    assertThat(receipts).hasSize(1);
    assertThat(receipts.get(0).succeeded()).isTrue();
    assertThat(receipts.get(0).result().toString()).isEqualTo("/queue");

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> !frames.isEmpty());
    assertThat(receipts).hasSize(2);
  }


  @Test
  public void testReceiptsOnSubscribeAndUnsubscribe() {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();
    AtomicReference<StompClientConnection> client = new AtomicReference<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      client.set(connection);
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
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
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> connection.ack(frame.getAck()),
          receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 2);
  }

  @Test
  public void testReceiptsWithNack() {
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", Headers.create(Frame.ACK, "client"),
          frame -> connection.nack(frame.getAck()),
          receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> Helper.hasDestination(server.stompHandler().getDestinations(), "/queue"));

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/queue", Buffer.buffer("Hello"), receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 2);
  }

  @Test
  public void testReceiptsInCommittedTransactions() {
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();
    List<Frame> frames = new CopyOnWriteArrayList<>();
    List<Frame> errors = new CopyOnWriteArrayList<>();
    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 1);

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(errors::add);
      connection.beginTX("my-tx", receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("Hello")), receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx").setBody(
          Buffer.buffer("World")), receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("!!!")), receipts::add);
      connection.commit("my-tx", receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 3 && errors.isEmpty()
        && receipts.size() == 6);
  }

  @Test
  public void testReceiptsInAbortedTransactions() throws InterruptedException {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    List<Frame> errors = new CopyOnWriteArrayList<>();
    List<AsyncResult<?>> receipts = new CopyOnWriteArrayList<>();

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/queue", frames::add, receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> receipts.size() == 1);

    clients.add(StompClient.create(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(errors::add);
      connection.beginTX("my-tx", receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("Hello")), receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx").setBody(
          Buffer.buffer("World")), receipts::add);
      connection.send(new Frame().setCommand(Command.SEND).setDestination("/queue").setTransaction("my-tx")
          .setBody(Buffer.buffer("!!!")), receipts::add);
      connection.abort("my-tx", receipts::add);
    }));

    Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 0 && errors.isEmpty()
        && receipts.size() == 6);
  }

}
