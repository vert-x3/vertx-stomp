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

/** @module vertx-stomp-js/destination_factory */
var utils = require('vertx-js/util/utils');
var Destination = require('vertx-stomp-js/destination');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDestinationFactory = io.vertx.ext.stomp.DestinationFactory;

/**
 Interface implemented to customize the destination creation.

 @class
*/
var DestinationFactory = function(j_val) {

  var j_destinationFactory = j_val;
  var that = this;

  /**
   Creates a destination for the given <em>address</em>.

   @public
   @param vertx {Vertx} the vert.x instance used by the STOMP server. 
   @param name {string} the destination name. 
   @return {Destination} the destination, <code>null</code> to reject the creation.
   */
  this.create = function(vertx, name) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      return utils.convReturnVertxGen(Destination, j_destinationFactory["create(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, name));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_destinationFactory;
};

DestinationFactory._jclass = utils.getJavaClass("io.vertx.ext.stomp.DestinationFactory");
DestinationFactory._jtype = {
  accept: function(obj) {
    return DestinationFactory._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(DestinationFactory.prototype, {});
    DestinationFactory.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
DestinationFactory._create = function(jdel) {
  var obj = Object.create(DestinationFactory.prototype, {});
  DestinationFactory.apply(obj, arguments);
  return obj;
}
module.exports = DestinationFactory;