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
import rx.Observable;
import io.vertx.ext.stomp.Frame;

/**
 * Structure passed to server handler when receiving a frame. It provides a reference on the received {@link io.vertx.ext.stomp.Frame}
 * but also on the {@link io.vertx.rxjava.ext.stomp.StompServerConnection}.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.ServerFrame original} non RX-ified interface using Vert.x codegen.
 */

public class ServerFrame {

  final io.vertx.ext.stomp.ServerFrame delegate;

  public ServerFrame(io.vertx.ext.stomp.ServerFrame delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the received frame
   * @return 
   */
  public Frame frame() { 
    Frame ret = delegate.frame();
    return ret;
  }

  /**
   * @return the connection
   * @return 
   */
  public StompServerConnection connection() { 
    StompServerConnection ret = StompServerConnection.newInstance(delegate.connection());
    return ret;
  }


  public static ServerFrame newInstance(io.vertx.ext.stomp.ServerFrame arg) {
    return arg != null ? new ServerFrame(arg) : null;
  }
}
