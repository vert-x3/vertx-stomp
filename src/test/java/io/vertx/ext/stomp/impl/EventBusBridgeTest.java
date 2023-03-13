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
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
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
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusBridgeTest {

  private StompServer server;
  private Vertx vertx;
  private List<StompClient> clients = new ArrayList<>();
  private List<MessageConsumer> consumers = new ArrayList<>();

  @Before
  public void setUp() {
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
                .bridge(new BridgeOptions()
                    .addInboundPermitted(new PermittedOptions().setAddress("/bus"))
                    .addOutboundPermitted(new PermittedOptions().setAddress("/bus")))
        );
    server.listen().onComplete(lock.handler());

    lock.waitForSuccess();
  }

  @After
  public void tearDown() {
    clients.forEach(StompClient::close);
    clients.clear();
    consumers.forEach(MessageConsumer::unregister);
    consumers.clear();

    AsyncLock<Void> lock = new AsyncLock<>();
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
  public void testThatStompMessagesAreTransferredToTheEventBus() {
    AtomicReference<Message> reference = new AtomicReference<>();
    consumers.add(vertx.eventBus().consumer("/bus", reference::set));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/bus", Headers.create("foo", "bar"), Buffer.buffer("Hello from STOMP"));
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);
    assertThat(reference.get().headers().get("foo")).isEqualTo("bar");
    assertThat(reference.get().headers().get("destination")).isEqualTo("/bus");
    assertThat(reference.get().headers().get("content-length")).isEqualTo("16");
    assertThat(reference.get().address()).isEqualTo("/bus");
    assertThat(reference.get().replyAddress()).isNullOrEmpty();
    assertThat(reference.get().body().toString()).isEqualTo("Hello from STOMP");
  }

  @Test
  public void testThatEventBusMessagesAreTransferredToStomp() {
    AtomicReference<Frame> reference = new AtomicReference<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection
            .subscribe("/bus", reference::set)
            .onComplete(f -> {
                vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);

    assertThat(reference.get().getHeaders().get("foo")).isEqualTo("bar");
    assertThat(reference.get().getHeaders().get("destination")).isEqualTo("/bus");
    assertThat(reference.get().getHeaders().get("content-length")).isEqualTo("17");
    assertThat(reference.get().getBodyAsString()).isEqualTo("Hello from Vert.x");
  }

  @Test
  public void testBidirectionalPingPong() {
    server.stompHandler().bridge(new BridgeOptions()
            .addInboundPermitted(new PermittedOptions().setAddressRegex("/toBu."))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex("/to.tomp"))
    );
    List<Frame> stomp = new ArrayList<>();
    List<Message> bus = new ArrayList<>();

    consumers.add(vertx.eventBus().consumer("/toBus", msg -> {
      bus.add(msg);
      if (bus.size() < 5) {
        vertx.eventBus().send("/toStomp", "pong");
      }
    }));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/toStomp", frame -> {
        stomp.add(frame);
        if (stomp.size() < 4) {
          connection.send("/toBus", Buffer.buffer("ping"));
        }
      }).onComplete(receipt -> {
        connection.send("/toBus", Buffer.buffer("ping"));
      });
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> bus.size() == 4 && stomp.size() == 4);

    for (Frame frame : stomp) {
      assertThat(frame.getBodyAsString()).isEqualTo("pong");
    }
    for (Message message : bus) {
      assertThat(message.body().toString()).isEqualTo("ping");
    }
  }

  @Test
  public void testThatEventBusMessagesContainingJsonObjectAreTransferredToStomp() {
    AtomicReference<Frame> reference = new AtomicReference<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", reference::set).onComplete(f -> {
                vertx.eventBus().publish("/bus", new JsonObject()
                        .put("name", "vert.x")
                        .put("count", 1)
                        .put("bool", true),
                    new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);

    assertThat(reference.get().getHeaders().get("foo")).isEqualTo("bar");
    assertThat(reference.get().getHeaders().get("destination")).isEqualTo("/bus");
    JsonObject object = new JsonObject(reference.get().getBodyAsString());
    assertThat(object.getString("name")).isEqualTo("vert.x");
    assertThat(object.getInteger("count")).isEqualTo(1);
    assertThat(object.getBoolean("bool")).isTrue();
  }

  @Test
  public void testThatEventBusMessagesContainingBufferAreTransferredToStomp() {
    AtomicReference<Frame> reference = new AtomicReference<>();

    byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6};
    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", reference::set).onComplete(f -> {
                vertx.eventBus().publish("/bus", Buffer.buffer(bytes),
                    new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);

    assertThat(reference.get().getHeaders().get("foo")).isEqualTo("bar");
    assertThat(reference.get().getHeaders().get("destination")).isEqualTo("/bus");
    byte[] body = reference.get().getBody().getBytes();
    assertThat(body).containsExactly(bytes);
  }

  @Test
  public void testThatEventBusMessagesContainingNoBodyAreTransferredToStomp() {
    AtomicReference<Frame> reference = new AtomicReference<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", reference::set).onComplete(f -> {
                vertx.eventBus().publish("/bus", null,
                    new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);

    assertThat(reference.get().getHeaders().get("foo")).isEqualTo("bar");
    assertThat(reference.get().getHeaders().get("destination")).isEqualTo("/bus");
    byte[] body = reference.get().getBody().getBytes();
    assertThat(body).hasSize(0);
  }

  @Test
  public void testThatTwoEventBusConsumersReceiveAStompMessage() {
    List<Message> messages = new CopyOnWriteArrayList<>();
    consumers.add(vertx.eventBus().consumer("/bus", messages::add));
    consumers.add(vertx.eventBus().consumer("/bus", messages::add));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/bus", Headers.create("foo", "bar"), Buffer.buffer("Hello from STOMP"));
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> messages.size() == 2);
  }

  @Test
  public void testThatOnlyOnEventBusConsumersReceiveAStompMessageInP2P() throws InterruptedException {
    server.stompHandler().bridge(new BridgeOptions()
            .addInboundPermitted(new PermittedOptions().setAddress("/toBus"))
            .setPointToPoint(true)
    );
    List<Message> messages = new CopyOnWriteArrayList<>();
    consumers.add(vertx.eventBus().consumer("/toBus", messages::add));
    consumers.add(vertx.eventBus().consumer("/toBus", messages::add));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/toBus", Headers.create("foo", "bar"), Buffer.buffer("Hello from STOMP"));
    }));

    Thread.sleep(500);
    assertThat(messages).hasSize(1);
  }

  @Test
  public void testThatEventBusMessagesAreTransferredToSeveralStompClients() {
    List<Frame> frames = new CopyOnWriteArrayList<>();

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/bus", frames::add).onComplete(f -> {
            client((ar2 -> {
              final StompClientConnection connection2 = ar2.result();
              connection2.subscribe("/bus", frames::add).onComplete(receipt -> {
                vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              });
            }));
          });
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 2);
  }

  @Test
  public void testThatEventBusMessagesAreOnlyTransferredToOneStompClientsInP2P() throws InterruptedException {
    List<Frame> frames = new CopyOnWriteArrayList<>();
    server.stompHandler().bridge(new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("/toStomp"))
            .setPointToPoint(true)
    );

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/toStomp", frames::add).onComplete(f -> {
            client((ar2 -> {
              final StompClientConnection connection2 = ar2.result();
              connection2.subscribe("/toStomp", frames::add).onComplete(receipt -> {
                vertx.eventBus().publish("/toStomp", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              });
            }));
          });
    }));

    Thread.sleep(500);
    assertThat(frames).hasSize(1);
  }

  @Test
  public void testThatEventBusConsumerCanReplyToStompMessages() {
    server.stompHandler().bridge(new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("/replyTo"))
            .addInboundPermitted(new PermittedOptions().setAddress("/request"))
            .setPointToPoint(true)
    );

    AtomicReference<Frame> response = new AtomicReference<>();

    consumers.add(vertx.eventBus().consumer("/request", msg -> {
      msg.reply("pong");
    }));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.subscribe("/replyTo", response::set).onComplete(r1 -> {
        connection.send("/request", Headers.create("reply-address", "/replyTo"), Buffer.buffer("ping"));
      });
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> response.get() != null);
  }

  @Test
  public void testThatStompClientCanUnsubscribe() throws InterruptedException {
    List<Frame> frames = new ArrayList<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", frame -> {
                frames.add(frame);
                connection.unsubscribe("/bus");
              }).onComplete(f -> {
                vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 1);

    // Send another message
    vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));

    Thread.sleep(500);
    assertThat(frames).hasSize(1);
  }

  @Test
  public void testThatStompClientCanCloseTheConnection() throws InterruptedException {
    List<Frame> frames = new ArrayList<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", frame -> {
                frames.add(frame);
                connection.close();
              }).onComplete(f -> {
                vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 1);

    // Send another message
    vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));

    Thread.sleep(500);
    assertThat(frames).hasSize(1);
  }

  @Test
  public void testThatStompClientCanDisconnect() throws InterruptedException {
    List<Frame> frames = new ArrayList<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", frame -> {
                frames.add(frame);
                connection.disconnect();
              }).onComplete(f -> {
                vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> frames.size() == 1);

    // Send another message
    vertx.eventBus().publish("/bus", "Hello from Vert.x", new DeliveryOptions().addHeader("foo", "bar"));

    Thread.sleep(500);
    assertThat(frames).hasSize(1);
  }

  @Test
  public void testThatStompFrameMatchingTheStructureAreTransferred() {
    tearDown();
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/bus").setMatch(new JsonObject().put("id", 1)))
                .addOutboundPermitted(new PermittedOptions().setAddress("/bus")))
        );
    server.listen().onComplete(lock.handler());

    lock.waitForSuccess();

    AtomicReference<Message> reference = new AtomicReference<>();
    consumers.add(vertx.eventBus().consumer("/bus", reference::set));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/bus", Headers.create("foo", "bar"), Buffer.buffer(new JsonObject().put("id", 1).put("msg",
          "Hello from STOMP").toString()));
    }));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);
    assertThat(reference.get().headers().get("foo")).isEqualTo("bar");
    assertThat(reference.get().headers().get("destination")).isEqualTo("/bus");
    assertThat(reference.get().address()).isEqualTo("/bus");
    assertThat(reference.get().replyAddress()).isNullOrEmpty();
    JsonObject json = new JsonObject(reference.get().body().toString());
    assertThat(json.getString("msg")).isEqualTo("Hello from STOMP");
  }

  @Test
  public void testThatStompFrameNotMatchingTheStructureAreRejected() throws InterruptedException {
    tearDown();
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/bus").setMatch(new JsonObject().put("id", 2)))
                .addOutboundPermitted(new PermittedOptions().setAddress("/bus")))
        );
    server.listen().onComplete(lock.handler());

    lock.waitForSuccess();

    AtomicReference<Message> reference = new AtomicReference<>();
    consumers.add(vertx.eventBus().consumer("/bus", reference::set));

    client((ar -> {
      final StompClientConnection connection = ar.result();
      connection.send("/bus", Headers.create("foo", "bar"), Buffer.buffer(new JsonObject().put("msg",
          "Hello from STOMP").toString()));
    }));

    Thread.sleep(2000);
    assertThat(reference.get()).isNull();
  }

  @Test
  public void testThatEventBusMessagesMatchingTheStructureAreTransferredToStomp() {
    tearDown();
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/bus"))
                .addOutboundPermitted(new PermittedOptions().setAddress("/bus").setMatch(new JsonObject().put("id", 2)
        ))));
    server.listen().onComplete(lock.handler());

    lock.waitForSuccess();


    AtomicReference<Frame> reference = new AtomicReference<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", reference::set).onComplete(f -> {
                JsonObject payload = new JsonObject().put("id", 2).put("message", "Hello from Vert.x");
                vertx.eventBus().publish("/bus", payload, new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));

    Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> reference.get() != null);

    assertThat(reference.get().getHeaders().get("foo")).isEqualTo("bar");
    assertThat(reference.get().getHeaders().get("destination")).isEqualTo("/bus");
    JsonObject json = new JsonObject(reference.get().getBodyAsString());
    assertThat(json.getString("message")).isEqualTo("Hello from Vert.x");
  }

  @Test
  public void testThatEventBusMessagesNotMatchingTheStructureAreRejected() throws InterruptedException {
    tearDown();
    AsyncLock<StompServer> lock = new AsyncLock<>();

    vertx = Vertx.vertx();
    server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/bus"))
                .addOutboundPermitted(new PermittedOptions().setAddress("/bus").setMatch(new JsonObject().put("id", 2)
                ))));
    server.listen().onComplete(lock.handler());

    lock.waitForSuccess();


    AtomicReference<Frame> reference = new AtomicReference<>();

    client((ar -> {
          final StompClientConnection connection = ar.result();
          connection.subscribe("/bus", reference::set).onComplete(f -> {
                JsonObject payload = new JsonObject().put("id", 1).put("message", "Hello from Vert.x");
                vertx.eventBus().publish("/bus", payload, new DeliveryOptions().addHeader("foo", "bar"));
              }
          );
        }
    ));
    Thread.sleep(2000);
    assertThat(reference.get()).isNull();
  }
}
