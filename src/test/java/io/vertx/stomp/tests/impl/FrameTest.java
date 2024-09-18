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

package io.vertx.stomp.tests.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Command;
import io.vertx.ext.stomp.Frame;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FrameTest {

  @Test
  public void testCopyFrame() {
    Command command = Command.UNKNOWN;
    Map<String, String> headers = new HashMap<String, String>() {{
      this.put("hdr0", "val0");
      this.put("hdr1", "val1");
    }};
    Buffer body = Buffer.buffer("body content");
    Frame other = new Frame(command, headers, body);
    Frame current = new Frame(other);
    assertThat(current.getCommand()).isEqualTo(Command.UNKNOWN);
    assertThat(current.getHeader("hdr0")).isEqualTo("val0");
    assertThat(current.getHeader("hdr1")).isEqualTo("val1");
    assertThat(current.getBody()).isNotSameAs(other.getBody());
    assertThat(current.getBodyAsString()).isEqualTo("body content");
  }

}
