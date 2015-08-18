require 'vertx-stomp/server_frame_handler'
require 'vertx-stomp/authentication_handler'
require 'vertx-stomp/transaction'
require 'vertx-stomp/acknowledgement'
require 'vertx-stomp/subscription'
require 'vertx/vertx'
require 'vertx-stomp/stomp_server'
require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.StompServerHandler
module VertxStomp
  #  STOMP server handler implements the behavior of the STOMP server when a specific event occurs. For instance, if
  #  let customize the behavior when specific STOMP frames arrives or when a connection is closed. This class has been
  #  designed to let you customize the server behavior. The default implementation is compliant with the STOMP
  #  specification. In this default implementation, not acknowledge frames are dropped.
  class StompServerHandler < ::VertxStomp::ServerFrameHandler
    # @private
    # @param j_del [::VertxStomp::StompServerHandler] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::StompServerHandler] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates an instance of {::VertxStomp::StompServerHandler} using the default (compliant) implementation.
    # @param [::Vertx::Vertx] vertx the vert.x instance to use
    # @return [::VertxStomp::StompServerHandler] the created {::VertxStomp::StompServerHandler}
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::StompServerHandler.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxStomp::StompServerHandler)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx)"
    end
    #  Configures the action to execute when a <code>CONNECT</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def connect_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:connectHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling connect_handler(handler)"
    end
    #  Configures the action to execute when a <code>STOMP</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def stomp_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:stompHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling stomp_handler(handler)"
    end
    #  Configures the action to execute when a <code>SUBSCRIBE</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def subscribe_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:subscribeHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling subscribe_handler(handler)"
    end
    #  Configures the action to execute when a <code>UNSUBSCRIBE</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def unsubscribe_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:unsubscribeHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe_handler(handler)"
    end
    #  Configures the action to execute when a <code>SEND</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def send_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:sendHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling send_handler(handler)"
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
      raise ArgumentError, "Invalid arguments when calling on_close(connection)"
    end
    #  Configures the action to execute when a <code>COMMIT</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def commit_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:commitHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling commit_handler(handler)"
    end
    #  Configures the action to execute when a <code>ABORT</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def abort_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:abortHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling abort_handler(handler)"
    end
    #  Configures the action to execute when a <code>BEGIN</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def begin_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:beginHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling begin_handler(handler)"
    end
    #  Configures the action to execute when a <code>DISCONNECT</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def disconnect_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:disconnectHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling disconnect_handler(handler)"
    end
    #  Configures the action to execute when a <code>ACK</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def ack_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:ackHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling ack_handler(handler)"
    end
    #  Configures the action to execute when a <code>NACK</code> frame is received.
    # @param [::VertxStomp::ServerFrameHandler] handler the handler
    # @return [self]
    def nack_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:nackHandler, [Java::IoVertxExtStomp::ServerFrameHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling nack_handler(handler)"
    end
    #  Called when the client connects to a server requiring authentication. It should invokes the handler configured
    #  using {::VertxStomp::StompServerHandler#authentication_handler}.
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
      raise ArgumentError, "Invalid arguments when calling on_authentication_request(server,login,passcode)"
    end
    #  Configures the action to execute when a an authentication request is made.
    # @param [::VertxStomp::AuthenticationHandler] handler the handler
    # @return [self]
    def authentication_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:authenticationHandler, [Java::IoVertxExtStomp::AuthenticationHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling authentication_handler(handler)"
    end
    #  @return the list of destination managed by the STOMP server. Don't forget the STOMP interprets destination as
    #  opaque Strings.
    # @return [Array<String>]
    def get_destinations
      if !block_given?
        return @j_del.java_method(:getDestinations, []).call().to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling get_destinations()"
    end
    #  Registers the given {::VertxStomp::Subscription}.
    # @param [::VertxStomp::Subscription] subscription the subscription
    # @return [true,false] <code>true</code> if the subscription has been registered correctly, <code>false</code> otherwise. The main reason to fail the registration is the non-uniqueness of the subscription id for a given client.
    def subscribe?(subscription=nil)
      if subscription.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:subscribe, [Java::IoVertxExtStomp::Subscription.java_class]).call(subscription.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling subscribe?(subscription)"
    end
    #  Unregisters the subscription 'id' from the given client.
    # @param [::VertxStomp::StompServerConnection] connection the connection (client)
    # @param [String] id the subscription id
    # @return [true,false] <code>true</code> if the subscription removal succeed, <code>false</code> otherwise. The main reason to fail this removal is because the associated subscription cannot be found.
    def unsubscribe?(connection=nil,id=nil)
      if connection.class.method_defined?(:j_del) && id.class == String && !block_given?
        return @j_del.java_method(:unsubscribe, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::java.lang.String.java_class]).call(connection.j_del,id)
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe?(connection,id)"
    end
    #  Unregisters all subscriptions from a given client / connection.
    # @param [::VertxStomp::StompServerConnection] connection the connection (client)
    # @return [self]
    def unsubscribe_connection(connection=nil)
      if connection.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:unsubscribeConnection, [Java::IoVertxExtStomp::StompServerConnection.java_class]).call(connection.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe_connection(connection)"
    end
    #  Gets the current list of subscriptions for the given destination.
    # @param [String] destination the destination
    # @return [Array<::VertxStomp::Subscription>] the list of subscription
    def get_subscriptions(destination=nil)
      if destination.class == String && !block_given?
        return @j_del.java_method(:getSubscriptions, [Java::java.lang.String.java_class]).call(destination).to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxStomp::Subscription) }
      end
      raise ArgumentError, "Invalid arguments when calling get_subscriptions(destination)"
    end
    #  Registers a transaction.
    # @param [::VertxStomp::Transaction] transaction the transaction
    # @return [true,false] <code>true</code> if the registration succeed, <code>false</code> otherwise. The main reason of failure is the non-uniqueness of the transaction id for a given client / connection
    def register_transaction?(transaction=nil)
      if transaction.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:registerTransaction, [Java::IoVertxExtStomp::Transaction.java_class]).call(transaction.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling register_transaction?(transaction)"
    end
    #  Gets a transaction.
    # @param [::VertxStomp::StompServerConnection] connection the connection used by the transaction
    # @param [String] id the id of the transaction
    # @return [::VertxStomp::Transaction] the transaction, <code>null</code> if not found
    def get_transaction(connection=nil,id=nil)
      if connection.class.method_defined?(:j_del) && id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTransaction, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::java.lang.String.java_class]).call(connection.j_del,id),::VertxStomp::Transaction)
      end
      raise ArgumentError, "Invalid arguments when calling get_transaction(connection,id)"
    end
    #  Unregisters a transaction
    # @param [::VertxStomp::Transaction] transaction the transaction to unregister
    # @return [true,false] <code>true</code> if the transaction is unregistered correctly, <code>false</code> otherwise.
    def unregister_transaction?(transaction=nil)
      if transaction.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:unregisterTransaction, [Java::IoVertxExtStomp::Transaction.java_class]).call(transaction.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling unregister_transaction?(transaction)"
    end
    #  Unregisters all transactions from the given connection / client.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @return [self]
    def unregister_transactions_from_connection(connection=nil)
      if connection.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:unregisterTransactionsFromConnection, [Java::IoVertxExtStomp::StompServerConnection.java_class]).call(connection.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unregister_transactions_from_connection(connection)"
    end
    #  Gets the list of current transactions.
    # @return [Array<::VertxStomp::Transaction>] the list of transactions, empty is none.
    def get_transactions
      if !block_given?
        return @j_del.java_method(:getTransactions, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxStomp::Transaction) }
      end
      raise ArgumentError, "Invalid arguments when calling get_transactions()"
    end
    #  Gets a subscription for the given connection / client and use the given acknowledgment id. Acknowledgement id
    #  is different from the subscription id as it point to a single message.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [String] ackId the ack id
    # @return [::VertxStomp::Subscription] the subscription, <code>null</code> if not found
    def get_subscription(connection=nil,ackId=nil)
      if connection.class.method_defined?(:j_del) && ackId.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getSubscription, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::java.lang.String.java_class]).call(connection.j_del,ackId),::VertxStomp::Subscription)
      end
      raise ArgumentError, "Invalid arguments when calling get_subscription(connection,ackId)"
    end
    #  Method called by single message (client-individual policy) or a set of message (client policy) are acknowledged.
    #  Implementations must call the handler configured using {::VertxStomp::StompServerHandler#on_ack_handler}.
    # @param [::VertxStomp::Subscription] subscription the subscription
    # @param [Array<Hash>] messages the acknowledge messages
    # @return [self]
    def on_ack(subscription=nil,messages=nil)
      if subscription.class.method_defined?(:j_del) && messages.class == Array && !block_given?
        @j_del.java_method(:onAck, [Java::IoVertxExtStomp::Subscription.java_class,Java::JavaUtil::List.java_class]).call(subscription.j_del,messages.map { |element| Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(element)) })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_ack(subscription,messages)"
    end
    #  Method called by single message (client-individual policy) or a set of message (client policy) are
    #  <storng>not</storng> acknowledged. Not acknowledgment can result from a <code>NACK</code> frame or from a timeout (no
    #  <code>ACK</code> frame received in a given time. Implementations must call the handler configured using
    #  {::VertxStomp::StompServerHandler#on_nack_handler}.
    # @param [::VertxStomp::Subscription] subscription the subscription
    # @param [Array<Hash>] messages the acknowledge messages
    # @return [self]
    def on_nack(subscription=nil,messages=nil)
      if subscription.class.method_defined?(:j_del) && messages.class == Array && !block_given?
        @j_del.java_method(:onNack, [Java::IoVertxExtStomp::Subscription.java_class,Java::JavaUtil::List.java_class]).call(subscription.j_del,messages.map { |element| Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(element)) })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling on_nack(subscription,messages)"
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
  end
end
