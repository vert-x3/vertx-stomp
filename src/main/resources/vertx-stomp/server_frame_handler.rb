require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.ServerFrameHandler
module VertxStomp
  #  Handles a server frame. This type of handler are called when the server receives a STOMP frame and let implement
  #  the behavior.
  class ServerFrameHandler
    # @private
    # @param j_del [::VertxStomp::ServerFrameHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::ServerFrameHandler] the underlying java delegate
    def j_del
      @j_del
    end
    #  Handler called when a server frame has been received.
    # @param [Hash] frame the frame
    # @param [::VertxStomp::StompServerConnection] connection the server connection that has received the frame
    # @return [void]
    def on_frame(frame=nil,connection=nil)
      if frame.class == Hash && connection.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:onFrame, [Java::IoVertxExtStomp::Frame.java_class,Java::IoVertxExtStomp::StompServerConnection.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)),connection.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling on_frame(frame,connection)"
    end
  end
end
