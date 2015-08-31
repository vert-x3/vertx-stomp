package io.vertx.ext.stomp.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.ext.stomp.*;

import java.util.HashMap;
import java.util.Objects;

/**
 * A STOMP frame parser compliant with the specification (including header decoding).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class FrameParser implements Handler<Buffer> {

  public static final String NULL = "\u0000";
  public static final String EOL = "\n";
  public static final String CARRIAGE_RETURN = "\r";

  public static final char LINE_FEED = '\n';
  public static final String COLON = ":";
  public static final char ESCAPE = '\\';

  public static final String COMMA = ",";

  private final StompServerOptions options;

  private Frame.Command command;
  private HashMap<String, String> headers = new HashMap<>();
  private Handler<Frame> handler;
  private int bodyLength = 0;

  private Handler<FrameException> errorHandler;

  enum State {
    COMMAND,
    HEADERS,
    BODY
  }

  private final RecordParser frameParser = RecordParser.newDelimited(EOL, this::handleLine);

  private State current = State.COMMAND;

  public FrameParser(StompServerOptions options) {
    this.options = options;
  }

  public FrameParser() {
    this(new StompServerOptions());
  }

  public synchronized FrameParser handler(Handler<Frame> handler) {
    Objects.requireNonNull(handler);
    this.handler = handler;
    return this;
  }

  public synchronized FrameParser errorHandler(Handler<FrameException> handler) {
    this.errorHandler = handler;
    return this;
  }

  private void handleLine(Buffer buffer) {
    switch (current) {
      case COMMAND:
        if (isEmpty(buffer)) {
          // ping frame.
          reset();
          handler.handle(Frames.ping());
          break;
        }

        // It's the verb line.
        // We try to find the right verb, here we can trim the line (would remove the optional \r).
        // Commands and Header are encoded in UTF-8 (spec)
        command = Frame.Command.valueOf(buffer.toString(StompOptions.UTF_8).trim());
        // Only one verb line, so next state
        current = State.HEADERS;
        break;
      case HEADERS:
        if (isEmpty(buffer)) {
          // End of headers.
          current = State.BODY;
          String length = headers.get(Frame.CONTENT_LENGTH);
          if (length != null) {
            int contentLength = Integer.valueOf(length);
            frameParser.fixedSizeMode(contentLength);
          } else {
            frameParser.delimitedMode(NULL);
          }
          break;
        } else {
          // Commands and Header are encoded in UTF-8 (spec)

          // Split should work here, if all server would have implemented the header encoding correctly. It's not
          // the case (ActiveMQ, looking at you right now), so, using subString instead.

          String line = buffer.toString(StompOptions.UTF_8);
          int index = line.indexOf(COLON);
          if (index == -1) {
            reportOrThrow("Invalid header line : '" + buffer.toString() + "'");
            return;
          }

          String header = line.substring(0, index);
          String value = line.substring(index + 1);

          if (hasExceededNumberOfHeaders()) {
            reportOrThrow("Number of headers exceeded");
            return;
          }

          if (hasExceededHeaderLength(header, value)) {
            reportOrThrow("Header length exceeded");
            return;
          }

          // By spec (repeated headers) - Put the header only if not already set.
          headers.putIfAbsent(header, decode(strip(value)));
          break;
        }
      case BODY:
        try {
          Frame frame = new Frame(command, headers, buffer);
          reset();
          handler.handle(frame);
        } catch (FrameException e) {
          reportOrThrow("Malformed frame received");
        }
    }
  }

  private void reset() {
    command = null;
    headers = new HashMap<>();
    current = State.COMMAND;
    frameParser.delimitedMode(EOL);
  }

  private boolean hasExceededHeaderLength(String header, String value) {
    return value.length() > options.getMaxHeaderLength()
        || header.length() > options.getMaxHeaderLength();
  }

  private boolean hasExceededNumberOfHeaders() {
    return headers.size() + 1 > options.getMaxHeaders();
  }

  private String decode(String value) {
    // By spec all frames except CONNECT and CONNECTED escape any carriage return, line feed or colon
    // found in the resulting UTF-8 encoded headers
    // Escape must be always escaped, STOMP frames are not part of the exception
    return HeaderCodec.decode(value, command == Frame.Command.CONNECT || command == Frame.Command.CONNECTED);
  }

  private String strip(String s) {
    if (s.endsWith(CARRIAGE_RETURN)) {
      return s.substring(0, s.length() - 1);
    }
    return s;
  }

  private boolean isEmpty(Buffer buffer) {
    return buffer.toString().length() == 0 || buffer.toString().equals(CARRIAGE_RETURN)
        || buffer.length() == 1 && buffer.getByte(0) == 0;
  }

  /**
   * Something has happened, so handle it.
   *
   * @param event the event to handle
   */
  @Override
  public synchronized void handle(Buffer event) {
    if (current == State.BODY) {
      bodyLength += event.length();
      if (!hasExceededBodySize()) {
        reportOrThrow("Body size exceeded");
        return;
      }
    }
    frameParser.handle(event);
  }

  private boolean hasExceededBodySize() {
    return bodyLength <= options.getMaxBodyLength();
  }

  /**
   * Invokes the error handler or throw an exception if no error handler.
   * Must be called when holding the monitor lock.
   *
   * @param error the error
   */
  private void reportOrThrow(String error) {
    FrameException exception = new FrameException(error);
    if (errorHandler != null) {
      errorHandler.handle(exception);
    } else {
      throw exception;
    }
  }
}
