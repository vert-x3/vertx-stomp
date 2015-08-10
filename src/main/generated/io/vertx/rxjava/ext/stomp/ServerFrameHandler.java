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
 * Handles a server frame. This type of handler are called when the server receives a STOMP frame and let implement
 * the behavior.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.ServerFrameHandler original} non RX-ified interface using Vert.x codegen.
 */

public class ServerFrameHandler {

  final io.vertx.ext.stomp.ServerFrameHandler delegate;

  public ServerFrameHandler(io.vertx.ext.stomp.ServerFrameHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Handler called when a server frame has been received.
   * @param frame the frame
   * @param connection the server connection that has received the frame
   */
  public void onFrame(Frame frame, StompServerConnection connection) { 
    this.delegate.onFrame(frame, (io.vertx.ext.stomp.StompServerConnection) connection.getDelegate());
  }


  public static ServerFrameHandler newInstance(io.vertx.ext.stomp.ServerFrameHandler arg) {
    return arg != null ? new ServerFrameHandler(arg) : null;
  }
}
