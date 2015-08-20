package io.vertx.ext.stomp.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Stomp;
import io.vertx.ext.stomp.StompClientConnection;
import io.vertx.ext.stomp.utils.Headers;

/**
 * A verticle connecting to a STOMP server and sending message in a transaction.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class TxSenderStompClient extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    Stomp.createStompClient(vertx).connect(ar -> {
      final StompClientConnection connection = ar.result();
      connection.errorHandler(frame -> System.err.println("Tx Sender has received an ERROR frame : \n" + frame));
      connection.beginTX("my-transaction");
      connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Hello"));
      connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("My"));
      connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Name"));
      connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Is"));
      connection.send("/queue", Headers.create(Frame.TRANSACTION, "my-transaction"), Buffer.buffer("Vert.x"));
      connection.commit("my-transaction");
      connection.disconnect();
    });
  }
}
