require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.stomp.AuthenticationHandler
module VertxStomp
  #  Handler called when an authentication is required by the server. Authentication happen during the <code>CONNECT /
 CONNECTED</code> phase. The <code>CONNECT</code> frame should includes the <code>login</code> and <code>passcode</code> header
  #  (configured from the client option. The login and passcode are passed to the handler than can check whether or not
  #  the access is granted. When the decision has been made, it must called the <code>resultHandler</code> with the value
  #  <code>true</code> if the access if granted, <code>false</code> otherwise.
  class AuthenticationHandler
    # @private
    # @param j_del [::VertxStomp::AuthenticationHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxStomp::AuthenticationHandler] the underlying java delegate
    def j_del
      @j_del
    end
    #  The authentication handler responsible for checking whether the couple <code>login/passcode</code> are valid to
    #  connect to this server.
    # @param [String] login the login
    # @param [String] passcode the password
    # @yield the result handler invoked when the decision has been made. It receives <code>true</code> if the access is granted, <code>false</code> otherwise.
    # @return [void]
    def authenticate(login=nil,passcode=nil)
      if login.class == String && passcode.class == String && block_given?
        return @j_del.java_method(:authenticate, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(login,passcode,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling authenticate(login,passcode)"
    end
  end
end
