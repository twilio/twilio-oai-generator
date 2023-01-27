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
        class Api < Domain
            class V2010 < Version
                class CallList < ListResource
                    ##
                    # Initialize the CallList
                    # @param [Version] version Version that contains the resource
                    # @return [CallList] CallList
                    def initialize(version, account_sid)
                        super(version)
                        # Path Solution
                        @solution = { account_sid: account_sid, }
                        @uri = "/Accounts/#{@solution[:account_sid]}/Calls.json"
                        
                    end
                
                    ##
                    # Create the CallInstance
                    # @param [String] required_string_property 
                    # @param [String] test_method The HTTP method that we should use to request the &#x60;TestArrayOfUri&#x60;.
                    # @param [Array&lt;String&gt;] test_array_of_strings 
                    # @param [Array&lt;String&gt;] test_array_of_uri 
                    # @return [CallInstance] Created CallInstance
                    def create(required_string_property: nil,
						test_method: nil,
						test_array_of_strings: :unset,
						test_array_of_uri: :unset
					)
                        data = Twilio::Values.of(
                            
                            'RequiredStringProperty' => required_string_property,
                            
                            'TestMethod' => test_method,
                            
                            'TestArrayOfStrings' =>  Twilio.serialize_list(test_array_of_strings) { |e| e },

                            'TestArrayOfUri' =>  Twilio.serialize_list(test_array_of_uri) { |e| e },
                        })

                        payload = @version.create('POST', @uri, data: data)

                        CallInstance.new(@version, payload,  account_sid: @solution[:account_sid],)
                    end
                    
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.Api.V2010.CallList>'
                    end
                end
                class CallPage < Page
                    ##
                    # Initialize the CallPage
                    # @param [Version] version Version that contains the resource
                    # @param [Response] response Response from the API
                    # @param [Hash] solution Path solution for the resource
                    # @return [CallPage] CallPage
                    def initialize(version, response, solution)
                        super(version, response)

                        # Path Solution
                        @solution = solution
                    end

                    ##
                    # Build an instance of CallInstance
                    # @param [Hash] payload Payload response from the API
                    # @return [CallInstance] CallInstance
                    def get_instance(payload)
                        CallInstance.new(@version, payload, account_sid: @solution[:account_sid])
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        '<Twilio.Api.V2010.CallPage>'
                    end
                end
            end
        end
    end
end
