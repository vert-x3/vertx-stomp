package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An utility class to wait for success.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AsyncLock<T> {

  private final CountDownLatch latch;
  private final AtomicReference<AsyncResult<T>> reference = new AtomicReference<>();

  public AsyncLock() {
    latch = new CountDownLatch(1);
  }

  public Handler<AsyncResult<T>> handler() {
    return (ar) -> {
      reference.set(ar);
      latch.countDown();
    };
  }

  public void waitForSuccess() {
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // interrupted
      Thread.currentThread().interrupt();
    }

    final AsyncResult<T> result = reference.get();
    if (result == null) {
      throw new AssertionError("Result not received after timeout");
    }

    if (result.failed()) {
      result.cause().printStackTrace();
      throw new AssertionError("Received a failed result " + result.cause().getMessage());
    }

  }

}
