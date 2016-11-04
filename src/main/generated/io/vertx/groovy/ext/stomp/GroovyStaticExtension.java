package io.vertx.groovy.ext.stomp;
public class GroovyStaticExtension {
  public static io.vertx.ext.stomp.Destination bridge(io.vertx.ext.stomp.Destination j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.stomp.Destination.bridge(vertx,
      options != null ? new io.vertx.ext.stomp.BridgeOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static java.util.Map<String, Object> createErrorFrame(io.vertx.ext.stomp.Frames j_receiver, java.lang.String message, java.util.Map<String, java.lang.String> headers, java.lang.String body) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.ext.stomp.Frames.createErrorFrame(message,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      body), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createReceiptFrame(io.vertx.ext.stomp.Frames j_receiver, java.lang.String receiptId, java.util.Map<String, java.lang.String> headers) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.ext.stomp.Frames.createReceiptFrame(receiptId,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static void handleReceipt(io.vertx.ext.stomp.Frames j_receiver, java.util.Map<String, Object> frame, io.vertx.ext.stomp.StompServerConnection connection) {
    io.vertx.ext.stomp.Frames.handleReceipt(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null,
      connection);
  }
  public static java.util.Map<String, Object> ping(io.vertx.ext.stomp.Frames j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.ext.stomp.Frames.ping(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static io.vertx.ext.stomp.StompClient create(io.vertx.ext.stomp.StompClient j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.stomp.StompClient.create(vertx,
      options != null ? new io.vertx.ext.stomp.StompClientOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.stomp.StompServer create(io.vertx.ext.stomp.StompServer j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.stomp.StompServer.create(vertx,
      options != null ? new io.vertx.ext.stomp.StompServerOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.stomp.StompServer create(io.vertx.ext.stomp.StompServer j_receiver, io.vertx.core.Vertx vertx, io.vertx.core.net.NetServer net, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.stomp.StompServer.create(vertx,
      net,
      options != null ? new io.vertx.ext.stomp.StompServerOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
}
