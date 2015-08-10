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

/** @module vertx-stomp-js/authentication_handler */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAuthenticationHandler = io.vertx.ext.stomp.AuthenticationHandler;

/**

 @class
*/
var AuthenticationHandler = function(j_val) {

  var j_authenticationHandler = j_val;
  var that = this;

  /**
   The authentication handler responsible for checking whether the couple <code>login/passcode</code> are valid to
   connect to this server.

   @public
   @param login {string} the login 
   @param passcode {string} the password 
   @param resultHandler {function} the result handler invoked when the decision has been made. It receives <code>true</code> if the access is granted, <code>false</code> otherwise. 
   */
  this.authenticate = function(login, passcode, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_authenticationHandler["authenticate(java.lang.String,java.lang.String,io.vertx.core.Handler)"](login, passcode, function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_authenticationHandler;
};

// We export the Constructor function
module.exports = AuthenticationHandler;