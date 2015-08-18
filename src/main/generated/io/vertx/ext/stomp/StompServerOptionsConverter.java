package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class StompServerOptionsConverter {

  public static void fromJson(JsonObject json, StompServerOptions obj) {
    if (json.getValue("ackTimeout") instanceof Number) {
      obj.setAckTimeout(((Number)json.getValue("ackTimeout")).longValue());
    }
    if (json.getValue("heartbeat") instanceof JsonObject) {
      obj.setHeartbeat(((JsonObject)json.getValue("heartbeat")).copy());
    }
    if (json.getValue("maxBodyLength") instanceof Number) {
      obj.setMaxBodyLength(((Number)json.getValue("maxBodyLength")).intValue());
    }
    if (json.getValue("maxFrameInTransaction") instanceof Number) {
      obj.setMaxFrameInTransaction(((Number)json.getValue("maxFrameInTransaction")).intValue());
    }
    if (json.getValue("maxHeaderLength") instanceof Number) {
      obj.setMaxHeaderLength(((Number)json.getValue("maxHeaderLength")).intValue());
    }
    if (json.getValue("maxHeaders") instanceof Number) {
      obj.setMaxHeaders(((Number)json.getValue("maxHeaders")).intValue());
    }
    if (json.getValue("secured") instanceof Boolean) {
      obj.setSecured((Boolean)json.getValue("secured"));
    }
    if (json.getValue("sendErrorOnNoSubscriptions") instanceof Boolean) {
      obj.setSendErrorOnNoSubscriptions((Boolean)json.getValue("sendErrorOnNoSubscriptions"));
    }
    if (json.getValue("supportedVersions") instanceof JsonArray) {
      java.util.List<java.lang.String> list = new java.util.ArrayList<>();
      json.getJsonArray("supportedVersions").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setSupportedVersions(list);
    }
    if (json.getValue("timeFactor") instanceof Number) {
      obj.setTimeFactor(((Number)json.getValue("timeFactor")).intValue());
    }
  }

  public static void toJson(StompServerOptions obj, JsonObject json) {
    json.put("ackTimeout", obj.getAckTimeout());
    if (obj.getHeartbeat() != null) {
      json.put("heartbeat", obj.getHeartbeat());
    }
    json.put("maxBodyLength", obj.getMaxBodyLength());
    json.put("maxFrameInTransaction", obj.getMaxFrameInTransaction());
    json.put("maxHeaderLength", obj.getMaxHeaderLength());
    json.put("maxHeaders", obj.getMaxHeaders());
    json.put("secured", obj.isSecured());
    json.put("sendErrorOnNoSubscriptions", obj.isSendErrorOnNoSubscriptions());
    if (obj.getSupportedVersions() != null) {
      json.put("supportedVersions", new JsonArray(
          obj.getSupportedVersions().
              stream().
              map(item -> item).
              collect(java.util.stream.Collectors.toList())));
    }
    json.put("timeFactor", obj.getTimeFactor());
  }
}