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
}
