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
import io.vertx.groovy.core.Vertx
/**
 * Interface implemented to customize the destination creation.
*/
@CompileStatic
public class DestinationFactory {
  private final def io.vertx.ext.stomp.DestinationFactory delegate;
  public DestinationFactory(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.DestinationFactory) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates a destination for the given <em>address</em>.
   * @param vertx the vert.x instance used by the STOMP server.
   * @param name the destination name.
   * @return the destination, <code>null</code> to reject the creation.
   */
  public Destination create(Vertx vertx, String name) {
    def ret= InternalHelper.safeCreate(this.delegate.create((io.vertx.core.Vertx)vertx.getDelegate(), name), io.vertx.groovy.ext.stomp.Destination.class);
    return ret;
  }
}
