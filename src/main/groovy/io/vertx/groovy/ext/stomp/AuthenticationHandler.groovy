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
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * Handler called when an authentication is required by the server. Authentication happen during the <code>CONNECT /
 CONNECTED</code> phase. The <code>CONNECT</code> frame should includes the <code>login</code> and <code>passcode</code> header
 * (configured from the client option. The login and passcode are passed to the handler than can check whether or not
 * the access is granted. When the decision has been made, it must called the <code>resultHandler</code> with the value
 * <code>true</code> if the access if granted, <code>false</code> otherwise.
*/
@CompileStatic
public class AuthenticationHandler {
  private final def io.vertx.ext.stomp.AuthenticationHandler delegate;
  public AuthenticationHandler(Object delegate) {
    this.delegate = (io.vertx.ext.stomp.AuthenticationHandler) delegate;
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
}
