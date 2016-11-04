package io.vertx.groovy.ext.stomp;
public class GroovyExtension {
  public static java.util.Map<String, Object> subscription(io.vertx.ext.stomp.Acknowledgement j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.subscription(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.List<java.util.Map<String, Object>> frames(io.vertx.ext.stomp.Acknowledgement j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.frames(), list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()));
  }
  public static io.vertx.ext.stomp.Destination dispatch(io.vertx.ext.stomp.Destination j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> frame) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.dispatch(connection,
      frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.Destination subscribe(io.vertx.ext.stomp.Destination j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> frame) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.subscribe(connection,
      frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null));
    return j_receiver;
  }
  public static boolean unsubscribe(io.vertx.ext.stomp.Destination j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> frame) {
    return j_receiver.unsubscribe(connection,
      frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null);
  }
  public static boolean ack(io.vertx.ext.stomp.Destination j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> frame) {
    return j_receiver.ack(connection,
      frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null);
  }
  public static boolean nack(io.vertx.ext.stomp.Destination j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> frame) {
    return j_receiver.nack(connection,
      frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null);
  }
  public static java.util.Map<String, Object> frame(io.vertx.ext.stomp.ServerFrame j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.frame(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static io.vertx.ext.stomp.StompClient receivedFrameHandler(io.vertx.ext.stomp.StompClient j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.receivedFrameHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClient writingFrameHandler(io.vertx.ext.stomp.StompClient j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.writingFrameHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClient errorFrameHandler(io.vertx.ext.stomp.StompClient j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.errorFrameHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static java.util.Map<String, Object> options(io.vertx.ext.stomp.StompClient j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.options(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static io.vertx.ext.stomp.StompClientConnection send(io.vertx.ext.stomp.StompClientConnection j_receiver, java.util.Map<String, java.lang.String> headers, io.vertx.core.buffer.Buffer body, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.send(headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      body,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection send(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, io.vertx.core.buffer.Buffer body, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.send(destination,
      body,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection send(io.vertx.ext.stomp.StompClientConnection j_receiver, java.util.Map<String, Object> frame) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.send(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection send(io.vertx.ext.stomp.StompClientConnection j_receiver, java.util.Map<String, Object> frame, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.send(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection send(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, java.util.Map<String, java.lang.String> headers, io.vertx.core.buffer.Buffer body, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.send(destination,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      body,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static java.lang.String subscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    return j_receiver.subscribe(destination,
      handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null);
  }
  public static java.lang.String subscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, io.vertx.core.Handler<java.util.Map<String, Object>> handler, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    return j_receiver.subscribe(destination,
      handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null);
  }
  public static java.lang.String subscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    return j_receiver.subscribe(destination,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null);
  }
  public static java.lang.String subscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> handler, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    return j_receiver.subscribe(destination,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null);
  }
  public static io.vertx.ext.stomp.StompClientConnection unsubscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.unsubscribe(destination,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection unsubscribe(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String destination, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.unsubscribe(destination,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection errorHandler(io.vertx.ext.stomp.StompClientConnection j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.errorHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection beginTX(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.beginTX(id,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection beginTX(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.beginTX(id,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection commit(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.commit(id,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection commit(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.commit(id,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection abort(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.abort(id,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection abort(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, java.util.Map<String, java.lang.String> headers, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.abort(id,
      headers != null ? headers.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> entry.getValue())) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection disconnect(io.vertx.ext.stomp.StompClientConnection j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.disconnect(receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection disconnect(io.vertx.ext.stomp.StompClientConnection j_receiver, java.util.Map<String, Object> frame) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.disconnect(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection disconnect(io.vertx.ext.stomp.StompClientConnection j_receiver, java.util.Map<String, Object> frame, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.disconnect(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection ack(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.ack(id,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection nack(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.nack(id,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection ack(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, java.lang.String txId, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.ack(id,
      txId,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection nack(io.vertx.ext.stomp.StompClientConnection j_receiver, java.lang.String id, java.lang.String txId, io.vertx.core.Handler<java.util.Map<String, Object>> receiptHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.nack(id,
      txId,
      receiptHandler != null ? event -> receiptHandler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection receivedFrameHandler(io.vertx.ext.stomp.StompClientConnection j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.receivedFrameHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompClientConnection writingFrameHandler(io.vertx.ext.stomp.StompClientConnection j_receiver, io.vertx.core.Handler<java.util.Map<String, Object>> handler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.writingFrameHandler(handler != null ? event -> handler.handle(io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))) : null));
    return j_receiver;
  }
  public static java.util.Map<String, Object> options(io.vertx.ext.stomp.StompServer j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.options(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static io.vertx.ext.stomp.StompServerConnection write(io.vertx.ext.stomp.StompServerConnection j_receiver, java.util.Map<String, Object> frame) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.write(frame != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(frame)) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompServerHandler onAck(io.vertx.ext.stomp.StompServerHandler j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> subscribe, java.util.List<java.util.Map<String, Object>> messages) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.onAck(connection,
      subscribe != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(subscribe)) : null,
      messages != null ? messages.stream().map(elt -> elt != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(elt)) : null).collect(java.util.stream.Collectors.toList()) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompServerHandler onNack(io.vertx.ext.stomp.StompServerHandler j_receiver, io.vertx.ext.stomp.StompServerConnection connection, java.util.Map<String, Object> subscribe, java.util.List<java.util.Map<String, Object>> messages) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.onNack(connection,
      subscribe != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(subscribe)) : null,
      messages != null ? messages.stream().map(elt -> elt != null ? new io.vertx.ext.stomp.Frame(io.vertx.lang.groovy.ConversionHelper.toJsonObject(elt)) : null).collect(java.util.stream.Collectors.toList()) : null));
    return j_receiver;
  }
  public static io.vertx.ext.stomp.StompServerHandler bridge(io.vertx.ext.stomp.StompServerHandler j_receiver, java.util.Map<String, Object> options) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.bridge(options != null ? new io.vertx.ext.stomp.BridgeOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
    return j_receiver;
  }
}
