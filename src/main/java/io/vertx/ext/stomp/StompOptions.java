package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * Defines a couples of constants shared by client and server options.
 */
public interface StompOptions {

  int DEFAULT_STOMP_PORT = 61613;
  String DEFAULT_STOMP_HOST = "0.0.0.0";
  List<String> SUPPORTED_VERSIONS = Arrays.asList("1.2", "1.1", "1.0");

  JsonObject DEFAULT_STOMP_HEARTBEAT = new JsonObject().put("x", 1000).put("y", 1000);
}
