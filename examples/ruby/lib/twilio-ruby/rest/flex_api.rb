require 'twilio-ruby/rest'

module Twilio
    module REST
        class FlexApi < FlexApiBase;
            def v1
                @v1 ||= V1.new self
            end
        end
    end
end

