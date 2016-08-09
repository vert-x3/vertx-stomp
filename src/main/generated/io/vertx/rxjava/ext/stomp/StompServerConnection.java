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
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import io.vertx.core.Handler;

/**
 * Class representing a connection between a STOMP client a the server. It keeps a references on the client socket,
 * so let write to this socket.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompServerConnection original} non RX-ified interface using Vert.x codegen.
 */

public class StompServerConnection {

  final io.vertx.ext.stomp.StompServerConnection delegate;

  public StompServerConnection(io.vertx.ext.stomp.StompServerConnection delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Writes the given frame to the socket.
   * @param frame the frame, must not be <code>null</code>.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerConnection}
   */
  public StompServerConnection write(Frame frame) { 
    delegate.write(frame);
    return this;
  }

  /**
   * Writes the given buffer to the socket. This is a low level API that should be used carefully.
   * @param buffer the buffer
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServerConnection}
   */
  public StompServerConnection write(Buffer buffer) { 
    delegate.write((io.vertx.core.buffer.Buffer)buffer.getDelegate());
    return this;
  }

  /**
   * @return the STOMP server serving this connection.
   */
  public StompServer server() { 
    StompServer ret = StompServer.newInstance(delegate.server());
    return ret;
  }

  /**
   * @return the STOMP server handler dealing with this connection
   */
  public StompServerHandler handler() { 
    StompServerHandler ret = StompServerHandler.newInstance(delegate.handler());
    return ret;
  }

  /**
   * @return the STOMP session id computed when the client has established the connection to the server
   */
  public String session() { 
    String ret = delegate.session();
    return ret;
  }

  /**
   * Closes the connection with the client.
   */
  public void close() { 
    delegate.close();
  }

  /**
   * Sends a `PING` frame to the client. A `PING` frame is a frame containing only <code>EOL</code>.
   */
  public void ping() { 
    delegate.ping();
  }

  /**
   * Notifies the connection about server activity (the server has sent a frame). This method is used to handle the
   * heartbeat.
   */
  public void onServerActivity() { 
    delegate.onServerActivity();
  }

  /**
   * Configures the heartbeat.
   * @param ping ping time
   * @param pong pong time
   * @param pingHandler the ping handler
   */
  public void configureHeartbeat(long ping, long pong, Handler<StompServerConnection> pingHandler) { 
    delegate.configureHeartbeat(ping, pong, new Handler<io.vertx.ext.stomp.StompServerConnection>() {
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        pingHandler.handle(StompServerConnection.newInstance(event));
      }
    });
  }


  public static StompServerConnection newInstance(io.vertx.ext.stomp.StompServerConnection arg) {
    return arg != null ? new StompServerConnection(arg) : null;
  }
}
