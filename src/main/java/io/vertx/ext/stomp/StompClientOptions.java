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
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClientOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Options used to configure a STOMP client. As a STOMP client wraps a Net client, you can also configure the
 * underlying NET client.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class StompClientOptions extends NetClientOptions implements StompOptions {

  private List<String> acceptedVersions;
  private int port = DEFAULT_STOMP_PORT;
  private String host = DEFAULT_STOMP_HOST;
  private String login;
  private String passcode;
  private boolean autoComputeContentLength = true;
  private boolean useStompFrame = false;
  private boolean bypassHostHeader = false;
  private JsonObject heartbeat = DEFAULT_STOMP_HEARTBEAT;
  private String virtualHost;

  /**
   * Default constructor.
   */
  public StompClientOptions() {
    super();
    acceptedVersions = new ArrayList<>(DEFAULT_SUPPORTED_VERSIONS);
    Collections.reverse(acceptedVersions);
  }

  /**
   * Copy constructor.
   *
   * @param other The other {@link StompServerOptions} to copy when creating this
   */
  public StompClientOptions(StompClientOptions other) {
    super(other);
    this.port = other.port;
    this.host = other.host;
    this.login = other.login;
    this.passcode = other.passcode;
    this.autoComputeContentLength = other.autoComputeContentLength;
    this.acceptedVersions = new ArrayList<>(other.acceptedVersions);
    this.bypassHostHeader = other.bypassHostHeader;
    this.heartbeat = other.heartbeat;
    this.virtualHost = other.virtualHost;
  }

  /**
   * Creates an instance from a {@link io.vertx.core.json.JsonObject}.
   *
   * @param json the JsonObject to create it from
   */
  public StompClientOptions(JsonObject json) {
    super(json);
    StompClientOptionsConverter.fromJson(json, this);
  }

  /**
   * @return a JSON representation of the options.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    StompClientOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * Gets the STOMP server host.
   *
   * @return the STOMP server host
   */
  public String getHost() {
    return host;
  }

  /**
   * Sets the STOMP server host. {@code 0.0.0.0} by default.
   *
   * @param host the host name of the STOMP server
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setHost(String host) {
    this.host = host;
    return this;
  }

  /**
   * Gets the configured login.
   *
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Sets the login to use if the STOMP server is secured.
   *
   * @param login the login
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setLogin(String login) {
    this.login = login;
    return this;
  }

  /**
   * Gets the configured passcode.
   *
   * @return the passcode
   */
  public String getPasscode() {
    return passcode;
  }

  /**
   * Sets the passcode to use if the STOMP server is secured.
   *
   * @param passcode the passcode
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setPasscode(String passcode) {
    this.passcode = passcode;
    return this;
  }

  /**
   * Gets the STOMP server port.
   *
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * Sets the STOMP server port. {@code 61613} by default.
   *
   * @param port the port
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setPort(int port) {
    this.port = port;
    return this;
  }

  /**
   * Gets the list of STOMP protocol versions accepted by the client.
   *
   * @return the list of accepted version
   */
  public List<String> getAcceptedVersions() {
    return acceptedVersions;
  }

  /**
   * Sets the list of STOMP protocol versions accepted by the client. The list must be ordered from the lowest
   * version to the highest. By default the following list is used: {@code 1.0, 1.1, 1.2}
   *
   * @param acceptedVersions the order list of accepted versions
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setAcceptedVersions(List<String> acceptedVersions) {
    this.acceptedVersions = acceptedVersions;
    return this;
  }

  /**
   * Whether or not the automatic computation of the {@code content-length} header is enabled.
   *
   * @return whether or not the option is enabled
   */
  public boolean isAutoComputeContentLength() {
    return autoComputeContentLength;
  }

  /**
   * Sets whether or not the automatic computation of the {@code content-length} header is enabled. If enabled, the
   * {@code content-length} header is set in all frame with a body that do not explicitly set the header. The option
   * is enabled by default.
   *
   * @param autoComputeContentLength {@code true} to enable the option, {@code false} to disable it.
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setAutoComputeContentLength(boolean autoComputeContentLength) {
    this.autoComputeContentLength = autoComputeContentLength;
    return this;
  }

  /**
   * Checks whether or not the connection is made using the {@code STOMP} command instead of the {@code CONNECT}
   * command. The {@code STOMP} command has been introduced in the 1.2 version of the protocol to ease the network
   * analysis (as {@code CONNECT} is also used by HTTP. To be compliant with server not implementing the 1.2
   * specification, this option should be disabled.
   *
   * @return whether or not the option is enabled
   */
  public boolean isUseStompFrame() {
    return useStompFrame;
  }

  /**
   * Sets whether or not the connection is made using the {@code STOMP} command instead of the {@code CONNECT} command.
   * The {@code STOMP} command has been introduced in the 1.2 version of the protocol to ease the network analysis
   * (as {@code CONNECT} is also used by HTTP. To be compliant with server not implementing the 1.2 specification,
   * this option should be disabled. This option is disabled by default.
   *
   * @param useStompFrame {@code true} to enable the option, {@code false} to disable it.
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setUseStompFrame(boolean useStompFrame) {
    this.useStompFrame = useStompFrame;
    return this;
  }

  @Override
  public StompClientOptions setSsl(boolean ssl) {
    super.setSsl(true);
    return this;
  }

  /**
   * Checks whether or not the {@code host} header must be dropped from the {@code CONNECT/STOMP} frame. Server may
   * be picky about this header (such as RabbitMQ that does not support it).
   *
   * @return whether or not the option is enabled
   */
  public boolean isBypassHostHeader() {
    return bypassHostHeader;
  }

  /**
   * Sets whether or not the {@code host} header must be dropped from the {@code CONNECT/STOMP} frame. Server may
   * be picky about this header (such as RabbitMQ that does not support it). Options disabled by default.
   *
   * @param bypassHostHeader {@code true} to enable the option, {@code false} to disable it.
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setBypassHostHeader(boolean bypassHostHeader) {
    this.bypassHostHeader = bypassHostHeader;
    return this;
  }

  /**
   * Gets the heartbeat configuration.
   *
   * @return the heartbeat configuration
   * @see io.vertx.ext.stomp.Frame.Heartbeat
   */
  public JsonObject getHeartbeat() {
    return heartbeat;
  }

  /**
   * Sets the heartbeat configuration.
   *
   * @param heartbeat the configuration
   * @return the current {@link StompClientOptions}
   * @see io.vertx.ext.stomp.Frame.Heartbeat
   */
  public StompClientOptions setHeartbeat(JsonObject heartbeat) {
    this.heartbeat = heartbeat;
    return this;
  }

  /**
   * Gets the virtual host that would be use a "host" header value in the `CONNECT` frame. This option is useful for
   * Cloud AMQP.
   *
   * @return the virtual host
   */
  public String getVirtualHost() {
    return virtualHost;
  }

  /**
   * Sets the virtual host that will be used as "host" header value in the `CONNECT` frame.
   *
   * @param virtualHost the virtual host
   * @return the current {@link StompClientOptions}
   */
  public StompClientOptions setVirtualHost(String virtualHost) {
    this.virtualHost = virtualHost;
    return this;
  }
}
