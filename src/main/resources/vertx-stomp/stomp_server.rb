require 'vertx-stomp/stomp_server_handler'
require 'vertx/server_web_socket'
require 'vertx/net_server'
require 'vertx/vertx'
require 'vertx-stomp/server_frame'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompServer
module VertxStomp
  #  Defines a STOMP server. STOMP servers delegates to a {::VertxStomp::StompServerHandler} that let customize the behavior of
  #  the server. By default, it uses a handler compliant with the STOMP specification, but let you change anything.
  class StompServer
    # @private
    # @param j_del [::VertxStomp::StompServer] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompServer] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates a {::VertxStomp::StompServer} based on the default Stomp Server implementation.
    # @overload create(vertx)
    #   @param [::Vertx::Vertx] vertx the vert.x instance to use
    # @overload create(vertx,options)
    #   @param [::Vertx::Vertx] vertx the vert.x instance to use
    #   @param [Hash] options the server options
    # @overload create(vertx,netServer)
    #   @param [::Vertx::Vertx] vertx the vert.x instance to use
    #   @param [::Vertx::NetServer] netServer the Net server used by the STOMP server
    # @overload create(vertx,net,options)
    #   @param [::Vertx::Vertx] vertx the vert.x instance to use
    #   @param [::Vertx::NetServer] net the Net server used by the STOMP server
    #   @param [Hash] options the server options
    # @return [::VertxStomp::StompServer] the created {::VertxStomp::StompServer}
    def self.create(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && !block_given? && param_2 == nil && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServer.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(param_1.j_del),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServer.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtStomp::StompServerOptions.java_class]).call(param_1.j_del,Java::IoVertxExtStomp::StompServerOptions.new(::Vertx::Util::Utils.to_json_object(param_2))),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServer.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCoreNet::NetServer.java_class]).call(param_1.j_del,param_2.j_del),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && param_3.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServer.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCoreNet::NetServer.java_class,Java::IoVertxExtStomp::StompServerOptions.java_class]).call(param_1.j_del,param_2.j_del,Java::IoVertxExtStomp::StompServerOptions.new(::Vertx::Util::Utils.to_json_object(param_3))),::VertxStomp::StompServer)
      end
      raise ArgumentError, "Invalid arguments when calling create(param_1,param_2,param_3)"
    end
    #  Configures the {::VertxStomp::StompServerHandler}. You must calls this method before calling the {::VertxStomp::StompServer#listen} method.
    # @param [::VertxStomp::StompServerHandler] handler the handler
    # @return [self]
    def handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:handler, [Java::IoVertxExtStomp::StompServerHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling handler(handler)"
    end
    #  Connects the STOMP server to the given port / interface. Once the socket it bounds calls the given handler with
    #  the result. The result may be a failure if the socket is already used.
    # @param [Fixnum] port the port
    # @param [String] host the host / interface
    # @yield the handler to call with the result
    # @return [self]
    def listen(port=nil,host=nil)
      if !block_given? && port == nil && host == nil
        @j_del.java_method(:listen, []).call()
        return self
      elsif port.class == Fixnum && !block_given? && host == nil
        @j_del.java_method(:listen, [Java::int.java_class]).call(port)
        return self
      elsif block_given? && port == nil && host == nil
        @j_del.java_method(:listen, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompServer) : nil) }))
        return self
      elsif port.class == Fixnum && host.class == String && !block_given?
        @j_del.java_method(:listen, [Java::int.java_class,Java::java.lang.String.java_class]).call(port,host)
        return self
      elsif port.class == Fixnum && block_given? && host == nil
        @j_del.java_method(:listen, [Java::int.java_class,Java::IoVertxCore::Handler.java_class]).call(port,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompServer) : nil) }))
        return self
      elsif port.class == Fixnum && host.class == String && block_given?
        @j_del.java_method(:listen, [Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(port,host,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxStomp::StompServer) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling listen(port,host)"
    end
    #  Closes the server.
    # @yield handler called once the server has been stopped
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      elsif block_given?
        return @j_del.java_method(:close, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  Checks whether or not the server is listening.
    # @return [true,false] <code>true</code> if the server is listening, <code>false</code> otherwise
    def listening?
      if !block_given?
        return @j_del.java_method(:isListening, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling listening?()"
    end
    #  Gets the port on which the server is listening.
    #  <p/>
    #  This is useful if you bound the server specifying 0 as port number signifying an ephemeral port.
    # @return [Fixnum] the port
    def actual_port
      if !block_given?
        return @j_del.java_method(:actualPort, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling actual_port()"
    end
    #  @return the server options
    # @return [Hash]
    def options
      if !block_given?
        return @j_del.java_method(:options, []).call() != nil ? JSON.parse(@j_del.java_method(:options, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling options()"
    end
    #  @return the instance of vert.x used by the server.
    # @return [::Vertx::Vertx]
    def vertx
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:vertx, []).call(),::Vertx::Vertx)
      end
      raise ArgumentError, "Invalid arguments when calling vertx()"
    end
    #  @return the {::VertxStomp::StompServerHandler} used by this server.
    # @return [::VertxStomp::StompServerHandler]
    def stomp_handler
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:stompHandler, []).call(),::VertxStomp::StompServerHandler)
      end
      raise ArgumentError, "Invalid arguments when calling stomp_handler()"
    end
    #  Gets the  able to manage web socket connections. If the web socket bridge is disabled, it returns
    #  <code>null</code>.
    # @return [Proc] the handler that can be passed to {::Vertx::HttpServer#websocket_handler}.
    def web_socket_handler
      if !block_given?
        return ::Vertx::Util::Utils.to_handler_proc(@j_del.java_method(:webSocketHandler, []).call()) { |val| val.j_del }
      end
      raise ArgumentError, "Invalid arguments when calling web_socket_handler()"
    end
    #  Configures the handler that is invoked every time a frame is going to be written to the "wire". It lets you log
    #  the frames, but also adapt the frame if needed.
    # @yield the handler, must not be <code>null</code>
    # @return [self]
    def writing_frame_handler
      if block_given?
        @j_del.java_method(:writingFrameHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling writing_frame_handler()"
    end
  end
end
