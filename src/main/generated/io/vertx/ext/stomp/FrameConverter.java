package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class FrameConverter {

  public static void fromJson(JsonObject json, Frame obj) {
    if (json.getValue("body") instanceof String) {
      obj.setBody(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)json.getValue("body"))));
    }
    if (json.getValue("command") instanceof String) {
      obj.setCommand(io.vertx.ext.stomp.Frame.Command.valueOf((String)json.getValue("command")));
    }
    if (json.getValue("destination") instanceof String) {
      obj.setDestination((String)json.getValue("destination"));
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("transaction") instanceof String) {
      obj.setTransaction((String)json.getValue("transaction"));
    }
  }

  public static void toJson(Frame obj, JsonObject json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody().getBytes());
    }
    if (obj.getCommand() != null) {
      json.put("command", obj.getCommand().name());
    }
    if (obj.getDestination() != null) {
      json.put("destination", obj.getDestination());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getTransaction() != null) {
      json.put("transaction", obj.getTransaction());
    }
  }
}