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

/** @module vertx-stomp-js/stomp_client */
var utils = require('vertx-js/util/utils');
var StompClientConnection = require('vertx-stomp-js/stomp_client_connection');
var Vertx = require('vertx-js/vertx');
var NetClient = require('vertx-js/net_client');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStompClient = io.vertx.ext.stomp.StompClient;
var Frame = io.vertx.ext.stomp.Frame;
var StompClientOptions = io.vertx.ext.stomp.StompClientOptions;

/**
 Defines a STOMP client.

 @class
*/
var StompClient = function(j_val) {

  var j_stompClient = j_val;
  var that = this;

  /**
   Connects to the server.

   @public
   @param port {number} the server port 
   @param host {string} the server host 
   @param net {NetClient} the NET client to use 
   @param resultHandler {function} handler called with the connection result 
   @return {StompClient} the current {@link StompClient}
   */
  this.connect = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompClient["connect(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](utils.convReturnVertxGen(ar.result(), StompClientConnection), null);
      } else {
        __args[0](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'function') {
      j_stompClient["connect(io.vertx.core.net.NetClient,io.vertx.core.Handler)"](__args[0]._jdel, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), StompClientConnection), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_stompClient["connect(int,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](utils.convReturnVertxGen(ar.result(), StompClientConnection), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 4 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'object' && __args[2]._jdel && typeof __args[3] === 'function') {
      j_stompClient["connect(int,java.lang.String,io.vertx.core.net.NetClient,io.vertx.core.Handler)"](__args[0], __args[1], __args[2]._jdel, function(ar) {
      if (ar.succeeded()) {
        __args[3](utils.convReturnVertxGen(ar.result(), StompClientConnection), null);
      } else {
        __args[3](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures a received handler that gets notified when a STOMP frame is received by the client.
   This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   <p>
   When a connection is created, the handler is used as
   {@link StompClientConnection#receivedFrameHandler}.

   @public
   @param handler {function} the handler 
   @return {StompClient} the current {@link StompClient}
   */
  this.receivedFrameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompClient["receivedFrameHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnDataObject(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures a writing handler that gets notified when a STOMP frame is written on the wire.
   This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
   <p>
   When a connection is created, the handler is used as
   {@link StompClientConnection#writingFrameHandler}.

   @public
   @param handler {function} the handler 
   @return {StompClient} the current {@link StompClient}
   */
  this.writingFrameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompClient["writingFrameHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnDataObject(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   A general error frame handler. It can be used to catch <code>ERROR</code> frame emitted during the connection process
   (wrong authentication). This error handler will be pass to all {@link StompClientConnection} created from this
   client. Obviously, the client can override it when the connection is established.

   @public
   @param handler {function} the handler 
   @return {StompClient} the current {@link StompClient}
   */
  this.errorFrameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompClient["errorFrameHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnDataObject(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Closes the client.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_stompClient["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Object} the client's options.
   */
  this.options = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnDataObject(j_stompClient["options()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Vertx} the vert.x instance used by the client.
   */
  this.vertx = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_stompClient["vertx()"](), Vertx);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} whether or not the client is connected to the server.
   */
  this.isClosed = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_stompClient["isClosed()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stompClient;
};

/**
 Creates a {@link StompClient} using the default implementation.

 @memberof module:vertx-stomp-js/stomp_client
 @param vertx {Vertx} the vert.x instance to use 
 @param options {Object} the options 
 @return {StompClient} the created {@link StompClient}
 */
StompClient.create = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JStompClient["create(io.vertx.core.Vertx)"](__args[0]._jdel), StompClient);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JStompClient["create(io.vertx.core.Vertx,io.vertx.ext.stomp.StompClientOptions)"](__args[0]._jdel, __args[1] != null ? new StompClientOptions(new JsonObject(JSON.stringify(__args[1]))) : null), StompClient);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = StompClient;