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

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.stomp.impl.DefaultStompHandler;

import java.util.List;

/**
 * STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 * let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 * designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 * specification. In this default implementation, not acknowledge frames are dropped.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompServerHandler extends Handler<ServerFrame> {

  /**
   * Creates an instance of {@link StompServerHandler} using the default (compliant) implementation.
   *
   * @param vertx the vert.x instance to use
   * @return the created {@link StompServerHandler}
   */
  static StompServerHandler create(Vertx vertx) {
    return new DefaultStompHandler(vertx);
  }

  /**
   * Configures the action to execute when a {@code CONNECT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler connectHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code STOMP} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler stompHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code SUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler subscribeHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code UNSUBSCRIBE} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler unsubscribeHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code SEND} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler sendHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a connection with the client is closed.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler closeHandler(Handler<StompServerConnection> handler);

  /**
   * Called when the connection is closed. This method executes a default behavior and must calls the configured
   * {@link #closeHandler(Handler)} if any.
   *
   * @param connection the connection
   */
  void onClose(StompServerConnection connection);

  /**
   * Configures the action to execute when a {@code COMMIT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler commitHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code ABORT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler abortHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code BEGIN} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler beginHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code DISCONNECT} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler disconnectHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code ACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler ackHandler(Handler<ServerFrame> handler);

  /**
   * Configures the action to execute when a {@code NACK} frame is received.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler nackHandler(Handler<ServerFrame> handler);

  /**
   * Called when the client connects to a server requiring authentication. It invokes the {@link AuthProvider} configured
   * using {@link #authProvider(AuthProvider)}.
   *
   * @param server   the STOMP server.
   * @param login    the login
   * @param passcode the password
   * @param handler  handler receiving the authentication result
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onAuthenticationRequest(StompServer server, String login, String passcode,
                                             Handler<AsyncResult<Boolean>> handler);

  /**
   * Configures the {@link AuthProvider} to be used to authenticate the user.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler authProvider(AuthProvider handler);

  /**
   * @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   * opaque Strings.
   */
  List<Destination> getDestinations();

  /**
   * Gets the destination with the given name.
   *
   * @param destination the destination
   * @return the {@link Destination}, {@code null} if not existing.
   */
  Destination getDestination(String destination);

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   * Implementations must call the handler configured using {@link #onAckHandler(Handler)}.
   *
   * @param connection the connection
   * @param subscribe  the {@code SUBSCRIBE} frame
   * @param messages   the acknowledge messages
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onAck(StompServerConnection connection, Frame subscribe, List<Frame> messages);

  /**
   * Method called by single message (client-individual policy) or a set of message (client policy) are
   * <strong>not</strong> acknowledged. Not acknowledgment can result from a {@code NACK} frame or from a timeout (no
   * {@code ACK} frame received in a given time. Implementations must call the handler configured using
   * {@link #onNackHandler(Handler)}.
   *
   * @param connection the connection
   * @param subscribe  the {@code SUBSCRIBE} frame
   * @param messages   the acknowledge messages
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler onNack(StompServerConnection connection, Frame subscribe, List<Frame> messages);

  /**
   * Configures the action to execute when messages are acknowledged.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   * @see #onAck(StompServerConnection, Frame, List)
   */
  @Fluent
  StompServerHandler onAckHandler(Handler<Acknowledgement> handler);

  /**
   * Configures the action to execute when messages are <strong>not</strong> acknowledged.
   *
   * @param handler the handler
   * @return the current {@link StompServerHandler}
   * @see #onNack(StompServerConnection, Frame, List)
   */
  @Fluent
  StompServerHandler onNackHandler(Handler<Acknowledgement> handler);


  /**
   * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   * frame containing {@code EOL} (specification). However, you can customize this and send another frame. However,
   * be aware that this may requires a custom client.
   * <p/>
   * The handler will only be called if the connection supports heartbeats.
   *
   * @param handler the action to execute when a `PING` needs to be sent.
   * @return the current {@link StompServerHandler}
   */
  @Fluent
  StompServerHandler pingHandler(Handler<StompServerConnection> handler);

  /**
   * Gets a {@link Destination} object if existing, or create a new one. The creation is delegated to the
   * {@link DestinationFactory}.
   *
   * @param destination the destination
   * @return the {@link Destination} instance, may have been created.
   */
  Destination getOrCreateDestination(String destination);

  /**
   * Configures the {@link DestinationFactory} used to create {@link Destination} objects.
   *
   * @param factory the factory
   * @return the current {@link StompServerHandler}.
   */
  @Fluent
  StompServerHandler destinationFactory(DestinationFactory factory);

}
