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

/**
 * = Vert.x-Stomp
 * :toc: left
 *
 * STOMP is the Simple (or Streaming) Text Orientated Messaging Protocol. STOMP
 * provides an interoperable wire format so that STOMP clients can communicate with any STOMP message broker to
 * provide easy and widespread messaging interoperability among many languages, platforms and brokers. Get more details about STOMP on https://stomp.github.io/index.html.
 *
 * Vertx-Stomp is an implementation of a STOMP server and client. You can use the STOMP server with other clients and
 * use the STOMP client with other servers. The server and the client supports the version 1.0, 1.1 and 1.2 of the
 * STOMP protocol (see https://stomp.github.io/stomp-specification-1.2.html). The STOMP server can also be used as a
 * bridge with the vert.x event bus.
 *
 * == Using vertx-stomp
 *
 * To use the Vert.x Stomp server and client, add the following dependency to the _dependencies_ section of your build
 * descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * == STOMP server
 *
 * === Creating a STOMP server
 *
 * The simplest way to create an STOMP server, using all default options is as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example1}
 * ----
 *
 * This creates a STOMP server listening on `localhost:61613` that is compliant with the STOMP specification.
 *
 * You can configure the port and host in the {@link io.vertx.ext.stomp.StompServer#listen(int, java.lang.String)}
 * method:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example2}
 * ----
 *
 * To be notified when the server is ready, use a handler as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example3}
 * ----
 *
 * The handler receive a reference on the {@link io.vertx.ext.stomp.StompServer}.
 *
 * You can also configure the host and port in {@link io.vertx.ext.stomp.StompServerOptions}:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example4}
 * ----
 *
 * === Closing a STOMP server
 *
 * STOMP servers are closed as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example10}
 * ----
 *
 * === Configuration
 *
 * The {@link io.vertx.ext.stomp.StompServerOptions} let you configure some aspects of the STOMP server.
 *
 * First, the STOMP server is based on a
 * {@link io.vertx.core.net.NetServer}, so you can configure the underlying {@link io.vertx.core.net.NetServer} from
 * the {@link io.vertx.ext.stomp.StompServerOptions}. Alternatively you can also pass the
 * {@link io.vertx.core.net.NetServer} you want to use:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example5}
 * ----
 *
 * The {@link io.vertx.ext.stomp.StompServerOptions} let you configure:
 *
 * * the host and port of the STOMP server - defaults to `0.0.0.0:61613`.
 * * whether or not the STOMP server is secured - defaults to `false`
 * * the max STOMP frame body - default to 10 Mb
 * * the maximum number of headers accepted in a STOMP frame - defaults to 1000
 * * the max length of a header line in a STOMP frame - defaults to 10240
 * * the STOMP heartbeat time - default to `1000, 1000`
 * * the supported STOMP protocol versions (1.0, 1.1 and 1.2 by default)
 * * the maximum number of frame allowed in a transaction (defaults to 1000)
 * * the size of the transaction chunk - defaults to 1000 (see
 * {@link io.vertx.ext.stomp.StompServerOptions#setTransactionChunkSize(int)})
 * * the maximum number of subscriptions a client can handle - defaults to 1000
 *
 * The STOMP heartbeat is configured using a JSON object as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example6}
 * ----
 *
 * Enabling security requires an additional {@link io.vertx.ext.auth.AuthProvider} handling the
 * authentication requests:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example7}
 * ----
 *
 * More information about {@link io.vertx.ext.auth.AuthProvider} is available
 * http://vertx.io/docs/#authentication_and_authorisation[here].
 *
 * If a frame exceeds one of the size limits, the frame is rejected and the client receives an `ERROR` frame. As the
 * specification requires, the client connection is closed immediately after having sent the error. The same behavior
 * happens with the other thresholds.
 *
 * === Subscriptions
 *
 * The default STOMP server handles subscription destination as opaque Strings. So it does not promote a structure
 * and it not hierarchic. By default the STOMP server follow a _topic_ semantic (so messages are dispatched to all
 * subscribers).
 *
 * === Type of destinations
 *
 * By default, the STOMP server manages _destinations_ as topics. So messages are dispatched to all subscribers. You
 * can configure the server to use queues, or mix both types:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example11}
 * ----
 *
 * In the last example, all destination starting with `/queue` are queues while others are topics. The destination is
 * created when the first subscription on this destination is received.
 *
 * A server can decide to reject the destination creation by returning `null`:
 *
 *[source,$lang]
 * ----
 * {@link examples.StompServerExamples#example12}
 * ----
 *
 * In this case, the subscriber received an `ERROR` frame.
 *
 * Queues dispatches messages using a round-robin strategies.
 *
 * === Providing your own type of destination
 *
 * On purpose the STOMP server does not implement any advanced feature. IF you need more advanced dispatching policy,
 * you can implement your own type of destination by providing a {@link io.vertx.ext.stomp.DestinationFactory}
 * returning your own {@link io.vertx.ext.stomp.Destination} object.
 *
 * === Acknowledgment
 *
 * By default, the STOMP server does nothing when a message is not acknowledged. You can customize this by
 * providing your own {@link io.vertx.ext.stomp.Destination} implementation.
 *
 * The custom destination should call the
 *
 * {@link io.vertx.ext.stomp.StompServerHandler#onAck(io.vertx.ext.stomp.StompServerConnection, io.vertx.ext.stomp.Frame, java.util.List)}
 * and
 * {@link io.vertx.ext.stomp.StompServerHandler#onNack(io.vertx.ext.stomp.StompServerConnection, io.vertx.ext.stomp.Frame, java.util.List)}
 * method in order to let the {@link io.vertx.ext.stomp.StompServerHandler} customizes the behavior:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example8}
 * ----
 *
 * === Customizing the STOMP server
 *
 * In addition to the handlers seen above, you can configure almost all aspects of the STOMP server, such as the
 * actions made when specific frames are received, the `ping` to sent to the client (to implement the heartbeat).
 * Here are some examples:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example9}
 * ----
 *
 * Be aware that changing the default behavior may break the compliance with the STOMP specification. So, please look
 * at the default implementations.
 *
 * == STOMP client
 *
 * STOMP clients connect to STOMP server and can send and receive frames.
 *
 * === Creating a STOMP client
 *
 * You create a {@link io.vertx.ext.stomp.StompClient} instance with default options as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example1(io.vertx.core.Vertx)}
 * ----
 *
 * The previous snippet creates a STOMP client connecting to "0.0.0.0:61613". Once connected, you get a
 * {@link io.vertx.ext.stomp.StompClientConnection} that let you interact with the server. You can
 * configure the host and port as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example2(io.vertx.core.Vertx)}
 * ----
 *
 * Alternatively you can also configure the host and port in the {@link io.vertx.ext.stomp.StompClientOptions}:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example3(io.vertx.core.Vertx)}
 * ----
 *
 * === Closing a STOMP client
 *
 * You can close a STOMP client:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example4(io.vertx.core.Vertx)}
 * ----
 *
 * However, this way would not notify the server of the disconnection. To cleanly close the connection, you should
 * use the {@link io.vertx.ext.stomp.StompClientConnection#disconnect()} method:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example5(io.vertx.core.Vertx)}
 * ----
 *
 * If the heartbeat is enabled and if the client did not detect server activity after the configured timeout, the
 * connection is automatically closed.
 *
 * === Handling errors
 *
 * On the {@link io.vertx.ext.stomp.StompClientConnection}, you can register an error handler receiving `ERROR`
 * frames sent by the server. Notice that the server closes the connection with the client after having sent such frame:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example6(io.vertx.core.Vertx)}
 * ----
 *
 * The client can also be notified when a connection drop has been detected. Connection failures are detected using the
 * STOMP heartbeat mechanism. When the server has not sent a message in the heartbeat time window, the connection is
 * closed and the `connectionDroppedHandler` is called (if set). To configure a `connectionDroppedHandler`, call
 * {@link io.vertx.ext.stomp.StompClientConnection#connectionDroppedHandler(io.vertx.core.Handler)}. The handler can
 * for instance tries to reconnect to the server.
 *
 * === Configuration
 *
 * You can configure various aspect by passing a
 * {@link io.vertx.ext.stomp.StompClientOptions} when creating the {@link io.vertx.ext.stomp.StompClient}. As the
 * STOMP client relies on a {@link io.vertx.core.net.NetClient}, you can configure the underlying Net Client from
 * the {@link io.vertx.ext.stomp.StompClientOptions}. Alternatively, you can pass the {@link io.vertx.core.net.NetClient}
 * you want to use in the
 * {@link io.vertx.ext.stomp.StompClient#connect(io.vertx.core.net.NetClient, io.vertx.core.Handler)} method:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example7(io.vertx.core.Vertx, io.vertx.core.net.NetClient)}
 * ----
 *
 * The {@link io.vertx.ext.stomp.StompClientOptions} let you configure:
 *
 * * the host and port ot the STOMP server
 * * the login and passcode to connect to the server
 * * whether or not the `content-length` header should be added to the frame if not set explicitly. (enabled by default)
 * * whether or not the `STOMP` command should be used instead of the `CONNECT` command (disabled by default)
 * * whether or not the `host` header should be ignored in the `CONNECT` frame (disabled by default)
 * * the heartbeat configuration (1000, 1000 by default)
 *
 * === Subscribing to destinations
 *
 * To subscribe to a destination, use:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example8(io.vertx.core.Vertx)}
 * ----
 *
 * To unsubscribe, use:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example9(io.vertx.core.Vertx)}
 * ----
 *
 * === Sending messages
 *
 * To send a message, use:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example10(io.vertx.core.Vertx)}
 * ----
 *
 * [language,java,groovy]
 * ----
 * In Java and Groovy, you can use the {@link io.vertx.ext.stomp.utils.Headers} class to ease the header creation.
 * ----
 *
 * === Acknowledgements
 *
 * Clients can send `ACK` and `NACK` frames:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example11(io.vertx.core.Vertx)}
 * ----
 *
 * === Transactions
 *
 * Clients can also create transactions. `ACK`, `NACK` and `SEND` frames sent in the transaction will be delivery
 * only when the transaction is committed.
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example12(io.vertx.core.Vertx)}
 * ----
 *
 * === Receipt
 *
 * Each sent commands can have a _receipt_ handler, notified when the server has processed the message:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompClientExamples#example13(io.vertx.core.Vertx)}
 * ----
 *
 * == Using the STOMP server as a bridge to the vert.x Event Bus
 *
 * The STOMP server can be used as a bridge to the vert.x Event Bus. The bridge is bi-directional meaning the STOMP
 * frames are translated to Event Bus messages and Event Bus messages are translated to STOMP frames.
 *
 * To enable the bridge you need to configure the inbount and outbound addresses. Inbound addresses are STOMP
 * destination that are transfered to the event bus. The STOMP destination is used as the event bus adress. Outbound
 * addresses are event bus addresses that are tranfered to STOMP.
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example13(io.vertx.core.Vertx)}
 * ----
 *
 * By default, the bridge use a publish/subscribe delivery (topic). You can configure it to use a point to point
 * delivery where only one STOMP client or Event Bus consumer is invoked:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example14(io.vertx.core.Vertx)}
 * ----
 *
 */
@ModuleGen(name = "vertx-stomp", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
