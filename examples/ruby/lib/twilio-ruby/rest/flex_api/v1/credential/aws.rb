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
             class CredentialContext < InstanceContext
            
                     class AwsList < ListResource
                    ##
                    # Initialize the AwsList
                    # @param [Version] version Version that contains the resource
                    # @return [AwsList] AwsList
                    def initialize(version)
                        super(version)
                        # Path Solution
                        @solution = {  }
                        @uri = "/Credentials/AWS"
                        
                    end
                
                    
                    ##
                    # Lists AwsInstance records from the API as a list.
                    # Unlike stream(), this operation is eager and will load `limit` records into
                    # memory before returning.
                    # @param [Integer] limit Upper limit for the number of records to return. stream()
                    #    guarantees to never return more than limit.  Default is no limit
                    # @param [Integer] page_size Number of records to fetch per request, when
                    #    not set will use the default value of 50 records.  If no page_size is defined
                    #    but a limit is defined, stream() will attempt to read the limit with the most
                    #    efficient page size, i.e. min(limit, 1000)
                    # @return [Array] Array of up to limit results
                    def list( limit: nil, page_size: nil)
                        self.stream(
                            limit: limit,
                            page_size: page_size
                        ).entries
                    end

                    ##
                    # Streams Instance records from the API as an Enumerable.
                    # This operation lazily loads records as efficiently as possible until the limit
                    # is reached.
                    # @param [Integer] limit Upper limit for the number of records to return. stream()
                    #    guarantees to never return more than limit.  Default is no limit
                    # @param [Integer] page_size Number of records to fetch per request, when
                    #    not set will use the default value of 50 records.  If no page_size is defined
                    #    but a limit is defined, stream() will attempt to read the limit with the most
                    #    efficient page size, i.e. min(limit, 1000)
                    # @return [Enumerable] Enumerable that will yield up to limit results
                    def stream( limit: nil, page_size: nil)
                        limits = @version.read_limits(limit, page_size)

                        page = self.page(
                            page_size: limits[:page_size], )

                        @version.stream(page, limit: limits[:limit], page_limit: limits[:page_limit])
                    end

                    ##
                    # When passed a block, yields AwsInstance records from the API.
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
                    # Retrieve a single page of AwsInstance records from the API.
                    # Request is executed immediately.
                    # @param [String] page_token PageToken provided by the API
                    # @param [Integer] page_number Page Number, this value is simply for client state
                    # @param [Integer] page_size Number of records to return, defaults to 50
                    # @return [Page] Page of AwsInstance
                    def page( page_token: :unset, page_number: :unset, page_size: :unset)
                        params = Twilio::Values.of({
                            
                            'PageToken' => page_token,
                            'Page' => page_number,
                            'PageSize' => page_size,
                        })

                        response = @version.page('GET', @uri, params: params)

                        AwsPage.new(@version, response, @solution)
                    end

                    ##
                    # Retrieve a single page of AwsInstance records from the API.
                    # Request is executed immediately.
                    # @param [String] target_url API-generated URL for the requested results page
                    # @return [Page] Page of AwsInstance
                    def get_page(target_url)
                        response = @version.domain.request(
                            'GET',
                            target_url
                        )
                    AwsPage.new(@version, response, @solution)
                    end
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.FlexApi.V1.AwsList>'
                    end
                end


                class AwsContext < InstanceContext
                    ##
                    # Initialize the AwsContext
                    # @param [Version] version Version that contains the resource
                    # @param [String] sid 
                    # @return [AwsContext] AwsContext
                    def initialize(version, sid)
                        super(version)

                        # Path Solution
                        @solution = { sid: sid,  }
                        @uri = "/Credentials/AWS/#{@solution[:sid]}"

                        # Dependents
                        @history = nil
                    end
                    ##
                    # Delete the AwsInstance
                    # @return [Boolean] True if delete succeeds, false otherwise
                    def delete

                        @version.delete('DELETE', @uri, )
                    end

                    ##
                    # Fetch the AwsInstance
                    # @return [AwsInstance]
                    Fetched AwsInstance
                    def fetch

                        payload = @version.fetch('GET',@uri )
                        AwsInstance.new(@version, payload, )
                    end

                    ##
                    # Update the AwsInstance
                     # @param [String] test_string 
                     # @param [Boolean] test_boolean 
                    # @return [AwsInstance]
                    Updated AwsInstance
                    def update(test_string: :unset, , test_boolean: :unset )

                        data = Twilio::Values.of({
                        'TestString' => test_string,
                        'TestBoolean' => test_boolean,
                        })

                        payload = @version.update('POST',@uri, data: data  )
                        AwsInstance.new(@version, payload, )
                    end

                    ##
                    # Access the history
                    # @return [HistoryList]
                    # @return [HistoryContext] if sid was passed.
                    def history()
                        if 
                            return HistoryContext.new(@version)
                        end
                        unless @history
                            @history = HistoryList.new(
                                @version
                                )
                        end

                     @history
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.FlexApi.V1.AwsContext #{context}>"
                    end

                    ##
                    # Provide a detailed, user friendly representation
                    def inspect
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.FlexApi.V1.AwsContext #{context}>"
                    end
                end

                class AwsPage < Page
                    ##
                    # Initialize the AwsPage
                    # @param [Version] version Version that contains the resource
                    # @param [Response] response Response from the API
                    # @param [Hash] solution Path solution for the resource
                    # @return [AwsPage] AwsPage
                    def initialize(version, response, solution)
                        super(version, response)

                        # Path Solution
                        @solution = solution
                    end

                    ##
                    # Build an instance of AwsInstance
                    # @param [Hash] payload Payload response from the API
                    # @return [AwsInstance] AwsInstance
                    def get_instance(payload)
                        AwsInstance.new(@version, payload)
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        '<Twilio.FlexApi.V1.AwsPage>'
                    end
                end
                class AwsInstance < InstanceResource
                    ##
                    # Initialize the AwsInstance
                    # @param [Version] version Version that contains the resource
                    # @param [Hash] payload payload that contains response from Twilio
                    # @param [String] account_sid The SID of the
                    #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this Aws
                    #   resource.
                    # @param [String] sid The SID of the Call resource to fetch.
                    # @return [AwsInstance] AwsInstance
                    def initialize(version, payload , sid: nil)
                        super(version)
                        # Marshaled Properties
                        @properties = { 
                            'account_sid' => payload['account_sid'],
                            'sid' => payload['sid'],
                            'test_string' => payload['test_string'],
                            'test_integer' => payload['test_integer'] == nil ? payload['test_integer'] : payload['test_integer'].to_i,
                             } 
                        # Context
                        @instance_context = nil
                        @params = { 'sid' => sid  || @properties['sid']  , }
                    end

                    ##
                    # Generate an instance context for the instance, the context is capable of
                    # performing various actions.  All instance actions are proxied to the context
                    # @return [AwsContext] CallContext for this CallInstance
                    def context
                        unless @instance_context
                            @instance_context = AwsContext.new(@version , @params['sid'])
                        end
                        @instance_context
                    end
                    
                    ##
                    # @return [String] 
                    def account_sid
                        @properties['account_sid']
                    end
                    
                    ##
                    # @return [String] 
                    def sid
                        @properties['sid']
                    end
                    
                    ##
                    # @return [String] 
                    def test_string
                        @properties['test_string']
                    end
                    
                    ##
                    # @return [Integer] 
                    def test_integer
                        @properties['test_integer']
                    end
                    
                    
                    ##
                    # Delete the AwsInstance
                    # @return [Boolean] true if delete succeeds, false otherwise
                    def delete
                        context.delete
                    end
                    
                    
                    ##
                    # Fetch the AwsInstance
                    # @return [AwsInstance] Fetched AwsInstance
                    def fetch
                        context.fetch
                    end
                    
                    
                    ##
                    # Update the AwsInstance
                    # @param [String] test_string 
                    # @param [Boolean] test_boolean 
                    # @return [AwsInstance]
                    # Updated AwsInstance
                    def update(test_string: :unset , test_boolean: :unset  )
                        context.update(
                            test_string: test_string, 
                            test_boolean: test_boolean, )
                     end
                    
                    ##
                    # Access the history
                    # @return [history] history
                    def history
                        context.history
                    end
                    ##
                    # Provide a user friendly representation
                    def to_s
                        values = @params.map{|k, v| "#{k}: #{v}"}.join(" ")
                        "<Twilio.FlexApi.V1.AwsInstance #{values}>"
                    end

                    ##
                    # Provide a detailed, user friendly representation
                    def inspect
                        values = @properties.map{|k, v| "#{k}: #{v}"}.join(',')
                        "<Twilio.FlexApi.V1.AwsInstance #{values}>"
                    end
                end

             end
            end
        end
    end
end


