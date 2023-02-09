##
#    This code was generated by
#    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
#     |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
#     |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
#
#    Twilio - Accounts
#    This is the public Twilio REST API.
#
#    NOTE: This class is auto generated by OpenAPI Generator.
#    https://openapi-generator.tech
#    Do not edit the class manually.
#

module Twilio
    module REST
        class FlexApi
            class V1 < Version
                ##
                # Initialize the V1 version of FlexApi
                def initialize(domain)
                    super
                    @version = 'v1'
                    @calls = nil
                    @credentials = nil
                end

                ##
                # @param [String] sid 
                # @return [Twilio::REST::FlexApi::V1::CallContext] if sid was passed.
                # @return [Twilio::REST::FlexApi::V1::CallList]
                def calls(sid=:unset)
                    if sid.nil?
                        raise ArgumentError, 'sid cannot be nil'
                    end
                    if sid == :unset
                        @calls ||= CallList.new self
                    else
                        CallContext.new(self, sid)
                    end
                end
                ##
                # @param [String] sid 
                # @return [Twilio::REST::FlexApi::V1::CredentialContext] if sid was passed.
                # @return [Twilio::REST::FlexApi::V1::CredentialList]
                def credentials(sid=:unset)
                    if sid.nil?
                        raise ArgumentError, 'sid cannot be nil'
                    end
                    if sid == :unset
                        @credentials ||= CredentialList.new self
                    else
                        CredentialContext.new(self, sid)
                    end
                end
                ##
                # Provide a user friendly representation
                def to_s
                    '<Twilio::REST::FlexApi::V1>';
                end
            end
        end
    end
end
