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

package io.vertx.ext.stomp.impl;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the received and writing frame handler
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class FrameHandlerTest {

  private Vertx vertx;
  private StompServer server;
  private StompClient client;
  private Buffer UNKNOWN_FRAME = Buffer.buffer("YEAH\nfoo:val\n\nMy body...")
      .appendString(FrameParser.NULL);

  // The last connection received by the server.
  private StompServerConnection connection;
  private List<Frame> receivedByServer = new ArrayList<>();
  private List<Frame> receivedByClient = new ArrayList<>();
  private List<Frame> writtenByServer = new ArrayList<>();

  @Before
  public void setUp() {
    AsyncLock<StompServer> lock = new AsyncLock<>();
    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .receivedFrameHandler(frame -> {
              frame.frame().addHeader("mark", "true");
              receivedByServer.add(frame.frame());
              connection = frame.connection();
            }))
        .writingFrameHandler(frame -> {
          frame.frame().addHeader("mark", "true");
          writtenByServer.add(frame.frame());
        })
        .listen(lock.handler());
    lock.waitForSuccess();

    client = StompClient.create(vertx);
    client.receivedFrameHandler(frame -> {
      frame.addHeader("c-mark", "true");
      receivedByClient.add(frame);
    });
  }

  @After
  public void tearDown() {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();
    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
    client.close();

    receivedByClient.clear();
    receivedByServer.clear();
  }

  @Test
  public void testFrameHandler() {
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();
    client.connect(connection -> {
      reference.set(connection.result());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByServer, Command.CONNECT));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByClient, Command.CONNECTED));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(writtenByServer, Command.CONNECTED));

    reference.get().send("foo", Buffer.buffer("hello"), f -> {
      // just there to receive a reply.
    });

    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommandAndIsMarked(receivedByServer,
        Command.SEND));
    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommandAndIsMarked(receivedByClient,
        Command.RECEIPT));
    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommandAndIsMarked(writtenByServer,
        Command.RECEIPT));
  }

  @Test
  public void testFrameHandlerWithPingFrames() {
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();

    client.connect(connection -> {
      reference.set(connection.result());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByServer, Command.CONNECT));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByClient, Command.CONNECTED));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(writtenByServer, Command.CONNECTED));

    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommand(receivedByServer,
        Command.PING));
    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommand(receivedByClient,
        Command.PING));
    await().atMost(10, TimeUnit.SECONDS).until(() -> containsFrameWithCommand(writtenByServer,
        Command.PING));
  }

  @Test
  public void testFrameHandlerWithInvalidFramesReceivedByServer() throws InterruptedException {
    AtomicReference<StompClientConnection> reference = new AtomicReference<>();

    client.connect(connection -> {
      reference.set(connection.result());
    });

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByServer, Command.CONNECT));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByClient, Command.CONNECTED));

    StompClientConnectionImpl impl = (StompClientConnectionImpl) reference.get();
    NetSocket socket = impl.socket();

    socket.write(UNKNOWN_FRAME);

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByServer, Command.UNKNOWN));

    Frame frame = getFrameWithCommand(receivedByServer, Command.UNKNOWN);
    assertThat(frame).isNotNull();
    assertThat(frame.getHeader(Frame.STOMP_FRAME_COMMAND)).isEqualToIgnoringCase("YEAH");
  }

  @Test
  public void testFrameHandlerWithInvalidFramesReceivedByClient() throws InterruptedException {
    client.connect(v -> { });

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByServer, Command.CONNECT));
    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByClient, Command.CONNECTED));

    assertThat(connection).isNotNull();
    connection.write(UNKNOWN_FRAME);

    await().atMost(10, TimeUnit.SECONDS).until(() ->
        containsFrameWithCommand(receivedByClient, Command.UNKNOWN));
    Frame frame = getFrameWithCommand(receivedByClient, Command.UNKNOWN);
    assertThat(frame).isNotNull();
    assertThat(frame.getHeader(Frame.STOMP_FRAME_COMMAND)).isEqualToIgnoringCase("YEAH");
  }

  private boolean containsFrameWithCommand(List<Frame> frames, Command command) {
    for (Frame frame : frames) {
      if (frame.getCommand() == command) {
        return true;
      }
    }
    return false;
  }

  private boolean containsFrameWithCommandAndIsMarked(List<Frame> frames, Command command) {
    for (Frame frame : frames) {
      if (frame.getCommand() == command  && frame.getHeader("mark") != null) {
        return true;
      }
    }
    return false;
  }

  private Frame getFrameWithCommand(List<Frame> frames, Command command) {
    for (Frame frame : frames) {
      if (frame.getCommand() == command) {
        return frame;
      }
    }
    return null;
  }

  @Test
  public void testThatWeReceiveConnectAndDisconnectFrames() throws InterruptedException {
    List<Frame> received = new ArrayList<>();
    List<Frame> sent = new ArrayList<>();
    StompClient stompClient = StompClient.create(vertx);
    AtomicReference<StompClientConnection> ref = new AtomicReference<>();
    stompClient
      .receivedFrameHandler(frame -> {
        if (frame.getCommand() != Command.PING) {
          received.add(frame);
        }

      })
      .writingFrameHandler(frame -> {
        if (frame.getCommand() != Command.PING) {
          sent.add(frame);
        }
      })
      .connect(ar -> {
        ref.set(ar.result());
        if (ar.succeeded()) {
          ref.set(ar.result());
        } else {
          ar.cause().printStackTrace();
        }
      });

    await().until(() -> sent.stream().anyMatch(f -> f.getCommand() == Command.CONNECT));
    await().until(() -> received.stream().anyMatch(f -> f.getCommand() == Command.CONNECTED));

    ref.get().disconnect();

    await().until(() -> sent.stream().anyMatch(f -> f.getCommand() == Command.DISCONNECT));

  }

}
