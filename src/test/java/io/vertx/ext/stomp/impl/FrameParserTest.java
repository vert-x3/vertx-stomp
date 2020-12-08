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

package io.vertx.ext.stomp.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Command;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompOptions;
import io.vertx.ext.stomp.StompServerOptions;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * Check the behavior of the {@link FrameParser}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class FrameParserTest {


  @Test
  public void testConnectFrame() {
    Buffer buffer = Buffer.buffer("CONNECT\n"
        + "accept-version:1.2\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);

    assertThat(frame.getCommand()).isEqualTo(Command.CONNECT);
    assertThat(frame.getBodyAsByteArray()).isEmpty();
    assertThat(frame.getHeader("accept-version")).isEqualTo("1.2");
    assertThat(frame.getHeader("host")).isEqualTo("stomp.github.org");
  }

  @Test
  public void testStompFrame() {
    Buffer buffer = Buffer.buffer("STOMP\n"
        + "accept-version:1.2\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);

    assertThat(frame.getCommand()).isEqualTo(Command.STOMP);
    assertThat(frame.getBodyAsByteArray()).isEmpty();
    assertThat(frame.getHeader("accept-version")).isEqualTo("1.2");
    assertThat(frame.getHeader("host")).isEqualTo("stomp.github.org");
  }

  @Test
  public void testConnectedFrame() {
    Buffer buffer = Buffer.buffer("CONNECTED\n" +
        "version:1.2\n" +
        "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);

    assertThat(frame.getCommand()).isEqualTo(Command.CONNECTED);
    assertThat(frame.getBodyAsByteArray()).isEmpty();
    assertThat(frame.getHeader("version")).isEqualTo("1.2");
  }

  @Test
  public void testErrorFrame() {
    Buffer buffer = Buffer.buffer("ERROR\n" +
        "version:1.2,2.1\n" +
        "content-type:text/plain\n" +
        "\n" + "Supported protocol versions are 1.2 2.1")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);

    assertThat(frame.getCommand()).isEqualTo(Command.ERROR);
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("Supported protocol versions are 1.2 2.1");
    assertThat(frame.getHeader("version")).isEqualTo("1.2,2.1");
    assertThat(frame.getHeader("content-type")).isEqualTo("text/plain");
  }

  @Test
  public void testEmptyMessage() {
    Buffer buffer = Buffer.buffer();
    Frame frame = parse(buffer);
    assertThat(frame).isNull();
  }

  @Test(expected = FrameException.class)
  public void testInvalidHeaders() {
    Buffer buffer = Buffer.buffer("CONNECTED\n" +
        "version 1.2\n" +
        "\n")
        .appendString(FrameParser.NULL);

    parse(buffer);
  }

  @Test
  public void testMessageUsingCarriageReturn() {
    Buffer buffer = Buffer.buffer("SEND\r\n" +
        "header:hello\r\n" +
        "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
  }

  @Test
  public void testMessageWithAdditionalEOLAfterNULL() {
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "\n" +
        "this is my content.")
        .appendString(FrameParser.NULL)
        .appendString(FrameParser.EOL)
        .appendString(FrameParser.EOL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my content.");
  }

  @Test
  public void testMessageWithHeaderValueUsingEscapedCharacters() {
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello" + (char) 92 + (char) 99 + "-)\n" +
        "\n" +
        "this is my content.")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello:-)");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my content.");
  }

  @Test
  public void testMessageWithHeaderValueUsingEscapedCharactersNotEscaped() {
    Buffer buffer = Buffer.buffer("CONNECT\n" +
        "header:hello" + (char) 92 + (char) 99 + "-)\n" +
        "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.CONNECT);
    //By spec, CONNECT and CONNECTED frames do not decode the header.
    assertThat(frame.getHeader("header")).isEqualTo("hello\\c-)");
  }

  @Test
  public void testMessageWithPaddingInHeaders() {
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header: hello\n" +
        "header2:hello \n" +
        "\n" +
        "this is my content.")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo(" hello");
    assertThat(frame.getHeader("header2")).isEqualTo("hello ");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my content.");
  }

  @Test
  public void testMessagesWithHeaderNameContainingSpaces() {
    Buffer buffer = Buffer.buffer("SEND\n" +
        "head er: hello\n" +
        "\n" +
        "this is my content.")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("head er")).isEqualTo(" hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my content.");
  }

  @Test
  public void testFrameWithoutContentLength() {
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "\n" +
        "this is my content.")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my content.");
  }

  @Test
  public void testFrameWithContentLength() {
    String content = "this is my \n content.";
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "content-length:" + content.length() + "\n" +
        "\n" +
        content)
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my \n content.");
  }

  @Test(expected = NumberFormatException.class)
  public void testFrameContainingAnIllegalContentLength() {
    String content = "this is my \n content.";
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "content-length:" + "illegal" + "\n" +
        "\n" +
        content)
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my \n content.");
  }

  @Test
  public void testFrameWithWrongContentLength() {
    String content = "this is my \n content.";
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "content-length:" + (content.length() - 2) + "\n" +
        "\n" +
        content)
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my \n conten");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFrameContainingANegativeContentLength() {
    String content = "this is my \n content.";
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "content-length:" + -1 + "\n" +
        "\n" +
        content)
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my \n content.");
  }

  @Test
  public void testFrameWithContentLengthAndContentContainsNULL() {
    String content = "this is my \u0000 content.";
    Buffer buffer = Buffer.buffer("SEND\n" +
        "header:hello\n" +
        "content-length:" + content.length() + "\n" +
        "\n" +
        content)
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.SEND);
    assertThat(frame.getHeader("header")).isEqualTo("hello");
    assertThat(frame.getBodyAsString(StompOptions.UTF_8)).isEqualTo("this is my \u0000 content.");
  }

  @Test
  public void testTelnetStyleFrame() {
    String connect = "CONNECT\r\n" +
        "accept-version:1.2\r\n" +
        "login:system\r\n" +
        "passcode:manager\r\n" +
        "\r\n" +
        "\u0000\r\n";
    Buffer buffer = Buffer.buffer(connect);
    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.CONNECT);
    assertThat(frame.getHeader("accept-version")).isEqualTo("1.2");
    assertThat(frame.getHeader("login")).isEqualTo("system");
    assertThat(frame.getBodyAsByteArray()).isEmpty();
  }

  @Test
  public void testRepeatedHeader() {
    Buffer buffer = Buffer.buffer("MESSAGE\n" +
        "foo:World\n" +
        "foo:Hello\n" +
        "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);

    assertThat(frame.getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frame.getBodyAsByteArray()).isEmpty();
    assertThat(frame.getHeader("foo")).isEqualTo("World");
  }

  private final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

  @Test
  public void testLargeFrames() {
    FrameParser parser = new FrameParser();
    AtomicReference<Frame> ref = new AtomicReference<>();
    parser.handler(ref::set);

    Buffer buffer = Buffer.buffer("MESSAGE\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("hello:world\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("\n");
    parser.handle(buffer);
    buffer = Buffer.buffer(LOREM);
    parser.handle(buffer);
    buffer = Buffer.buffer(LOREM);
    parser.handle(buffer);
    buffer = Buffer.buffer(FrameParser.NULL);
    parser.handle(buffer);

    Frame frame = ref.get();

    assertThat(frame.getCommand()).isEqualTo(Command.MESSAGE);
    assertThat(frame.getHeader("hello")).isEqualTo("world");
    assertThat(frame.getBody().length()).isEqualTo(LOREM.length() * 2);
  }

  @Test
  public void testMultipleFrames() {
    FrameParser parser = new FrameParser();
    List<Frame> frames = new ArrayList<>();
    parser.handler(frames::add);

    Buffer buffer = Buffer
        .buffer("CONNECT\n" + "login:system\n" + "passcode:manager\n\n" + FrameParser.NULL)
        .appendString("SEND\n" + "destination:/queue" + "\n\n" + "Hello World" + FrameParser.NULL);
    parser.handle(buffer);
    assertThat(frames).hasSize(2);
    assertThat(frames.get(0).getCommand()).isEqualTo(Command.CONNECT);
    assertThat(frames.get(0).getHeader("login")).isEqualTo("system");
    assertThat(frames.get(0).getHeader("passcode")).isEqualTo("manager");
    assertThat(frames.get(0).getHeader("destination")).isNull();
    assertThat(frames.get(0).hasEmptyBody()).isTrue();
    assertThat(frames.get(1).getCommand()).isEqualTo(Command.SEND);
    assertThat(frames.get(1).getHeader("destination")).isEqualTo("/queue");
    assertThat(frames.get(1).getHeader("login")).isNull();
    assertThat(frames.get(1).getBodyAsString()).isEqualTo("Hello World");
  }

  @Test
  public void testWrongCommand() {
    Buffer buffer = Buffer.buffer("ILLEGAL\n"
        + "accept-version:1.2\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getCommand()).isEqualTo(Command.UNKNOWN);
    assertThat(frame.getHeader(Frame.STOMP_FRAME_COMMAND)).isEqualTo("ILLEGAL");
  }

  @Test(expected = FrameException.class)
  public void testNumberOfHeadersExceeded() {
    Buffer buffer = Buffer.buffer("CONNECT\n"
        + "accept-version:1.2\n"
        + "header1:1.2\n"
        + "header2:1.2\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    parse(new StompServerOptions().setMaxHeaders(2), buffer);
  }

  @Test(expected = FrameException.class)
  public void testHeaderLengthExceeded() {
    Buffer buffer = Buffer.buffer("CONNECT\n"
        + "header1:" + LOREM + "\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    parse(new StompServerOptions().setMaxHeaderLength(50), buffer);
  }

  @Test
  public void testWronglyEncodedHeaderBecauseOfActiveMQ() {
    // ActiveMQ does not encode the header as stated in the spec.
    Buffer buffer = Buffer.buffer("CONNECT\n"
        + "session:123-456:78\n"
        + "host:stomp.github.org\n"
        + "\n")
        .appendString(FrameParser.NULL);

    final Frame frame = parse(buffer);
    assertThat(frame.getHeader("session")).isEqualTo("123-456:78");
  }

  @Test
  public void testDecoding() {
    String value = "test-" + (char) 92 + (char) 114 + (char) 92 + (char) 110 + " " + (char) 92 + (char) 99 + (char) 92
        + (char) 92 + "-test";
    Buffer buffer = Buffer.buffer("SEND\n"
        + "header:" + value + "\n"
        + "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getHeader("header")).isEqualTo("test-\r\n :\\-test");
  }

  @Test
  public void testDecodingOnConnectAndConnectedFrames() {
    String value = "test-" + (char) 92 + (char) 114 + (char) 92 + (char) 110 + " " + (char) 92 + (char) 99 + (char) 92
        + (char) 92 + "-test";
    String expected = "test-" + (char) 92 + (char) 114 + (char) 92 + (char) 110 + " " + (char) 92 + (char) 99 +
        "\\-test";
    Buffer buffer = Buffer.buffer("CONNECT\n"
        + "header:" + value + "\n"
        + "\n")
        .appendString(FrameParser.NULL);

    Frame frame = parse(buffer);
    assertThat(frame.getHeader("header")).isEqualTo(expected);

    buffer = Buffer.buffer("CONNECTED\n"
        + "header:" + value + "\n"
        + "\n")
        .appendString(FrameParser.NULL);

    frame = parse(buffer);
    assertThat(frame.getHeader("header")).isEqualTo(expected);

    // Regular decoding
    buffer = Buffer.buffer("STOMP\n"
        + "header:" + value + "\n"
        + "\n")
        .appendString(FrameParser.NULL);
    frame = parse(buffer);
    assertThat(frame.getHeader("header")).isEqualTo("test-\r\n :\\-test");
  }

  @Test
  public void testBodySizeExceeded() {
    // Would fail at the second body buffer.
    FrameParser parser = new FrameParser(new StompServerOptions().setMaxBodyLength(LOREM.length()
        + 20));
    AtomicReference<Frame> ref = new AtomicReference<>();
    parser.handler(ref::set);

    Buffer buffer = Buffer.buffer("MESSAGE\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("hello:world\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("\n");
    parser.handle(buffer);
    buffer = Buffer.buffer(LOREM);
    parser.handle(buffer);
    assertThat(getCurrentBodySize(parser)).isGreaterThan(0);

    try {
      buffer = Buffer.buffer(LOREM);
      parser.handle(buffer);
      fail("Exception expected");
    } catch (FrameException e) {
      // OK.
    }

    assertThat(getCurrentBodySize(parser)).isEqualTo(0);
  }

  @Test
  public void testBodySizeExceededInOneFrame() {
    // Would fail at the second body buffer.
    FrameParser parser = new FrameParser(new StompServerOptions().setMaxBodyLength(LOREM.length()
        + 5));
    AtomicReference<Frame> ref = new AtomicReference<>();
    parser.handler(ref::set);

    Buffer buffer = Buffer.buffer("MESSAGE\n")
        .appendString("hello:world\n")
        .appendString("\n")
        .appendString(LOREM)
        .appendString("\n")
        .appendString(LOREM)
        .appendString(FrameParser.NULL);

    try {
      parser.handle(buffer);
      fail("Exception expected");
    } catch (FrameException e) {
      // OK.
    }
    // Counter has been reset.
    assertThat(getCurrentBodySize(parser)).isEqualTo(0);
  }

  @Test
  public void testBodySizeComputation() {
    List<Integer> sizes = new ArrayList<>();
    List<Integer> stored = new ArrayList<>();
    FrameParser parser = new FrameParser(new StompServerOptions());
    parser.handler(frame -> {
      sizes.add(frame.toBuffer().length());
      stored.add(getCurrentBodySize(parser));
    });

    // Frame 1
    Buffer buffer = Buffer.buffer("MESSAGE\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("hello:world\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("\n");
    parser.handle(buffer);
    parser.handle(Buffer.buffer(FrameParser.NULL));
    assertThat(getCurrentBodySize(parser)).isEqualTo(0);

    // Frame 2
    buffer = Buffer.buffer("MESSAGE\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("hello:world\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("\n" + LOREM);
    parser.handle(buffer);
    parser.handle(Buffer.buffer(FrameParser.NULL));

    assertThat(getCurrentBodySize(parser)).isEqualTo(0);

    // Frame 3
    buffer = Buffer.buffer("MESSAGE\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("hello:world\n");
    parser.handle(buffer);
    buffer = Buffer.buffer("\n" + FrameParser.NULL);
    parser.handle(buffer);
    assertThat(getCurrentBodySize(parser)).isEqualTo(0);

    for (int size : sizes) {
      assertThat(size).isGreaterThan(0);
    }

    for (int size : stored) {
      assertThat(size).isEqualTo(0);
    }
  }

  private int getCurrentBodySize(FrameParser parser) {
    try {
      Field field = parser.getClass().getDeclaredField("bodyLength");
      field.setAccessible(true);
      return (int) field.get(parser);
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve the current body length", e);
    }
  }

  private Frame parse(Buffer buffer) {
    FrameParser parser = new FrameParser();
    AtomicReference<Frame> frame = new AtomicReference<>();
    parser.handler(
        (f) -> {
          if (frame.get() == null) {
            frame.set(f);
          }
        });
    parser.handle(buffer);
    return frame.get();
  }

  private Frame parse(StompServerOptions options, Buffer buffer) {
    FrameParser parser = new FrameParser(options);
    AtomicReference<Frame> frame = new AtomicReference<>();
    parser.handler(frame::set);
    parser.handle(buffer);
    return frame.get();
  }

}
