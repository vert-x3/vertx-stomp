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
@DataObject
public class EventBusBridgeOptions {

  private List<PermittedOptions> inbound = new ArrayList<>();
  private List<PermittedOptions> outbound = new ArrayList<>();

  private boolean pointToPoint = false;


  public EventBusBridgeOptions() {
  }

  public EventBusBridgeOptions(EventBusBridgeOptions that) {
    this.inbound = that.inbound;
    this.outbound = that.outbound;
    this.pointToPoint = that.pointToPoint;
  }

  public EventBusBridgeOptions(JsonObject json) {
    JsonArray inbound = json.getJsonArray("inbound");
    JsonArray outbound = json.getJsonArray("outbound");
    for (Object object : inbound) {
      if (!(object instanceof JsonObject)) {
        throw new IllegalArgumentException("Malformed permitted options");
      }
      this.inbound.add(new PermittedOptions((JsonObject) object));
    }

    for (Object object : outbound) {
      if (!(object instanceof JsonObject)) {
        throw new IllegalArgumentException("Malformed permitted options");
      }
      this.outbound.add(new PermittedOptions((JsonObject) object));
    }
    this.pointToPoint = json.getBoolean("pointToPoint", false);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    JsonArray in = new JsonArray();
    JsonArray out = new JsonArray();
    for (PermittedOptions p : inbound) {
      in.add(p.toJson());
    }
    for (PermittedOptions p : outbound) {
      out.add(p.toJson());
    }
    json.put("inbound", in);
    json.put("outbound", out);
    json.put("pointToPoint", pointToPoint);
    return json;
  }

  public List<PermittedOptions> getInbound() {
    return inbound;
  }

  public EventBusBridgeOptions setInbound(List<PermittedOptions> inbound) {
    this.inbound = inbound;
    return this;
  }

  public List<PermittedOptions> getOutbound() {
    return outbound;
  }

  public EventBusBridgeOptions setOutbound(List<PermittedOptions> outbound) {
    this.outbound = outbound;
    return this;
  }

  public EventBusBridgeOptions addInbound(PermittedOptions inbound) {
    this.inbound.add(inbound);
    return this;
  }

  public EventBusBridgeOptions addOutbound(PermittedOptions outbound) {
    this.outbound.add(outbound);
    return this;
  }

  public boolean isPointToPoint() {
    return pointToPoint;
  }

  public EventBusBridgeOptions setPointToPoint(boolean pointToPoint) {
    this.pointToPoint = pointToPoint;
    return this;
  }
}
