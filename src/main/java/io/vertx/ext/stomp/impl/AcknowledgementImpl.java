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
