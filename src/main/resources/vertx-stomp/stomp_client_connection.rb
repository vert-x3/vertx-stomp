require 'vertx/buffer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompClientConnection
module VertxStomp
  #  Once a connection to the STOMP server has been made, client receives a {::VertxStomp::StompClientConnection}, that let
  #  send and receive STOMP frames.
  class StompClientConnection
    # @private
    # @param j_del [::VertxStomp::StompClientConnection] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompClientConnection] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the session id.
    # @return [String]
    def session
      if !block_given?
        return @j_del.java_method(:session, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    #  @return the STOMP protocol version negotiated with the server.
    # @return [String]
    def version
      if !block_given?
        return @j_del.java_method(:version, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling version()"
    end
    #  Closes the connection without sending the <code>DISCONNECT</code> frame.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  @return the server name.
    # @return [String]
    def server
      if !block_given?
        return @j_del.java_method(:server, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling server()"
    end
    #  Sends a <code>SEND</code> frame to the server to the given destination.
    # @overload send(frame)
    #   @param [Hash] frame the frame
    # @overload send(headers,body)
    #   @param [Hash{String => String}] headers the headers, must not be <code>null</code>
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    # @overload send(destination,body)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    # @overload send(frame,receiptHandler)
    #   @param [Hash] frame the frame
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
    # @overload send(headers,body,receiptHandler)
    #   @param [Hash{String => String}] headers the headers, must not be <code>null</code>
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
    # @overload send(destination,body,receiptHandler)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
    # @overload send(destination,headers,body)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [Hash{String => String}] headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    # @overload send(destination,headers,body,receiptHandler)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [Hash{String => String}] headers the header. The <code>destination</code> header is replaced by the value given to the <code>destination</code> parameter
    #   @param [::Vertx::Buffer] body the body, may be <code>null</code>
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the sent frame has been received. The handler receives the sent frame.
    # @return [self]
    def send(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class == Hash && !block_given? && param_2 == nil && param_3 == nil
        @j_del.java_method(:send, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(param_1)))
        return self
      elsif param_1.class == Hash && param_2.class.method_defined?(:j_del) && !block_given? && param_3 == nil
        @j_del.java_method(:send, [Java::JavaUtil::Map.java_class,Java::IoVertxCoreBuffer::Buffer.java_class]).call(Hash[param_1.map { |k,v| [k,v] }],param_2.j_del)
        return self
      elsif param_1.class == String && param_2.class.method_defined?(:j_del) && !block_given? && param_3 == nil
        @j_del.java_method(:send, [Java::java.lang.String.java_class,Java::IoVertxCoreBuffer::Buffer.java_class]).call(param_1,param_2.j_del)
        return self
      elsif param_1.class == Hash && block_given? && param_2 == nil && param_3 == nil
        @j_del.java_method(:send, [Java::IoVertxExtStomp::Frame.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(param_1)),(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif param_1.class == Hash && param_2.class.method_defined?(:j_del) && block_given? && param_3 == nil
        @j_del.java_method(:send, [Java::JavaUtil::Map.java_class,Java::IoVertxCoreBuffer::Buffer.java_class,Java::IoVertxCore::Handler.java_class]).call(Hash[param_1.map { |k,v| [k,v] }],param_2.j_del,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif param_1.class == String && param_2.class.method_defined?(:j_del) && block_given? && param_3 == nil
        @j_del.java_method(:send, [Java::java.lang.String.java_class,Java::IoVertxCoreBuffer::Buffer.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,param_2.j_del,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif param_1.class == String && param_2.class == Hash && param_3.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:send, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCoreBuffer::Buffer.java_class]).call(param_1,Hash[param_2.map { |k,v| [k,v] }],param_3.j_del)
        return self
      elsif param_1.class == String && param_2.class == Hash && param_3.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:send, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCoreBuffer::Buffer.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,Hash[param_2.map { |k,v| [k,v] }],param_3.j_del,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling send(param_1,param_2,param_3)"
    end
    #  Subscribes to the given destination.
    # @overload subscribe(destination,handler)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @yield the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
    # @overload subscribe(destination,handler,receiptHandler)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [Proc] handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
    # @overload subscribe(destination,headers,handler)
    #   @param [String] destination the destination, must not be <code>null</code>.
    #   @param [Hash{String => String}] headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
    #   @yield the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
    # @overload subscribe(destination,headers,handler,receiptHandler)
    #   @param [String] destination the destination, must not be <code>null</code>
    #   @param [Hash{String => String}] headers the headers to configure the subscription. It may contain the <code>ack</code> header to configure the acknowledgment policy. If the given set of headers contains the <code>id</code> header, this value is used as subscription id.
    #   @param [Proc] handler the handler invoked when a message is received on the given destination. Must not be <code>null</code>.
    #   @yield the handler invoked when the <code>RECEIPT</code> frame associated with the subscription has been received. The handler receives the sent frame (<code>SUBSCRIBE</code>).
    # @return [String] the subscription id, which can either be the destination or the id set in the headers.
    def subscribe(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class == String && block_given? && param_2 == nil && param_3 == nil
        return @j_del.java_method(:subscribe, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
      elsif param_1.class == String && param_2.class == Proc && block_given? && param_3 == nil
        return @j_del.java_method(:subscribe, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
      elsif param_1.class == String && param_2.class == Hash && block_given? && param_3 == nil
        return @j_del.java_method(:subscribe, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,Hash[param_2.map { |k,v| [k,v] }],(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
      elsif param_1.class == String && param_2.class == Hash && param_3.class == Proc && block_given?
        return @j_del.java_method(:subscribe, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,Hash[param_2.map { |k,v| [k,v] }],(Proc.new { |event| param_3.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling subscribe(param_1,param_2,param_3)"
    end
    #  Un-subscribes from the given destination. This method computes the subscription id as follows. If the given
    #  headers contains the <code>id</code> header, the header value is used. Otherwise the destination is used.
    # @param [String] destination the destination
    # @param [Hash{String => String}] headers the headers
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the un-subscription has been received. The handler receives the sent frame (<code>UNSUBSCRIBE</code>).
    # @return [self]
    def unsubscribe(destination=nil,headers=nil)
      if destination.class == String && !block_given? && headers == nil
        @j_del.java_method(:unsubscribe, [Java::java.lang.String.java_class]).call(destination)
        return self
      elsif destination.class == String && block_given? && headers == nil
        @j_del.java_method(:unsubscribe, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(destination,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif destination.class == String && headers.class == Hash && !block_given?
        @j_del.java_method(:unsubscribe, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(destination,Hash[headers.map { |k,v| [k,v] }])
        return self
      elsif destination.class == String && headers.class == Hash && block_given?
        @j_del.java_method(:unsubscribe, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class]).call(destination,Hash[headers.map { |k,v| [k,v] }],(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe(destination,headers)"
    end
    #  Sets a handler notified when an <code>ERROR</code> frame is received by the client. The handler receives the <code>ERROR</code> frame and a reference on the {::VertxStomp::StompClientConnection}.
    # @yield the handler
    # @return [self]
    def error_handler
      if block_given?
        @j_del.java_method(:errorHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling error_handler()"
    end
    #  Sets a handler notified when the STOMP connection is closed.
    # @yield the handler
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompClientConnection)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    #  Sets a handler notified when the server does not respond to a <code>ping</code> request in time. In other
    #  words, this handler is invoked when the heartbeat has detected a connection failure with the server.
    #  The handler can decide to reconnect to the server.
    # @yield the handler
    # @return [self]
    def connection_dropped_handler
      if block_given?
        @j_del.java_method(:connectionDroppedHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompClientConnection)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling connection_dropped_handler()"
    end
    #  Sets a handler that let customize the behavior when a ping needs to be sent to the server. Be aware that
    #  changing the default behavior may break the compliance with the STOMP specification.
    # @yield the handler
    # @return [self]
    def ping_handler
      if block_given?
        @j_del.java_method(:pingHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompClientConnection)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling ping_handler()"
    end
    #  Begins a transaction.
    # @param [String] id the transaction id, must not be <code>null</code>
    # @param [Hash{String => String}] headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the transaction begin has been processed by the server. The handler receives the sent frame (<code>BEGIN</code>).
    # @return [self]
    def begin_tx(id=nil,headers=nil)
      if id.class == String && !block_given? && headers == nil
        @j_del.java_method(:beginTX, [Java::java.lang.String.java_class]).call(id)
        return self
      elsif id.class == String && block_given? && headers == nil
        @j_del.java_method(:beginTX, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif id.class == String && headers.class == Hash && !block_given?
        @j_del.java_method(:beginTX, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }])
        return self
      elsif id.class == String && headers.class == Hash && block_given?
        @j_del.java_method(:beginTX, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }],(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling begin_tx(id,headers)"
    end
    #  Commits a transaction.
    # @param [String] id the transaction id, must not be <code>null</code>
    # @param [Hash{String => String}] headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the transaction commit has been processed by the server. The handler receives the sent frame (<code>COMMIT</code>).
    # @return [self]
    def commit(id=nil,headers=nil)
      if id.class == String && !block_given? && headers == nil
        @j_del.java_method(:commit, [Java::java.lang.String.java_class]).call(id)
        return self
      elsif id.class == String && block_given? && headers == nil
        @j_del.java_method(:commit, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif id.class == String && headers.class == Hash && !block_given?
        @j_del.java_method(:commit, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }])
        return self
      elsif id.class == String && headers.class == Hash && block_given?
        @j_del.java_method(:commit, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }],(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling commit(id,headers)"
    end
    #  Aborts a transaction.
    # @param [String] id the transaction id, must not be <code>null</code>
    # @param [Hash{String => String}] headers additional headers to send to the server. The <code>transaction</code> header is replaced by the value passed in the @{code id} parameter
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the transaction cancellation has been processed by the server. The handler receives the sent frame (<code>ABORT</code>).
    # @return [self]
    def abort(id=nil,headers=nil)
      if id.class == String && !block_given? && headers == nil
        @j_del.java_method(:abort, [Java::java.lang.String.java_class]).call(id)
        return self
      elsif id.class == String && block_given? && headers == nil
        @j_del.java_method(:abort, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif id.class == String && headers.class == Hash && !block_given?
        @j_del.java_method(:abort, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }])
        return self
      elsif id.class == String && headers.class == Hash && block_given?
        @j_del.java_method(:abort, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::IoVertxCore::Handler.java_class]).call(id,Hash[headers.map { |k,v| [k,v] }],(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling abort(id,headers)"
    end
    #  Disconnects the client. Unlike the {::VertxStomp::StompClientConnection#close} method, this method send the <code>DISCONNECT</code> frame to the
    #  server. This method lets you customize the <code>DISCONNECT</code> frame.
    # @param [Hash] frame the <code>DISCONNECT</code> frame.
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the disconnection has been processed by the server. The handler receives the sent frame (<code>DISCONNECT</code>).
    # @return [self]
    def disconnect(frame=nil)
      if !block_given? && frame == nil
        @j_del.java_method(:disconnect, []).call()
        return self
      elsif block_given? && frame == nil
        @j_del.java_method(:disconnect, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif frame.class == Hash && !block_given?
        @j_del.java_method(:disconnect, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
        return self
      elsif frame.class == Hash && block_given?
        @j_del.java_method(:disconnect, [Java::IoVertxExtStomp::Frame.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)),(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling disconnect(frame)"
    end
    #  Sends an acknowledgement for the given frame. It means that the frame has been handled and processed by the
    #  client. The sent acknowledgement is part of the transaction identified by the given id.
    # @param [String] id the message id of the message to acknowledge
    # @param [String] txId the transaction id
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the acknowledgment has been processed by the server. The handler receives the sent frame (<code>ACK</code>).
    # @return [self]
    def ack(id=nil,txId=nil)
      if id.class == String && !block_given? && txId == nil
        @j_del.java_method(:ack, [Java::java.lang.String.java_class]).call(id)
        return self
      elsif id.class == String && block_given? && txId == nil
        @j_del.java_method(:ack, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif id.class == String && txId.class == String && !block_given?
        @j_del.java_method(:ack, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(id,txId)
        return self
      elsif id.class == String && txId.class == String && block_given?
        @j_del.java_method(:ack, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,txId,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling ack(id,txId)"
    end
    #  Sends a non-acknowledgement for the given frame. It means that the frame has not been handled by the client.
    #  The sent non-acknowledgement is part of the transaction identified by the given id.
    # @param [String] id the message id of the message to acknowledge
    # @param [String] txId the transaction id
    # @yield the handler invoked when the <code>RECEIPT</code> frame associated with the non-acknowledgment has been processed by the server. The handler receives the sent frame (<code>NACK</code>).
    # @return [self]
    def nack(id=nil,txId=nil)
      if id.class == String && !block_given? && txId == nil
        @j_del.java_method(:nack, [Java::java.lang.String.java_class]).call(id)
        return self
      elsif id.class == String && block_given? && txId == nil
        @j_del.java_method(:nack, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      elsif id.class == String && txId.class == String && !block_given?
        @j_del.java_method(:nack, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(id,txId)
        return self
      elsif id.class == String && txId.class == String && block_given?
        @j_del.java_method(:nack, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,txId,(Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling nack(id,txId)"
    end
  end
end
