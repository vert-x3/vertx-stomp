/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * STOMP Server options. You can also configure the Net Server used by the STOMP server from these options.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class StompServerOptions extends NetServerOptions implements StompOptions {

  public static final int DEFAULT_MAX_HEADER_LENGTH = 1024 * 10;
  public static final int DEFAULT_MAX_HEADERS = 1000;

  public static final int DEFAULT_MAX_BODY_LENGTH = 1024 * 1024 * 100;

  public static final int DEFAULT_MAX_FRAME_IN_TRANSACTION = 1000;
  public static final int DEFAULT_TRANSACTION_CHUNK_SIZE = 1000;
  public static final int DEFAULT_MAX_SUBSCRIPTIONS_BY_CLIENT = 1000;

  public static final String DEFAULT_WEBSOCKET_PATH = "/stomp";


  private int maxHeaderLength = DEFAULT_MAX_HEADER_LENGTH;
  private int maxHeaders = DEFAULT_MAX_HEADERS;
  private int maxBodyLength = DEFAULT_MAX_BODY_LENGTH;

  private int maxFrameInTransaction = DEFAULT_MAX_FRAME_IN_TRANSACTION;

  /**
   * The set of version of the STOMP specification supported by the server. Must be decreasing.
   */
  private List<String> supportedVersions = new ArrayList<>(DEFAULT_SUPPORTED_VERSIONS);
  private boolean secured = false;
  private boolean sendErrorOnNoSubscriptions = false;
  private long ackTimeout = 10000;
  private int timeFactor = 1;
  private JsonObject heartbeat = DEFAULT_STOMP_HEARTBEAT;
  private int transactionChunkSize = DEFAULT_TRANSACTION_CHUNK_SIZE;
  private int maxSubscriptionsByClient = DEFAULT_MAX_SUBSCRIPTIONS_BY_CLIENT;

  private boolean websocketBridge = false;
  private String websocketPath = DEFAULT_WEBSOCKET_PATH;

  private boolean disableTCPServer;
  private boolean trailingLine = DEFAULT_TRAILING_LINE;

  /**
   * Default constructor.
   */
  public StompServerOptions() {
    super();
    setPort(DEFAULT_STOMP_PORT);
    setHost(DEFAULT_STOMP_HOST);
  }

  /**
   * Copy constructor.
   *
   * @param other The other {@link StompServerOptions} to copy when creating this
   */
  public StompServerOptions(StompServerOptions other) {
    super(other);
    this.maxHeaderLength = other.maxHeaderLength;
    this.maxHeaders = other.maxHeaders;
    this.maxBodyLength = other.maxBodyLength;
    this.supportedVersions = new ArrayList<>(other.supportedVersions);
    this.secured = other.secured;
    this.sendErrorOnNoSubscriptions = other.sendErrorOnNoSubscriptions;
    this.ackTimeout = other.ackTimeout;
    this.timeFactor = other.timeFactor;
    this.heartbeat = other.heartbeat;
    this.maxFrameInTransaction = other.maxFrameInTransaction;
    this.transactionChunkSize = other.transactionChunkSize;
    this.maxSubscriptionsByClient = other.maxSubscriptionsByClient;

    this.websocketBridge = other.websocketBridge;
    this.websocketPath = other.websocketPath;

    this.disableTCPServer = other.disableTCPServer;
    this.trailingLine = other.trailingLine;
  }

  /**
   * Create an instance from a {@link io.vertx.core.json.JsonObject}.
   *
   * @param json the JsonObject to create it from
   */
  public StompServerOptions(JsonObject json) {
    super(json);
    StompServerOptionsConverter.fromJson(json, this);
  }

  /**
   * @return the JSON representation of this object.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    StompServerOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * Gets the max length of the frame body accepted by the server. If a frame exceeds this size, the frame is
   * rejected and an error is sent to the client.
   *
   * @return the max body length in bytes
   */
  public int getMaxBodyLength() {
    return maxBodyLength;
  }

  /**
   * Sets the max body length accepted by the server. 10 Mb by default.
   *
   * @param maxBodyLength the length in bytes.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setMaxBodyLength(int maxBodyLength) {
    this.maxBodyLength = maxBodyLength;
    return this;
  }

  /**
   * Gets the max length of header's value. If a frame has an header with a value exceeding this length, the frame is
   * rejected and an error is sent to the client. 10240 by default.
   *
   * @return the max header length
   */
  public int getMaxHeaderLength() {
    return maxHeaderLength;
  }

  /**
   * Sets the max header length.
   *
   * @param maxHeaderLength the max length of headers
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setMaxHeaderLength(int maxHeaderLength) {
    this.maxHeaderLength = maxHeaderLength;
    return this;
  }

  /**
   * Gets the maximum number of headers supported by the server. If a frame exceeds the number of headers, the frame
   * is rejected and an error is sent to the client.
   *
   * @return the max number of headers
   */
  public int getMaxHeaders() {
    return maxHeaders;
  }

  /**
   * Sets the maximum number of headers. 1000 by default.
   *
   * @param maxHeaders the number of headers
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setMaxHeaders(int maxHeaders) {
    this.maxHeaders = maxHeaders;
    return this;
  }

  /**
   * Gets the STOMP protocol versions supported by the server. Versions are provided in the decreasing order. By
   * default it's {@code "1.2", "1.1", "1.0"}.
   *
   * @return the list of supported versions
   */
  public List<String> getSupportedVersions() {
    return supportedVersions;
  }

  /**
   * Sets the STOMP protocol versions supported by the server. Versions must be given in the decreasing order.
   *
   * @param supportedVersions the set of supported versions.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setSupportedVersions(List<String> supportedVersions) {
    this.supportedVersions = supportedVersions;
    return this;
  }

  /**
   * Checks whether or not the server is secured (meaning it has an authentication mechanism). Security is disabled
   * by default and requires an {@link io.vertx.ext.auth.AuthProvider} handler.
   *
   * @return whether or not the option is enabled.
   */
  public boolean isSecured() {
    return secured;
  }

  /**
   * Enables or disables the server security feature. It requires an {@link io.vertx.ext.auth.AuthProvider} handler.
   *
   * @param secured whether or not the option should be enabled.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setSecured(boolean secured) {
    this.secured = secured;
    return this;
  }

  /**
   * Checks whether or not an error is sent to the client when this client sends a message to an not subscribed
   * destinations (no subscriptions on this destination).
   *
   * @return whether or not the option is enabled.
   */
  public boolean isSendErrorOnNoSubscriptions() {
    return sendErrorOnNoSubscriptions;
  }

  /**
   * Sets whether or not an error is sent to the client when this client sends a message to an not subscribed
   * destinations (no subscriptions on this destination).
   *
   * @param sendErrorOnNoSubscriptions whether or not the option should be enabled.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setSendErrorOnNoSubscriptions(boolean sendErrorOnNoSubscriptions) {
    this.sendErrorOnNoSubscriptions = sendErrorOnNoSubscriptions;
    return this;
  }

  /**
   * Gets the time factor, i.e. a multiplier applied to time constraints as a window error. 1 by default.
   *
   * @return the time factor.
   */
  public int getTimeFactor() {
    return timeFactor;
  }

  /**
   * Sets the time factor.
   *
   * @param timeFactor the time factor
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setTimeFactor(int timeFactor) {
    if (timeFactor <= 0) {
      throw new IllegalArgumentException("The time factor must be strictly positive");
    }
    this.timeFactor = timeFactor;
    return this;
  }

  @Override
  public StompServerOptions setSsl(boolean ssl) {
    super.setSsl(ssl);
    return this;
  }

  /**
   * Sets the port on which the server is going to listen for TCP connection.
   *
   * @param port the port number, {@code -1} to disable the TCP server.
   * @return the current {@link StompServerOptions}.
   */
  @Override
  public StompServerOptions setPort(int port) {
    if (port != -1) {
      // -1 is not a valid port, we can't pass it to the super class.
      super.setPort(port);
    } else {
      disableTCPServer = true;
    }
    return this;
  }

  /**
   * @return the port, -1 if the TCP server has been disabled.
   */
  @Override
  public int getPort() {
    if (disableTCPServer) {
      return -1;
    } else {
      return super.getPort();
    }
  }

  @Override
  public StompServerOptions setHost(String host) {
    super.setHost(host);
    return this;
  }

  /**
   * Gets the heartbeat configuration. Defaults to {@code x: 1000, y: 1000}.
   *
   * @return the heartbeat configuration.
   * @see io.vertx.ext.stomp.Frame.Heartbeat
   */
  public JsonObject getHeartbeat() {
    return heartbeat;
  }

  /**
   * Sets the heartbeat configuration.
   *
   * @param heartbeat the heartbeat configuration
   * @return the current {@link StompServerOptions}
   * @see io.vertx.ext.stomp.Frame.Heartbeat
   */
  public StompServerOptions setHeartbeat(JsonObject heartbeat) {
    this.heartbeat = heartbeat;
    return this;
  }

  /**
   * Gets the maximum number of frames that can be added to a transaction. If the number of frame added to a
   * transaction exceeds this threshold, the client receives an {@code ERROR} frame and is disconnected.
   *
   * @return the max number of frame in transaction
   */
  public int getMaxFrameInTransaction() {
    return maxFrameInTransaction;
  }

  /**
   * Sets the maximum number of frame that can be added in a transaction. If the number of frame added to a
   * transaction exceeds this threshold, the client receives an {@code ERROR} frame and is disconnected. The default
   * is 1000.
   *
   * @param maxFrameInTransaction the max number of frame
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setMaxFrameInTransaction(int maxFrameInTransaction) {
    this.maxFrameInTransaction = maxFrameInTransaction;
    return this;
  }

  /**
   * Gets the chunk size when replaying a transaction. To avoid blocking the event loop for too long, large
   * transactions are split into chunks, replayed one by one. This settings gets the chunk size.
   *
   * @return the size of the chunk
   */
  public int getTransactionChunkSize() {
    return transactionChunkSize;
  }

  /**
   * Sets the chunk size when replaying a transaction. To avoid blocking the event loop for too long, large
   * transactions are split into chunks, replayed one by one. This settings sets the chunk size.
   *
   * @param transactionChunkSize the size, must be strictly positive
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setTransactionChunkSize(int transactionChunkSize) {
    if (transactionChunkSize <= 0) {
      throw new IllegalArgumentException("Chunk size must be strictly positive");
    }
    this.transactionChunkSize = transactionChunkSize;
    return this;
  }

  /**
   * Gets the maximum of subscriptions a client is allowed to register. If a client exceeds this number, it receives
   * an error and the connection is closed.
   *
   * @return the max number of subscriptions per client
   */
  public int getMaxSubscriptionsByClient() {
    return maxSubscriptionsByClient;
  }

  /**
   * Sets the maximum of subscriptions a client is allowed to register. If a client exceeds this number, it receives
   * an error and the connection is closed.
   *
   * @param maxSubscriptionsByClient the max number of subscriptions
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setMaxSubscriptionsByClient(int maxSubscriptionsByClient) {
    this.maxSubscriptionsByClient = maxSubscriptionsByClient;
    return this;
  }

  /**
   * Checks whether or not the web socket bridge is enabled. This bridge allows receiving and sending STOMP frames on
   * a web socket. If set to {@code true}, the Stomp server provides a
   * {@link io.vertx.core.Handler<io.vertx.core.http.ServerWebSocket>} to read and write from the web socket. This
   * {@link Handler} must be passed to {@link io.vertx.core.http.HttpServer#webSocketHandler(Handler)}.
   *
   * @return whether or not the web socket bridge is enabled, {@code false} by default.
   */
  public boolean isWebsocketBridge() {
    return websocketBridge;
  }

  /**
   * Enables or disables the web socket bridge.
   *
   * @param websocketBridge whether or not the web socket bridge should be enabled.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setWebsocketBridge(boolean websocketBridge) {
    this.websocketBridge = websocketBridge;
    return this;
  }

  /**
   * Gets the path for the web socket. Only web sockets frame receiving on this path would be handled. By default
   * it's {@link #DEFAULT_WEBSOCKET_PATH}. The returned String is not a prefix but an exact match.
   *
   * @return the path
   */
  public String getWebsocketPath() {
    return websocketPath;
  }

  /**
   * Sets the websocket path. Only frames received on this path would be considered as STOMP frame.
   *
   * @param websocketPath the path, must not be {@code null}.
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setWebsocketPath(String websocketPath) {
    this.websocketPath = websocketPath;
    return this;
  }

  /**
   * Gets whether or not an empty line should be appended to the written STOMP frame. This option is disabled by
   * default. This option is not compliant with the STOMP specification, and so is not documented on purpose.
   *
   * @return whether or not an empty line should be appended to the written STOMP frame.
   */
  public boolean isTrailingLine() {
    return trailingLine;
  }

  /**
   * Sets whether or not an empty line should be appended to the written STOMP frame. This option is disabled by
   * default. This option is not compliant with the STOMP specification, and so is not documented on purpose.
   *
   * @param trailingLine {@code true} to add an empty line, {@code false} otherwise
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setTrailingLine(boolean trailingLine) {
    this.trailingLine = trailingLine;
    return this;
  }
}
