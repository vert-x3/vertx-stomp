require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.ServerFrame
module VertxStomp
  #  Structure passed to server handler when receiving a frame. It provides a reference on the received {Hash}
  #  but also on the {::VertxStomp::StompServerConnection}.
  class ServerFrame
    # @private
    # @param j_del [::VertxStomp::ServerFrame] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::ServerFrame] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the received frame
    # @return [Hash]
    def frame
      if !block_given?
        return @j_del.java_method(:frame, []).call() != nil ? JSON.parse(@j_del.java_method(:frame, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling frame()"
    end
    #  @return the connection
    # @return [::VertxStomp::StompServerConnection]
    def connection
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:connection, []).call(),::VertxStomp::StompServerConnection)
      end
      raise ArgumentError, "Invalid arguments when calling connection()"
    end
  end
end
