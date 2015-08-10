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
import java.util.List
import io.vertx.ext.stomp.Frame
/**
 * Represents a transaction in the STOMP server.
*/
@CompileStatic
public class Transaction {
  private final def io.vertx.ext.stomp.Transaction delegate;
  public Transaction(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.Transaction) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates an instance of {@link io.vertx.groovy.ext.stomp.Transaction} using the default implementation.
   * @param connection the connection (client)
   * @param id the transaction id
   * @return the created {@link io.vertx.groovy.ext.stomp.Transaction}
   */
  public static Transaction create(StompServerConnection connection, String id) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.stomp.Transaction.create((io.vertx.ext.stomp.StompServerConnection)connection.getDelegate(), id), io.vertx.groovy.ext.stomp.Transaction.class);
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
  /**
   * @return the transaction id
   * @return 
   */
  public String id() {
    def ret = this.delegate.id();
    return ret;
  }
  /**
   * Adds a frame to the transaction. By default, only <code>SEND, ACK and NACK</code> frames can be in transactions.
   * @param frame the frame to add (see <a href="../../../../../../../cheatsheet/Frame.html">Frame</a>)
   * @return the current {@link io.vertx.groovy.ext.stomp.Transaction}
   */
  public Transaction addFrameToTransaction(Map<String, Object> frame = [:]) {
    this.delegate.addFrameToTransaction(frame != null ? new io.vertx.ext.stomp.Frame(new io.vertx.core.json.JsonObject(frame)) : null);
    return this;
  }
  /**
   * Clears the list of frames added to the transaction.
   * @return the current {@link io.vertx.groovy.ext.stomp.Transaction}
   */
  public Transaction clear() {
    this.delegate.clear();
    return this;
  }
  /**
   * @return the ordered list of frames added to the transaction.
   * @return 
   */
  public List<Map<String, Object>> getFrames() {
    def ret = this.delegate.getFrames()?.collect({underpants -> (Map<String, Object>)InternalHelper.wrapObject(underpants?.toJson())}) as List;
    return ret;
  }
}
