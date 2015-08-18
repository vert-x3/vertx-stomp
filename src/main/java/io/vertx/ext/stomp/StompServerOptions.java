package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * STOMP Server options. You can also configure the Net Server used by the STOMP server from these options.
 */
@DataObject(generateConverter = true)
public class StompServerOptions extends NetServerOptions implements StompOptions {

  public static final int MAX_HEADER_LENGTH = 1024 * 10;
  public static final int MAX_HEADERS = 1000;

  public static final int MAX_BODY_LENGTH = 1024 * 1024 * 100;

  public static final int MAX_FRAME_IN_TRANSACTION = 1000;

  private int maxHeaderLength = MAX_HEADER_LENGTH;
  private int maxHeaders = MAX_HEADERS;
  private int maxBodyLength = MAX_BODY_LENGTH;

  private int maxFrameInTransaction = MAX_FRAME_IN_TRANSACTION;

  /**
   * The set of version of the STOMP specification supported by the server. Must be decreasing.
   */
  private List<String> supportedVersions = new ArrayList<>(SUPPORTED_VERSIONS);
  private boolean secured = false;
  private boolean sendErrorOnNoSubscriptions = false;
  private long ackTimeout = 10000;
  private int timeFactor = 1;
  private JsonObject heartbeat = DEFAULT_STOMP_HEARTBEAT;

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
   * Gets the {@code ACK} timeout, i.e. the time after which a non acknowledged message is considered not-acknowledge
   * ({@code NACK}). Defaults to 10 seconds.
   *
   * @return the ack timeout is milliseconds.
   */
  public long getAckTimeout() {
    return ackTimeout;
  }

  /**
   * Sets the {@code ACK} timeout. Be careful when setting this value as a too low value by considered messages as
   * not received even if they are currently processed by the client.
   *
   * @param ackTimeout the {@code ACK} timeout in milliseconds
   * @return the current {@link StompServerOptions}
   */
  public StompServerOptions setAckTimeout(long ackTimeout) {
    if (timeFactor <= 0) {
      throw new IllegalArgumentException("The ack timeout must be strictly positive");
    }
    this.ackTimeout = ackTimeout;
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
    super.setSsl(true);
    return this;
  }

  @Override
  public StompServerOptions setPort(int port) {
    super.setPort(port);
    return this;
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
}
