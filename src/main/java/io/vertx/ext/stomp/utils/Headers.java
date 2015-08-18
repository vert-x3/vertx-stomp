package io.vertx.ext.stomp.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation  of {@link HashMap} to store STOMP frame headers. This implementations offer fluent methods to
 * ease the construction of the headers.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Headers extends HashMap<String, String> {

  public static Headers create() {
    return new Headers();
  }

  public static Headers create(String... kv) {
    Headers headers = create();
    if (kv.length % 2 != 0) {
      throw new IllegalArgumentException("Wrong number of parameters: " + Arrays.toString(kv));
    }
    for (int i = 0; i < kv.length; i = i + 2) {
      headers.add(kv[i], kv[i + 1]);
    }
    return headers;
  }

  public Headers add(String header, String value) {
    this.put(header, value);
    return this;
  }

  public Headers addAll(Map<String, String> other) {
    this.putAll(other);
    return this;
  }

  public static Headers create(Map<String, String> headers) {
    return create().addAll(headers);
  }
}
