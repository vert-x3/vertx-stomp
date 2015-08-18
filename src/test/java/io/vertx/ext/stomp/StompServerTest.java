package io.vertx.ext.stomp;

import io.vertx.ext.stomp.utils.Server;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerTest {

  @Test
  public void checkThatVersionIsReadFromFile() {
    String server = Server.SERVER_NAME;
    System.out.println(server);
    assertThat(server).startsWith("vertx-stomp");
  }

}