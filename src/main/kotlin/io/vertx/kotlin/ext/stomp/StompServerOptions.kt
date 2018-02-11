package io.vertx.kotlin.ext.stomp

import io.vertx.ext.stomp.StompServerOptions
import io.vertx.core.http.ClientAuth
import io.vertx.core.net.JdkSSLEngineOptions
import io.vertx.core.net.JksOptions
import io.vertx.core.net.OpenSSLEngineOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.core.net.PemTrustOptions
import io.vertx.core.net.PfxOptions

/**
 * A function providing a DSL for building [io.vertx.ext.stomp.StompServerOptions] objects.
 *
 * STOMP Server options. You can also configure the Net Server used by the STOMP server from these options.
 *
 * @param acceptBacklog 
 * @param clientAuth 
 * @param clientAuthRequired 
 * @param crlPaths 
 * @param crlValues 
 * @param enabledCipherSuites 
 * @param enabledSecureTransportProtocols 
 * @param heartbeat  Sets the heartbeat configuration.
 * @param host 
 * @param idleTimeout 
 * @param jdkSslEngineOptions 
 * @param keyStoreOptions 
 * @param logActivity 
 * @param maxBodyLength  Sets the max body length accepted by the server. 10 Mb by default.
 * @param maxFrameInTransaction  Sets the maximum number of frame that can be added in a transaction. If the number of frame added to a transaction exceeds this threshold, the client receives an <code>ERROR</code> frame and is disconnected. The default is 1000.
 * @param maxHeaderLength  Sets the max header length.
 * @param maxHeaders  Sets the maximum number of headers. 1000 by default.
 * @param maxSubscriptionsByClient  Sets the maximum of subscriptions a client is allowed to register. If a client exceeds this number, it receives an error and the connection is closed.
 * @param openSslEngineOptions 
 * @param pemKeyCertOptions 
 * @param pemTrustOptions 
 * @param pfxKeyCertOptions 
 * @param pfxTrustOptions 
 * @param port  Sets the port on which the server is going to listen for TCP connection.
 * @param receiveBufferSize 
 * @param reuseAddress 
 * @param reusePort 
 * @param secured  Enables or disables the server security feature. It requires an [io.vertx.ext.auth.AuthProvider] handler.
 * @param sendBufferSize 
 * @param sendErrorOnNoSubscriptions  Sets whether or not an error is sent to the client when this client sends a message to an not subscribed destinations (no subscriptions on this destination).
 * @param sni 
 * @param soLinger 
 * @param ssl 
 * @param supportedVersions  Sets the STOMP protocol versions supported by the server. Versions must be given in the decreasing order.
 * @param tcpCork 
 * @param tcpFastOpen 
 * @param tcpKeepAlive 
 * @param tcpNoDelay 
 * @param tcpQuickAck 
 * @param timeFactor  Sets the time factor.
 * @param trafficClass 
 * @param trailingLine  Sets whether or not an empty line should be appended to the written STOMP frame. This option is disabled by default. This option is not compliant with the STOMP specification, and so is not documented on purpose.
 * @param transactionChunkSize  Sets the chunk size when replaying a transaction. To avoid blocking the event loop for too long, large transactions are split into chunks, replayed one by one. This settings sets the chunk size.
 * @param trustStoreOptions 
 * @param useAlpn 
 * @param usePooledBuffers 
 * @param websocketBridge  Enables or disables the web socket bridge.
 * @param websocketPath  Sets the websocket path. Only frames received on this path would be considered as STOMP frame.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.stomp.StompServerOptions original] using Vert.x codegen.
 */
