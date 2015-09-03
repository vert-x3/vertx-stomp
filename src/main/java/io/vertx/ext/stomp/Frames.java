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

package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.impl.FrameException;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;

import java.util.Map;
import java.util.Objects;

/**
 * Utility methods to build common {@link Frame}s. It defines a non-STOMP frame ({@code PING}) that is used for
 * heartbeats. When such frame is written on the wire it is just the {@code 0} byte.
 * <p/>
 * This class is thread-safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface Frames {

  Frame PING = new Frame(Frame.Command.PING, Headers.create(), null) {

    @Override
    public Buffer toBuffer() {
      return Buffer.buffer(FrameParser.EOL);
    }
  };

  static Frame createErrorFrame(String message, Map<String, String> headers, String body) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(headers);
    Objects.requireNonNull(body);
    return new Frame(Frame.Command.ERROR,
        Headers.create(headers)
            .add(Frame.MESSAGE, message)
            .add(Frame.CONTENT_LENGTH, Integer.toString(body.length()))
            .add(Frame.CONTENT_TYPE, "test/plain"),
        Buffer.buffer(body));
  }

  @GenIgnore
  static Frame createInvalidFrameErrorFrame(FrameException exception) {
    return createErrorFrame("Invalid frame received", Headers.create(), exception.getMessage());
  }

  static Frame createReceiptFrame(String receiptId, Map<String, String> headers) {
    Objects.requireNonNull(receiptId);
    Objects.requireNonNull(headers);
    return new Frame(Frame.Command.RECEIPT,
        Headers.create(headers)
            .add(Frame.RECEIPT_ID, receiptId),
        null);
  }

  static void handleReceipt(Frame frame, StompServerConnection connection) {
    String receipt = frame.getReceipt();
    if (receipt != null) {
      connection.write(createReceiptFrame(receipt, Headers.create()));
    }
  }

  static Frame ping() {
    return PING;
  }
}
