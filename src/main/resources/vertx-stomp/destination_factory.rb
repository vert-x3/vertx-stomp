require 'vertx-stomp/destination'
require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.DestinationFactory
module VertxStomp
  #  Interface implemented to customize the destination creation.
  class DestinationFactory
    # @private
    # @param j_del [::VertxStomp::DestinationFactory] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::DestinationFactory] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates a destination for the given <em>address</em>.
    # @param [::Vertx::Vertx] vertx the vert.x instance used by the STOMP server.
    # @param [String] name the name
    # @return [::VertxStomp::Destination] the destination, <code>null</code> to reject the creation.
    def create(vertx=nil,name=nil)
      if vertx.class.method_defined?(:j_del) && name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,name),::VertxStomp::Destination)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,name)"
    end
  end
end
