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
 * Handler called by {@link io.vertx.ext.stomp.impl.FrameParser} when a STOMP frame has been parsed. STOMP client and
 * server use specialized versions passing the associated STOMP connection.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.FrameHandler original} non RX-ified interface using Vert.x codegen.
 */

public class FrameHandler {

  final io.vertx.ext.stomp.FrameHandler delegate;

  public FrameHandler(io.vertx.ext.stomp.FrameHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public void onFrame(Frame frame) { 
    this.delegate.onFrame(frame);
  }


  public static FrameHandler newInstance(io.vertx.ext.stomp.FrameHandler arg) {
    return arg != null ? new FrameHandler(arg) : null;
  }
}
