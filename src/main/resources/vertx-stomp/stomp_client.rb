require 'vertx-stomp/stomp_client_connection'
require 'vertx/vertx'
require 'vertx/net_client'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompClient
module VertxStomp
  #  Defines a STOMP client.
  class StompClient
    # @private
    # @param j_del [::VertxStomp::StompClient] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompClient] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates a {::VertxStomp::StompClient} using the default implementation.
    # @param [::Vertx::Vertx] vertx the vert.x instance to use
    # @param [Hash] options the options
    # @return [::VertxStomp::StompClient] the created {::VertxStomp::StompClient}
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxStomp::StompClient)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtStomp::StompClientOptions.java_class]).call(vertx.j_del,Java::IoVertxExtStomp::StompClientOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxStomp::StompClient)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,options)"
    end
    #  Connects to the server.
    # @overload connect(resultHandler)
    #   @yield handler called with the connection result
    # @overload connect(net,resultHandler)
    #   @param [::Vertx::NetClient] net the NET client to use
    #   @yield handler called with the connection result
    # @overload connect(port,host,resultHandler)
    #   @param [Fixnum] port the server port
    #   @param [String] host the server host
    #   @yield handler called with the connection result
    # @overload connect(port,host,net,resultHandler)
    #   @param [Fixnum] port the server port
    #   @param [String] host the server host
    #   @param [::Vertx::NetClient] net the NET client to use
    #   @yield handler called with the connection result
    # @return [self]
    def connect(param_1=nil,param_2=nil,param_3=nil)
      if block_given? && param_1 == nil && param_2 == nil && param_3 == nil
        @j_del.java_method(:connect, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompClientConnection) : nil) }))
        return self
      elsif param_1.class.method_defined?(:j_del) && block_given? && param_2 == nil && param_3 == nil
        @j_del.java_method(:connect, [Java::IoVertxCoreNet::NetClient.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompClientConnection) : nil) }))
        return self
      elsif param_1.class == Fixnum && param_2.class == String && block_given? && param_3 == nil
        @j_del.java_method(:connect, [Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,param_2,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompClientConnection) : nil) }))
        return self
      elsif param_1.class == Fixnum && param_2.class == String && param_3.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:connect, [Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreNet::NetClient.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,param_2,param_3.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompClientConnection) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling connect(param_1,param_2,param_3)"
    end
    #  Configures a received handler that gets notified when a STOMP frame is received by the client.
    #  This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
    # 
    #  When a connection is created, the handler is used as
    #  {::VertxStomp::StompClientConnection#received_frame_handler}.
    # @yield the handler
    # @return [self]
    def received_frame_handler
      if block_given?
        @j_del.java_method(:receivedFrameHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling received_frame_handler()"
    end
    #  Configures a writing handler that gets notified when a STOMP frame is written on the wire.
    #  This handler can be used for logging, debugging or ad-hoc behavior. The frame can still be modified at the time.
    # 
    #  When a connection is created, the handler is used as
    #  {::VertxStomp::StompClientConnection#writing_frame_handler}.
    # @yield the handler
    # @return [self]
    def writing_frame_handler
      if block_given?
        @j_del.java_method(:writingFrameHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event != nil ? JSON.parse(event.toJson.encode) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling writing_frame_handler()"
    end
    #  Closes the client.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  @return the client's options.
    # @return [Hash]
    def options
      if !block_given?
        return @j_del.java_method(:options, []).call() != nil ? JSON.parse(@j_del.java_method(:options, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling options()"
    end
    #  @return the vert.x instance used by the client.
    # @return [::Vertx::Vertx]
    def vertx
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:vertx, []).call(),::Vertx::Vertx)
      end
      raise ArgumentError, "Invalid arguments when calling vertx()"
    end
  end
end
