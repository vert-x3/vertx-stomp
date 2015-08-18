package io.vertx.ext.stomp;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Frames;
import io.vertx.ext.stomp.ServerFrameHandler;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.ext.stomp.utils.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * STAMP compliant actions executed when receiving a {@code CONNECT} frame. It may also be called when receiving a
 * {@code STOMP} frame depending on the {@link io.vertx.ext.stomp.StompServerHandler} configuration.
 * <p/>
 * This handler manages the STOMP version negotiation and authentication (if enabled). Once all the checks have been
 * passed, the {@code CONNECTED} frame is sent to the client.
 *
 * This handler is thread safe.
 */
public class DefaultConnectHandler implements ServerFrameHandler {
  @Override
  public void onFrame(Frame frame, StompServerConnection connection) {
    // Server negotiation
    List<String> accepted = new ArrayList<>();
    String accept = frame.getHeader(Frame.ACCEPT_VERSION);
    if (accept == null) {
      accepted.add("1.0");
    } else {
      accepted.addAll(Arrays.asList(accept.split(FrameParser.COMMA)));
    }

    String version = negotiate(accepted, connection);
    if (version == null) {
      // Spec says: if the server and the client do not share any common protocol versions, then the server MUST
      // respond with an error.
      connection.write(Frames.createErrorFrame(
              "Incompatible versions",
              Headers.create(
                  Frame.VERSION, getSupportedVersionsHeaderLine(connection),
                  Frame.CONTENT_TYPE, "text/plain"),
              "Client protocol requirement does not mach versions supported by the server. " +
                  "Supported protocol versions are " + getSupportedVersionsHeaderLine(connection))
      );
      connection.close();
      return;
    }

    // Login / Passcode
    authenticate(frame, connection, ar -> {
      // Spec says: The server will respond back with the highest version of the protocol -> version
      connection.write(new Frame(Frame.Command.CONNECTED, Headers.create(
          Frame.VERSION, version,
          Frame.SERVER, Server.SERVER_NAME,
          Frame.SESSION, connection.session(),
          Frame.HEARTBEAT, Frame.Heartbeat.create(connection.server().getOptions().getHeartbeat()).toString()), null));
    });
  }

  private void authenticate(Frame frame, StompServerConnection connection,
                            Handler<AsyncResult<Void>> remainingActions) {
    if (connection.server().getOptions().isSecured()) {
      String login = frame.getHeader(Frame.LOGIN);
      String passcode = frame.getHeader(Frame.PASSCODE);

      connection.handler().onAuthenticationRequest(connection.server(), login, passcode, ar -> {
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
    connection.server().getOptions().getSupportedVersions().stream().forEach(
        v -> builder.append(builder.length() == 0 ? v : FrameParser.COMMA + v));
    return builder.toString();
  }

  private String negotiate(List<String> accepted, StompServerConnection connection) {
    List<String> supported = connection.server().getOptions().getSupportedVersions();
    for (String v : supported) {
      if (accepted.contains(v)) {
        return v;
      }
    }
    return null;
  }
}
