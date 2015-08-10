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

/** @module vertx-stomp-js/acknowledgment_handler */
var utils = require('vertx-js/util/utils');
var Subscription = require('vertx-stomp-js/subscription');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAcknowledgmentHandler = io.vertx.ext.stomp.AcknowledgmentHandler;
var Frame = io.vertx.ext.stomp.Frame;

/**

 @class
*/
var AcknowledgmentHandler = function(j_val) {

  var j_acknowledgmentHandler = j_val;
  var that = this;

  /**
   Called when a <code>ACK / NACK</code> frame is received.

   @public
   @param subscription {Subscription} the subscription 
   @param frames {Array.<Object>} the impacted frames. If the subscription uses the <code>client</code> mode, it contains all impacted messages (cumulative acknowledgment). In <code>client-individual</code> mode, the list contains only the acknowledged frame. 
   */
  this.handle = function(subscription, frames) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1] instanceof Array) {
      j_acknowledgmentHandler["handle(io.vertx.ext.stomp.Subscription,java.util.List)"](subscription._jdel, utils.convParamListDataObject(frames, function(json) { return new Frame(json); }));
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_acknowledgmentHandler;
};

// We export the Constructor function
module.exports = AcknowledgmentHandler;