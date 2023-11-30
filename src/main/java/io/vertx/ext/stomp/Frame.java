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
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.JsonGen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.impl.FrameException;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.impl.HeaderCodec;
import io.vertx.ext.stomp.utils.Headers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a STOMP frame. STOMP frames are structured as follows. It starts by a {@code command}, followed by a
 * set of headers. Then the frame may have a body and is finished by a {@code 0} byte. This class represents this
 * structure and provide access to the different parts.
 * <p/>
 * This class is <strong>NOT</strong> thread-safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject
@JsonGen(publicConverter = false)
public class Frame {

  // General headers
  public static final String CONTENT_LENGTH = "content-length";
  public static final String CONTENT_TYPE = "content-type";

  // Connection headers
  public static final String HOST = "host";

  public static final String VERSION = "version";
  public static final String ACCEPT_VERSION = "accept-version";

  public static final String SESSION = "session";
  public static final String SERVER = "server";

  public static final String LOGIN = "login";
  public static final String PASSCODE = "passcode";
  public static final String HEARTBEAT = "heart-beat";

  // Message headers
  public static final String DESTINATION = "destination";
  public static final String RECEIPT = "receipt";
  public static final String RECEIPT_ID = "receipt-id";
  public static final String ACK = "ack";
  public static final String ID = "id";
  public static final String SUBSCRIPTION = "subscription";
  public static final String MESSAGE_ID = "message-id";
  public static final String TRANSACTION = "transaction";
  public static final String MESSAGE = "message";

  /**
   * Header used when a frame using an unknown command is received. The created {@link Frame} object uses
   * the {@link Command#UNKNOWN} command and gives the original command in this header.
   */
  public static final String STOMP_FRAME_COMMAND = "frame-command";

  /**
   * Regex used to extract the body encoding that may be specified in the {@code content-type} header. By default,
   * UTF-8 is used.
   */
  private final static Pattern CHARSET_PATTERN = Pattern.compile(".+;charset=([a-zA-Z0-9\\-]+);?.*");

  /**
   * Represents the heartbeat configuration. Heartbeat determine when a party involved in the exchange (either the
   * client or the server) can detect the inactivity of the other party and close the connection. Configuration is
   * made in the {@code heartbeat} header.
   * <p/>
   * This class is thread-safe.
   */
  public static class Heartbeat {
    final int x;
    final int y;

    public Heartbeat(int x, int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Creates an instance of {@link io.vertx.ext.stomp.Frame.Heartbeat} from the {@code heartbeat header} of a frame
     * . If the header is {@code null}, the (0,0) configuration is returned.
     *
     * @param header the header
     * @return the heartbeat configuration
     */
    public static Heartbeat parse(String header) {
      if (header == null) {
        return new Heartbeat(0, 0);
      } else {
        String[] token = header.split(FrameParser.COMMA);
        return new Heartbeat(Integer.parseInt(token[0]), Integer.parseInt(token[1]));
      }
    }

    /**
     * Creates an instance of {@link io.vertx.ext.stomp.Frame.Heartbeat} from the JSON configuration provides in the
     * client / server options. The JSON is structure as follows: {@code {"x": 1000, "y": 1000}}. The {@code x} and
     * {@code y} time are given in milliseconds.
     *
     * @param json the json object configuring the heartbeat.
     * @return the heartbeat configuration
     */
    public static Heartbeat create(JsonObject json) {
      return new Heartbeat(
          json.getInteger("x", 0),
          json.getInteger("y", 0));
    }

    /**
     * @return the heartbeat configuration to be used in the {@code heartbeat} header.
     */
    @Override
    public String toString() {
      return x + "," + y;
    }

    /**
     * Computes the ping period. The party must send a {@code ping} to the other party every x milliseconds, where x
     * is the value returned by this method. The value is computed from the two parties heartbeat configuration.
     *
     * @param client the client configuration
     * @param server the server configuration
     * @return the ping period
     */
    public static long computePingPeriod(Heartbeat client, Heartbeat server) {
      if (client.x == 0 || server.y == 0) {
        return 0;
      }
      return Math.max(client.x, server.y);
    }

    /**
     * Computes the pong period. The party can consider the other party inactive when it did not receives any message
     * since x, where x is the value returned by this method. The value is computed from the two parties heartbeat
     * configuration.
     *
     * @param client the client configuration
     * @param server the server configuration
     * @return the pong period
     */
    public static long computePongPeriod(Heartbeat client, Heartbeat server) {
      if (client.y == 0 || server.x == 0) {
        return 0;
      }
      return Math.max(client.y, server.x);
    }
  }

