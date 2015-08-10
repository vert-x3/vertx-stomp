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

/** @module vertx-stomp-js/stomp */
var utils = require('vertx-js/util/utils');
var StompClient = require('vertx-stomp-js/stomp_client');
var NetServer = require('vertx-js/net_server');
var Vertx = require('vertx-js/vertx');
var StompServer = require('vertx-stomp-js/stomp_server');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStomp = io.vertx.ext.stomp.Stomp;
var StompServerOptions = io.vertx.ext.stomp.StompServerOptions;
var StompClientOptions = io.vertx.ext.stomp.StompClientOptions;

/**
 Interface used to create STOMP server and clients.

 @class
*/
var Stomp = function(j_val) {

  var j_stomp = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stomp;
};

/**

 @memberof module:vertx-stomp-js/stomp
 @param vertx {Vertx} 
 @param netServer {NetServer} 
 @param options {Object} 
 @return {StompServer}
 */
Stomp.createStompServer = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JStomp["createStompServer(io.vertx.core.Vertx)"](__args[0]._jdel), StompServer);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
    return utils.convReturnVertxGen(JStomp["createStompServer(io.vertx.core.Vertx,io.vertx.ext.stomp.StompServerOptions)"](__args[0]._jdel, __args[1] != null ? new StompServerOptions(new JsonObject(JSON.stringify(__args[1]))) : null), StompServer);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel) {
    return utils.convReturnVertxGen(JStomp["createStompServer(io.vertx.core.Vertx,io.vertx.core.net.NetServer)"](__args[0]._jdel, __args[1]._jdel), StompServer);
  }else if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel && typeof __args[2] === 'object') {
    return utils.convReturnVertxGen(JStomp["createStompServer(io.vertx.core.Vertx,io.vertx.core.net.NetServer,io.vertx.ext.stomp.StompServerOptions)"](__args[0]._jdel, __args[1]._jdel, __args[2] != null ? new StompServerOptions(new JsonObject(JSON.stringify(__args[2]))) : null), StompServer);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-stomp-js/stomp
 @param vertx {Vertx} 
 @param options {Object} 
 @return {StompClient}
 */
Stomp.createStompClient = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JStomp["createStompClient(io.vertx.core.Vertx)"](__args[0]._jdel), StompClient);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
    return utils.convReturnVertxGen(JStomp["createStompClient(io.vertx.core.Vertx,io.vertx.ext.stomp.StompClientOptions)"](__args[0]._jdel, __args[1] != null ? new StompClientOptions(new JsonObject(JSON.stringify(__args[1]))) : null), StompClient);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Stomp;