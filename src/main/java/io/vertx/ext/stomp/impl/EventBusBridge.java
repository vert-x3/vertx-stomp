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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusBridge extends Topic {

  private final BridgeOptions options;

  private final Map<String, Pattern> expressions = new HashMap<>();

  private final Map<String, MessageConsumer<?>> registry = new HashMap<>();


  public EventBusBridge(Vertx vertx, BridgeOptions options) {
    super(vertx, null);
    this.options = options;
  }

  /**
   * @return the destination address.
   */
  @Override
  public String destination() {
    return "<<bridge>>";
  }

  /**
   * Handles a subscription request to the current {@link Destination}. All check about the frame format and unicity
   * of the id should have been done beforehand.
   *
   * @param connection the connection
   * @param frame      the {@code SUBSCRIBE} frame
   * @return the current instance of {@link Destination}
   */
  @Override
  public synchronized Destination subscribe(StompServerConnection connection, Frame frame) {
    String address = frame.getDestination();
    // Need to check whether the client can receive message from the event bus (outbound).
    if (checkMatches(false, address, null)) {
      // We need the subscription object to transform messages.
      Subscription subscription = new Subscription(connection, frame);
      subscriptions.add(subscription);
      if (!registry.containsKey(address)) {
        registry.put(address, vertx.eventBus().consumer(address, msg -> {
          if (!checkMatches(false, address, msg.body())) {
            return;
          }
          if (options.isPointToPoint()) {
            Optional<Subscription> chosen = subscriptions.stream().filter(s -> s.destination.equals(address)).findAny();
            if (chosen.isPresent()) {
              Frame stompFrame = transform(msg, chosen.get());
              chosen.get().connection.write(stompFrame);
            }
          } else {
            subscriptions.stream().filter(s -> s.destination.equals(address)).forEach(s -> {
              Frame stompFrame = transform(msg, s);
              s.connection.write(stompFrame);
            });
          }
        }));
      }
      return this;
    }
    return null;
  }

  /**
   * Handles a un-subscription request to the current {@link Destination}.
   *
   * @param connection the connection
   * @param frame      the {@code UNSUBSCRIBE} frame
   * @return {@code true} if the un-subscription has been handled, {@code false} otherwise.
   */
  @Override
  public synchronized boolean unsubscribe(StompServerConnection connection, Frame frame) {
    for (Subscription subscription : new ArrayList<>(subscriptions)) {
      if (subscription.connection.equals(connection)
          && subscription.id.equals(frame.getId())) {

        boolean r = subscriptions.remove(subscription);
        Optional<Subscription> any = subscriptions.stream().filter(s -> s.destination.equals(subscription.destination)).findAny();
        // We unregister the event bus consumer if there are no subscription on this address anymore.
        if (!any.isPresent()) {
          MessageConsumer<?> consumer = registry.remove(subscription.destination);
          if (consumer != null) {
            consumer.unregister();
          }
        }
        return r;
      }
    }
    return false;
  }

  /**
   * Removes all subscriptions of the given connection
   *
   * @param connection the connection
   * @return the current instance of {@link Destination}
   */
  @Override
  public synchronized Destination unsubscribeConnection(StompServerConnection connection) {
    new ArrayList<>(subscriptions)
        .stream()
        .filter(subscription -> subscription.connection.equals(connection))
        .forEach(s -> {
          subscriptions.remove(s);
          Optional<Subscription> any = subscriptions.stream().filter(s2 -> s2.destination.equals(s.destination))
              .findAny();
          // We unregister the event bus consumer if there are no subscription on this address anymore.
          if (!any.isPresent()) {
            MessageConsumer<?> consumer = registry.remove(s.destination);
            if (consumer != null) {
              consumer.unregister();
            }
          }
        });
    return this;
  }

  private Frame transform(Message<Object> msg, Subscription subscription) {
    String messageId = UUID.randomUUID().toString();

    Frame frame = new Frame();
    frame.setCommand(Command.MESSAGE);

    final Headers headers = Headers.create(frame.getHeaders())
        // Destination already set in the input headers.
        .add(Frame.SUBSCRIPTION, subscription.id)
        .add(Frame.MESSAGE_ID, messageId)
        .add(Frame.DESTINATION, msg.address());
    if (!"auto".equals(subscription.ackMode)) {
      // We reuse the message Id as ack Id
      headers.add(Frame.ACK, messageId);
    }

    // Specific headers.
    if (msg.replyAddress() != null) {
      headers.put("reply-address", msg.replyAddress());
    }

    // Copy headers.
    for (Map.Entry<String, String> entry : msg.headers()) {
      headers.putIfAbsent(entry.getKey(), entry.getValue());
    }

    frame.setHeaders(headers);
    Object body = msg.body();
    if (body != null) {
      if (body instanceof String) {
        frame.setBody(Buffer.buffer((String) body));
      } else if (body instanceof Buffer) {
        frame.setBody((Buffer) body);
      } else if (body instanceof JsonObject) {
        frame.setBody(Buffer.buffer(((JsonObject) body).encode()));
      } else {
        throw new IllegalStateException("Illegal body - unsupported body type: " + body.getClass().getName());
      }
    }

    if (body != null && frame.getHeader(Frame.CONTENT_LENGTH) == null) {
      frame.addHeader(Frame.CONTENT_LENGTH, Integer.toString(frame.getBody().length()));
    }

    return frame;
  }

  /**
   * Dispatches the given frame.
   *
   * @param connection the connection
   * @param frame      the frame
   * @return the current instance of {@link Destination}
   */
  @Override
  public Destination dispatch(StompServerConnection connection, Frame frame) {
    String address = frame.getDestination();
    // Send a frame to the event bus, check if this inbound traffic is allowed.
    if (checkMatches(true, address, frame.getBody())) {
      final String replyAddress = frame.getHeader("reply-address");
      if (replyAddress != null) {
        send(address, frame, (AsyncResult<Message<Object>> res) -> {
          if (res.failed()) {
            Throwable cause = res.cause();
            connection.write(Frames.createErrorFrame("Message dispatch error", Headers.create(Frame.DESTINATION,
                address, "reply-address", replyAddress), cause.getMessage())).close();
          } else {
            // We are in a request-response interaction, only one STOMP client must receive the message (the one
            // having sent the given frame).
            // We look for the subscription with registered to the 'reply-to' destination. It must be unique.
            Optional<Subscription> subscription = subscriptions.stream()
                .filter(s -> s.connection.equals(connection) && s.destination.equals(replyAddress))
                .findFirst();
            if (subscription.isPresent()) {
              Frame stompFrame = transform(res.result(), subscription.get());
              subscription.get().connection.write(stompFrame);
            }
          }
        });
      } else {
        send(address, frame, null);
      }
    } else {
      connection.write(Frames.createErrorFrame("Access denied", Headers.create(Frame.DESTINATION,
          address), "Access denied to " + address)).close();
      return null;
    }
    return this;
  }

  private void send(String address, Frame frame, Handler<AsyncResult<Message<Object>>> replyHandler) {
    if (options.isPointToPoint()) {
      vertx.eventBus().request(address, frame.getBody(),
          new DeliveryOptions().setHeaders(toMultimap(frame.getHeaders()))).onComplete(replyHandler);
    } else {
      // the reply handler is ignored in non point to point interaction.
      vertx.eventBus().publish(address, frame.getBody(),
          new DeliveryOptions().setHeaders(toMultimap(frame.getHeaders())));
    }
  }

  private MultiMap toMultimap(Map<String, String> headers) {
    return MultiMap.caseInsensitiveMultiMap().addAll(headers);
  }

  /**
   * Checks whether or not the given address matches with the current destination.
   *
   * @param address the address
   * @param payload the payload - may be {@link null}
   * @return {@code true} if it matches, {@code false} otherwise.
   */
  public boolean matches(String address, Buffer payload) {
    return checkMatches(false, address, payload) || checkMatches(true, address, payload);
  }

  /**
   * Checks whether or not the given address matches with the current destination.
   *
   * @param address the address
   * @return {@code true} if it matches, {@code false} otherwise.
   */
  public boolean matches(String address) {
    return checkMatches(false, address, null) || checkMatches(true, address, null);
  }

  private boolean regexMatches(String matchRegex, String address) {
    Pattern pattern = expressions.get(matchRegex);
    if (pattern == null) {
      pattern = Pattern.compile(matchRegex);
      expressions.put(matchRegex, pattern);
    }
    Matcher m = pattern.matcher(address);
    return m.matches();
  }

  private boolean checkMatches(boolean inbound, String address, Object body) {

    List<PermittedOptions> matches = inbound ? options.getInboundPermitteds() : options.getOutboundPermitteds();

    for (PermittedOptions matchHolder : matches) {
      String matchAddress = matchHolder.getAddress();
      String matchRegex;
      if (matchAddress == null) {
        matchRegex = matchHolder.getAddressRegex();
      } else {
        matchRegex = null;
      }

      boolean addressOK;
      if (matchAddress == null) {
        addressOK = matchRegex == null || regexMatches(matchRegex, address);
      } else {
        addressOK = matchAddress.equals(address);
      }

      if (addressOK) {
        return structureMatches(matchHolder.getMatch(), body);
      }
    }

    return false;
  }

  /**
   * Checks whether or not the given message payload matches the permitted option required structure (match).
   * It only supports JSON payload. If the payload is not JSOn and the `match` is not {@link null}, the structure
   * does not matches (it returns {@link false}).
   *
   * @param match the required structure, may be {@code null}
   * @param body  the body, may be {@code null}. It is either a {@link JsonObject} or a {@link Buffer}.
   * @return whether or not the payload matches the structure. {@link true} is returned if `match` is {@code null},
   * or `body` is {@code null}.
   */
  private boolean structureMatches(JsonObject match, Object body) {
    if (match == null || body == null) {
      return true;
    }

    // Can send message other than JSON too - in which case we can't do deep matching on structure of message

    try {
      JsonObject object;
      if (body instanceof JsonObject) {
        object = (JsonObject) body;
      } else if (body instanceof Buffer) {
        object = new JsonObject(((Buffer) body).toString("UTF-8"));
      } else if (body instanceof String) {
        object = new JsonObject((String) body);
      } else {
        return false;
      }

      for (String fieldName : match.fieldNames()) {
        Object mv = match.getValue(fieldName);
        Object bv = object.getValue(fieldName);
        // Support deep matching
        if (mv instanceof JsonObject) {
          if (!structureMatches((JsonObject) mv, bv)) {
            return false;
          }
        } else if (!match.getValue(fieldName).equals(object.getValue(fieldName))) {
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      // Was not a valid json object refuse the message
      return false;
    }
  }
}
