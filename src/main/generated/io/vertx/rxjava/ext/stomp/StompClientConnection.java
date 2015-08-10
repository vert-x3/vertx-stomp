/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.stomp;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import java.util.Map;
import io.vertx.core.Handler;

/**
 * Once a connection to the STOMP server has been made, client receives a {@link io.vertx.rxjava.ext.stomp.StompClientConnection}, that let
 * send and receive STOMP frames.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompClientConnection original} non RX-ified interface using Vert.x codegen.
 */

public class StompClientConnection {

  final io.vertx.ext.stomp.StompClientConnection delegate;

  public StompClientConnection(io.vertx.ext.stomp.StompClientConnection delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the session id.
   * @return 
   */
  public String session() { 
    String ret = this.delegate.session();
    return ret;
  }

  /**
   * @return the STOMP protocol version negotiated with the server.
   * @return 
   */
  public String version() { 
    String ret = this.delegate.version();
    return ret;
  }

  /**
   * Closes the connection without sending the <code>DISCONNECT</code> frame.
   */
  public void close() { 
    this.delegate.close();
  }

  /**
   * @return the server name.
   * @return 
   */
  public String server() { 
    String ret = this.delegate.server();
    return ret;
  }

  /**
   * Sends a <code>SEND</code> frame to the server.
   * @param headers the headers, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String,String> headers, Buffer body) { 
    this.delegate.send(headers, (io.vertx.core.buffer.Buffer) body.getDelegate());
    return this;
  }

  /**
   * Sends a <code>SEND</code> frame to the server.
   * @param headers the headers, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Map<String,String> headers, Buffer body, Handler<Frame> receiptHandler) { 
    this.delegate.send(headers, (io.vertx.core.buffer.Buffer) body.getDelegate(), receiptHandler);
    return this;
  }

  /**
   * Sends a <code>SEND</code> frame to the server to the given destination. The message does not have any other header.
   * @param destination the destination, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Buffer body) { 
    this.delegate.send(destination, (io.vertx.core.buffer.Buffer) body.getDelegate());
    return this;
  }

  /**
   * Sends a <code>SEND</code> frame to the server to the given destination. The message does not have any other header.
   * @param destination the destination, must not be <code>null</code>
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Buffer body, Handler<Frame> receiptHandler) { 
    this.delegate.send(destination, (io.vertx.core.buffer.Buffer) body.getDelegate(), receiptHandler);
    return this;
  }

  /**
   * Sends the given frame to the server.
   * @param frame the frame
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Frame frame) { 
    this.delegate.send(frame);
    return this;
  }

  /**
   * Sends the given frame to the server.
   * @param frame the frame
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(Frame frame, Handler<Frame> receiptHandler) { 
    this.delegate.send(frame, receiptHandler);
    return this;
  }

  /**
   * Sends a <code>SEND</code> frame to the server to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
   * @param body the body, may be <code>null</code>
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Map<String,String> headers, Buffer body) { 
    this.delegate.send(destination, headers, (io.vertx.core.buffer.Buffer) body.getDelegate());
    return this;
  }

  /**
   * Sends a <code>SEND</code> frame to the server to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
   * @param body the body, may be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection send(String destination, Map<String,String> headers, Buffer body, Handler<Frame> receiptHandler) { 
    this.delegate.send(destination, headers, (io.vertx.core.buffer.Buffer) body.getDelegate(), receiptHandler);
    return this;
  }

  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   * @param destination the destination, must not be <code>null</code>
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @return the subscription id.
   */
  public String subscribe(String destination, FrameHandler handler) { 
    String ret = this.delegate.subscribe(destination, (io.vertx.ext.stomp.FrameHandler) handler.getDelegate());
    return ret;
  }

  /**
   * Subscribes to the given destination. This destination is used as subscription id.
   * @param destination the destination, must not be <code>null</code>
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
   * @return the subscription id.
   */
  public String subscribe(String destination, FrameHandler handler, Handler<Frame> receiptHandler) { 
    String ret = this.delegate.subscribe(destination, (io.vertx.ext.stomp.FrameHandler) handler.getDelegate(), receiptHandler);
    return ret;
  }

  /**
   * Subscribes to the given destination.
   * @param destination the destination, must not be <code>null</code>.
   * @param headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  public String subscribe(String destination, Map<String,String> headers, FrameHandler handler) { 
    String ret = this.delegate.subscribe(destination, headers, (io.vertx.ext.stomp.FrameHandler) handler.getDelegate());
    return ret;
  }

  /**
   * Subscribes to the given destination.
   * @param destination the destination, must not be <code>null</code>
   * @param headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
   * @param handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
   * @return the subscription id, which can either be the destination or the id set in the headers.
   */
  public String subscribe(String destination, Map<String,String> headers, FrameHandler handler, Handler<Frame> receiptHandler) { 
    String ret = this.delegate.subscribe(destination, headers, (io.vertx.ext.stomp.FrameHandler) handler.getDelegate(), receiptHandler);
    return ret;
  }

  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the <code>id</code> header).
   * @param destination the destination
   * @return the current {@link io.vertx.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination) { 
    this.delegate.unsubscribe(destination);
    return this;
  }

