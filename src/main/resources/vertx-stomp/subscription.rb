require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Subscription
module VertxStomp
  #  Represents a subscription in the STOMP server.
  class Subscription
    # @private
    # @param j_del [::VertxStomp::Subscription] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Subscription] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates an instance of {::VertxStomp::Subscription} using the default implementation.
    # @param [::VertxStomp::StompServerConnection] connection the connection (client)
    # @param [String] destination the destination
    # @param [String] ack the acknowledgment policy, <code>auto</code> by default.
    # @param [String] id the subscription id
    # @return [::VertxStomp::Subscription] the created {::VertxStomp::Subscription}
    def self.create(connection=nil,destination=nil,ack=nil,id=nil)
      if connection.class.method_defined?(:j_del) && destination.class == String && ack.class == String && id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Subscription.java_method(:create, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(connection.j_del,destination,ack,id),::VertxStomp::Subscription)
      end
      raise ArgumentError, "Invalid arguments when calling create(connection,destination,ack,id)"
    end
    #  @return the connection.
    # @return [::VertxStomp::StompServerConnection]
    def connection
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:connection, []).call(),::VertxStomp::StompServerConnection)
      end
      raise ArgumentError, "Invalid arguments when calling connection()"
    end
    #  @return the acknowledgment policy among <code>auto</code> (default), <code>client</code> (cumulative acknowledgment) and
    #  <code>client-individual</code>.
    # @return [String]
    def ack_mode
      if !block_given?
        return @j_del.java_method(:ackMode, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling ack_mode()"
    end
    #  @return the subscription id.
    # @return [String]
    def id
      if !block_given?
        return @j_del.java_method(:id, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    #  @return the destination.
    # @return [String]
    def destination
      if !block_given?
        return @j_del.java_method(:destination, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling destination()"
    end
    #  Acknowledges the message with the given id.
    # @param [String] messageId the message id
    # @return [true,false] <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
    def ack?(messageId=nil)
      if messageId.class == String && !block_given?
        return @j_del.java_method(:ack, [Java::java.lang.String.java_class]).call(messageId)
      end
      raise ArgumentError, "Invalid arguments when calling ack?(messageId)"
    end
    #  Not-acknowledges the message with the given id.
    # @param [String] messageId the message id
    # @return [true,false] <code>true</code> if the message was waiting for acknowledgment, <code>false</code> otherwise.
    def nack?(messageId=nil)
      if messageId.class == String && !block_given?
        return @j_del.java_method(:nack, [Java::java.lang.String.java_class]).call(messageId)
      end
      raise ArgumentError, "Invalid arguments when calling nack?(messageId)"
    end
    #  Adds a message (identified by its id) to the list of message waiting for acknowledgment.
    # @param [Hash] messageId the message id
    # @return [void]
    def enqueue(messageId=nil)
      if messageId.class == Hash && !block_given?
        return @j_del.java_method(:enqueue, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(messageId)))
      end
      raise ArgumentError, "Invalid arguments when calling enqueue(messageId)"
    end
    #  Checks whether or not the message identified by the given message id is waiting for an acknowledgment.
    # @param [String] messageId the message id
    # @return [true,false] <code>true</code> if the message requires an acknowledgment, <code>false</code> otherwise.
    def contains?(messageId=nil)
      if messageId.class == String && !block_given?
        return @j_del.java_method(:contains, [Java::java.lang.String.java_class]).call(messageId)
      end
      raise ArgumentError, "Invalid arguments when calling contains?(messageId)"
    end
  end
end
