##
#    This code was generated by
#    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
#    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
#    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
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
        class FlexApi < Domain
            class V1 < Version
                class CallList < ListResource
                    ##
                    # Initialize the CallList
                    # @param [Version] version Version that contains the resource
                    # @return [CallList] CallList
                    def initialize(version)
                        super(version)
                        # Path Solution
                        @solution = {  }
                        
                        
                    end
                
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.FlexApi.V1.CallList>'
                    end
                end
            end
        end
    end
end
