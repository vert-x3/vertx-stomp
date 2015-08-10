require 'vertx-stomp/stomp_client_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.ClientFrameHandler
module VertxStomp
  #  Handles a client frame. This type of handler are called when the client receives a STOMP frame and let implement
  #  the behavior.
  class ClientFrameHandler
    # @private
    # @param j_del [::VertxStomp::ClientFrameHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::ClientFrameHandler] the underlying java delegate
    def j_del
      @j_del
    end
    #  Handler called when a client frame has been received.
    # @param [Hash] frame the frame
    # @param [::VertxStomp::StompClientConnection] connection the client connection that has received the frame
    # @return [void]
    def on_frame(frame=nil,connection=nil)
      if frame.class == Hash && connection.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:onFrame, [Java::IoVertxExtStomp::Frame.java_class,Java::IoVertxExtStomp::StompClientConnection.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)),connection.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling on_frame(frame,connection)"
    end
  end
end
