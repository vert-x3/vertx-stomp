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
import io.vertx.ext.stomp.Frame
/**
 * Handles a client frame. This type of handler are called when the client receives a STOMP frame and let implement
 * the behavior.
*/
@CompileStatic
public class ClientFrameHandler {
  private final def io.vertx.ext.stomp.ClientFrameHandler delegate;
  public ClientFrameHandler(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.ClientFrameHandler) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Handler called when a client frame has been received.
   * @param frame the frame (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @param connection the client connection that has received the frame
   */
  public void onFrame(Map<String, Object> frame = [:], StompClientConnection connection) {
    this.delegate.onFrame(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null, (io.vertx.ext.stomp.StompClientConnection)connection.getDelegate());
  }
}