  /**
   * Only SEND, MESSAGE and ERROR frames accept bodies.
   */
  private final static List<Command> COMMANDS_ACCEPTING_BODY = Arrays.asList(
      Command.SEND, Command.MESSAGE, Command.ERROR, Command.UNKNOWN);

  private Command command;

  private final Map<String, String> headers;

  private Buffer body;

  /**
   * Creates an un-configured frame. Should only be used by converters.
   */
  public Frame() {
    // Default constructor.
    headers = new Headers();
  }

  /**
   * Creates a new instance of {@link Frame} by copying the values from the {@code other} frame. The body of the
   * frame is copied.
   *
   * @param other the frame to copy.
   */
  public Frame(Frame other) {
    this();
    this.command = other.command;
    this.headers.putAll(other.headers);
    if (other.body != null) {
      this.body = other.body.copy();
    }
    validate();
  }

  /**
   * Creates a new instance of {@link Frame} from its JSON representation.
   *
   * @param json the json form of the frame
   */
  public Frame(JsonObject json) {
    this();
    FrameConverter.fromJson(json, this);
    validate();
  }

  /**
   * Creates a new instance of {@link Frame}.
   *
   * @param command the command, must not be {@code null}
   * @param headers the headers, must not be {@code null}
   * @param body    the body
   */
  public Frame(Command command, Map<String, String> headers, Buffer body) {
    Objects.requireNonNull(command, "The frame command must be set");
    Objects.requireNonNull(headers, "The headers must be set to empty if none");
    this.command = command;
    this.headers = headers;
    this.body = body;
    validate();
  }

  /**
   * Adds a header to the frame.
   *
   * @param key   the header name
   * @param value the header value
   * @return the current {@link Frame}
   */
  public Frame addHeader(String key, String value) {
    headers.putIfAbsent(key, value);
    return this;
  }

  /**
   * Gets the value of the {@code ack} header.
   *
   * @return the {@code ack} header value, {@code null} if not set
   */
  public String getAck() {
    return headers.get(ACK);
  }

  /**
   * Gets the frame headers. Modifications to the returned {@link Map} modifies the headers of the frame.
   *
   * @return the headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Sets the headers of the frames.
   *
   * @param headers the header, may be {@code null}. In the {@code null} case, an empty map is used to store the
   *                frame headers.
   * @return the current {@link Frame}
   */
  public Frame setHeaders(Map<String, String> headers) {
    if (headers == null) {
      this.headers.clear();
    } else {
      this.headers.clear();
      this.headers.putAll(headers);
    }
    return this;
  }

  /**
   * Sets the frame command.
   *
   * @param command the command, must not be {@code null}
   * @return the current {@link Frame}
   */
  public Frame setCommand(Command command) {
    Objects.requireNonNull(command, "The frame command must not be null.");
    this.command = command;
    return this;
  }

  /**
   * Sets the body of the frame.
   *
   * @param body the body
   * @return the current {@link Frame}
   */
  public Frame setBody(Buffer body) {
    this.body = body;
    return this;
  }

  /**
   * @return the JSON representation of the current frame.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    FrameConverter.toJson(this, json);
    return json;
  }

  /**
   * Checks the validity of the frame. Frames must have a valid command, and not all frames can have a body.
   */
  public void validate() {
    // A frame must have a valid command
    if (command == null) {
      throw new FrameException("The frame does not have a command");
    }

    // Spec says: Only the SEND, MESSAGE and ERROR frames MAY have a body, All other frames MUST NOT
    // have a body
    if (!COMMANDS_ACCEPTING_BODY.contains(command) && !hasEmptyBody()) {
      throw new FrameException("The frame " + command.name() + " cannot have a body");
    }

  }

