package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompServerExamples {

  public void example1(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen();
  }


  public void example2(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .listen(1234, "0.0.0.0");
  }

  public void example3(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
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
    StompServer server = StompServer.create(vertx, new StompServerOptions().setPort(1234).setHost("0.0.0.0"))
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example5(Vertx vertx, NetServer netServer) {
    StompServer server = StompServer.create(vertx, netServer)
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example6(Vertx vertx) {
    StompServer server = StompServer.create(vertx, new StompServerOptions().setHeartbeat(
        new JsonObject().put("x", 1000).put("y", 1000)))
        .handler(StompServerHandler.create(vertx))
        .listen();
  }

  public void example7(Vertx vertx, AuthProvider provider) {
    StompServer server = StompServer.create(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx).authProvider(provider))
        .listen();
  }

  public void example8(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .onAckHandler(acknowledgement -> {
              // Action to execute when the frames (one in `client-individual` mode, several
              // in `client` mode are acknowledged.
            })
            .onNackHandler(acknowledgement -> {
              // Action to execute when the frames (1 in `client-individual` mode, several in
              // `client` mode are not acknowledged.
            }))
        .listen();
  }

  public void example9(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
                .closeHandler(connection -> {
                  // client connection closed
                })
                .beginHandler(frame -> {
                  // transaction starts
                })
                .commitHandler(frame -> {
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

  public void example11(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .destinationFactory((v, name) -> {
              if (name.startsWith("/queue")) {
                return Destination.queue(vertx, name);
              } else {
                return Destination.topic(vertx, name);
              }
            }))
        .listen();
  }

  public void example12(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .destinationFactory((v, name) -> {
              if (name.startsWith("/forbidden")) {
                return null;
              } else if (name.startsWith("/queue")) {
                return Destination.queue(vertx, name);
              } else {
                return Destination.topic(vertx, name);
              }
            }))
        .listen();
  }

}
