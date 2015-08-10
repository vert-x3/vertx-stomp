require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.FrameHandler
module VertxStomp
  #  Handler called by {Nil} when a STOMP frame has been parsed. STOMP client and
  #  server use specialized versions passing the associated STOMP connection.
  class FrameHandler
    # @private
    # @param j_del [::VertxStomp::FrameHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::FrameHandler] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [Hash] frame 
    # @return [void]
    def on_frame(frame=nil)
      if frame.class == Hash && !block_given?
        return @j_del.java_method(:onFrame, [Java::IoVertxExtStomp::Frame.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)))
      end
      raise ArgumentError, "Invalid arguments when calling on_frame(frame)"
    end
  end
end
