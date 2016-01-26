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
import io.vertx.rxjava.core.http.ServerWebSocket;
import io.vertx.rxjava.core.net.NetServer;
import io.vertx.rxjava.core.Vertx;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Defines a STOMP server. STOMP servers delegates to a {@link io.vertx.rxjava.ext.stomp.StompServerHandler} that let customize the behavior of
 * the server. By default, it uses a handler compliant with the STOMP specification, but let you change anything.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompServer original} non RX-ified interface using Vert.x codegen.
 */

public class StompServer {

  final io.vertx.ext.stomp.StompServer delegate;

  public StompServer(io.vertx.ext.stomp.StompServer delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompServer} based on the default Stomp Server implementation.
   * @param vertx the vert.x instance to use
   * @param options the server options
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public static StompServer create(Vertx vertx, StompServerOptions options) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.StompServer.create((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompServer} based on the default Stomp Server implementation.
   * @param vertx the vert.x instance to use
   * @param netServer the Net server used by the STOMP server
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public static StompServer create(Vertx vertx, NetServer netServer) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.StompServer.create((io.vertx.core.Vertx) vertx.getDelegate(), (io.vertx.core.net.NetServer) netServer.getDelegate()));
    return ret;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompServer} based on the default Stomp Server implementation.
   * @param vertx the vert.x instance to use
   * @param net the Net server used by the STOMP server
   * @param options the server options
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public static StompServer create(Vertx vertx, NetServer net, StompServerOptions options) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.StompServer.create((io.vertx.core.Vertx) vertx.getDelegate(), (io.vertx.core.net.NetServer) net.getDelegate(), options));
    return ret;
  }

  /**
   * Creates a {@link io.vertx.rxjava.ext.stomp.StompServer} based on the default Stomp Server implementation, and use the default options.
   * @param vertx the vert.x instance to use
   * @return the created {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public static StompServer create(Vertx vertx) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.StompServer.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * Configures the {@link io.vertx.rxjava.ext.stomp.StompServerHandler}. You must calls this method before calling the {@link io.vertx.rxjava.ext.stomp.StompServer#listen} method.
   * @param handler the handler
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public StompServer handler(StompServerHandler handler) { 
    this.delegate.handler((io.vertx.ext.stomp.StompServerHandler) handler.getDelegate());
    return this;
  }

  /**
   * Connects the STOMP server to the given port.
   * @param port the port
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public StompServer listen(int port) { 
    this.delegate.listen(port);
    return this;
  }

  /**
   * Connects the STOMP server to the given port / interface.
   * @param port the port
   * @param host the interface
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public StompServer listen(int port, String host) { 
    this.delegate.listen(port, host);
    return this;
  }

  /**
   * Connects the STOMP server to the port / host configured in the server options.
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public StompServer listen() { 
    this.delegate.listen();
    return this;
  }

  /**
   * Connects the STOMP server default port (61613) and network interface (<code>0.0.0.0</code>). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   * @param handler the handler to call with the result
   * @return the current {@link io.vertx.rxjava.ext.stomp.StompServer}
   */
  public StompServer listen(Handler<AsyncResult<StompServer>> handler) { 
    this.delegate.listen(new Handler<AsyncResult<io.vertx.ext.stomp.StompServer>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompServer> event) {
        AsyncResult<StompServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompServer>result(new StompServer(event.result()));
        } else {
          f = InternalHelper.<StompServer>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects the STOMP server default port (61613) and network interface (<code>0.0.0.0</code>). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   * @return 
   */
  public Observable<StompServer> listenObservable() { 
    io.vertx.rx.java.ObservableFuture<StompServer> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(handler.toHandler());
    return handler;
  }

  /**
   * Connects the STOMP server to the given port. This method use the default host (<code>0.0.0.0</code>). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   * @param port the port
   * @param handler the handler to call with the result
   * @return the current {@link io.vertx.ext.stomp.StompServer}
   */
  public StompServer listen(int port, Handler<AsyncResult<StompServer>> handler) { 
    this.delegate.listen(port, new Handler<AsyncResult<io.vertx.ext.stomp.StompServer>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompServer> event) {
        AsyncResult<StompServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompServer>result(new StompServer(event.result()));
        } else {
          f = InternalHelper.<StompServer>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects the STOMP server to the given port. This method use the default host (<code>0.0.0.0</code>). Once the socket
   * it bounds calls the given handler with the result. The result may be a failure if the socket is already used.
   * @param port the port
   * @return 
   */
  public Observable<StompServer> listenObservable(int port) { 
    io.vertx.rx.java.ObservableFuture<StompServer> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(port, handler.toHandler());
    return handler;
  }

  /**
   * Connects the STOMP server to the given port / interface. Once the socket it bounds calls the given handler with
   * the result. The result may be a failure if the socket is already used.
   * @param port the port
   * @param host the host / interface
   * @param handler the handler to call with the result
   * @return the current {@link io.vertx.ext.stomp.StompServer}
   */
  public StompServer listen(int port, String host, Handler<AsyncResult<StompServer>> handler) { 
    this.delegate.listen(port, host, new Handler<AsyncResult<io.vertx.ext.stomp.StompServer>>() {
      public void handle(AsyncResult<io.vertx.ext.stomp.StompServer> event) {
        AsyncResult<StompServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<StompServer>result(new StompServer(event.result()));
        } else {
          f = InternalHelper.<StompServer>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  /**
   * Connects the STOMP server to the given port / interface. Once the socket it bounds calls the given handler with
   * the result. The result may be a failure if the socket is already used.
   * @param port the port
   * @param host the host / interface
   * @return 
   */
  public Observable<StompServer> listenObservable(int port, String host) { 
    io.vertx.rx.java.ObservableFuture<StompServer> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(port, host, handler.toHandler());
    return handler;
  }

  /**
   * Closes the server.
   * @param completionHandler handler called once the server has been stopped
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) { 
    this.delegate.close(completionHandler);
  }

  /**
   * Closes the server.
   * @return 
   */
  public Observable<Void> closeObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Closes the server.
   */
  public void close() { 
    this.delegate.close();
  }

  /**
   * Checks whether or not the server is listening.
   * @return <code>true</code> if the server is listening, <code>false</code> otherwise
   */
  public boolean isListening() { 
    boolean ret = this.delegate.isListening();
    return ret;
  }

  /**
   * Gets the port on which the server is listening.
   * <p/>
   * This is useful if you bound the server specifying 0 as port number signifying an ephemeral port.
   * @return the port
   */
  public int actualPort() { 
    int ret = this.delegate.actualPort();
    return ret;
  }

  /**
   * @return the server options
   * @return 
   */
  public StompServerOptions options() { 
    StompServerOptions ret = this.delegate.options();
    return ret;
  }

  /**
   * @return the instance of vert.x used by the server.
   * @return 
   */
  public Vertx vertx() { 
    Vertx ret= Vertx.newInstance(this.delegate.vertx());
    return ret;
  }

  /**
   * @return the {@link io.vertx.rxjava.ext.stomp.StompServerHandler} used by this server.
   * @return 
   */
  public StompServerHandler stompHandler() { 
    StompServerHandler ret= StompServerHandler.newInstance(this.delegate.stompHandler());
    return ret;
  }

  /**
   * Gets the  able to manage web socket connections. If the web socket bridge is disabled, it returns
   * <code>null</code>.
   * @return the handler that can be passed to {@link io.vertx.rxjava.core.http.HttpServer#websocketHandler}.
   */
  public Handler<ServerWebSocket> webSocketHandler() { 
    io.vertx.core.Handler<io.vertx.core.http.ServerWebSocket> handlerDelegate = this.delegate.webSocketHandler();
    Handler<ServerWebSocket> ret = event -> {
      handlerDelegate.handle((io.vertx.core.http.ServerWebSocket) event.getDelegate());
    };
    return ret;
  }


  public static StompServer newInstance(io.vertx.ext.stomp.StompServer arg) {
    return arg != null ? new StompServer(arg) : null;
  }
}
