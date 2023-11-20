module Twilio
  module REST
    class Accounts < AccountsBase
      def v1
        @v1 ||= V1.new self
      end
    end
  end
end
