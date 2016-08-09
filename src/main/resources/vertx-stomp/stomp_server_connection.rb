require 'vertx-stomp/stomp_server_handler'
require 'vertx/buffer'
require 'vertx-stomp/stomp_server'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompServerConnection
module VertxStomp
  #  Class representing a connection between a STOMP client a the server. It keeps a references on the client socket,
  #  so let write to this socket.
  class StompServerConnection
    # @private
    # @param j_del [::VertxStomp::StompServerConnection] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompServerConnection] the underlying java delegate
    def j_del
      @j_del
    end
    #  Writes the given buffer to the socket. This is a low level API that should be used carefully.
    # @overload write(frame)
    #   @param [Hash] frame the frame, must not be <code>null</code>.
    # @overload write(buffer)
    #   @param [::Vertx::Buffer] buffer the buffer
    # @return [self]
    def write(param_1=nil)
      if param_1.class == Hash && !block_given?
        @j_del.java_method(:write, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(param_1)))
        return self
      elsif param_1.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:write, [Java::IoVertxCoreBuffer::Buffer.java_class]).call(param_1.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(param_1)"
    end
    # @return [::VertxStomp::StompServer] the STOMP server serving this connection.
    def server
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:server, []).call(),::VertxStomp::StompServer)
      end
      raise ArgumentError, "Invalid arguments when calling server()"
    end
    # @return [::VertxStomp::StompServerHandler] the STOMP server handler dealing with this connection
    def handler
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:handler, []).call(),::VertxStomp::StompServerHandler)
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
    # @return [String] the STOMP session id computed when the client has established the connection to the server
    def session
      if !block_given?
        return @j_del.java_method(:session, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    #  Closes the connection with the client.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  Sends a `PING` frame to the client. A `PING` frame is a frame containing only <code>EOL</code>.
    # @return [void]
    def ping
      if !block_given?
        return @j_del.java_method(:ping, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling ping()"
    end
    #  Notifies the connection about server activity (the server has sent a frame). This method is used to handle the
    #  heartbeat.
    # @return [void]
    def on_server_activity
      if !block_given?
        return @j_del.java_method(:onServerActivity, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling on_server_activity()"
    end
    #  Configures the heartbeat.
    # @param [Fixnum] ping ping time
    # @param [Fixnum] pong pong time
    # @yield the ping handler
    # @return [void]
    def configure_heartbeat(ping=nil,pong=nil)
      if ping.class == Fixnum && pong.class == Fixnum && block_given?
        return @j_del.java_method(:configureHeartbeat, [Java::long.java_class,Java::long.java_class,Java::IoVertxCore::Handler.java_class]).call(ping,pong,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompServerConnection)) }))
      end
      raise ArgumentError, "Invalid arguments when calling configure_heartbeat(ping,pong)"
    end
  end
end
