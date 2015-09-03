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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks the behavior of the {@link HeaderCodec}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HeaderCodecTest {

  @Test
  public void testDecoding() {
    assertThat(HeaderCodec.decode("hello", false)).isEqualTo("hello");
    assertThat(HeaderCodec.decode("h\\ce", false)).isEqualTo("h:e");
    assertThat(HeaderCodec.decode("\\c-)", false)).isEqualTo(":-)");
    assertThat(HeaderCodec.decode(")-\\c", false)).isEqualTo(")-:");
    assertThat(HeaderCodec.decode("multi\nline", false)).isEqualTo("multi\nline");
    assertThat(HeaderCodec.decode("multi\r\nline", false)).isEqualTo("multi\r\nline");

    assertThat(HeaderCodec.decode("multi" + (char) 92 + (char) 110 + "line", false))
        .isEqualTo("multi\nline");

    assertThat(HeaderCodec.decode("multi"
        + (char) 92 + (char) 114
        + (char) 92 + (char) 110 + "line", false)).isEqualTo("multi\r\nline");

    assertThat(HeaderCodec.decode("test"
        + (char) 92 + (char) 92
        + "slash", false)).isEqualTo("test\\slash");
  }

  @Test
  public void testDecodingOnConnectOrConnectedFrames() {
    assertThat(HeaderCodec.decode("hello", true)).isEqualTo("hello");
    assertThat(HeaderCodec.decode("h\\ce", true)).isEqualTo("h\\ce");
    assertThat(HeaderCodec.decode("\\c-)", true)).isEqualTo("\\c-)");
    assertThat(HeaderCodec.decode(")-\\c", true)).isEqualTo(")-\\c");
    assertThat(HeaderCodec.decode("multi\nline", true)).isEqualTo("multi\nline");
    assertThat(HeaderCodec.decode("multi\r\nline", true)).isEqualTo("multi\r\nline");

    assertThat(HeaderCodec.decode("multi" + (char) 92 + (char) 110 + "line", true))
        .isEqualTo("multi" + (char) 92 + (char) 110 + "line");

    assertThat(HeaderCodec.decode("multi"
        + (char) 92 + (char) 114
        + (char) 92 + (char) 110 + "line", true)).isEqualTo("multi"
        + (char) 92 + (char) 114
        + (char) 92 + (char) 110 + "line");

    // Slash is decoded.
    assertThat(HeaderCodec.decode("test"
        + (char) 92 + (char) 92
        + "slash", true)).isEqualTo("test\\slash");
  }

  @Test(expected = FrameException.class)
  public void testDecodingIllegalEscape() {
    HeaderCodec.decode("this is an illegal " + (char) 92 + (char) 116 + " escape", false);
  }

  @Test
  public void testEncoding() {
    assertThat(HeaderCodec.encode("hello", false)).isEqualTo("hello");
    assertThat(HeaderCodec.encode("h:e", false)).isEqualTo("h\\ce");
    assertThat(HeaderCodec.encode(":-)", false)).isEqualTo("\\c-)");
    assertThat(HeaderCodec.encode(")-:", false)).isEqualTo(")-\\c");
    assertThat(HeaderCodec.encode("multi\nline", false)).isEqualTo("multi" + (char) 92 + (char) 110 + "line");
    assertThat(HeaderCodec.encode("multi\r\nline", false)).isEqualTo("multi" + (char) 92 + (char) 114
        + (char) 92 + (char) 110
        + "line");
    assertThat(HeaderCodec.encode("test\\slash", false)).isEqualTo("test" + (char) 92 + (char) 92
        + "slash");
  }

  @Test
  public void testEncodingOnConnectOrConnectedFrames() {
    assertThat(HeaderCodec.encode("hello", true)).isEqualTo("hello");
    assertThat(HeaderCodec.encode("h:e", true)).isEqualTo("h:e");
    assertThat(HeaderCodec.encode(":-)", true)).isEqualTo(":-)");
    assertThat(HeaderCodec.encode(")-:", true)).isEqualTo(")-:");
    assertThat(HeaderCodec.encode("multi\nline", true)).isEqualTo("multi\nline");
    assertThat(HeaderCodec.encode("multi\r\nline", true)).isEqualTo("multi\r\nline");
    // Slash is encoded.
    assertThat(HeaderCodec.encode("test\\slash", true)).isEqualTo("test" + (char) 92 + (char) 92
        + "slash");
  }

}