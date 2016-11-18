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

/** @module vertx-stomp-js/stomp_server_handler */
var utils = require('vertx-js/util/utils');
var Destination = require('vertx-stomp-js/destination');
var DestinationFactory = require('vertx-stomp-js/destination_factory');
var Acknowledgement = require('vertx-stomp-js/acknowledgement');
var Vertx = require('vertx-js/vertx');
var StompServer = require('vertx-stomp-js/stomp_server');
var ServerFrame = require('vertx-stomp-js/server_frame');
var StompServerConnection = require('vertx-stomp-js/stomp_server_connection');
var AuthProvider = require('vertx-auth-common-js/auth_provider');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStompServerHandler = io.vertx.ext.stomp.StompServerHandler;
var BridgeOptions = io.vertx.ext.stomp.BridgeOptions;
var Frame = io.vertx.ext.stomp.Frame;

/**
 STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
 let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
 designed to let you customize the server behavior. The default implementation is compliant with the STOMP
 specification. In this default implementation, not acknowledge frames are dropped.

 @class
*/
var StompServerHandler = function(j_val) {

  var j_stompServerHandler = j_val;
  var that = this;

  /**

   @public
   @param arg0 {ServerFrame} 
   */
  this.handle = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["handle(io.vertx.ext.stomp.ServerFrame)"](arg0._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures a handler that get notified when a STOMP frame is received by the server.
   This handler can be used for logging, debugging or ad-hoc behavior.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.receivedFrameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["receivedFrameHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>CONNECT</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.connectHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["connectHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>STOMP</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.stompHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["stompHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.subscribeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["subscribeHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.unsubscribeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["unsubscribeHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>SEND</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.sendHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["sendHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a connection with the client is closed.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["closeHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(StompServerConnection, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Called when the connection is closed. This method executes a default behavior and must calls the configured
   {@link StompServerHandler#closeHandler} if any.

   @public
   @param connection {StompServerConnection} the connection 
   */
  this.onClose = function(connection) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["onClose(io.vertx.ext.stomp.StompServerConnection)"](connection._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>COMMIT</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.commitHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["commitHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>ABORT</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.abortHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["abortHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>BEGIN</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.beginHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["beginHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>DISCONNECT</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.disconnectHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["disconnectHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>ACK</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.ackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["ackHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when a <code>NACK</code> frame is received.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.nackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["nackHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(ServerFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Called when the client connects to a server requiring authentication. It invokes the  configured
   using {@link StompServerHandler#authProvider}.

   @public
   @param server {StompServer} the STOMP server. 
   @param login {string} the login 
   @param passcode {string} the password 
   @param handler {function} handler receiving the authentication result 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onAuthenticationRequest = function(server, login, passcode, handler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string' && typeof __args[2] === 'string' && typeof __args[3] === 'function') {
      j_stompServerHandler["onAuthenticationRequest(io.vertx.ext.stomp.StompServer,java.lang.String,java.lang.String,io.vertx.core.Handler)"](server._jdel, login, passcode, function(ar) {
      if (ar.succeeded()) {
        handler(ar.result(), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the  to be used to authenticate the user.

   @public
   @param handler {AuthProvider} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.authProvider = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["authProvider(io.vertx.ext.auth.AuthProvider)"](handler._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<Destination>} the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as opaque Strings.
   */
  this.getDestinations = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_stompServerHandler["getDestinations()"](), Destination);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets the destination with the given name.

   @public
   @param destination {string} the destination 
   @return {Destination} the {@link Destination}, <code>null</code> if not existing.
   */
  this.getDestination = function(destination) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(Destination, j_stompServerHandler["getDestination(java.lang.String)"](destination));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   Implementations must call the handler configured using {@link StompServerHandler#onAckHandler}.

   @public
   @param connection {StompServerConnection} the connection 
   @param subscribe {Object} the <code>SUBSCRIBE</code> frame 
   @param messages {Array.<Object>} the acknowledge messages 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onAck = function(connection, subscribe, messages) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'object' && __args[2] instanceof Array) {
      j_stompServerHandler["onAck(io.vertx.ext.stomp.StompServerConnection,io.vertx.ext.stomp.Frame,java.util.List)"](connection._jdel, subscribe != null ? new Frame(new JsonObject(JSON.stringify(subscribe))) : null, utils.convParamListDataObject(messages, function(json) { return new Frame(json); }));
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Method called by single message (client-individual policy) or a set of message (client policy) are
   <strong>not</strong> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
   <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
   {@link StompServerHandler#onNackHandler}.

   @public
   @param connection {StompServerConnection} the connection 
   @param subscribe {Object} the <code>SUBSCRIBE</code> frame 
   @param messages {Array.<Object>} the acknowledge messages 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onNack = function(connection, subscribe, messages) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'object' && __args[2] instanceof Array) {
      j_stompServerHandler["onNack(io.vertx.ext.stomp.StompServerConnection,io.vertx.ext.stomp.Frame,java.util.List)"](connection._jdel, subscribe != null ? new Frame(new JsonObject(JSON.stringify(subscribe))) : null, utils.convParamListDataObject(messages, function(json) { return new Frame(json); }));
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when messages are acknowledged.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onAckHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["onAckHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(Acknowledgement, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the action to execute when messages are <strong>not</strong> acknowledged.

   @public
   @param handler {function} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onNackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["onNackHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(Acknowledgement, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
   frame containing <code>EOL</code> (specification). However, you can customize this and send another frame. However,
   be aware that this may requires a custom client.
   <p/>
   The handler will only be called if the connection supports heartbeats.

   @public
   @param handler {function} the action to execute when a `PING` needs to be sent. 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.pingHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_stompServerHandler["pingHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(StompServerConnection, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a {@link Destination} object if existing, or create a new one. The creation is delegated to the
   {@link DestinationFactory}.

   @public
   @param destination {string} the destination 
   @return {Destination} the {@link Destination} instance, may have been created.
   */
  this.getOrCreateDestination = function(destination) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(Destination, j_stompServerHandler["getOrCreateDestination(java.lang.String)"](destination));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the {@link DestinationFactory} used to create {@link Destination} objects.

   @public
   @param factory {DestinationFactory} the factory 
   @return {StompServerHandler} the current {@link StompServerHandler}.
   */
  this.destinationFactory = function(factory) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["destinationFactory(io.vertx.ext.stomp.DestinationFactory)"](factory._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Configures the STOMP server to act as a bridge with the Vert.x event bus.

   @public
   @param options {Object} the configuration options 
   @return {StompServerHandler} the current {@link StompServerHandler}.
   */
  this.bridge = function(options) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      j_stompServerHandler["bridge(io.vertx.ext.stomp.BridgeOptions)"](options != null ? new BridgeOptions(new JsonObject(JSON.stringify(options))) : null);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stompServerHandler;
};

StompServerHandler._jclass = utils.getJavaClass("io.vertx.ext.stomp.StompServerHandler");
StompServerHandler._jtype = {
  accept: function(obj) {
    return StompServerHandler._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(StompServerHandler.prototype, {});
    StompServerHandler.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
StompServerHandler._create = function(jdel) {
  var obj = Object.create(StompServerHandler.prototype, {});
  StompServerHandler.apply(obj, arguments);
  return obj;
}
/**
 Creates an instance of {@link StompServerHandler} using the default (compliant) implementation.

 @memberof module:vertx-stomp-js/stomp_server_handler
 @param vertx {Vertx} the vert.x instance to use 
 @return {StompServerHandler} the created {@link StompServerHandler}
 */
StompServerHandler.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(StompServerHandler, JStompServerHandler["create(io.vertx.core.Vertx)"](vertx._jdel));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = StompServerHandler;