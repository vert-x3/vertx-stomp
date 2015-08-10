package examples;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;

public class StompServerExamples {

  public void example1(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example2(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(1234, "0.0.0.0");
  }

  public void example3(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(ar -> {
          if (ar.failed()) {
            System.out.println("Failing to start the STOMP server : " + ar.cause().getMessage());
          } else {
            System.out.println("Ready to receive STOMP frames");
          }
        });
  }

  public void example4(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx, new StompServerOptions().setPort(1234).setHost("0.0.0.0"))
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example5(Vertx vertx, NetServer netServer) {
    StompServer server = Stomp.createStompServer(vertx, netServer)
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example6(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx, new StompServerOptions().setHeartbeat(
        new JsonObject().put("x", 1000).put("y", 1000)))
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example7(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx).authenticationHandler(
            (login, passcode, resultHandler) -> {
              // Don't reuse this code.
              if ("admin".equals(login) && "admin".equals(passcode)) {
                resultHandler.handle(Future.succeededFuture(true));
              } else {
                resultHandler.handle(Future.succeededFuture(false));
              }
            }))
        .listen();
  }

  public void example8(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx)
            .onAckHandler((subscription, frames) -> {
              // Action to execute when the frames (one in `client-individual` mode, several
              // in `client` mode are acknowledged.
            })
            .onNackHandler((subscription, frames) -> {
              // Action to execute when the frames (1 in `client-individual` mode, several in
              // `client` mode are not acknowledged.
            }))
        .listen();
  }

  public void example9(Vertx vertx) {
    StompServer server = Stomp.createStompServer(vertx)
        .handler(StompServerHandler.create(vertx)
                .closeHandler(connection -> {
                  // client connection closed
                })
                .beginHandler((frame, connection) -> {
                  // transaction starts
                })
                .commitHandler((frame, connection) -> {
                      // transaction committed
                    }
                )
            //...
        ).listen();
  }

  public void example10(StompServer server) {
    server.close(ar -> {
      if (ar.succeeded()) {
        System.out.println("The STOMP server has been closed");
      } else {
        System.out.println("The STOMP server failed to close : " + ar.cause().getMessage());
      }
    });
  }

}