  /**
   * @return whether or not the frame has a body.
   */
  public boolean hasEmptyBody() {
    return body == null || body.length() == 0;
  }

  public Command getCommand() {
    return command;
  }

  /**
   * Gets the value of the header with the given name.
   *
   * @param name the header name
   * @return the value, {@code null} if not set
   */
  public String getHeader(String name) {
    return headers.get(name);
  }

  public Buffer getBody() {
    return body;
  }

  /**
   * Gets the body of the frames as a String encoded in the given encoding.
   *
   * @param encoding the encoding
   * @return the body, {@code null} if none
   */
  public String getBodyAsString(String encoding) {
    if (body == null) {
      return null;
    }
    return body.toString(encoding);
  }

  /**
   * Gets the body of the frames as a String encoded in the frame encoding.
   *
   * @return the body, {@code null} if none
   */
  public String getBodyAsString() {
    return getBodyAsString(encoding());
  }

  /**
   * Read the frame encoding. If not set defaults to utf-8.
   *
   * @return the encoding
   */
  public String encoding() {
    String header = getHeader(CONTENT_TYPE);
    if (header == null) {
      return StompOptions.UTF_8;
    } else {
      final Matcher matcher = CHARSET_PATTERN.matcher(header);
      if (matcher.matches()) {
        return matcher.group(1);
      } else {
        return StompOptions.UTF_8;
      }
    }
  }

  /**
   * @return the body of the frame as a byte array, {@code null} if none.
   */
  @GenIgnore
  public byte[] getBodyAsByteArray() {
    if (body == null) {
      return null;
    }
    return body.getBytes();
  }

  /**
   * Creates a buffer for the current frame. This buffer may contain an empty line if the {@code trailingLine} is set
   * to {@code true}
   *
   * @param trailingLine whether or not a trailing line should be added to the buffer
   * @return a {@link Buffer} containing the STOMP frame. It follows strictly the STOMP specification (including
   * header encoding).
   */
  public Buffer toBuffer(boolean trailingLine) {
    Buffer buffer = toBuffer();
    if (trailingLine) {
      buffer.appendString("\n");
    }
    return buffer;
  }

  /**
   * This method does not enforce the trailing line option. It should not be used directly, except for the PING frame.
   *
   * @return a {@link Buffer} containing the STOMP frame. It follows strictly the STOMP specification (including
   * header encoding).
   */
  public Buffer toBuffer() {
    Buffer buffer = Buffer.buffer(command.name() + "\n");
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      buffer.appendString(encode(entry.getKey()) + ":" + encode(entry.getValue()) + "\n");
    }
    buffer.appendString("\n");
    if (body != null) {
      buffer.appendBuffer(body);
    }
    buffer.appendString(FrameParser.NULL);
    return buffer;
  }

  private String encode(String header) {
    // By spec, frame headers need to be encoded. CONNECT and CONNECTED frames do not encode \r \n \c but still
    // require the encoding of \\.
    return HeaderCodec.encode(header, command == Command.CONNECT || command == Command.CONNECTED);
  }

  public String toString() {
    StringBuilder buffer = new StringBuilder(command.name() + "\n");
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      if (entry.getKey().equals(PASSCODE)) {
        buffer.append(entry.getKey()).append(":").append("********").append("\n");
      } else {
        buffer.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
      }
    }
    buffer.append("\n");
    if (body != null) {
      buffer.append(body);
    }
    buffer.append("^@");
    return buffer.toString();
  }

  // Getter and Setter on basic headers

  public Frame setDestination(String destination) {
    Objects.requireNonNull(destination);
    return addHeader(DESTINATION, destination);
  }

  public Frame setTransaction(String id) {
    Objects.requireNonNull(id);
    return addHeader(TRANSACTION, id);
  }

  public Frame setId(String id) {
    Objects.requireNonNull(id);
    return addHeader(ID, id);
  }

  public String getId() {
    return getHeader(ID);
  }

  public String getReceipt() {
    return getHeader(RECEIPT);
  }

  public String getTransaction() {
    return getHeader(TRANSACTION);
  }

  public String getDestination() {
    return getHeader(DESTINATION);
  }

}
