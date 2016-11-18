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

/** @module vertx-stomp-js/stomp_server_connection */
var utils = require('vertx-js/util/utils');
var StompServerHandler = require('vertx-stomp-js/stomp_server_handler');
var Buffer = require('vertx-js/buffer');
var StompServer = require('vertx-stomp-js/stomp_server');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStompServerConnection = io.vertx.ext.stomp.StompServerConnection;
var Frame = io.vertx.ext.stomp.Frame;

/**
 Class representing a connection between a STOMP client a the server. It keeps a references on the client socket,
 so let write to this socket.

 @class
*/
var StompServerConnection = function(j_val) {

  var j_stompServerConnection = j_val;
  var that = this;

  /**
   Writes the given buffer to the socket. This is a low level API that should be used carefully.

   @public
   @param buffer {Buffer} the buffer 
   @return {StompServerConnection} the current {@link StompServerConnection}
   */
  this.write = function() {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      j_stompServerConnection["write(io.vertx.ext.stomp.Frame)"](__args[0] != null ? new Frame(new JsonObject(JSON.stringify(__args[0]))) : null);
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerConnection["write(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {StompServer} the STOMP server serving this connection.
   */
  this.server = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(StompServer, j_stompServerConnection["server()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {StompServerHandler} the STOMP server handler dealing with this connection
   */
  this.handler = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(StompServerHandler, j_stompServerConnection["handler()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the STOMP session id computed when the client has established the connection to the server
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_stompServerConnection["session()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Closes the connection with the client.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_stompServerConnection["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sends a `PING` frame to the client. A `PING` frame is a frame containing only <code>EOL</code>.

   @public

   */
  this.ping = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_stompServerConnection["ping()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Notifies the connection about server activity (the server has sent a frame). This method is used to handle the
   heartbeat.

   @public

   */
  this.onServerActivity = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_stompServerConnection["onServerActivity()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the heartbeat.

   @public
   @param ping {number} ping time 
   @param pong {number} pong time 
   @param pingHandler {function} the ping handler 
   */
  this.configureHeartbeat = function(ping, pong, pingHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_stompServerConnection["configureHeartbeat(long,long,io.vertx.core.Handler)"](ping, pong, function(jVal) {
      pingHandler(utils.convReturnVertxGen(StompServerConnection, jVal));
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stompServerConnection;
};

StompServerConnection._jclass = utils.getJavaClass("io.vertx.ext.stomp.StompServerConnection");
StompServerConnection._jtype = {
  accept: function(obj) {
    return StompServerConnection._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(StompServerConnection.prototype, {});
    StompServerConnection.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
StompServerConnection._create = function(jdel) {
  var obj = Object.create(StompServerConnection.prototype, {});
  StompServerConnection.apply(obj, arguments);
  return obj;
}
module.exports = StompServerConnection;