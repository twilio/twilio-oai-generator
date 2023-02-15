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
          class CallContext < InstanceContext
            class FeedbackCallSummaryList < ListResource
              ##
              # Initialize the FeedbackCallSummaryList
              # @param [Version] version Version that contains the resource
              # @return [FeedbackCallSummaryList] FeedbackCallSummaryList
              def initialize(version, account_sid: nil)
                super(version)
                # Path Solution
                @solution = { account_sid: account_sid, }
              end

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
                @solution = { account_sid: account_sid, sid: sid, }
                @uri = "/Accounts/#{@solution[:account_sid]}/Calls/Feedback/Summary/#{@solution[:sid]}.json"
              end

              ##
              # Update the FeedbackCallSummaryInstance
              # @param [String] account_sid
              # @param [Date] end_date
              # @param [Date] start_date
              # @return [FeedbackCallSummaryInstance] Updated FeedbackCallSummaryInstance
              def update(
                account_sid: :unset,
                end_date: nil,
                start_date: nil
              )

                data = Twilio::Values.of({
                                           'EndDate' => Twilio.serialize_iso8601_date(end_date),
                                           'StartDate' => Twilio.serialize_iso8601_date(start_date),
                                           'AccountSid' => account_sid,
                                         })

                payload = @version.update('POST', @uri, data: data)
                FeedbackCallSummaryInstance.new(
                  @version,
                  payload,
                  account_sid: @solution[:account_sid],
                  sid: @solution[:sid],
                )
              end

              ##
              # Provide a user friendly representation
              def to_s
                context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
                "#<Twilio.Api.V2010.FeedbackCallSummaryContext #{context}>"
              end

              ##
              # Provide a detailed, user friendly representation
              def inspect
                context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
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

            class FeedbackCallSummaryInstance < InstanceResource
              ##
              # Initialize the FeedbackCallSummaryInstance
              # @param [Version] version Version that contains the resource
              # @param [Hash] payload payload that contains response from Twilio
              # @param [String] account_sid The SID of the
              #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this FeedbackCallSummary
              #   resource.
              # @param [String] sid The SID of the Call resource to fetch.
              # @return [FeedbackCallSummaryInstance] FeedbackCallSummaryInstance
              def initialize(version, payload, account_sid: nil, sid: nil)
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
                @params = { 'account_sid' => account_sid, 'sid' => sid || @properties['sid'], }
              end

              ##
              # Generate an instance context for the instance, the context is capable of
              # performing various actions.  All instance actions are proxied to the context
              # @return [FeedbackCallSummaryContext] CallContext for this CallInstance
              def context
                unless @instance_context
                  @instance_context = FeedbackCallSummaryContext.new(@version, @params['account_sid'],
                                                                     @params['sid'])
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
              # @return [TestStatus]
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
              # @return [Array<TestStatus>] Permissions authorized to the app
              def test_array_of_enum
                @properties['test_array_of_enum']
              end

              ##
              # Update the FeedbackCallSummaryInstance
              # @param [String] account_sid
              # @param [Date] end_date
              # @param [Date] start_date
              # @return [FeedbackCallSummaryInstance] Updated FeedbackCallSummaryInstance
              def update(
                account_sid: :unset,
                end_date: nil,
                start_date: nil
              )

                context.update(
                  account_sid: account_sid,
                  end_date: end_date,
                  start_date: start_date,
                )
              end

              ##
              # Provide a user friendly representation
              def to_s
                values = @params.map { |k, v| "#{k}: #{v}" }.join(" ")
                "<Twilio.Api.V2010.FeedbackCallSummaryInstance #{values}>"
              end

              ##
              # Provide a detailed, user friendly representation
              def inspect
                values = @properties.map { |k, v| "#{k}: #{v}" }.join(" ")
                "<Twilio.Api.V2010.FeedbackCallSummaryInstance #{values}>"
              end
            end
          end
        end
      end
    end
  end
end
