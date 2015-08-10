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

/** @module vertx-stomp-js/client_frame_handler */
var utils = require('vertx-js/util/utils');
var StompClientConnection = require('vertx-stomp-js/stomp_client_connection');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JClientFrameHandler = io.vertx.ext.stomp.ClientFrameHandler;
var Frame = io.vertx.ext.stomp.Frame;

/**
 Handles a client frame. This type of handler are called when the client receives a STOMP frame and let implement
 the behavior.

 @class
*/
var ClientFrameHandler = function(j_val) {

  var j_clientFrameHandler = j_val;
  var that = this;

  /**
   Handler called when a client frame has been received.

   @public
   @param frame {Object} the frame 
   @param connection {StompClientConnection} the client connection that has received the frame 
   */
  this.onFrame = function(frame, connection) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && typeof __args[1] === 'object' && __args[1]._jdel) {
      j_clientFrameHandler["onFrame(io.vertx.ext.stomp.Frame,io.vertx.ext.stomp.StompClientConnection)"](frame != null ? new Frame(new JsonObject(JSON.stringify(frame))) : null, connection._jdel);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_clientFrameHandler;
};

// We export the Constructor function
module.exports = ClientFrameHandler;