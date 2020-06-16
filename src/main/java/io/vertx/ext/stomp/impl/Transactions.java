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

package io.vertx.ext.stomp.impl;

import io.vertx.ext.stomp.StompServerConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the active transactions of the STOMP server.
 * Transactions are not shared between server instances, as transactions are 'attached' to a STOMP client (i.e.
 * connection). So 2 connections cannot use the same transaction object.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Transactions {

  private final static Transactions INSTANCE = new Transactions();

  private final List<Transaction> transactions = new ArrayList<>();

  public static Transactions instance() {
    return INSTANCE;
  }

  private Transactions() {
    // Avoid direct instantiation.
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

}
