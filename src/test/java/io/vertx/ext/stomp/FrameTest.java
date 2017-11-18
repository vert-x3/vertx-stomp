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

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.impl.FrameException;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Check the behavior of {@link Frame}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class FrameTest {

  private Frame frame;

  @Test
  public void testThatPassCodeAreNotInToString() {
    Frame frame = new Frame(Frame.Command.STOMP, Headers.create("login", "vertx", "passcode", "secret"), null);
    assertThat(frame.toString()).doesNotContain("secret").contains("********");
  }

  @Test(expected = FrameException.class)
  public void testThatConnectFrameCannotHaveBody() {
    new Frame(Frame.Command.CONNECT, Headers.create("host", "foo"),
      Buffer.buffer("illegal"));
  }

  @Test
  public void testDefaultEncoding() {
    final String content = "This content contains utf-8 characters: ü ß é ø î";
    Frame frame = new Frame(Frame.Command.SEND, Headers.create(), Buffer.buffer(content));
    assertThat(frame.getBodyAsString()).isEqualTo(content);
  }

  @Test
  public void testEncoding() {
    final String content = "\u03B1";
    Frame frame = new Frame(Frame.Command.SEND, Headers.create("content-type",
      "text/plain;charset=utf-16"), Buffer.buffer(content));
    assertThat(frame.encoding()).isEqualTo("utf-16");
    frame = new Frame(Frame.Command.SEND, Headers.create("content-type",
      "text/plain;charset=utf-8"), Buffer.buffer(content));
    assertThat(frame.encoding()).isEqualTo("utf-8");
  }

  @Test
  public void testHeaderEncoding() {
    String value = "test-\r\n :\\-test";
    String expected = "test-" + (char) 92 + (char) 114 + (char) 92 + (char) 110 + " " + (char) 92 + (char) 99 +
      (char) 92 + (char) 92 + "-test";

    Frame frame = new Frame(Frame.Command.SEND, Headers.create("header", value), null);
    assertThat(frame.toBuffer().toString()).contains("header:" + expected + "\n");
  }

  @Test
  public void testHeaderEncodingOnConnectAndConnectedFrames() {
    String value = "test-\r\n :\\-test";
    String expected = "test-\r\n :" + (char) 92 + (char) 92 + "-test";

    Frame frame = new Frame(Frame.Command.CONNECT, Headers.create("header", value), null);
    assertThat(frame.toBuffer().toString()).contains("header:" + expected + "\n");

    frame = new Frame(Frame.Command.CONNECTED, Headers.create("header", value), null);
    assertThat(frame.toBuffer().toString()).contains("header:" + expected + "\n");
  }

  @Test
  public void testHeartbeatComputationWith00() {
    Frame.Heartbeat client = Frame.Heartbeat.parse("0,0");
    Frame.Heartbeat server = Frame.Heartbeat.parse("0,0");
    assertThat(Frame.Heartbeat.computePingPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(server, client)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePingPeriod(server, client)).isEqualTo(0);
  }

  @Test
  public void testHeartbeatComputationWith01() {
    Frame.Heartbeat client = Frame.Heartbeat.parse("0,1");
    Frame.Heartbeat server = Frame.Heartbeat.parse("0,1");
    assertThat(Frame.Heartbeat.computePingPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePingPeriod(server, client)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(server, client)).isEqualTo(0);
  }

  @Test
  public void testHeartbeatComputationWith10() {
    Frame.Heartbeat client = Frame.Heartbeat.parse("1,0");
    Frame.Heartbeat server = Frame.Heartbeat.parse("1,0");
    assertThat(Frame.Heartbeat.computePingPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(client, server)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePingPeriod(server, client)).isEqualTo(0);
    assertThat(Frame.Heartbeat.computePongPeriod(server, client)).isEqualTo(0);
  }

  @Test
  public void testHeartbeatComputationWith11() {
    Frame.Heartbeat client = Frame.Heartbeat.parse("1,1");
    Frame.Heartbeat server = Frame.Heartbeat.parse("1,1");
    assertThat(Frame.Heartbeat.computePingPeriod(client, server)).isEqualTo(1);
    assertThat(Frame.Heartbeat.computePongPeriod(client, server)).isEqualTo(1);
    assertThat(Frame.Heartbeat.computePingPeriod(server, client)).isEqualTo(1);
    assertThat(Frame.Heartbeat.computePongPeriod(server, client)).isEqualTo(1);
  }

  @Test
  public void testHeartbeatComputationNotSymmetric() {
    Frame.Heartbeat client = Frame.Heartbeat.parse("1,2");
    Frame.Heartbeat server = Frame.Heartbeat.parse("3,4");
    assertThat(Frame.Heartbeat.computePingPeriod(client, server)).isEqualTo(4);
    assertThat(Frame.Heartbeat.computePongPeriod(client, server)).isEqualTo(3);
    assertThat(Frame.Heartbeat.computePingPeriod(server, client)).isEqualTo(3);
    assertThat(Frame.Heartbeat.computePongPeriod(server, client)).isEqualTo(4);
  }

  @Test
  public void testWithTrailingSpaces() {
    frame = new Frame(Frame.Command.MESSAGE, Headers.create("foo", "bar"), Buffer.buffer("hello"));
    assertThat(frame.toBuffer(true).toString()).endsWith(FrameParser.NULL + "\n");

    frame = new Frame(Frame.Command.MESSAGE, Headers.create("foo", "bar"), null);
    assertThat(frame.toBuffer(true).toString()).endsWith(FrameParser.NULL + "\n");

    frame = new Frame(Frame.Command.MESSAGE, Headers.create(), null);
    assertThat(frame.toBuffer(true).toString()).endsWith(FrameParser.NULL + "\n");
  }

  @Test
  public void testErrorFrameContentType() {
    Frame errorFrame = Frames.createErrorFrame("Test Message", Headers.create("foo", "bar"), "hello");
    assertThat(errorFrame.getHeader(Frame.CONTENT_TYPE)).isEqualTo("text/plain");
  }

}
