package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.stomp.Frame}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.Frame} original class using Vert.x codegen.
 */
public class FrameConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Frame obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "ack":
          break;
        case "body":
          if (member.getValue() instanceof String) {
            obj.setBody(io.vertx.core.buffer.Buffer.buffer(BASE64_DECODER.decode((String)member.getValue())));
          }
          break;
        case "bodyAsString":
          break;
        case "command":
          if (member.getValue() instanceof String) {
            obj.setCommand(io.vertx.ext.stomp.Command.valueOf((String)member.getValue()));
          }
          break;
        case "destination":
          if (member.getValue() instanceof String) {
            obj.setDestination((String)member.getValue());
          }
          break;
        case "headers":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                obj.addHeader(entry.getKey(), (String)entry.getValue());
            });
          }
          break;
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "receipt":
          break;
        case "transaction":
          if (member.getValue() instanceof String) {
            obj.setTransaction((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(Frame obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Frame obj, java.util.Map<String, Object> json) {
    if (obj.getAck() != null) {
      json.put("ack", obj.getAck());
    }
    if (obj.getBody() != null) {
      json.put("body", BASE64_ENCODER.encodeToString(obj.getBody().getBytes()));
    }
    if (obj.getBodyAsString() != null) {
      json.put("bodyAsString", obj.getBodyAsString());
    }
    if (obj.getCommand() != null) {
      json.put("command", obj.getCommand().name());
    }
    if (obj.getDestination() != null) {
      json.put("destination", obj.getDestination());
    }
    if (obj.getHeaders() != null) {
      JsonObject map = new JsonObject();
      obj.getHeaders().forEach((key, value) -> map.put(key, value));
      json.put("headers", map);
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getReceipt() != null) {
      json.put("receipt", obj.getReceipt());
    }
    if (obj.getTransaction() != null) {
      json.put("transaction", obj.getTransaction());
    }
  }
}
