require 'vertx-stomp/subscription'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.AcknowledgmentHandler
module VertxStomp
  #  Handler called when a <code>ACK</code> or <code>NACK</code> frame is received. The handler receives the {::VertxStomp::Subscription}
  #  and the impacted messages. This list of messages depends on the type of acknowledgment used by the subscription.
  #  Subscriptions using <code>auto</code> do not call this handler (because there are no <code>ACK/NACK</code> in @{code auto}).
  #  Subscriptions using the <code>client</code> mode receives all messages that were waiting for acknowledgment that were
  #  sent before the acknowledged messages. The list also contains the acknowledged message. This is a cumulative
  #  acknowledgement. Subscriptions using the <code>client-individual</code> mode receives a singleton list containing only
  #  the acknowledged message.
  class AcknowledgmentHandler
    # @private
    # @param j_del [::VertxStomp::AcknowledgmentHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::AcknowledgmentHandler] the underlying java delegate
    def j_del
      @j_del
    end
    #  Called when a <code>ACK / NACK</code> frame is received.
    # @param [::VertxStomp::Subscription] subscription the subscription
    # @param [Array<Hash>] frames the impacted frames. If the subscription uses the <code>client</code> mode, it contains all impacted messages (cumulative acknowledgment). In <code>client-individual</code> mode, the list contains only the acknowledged frame.
    # @return [void]
    def handle(subscription=nil,frames=nil)
      if subscription.class.method_defined?(:j_del) && frames.class == Array && !block_given?
        return @j_del.java_method(:handle, [Java::IoVertxExtStomp::Subscription.java_class,Java::JavaUtil::List.java_class]).call(subscription.j_del,frames.map { |element| Java::IoVertxExtStomp::Frame.new(::Vertx::Util::Utils.to_json_object(element)) })
      end
      raise ArgumentError, "Invalid arguments when calling handle(subscription,frames)"
    end
  end
end
