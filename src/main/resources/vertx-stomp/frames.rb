require 'vertx-stomp/stomp_server_connection'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Frames
module VertxStomp
  #  Utility methods to build common {Hash}s. It defines a non-STOMP frame (<code>PING</code>) that is used for
  #  heartbeats. When such frame is written on the wire it is just the <code>0</code> byte.
  #  <p/>
  #  This class is thread-safe.
  class Frames
    # @private
    # @param j_del [::VertxStomp::Frames] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Frames] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == Frames
    end
    def @@j_api_type.wrap(obj)
      Frames.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtStomp::Frames.java_class
    end
    # @param [String] message 
    # @param [Hash{String => String}] headers 
    # @param [String] body 
    # @return [Hash]
    def self.create_error_frame(message=nil,headers=nil,body=nil)
      if message.class == String && headers.class == Hash && body.class == String && !block_given?
        return Java::IoVertxExtStomp::Frames.java_method(:createErrorFrame, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::java.lang.String.java_class]).call(message,Hash[headers.map { |k,v| [k,v] }],body) != nil ? JSON.parse(Java::IoVertxExtStomp::Frames.java_method(:createErrorFrame, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class,Java::java.lang.String.java_class]).call(message,Hash[headers.map { |k,v| [k,v] }],body).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_error_frame(#{message},#{headers},#{body})"
    end
    # @param [String] receiptId 
    # @param [Hash{String => String}] headers 
    # @return [Hash]
    def self.create_receipt_frame(receiptId=nil,headers=nil)
      if receiptId.class == String && headers.class == Hash && !block_given?
        return Java::IoVertxExtStomp::Frames.java_method(:createReceiptFrame, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(receiptId,Hash[headers.map { |k,v| [k,v] }]) != nil ? JSON.parse(Java::IoVertxExtStomp::Frames.java_method(:createReceiptFrame, [Java::java.lang.String.java_class,Java::JavaUtil::Map.java_class]).call(receiptId,Hash[headers.map { |k,v| [k,v] }]).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_receipt_frame(#{receiptId},#{headers})"
    end
    # @param [Hash] frame 
    # @param [::VertxStomp::StompServerConnection] connection 
    # @return [void]
    def self.handle_receipt(frame=nil,connection=nil)
      if frame.class == Hash && connection.class.method_defined?(:j_del) && !block_given?
        return Java::IoVertxExtStomp::Frames.java_method(:handleReceipt, [Java::IoVertxExtStomp::Frame.java_class,Java::IoVertxExtStomp::StompServerConnection.java_class]).call(Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(frame)),connection.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling handle_receipt(#{frame},#{connection})"
    end
    # @return [Hash]
    def self.ping
      if !block_given?
        return Java::IoVertxExtStomp::Frames.java_method(:ping, []).call() != nil ? JSON.parse(Java::IoVertxExtStomp::Frames.java_method(:ping, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling ping()"
    end
  end
end
