/*
 *  Copyright (c) 2011-2015 The original author or authors
 *
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


/**
 * Specify a match to allow for inbound and outbound traffic.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 * @deprecated use {@link io.vertx.ext.bridge.PermittedOptions} instead.
 */
@DataObject
@Deprecated
public class PermittedOptions extends io.vertx.ext.bridge.PermittedOptions {


  public PermittedOptions() {
  }

  public PermittedOptions(PermittedOptions that) {
    super(that);
  }

  public PermittedOptions(JsonObject json) {
    super(json);
  }


  public JsonObject toJson() {
    return super.toJson();
  }

}
