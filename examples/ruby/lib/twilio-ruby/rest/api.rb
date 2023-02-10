
module Twilio
    module REST
      class Api < ApiBase
        def v2010
            @v2010 ||= V2010.new self
        end
      end
    end
end
