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

package io.vertx.groovy.ext.stomp;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.ext.stomp.Frame
import io.vertx.core.Handler
/**
 * Class representing a connection between a STOMP client a the server. It keeps a references on the client socket,
 * so let write to this socket.
*/
@CompileStatic
public class StompServerConnection {
  private final def io.vertx.ext.stomp.StompServerConnection delegate;
  public StompServerConnection(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.StompServerConnection) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Writes the given frame to the socket.
   * @param frame the frame, must not be <code>null</code>. (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerConnection}
   */
  public StompServerConnection write(Map<String, Object> frame = [:]) {
    delegate.write(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null);
    return this;
  }
  /**
   * Writes the given buffer to the socket. This is a low level API that should be used carefully.
   * @param buffer the buffer
   * @return the current {@link io.vertx.groovy.ext.stomp.StompServerConnection}
   */
  public StompServerConnection write(Buffer buffer) {
    delegate.write(buffer != null ? (io.vertx.core.buffer.Buffer)buffer.getDelegate() : null);
    return this;
  }
  /**
   * @return the STOMP server serving this connection.
   */
  public StompServer server() {
    def ret = InternalHelper.safeCreate(delegate.server(), io.vertx.groovy.ext.stomp.StompServer.class);
    return ret;
  }
  /**
   * @return the STOMP server handler dealing with this connection
   */
  public StompServerHandler handler() {
    def ret = InternalHelper.safeCreate(delegate.handler(), io.vertx.groovy.ext.stomp.StompServerHandler.class);
    return ret;
  }
  /**
   * @return the STOMP session id computed when the client has established the connection to the server
   */
  public String session() {
    def ret = delegate.session();
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
    delegate.configureHeartbeat(ping, pong, pingHandler != null ? new Handler<io.vertx.ext.stomp.StompServerConnection>(){
      public void handle(io.vertx.ext.stomp.StompServerConnection event) {
        pingHandler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.stomp.StompServerConnection.class));
      }
    } : null);
  }
}
