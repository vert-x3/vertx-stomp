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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.stomp.Acknowledgement;
import io.vertx.ext.stomp.Frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of {@link io.vertx.ext.stomp.Acknowledgement}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public class AcknowledgementImpl implements Acknowledgement {

  private final Frame subscription;
  private final List<Frame> frames;

  public AcknowledgementImpl(Frame subscription, List<Frame> frames) {
    this.subscription = subscription;
    this.frames = new ArrayList<>(frames);
  }

  public Frame subscription() {
    return subscription;
  }

  public List<Frame> frames() {
    return frames;
  }
}
