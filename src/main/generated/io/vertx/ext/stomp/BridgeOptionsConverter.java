package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.ext.stomp.BridgeOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.stomp.BridgeOptions} original class using Vert.x codegen.
 */
public class BridgeOptionsConverter implements JsonCodec<BridgeOptions, JsonObject> {

  public static final BridgeOptionsConverter INSTANCE = new BridgeOptionsConverter();

  @Override public JsonObject encode(BridgeOptions value) { return (value != null) ? value.toJson() : null; }

  @Override public BridgeOptions decode(JsonObject value) { return (value != null) ? new BridgeOptions(value) : null; }

  @Override public Class<BridgeOptions> getTargetClass() { return BridgeOptions.class; }
}
