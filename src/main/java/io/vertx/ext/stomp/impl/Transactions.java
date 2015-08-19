package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.StompServerConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the active transactions of the STOMP server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Transactions {

  public static final Transactions INSTANCE = new Transactions();

  private List<Transaction> transactions = new ArrayList<>();

  private Transactions() {
    // avoid direct instantiation.
  }

  /**
   * Gets an active transaction.
   *
   * @param connection the connection
   * @param txId       the transaction id
   * @return the transaction object, {@code null} if not found
   */
  public synchronized Transaction getTransaction(StompServerConnection connection, String txId) {
    return transactions.stream()
        .filter(t -> t.connection().equals(connection) && t.id().equals(txId))
        .findFirst().orElse(null);
  }

  /**
   * Registers a transaction.
   *
   * @param connection the connection
   * @param txId       the transaction id
   * @return {@code true} if the registration succeed, {@code false} otherwise. The main reason of failure is the
   * non-uniqueness of the transaction id for a given client / connection
   */
  public synchronized boolean registerTransaction(StompServerConnection connection, String txId) {
    if (getTransaction(connection, txId) != null) {
      return false;
    }
    transactions.add(new Transaction(connection, txId));
    return true;
  }

  /**
   * Checks whether the given connection has registered a connection with the given id.
   *
   * @param connection the connection used by the transaction
   * @param id         the id of the transaction
   * @return {@code true} if the transaction is active, {@code false} otherwise
   */
  public synchronized boolean isTransactionActive(StompServerConnection connection, String id) {
    return getTransaction(connection, id) != null;
  }


  /**
   * Unregisters a transaction
   *
   * @param connection the connection used by the transaction
   * @param id         the id of the transaction
   * @return {@code true} if the transaction is unregistered correctly, {@code false} otherwise.
   */
  public synchronized boolean unregisterTransaction(StompServerConnection connection, String id) {
    Transaction transaction = getTransaction(connection, id);
    return transaction != null && transactions.remove(transaction);
  }

  /**
   * Unregisters all transactions from the given connection / client.
   *
   * @param connection the connection
   */
  public synchronized void unregisterTransactionsFromConnection(StompServerConnection connection) {
    transactions.stream()
        .filter(transaction -> transaction.connection().equals(connection))
        .sorted() // Avoid using baking up collection.
        .forEach(transactions::remove);
  }

  /**
   * Gets the number of transaction handled by the server.
   *
   * @return the number of transaction, {@code 0} if none.
   */
  public synchronized int getTransactionCount() {
    return transactions.size();
  }

  /**
   * Adds the given frame to the transaction.
   *
   * @param connection the connection
   * @param txId       the transaction id
   * @param frame      the frame to be added
   * @return {@code true} if the frame was added, {@code false} otherwise. {@code false} may mean that the
   * transaction does not exits, or the frame cannot be added because the transaction have reached the sie limit.
   */
  public synchronized boolean addFrameToTransaction(StompServerConnection connection, String txId, Frame frame) {
    Transaction transaction = getTransaction(connection, txId);
    return transaction != null && transaction.addFrameToTransaction(frame);
  }


}
