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
/**
 * Structure passed to server handler when receiving a frame. It provides a reference on the received <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>
 * but also on the {@link io.vertx.groovy.ext.stomp.StompServerConnection}.
*/
@CompileStatic
public class ServerFrame {
  private final def io.vertx.ext.stomp.ServerFrame delegate;
  public ServerFrame(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.ServerFrame) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the received frame
   * @return  (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   */
  public Map<String, Object> frame() {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(this.delegate.frame()?.toJson());
    return ret;
  }
  /**
   * @return the connection
   * @return 
   */
  public StompServerConnection connection() {
    def ret= InternalHelper.safeCreate(this.delegate.connection(), io.vertx.groovy.ext.stomp.StompServerConnection.class);
    return ret;
  }
}
