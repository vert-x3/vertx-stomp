require 'vertx-stomp/destination'
require 'vertx-stomp/destination_factory'
require 'vertx-stomp/acknowledgement'
require 'vertx/vertx'
require 'vertx-stomp/stomp_server'
require 'vertx-stomp/server_frame'
require 'vertx-stomp/stomp_server_connection'
require 'vertx-auth-common/auth_provider'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompServerHandler
module VertxStomp
  #  STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
  #  let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
  #  designed to let you customize the server behavior. The default implementation is compliant with the STOMP
  #  specification. In this default implementation, not acknowledge frames are dropped.
  class StompServerHandler
    # @private
    # @param j_del [::VertxStomp::StompServerHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompServerHandler] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == StompServerHandler
    end
    def @@j_api_type.wrap(obj)
      StompServerHandler.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtStomp::StompServerHandler.java_class
    end
    # @param [::VertxStomp::ServerFrame] arg0 
    # @return [void]
    def handle(arg0=nil)
      if arg0.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:handle, [Java::IoVertxExtStomp::ServerFrame.java_class]).call(arg0.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling handle(#{arg0})"
    end
    #  Creates an instance of {::VertxStomp::StompServerHandler} using the default (compliant) implementation.
    # @param [::Vertx::Vertx] vertx the vert.x instance to use
    # @return [::VertxStomp::StompServerHandler] the created {::VertxStomp::StompServerHandler}
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServerHandler.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxStomp::StompServerHandler)
      end
      raise ArgumentError, "Invalid arguments when calling create(#{vertx})"
    end
    #  Configures a handler that get notified when a STOMP frame is received by the server.
    #  This handler can be used for logging, debugging or ad-hoc behavior.
    # @yield the handler
    # @return [self]
    def received_frame_handler
      if block_given?
        @j_del.java_method(:receivedFrameHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling received_frame_handler()"
    end
    #  Configures the action to execute when a <code>CONNECT</code> frame is received.
    # @yield the handler
    # @return [self]
    def connect_handler
      if block_given?
        @j_del.java_method(:connectHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling connect_handler()"
    end
    #  Configures the action to execute when a <code>STOMP</code> frame is received.
    # @yield the handler
    # @return [self]
    def stomp_handler
      if block_given?
        @j_del.java_method(:stompHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling stomp_handler()"
    end
    #  Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
    # @yield the handler
    # @return [self]
    def subscribe_handler
      if block_given?
        @j_del.java_method(:subscribeHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling subscribe_handler()"
    end
    #  Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
    # @yield the handler
    # @return [self]
    def unsubscribe_handler
      if block_given?
        @j_del.java_method(:unsubscribeHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe_handler()"
    end
    #  Configures the action to execute when a <code>SEND</code> frame is received.
    # @yield the handler
    # @return [self]
    def send_handler
      if block_given?
        @j_del.java_method(:sendHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling send_handler()"
    end
    #  Configures the action to execute when a connection with the client is closed.
    # @yield the handler
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompServerConnection)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    #  Called when the connection is closed. This method executes a default behavior and must calls the configured
    #  {::VertxStomp::StompServerHandler#close_handler} if any.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @return [void]
    def on_close(connection=nil)
      if connection.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:onClose, [Java::IoVertxExtStomp::StompServerConnection.java_class]).call(connection.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling on_close(#{connection})"
    end
    #  Configures the action to execute when a <code>COMMIT</code> frame is received.
    # @yield the handler
    # @return [self]
    def commit_handler
      if block_given?
        @j_del.java_method(:commitHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling commit_handler()"
    end
    #  Configures the action to execute when a <code>ABORT</code> frame is received.
    # @yield the handler
    # @return [self]
    def abort_handler
      if block_given?
        @j_del.java_method(:abortHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling abort_handler()"
    end
    #  Configures the action to execute when a <code>BEGIN</code> frame is received.
    # @yield the handler
    # @return [self]
    def begin_handler
      if block_given?
        @j_del.java_method(:beginHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling begin_handler()"
    end
    #  Configures the action to execute when a <code>DISCONNECT</code> frame is received.
    # @yield the handler
    # @return [self]
    def disconnect_handler
      if block_given?
        @j_del.java_method(:disconnectHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling disconnect_handler()"
    end
    #  Configures the action to execute when a <code>ACK</code> frame is received.
    # @yield the handler
    # @return [self]
    def ack_handler
      if block_given?
        @j_del.java_method(:ackHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling ack_handler()"
    end
    #  Configures the action to execute when a <code>NACK</code> frame is received.
    # @yield the handler
    # @return [self]
    def nack_handler
      if block_given?
        @j_del.java_method(:nackHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::ServerFrame)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling nack_handler()"
    end
    #  Called when the client connects to a server requiring authentication. It invokes the  configured
    #  using {::VertxStomp::StompServerHandler#auth_provider}.
    # @param [::VertxStomp::StompServer] server the STOMP server.
    # @param [String] login the login
    # @param [String] passcode the password
    # @yield handler receiving the authentication result
    # @return [self]
    def on_authentication_request(server=nil,login=nil,passcode=nil)
      if server.class.method_defined?(:j_del) && login.class == String && passcode.class == String && block_given?
        @j_del.java_method(:onAuthenticationRequest, [Java::IoVertxExtStomp::StompServer.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(server.j_del,login,passcode,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_authentication_request(#{server},#{login},#{passcode})"
    end
    #  Configures the  to be used to authenticate the user.
    # @param [::VertxAuthCommon::AuthProvider] handler the handler
    # @return [self]
    def auth_provider(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:authProvider, [Java::IoVertxExtAuth::AuthProvider.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling auth_provider(#{handler})"
    end
    # @return [Array<::VertxStomp::Destination>] the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as opaque Strings.
    def get_destinations
      if !block_given?
        return @j_del.java_method(:getDestinations, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxStomp::Destination) }
      end
      raise ArgumentError, "Invalid arguments when calling get_destinations()"
    end
    #  Gets the destination with the given name.
    # @param [String] destination the destination
    # @return [::VertxStomp::Destination] the {::VertxStomp::Destination}, <code>null</code> if not existing.
    def get_destination(destination=nil)
      if destination.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getDestination, [Java::java.lang.String.java_class]).call(destination),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling get_destination(#{destination})"
    end
    #  Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
    #  Implementations must call the handler configured using {::VertxStomp::StompServerHandler#on_ack_handler}.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] subscribe the <code>SUBSCRIBE</code> frame
    # @param [Array<Hash>] messages the acknowledge messages
    # @return [self]
    def on_ack(connection=nil,subscribe=nil,messages=nil)
      if connection.class.method_defined?(:j_del) && subscribe.class == Hash && messages.class == Array && !block_given?
        @j_del.java_method(:onAck, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class,Java::JavaUtil::List.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(subscribe)),messages.map { |element| Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(element)) })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_ack(#{connection},#{subscribe},#{messages})"
    end
    #  Method called by single message (client-individual policy) or a set of message (client policy) are
    #  <strong>not</strong> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
    #  <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
    #  {::VertxStomp::StompServerHandler#on_nack_handler}.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] subscribe the <code>SUBSCRIBE</code> frame
    # @param [Array<Hash>] messages the acknowledge messages
    # @return [self]
    def on_nack(connection=nil,subscribe=nil,messages=nil)
      if connection.class.method_defined?(:j_del) && subscribe.class == Hash && messages.class == Array && !block_given?
        @j_del.java_method(:onNack, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class,Java::JavaUtil::List.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(subscribe)),messages.map { |element| Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(element)) })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_nack(#{connection},#{subscribe},#{messages})"
    end
    #  Configures the action to execute when messages are acknowledged.
    # @yield the handler
    # @return [self]
    def on_ack_handler
      if block_given?
        @j_del.java_method(:onAckHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::Acknowledgement)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_ack_handler()"
    end
    #  Configures the action to execute when messages are <strong>not</strong> acknowledged.
    # @yield the handler
    # @return [self]
    def on_nack_handler
      if block_given?
        @j_del.java_method(:onNackHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::Acknowledgement)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_nack_handler()"
    end
    #  Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
    #  frame containing <code>EOL</code> (specification). However, you can customize this and send another frame. However,
    #  be aware that this may requires a custom client.
    #  <p/>
    #  The handler will only be called if the connection supports heartbeats.
    # @yield the action to execute when a `PING` needs to be sent.
    # @return [self]
    def ping_handler
      if block_given?
        @j_del.java_method(:pingHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxStomp::StompServerConnection)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling ping_handler()"
    end
    #  Gets a {::VertxStomp::Destination} object if existing, or create a new one. The creation is delegated to the
    #  {::VertxStomp::DestinationFactory}.
    # @param [String] destination the destination
    # @return [::VertxStomp::Destination] the {::VertxStomp::Destination} instance, may have been created.
    def get_or_create_destination(destination=nil)
      if destination.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getOrCreateDestination, [Java::java.lang.String.java_class]).call(destination),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling get_or_create_destination(#{destination})"
    end
    #  Configures the {::VertxStomp::DestinationFactory} used to create {::VertxStomp::Destination} objects.
    # @param [::VertxStomp::DestinationFactory] factory the factory
    # @return [self]
    def destination_factory(factory=nil)
      if factory.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:destinationFactory, [Java::IoVertxExtStomp::DestinationFactory.java_class]).call(factory.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling destination_factory(#{factory})"
    end
    #  Configures the STOMP server to act as a bridge with the Vert.x event bus.
    # @param [Hash] options the configuration options
    # @return [self]
    def bridge(options=nil)
      if options.class == Hash && !block_given?
        @j_del.java_method(:bridge, [Java::IoVertxExtStomp::BridgeOptions.java_class]).call(Java::IoVertxExtStomp::BridgeOptions.new(::Vertx::Util::Utils.to_json_object(options)))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling bridge(#{options})"
    end
  end
end
