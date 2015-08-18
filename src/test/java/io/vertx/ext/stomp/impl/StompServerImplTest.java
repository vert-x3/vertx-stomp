package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * test the {@link StompServerImpl}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class StompServerImplTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    vertx.close();
  }

  @Test
  public void testStartStop(TestContext context) {
    final Async async = context.async();
    Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(ar -> {
          ensureListening(context, ar);
          ar.result().close(ar2 -> {
            ensureClosed(context, ar2, ar.result());
            async.complete();
          });
        });
  }


  @Test
  public void testStartReceiveStop(TestContext context) {
    final Async async = context.async();
    StompServer server = Stomp.createStompServer(vertx);
    server.handler(StompServerHandler.create(vertx)
            .connectHandler(
                sf -> {
                  Frame frame = sf.frame();
                  context.assertTrue(frame.getCommand() == Frame.Command.CONNECT);
                  context.assertTrue(frame.getHeader("login").equals("system"));
                  server.close(ar2 -> {
                    ensureClosed(context, ar2, server);
                    async.complete();
                  });
                }
            )
    ).listen(ar -> {
      ensureListening(context, ar);
      writeMessage(vertx);
    });
  }

  @Test
  public void testWithStompServerHandler(TestContext context) {
    final Async async = context.async();
    StompServer server = Stomp.createStompServer(vertx);
    server.handler(
        StompServerHandler.create(vertx).connectHandler(
            sf -> {
              Frame frame = sf.frame();
              context.assertTrue(frame.getCommand() == Frame.Command.CONNECT);
              context.assertTrue(frame.getHeader("login").equals("system"));
              server.close(ar2 -> {
                ensureClosed(context, ar2, server);
                async.complete();
              });
            }
        )).listen(ar -> {
      ensureListening(context, ar);
      writeMessage(vertx);
    });
  }

  private void writeMessage(Vertx vertx) {
    vertx.createNetClient().connect(StompServerOptions.DEFAULT_STOMP_PORT, "0.0.0.0",
        ar -> ar.result().write("CONNECT\n" + "login:system\n" + "passcode:manager\n\n" + FrameParser.NULL));
  }

  private void ensureClosed(TestContext context, AsyncResult<Void> ar, StompServer server) {
    context.assertTrue(ar.succeeded());
    context.assertFalse(server.isListening());
  }

  private void ensureListening(TestContext context, AsyncResult<StompServer> ar) {
    context.assertTrue(ar.succeeded());
    context.assertNotNull(ar.result());
    context.assertTrue(ar.result().isListening());
  }

}