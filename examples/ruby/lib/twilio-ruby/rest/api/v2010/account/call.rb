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
    class Api < ApiBase
      class V2010 < Version
        class AccountContext < InstanceContext
          class CallList < ListResource
            ##
            # Initialize the CallList
            # @param [Version] version Version that contains the resource
            # @return [CallList] CallList
            def initialize(version, account_sid: nil)
              super(version)
              # Path Solution
              @solution = { account_sid: account_sid }
              @uri = "/Accounts/#{@solution[:account_sid]}/Calls.json"
              # Components
              @feedback_call_summary = nil
            end

            ##
            # Create the CallInstance
            # @param [String] required_string_property
            # @param [Array[String]] test_array_of_strings
            # @param [Array[String]] test_array_of_uri
            # @param [String] test_method The HTTP method that we should use to request the `TestArrayOfUri`.
            # @return [CallInstance] Created CallInstance
            def create(
              required_string_property: nil,
              test_array_of_strings: :unset,
              test_array_of_uri: :unset,
              test_method: nil
            )

              data = Twilio::Values.of({
                                         'RequiredStringProperty' => required_string_property,
                                         'TestMethod' => test_method,
                                         'TestArrayOfStrings' => Twilio.serialize_list(test_array_of_strings) { |e|
                                                                   e
                                                                 },
                                         'TestArrayOfUri' => Twilio.serialize_list(test_array_of_uri) { |e| e },
                                       })

              payload = @version.create('POST', @uri, data: data)
              CallInstance.new(
                @version,
                payload,
                account_sid: @solution[:account_sid],
              )
            end

            ##
            # Access the feedback_call_summary
            # @return [FeedbackCallSummaryList]
            # @return [FeedbackCallSummaryContext]
            def feedback_call_summary
              @feedback_call_summary ||= FeedbackCallSummaryList.new(@version,
                                                                     account_sid: @solution[:account_sid])
            end

            # Provide a user friendly representation
            def to_s
              '#<Twilio.Api.V2010.CallList>'
            end
          end

          class CallContext < InstanceContext
            ##
            # Initialize the CallContext
            # @param [Version] version Version that contains the resource
            # @param [String] account_sid
            # @param [String] test_integer INTEGER ID param!!!
            # @return [CallContext] CallContext
            def initialize(version, account_sid, test_integer)
              super(version)

              # Path Solution
              @solution = { account_sid: account_sid, test_integer: test_integer, }
              @uri = "/Accounts/#{@solution[:account_sid]}/Calls/#{@solution[:test_integer]}.json"

              # Dependents
              @feedback_call_summary = nil
            end

            ##
            # Delete the CallInstance
            # @return [Boolean] True if delete succeeds, false otherwise
            def delete
              @version.delete('DELETE', @uri)
            end

            ##
            # Fetch the CallInstance
            # @return [CallInstance] Fetched CallInstance
            def fetch
              payload = @version.fetch('GET', @uri)
              CallInstance.new(
                @version,
                payload,
                account_sid: @solution[:account_sid],
                test_integer: @solution[:test_integer],
              )
            end

            ##
            # Access the feedback_call_summary
            # @return [FeedbackCallSummaryList]
            # @return [FeedbackCallSummaryContext] if sid was passed.
            def feedback_call_summary(sid = :unset)
              raise ArgumentError, 'sid cannot be nil' if sid.nil?

              if sid != :unset
                return FeedbackCallSummaryContext.new(@version, @solution[:account_sid],
                                                      @solution[:test_integer], sid)
              end

              unless @feedback_call_summary
                @feedback_call_summary = FeedbackCallSummaryList.new(
                  @version,
                  account_sid: @solution[:account_sid],
                  call_test_integer: @solution[:test_integer]
                )
              end

              @feedback_call_summary
            end

            ##
            # Provide a user friendly representation
            def to_s
              context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
              "#<Twilio.Api.V2010.CallContext #{context}>"
            end

            ##
            # Provide a detailed, user friendly representation
            def inspect
              context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
              "#<Twilio.Api.V2010.CallContext #{context}>"
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

          class CallInstance < InstanceResource
            ##
            # Initialize the CallInstance
            # @param [Version] version Version that contains the resource
            # @param [Hash] payload payload that contains response from Twilio
            # @param [String] account_sid The SID of the
            #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this Call
            #   resource.
            # @param [String] sid The SID of the Call resource to fetch.
            # @return [CallInstance] CallInstance
            def initialize(version, payload, account_sid: nil, test_integer: nil)
              super(version)

              # Marshaled Properties
              @properties = {
                'account_sid' => payload['account_sid'],
                'sid' => payload['sid'],
                'test_string' => payload['test_string'],
                'test_integer' => payload['test_integer'] == nil ? payload['test_integer'] : payload['test_integer'].to_i,
                'test_object' => payload['test_object'],
                'test_date_time' => Twilio.deserialize_rfc2822(payload['test_date_time']),
                'test_number' => payload['test_number'],
                'from' => payload['from'],
                'price_unit' => payload['price_unit'],
                'test_number_float' => payload['test_number_float'],
                'test_number_decimal' => payload['test_number_decimal'] == nil ? payload['test_number_decimal'] : payload['test_number_decimal'].to_f,
                'test_enum' => payload['test_enum'],
                'a2p_profile_bundle_sid' => payload['a2p_profile_bundle_sid'],
                'test_array_of_integers' => payload['test_array_of_integers'],
                'test_array_of_array_of_integers' => payload['test_array_of_array_of_integers'],
                'test_array_of_objects' => payload['test_array_of_objects'],
                'test_array_of_enum' => payload['test_array_of_enum'],
              }

              # Context
              @instance_context = nil
              @params = { 'account_sid' => account_sid,
                          'test_integer' => test_integer || @properties['test_integer'], }
            end

            ##
            # Generate an instance context for the instance, the context is capable of
            # performing various actions.  All instance actions are proxied to the context
            # @return [CallContext] CallContext for this CallInstance
            def context
              unless @instance_context
                @instance_context = CallContext.new(@version, @params['account_sid'],
                                                    @params['test_integer'])
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
            # @return [String]
            def test_integer
              @properties['test_integer']
            end

            ##
            # @return [TestResponseObjectTestObject]
            def test_object
              @properties['test_object']
            end

            ##
            # @return [Time]
            def test_date_time
              @properties['test_date_time']
            end

            ##
            # @return [Float]
            def test_number
              @properties['test_number']
            end

            ##
            # @return [String]
            def from
              @properties['from']
            end

            ##
            # @return [String]
            def price_unit
              @properties['price_unit']
            end

            ##
            # @return [Float]
            def test_number_float
              @properties['test_number_float']
            end

            ##
            # @return [Float]
            def test_number_decimal
              @properties['test_number_decimal']
            end

            ##
            # @return [Status]
            def test_enum
              @properties['test_enum']
            end

            ##
            # @return [String] A2P Messaging Profile Bundle BundleSid
            def a2p_profile_bundle_sid
              @properties['a2p_profile_bundle_sid']
            end

            ##
            # @return [Array<String>]
            def test_array_of_integers
              @properties['test_array_of_integers']
            end

            ##
            # @return [Array<Array<String>>]
            def test_array_of_array_of_integers
              @properties['test_array_of_array_of_integers']
            end

            ##
            # @return [Array<TestResponseObjectTestArrayOfObjects>]
            def test_array_of_objects
              @properties['test_array_of_objects']
            end

            ##
            # @return [Array<Status>] Permissions authorized to the app
            def test_array_of_enum
              @properties['test_array_of_enum']
            end

            ##
            # Delete the CallInstance
            # @return [Boolean] True if delete succeeds, false otherwise
            def delete
              context.delete
            end

            ##
            # Fetch the CallInstance
            # @return [CallInstance] Fetched CallInstance
            def fetch
              context.fetch
            end

            ##
            # Access the feedback_call_summary
            # @return [feedback_call_summary] feedback_call_summary
            def feedback_call_summary
              context.feedback_call_summary
            end

            ##
            # Provide a user friendly representation
            def to_s
              values = @params.map { |k, v| "#{k}: #{v}" }.join(" ")
              "<Twilio.Api.V2010.CallInstance #{values}>"
            end

            ##
            # Provide a detailed, user friendly representation
            def inspect
              values = @properties.map { |k, v| "#{k}: #{v}" }.join(" ")
              "<Twilio.Api.V2010.CallInstance #{values}>"
            end
          end
        end
      end
    end
  end
end