fun StompServerOptions(
  acceptBacklog: Int? = null,
  clientAuth: ClientAuth? = null,
  clientAuthRequired: Boolean? = null,
  crlPaths: Iterable<String>? = null,
  crlValues: Iterable<io.vertx.core.buffer.Buffer>? = null,
  enabledCipherSuites: Iterable<String>? = null,
  enabledSecureTransportProtocols: Iterable<String>? = null,
  heartbeat: io.vertx.core.json.JsonObject? = null,
  host: String? = null,
  idleTimeout: Int? = null,
  jdkSslEngineOptions: io.vertx.core.net.JdkSSLEngineOptions? = null,
  keyStoreOptions: io.vertx.core.net.JksOptions? = null,
  logActivity: Boolean? = null,
  maxBodyLength: Int? = null,
  maxFrameInTransaction: Int? = null,
  maxHeaderLength: Int? = null,
  maxHeaders: Int? = null,
  maxSubscriptionsByClient: Int? = null,
  openSslEngineOptions: io.vertx.core.net.OpenSSLEngineOptions? = null,
  pemKeyCertOptions: io.vertx.core.net.PemKeyCertOptions? = null,
  pemTrustOptions: io.vertx.core.net.PemTrustOptions? = null,
  pfxKeyCertOptions: io.vertx.core.net.PfxOptions? = null,
  pfxTrustOptions: io.vertx.core.net.PfxOptions? = null,
  port: Int? = null,
  receiveBufferSize: Int? = null,
  reuseAddress: Boolean? = null,
  reusePort: Boolean? = null,
  secured: Boolean? = null,
  sendBufferSize: Int? = null,
  sendErrorOnNoSubscriptions: Boolean? = null,
  sni: Boolean? = null,
  soLinger: Int? = null,
  ssl: Boolean? = null,
  supportedVersions: Iterable<String>? = null,
  tcpCork: Boolean? = null,
  tcpFastOpen: Boolean? = null,
  tcpKeepAlive: Boolean? = null,
  tcpNoDelay: Boolean? = null,
  tcpQuickAck: Boolean? = null,
  timeFactor: Int? = null,
  trafficClass: Int? = null,
  trailingLine: Boolean? = null,
  transactionChunkSize: Int? = null,
  trustStoreOptions: io.vertx.core.net.JksOptions? = null,
  useAlpn: Boolean? = null,
  usePooledBuffers: Boolean? = null,
  websocketBridge: Boolean? = null,
  websocketPath: String? = null): StompServerOptions = io.vertx.ext.stomp.StompServerOptions().apply {

  if (acceptBacklog != null) {
    this.setAcceptBacklog(acceptBacklog)
  }
  if (clientAuth != null) {
    this.setClientAuth(clientAuth)
  }
  if (clientAuthRequired != null) {
    this.setClientAuthRequired(clientAuthRequired)
  }
  if (crlPaths != null) {
    for (item in crlPaths) {
      this.addCrlPath(item)
    }
  }
  if (crlValues != null) {
    for (item in crlValues) {
      this.addCrlValue(item)
    }
  }
  if (enabledCipherSuites != null) {
    for (item in enabledCipherSuites) {
      this.addEnabledCipherSuite(item)
    }
  }
  if (enabledSecureTransportProtocols != null) {
    this.setEnabledSecureTransportProtocols(enabledSecureTransportProtocols.toSet())
  }
  if (heartbeat != null) {
    this.setHeartbeat(heartbeat)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (idleTimeout != null) {
    this.setIdleTimeout(idleTimeout)
  }
  if (jdkSslEngineOptions != null) {
    this.setJdkSslEngineOptions(jdkSslEngineOptions)
  }
  if (keyStoreOptions != null) {
    this.setKeyStoreOptions(keyStoreOptions)
  }
  if (logActivity != null) {
    this.setLogActivity(logActivity)
  }
  if (maxBodyLength != null) {
    this.setMaxBodyLength(maxBodyLength)
  }
  if (maxFrameInTransaction != null) {
    this.setMaxFrameInTransaction(maxFrameInTransaction)
  }
  if (maxHeaderLength != null) {
    this.setMaxHeaderLength(maxHeaderLength)
  }
  if (maxHeaders != null) {
    this.setMaxHeaders(maxHeaders)
  }
  if (maxSubscriptionsByClient != null) {
    this.setMaxSubscriptionsByClient(maxSubscriptionsByClient)
  }
  if (openSslEngineOptions != null) {
    this.setOpenSslEngineOptions(openSslEngineOptions)
  }
  if (pemKeyCertOptions != null) {
    this.setPemKeyCertOptions(pemKeyCertOptions)
  }
  if (pemTrustOptions != null) {
    this.setPemTrustOptions(pemTrustOptions)
  }
  if (pfxKeyCertOptions != null) {
    this.setPfxKeyCertOptions(pfxKeyCertOptions)
  }
  if (pfxTrustOptions != null) {
    this.setPfxTrustOptions(pfxTrustOptions)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (receiveBufferSize != null) {
    this.setReceiveBufferSize(receiveBufferSize)
  }
  if (reuseAddress != null) {
    this.setReuseAddress(reuseAddress)
  }
  if (reusePort != null) {
    this.setReusePort(reusePort)
  }
  if (secured != null) {
    this.setSecured(secured)
  }
  if (sendBufferSize != null) {
    this.setSendBufferSize(sendBufferSize)
  }
  if (sendErrorOnNoSubscriptions != null) {
    this.setSendErrorOnNoSubscriptions(sendErrorOnNoSubscriptions)
  }
  if (sni != null) {
    this.setSni(sni)
  }
  if (soLinger != null) {
    this.setSoLinger(soLinger)
  }
  if (ssl != null) {
    this.setSsl(ssl)
  }
  if (supportedVersions != null) {
    this.setSupportedVersions(supportedVersions.toList())
  }
  if (tcpCork != null) {
    this.setTcpCork(tcpCork)
  }
  if (tcpFastOpen != null) {
    this.setTcpFastOpen(tcpFastOpen)
  }
  if (tcpKeepAlive != null) {
    this.setTcpKeepAlive(tcpKeepAlive)
  }
  if (tcpNoDelay != null) {
    this.setTcpNoDelay(tcpNoDelay)
  }
  if (tcpQuickAck != null) {
    this.setTcpQuickAck(tcpQuickAck)
  }
  if (timeFactor != null) {
    this.setTimeFactor(timeFactor)
  }
  if (trafficClass != null) {
    this.setTrafficClass(trafficClass)
  }
  if (trailingLine != null) {
    this.setTrailingLine(trailingLine)
  }
  if (transactionChunkSize != null) {
    this.setTransactionChunkSize(transactionChunkSize)
  }
  if (trustStoreOptions != null) {
    this.setTrustStoreOptions(trustStoreOptions)
  }
  if (useAlpn != null) {
    this.setUseAlpn(useAlpn)
  }
  if (usePooledBuffers != null) {
    this.setUsePooledBuffers(usePooledBuffers)
  }
  if (websocketBridge != null) {
    this.setWebsocketBridge(websocketBridge)
  }
  if (websocketPath != null) {
    this.setWebsocketPath(websocketPath)
  }
}

