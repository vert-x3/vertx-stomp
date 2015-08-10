package io.vertx.ext.stomp;

import io.vertx.ext.stomp.utils.Server;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class StompServerTest {

  @Test
  public void checkThatVersionIsReadFromFile() {
    String server = Server.SERVER_NAME;
    System.out.println(server);
    assertThat(server).startsWith("vertx-stomp");
  }

}