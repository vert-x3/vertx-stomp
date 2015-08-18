/**
 * = Vert.x-Stomp
 * :toc: left
 *
 * STOMP (https://stomp.github.io/index.html) is the Simple (or Streaming) Text Orientated Messaging Protocol. STOMP
 * provides an interoperable wire format so that STOMP clients can communicate with any STOMP message broker to
 * provide easy and widespread messaging interoperability among many languages, platforms and brokers.
 *
 * Vertx-Stomp is an implementation of a STOMP server and client. You can use the STOMP server with other clients and
 * use the STOMP client with other servers. The server and the client supports the version 1.0, 1.1 and 1.2 of the
 * STOMP protocol (see https://stomp.github.io/stomp-specification-1.2.html).
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
 *   <groupId>{maven-groupId}</groupId>
 *   <artifactId>{maven-artifactId}</artifactId>
 *   <version>{maven-version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile {maven-groupId}:{maven-artifactId}:{maven-version}
 * ----
 *
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
 * To be notified when the server is ready, pass a handler as follows:
 *
 * [source,$lang]
 * ----
 * {@link examples.StompServerExamples#example3}
 * ----
 *
 * You can also pass the host and port in {@link io.vertx.ext.stomp.StompServerOptions}:
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
 * * the _acknowledgement timeout_ (time before a message is considered not-acknowledged) - default to 10 seconds
 * * the supported STOMP protocol versions (1.0, 1.1 and 1.2 by default)
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
 * If a frame exceeds on of the size limits, the frame is rejected and the client receives an `ERROR` frame. As the
 * specification requires, the client connection is closed immediately after having sent the error.
 *
 * === Subscriptions
 *
 * The default STOMP server handles subscription destination as opaque Strings. So it does not promote a structure
 * and it not hierarchic.
 *
 * === Acknowledgment
 *
 * Messages requiring acknowledgment are placed in a queue. If the acknowledgment does not happen in time (the
 * _acknowledgement timeout_), the message is considered as non-acknowledged. By default, the STOMP server does
 * nothing (except writing a log message) when a message is not acknowledged. You can customize this using a specific
 * handler:
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
 * STOMP clients connect to STOMP server and can sends and receive frames.
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
 * the previous snippet creates a STOMP client connecting to "0.0.0.0:61613". Once connected, you get a
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
 */
@GenModule(name = "vertx-stomp")
@Document(fileName = "index.adoc")
package io.vertx.ext.stomp;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;