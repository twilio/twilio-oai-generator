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
                class AccountList < ListResource
                    ##
                    # Initialize the AccountList
                    # @param [Version] version Version that contains the resource
                    # @return [AccountList] AccountList
                    def initialize(version)
                        super(version)
                        # Path Solution
                        @solution = {  }
                        @uri = "/Accounts.json"
                        
                    end
                
                    ##
                    # Create the AccountInstance
                    # @param [String] x_twilio_webhook_enabled 
                    # @param [String] recording_status_callback 
                    # @param [Array&lt;String&gt;] recording_status_callback_event 
                    # @param [String] twiml 
                    # @return [AccountInstance] Created AccountInstance
                    def create(x_twilio_webhook_enabled: :unset,
						recording_status_callback: :unset,
						recording_status_callback_event: :unset,
						twiml: :unset
					)
                        data = Twilio::Values.of(
                            
                            'X-Twilio-Webhook-Enabled' => x_twilio_webhook_enabled,
                            
                            'RecordingStatusCallback' => recording_status_callback,
                            
                            'RecordingStatusCallbackEvent' =>  Twilio.serialize_list(recording_status_callback_event) { |e| e },

                            'Twiml' => twiml,
                                                    })

                        payload = @version.create('POST', @uri, data: data)

                        AccountInstance.new(@version, payload, )
                    end
                    
                    
                    ##
                    # Lists AccountInstance records from the API as a list.
                    # Unlike stream(), this operation is eager and will load `limit` records into
                    # memory before returning.
                    # @param [Time] date_created 
                    # @param [Date] date_test 
                    # @param [Time] date_created_before 
                    # @param [Time] date_created_after 
                    # @param [Integer] limit Upper limit for the number of records to return. stream()
                    #    guarantees to never return more than limit.  Default is no limit
                    # @param [Integer] page_size Number of records to fetch per request, when
                    #    not set will use the default value of 50 records.  If no page_size is defined
                    #    but a limit is defined, stream() will attempt to read the limit with the most
                    #    efficient page size, i.e. min(limit, 1000)
                    # @return [Array] Array of up to limit results
                    def list(date_created: :unset,date_test: :unset,date_created_before: :unset,date_created_after: :unset, limit: nil, page_size: nil)
                        self.stream(
                            date_created: date_created,
                            date_test: date_test,
                            date_created_before: date_created_before,
                            date_created_after: date_created_after,
                            limit: limit,
                            page_size: page_size
                        ).entries
                    end

                    ##
                    # Streams Instance records from the API as an Enumerable.
                    # This operation lazily loads records as efficiently as possible until the limit
                    # is reached.
                    # @param [Time] date_created 
                    # @param [Date] date_test 
                    # @param [Time] date_created_before 
                    # @param [Time] date_created_after 
                    # @param [Integer] limit Upper limit for the number of records to return. stream()
                    #    guarantees to never return more than limit.  Default is no limit
                    # @param [Integer] page_size Number of records to fetch per request, when
                    #    not set will use the default value of 50 records.  If no page_size is defined
                    #    but a limit is defined, stream() will attempt to read the limit with the most
                    #    efficient page size, i.e. min(limit, 1000)
                    # @return [Enumerable] Enumerable that will yield up to limit results
                    def stream(date_created: :unset,date_test: :unset,date_created_before: :unset,date_created_after: :unset, limit: nil, page_size: nil)
                        limits = @version.read_limits(limit, page_size)

                        page = self.page(
                            date_created: date_created,
                            date_test: date_test,
                            date_created_before: date_created_before,
                            date_created_after: date_created_after,
                            page_size: limits[:page_size], )

                        @version.stream(page, limit: limits[:limit], page_limit: limits[:page_limit])
                    end

                    ##
                    # When passed a block, yields AccountInstance records from the API.
                    # This operation lazily loads records as efficiently as possible until the limit
                    # is reached.
                    def each
                        limits = @version.read_limits

                        page = self.page(page_size: limits[:page_size], )

                        @version.stream(page,
                        limit: limits[:limit],
                        page_limit: limits[:page_limit]).each {|x| yield x}
                    end

                    ##
                    # Retrieve a single page of AccountInstance records from the API.
                    # Request is executed immediately.
                    # @param [Time] date_created 
                    # @param [Date] date_test 
                    # @param [Time] date_created_before 
                    # @param [Time] date_created_after 
                    # @param [String] page_token PageToken provided by the API
                    # @param [Integer] page_number Page Number, this value is simply for client state
                    # @param [Integer] page_size Number of records to return, defaults to 50
                    # @return [Page] Page of AccountInstance
                    def page(date_created: :unset,date_test: :unset,date_created_before: :unset,date_created_after: :unset, page_token: :unset, page_number: :unset, page_size: :unset)
                        params = Twilio::Values.of({
                            
                            'DateCreated' =>   Twilio.serialize_iso8601_datetime(date_created),
                            
                            'Date.Test' =>  Twilio.serialize_iso8601_date(date_test),
                            
                            'DateCreated<' =>   Twilio.serialize_iso8601_datetime(date_created_before),
                            
                            'DateCreated>' =>   Twilio.serialize_iso8601_datetime(date_created_after),
                            
                            'PageToken' => page_token,
                            'Page' => page_number,
                            'PageSize' => page_size,
                        })

                        response = @version.page('GET', @uri, params: params)

                        AccountPage.new(@version, response, @solution)
                    end

                    ##
                    # Retrieve a single page of AccountInstance records from the API.
                    # Request is executed immediately.
                    # @param [String] target_url API-generated URL for the requested results page
                    # @return [Page] Page of AccountInstance
                    def get_page(target_url)
                        response = @version.domain.request(
                            'GET',
                            target_url
                        )
                    AccountPage.new(@version, response, @solution)
                    end
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.Api.V2010.AccountList>'
                    end
                end

                class AccountContext < InstanceContext
                    ##
                    # Initialize the AccountContext
                    # @param [Version] version Version that contains the resource
                    # @param [String] sid 
                    # @return [AccountContext] AccountContext
                    def initialize(version, sid)
                        super(version)

                        # Path Solution
                        @solution = { sid: sid,  }
                        @uri = "/Accounts/#{@solution[:sid]}.json"

                        # Dependents
                        @calls = nil
                    end
                    ##
                    # Delete the AccountInstance
                    # @return [Boolean] True if delete succeeds, false otherwise
                    def delete

                        @version.delete('DELETE', @uri, )
                    end

                    ##
                    # Fetch the AccountInstance
                    # @return [AccountInstance]
                    Fetched AccountInstance
                    def fetch

                        payload = @version.fetch('GET',@uri )
                        AccountInstance.new(@version, payload, )
                    end

                    ##
                    # Update the AccountInstance
                    # @param [TestStatus] status 
                     # @param [String] pause_behavior 
                    # @return [AccountInstance]
                    Updated AccountInstance
                    def update(status: nil , pause_behavior: :unset )

                        data = Twilio::Values.of({
                          'Status' => status,
                        'PauseBehavior' => pause_behavior,
                        })

                        payload = @version.update('POST',@uri, data: data  )
                        AccountInstance.new(@version, payload, )
                    end

                    ##
                    # Access the calls
                    # @return [CallsList]
                    # @return [CallContext] if sid was passed.
                    def calls(test_integer=:unset)
                        raise ArgumentError, 'test_integer cannot be nil' if test_integer.nil?
                        if test_integer != :unset
                            return CallsContext.new(@version,test_integer )
                        end
                        unless @calls
                            @calls = CallList.new(
                                @version
                                )
                        end

                     @calls
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Api.V2010.AccountContext #{context}>"
                    end

                    ##
                    # Provide a detailed, user friendly representation
                    def inspect
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Api.V2010.AccountContext #{context}>"
                    end
                end

                class AccountPage < Page
                    ##
                    # Initialize the AccountPage
                    # @param [Version] version Version that contains the resource
                    # @param [Response] response Response from the API
                    # @param [Hash] solution Path solution for the resource
                    # @return [AccountPage] AccountPage
                    def initialize(version, response, solution)
                        super(version, response)

                        # Path Solution
                        @solution = solution
                    end

                    ##
                    # Build an instance of AccountInstance
                    # @param [Hash] payload Payload response from the API
                    # @return [AccountInstance] AccountInstance
                    def get_instance(payload)
                        AccountInstance.new(@version, payload)
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        '<Twilio.Api.V2010.AccountPage>'
                    end
                end
            end
        end
    end
end
