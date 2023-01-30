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
                class FeedbackCallSummaryList < ListResource
                    ##
                    # Initialize the FeedbackCallSummaryList
                    # @param [Version] version Version that contains the resource
                    # @return [FeedbackCallSummaryList] FeedbackCallSummaryList
                    def initialize(version, account_sid)
                        super(version)
                        # Path Solution
                        @solution = { account_sid: account_sid, }
                        
                        
                    end
                
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.Api.V2010.FeedbackCallSummaryList>'
                    end
                end

                class FeedbackCallSummaryContext < InstanceContext
                    ##
                    # Initialize the FeedbackCallSummaryContext
                    # @param [Version] version Version that contains the resource
                    # @param [String] account_sid 
                    # @param [String] sid 
                    # @return [FeedbackCallSummaryContext] FeedbackCallSummaryContext
                    def initialize(version, account_sid, sid)
                        super(version)

                        # Path Solution
                        @solution = { account_sid: account_sid, sid: sid,  }
                        @uri = "/Accounts/#{@solution[:account_sid]}/Calls/Feedback/Summary/#{@solution[:sid]}.json"

                        # Dependents
                    end
                    ##
                    # Update the FeedbackCallSummaryInstance
                    # @param [Date] end_date 
                    # @param [Date] start_date 
                     # @param [String] account_sid 
                    # @return [FeedbackCallSummaryInstance]
                    Updated FeedbackCallSummaryInstance
                    def update(end_date: nil , start_date: nil , account_sid: :unset )

                        data = Twilio::Values.of({
                         'EndDate' => Twilio.serialize_iso8601_date(end_date),
                         'StartDate' => Twilio.serialize_iso8601_date(start_date),
                        'AccountSid' => account_sid,
                        })

                        payload = @version.update('POST',@uri, data: data  )
                        FeedbackCallSummaryInstance.new(@version, payload,  account_sid: @solution[:account_sid],)
                    end


                    ##
                    # Provide a user friendly representation
                    def to_s
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Api.V2010.FeedbackCallSummaryContext #{context}>"
                    end

                    ##
                    # Provide a detailed, user friendly representation
                    def inspect
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Api.V2010.FeedbackCallSummaryContext #{context}>"
                    end
                end

                class FeedbackCallSummaryPage < Page
                    ##
                    # Initialize the FeedbackCallSummaryPage
                    # @param [Version] version Version that contains the resource
                    # @param [Response] response Response from the API
                    # @param [Hash] solution Path solution for the resource
                    # @return [FeedbackCallSummaryPage] FeedbackCallSummaryPage
                    def initialize(version, response, solution)
                        super(version, response)

                        # Path Solution
                        @solution = solution
                    end

                    ##
                    # Build an instance of FeedbackCallSummaryInstance
                    # @param [Hash] payload Payload response from the API
                    # @return [FeedbackCallSummaryInstance] FeedbackCallSummaryInstance
                    def get_instance(payload)
                        FeedbackCallSummaryInstance.new(@version, payload, account_sid: @solution[:account_sid])
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        '<Twilio.Api.V2010.FeedbackCallSummaryPage>'
                    end
                end

            end
        end
    end
end