  /**
   * Un-subscribes from the given destination. This method only works if the subscription did not specifies a
   * subscription id (using the <code>id</code> header).
   * @param destination the destination
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the un-subscription has been received. The handler receives the sent frame (<code>UNSUBSCRIBE</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Handler<Frame> receiptHandler) { 
    this.delegate.unsubscribe(destination, receiptHandler);
    return this;
  }

  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the <code>id</code> header, the header value is used. Otherwise the destination is used.
   * @param destination the destination
   * @param headers the headers
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Map<String,String> headers) { 
    this.delegate.unsubscribe(destination, headers);
    return this;
  }

  /**
   * Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
   * headers contains the <code>id</code> header, the header value is used. Otherwise the destination is used.
   * @param destination the destination
   * @param headers the headers
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the un-subscription has been received. The handler receives the sent frame (<code>UNSUBSCRIBE</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection unsubscribe(String destination, Map<String,String> headers, Handler<Frame> receiptHandler) { 
    this.delegate.unsubscribe(destination, headers, receiptHandler);
    return this;
  }

  /**
   * Sets a handler notified when an <code>ERROR</code> frame is received by the client. The handler receives the <code>ERROR</code> frame and a reference on the {@link io.vertx.rxjava.ext.stomp.StompClientConnection}.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection errorHandler(FrameHandler handler) { 
    this.delegate.errorHandler((io.vertx.ext.stomp.FrameHandler) handler.getDelegate());
    return this;
  }

  /**
   * Sets a handler notified when the STOMP connection is closed.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection closeHandler(Handler<StompClientConnection> handler) { 
    this.delegate.closeHandler(new Handler<io.vertx.ext.stomp.StompClientConnection>() {
      public void handle(io.vertx.ext.stomp.StompClientConnection event) {
        handler.handle(new StompClientConnection(event));
      }
    });
    return this;
  }

  /**
   * Sets a handler that let customize the behavior when a ping needs to be sent to the server. Be aware that
   * changing the default behavior may break the compliance with the STOMP specification.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection pingHandler(Handler<StompClientConnection> handler) { 
    this.delegate.pingHandler(new Handler<io.vertx.ext.stomp.StompClientConnection>() {
      public void handle(io.vertx.ext.stomp.StompClientConnection event) {
        handler.handle(new StompClientConnection(event));
      }
    });
    return this;
  }

  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction begin has been processed by the server. The handler receives the sent frame (<code>BEGIN</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection begin(String id, Handler<Frame> receiptHandler) { 
    this.delegate.begin(id, receiptHandler);
    return this;
  }

  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection begin(String id) { 
    this.delegate.begin(id);
    return this;
  }

  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection begin(String id, Map<String,String> headers) { 
    this.delegate.begin(id, headers);
    return this;
  }

  /**
   * Begins a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction begin has been processed by the server. The handler receives the sent frame (<code>BEGIN</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection begin(String id, Map<String,String> headers, Handler<Frame> receiptHandler) { 
    this.delegate.begin(id, headers, receiptHandler);
    return this;
  }

  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id) { 
    this.delegate.commit(id);
    return this;
  }

  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction commit has been processed by the server. The handler receives the sent frame (<code>COMMIT</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Handler<Frame> receiptHandler) { 
    this.delegate.commit(id, receiptHandler);
    return this;
  }

  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Map<String,String> headers) { 
    this.delegate.commit(id, headers);
    return this;
  }

  /**
   * Commits a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction commit has been processed by the server. The handler receives the sent frame (<code>COMMIT</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection commit(String id, Map<String,String> headers, Handler<Frame> receiptHandler) { 
    this.delegate.commit(id, headers, receiptHandler);
    return this;
  }

  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id) { 
    this.delegate.abort(id);
    return this;
  }

  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction cancellation has been processed by the server. The handler receives the sent frame (<code>ABORT</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Handler<Frame> receiptHandler) { 
    this.delegate.abort(id, receiptHandler);
    return this;
  }

  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Map<String,String> headers) { 
    this.delegate.abort(id, headers);
    return this;
  }

  /**
   * Aborts a transaction.
   * @param id the transaction id, must not be <code>null</code>
   * @param headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the transaction cancellation has been processed by the server. The handler receives the sent frame (<code>ABORT</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection abort(String id, Map<String,String> headers, Handler<Frame> receiptHandler) { 
    this.delegate.abort(id, headers, receiptHandler);
    return this;
  }

  /**
   * Disconnects the client. Unlike the {@link io.vertx.rxjava.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect() { 
    this.delegate.disconnect();
    return this;
  }

  /**
   * Disconnects the client. Unlike the {@link io.vertx.rxjava.ext.stomp.StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
   * server.
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the disconnection has been processed by the server. The handler receives the sent frame (<code>DISCONNECT</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection disconnect(Handler<Frame> receiptHandler) { 
    this.delegate.disconnect(receiptHandler);
    return this;
  }

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the client.
   * @param frame the frame
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(Frame frame) { 
    this.delegate.ack(frame);
    return this;
  }

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the client.
   * @param frame the frame
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(Frame frame, Handler<Frame> receiptHandler) { 
    this.delegate.ack(frame, receiptHandler);
    return this;
  }

  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id) { 
    this.delegate.ack(id);
    return this;
  }

  /**
   * Sends an acknowledgement for a specific message. It means that the message has been handled and processed by the
   * client. The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(String id, Handler<Frame> receiptHandler) { 
    this.delegate.ack(id, receiptHandler);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * @param frame the frame
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(Frame frame) { 
    this.delegate.nack(frame);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * @param frame the frame
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(Frame frame, Handler<Frame> receiptHandler) { 
    this.delegate.nack(frame, receiptHandler);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id) { 
    this.delegate.nack(id);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given message. It means that the message has not been handled by the client.
   * The <code>id</code> parameter is the message id received in the frame.
   * @param id the message id of the message to acknowledge
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(String id, Handler<Frame> receiptHandler) { 
    this.delegate.nack(id, receiptHandler);
    return this;
  }

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   * @param frame the frame
   * @param txId the transaction id
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(Frame frame, String txId) { 
    this.delegate.ack(frame, txId);
    return this;
  }

  /**
   * Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
   * client. The sent acknowledgement is part of the transaction identified by the given id.
   * @param frame the frame
   * @param txId the transaction id
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection ack(Frame frame, String txId, Handler<Frame> receiptHandler) { 
    this.delegate.ack(frame, txId, receiptHandler);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   * @param frame the frame
   * @param txId the transaction id
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(Frame frame, String txId) { 
    this.delegate.nack(frame, txId);
    return this;
  }

  /**
   * Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
   * The sent non-acknowledgement is part of the transaction identified by the given id.
   * @param frame the frame
   * @param txId the transaction id
   * @param receiptHandler the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClientConnection nack(Frame frame, String txId, Handler<Frame> receiptHandler) { 
    this.delegate.nack(frame, txId, receiptHandler);
    return this;
  }


  public static StompClientConnection newInstance(io.vertx.ext.stomp.StompClientConnection arg) {
    return arg != null ? new StompClientConnection(arg) : null;
  }
}
