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

/** @module vertx-stomp-js/transaction */
var utils = require('vertx-js/util/utils');
var StompServerConnection = require('vertx-stomp-js/stomp_server_connection');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTransaction = io.vertx.ext.stomp.Transaction;
var Frame = io.vertx.ext.stomp.Frame;

/**
 Represents a transaction in the STOMP server.

 @class
*/
var Transaction = function(j_val) {

  var j_transaction = j_val;
  var that = this;

  /**
   @return the connection

   @public

   @return {StompServerConnection}
   */
  this.connection = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_transaction["connection()"](), StompServerConnection);
    } else utils.invalidArgs();
  };

  /**
   @return the transaction id

   @public

   @return {string}
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_transaction["id()"]();
    } else utils.invalidArgs();
  };

  /**
   Adds a frame to the transaction. By default, only <code>SEND, ACK and NACK</code> frames can be in transactions.

   @public
   @param frame {Object} the frame to add 
   @return {boolean} <code>true</code> if the frame was added, <code>false</code> otherwise. Main failure reason is the number of frames stored in the transaction that have exceed the number of allowed frames in transaction.
   */
  this.addFrameToTransaction = function(frame) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object') {
      return j_transaction["addFrameToTransaction(io.vertx.ext.stomp.Frame)"](frame != null ? new Frame(new JsonObject(JSON.stringify(frame))) : null);
    } else utils.invalidArgs();
  };

  /**
   Clears the list of frames added to the transaction.

   @public

   @return {Transaction} the current {@link Transaction}
   */
  this.clear = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_transaction["clear()"]();
      return that;
    } else utils.invalidArgs();
  };

  /**
   @return the ordered list of frames added to the transaction.

   @public

   @return {Array.<Object>}
   */
  this.getFrames = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetDataObject(j_transaction["getFrames()"]());
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_transaction;
};

/**
 Creates an instance of {@link Transaction} using the default implementation.

 @memberof module:vertx-stomp-js/transaction
 @param connection {StompServerConnection} the connection (client) 
 @param id {string} the transaction id 
 @return {Transaction} the created {@link Transaction}
 */
Transaction.create = function(connection, id) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(JTransaction["create(io.vertx.ext.stomp.StompServerConnection,java.lang.String)"](connection._jdel, id), Transaction);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Transaction;