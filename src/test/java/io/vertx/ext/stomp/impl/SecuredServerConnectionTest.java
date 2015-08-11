package io.vertx.ext.stomp.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests STOMP server with security.
 */
@RunWith(io.vertx.ext.unit.junit.VertxUnitRunner.class)
public class SecuredServerConnectionTest {
  public static final String ADMIN = "admin";
  private Vertx vertx;
  private StompServer server;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    AsyncLock<StompServer> lock = new AsyncLock<>();
    server = Stomp.createStompServer(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx)
            .authenticationHandler((login, passcode, resultHandler) -> {
              if (ADMIN.equals(login) && ADMIN.equals(passcode)) {
                resultHandler.handle(Future.succeededFuture(true));
              } else {
                resultHandler.handle(Future.succeededFuture(false));
              }
            }))
        .listen(lock.handler());
    lock.waitForSuccess();
  }

  @After
  public void tearDown(TestContext context) {
    AsyncLock<Void> lock = new AsyncLock<>();
    server.close(lock.handler());
    lock.waitForSuccess();

    lock = new AsyncLock<>();
    vertx.close(lock.handler());
    lock.waitForSuccess();
  }

  @Test
  public void testAuthenticatedConnection(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.getPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\npasscode:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testFailedAuthentication(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.getPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("Authentication failed"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\npasscode:nope\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test(timeout = 5000)
  public void testFailedAuthenticationBecauseOfMissingHeaders(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.getPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("ERROR"));
        context.assertTrue(buffer.toString().contains("Authentication failed"));
        async.complete();
      });
      socket.write("CONNECT\n" + "accept-version:1.2\nlogin:admin\n" + "\n" + FrameParser.NULL);
    });
  }

  @Test
  public void testAuthenticatedConnectionWithStompFrame(TestContext context) {
    Async async = context.async();
    vertx.createNetClient().connect(server.getPort(), "0.0.0.0", result -> {
      if (result.failed()) {
        context.fail("Connection failed");
        return;
      }
      NetSocket socket = result.result();
      socket.handler(buffer -> {
        context.assertTrue(buffer.toString().contains("CONNECTED"));
        context.assertTrue(buffer.toString().contains("version:1.2"));
        async.complete();
      });
      socket.write("STOMP\n" + "accept-version:1.2\nlogin:admin\npasscode:admin\n" + "\n" + FrameParser.NULL);
    });
  }


}
