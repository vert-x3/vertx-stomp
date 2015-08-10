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
import io.vertx.ext.stomp.Frame;

/**
 * Handles a client frame. This type of handler are called when the client receives a STOMP frame and let implement
 * the behavior.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.ClientFrameHandler original} non RX-ified interface using Vert.x codegen.
 */

public class ClientFrameHandler {

  final io.vertx.ext.stomp.ClientFrameHandler delegate;

  public ClientFrameHandler(io.vertx.ext.stomp.ClientFrameHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Handler called when a client frame has been received.
   * @param frame the frame
   * @param connection the client connection that has received the frame
   */
  public void onFrame(Frame frame, StompClientConnection connection) { 
    this.delegate.onFrame(frame, (io.vertx.ext.stomp.StompClientConnection) connection.getDelegate());
  }


  public static ClientFrameHandler newInstance(io.vertx.ext.stomp.ClientFrameHandler arg) {
    return arg != null ? new ClientFrameHandler(arg) : null;
  }
}
