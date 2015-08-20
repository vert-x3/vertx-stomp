package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Destination;

import java.util.List;

/**
 * Some helper methods.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Helper {
  public static boolean hasDestination(List<Destination> destinations, String dest) {
    for (Destination d : destinations) {
      if (d.destination().equals(dest)) {
        return true;
      }
    }
    return false;
  }
}
