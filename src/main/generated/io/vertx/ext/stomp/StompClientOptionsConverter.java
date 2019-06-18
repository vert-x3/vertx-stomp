package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.ext.stomp.StompClientOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompClientOptions} original class using Vert.x codegen.
 */
public class StompClientOptionsConverter implements JsonCodec<StompClientOptions, JsonObject> {

  public static final StompClientOptionsConverter INSTANCE = new StompClientOptionsConverter();

  @Override public JsonObject encode(StompClientOptions value) { return (value != null) ? value.toJson() : null; }

  @Override public StompClientOptions decode(JsonObject value) { return (value != null) ? new StompClientOptions(value) : null; }

  @Override public Class<StompClientOptions> getTargetClass() { return StompClientOptions.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, StompClientOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "acceptedVersions":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setAcceptedVersions(list);
          }
          break;
        case "autoComputeContentLength":
          if (member.getValue() instanceof Boolean) {
            obj.setAutoComputeContentLength((Boolean)member.getValue());
          }
          break;
        case "bypassHostHeader":
          if (member.getValue() instanceof Boolean) {
            obj.setBypassHostHeader((Boolean)member.getValue());
          }
          break;
        case "heartbeat":
          if (member.getValue() instanceof JsonObject) {
            obj.setHeartbeat(((JsonObject)member.getValue()).copy());
          }
          break;
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "login":
          if (member.getValue() instanceof String) {
            obj.setLogin((String)member.getValue());
          }
          break;
        case "passcode":
          if (member.getValue() instanceof String) {
            obj.setPasscode((String)member.getValue());
          }
          break;
        case "port":
          if (member.getValue() instanceof Number) {
            obj.setPort(((Number)member.getValue()).intValue());
          }
          break;
        case "trailingLine":
          if (member.getValue() instanceof Boolean) {
            obj.setTrailingLine((Boolean)member.getValue());
          }
          break;
        case "useStompFrame":
          if (member.getValue() instanceof Boolean) {
            obj.setUseStompFrame((Boolean)member.getValue());
          }
          break;
        case "virtualHost":
          if (member.getValue() instanceof String) {
            obj.setVirtualHost((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(StompClientOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(StompClientOptions obj, java.util.Map<String, Object> json) {
    if (obj.getAcceptedVersions() != null) {
      JsonArray array = new JsonArray();
      obj.getAcceptedVersions().forEach(item -> array.add(item));
      json.put("acceptedVersions", array);
    }
    json.put("autoComputeContentLength", obj.isAutoComputeContentLength());
    json.put("bypassHostHeader", obj.isBypassHostHeader());
    if (obj.getHeartbeat() != null) {
      json.put("heartbeat", obj.getHeartbeat());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getLogin() != null) {
      json.put("login", obj.getLogin());
    }
    if (obj.getPasscode() != null) {
      json.put("passcode", obj.getPasscode());
    }
    json.put("port", obj.getPort());
    json.put("trailingLine", obj.isTrailingLine());
    json.put("useStompFrame", obj.isUseStompFrame());
    if (obj.getVirtualHost() != null) {
      json.put("virtualHost", obj.getVirtualHost());
    }
  }
}
