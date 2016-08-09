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
import io.vertx.ext.stomp.Frame
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.groovy.core.net.NetClient
import io.vertx.ext.stomp.StompClientOptions
/**
 * Defines a STOMP client.
*/
@CompileStatic
public class StompClient {
  private final def io.vertx.ext.stomp.StompClient delegate;
  public StompClient(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.StompClient) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates a {@link io.vertx.groovy.ext.stomp.StompClient} using the default implementation.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public static StompClient create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.StompClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.groovy.ext.stomp.StompClient.class);
    return ret;
  }
  /**
   * Creates a {@link io.vertx.groovy.ext.stomp.StompClient} using the default implementation.
   * @param vertx the vert.x instance to use
   * @param options the options (see <a href="../../../../../../../cheatsheet/StompClientOptions.html">StompClientOptions</a>)
   * @return the created {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public static StompClient create(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.stomp.StompClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.stomp.StompClientOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null), io.vertx.groovy.ext.stomp.StompClient.class);
    return ret;
  }
  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient connect(int port, String host, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    delegate.connect(port, host, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), io.vertx.groovy.ext.stomp.StompClientConnection.class)));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Connects to the server.
   * @param net the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient connect(NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    delegate.connect(net != null ? (io.vertx.core.net.NetClient)net.getDelegate() : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), io.vertx.groovy.ext.stomp.StompClientConnection.class)));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @param net the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient connect(int port, String host, NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler) {
    delegate.connect(port, host, net != null ? (io.vertx.core.net.NetClient)net.getDelegate() : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), io.vertx.groovy.ext.stomp.StompClientConnection.class)));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Connects to the server using the host and port configured in the client's options.
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient connect(Handler<AsyncResult<StompClientConnection>> resultHandler) {
    delegate.connect(resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(InternalHelper.safeCreate(ar.result(), io.vertx.groovy.ext.stomp.StompClientConnection.class)));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Configures a received handler that gets notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   * <p>
   * When a connection is created, the handler is used as
   * {@link io.vertx.groovy.ext.stomp.StompClientConnection#receivedFrameHandler}.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient receivedFrameHandler(Handler<Map<String, Object>> handler) {
    delegate.receivedFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Configures a writing handler that gets notified when a STOMP frame is written on the wire.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   * <p>
   * When a connection is created, the handler is used as
   * {@link io.vertx.groovy.ext.stomp.StompClientConnection#writingFrameHandler}.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient writingFrameHandler(Handler<Map<String, Object>> handler) {
    delegate.writingFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * A general error frame handler. It can be used to catch <code>ERROR</code> frame emitted during the connection process
   * (wrong authentication). This error handler will be pass to all {@link io.vertx.groovy.ext.stomp.StompClientConnection} created from this
   * client. Obviously, the client can override it when the connection is established.
   * @param handler the handler
   * @return the current {@link io.vertx.groovy.ext.stomp.StompClient}
   */
  public StompClient errorFrameHandler(Handler<Map<String, Object>> handler) {
    delegate.errorFrameHandler(handler != null ? new Handler<io.vertx.ext.stomp.Frame>(){
      public void handle(io.vertx.ext.stomp.Frame event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event?.toJson()));
      }
    } : null);
    return this;
  }
  /**
   * Closes the client.
   */
  public void close() {
    delegate.close();
  }
  /**
   * @return the client's options. (see <a href="../../../../../../../cheatsheet/StompClientOptions.html">StompClientOptions</a>)
   */
  public Map<String, Object> options() {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(delegate.options()?.toJson());
    return ret;
  }
  /**
   * @return the vert.x instance used by the client.
   */
  public Vertx vertx() {
    def ret = InternalHelper.safeCreate(delegate.vertx(), io.vertx.groovy.core.Vertx.class);
    return ret;
  }
  /**
   * @return whether or not the client is connected to the server.
   */
  public boolean isClosed() {
    def ret = delegate.isClosed();
    return ret;
  }
}
