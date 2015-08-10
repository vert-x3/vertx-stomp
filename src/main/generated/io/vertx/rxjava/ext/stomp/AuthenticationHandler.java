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
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Handler called when an authentication is required by the server. Authentication happen during the <code>CONNECT /
 CONNECTED</code> phase. The <code>CONNECT</code> frame should includes the <code>login</code> and <code>passcode</code> header
 * (configured from the client option. The login and passcode are passed to the handler than can check whether or not
 * the access is granted. When the decision has been made, it must called the <code>resultHandler</code> with the value
 * <code>true</code> if the access if granted, <code>false</code> otherwise.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.AuthenticationHandler original} non RX-ified interface using Vert.x codegen.
 */

public class AuthenticationHandler {

  final io.vertx.ext.stomp.AuthenticationHandler delegate;

  public AuthenticationHandler(io.vertx.ext.stomp.AuthenticationHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The authentication handler responsible for checking whether the couple <code>login/passcode</code> are valid to
   * connect to this server.
   * @param login the login
   * @param passcode the password
   * @param resultHandler the result handler invoked when the decision has been made. It receives <code>true</code> if the access is granted, <code>false</code> otherwise.
   */
  public void authenticate(String login, String passcode, Handler<AsyncResult<Boolean>> resultHandler) { 
    this.delegate.authenticate(login, passcode, resultHandler);
  }

  /**
   * The authentication handler responsible for checking whether the couple <code>login/passcode</code> are valid to
   * connect to this server.
   * @param login the login
   * @param passcode the password
   * @return 
   */
  public Observable<Boolean> authenticateObservable(String login, String passcode) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    authenticate(login, passcode, resultHandler.toHandler());
    return resultHandler;
  }


  public static AuthenticationHandler newInstance(io.vertx.ext.stomp.AuthenticationHandler arg) {
    return arg != null ? new AuthenticationHandler(arg) : null;
  }
}
