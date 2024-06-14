package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.stomp.StompServerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.StompServerOptions} original class using Vert.x codegen.
 */
public class StompServerOptionsConverter {

  private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
  private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, StompServerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "maxBodyLength":
          if (member.getValue() instanceof Number) {
            obj.setMaxBodyLength(((Number)member.getValue()).intValue());
          }
          break;
        case "maxHeaderLength":
          if (member.getValue() instanceof Number) {
            obj.setMaxHeaderLength(((Number)member.getValue()).intValue());
          }
          break;
        case "maxHeaders":
          if (member.getValue() instanceof Number) {
            obj.setMaxHeaders(((Number)member.getValue()).intValue());
          }
          break;
        case "supportedVersions":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setSupportedVersions(list);
          }
          break;
        case "secured":
          if (member.getValue() instanceof Boolean) {
            obj.setSecured((Boolean)member.getValue());
          }
          break;
        case "sendErrorOnNoSubscriptions":
          if (member.getValue() instanceof Boolean) {
            obj.setSendErrorOnNoSubscriptions((Boolean)member.getValue());
          }
          break;
        case "timeFactor":
          if (member.getValue() instanceof Number) {
            obj.setTimeFactor(((Number)member.getValue()).intValue());
          }
          break;
        case "heartbeat":
          if (member.getValue() instanceof JsonObject) {
            obj.setHeartbeat(((JsonObject)member.getValue()).copy());
          }
          break;
        case "maxFrameInTransaction":
          if (member.getValue() instanceof Number) {
            obj.setMaxFrameInTransaction(((Number)member.getValue()).intValue());
          }
          break;
        case "transactionChunkSize":
          if (member.getValue() instanceof Number) {
            obj.setTransactionChunkSize(((Number)member.getValue()).intValue());
          }
          break;
        case "maxSubscriptionsByClient":
          if (member.getValue() instanceof Number) {
            obj.setMaxSubscriptionsByClient(((Number)member.getValue()).intValue());
          }
          break;
        case "websocketBridge":
          if (member.getValue() instanceof Boolean) {
            obj.setWebsocketBridge((Boolean)member.getValue());
          }
          break;
        case "websocketPath":
          if (member.getValue() instanceof String) {
            obj.setWebsocketPath((String)member.getValue());
          }
          break;
        case "trailingLine":
          if (member.getValue() instanceof Boolean) {
            obj.setTrailingLine((Boolean)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(StompServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(StompServerOptions obj, java.util.Map<String, Object> json) {
    json.put("maxBodyLength", obj.getMaxBodyLength());
    json.put("maxHeaderLength", obj.getMaxHeaderLength());
    json.put("maxHeaders", obj.getMaxHeaders());
    if (obj.getSupportedVersions() != null) {
      JsonArray array = new JsonArray();
      obj.getSupportedVersions().forEach(item -> array.add(item));
      json.put("supportedVersions", array);
    }
    json.put("secured", obj.isSecured());
    json.put("sendErrorOnNoSubscriptions", obj.isSendErrorOnNoSubscriptions());
    json.put("timeFactor", obj.getTimeFactor());
    if (obj.getHeartbeat() != null) {
      json.put("heartbeat", obj.getHeartbeat());
    }
    json.put("maxFrameInTransaction", obj.getMaxFrameInTransaction());
    json.put("transactionChunkSize", obj.getTransactionChunkSize());
    json.put("maxSubscriptionsByClient", obj.getMaxSubscriptionsByClient());
    json.put("websocketBridge", obj.isWebsocketBridge());
    if (obj.getWebsocketPath() != null) {
      json.put("websocketPath", obj.getWebsocketPath());
    }
    json.put("trailingLine", obj.isTrailingLine());
  }
}
