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

package examples;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.*;

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

  public void example13(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/toBus"))
                .addOutboundPermitted(new PermittedOptions().setAddress("/toStomp"))
            )
        )
        .listen();
  }

  public void example14(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
                .bridge(new BridgeOptions()
                        .addInboundPermitted(new PermittedOptions().setAddress("/toBus"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("/toStomp"))
                        .setPointToPoint(true)
                )
        )
        .listen();
  }

  public void example15(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx)
            .bridge(new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("/toBus")
                    .setMatch(new JsonObject().put("foo", "bar")))
                .addOutboundPermitted(new PermittedOptions().setAddress("/toStomp"))
                .setPointToPoint(true)
            )
        )
        .listen();
  }

  public void example16(Vertx vertx) {
    StompServer server = StompServer.create(vertx, new StompServerOptions()
        .setPort(-1) // Disable the TCP port, optional
        .setWebsocketBridge(true) // Enable the web socket support
        .setWebsocketPath("/stomp")) // Configure the web socket path, /stomp by default
        .handler(StompServerHandler.create(vertx));

    Future<HttpServer> http = vertx.createHttpServer(
        new HttpServerOptions().setWebsocketSubProtocols("v10.stomp, v11.stomp")
    )
        .websocketHandler(server.webSocketHandler())
        .listen(8080);
  }

  public void example17(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx).receivedFrameHandler(sf -> {
          System.out.println(sf.frame());
        }))
        .listen();

    StompClient client = StompClient.create(vertx).receivedFrameHandler(frame -> System.out.println(frame));
  }

  public void example18(Vertx vertx) {
    StompServer server = StompServer.create(vertx)
        .handler(StompServerHandler.create(vertx))
        .writingFrameHandler(sf -> {
          System.out.println(sf.frame());
        })
        .listen();

    StompClient client = StompClient.create(vertx).writingFrameHandler(frame -> {
      System.out.println(frame);
    });
  }

}
