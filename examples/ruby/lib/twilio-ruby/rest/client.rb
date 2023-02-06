module Twilio
    module REST
        ##
        # A client for accessing the Twilio API.
        class Client < ClientBase

            ##
            # Access the Api Twilio Domain
            def api
                @api ||= Api.new self
            end

            ##
            # @param [integer] testInteger INTEGER ID param!!!

            # @return [Twilio::REST::Api::V2010::AccountContext::CallInstance] if testInteger was passed.
            # @return [Twilio::REST::Api::V2010::AccountContext::CallList]
            def calls(testInteger=:unset)
                self.api.v2010.account.calls(testInteger)
            end

            ##
            # Provide a user friendly representation
            def to_s
                "#<Twilio::REST::Client #{@account_sid}>"
            end
        end
    end
end