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
import java.util.List;
import io.vertx.ext.stomp.Frame;

/**
 * Represents a transaction in the STOMP server.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.Transaction original} non RX-ified interface using Vert.x codegen.
 */

public class Transaction {

  final io.vertx.ext.stomp.Transaction delegate;

  public Transaction(io.vertx.ext.stomp.Transaction delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Creates an instance of {@link io.vertx.rxjava.ext.stomp.Transaction} using the default implementation.
   * @param connection the connection (client)
   * @param id the transaction id
   * @return the created {@link io.vertx.rxjava.ext.stomp.Transaction}
   */
  public static Transaction create(StompServerConnection connection, String id) { 
    Transaction ret= Transaction.newInstance(io.vertx.ext.stomp.Transaction.create((io.vertx.ext.stomp.StompServerConnection) connection.getDelegate(), id));
    return ret;
  }

  /**
   * @return the connection
   * @return 
   */
  public StompServerConnection connection() { 
    StompServerConnection ret= StompServerConnection.newInstance(this.delegate.connection());
    return ret;
  }

  /**
   * @return the transaction id
   * @return 
   */
  public String id() { 
    String ret = this.delegate.id();
    return ret;
  }

  /**
   * Adds a frame to the transaction. By default, only <code>SEND, ACK and NACK</code> frames can be in transactions.
   * @param frame the frame to add
   * @return the current {@link io.vertx.ext.stomp.Transaction}
   */
  public Transaction addFrameToTransaction(Frame frame) { 
    this.delegate.addFrameToTransaction(frame);
    return this;
  }

  /**
   * Clears the list of frames added to the transaction.
   * @return the current {@link io.vertx.rxjava.ext.stomp.Transaction}
   */
  public Transaction clear() { 
    this.delegate.clear();
    return this;
  }

  /**
   * @return the ordered list of frames added to the transaction.
   * @return 
   */
  public List<Frame> getFrames() { 
    List<Frame> ret = this.delegate.getFrames();
;
    return ret;
  }


  public static Transaction newInstance(io.vertx.ext.stomp.Transaction arg) {
    return arg != null ? new Transaction(arg) : null;
  }
}
