package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Handler called when an authentication is required by the server. Authentication happen during the {@code CONNECT /
 * CONNECTED} phase. The {@code CONNECT} frame should includes the {@code login} and {@code passcode} header
 * (configured from the client option. The login and passcode are passed to the handler than can check whether or not
 * the access is granted. When the decision has been made, it must called the {@code resultHandler} with the value
 * {@code true} if the access if granted, {@code false} otherwise.
 */
@VertxGen
public interface AuthenticationHandler {

  /**
   * The authentication handler responsible for checking whether the couple {@code login/passcode} are valid to
   * connect to this server.
   *
   * @param login         the login
   * @param passcode      the password
   * @param resultHandler the result handler invoked when the decision has been made. It receives {@code true} if the
   *                      access is granted, {@code false} otherwise.
   */
  void authenticate(String login, String passcode, Handler<AsyncResult<Boolean>> resultHandler);

}
