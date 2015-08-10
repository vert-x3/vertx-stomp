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

/** @module vertx-stomp-js/subscription */
var utils = require('vertx-js/util/utils');
var StompServerConnection = require('vertx-stomp-js/stomp_server_connection');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSubscription = io.vertx.ext.stomp.Subscription;
var Frame = io.vertx.ext.stomp.Frame;

/**
 Represents a subscription in the STOMP server.

 @class
*/
var Subscription = function(j_val) {

  var j_subscription = j_val;
  var that = this;

  /**
   @return the connection.

   @public

   @return {StompServerConnection}
   */
  this.connection = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_subscription["connection()"](), StompServerConnection);
    } else utils.invalidArgs();
  };

  /**
   @return the acknowledgment policy among <code>auto</code> (default), <code>client</code> (cumulative acknowledgment) and
   <code>client-individual</code>.

   @public

   @return {string}
   */
  this.ackMode = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_subscription["ackMode()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the subscription id.

   @public

   @return {string}
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_subscription["id()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the destination.

   @public

   @return {string}
   */
  this.destination = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_subscription["destination()"]();
    } else utils.invalidArgs();
  };

  /**
   Acknowledges the message with the given id.

   @public
   @param messageId {string} the message id 
   @return {boolean} <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  this.ack = function(messageId) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_subscription["ack(java.lang.String)"](messageId);
    } else utils.invalidArgs();
  };

  /**
   Not-acknowledges the message with the given id.

   @public
   @param messageId {string} the message id 
   @return {boolean} <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
   */
  this.nack = function(messageId) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_subscription["nack(java.lang.String)"](messageId);
    } else utils.invalidArgs();
  };

  /**
   Adds a message (identified by its id) to the list of message waiting for acknowledgment.

   @public
   @param messageId {Object} the message id 
   */
  this.enqueue = function(messageId) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object') {
      j_subscription["enqueue(io.vertx.ext.stomp.Frame)"](messageId != null ? new Frame(new JsonObject(JSON.stringify(messageId))) : null);
    } else utils.invalidArgs();
  };

  /**
   Checks whether or not the message identified by the given message id is waiting for an acknowledgment.

   @public
   @param messageId {string} the message id 
   @return {boolean} <code>true</code> if the message requires an acknowledgment, <code>false</code> otherwise.
   */
  this.contains = function(messageId) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_subscription["contains(java.lang.String)"](messageId);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_subscription;
};

/**
 Creates an instance of {@link Subscription} using the default implementation.

 @memberof module:vertx-stomp-js/subscription
 @param connection {StompServerConnection} the connection (client) 
 @param destination {string} the destination 
 @param ack {string} the acknowledgment policy, <code>auto</code> by default. 
 @param id {string} the subscription id 
 @return {Subscription} the created {@link Subscription}
 */
Subscription.create = function(connection, destination, ack, id) {
  var __args = arguments;
  if (__args.length === 4 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string' && typeof __args[2] === 'string' && typeof __args[3] === 'string') {
    return utils.convReturnVertxGen(JSubscription["create(io.vertx.ext.stomp.StompServerConnection,java.lang.String,java.lang.String,java.lang.String)"](connection._jdel, destination, ack, id), Subscription);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Subscription;