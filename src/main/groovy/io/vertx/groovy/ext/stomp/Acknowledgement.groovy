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
import java.util.List
import io.vertx.ext.stomp.Frame
/**
 * Structure passed to acknowledgement handler called when a <code>ACK</code> or <code>NACK</code> frame is received. The handler
 * receives an instance of {@link io.vertx.groovy.ext.stomp.Acknowledgement} with the subscription <a href="../../../../../../../cheatsheet/Frame.html">Frame</a> and the impacted messages. The
 * list of messages depends on the type of acknowledgment used by the subscription.
 * <p/>
 * Subscriptions using the <code>client</code> mode receives all messages that were waiting for acknowledgment that were
 * sent before the acknowledged messages. The list also contains the acknowledged message. This is a cumulative
 * acknowledgement. Subscriptions using the <code>client-individual</code> mode receives a singleton list containing only
 * the acknowledged message.
*/
@CompileStatic
public class Acknowledgement {
  private final def io.vertx.ext.stomp.Acknowledgement delegate;
  public Acknowledgement(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Acknowledgement) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the subscription frame
   * @return  (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   */
  public Map<String, Object> subscription() {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(this.delegate.subscription()?.toJson());
    return ret;
  }
  /**
   * @return the list of frames that have been acknowledged / not-acknowledged. The content of the list depends on
   * the type of subscription.
   * @return 
   */
  public List<Map<String, Object>> frames() {
    def ret = this.delegate.frames()?.collect({underpants -> (Map<String, Object>)InternalHelper.wrapObject(underpants?.toJson())}) as List;
    return ret;
  }
}
