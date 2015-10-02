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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Specify the event bus bridge options.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = false)
public class BridgeOptions {

  public static final boolean DEFAULT_POINT_TO_POINT = false;

  private List<PermittedOptions> inboundPermitted = new ArrayList<>();
  private List<PermittedOptions> outboundPermitted = new ArrayList<>();

  private boolean pointToPoint;


  public BridgeOptions() {
    pointToPoint = DEFAULT_POINT_TO_POINT;
  }

  public BridgeOptions(BridgeOptions that) {
    this.inboundPermitted = that.inboundPermitted;
    this.outboundPermitted = that.outboundPermitted;
    this.pointToPoint = that.pointToPoint;
  }

  public BridgeOptions(JsonObject json) {
    JsonArray inbound = json.getJsonArray("inboundPermitteds");
    JsonArray outbound = json.getJsonArray("outboundPermitteds");
    if (inbound != null) {
      for (Object object : inbound) {
        if (!(object instanceof JsonObject)) {
          throw new IllegalArgumentException("Invalid type " + object.getClass() + " in inboundPermitteds array");
        }
        this.inboundPermitted.add(new PermittedOptions((JsonObject) object));
      }
    }

    if (outbound != null) {
      for (Object object : outbound) {
        if (!(object instanceof JsonObject)) {
          throw new IllegalArgumentException("Invalid type " + object.getClass() + " in outboundPermitteds array");
        }
        this.outboundPermitted.add(new PermittedOptions((JsonObject) object));
      }
    }


    this.pointToPoint = json.getBoolean("pointToPoint", DEFAULT_POINT_TO_POINT);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    JsonArray in = new JsonArray();
    JsonArray out = new JsonArray();
    for (PermittedOptions p : inboundPermitted) {
      in.add(p.toJson());
    }
    for (PermittedOptions p : outboundPermitted) {
      out.add(p.toJson());
    }
    json.put("inboundPermitteds", in);
    json.put("outboundPermitteds", out);
    json.put("pointToPoint", pointToPoint);
    return json;
  }

  public BridgeOptions addInboundPermitted(PermittedOptions permitted) {
    inboundPermitted.add(permitted);
    return this;
  }

  public List<PermittedOptions> getInboundPermitteds() {
    return inboundPermitted;
  }

  public void setInboundPermitted(List<PermittedOptions> inboundPermitted) {
    this.inboundPermitted = inboundPermitted;
  }

  public BridgeOptions addOutboundPermitted(PermittedOptions permitted) {
    outboundPermitted.add(permitted);
    return this;
  }

  public List<PermittedOptions> getOutboundPermitteds() {
    return outboundPermitted;
  }

  public void setOutboundPermitted(List<PermittedOptions> outboundPermitted) {
    this.outboundPermitted = outboundPermitted;
  }

  public BridgeOptions setPointToPoint(boolean v) {
    this.pointToPoint = v;
    return this;
  }

  public boolean isPointToPoint() {
    return pointToPoint;
  }
}
