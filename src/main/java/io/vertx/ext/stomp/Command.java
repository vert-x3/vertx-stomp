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

import io.vertx.codegen.annotations.VertxGen;

/**
 * The list of command defined by the STOMP specification.
 * It also contains a {@code PING} command used for heartbeat. It should not be used in frames (as it's not a valid
 * command).
 */
@VertxGen
public enum Command {
  // Connection
  CONNECT,
  CONNECTED,
  STOMP,

  // Client
  SEND,
  SUBSCRIBE,
  UNSUBSCRIBE,
  ACK,
  NACK,
  BEGIN,
  COMMIT,
  ABORT,
  DISCONNECT,

  // Server
  MESSAGE,
  RECEIPT,
  ERROR,

  // This is not a real STOMP frame, it just notice a ping frame from the client.
  PING,

  UNKNOWN
}
