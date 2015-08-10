package io.vertx.ext.stomp.utils;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

/**
 * Checks the behavior of {@link Headers}.
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