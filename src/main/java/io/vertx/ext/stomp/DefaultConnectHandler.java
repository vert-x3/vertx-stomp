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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.ext.stomp.utils.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * STOMP compliant actions executed when receiving a {@code CONNECT} frame. It may also be called when receiving a
 * {@code STOMP} frame depending on the {@link io.vertx.ext.stomp.StompServerHandler} configuration.
 * <p/>
 * This handler manages the STOMP version negotiation and authentication (if enabled). Once all the checks have been
 * passed, the {@code CONNECTED} frame is sent to the client.
 * <p/>
 * This handler is thread safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultConnectHandler implements Handler<ServerFrame> {
  @Override
  public void handle(ServerFrame sf) {
    // Server negotiation
    List<String> accepted = new ArrayList<>();
    String accept = sf.frame().getHeader(Frame.ACCEPT_VERSION);
    if (accept == null) {
      accepted.add("1.0");
    } else {
      accepted.addAll(Arrays.asList(accept.split(FrameParser.COMMA)));
    }

    String version = negotiate(accepted, sf.connection());
    if (version == null) {
      // Spec says: if the server and the client do not share any common protocol versions, then the server MUST
      // respond with an error.
      sf.connection().write(Frames.createErrorFrame(
              "Incompatible versions",
              Headers.create(
                  Frame.VERSION, getSupportedVersionsHeaderLine(sf.connection()),
                  Frame.CONTENT_TYPE, "text/plain"),
              "Client protocol requirement does not mach versions supported by the server. " +
                  "Supported protocol versions are " + getSupportedVersionsHeaderLine(sf.connection()))
      );
      sf.connection().close();
      return;
    }

    // Login / Passcode
    authenticate(sf.frame(), sf.connection(), ar -> {
      // Spec says: The server will respond back with the highest version of the protocol -> version
      sf.connection().write(new Frame(Command.CONNECTED, Headers.create(
          Frame.VERSION, version,
          Frame.SERVER, Server.SERVER_NAME,
          Frame.SESSION, sf.connection().session(),
          Frame.HEARTBEAT, Frame.Heartbeat.create(sf.connection().server().options().getHeartbeat()).toString()), null));
    });
  }

  private void authenticate(Frame frame, StompServerConnection connection,
                            Handler<AsyncResult<Void>> remainingActions) {
    if (connection.server().options().isSecured()) {
      String login = frame.getHeader(Frame.LOGIN);
      String passcode = frame.getHeader(Frame.PASSCODE);

      connection.handler().onAuthenticationRequest(connection, login, passcode).onComplete(ar -> {
        if (ar.result()) {
          remainingActions.handle(Future.succeededFuture());
        } else {
          connection.write(Frames.createErrorFrame(
                  "Authentication failed",
                  Headers.create(
                      Frame.VERSION, getSupportedVersionsHeaderLine(connection),
                      Frame.CONTENT_TYPE, "text/plain"),
                  "The connection frame does not contain valid credentials.")
          );
          connection.close();
        }
      });
    } else {
      remainingActions.handle(Future.succeededFuture());
    }
  }

  private String getSupportedVersionsHeaderLine(StompServerConnection connection) {
    StringBuilder builder = new StringBuilder();
    connection.server().options().getSupportedVersions().stream().forEach(
        v -> builder.append(builder.length() == 0 ? v : FrameParser.COMMA + v));
    return builder.toString();
  }

  private String negotiate(List<String> accepted, StompServerConnection connection) {
    List<String> supported = connection.server().options().getSupportedVersions();
    for (String v : supported) {
      if (accepted.contains(v)) {
        return v;
      }
    }
    return null;
  }
}
