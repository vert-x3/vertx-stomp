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

import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * Defines a couples of constants shared by client and server options.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface StompOptions {

  /**
   * UTF-8 encoding name.
   */
  String UTF_8 = "utf-8";

  int DEFAULT_STOMP_PORT = 61613;
  String DEFAULT_STOMP_HOST = "0.0.0.0";
  List<String> DEFAULT_SUPPORTED_VERSIONS = Arrays.asList("1.2", "1.1", "1.0");

  JsonObject DEFAULT_STOMP_HEARTBEAT = new JsonObject().put("x", 1000).put("y", 1000);

  boolean DEFAULT_TRAILING_LINE = false;
}
