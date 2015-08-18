require 'vertx-stomp/subscription'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.Acknowledgement
module VertxStomp
  #  Structure passed to acknowledgement handler called when a <code>ACK</code> or <code>NACK</code> frame is received. The handler
  #  receives an instance of {::VertxStomp::Acknowledgement} with the {::VertxStomp::Subscription} and the impacted messages. The list
  #  of messages depends on the type of acknowledgment used by the subscription.
  #  <p/>
  #  Subscriptions using the <code>client</code> mode receives all messages that were waiting for acknowledgment that were
  #  sent before the acknowledged messages. The list also contains the acknowledged message. This is a cumulative
  #  acknowledgement. Subscriptions using the <code>client-individual</code> mode receives a singleton list containing only
  #  the acknowledged message.
  class Acknowledgement
    # @private
    # @param j_del [::VertxStomp::Acknowledgement] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::Acknowledgement] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the subscription
    # @return [::VertxStomp::Subscription]
    def subscription
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:subscription, []).call(),::VertxStomp::Subscription)
      end
      raise ArgumentError, "Invalid arguments when calling subscription()"
    end
    #  @return the list of frames that have been acknowledged / not-acknowledged. The content of the list depends on
    #  the type of subscription.
    # @return [Array<Hash>]
    def frames
      if !block_given?
        return @j_del.java_method(:frames, []).call().to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil }
      end
      raise ArgumentError, "Invalid arguments when calling frames()"
    end
  end
end
