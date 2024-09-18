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

package io.vertx.stomp.tests;

import io.vertx.ext.stomp.utils.Headers;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Checks the behavior of {@link Headers}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HeadersTest {

  @Test
  public void testEmptyCreation() {
    assertThat(Headers.create()).isInstanceOf(Map.class);
  }

  @Test
  public void testCreationWithEmptyArray() {
    final Headers headers = Headers.create(new String[0]);
    assertThat(headers).isInstanceOf(Map.class).isEmpty();
  }

  @Test
  public void testCreationWithArgs() {
    Headers map = Headers.create("a", "b", "c", "d");
    assertThat(map).containsExactly(entry("a", "b"), entry("c", "d"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreationWithWrongNumberOfArgs() {
    Headers.create("a", "b", "c", "d", "illegal");
  }


  @Test
  public void testAdd() {
    Headers map = Headers.create().add("a", "b").add("c", "d");
    map.put("e", "f");
    assertThat(map).containsExactly(entry("a", "b"), entry("c", "d"), entry("e", "f"));
    map.add("e", "f2");
    assertThat(map).containsExactly(entry("a", "b"), entry("c", "d"), entry("e", "f2"));
  }

  @Test
  public void testAddAll() {
    Headers map = Headers.create().add("a", "b").add("c", "d")
        .addAll(Headers.create("e", "f"));
    assertThat(map).containsExactly(entry("a", "b"), entry("c", "d"), entry("e", "f"));
  }

}
