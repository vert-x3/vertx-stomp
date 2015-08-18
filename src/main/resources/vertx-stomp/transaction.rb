require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Transaction
module VertxStomp
  #  Represents a transaction in the STOMP server.
  class Transaction
    # @private
    # @param j_del [::VertxStomp::Transaction] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Transaction] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates an instance of {::VertxStomp::Transaction} using the default implementation.
    # @param [::VertxStomp::StompServerConnection] connection the connection (client)
    # @param [String] id the transaction id
    # @return [::VertxStomp::Transaction] the created {::VertxStomp::Transaction}
    def self.create(connection=nil,id=nil)
      if connection.class.method_defined?(:j_del) && id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Transaction.java_method(:create, [Java::IoVertxExtStomp::StompServerConnection.java_class,Java::java.lang.String.java_class]).call(connection.j_del,id),::VertxStomp::Transaction)
      end
      raise ArgumentError, "Invalid arguments when calling create(connection,id)"
    end
    #  @return the connection
    # @return [::VertxStomp::StompServerConnection]
    def connection
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:connection, []).call(),::VertxStomp::StompServerConnection)
      end
      raise ArgumentError, "Invalid arguments when calling connection()"
    end
    #  @return the transaction id
    # @return [String]
    def id
      if !block_given?
        return @j_del.java_method(:id, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    #  Adds a frame to the transaction. By default, only <code>SEND, ACK and NACK</code> frames can be in transactions.
    # @param [Hash] frame the frame to add
    # @return [true,false] <code>true</code> if the frame was added, <code>false</code> otherwise. Main failure reason is the number of frames stored in the transaction that have exceed the number of allowed frames in transaction.
    def add_frame_to_transaction?(frame=nil)
      if frame.class == Hash && !block_given?
        return @j_del.java_method(:addFrameToTransaction, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
      end
      raise ArgumentError, "Invalid arguments when calling add_frame_to_transaction?(frame)"
    end
    #  Clears the list of frames added to the transaction.
    # @return [self]
    def clear
      if !block_given?
        @j_del.java_method(:clear, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling clear()"
    end
    #  @return the ordered list of frames added to the transaction.
    # @return [Array<Hash>]
    def get_frames
      if !block_given?
        return @j_del.java_method(:getFrames, []).call().to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil }
      end
      raise ArgumentError, "Invalid arguments when calling get_frames()"
    end
  end
end
