package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.stomp.impl.TransactionImpl;

import java.util.List;

/**
 * Represents a transaction in the STOMP server.
 */
@VertxGen
public interface Transaction {

  /**
   * Creates an instance of {@link Transaction} using the default implementation.
   *
   * @param connection the connection (client)
   * @param id         the transaction id
   * @return the created {@link Transaction}
   */
  static Transaction create(StompServerConnection connection, String id) {
    return new TransactionImpl(connection, id);
  }

  /**
   * @return the connection
   */
  StompServerConnection connection();

  /**
   * @return the transaction id
   */
  String id();

  /**
   * Adds a frame to the transaction. By default, only {@code SEND, ACK and NACK} frames can be in transactions.
   *
   * @param frame the frame to add
   * @return {@code true} if the frame was added, {@code false} otherwise. Main failure reason is the number of
   * frames stored in the transaction that have exceed the number of allowed frames in transaction.
   */
  boolean addFrameToTransaction(Frame frame);

  /**
   * Clears the list of frames added to the transaction.
   *
   * @return the current {@link Transaction}
   */
  @Fluent
  Transaction clear();

  /**
   * @return the ordered list of frames added to the transaction.
   */
  List<Frame> getFrames();

}
