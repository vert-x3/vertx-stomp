/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.*;

import java.util.List;

/**
 * Specify the event bus bridge options.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject
public class BridgeOptions extends io.vertx.ext.bridge.BridgeOptions {

  public static final boolean DEFAULT_POINT_TO_POINT = false;

  private boolean pointToPoint;


  public BridgeOptions() {
    super();
    pointToPoint = DEFAULT_POINT_TO_POINT;
  }

  public BridgeOptions(BridgeOptions that) {
    super(that);
    this.pointToPoint = that.pointToPoint;
  }

  public BridgeOptions(JsonObject json) {
    super(json);
    this.pointToPoint=json.getBoolean("pointToPoint",DEFAULT_POINT_TO_POINT);
  }

  public JsonObject toJson() {
    JsonObject json = super.toJson();
    json.put("pointToPoint", pointToPoint);
    return json;
  }

  public BridgeOptions setPointToPoint(boolean v) {
    this.pointToPoint = v;
    return this;
  }

  public boolean isPointToPoint() {
    return pointToPoint;
  }

  @Override
  public BridgeOptions setInboundPermitteds(List<PermittedOptions> inboundPermitted) {
    super.setInboundPermitteds(inboundPermitted);
    return this;
  }

  @Override
  public BridgeOptions setOutboundPermitteds(List<PermittedOptions> outboundPermitted) {
    super.setOutboundPermitteds(outboundPermitted);
    return this;
  }

  @Override
  public BridgeOptions addInboundPermitted(PermittedOptions permitted) {
    super.addInboundPermitted(permitted);
    return this;
  }

  @Override
  public BridgeOptions addOutboundPermitted(PermittedOptions permitted) {
    super.addOutboundPermitted(permitted);
    return this;
  }
}
