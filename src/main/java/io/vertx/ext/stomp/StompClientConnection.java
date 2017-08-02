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
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

import java.util.Map;

/**
 * Once a connection to the STOMP server has been made, client receives a {@link StompClientConnection}, that let
 * send and receive STOMP frames.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface StompClientConnection {

  /**
   * @return the session id.
   */
  String session();

  /**
   * @return the STOMP protocol version negotiated with the server.
   */
  String version();

  /**
   * Closes the connection without sending the {@code DISCONNECT} frame.
   *
   * @see #disconnect()
   * @see #disconnect(Handler)
   */
  void close();

  /**
   * @return the server name.
   */
  String server();

  /**
   * Sends a {@code SEND} frame to the server.
   *
   * @param headers the headers, must not be {@code null}
   * @param body    the body, may be {@code null}
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(Map<String, String> headers, Buffer body);

  /**
   * Sends a {@code SEND} frame to the server.
   *
   * @param headers        the headers, must not be {@code null}
   * @param body           the body, may be {@code null}
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       sent frame has been received. The handler receives the sent frame.
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(Map<String, String> headers, Buffer body, Handler<Frame> receiptHandler);

  /**
   * Sends a {@code SEND} frame to the server to the given destination. The message does not have any other header.
   *
   * @param destination the destination, must not be {@code null}
   * @param body        the body, may be {@code null}
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(String destination, Buffer body);

  /**
   * Sends a {@code SEND} frame to the server to the given destination. The message does not have any other header.
   *
   * @param destination    the destination, must not be {@code null}
   * @param body           the body, may be {@code null}
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       sent frame has been received. The handler receives the sent frame.
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(String destination, Buffer body, Handler<Frame> receiptHandler);

  /**
   * Sends the given frame to the server.
   *
   * @param frame the frame
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(Frame frame);

  /**
   * Sends the given frame to the server.
   *
   * @param frame          the frame
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       sent frame has been received. The handler receives the sent frame.
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(Frame frame, Handler<Frame> receiptHandler);

  /**
   * Sends a {@code SEND} frame to the server to the given destination.
   *
   * @param destination the destination, must not be {@code null}
   * @param body        the body, may be {@code null}
   * @param headers     the header. The {@code destination} header is replaced by the value given to the {@code
   *                    destination} parameter
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(String destination, Map<String, String> headers, Buffer body);

  /**
   * Sends a {@code SEND} frame to the server to the given destination.
   *
   * @param destination    the destination, must not be {@code null}
   * @param body           the body, may be {@code null}
   * @param headers        the header. The {@code destination} header is replaced by the value given to the {@code
   *                       destination} parameter
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       sent frame has been received. The handler receives the sent frame.
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection send(String destination, Map<String, String> headers, Buffer body, Handler<Frame> receiptHandler);

  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   *
   * @param destination the destination, must not be {@code null}
   * @param handler     the handler invoked when a message is received on the given destination. Must not be {@code null}.
   * @return the subscription id.
   */
  String subscribe(String destination, Handler<Frame> handler);

  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   *
   * @param destination    the destination, must not be {@code null}
   * @param handler        the handler invoked when a message is received on the given destination. Must not be {@code null}.
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       subscription has been received. The handler receives the sent frame ({@code SUBSCRIBE}).
   * @return the subscription id.
   */
  String subscribe(String destination, Handler<Frame> handler, Handler<Frame> receiptHandler);

  /**
   * Subscribes to the given destination.
   *
   * @param destination the destination, must not be {@code null}.
   * @param headers     the headers to configure the subscription. It may contain the {@code ack}
   *                    header to configure the acknowledgment policy. If the given set of headers contains the
   *                    {@code id} header, this value is used as subscription id.
   * @param handler     the handler invoked when a message is received on the given destination. Must not be {@code null}.
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  String subscribe(String destination, Map<String, String> headers, Handler<Frame> handler);

  /**
   * Subscribes to the given destination.
   *
   * @param destination    the destination, must not be {@code null}
   * @param headers        the headers to configure the subscription. It may contain the {@code ack}
   *                       header to configure the acknowledgment policy. If the given set of headers contains the
   *                       {@code id} header, this value is used as subscription id.
   * @param handler        the handler invoked when a message is received on the given destination. Must not be {@code null}.
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       subscription has been received. The handler receives the sent frame ({@code SUBSCRIBE}).
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  String subscribe(String destination, Map<String, String> headers, Handler<Frame> handler, Handler<Frame> receiptHandler);

  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the {@code id} header).
   *
   * @param destination the destination
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection unsubscribe(String destination);

  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the {@code id} header).
   *
   * @param destination    the destination
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       un-subscription has been received. The handler receives the sent frame ({@code UNSUBSCRIBE}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection unsubscribe(String destination, Handler<Frame> receiptHandler);

  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the {@code id} header, the header value is used. Otherwise the destination is used.
   *
   * @param destination the destination
   * @param headers     the headers
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection unsubscribe(String destination, Map<String, String> headers);

  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the {@code id} header, the header value is used. Otherwise the destination is used.
   *
   * @param destination    the destination
   * @param headers        the headers
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       un-subscription has been received. The handler receives the sent frame ({@code UNSUBSCRIBE}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection unsubscribe(String destination, Map<String, String> headers, Handler<Frame> receiptHandler);

  /**
   * Sets a handler notified when an {@code ERROR} frame is received by the client. The handler receives the {@code
   * ERROR} frame and a reference on the {@link StompClientConnection}.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection errorHandler(Handler<Frame> handler);

  /**
   * Sets a handler notified when the STOMP connection is closed.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection closeHandler(Handler<StompClientConnection> handler);


  /**
   * Sets a handler notified when the server does not respond to a {@code ping} request in time. In other
   * words, this handler is invoked when the heartbeat has detected a connection failure with the server.
   * The handler can decide to reconnect to the server.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection} receiving the dropped connection.
   */
  @Fluent
  StompClientConnection connectionDroppedHandler(Handler<StompClientConnection> handler);

  /**
   * Sets a handler that let customize the behavior when a ping needs to be sent to the server. Be aware that
   * changing the default behavior may break the compliance with the STOMP specification.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection pingHandler(Handler<StompClientConnection> handler);

  /**
   * Begins a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction begin has been processed by the server. The handler receives the sent frame
   *                       ({@code BEGIN}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection beginTX(String id, Handler<Frame> receiptHandler);

  /**
   * Begins a transaction.
   *
   * @param id the transaction id, must not be {@code null}
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection beginTX(String id);

  /**
   * Begins a transaction.
   *
   * @param id      the transaction id, must not be {@code null}
   * @param headers additional headers to send to the server. The {@code transaction} header is replaced by the value
   *                passed in the @{code id} parameter
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection beginTX(String id, Map<String, String> headers);

  /**
   * Begins a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param headers        additional headers to send to the server. The {@code transaction} header is replaced by the
   *                       value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction begin has been processed by the server. The handler receives the sent frame
   *                       ({@code BEGIN}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection beginTX(String id, Map<String, String> headers, Handler<Frame> receiptHandler);

  /**
   * Commits a transaction.
   *
   * @param id the transaction id, must not be {@code null}
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection commit(String id);

  /**
   * Commits a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction commit has been processed by the server. The handler receives the sent frame
   *                       ({@code COMMIT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection commit(String id, Handler<Frame> receiptHandler);

  /**
   * Commits a transaction.
   *
   * @param id      the transaction id, must not be {@code null}
   * @param headers additional headers to send to the server. The {@code transaction} header is replaced by the
   *                value passed in the @{code id} parameter
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection commit(String id, Map<String, String> headers);

  /**
   * Commits a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param headers        additional headers to send to the server. The {@code transaction} header is replaced by the
   *                       value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction commit has been processed by the server. The handler receives the sent frame
   *                       ({@code COMMIT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection commit(String id, Map<String, String> headers, Handler<Frame> receiptHandler);

  /**
   * Aborts a transaction.
   *
   * @param id the transaction id, must not be {@code null}
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection abort(String id);

  /**
   * Aborts a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction cancellation has been processed by the server. The handler receives the sent
   *                       frame ({@code ABORT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection abort(String id, Handler<Frame> receiptHandler);

  /**
   * Aborts a transaction.
   *
   * @param id      the transaction id, must not be {@code null}
   * @param headers additional headers to send to the server. The {@code transaction} header is replaced by the
   *                value passed in the @{code id} parameter
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection abort(String id, Map<String, String> headers);

  /**
   * Aborts a transaction.
   *
   * @param id             the transaction id, must not be {@code null}
   * @param headers        additional headers to send to the server. The {@code transaction} header is replaced by the
   *                       value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       transaction cancellation has been processed by the server. The handler receives the sent
   *                       frame ({@code ABORT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection abort(String id, Map<String, String> headers, Handler<Frame> receiptHandler);

  /**
   * Disconnects the client. Unlike the {@link #close()} method, this method send the {@code DISCONNECT} frame to the
   * server.
   *
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection disconnect();

  /**
   * Disconnects the client. Unlike the {@link #close()} method, this method send the {@code DISCONNECT} frame to the
   * server.
   *
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       disconnection has been processed by the server. The handler receives the sent
   *                       frame ({@code DISCONNECT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection disconnect(Handler<Frame> receiptHandler);

  /**
   * Disconnects the client. Unlike the {@link #close()} method, this method send the {@code DISCONNECT} frame to the
   * server. This method lets you customize the {@code DISCONNECT} frame.
   *
   * @param frame the {@code DISCONNECT} frame.
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection disconnect(Frame frame);

  /**
   * Disconnects the client. Unlike the {@link #close()} method, this method send the {@code DISCONNECT} frame to the
   * server. This method lets you customize the {@code DISCONNECT} frame.
   *
   * @param frame          the {@code DISCONNECT} frame.
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       disconnection has been processed by the server. The handler receives the sent
   *                       frame ({@code DISCONNECT}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection disconnect(Frame frame, Handler<Frame> receiptHandler);

  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The {@code id} parameter is the message id received in the frame.
   *
   * @param id the message id of the message to acknowledge
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection ack(String id);

  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The {@code id} parameter is the message id received in the frame.
   *
   * @param id             the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       acknowledgment has been processed by the server. The handler receives the sent
   *                       frame ({@code ACK}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection ack(String id, Handler<Frame> receiptHandler);

  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The {@code id} parameter is the message id received in the frame.
   *
   * @param id the message id of the message to acknowledge
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection nack(String id);

  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The {@code id} parameter is the message id received in the frame.
   *
   * @param id             the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       non-acknowledgment has been processed by the server. The handler receives the sent
   *                       frame ({@code NACK}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection nack(String id, Handler<Frame> receiptHandler);

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   *
   * @param id   the message id of the message to acknowledge
   * @param txId the transaction id
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection ack(String id, String txId);

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   *
   * @param id             the message id of the message to acknowledge
   * @param txId           the transaction id
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       acknowledgment has been processed by the server. The handler receives the sent
   *                       frame ({@code ACK}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection ack(String id, String txId, Handler<Frame> receiptHandler);

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   *
   * @param id   the message id of the message to acknowledge
   * @param txId the transaction id
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection nack(String id, String txId);

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   *
   * @param id             the message id of the message to acknowledge
   * @param txId           the transaction id
   * @param receiptHandler the handler invoked when the {@code RECEIPT} frame associated with the
   *                       non-acknowledgment has been processed by the server. The handler receives the sent
   *                       frame ({@code NACK}).
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection nack(String id, String txId, Handler<Frame> receiptHandler);


  /**
   * Configures a received handler that get notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified by the handler.
   * <p>
   * Unlike {@link StompClient#receivedFrameHandler(Handler)}, the given handler won't receive the {@code
   * CONNECTED} frame. If a received frame handler is set on the {@link StompClient}, it will be used by all
   * clients connection, so calling this method is useless, except if you want to use a different handler.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection receivedFrameHandler(Handler<Frame> handler);

  /**
   * Configures a handler notified when a frame is going to be written on the wire. This handler can be used from
   * logging, debugging. The handler can modify the received frame.
   * <p>
   * If a writing frame handler is set on the {@link StompClient}, it will be used by all
   * clients connection, so calling this method is useless, except if you want to use a different handler.
   *
   * @param handler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection writingFrameHandler(Handler<Frame> handler);

  /**
   * Configures the exception handler notified upon TCP-level errors.
   *
   * @param exceptionHandler the handler
   * @return the current {@link StompClientConnection}
   */
  @Fluent
  StompClientConnection exceptionHandler(Handler<Throwable> exceptionHandler);

  /**
   * Returns whether or not the `CONNECTED` frame has been receive meaning that the Stomp connection is established.
   *
   * @return {@code true} if the connection is established, {@code false} otherwise
   */
  boolean isConnected();
}
