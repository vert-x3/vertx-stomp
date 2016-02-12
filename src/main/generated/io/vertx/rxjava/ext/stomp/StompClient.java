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
import io.vertx.ext.stomp.Frame;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.net.NetClient;
import io.vertx.ext.stomp.StompClientOptions;

/**
 * Defines a STOMP client.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompClient original} non RX-ified interface using Vert.x codegen.
 */

public class StompClient {

  final io.vertx.ext.stomp.StompClient delegate;

  public StompClient(io.vertx.ext.stomp.StompClient delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompClient} using the default implementation.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompClient}
   */
  public static StompClient create(Vertx vertx) { 
    StompClient ret= StompClient.newInstance(io.vertx.ext.stomp.StompClient.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompClient} using the default implementation.
   * @param vertx the vert.x instance to use
   * @param options the options
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompClient}
   */
  public static StompClient create(Vertx vertx, StompClientOptions options) { 
    StompClient ret= StompClient.newInstance(io.vertx.ext.stomp.StompClient.create((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClient}
   */
  public StompClient connect(int port, String host, Handler<AsyncResult<StompClientConnection>> resultHandler) { 
    this.delegate.connect(port, host, new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> event) {
        AsyncResult<StompClientConnection> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompClientConnection>result(new StompClientConnection(event.result()));
        } else {
          f = InternalHelper.<StompClientConnection>failure(event.cause());
        }
        resultHandler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @return 
   */
  public Observable<StompClientConnection> connectObservable(int port, String host) { 
    io.vertx.rx.java.ObservableFuture<StompClientConnection> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    connect(port, host, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Connects to the server.
   * @param net the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.ext.stomp.StompClient}
   */
  public StompClient connect(NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler) { 
    this.delegate.connect((io.vertx.core.net.NetClient) net.getDelegate(), new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> event) {
        AsyncResult<StompClientConnection> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompClientConnection>result(new StompClientConnection(event.result()));
        } else {
          f = InternalHelper.<StompClientConnection>failure(event.cause());
        }
        resultHandler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects to the server.
   * @param net the NET client to use
   * @return 
   */
  public Observable<StompClientConnection> connectObservable(NetClient net) { 
    io.vertx.rx.java.ObservableFuture<StompClientConnection> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    connect(net, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @param net the NET client to use
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.ext.stomp.StompClient}
   */
  public StompClient connect(int port, String host, NetClient net, Handler<AsyncResult<StompClientConnection>> resultHandler) { 
    this.delegate.connect(port, host, (io.vertx.core.net.NetClient) net.getDelegate(), new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> event) {
        AsyncResult<StompClientConnection> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompClientConnection>result(new StompClientConnection(event.result()));
        } else {
          f = InternalHelper.<StompClientConnection>failure(event.cause());
        }
        resultHandler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects to the server.
   * @param port the server port
   * @param host the server host
   * @param net the NET client to use
   * @return 
   */
  public Observable<StompClientConnection> connectObservable(int port, String host, NetClient net) { 
    io.vertx.rx.java.ObservableFuture<StompClientConnection> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    connect(port, host, net, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Connects to the server using the host and port configured in the client's options.
   * @param resultHandler handler called with the connection result
   * @return the current {@link io.vertx.ext.stomp.StompClient}
   */
  public StompClient connect(Handler<AsyncResult<StompClientConnection>> resultHandler) { 
    this.delegate.connect(new Handler<AsyncResult<io.vertx.ext.stomp.StompClientConnection>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompClientConnection> event) {
        AsyncResult<StompClientConnection> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompClientConnection>result(new StompClientConnection(event.result()));
        } else {
          f = InternalHelper.<StompClientConnection>failure(event.cause());
        }
        resultHandler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects to the server using the host and port configured in the client's options.
   * @return 
   */
  public Observable<StompClientConnection> connectObservable() { 
    io.vertx.rx.java.ObservableFuture<StompClientConnection> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    connect(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Configures a received handler that gets notified when a STOMP frame is received by the client.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   *
   * When a connection is created, the handler is used as
   * {@link io.vertx.ext.stomp.StompClientConnection}.
   * @param handler the handler
   * @return the current {@link io.vertx.ext.stomp.StompClientConnection}
   */
  public StompClient receivedFrameHandler(Handler<Frame> handler) { 
    this.delegate.receivedFrameHandler(handler);
    return this;
  }

  /**
   * Configures a writing handler that gets notified when a STOMP frame is written on the wire.
   * This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   *
   * When a connection is created, the handler is used as
   * {@link io.vertx.rxjava.ext.stomp.StompClientConnection#writingFrameHandler}.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompClientConnection}
   */
  public StompClient writingFrameHandler(Handler<Frame> handler) { 
    this.delegate.writingFrameHandler(handler);
    return this;
  }

  /**
   * Closes the client.
   */
  public void close() { 
    this.delegate.close();
  }

  /**
   * @return the client's options.
   * @return 
   */
  public StompClientOptions options() { 
    StompClientOptions ret = this.delegate.options();
    return ret;
  }

  /**
   * @return the vert.x instance used by the client.
   * @return 
   */
  public Vertx vertx() { 
    Vertx ret= Vertx.newInstance(this.delegate.vertx());
    return ret;
  }


  public static StompClient newInstance(io.vertx.ext.stomp.StompClient arg) {
    return arg != null ? new StompClient(arg) : null;
  }
}
