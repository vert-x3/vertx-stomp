package io.vertx.ext.stomp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Class responsible for the computation of the server id. From the STOMP specification, this id must be constructed as
 * follows: name/version comments.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Server {

  public static final String SERVER_NAME;

  static {
    try (InputStream is = Server.class.getClassLoader().getResourceAsStream("vertx-stomp-version.txt")) {
      if (is == null) {
        throw new IllegalStateException("Cannot find vertx-version.txt on classpath");
      }
      try (Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A")) {
        SERVER_NAME = "vertx-stomp" + (scanner.hasNext() ? "/" + scanner.next() : "");
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

}
