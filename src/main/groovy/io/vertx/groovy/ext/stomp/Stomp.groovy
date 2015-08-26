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
import io.vertx.groovy.core.net.NetServer
import io.vertx.groovy.core.Vertx
import io.vertx.ext.stomp.StompServerOptions
import io.vertx.ext.stomp.StompClientOptions
/**
 * Interface used to create STOMP server and clients.
*/
@CompileStatic
public class Stomp {
  private final def io.vertx.ext.stomp.Stomp delegate;
  public Stomp(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Stomp) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static StompServer createStompServer(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.stomp.StompServer.class);
    return ret;
  }
  public static StompServer createStompServer(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.stomp.StompServerOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.stomp.StompServer.class);
    return ret;
  }
  public static StompServer createStompServer(Vertx vertx, NetServer netServer) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.core.net.NetServer)netServer.getDelegate()), io.vertx.groovy.ext.stomp.StompServer.class);
    return ret;
  }
  public static StompServer createStompServer(Vertx vertx, NetServer netServer, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompServer((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.core.net.NetServer)netServer.getDelegate(), options != null ? new io.vertx.ext.stomp.StompServerOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.stomp.StompServer.class);
    return ret;
  }
  public static StompClient createStompClient(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompClient((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.stomp.StompClient.class);
    return ret;
  }
  public static StompClient createStompClient(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Stomp.createStompClient((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.stomp.StompClientOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.stomp.StompClient.class);
    return ret;
  }
}
