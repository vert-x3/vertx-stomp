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
import java.util.Map
/**
 * Utility methods to build common <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>s. It defines a non-STOMP frame (<code>PING</code>) that is used for
 * heartbeats. When such frame is written on the wire it is just the <code>0</code> byte.
 * <p/>
 * This class is thread-safe.
*/
@CompileStatic
public class Frames {
  private final def io.vertx.ext.stomp.Frames delegate;
  public Frames(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Frames) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Map<String, Object> createErrorFrame(String message, Map<String, String> headers, String body) {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(io.vertx.ext.stomp.Frames.createErrorFrame(message, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null, body)?.toJson());
    return ret;
  }
  public static Map<String, Object> createReceiptFrame(String receiptId, Map<String, String> headers) {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(io.vertx.ext.stomp.Frames.createReceiptFrame(receiptId, headers != null ? (Map)headers.collectEntries({[it.key,it.value]}) : null)?.toJson());
    return ret;
  }
  public static void handleReceipt(Map<String, Object> frame = [:], StompServerConnection connection) {
    io.vertx.ext.stomp.Frames.handleReceipt(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.InternalHelper.toJsonObject(frame)) : null, connection != null ? (io.vertx.ext.stomp.StompServerConnection)connection.getDelegate() : null);
  }
  public static Map<String, Object> ping() {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(io.vertx.ext.stomp.Frames.ping()?.toJson());
    return ret;
  }
}
