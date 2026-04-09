/*
 * Copyright (c) 2011-2026 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.stomp;

/**
 * Defines the type of WebSocket frames to use when sending STOMP messages over WebSocket.
 * <p>
 * This configuration allows choosing between text and binary WebSocket frames for STOMP message transmission.
 * The choice affects compatibility with different STOMP clients:
 * <ul>
 *   <li>{@link #TEXT} - Uses text WebSocket frames. Compatible with JavaScript STOMP clients (e.g. StompJS).
 *       This is the recommended default for most use cases as STOMP is a text-based protocol.</li>
 *   <li>{@link #BINARY} - Uses binary WebSocket frames. This was the legacy behavior and may be needed for
 *       backward compatibility with existing applications that expect binary frames.</li>
 * </ul>
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public enum WebSocketFrameType {
  /**
   * Use text WebSocket frames for STOMP messages.
   * <p>
   * This is the recommended option for maximum compatibility with JavaScript STOMP clients
   * and aligns with STOMP being a text-oriented protocol.
   * <p>
   * Note: When using text frames, STOMP message bodies containing binary data must be UTF-8 safe
   * or should be Base64 encoded at the application level.
   */
  TEXT,

  /**
   * Use binary WebSocket frames for STOMP messages.
   * <p>
   * This was the legacy behavior in earlier versions. It handles any content (text or binary)
   * but may not be compatible with some JavaScript STOMP clients that expect text frames.
   * <p>
   * Use this option for backward compatibility with existing applications.
   */
  BINARY
}
