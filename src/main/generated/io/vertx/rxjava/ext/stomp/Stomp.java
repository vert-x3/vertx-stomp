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
import io.vertx.rxjava.core.net.NetServer;
import io.vertx.rxjava.core.Vertx;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.stomp.StompClientOptions;

/**
 * Interface used to create STOMP server and clients.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.Stomp original} non RX-ified interface using Vert.x codegen.
 */

public class Stomp {

  final io.vertx.ext.stomp.Stomp delegate;

  public Stomp(io.vertx.ext.stomp.Stomp delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static StompServer createStompServer(Vertx vertx) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public static StompServer createStompServer(Vertx vertx, StompServerOptions options) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  public static StompServer createStompServer(Vertx vertx, NetServer netServer) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx) vertx.getDelegate(), (io.vertx.core.net.NetServer) netServer.getDelegate()));
    return ret;
  }

  public static StompServer createStompServer(Vertx vertx, NetServer netServer, StompServerOptions options) { 
    StompServer ret= StompServer.newInstance(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx) vertx.getDelegate(), (io.vertx.core.net.NetServer) netServer.getDelegate(), options));
    return ret;
  }

  public static StompClient createStompClient(Vertx vertx) { 
    StompClient ret= StompClient.newInstance(io.vertx.ext.stomp.Stomp.createStompClient((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public static StompClient createStompClient(Vertx vertx, StompClientOptions options) { 
    StompClient ret= StompClient.newInstance(io.vertx.ext.stomp.Stomp.createStompClient((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }


  public static Stomp newInstance(io.vertx.ext.stomp.Stomp arg) {
    return arg != null ? new Stomp(arg) : null;
  }
}
