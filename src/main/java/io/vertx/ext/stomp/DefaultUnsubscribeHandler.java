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

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.impl.VertxInternal;
import io.vertx.ext.stomp.utils.Headers;

import java.util.ArrayList;
import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code UNSUBSCRIBE} frame.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultUnsubscribeHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame serverFrame) {
    Frame frame = serverFrame.frame();
    StompServerConnection connection = serverFrame.connection();
    final String id = frame.getHeader(Frame.ID);

    if (id == null) {
      connection.write(
          Frames.createErrorFrame(
              "Invalid unsubscribe",
              Headers.create(frame.getHeaders()),
              "The 'id' header must be set"));
      connection.close();
      return;
    }

    List<Destination> destinations = new ArrayList<>(connection.handler().getDestinations());
    unsub(connection, frame, 0, destinations);
  }

  private void unsub(StompServerConnection connection, Frame frame, int index, List<Destination> destinations) {
    if (index < destinations.size()) {
      Destination dst = destinations.get(index);
      Promise<Void> promise = ((VertxInternal)connection.server().vertx()).promise();
      dst.unsubscribe(connection, frame, promise);
      promise.future().onComplete(ar -> {
        if (ar.succeeded()) {
          Frames.handleReceipt(frame, connection);
        } else {
          unsub(connection, frame, index + 1, destinations);
        }
      });
    } else {
      connection.write(Frames.createErrorFrame(
        "Invalid unsubscribe",
        Headers.create(frame.getHeaders()),
        "No subscription associated with the given 'id'"));
      connection.close();
    }
  }

}
