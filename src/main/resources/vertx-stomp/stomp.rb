require 'vertx-stomp/stomp_client'
require 'vertx/net_server'
require 'vertx/vertx'
require 'vertx-stomp/stomp_server'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Stomp
module VertxStomp
  #  Interface used to create STOMP server and clients.
  class Stomp
    # @private
    # @param j_del [::VertxStomp::Stomp] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Stomp] the underlying java delegate
    def j_del
      @j_del
    end
    # @overload createStompServer(vertx)
    #   @param [::Vertx::Vertx] vertx 
    # @overload createStompServer(vertx,options)
    #   @param [::Vertx::Vertx] vertx 
    #   @param [Hash] options 
    # @overload createStompServer(vertx,netServer)
    #   @param [::Vertx::Vertx] vertx 
    #   @param [::Vertx::NetServer] netServer 
    # @overload createStompServer(vertx,netServer,options)
    #   @param [::Vertx::Vertx] vertx 
    #   @param [::Vertx::NetServer] netServer 
    #   @param [Hash] options 
    # @return [::VertxStomp::StompServer]
    def self.create_stomp_server(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && !block_given? && param_2 == nil && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompServer, [Java::IoVertxCore::Vertx.java_class]).call(param_1.j_del),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtStomp::StompServerOptions.java_class]).call(param_1.j_del,Java::IoVertxExtStomp::StompServerOptions.new(::Vertx::Util::Utils.to_json_object(param_2))),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCoreNet::NetServer.java_class]).call(param_1.j_del,param_2.j_del),::VertxStomp::StompServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && param_3.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCoreNet::NetServer.java_class,Java::IoVertxExtStomp::StompServerOptions.java_class]).call(param_1.j_del,param_2.j_del,Java::IoVertxExtStomp::StompServerOptions.new(::Vertx::Util::Utils.to_json_object(param_3))),::VertxStomp::StompServer)
      end
      raise ArgumentError, "Invalid arguments when calling create_stomp_server(param_1,param_2,param_3)"
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [Hash] options 
    # @return [::VertxStomp::StompClient]
    def self.create_stomp_client(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompClient, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxStomp::StompClient)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtStomp::Stomp.java_method(:createStompClient, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtStomp::StompClientOptions.java_class]).call(vertx.j_del,Java::IoVertxExtStomp::StompClientOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxStomp::StompClient)
      end
      raise ArgumentError, "Invalid arguments when calling create_stomp_client(vertx,options)"
    end
  end
end
