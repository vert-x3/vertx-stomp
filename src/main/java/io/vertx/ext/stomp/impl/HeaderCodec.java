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

/**
 * Class responsible for the encoding and decoding of the STOMP frame headers.
 * This class is thread-safe.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HeaderCodec {

  private static final String ESCAPE_ESCAPE = String.valueOf(new char[]{(char) 92, (char) 92});
  private static final String COLON_ESCAPE = String.valueOf(new char[]{(char) 92, (char) 99});
  private static final String LINE_FEED_ESCAPE = String.valueOf(new char[]{(char) 92, (char) 110});
  private static final String CARRIAGE_RETURN_ESCAPE = String.valueOf(new char[]{(char) 92, (char) 114});

  private HeaderCodec() {
    //Avoid direct instantiation.
  }

  public static String encode(String header, boolean connectOrConnectedFrame) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < header.length(); i++) {
      char value = header.charAt(i);
      switch (value) {
        case FrameParser.ESCAPE:
          // Always encoded.
          builder.append(ESCAPE_ESCAPE);
          break;
        case FrameParser.LINE_FEED:
          if (connectOrConnectedFrame) {
            builder.append(value);
          } else {
            builder.append(LINE_FEED_ESCAPE);
          }
          break;
        case ':':
          if (connectOrConnectedFrame) {
            builder.append(value);
          } else {
            builder.append(COLON_ESCAPE);
          }
          break;
        case '\r':
          if (connectOrConnectedFrame) {
            builder.append(value);
          } else {
            builder.append(CARRIAGE_RETURN_ESCAPE);
          }
          break;
        default:
          builder.append(value);
      }
    }
    return builder.toString();
  }

  public static String decode(String header, boolean connectOrConnectedFrame) {
    StringBuilder builder = new StringBuilder();

    int i = 0;
    while (i < header.length()) {
      char value = header.charAt(i);
      if (value == 92 && i + 1 < header.length()) {
        char next = header.charAt(i + 1);
        switch (next) {
          case 114:
            if (connectOrConnectedFrame) {
              builder.append(value);
            } else {
              builder.append(FrameParser.CARRIAGE_RETURN);
              i++;
            }
            break;
          case 110:
            if (connectOrConnectedFrame) {
              builder.append(value);
            } else {
              builder.append(FrameParser.LINE_FEED);
              i++;
            }
            break;
          case 99:
            if (connectOrConnectedFrame) {
              builder.append(value);
            } else {
              builder.append(FrameParser.COLON);
              i++;
            }
            break;
          case 92:
            // Always decoded.
            builder.append(FrameParser.ESCAPE);
            i++;
            break;
          default:
            // By spec, all other escape must be treated as a fatal protocol error.
            throw new FrameException("Incorrect header value " +
                "- the header uses an illegal escaped character '" + next + "' (" + (byte) next + ")");
        }
      } else {
        builder.append(value);
      }
      i++;
    }
    return builder.toString();
  }
}
