package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class StompClientOptionsConverter {

  public static void fromJson(JsonObject json, StompClientOptions obj) {
    if (json.getValue("acceptedVersions") instanceof JsonArray) {
      java.util.List<java.lang.String> list = new java.util.ArrayList<>();
      json.getJsonArray("acceptedVersions").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setAcceptedVersions(list);
    }
    if (json.getValue("autoComputeContentLength") instanceof Boolean) {
      obj.setAutoComputeContentLength((Boolean)json.getValue("autoComputeContentLength"));
    }
    if (json.getValue("bypassHostHeader") instanceof Boolean) {
      obj.setBypassHostHeader((Boolean)json.getValue("bypassHostHeader"));
    }
    if (json.getValue("heartbeat") instanceof JsonObject) {
      obj.setHeartbeat(((JsonObject)json.getValue("heartbeat")).copy());
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("login") instanceof String) {
      obj.setLogin((String)json.getValue("login"));
    }
    if (json.getValue("passcode") instanceof String) {
      obj.setPasscode((String)json.getValue("passcode"));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("useStompFrame") instanceof Boolean) {
      obj.setUseStompFrame((Boolean)json.getValue("useStompFrame"));
    }
  }

  public static void toJson(StompClientOptions obj, JsonObject json) {
    if (obj.getAcceptedVersions() != null) {
      json.put("acceptedVersions", new JsonArray(
          obj.getAcceptedVersions().
              stream().
              map(item -> item).
              collect(java.util.stream.Collectors.toList())));
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
    json.put("useStompFrame", obj.isUseStompFrame());
  }
}