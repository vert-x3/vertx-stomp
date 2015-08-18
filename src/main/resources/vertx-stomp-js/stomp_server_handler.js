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
var ServerFrameHandler = require('vertx-stomp-js/server_frame_handler');
var AuthenticationHandler = require('vertx-stomp-js/authentication_handler');
var Transaction = require('vertx-stomp-js/transaction');
var Acknowledgement = require('vertx-stomp-js/acknowledgement');
var Subscription = require('vertx-stomp-js/subscription');
var Vertx = require('vertx-js/vertx');
var StompServer = require('vertx-stomp-js/stomp_server');
var StompServerConnection = require('vertx-stomp-js/stomp_server_connection');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStompServerHandler = io.vertx.ext.stomp.StompServerHandler;
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
  ServerFrameHandler.call(this, j_val);

  /**
   Configures the action to execute when a <code>CONNECT</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.connectHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["connectHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>STOMP</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.stompHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["stompHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.subscribeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["subscribeHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.unsubscribeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["unsubscribeHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>SEND</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.sendHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["sendHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
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
      handler(utils.convReturnVertxGen(jVal, StompServerConnection));
    });
      return that;
    } else utils.invalidArgs();
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
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>COMMIT</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.commitHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["commitHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>ABORT</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.abortHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["abortHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>BEGIN</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.beginHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["beginHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>DISCONNECT</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.disconnectHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["disconnectHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>ACK</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.ackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["ackHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a <code>NACK</code> frame is received.

   @public
   @param handler {ServerFrameHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.nackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["nackHandler(io.vertx.ext.stomp.ServerFrameHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Called when the client connects to a server requiring authentication. It should invokes the handler configured
   using {@link StompServerHandler#authenticationHandler}.

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
    } else utils.invalidArgs();
  };

  /**
   Configures the action to execute when a an authentication request is made.

   @public
   @param handler {AuthenticationHandler} the handler 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.authenticationHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["authenticationHandler(io.vertx.ext.stomp.AuthenticationHandler)"](handler._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
   opaque Strings.

   @public

   @return {Array.<string>}
   */
  this.getDestinations = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_stompServerHandler["getDestinations()"]();
    } else utils.invalidArgs();
  };

  /**
   Registers the given {@link Subscription}.

   @public
   @param subscription {Subscription} the subscription 
   @return {boolean} <code>true</code> if the subscription has been registered correctly, <code>false</code> otherwise. The main reason to fail the registration is the non-uniqueness of the subscription id for a given client.
   */
  this.subscribe = function(subscription) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_stompServerHandler["subscribe(io.vertx.ext.stomp.Subscription)"](subscription._jdel);
    } else utils.invalidArgs();
  };

  /**
   Unregisters the subscription 'id' from the given client.

   @public
   @param connection {StompServerConnection} the connection (client) 
   @param id {string} the subscription id 
   @return {boolean} <code>true</code> if the subscription removal succeed, <code>false</code> otherwise. The main reason to fail this removal is because the associated subscription cannot be found.
   */
  this.unsubscribe = function(connection, id) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      return j_stompServerHandler["unsubscribe(io.vertx.ext.stomp.StompServerConnection,java.lang.String)"](connection._jdel, id);
    } else utils.invalidArgs();
  };

  /**
   Unregisters all subscriptions from a given client / connection.

   @public
   @param connection {StompServerConnection} the connection (client) 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.unsubscribeConnection = function(connection) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["unsubscribeConnection(io.vertx.ext.stomp.StompServerConnection)"](connection._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Gets the current list of subscriptions for the given destination.

   @public
   @param destination {string} the destination 
   @return {Array.<Subscription>} the list of subscription
   */
  this.getSubscriptions = function(destination) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnListSetVertxGen(j_stompServerHandler["getSubscriptions(java.lang.String)"](destination), Subscription);
    } else utils.invalidArgs();
  };

  /**
   Registers a transaction.

   @public
   @param transaction {Transaction} the transaction 
   @return {boolean} <code>true</code> if the registration succeed, <code>false</code> otherwise. The main reason of failure is the non-uniqueness of the transaction id for a given client / connection
   */
  this.registerTransaction = function(transaction) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_stompServerHandler["registerTransaction(io.vertx.ext.stomp.Transaction)"](transaction._jdel);
    } else utils.invalidArgs();
  };

  /**
   Gets a transaction.

   @public
   @param connection {StompServerConnection} the connection used by the transaction 
   @param id {string} the id of the transaction 
   @return {Transaction} the transaction, <code>null</code> if not found
   */
  this.getTransaction = function(connection, id) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      return utils.convReturnVertxGen(j_stompServerHandler["getTransaction(io.vertx.ext.stomp.StompServerConnection,java.lang.String)"](connection._jdel, id), Transaction);
    } else utils.invalidArgs();
  };

  /**
   Unregisters a transaction

   @public
   @param transaction {Transaction} the transaction to unregister 
   @return {boolean} <code>true</code> if the transaction is unregistered correctly, <code>false</code> otherwise.
   */
  this.unregisterTransaction = function(transaction) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_stompServerHandler["unregisterTransaction(io.vertx.ext.stomp.Transaction)"](transaction._jdel);
    } else utils.invalidArgs();
  };

  /**
   Unregisters all transactions from the given connection / client.

   @public
   @param connection {StompServerConnection} the connection 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.unregisterTransactionsFromConnection = function(connection) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_stompServerHandler["unregisterTransactionsFromConnection(io.vertx.ext.stomp.StompServerConnection)"](connection._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Gets the list of current transactions.

   @public

   @return {Array.<Transaction>} the list of transactions, empty is none.
   */
  this.getTransactions = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_stompServerHandler["getTransactions()"](), Transaction);
    } else utils.invalidArgs();
  };

  /**
   Gets a subscription for the given connection / client and use the given acknowledgment id. Acknowledgement id
   is different from the subscription id as it point to a single message.

   @public
   @param connection {StompServerConnection} the connection 
   @param ackId {string} the ack id 
   @return {Subscription} the subscription, <code>null</code> if not found
   */
  this.getSubscription = function(connection, ackId) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      return utils.convReturnVertxGen(j_stompServerHandler["getSubscription(io.vertx.ext.stomp.StompServerConnection,java.lang.String)"](connection._jdel, ackId), Subscription);
    } else utils.invalidArgs();
  };

  /**
   Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
   Implementations must call the handler configured using {@link StompServerHandler#onAckHandler}.

   @public
   @param subscription {Subscription} the subscription 
   @param messages {Array.<Object>} the acknowledge messages 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onAck = function(subscription, messages) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1] instanceof Array) {
      j_stompServerHandler["onAck(io.vertx.ext.stomp.Subscription,java.util.List)"](subscription._jdel, utils.convParamListDataObject(messages, function(json) { return new Frame(json); }));
      return that;
    } else utils.invalidArgs();
  };

  /**
   Method called by single message (client-individual policy) or a set of message (client policy) are
   <storng>not</storng> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
   <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
   {@link StompServerHandler#onNackHandler}.

   @public
   @param subscription {Subscription} the subscription 
   @param messages {Array.<Object>} the acknowledge messages 
   @return {StompServerHandler} the current {@link StompServerHandler}
   */
  this.onNack = function(subscription, messages) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1] instanceof Array) {
      j_stompServerHandler["onNack(io.vertx.ext.stomp.Subscription,java.util.List)"](subscription._jdel, utils.convParamListDataObject(messages, function(json) { return new Frame(json); }));
      return that;
    } else utils.invalidArgs();
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
      handler(utils.convReturnVertxGen(jVal, Acknowledgement));
    });
      return that;
    } else utils.invalidArgs();
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
      handler(utils.convReturnVertxGen(jVal, Acknowledgement));
    });
      return that;
    } else utils.invalidArgs();
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
      handler(utils.convReturnVertxGen(jVal, StompServerConnection));
    });
      return that;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stompServerHandler;
};

/**
 Creates an instance of {@link StompServerHandler} using the default (compliant) implementation.

 @memberof module:vertx-stomp-js/stomp_server_handler
 @param vertx {Vertx} the vert.x instance to use 
 @return {StompServerHandler} the created {@link StompServerHandler}
 */
StompServerHandler.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JStompServerHandler["create(io.vertx.core.Vertx)"](vertx._jdel), StompServerHandler);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = StompServerHandler;