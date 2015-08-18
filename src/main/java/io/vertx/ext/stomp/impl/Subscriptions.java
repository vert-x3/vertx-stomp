package io.vertx.ext.stomp.impl;

import io.vertx.core.shareddata.Shareable;
import io.vertx.ext.stomp.Subscription;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shared set of subscriptions, generally using the same destination.
 * This class is thread-safe and can be shared in a local map.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Subscriptions extends CopyOnWriteArrayList<Subscription> implements Shareable, List<Subscription> {

}
