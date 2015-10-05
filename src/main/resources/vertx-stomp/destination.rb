require 'vertx/vertx'
require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Destination
module VertxStomp
  #  Represents a STOMP destination.
  #  Depending on the implementation, the message delivery is different. Queue are sending message to only one
  #  subscribers, while topics are broadcasting the message to all subscribers.
  #  <p/>
  #  Implementations <strong>must</strong> be thread-safe.
  class Destination
    # @private
    # @param j_del [::VertxStomp::Destination] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Destination] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [String] destination 
    # @return [::VertxStomp::Destination]
    def self.topic(vertx=nil,destination=nil)
      if vertx.class.method_defined?(:j_del) && destination.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Destination.java_method(:topic, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,destination),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling topic(vertx,destination)"
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [String] destination 
    # @return [::VertxStomp::Destination]
    def self.queue(vertx=nil,destination=nil)
      if vertx.class.method_defined?(:j_del) && destination.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Destination.java_method(:queue, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,destination),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling queue(vertx,destination)"
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [Hash] options 
    # @return [::VertxStomp::Destination]
    def self.bridge(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Destination.java_method(:bridge, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtStomp::BridgeOptions.java_class]).call(vertx.j_del,Java::IoVertxExtStomp::BridgeOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling bridge(vertx,options)"
    end
    #  @return the destination address.
    # @return [String]
    def destination
      if !block_given?
        return @j_del.java_method(:destination, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling destination()"
    end
    #  Dispatches the given frame.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] frame the frame
    # @return [self]
    def dispatch(connection=nil,frame=nil)
      if connection.class.method_defined?(:j_del) && frame.class == Hash && !block_given?
        @j_del.java_method(:dispatch, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling dispatch(connection,frame)"
    end
    #  Handles a subscription request to the current {::VertxStomp::Destination}.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] frame the <code>SUBSCRIBE</code> frame
    # @return [self]
    def subscribe(connection=nil,frame=nil)
      if connection.class.method_defined?(:j_del) && frame.class == Hash && !block_given?
        @j_del.java_method(:subscribe, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling subscribe(connection,frame)"
    end
    #  Handles a un-subscription request to the current {::VertxStomp::Destination}.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] frame the <code>UNSUBSCRIBE</code> frame
    # @return [true,false] <code>true</code> if the un-subscription has been handled, <code>false</code> otherwise.
    def unsubscribe?(connection=nil,frame=nil)
      if connection.class.method_defined?(:j_del) && frame.class == Hash && !block_given?
        return @j_del.java_method(:unsubscribe, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe?(connection,frame)"
    end
    #  Removes all subscriptions of the given connection
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @return [self]
    def unsubscribe_connection(connection=nil)
      if connection.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:unsubscribeConnection, [Java::IoVertxExtStomp::StompServerConnection.java_class]).call(connection.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unsubscribe_connection(connection)"
    end
    #  Handles a <code>ACK</code> frame.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] frame the <code>ACK</code> frame
    # @return [true,false] <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
    def ack?(connection=nil,frame=nil)
      if connection.class.method_defined?(:j_del) && frame.class == Hash && !block_given?
        return @j_del.java_method(:ack, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
      end
      raise ArgumentError, "Invalid arguments when calling ack?(connection,frame)"
    end
    #  Handles a <code>NACK</code> frame.
    # @param [::VertxStomp::StompServerConnection] connection the connection
    # @param [Hash] frame the <code>NACK</code> frame
    # @return [true,false] <code>true</code> if the destination has handled the frame (meaning it has sent the message with id)
    def nack?(connection=nil,frame=nil)
      if connection.class.method_defined?(:j_del) && frame.class == Hash && !block_given?
        return @j_del.java_method(:nack, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::IoVertxExtStomp::Frame.java_class]).call(connection.j_del,Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
      end
      raise ArgumentError, "Invalid arguments when calling nack?(connection,frame)"
    end
    #  Gets all subscription ids for the given destination hold by the given client
    # @param [::VertxStomp::StompServerConnection] connection the connection (client)
    # @return [Array<String>] the list of subscription id, empty if none
    def get_subscriptions(connection=nil)
      if connection.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:getSubscriptions, [Java::IoVertxExtStomp::StompServerConnection.java_class]).call(connection.j_del).to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling get_subscriptions(connection)"
    end
    #  Gets the number of subscriptions attached to the current {::VertxStomp::Destination}.
    # @return [Fixnum] the number of subscriptions.
    def number_of_subscriptions
      if !block_given?
        return @j_del.java_method(:numberOfSubscriptions, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling number_of_subscriptions()"
    end
    #  Checks whether or not the given address matches with the current destination.
    # @param [String] address the address
    # @return [true,false] <code>true</code> if it matches, <code>false</code> otherwise.
    def matches?(address=nil)
      if address.class == String && !block_given?
        return @j_del.java_method(:matches, [Java::java.lang.String.java_class]).call(address)
      end
      raise ArgumentError, "Invalid arguments when calling matches?(address)"
    end
  end
end
