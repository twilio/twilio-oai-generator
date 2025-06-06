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
    class FlexApi < FlexApiBase
      class V1 < Version
        class CredentialList < ListResource
          class AwsContext < InstanceContext
            class HistoryList < ListResource
              ##
              # Initialize the HistoryList
              # @param [Version] version Version that contains the resource
              # @return [HistoryList] HistoryList
              def initialize(version, sid: nil)
                super(version)
                # Path Solution
                @solution = { sid: sid }
              end

              # Provide a user friendly representation
              def to_s
                '#<Twilio.FlexApi.V1.HistoryList>'
              end
            end

            class HistoryContext < InstanceContext
              ##
              # Initialize the HistoryContext
              # @param [Version] version Version that contains the resource
              # @param [String] sid
              # @return [HistoryContext] HistoryContext
              def initialize(version, sid)
                super(version)

                # Path Solution
                @solution = { sid: sid, }
                @uri = "/Credentials/AWS/#{@solution[:sid]}/History"
              end

              ##
              # Fetch the HistoryInstance
              # @param [Hash] add_ons_data
              # @return [HistoryInstance] Fetched HistoryInstance
              def fetch(
                add_ons_data: :unset
              )
                params = Twilio::Values.of({})
                params.merge!(Twilio.prefixed_collapsible_map(add_ons_data, 'AddOns'))
                headers = Twilio::Values.of({ 'Content-Type' => 'application/x-www-form-urlencoded', })

                payload = @version.fetch('GET', @uri, params: params, headers: headers)
                HistoryInstance.new(
                  @version,
                  payload,
                  sid: @solution[:sid],
                )
              end

              ##
              # Provide a user friendly representation
              def to_s
                context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
                "#<Twilio.FlexApi.V1.HistoryContext #{context}>"
              end

              ##
              # Provide a detailed, user friendly representation
              def inspect
                context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
                "#<Twilio.FlexApi.V1.HistoryContext #{context}>"
              end
            end

            class HistoryPage < Page
              ##
              # Initialize the HistoryPage
              # @param [Version] version Version that contains the resource
              # @param [Response] response Response from the API
              # @param [Hash] solution Path solution for the resource
              # @return [HistoryPage] HistoryPage
              def initialize(version, response, solution)
                super(version, response)

                # Path Solution
                @solution = solution
              end

              ##
              # Build an instance of HistoryInstance
              # @param [Hash] payload Payload response from the API
              # @return [HistoryInstance] HistoryInstance
              def get_instance(payload)
                HistoryInstance.new(@version, payload, sid: @solution[:sid])
              end

              ##
              # Provide a user friendly representation
              def to_s
                '<Twilio.FlexApi.V1.HistoryPage>'
              end
            end

            class HistoryInstance < InstanceResource
              ##
              # Initialize the HistoryInstance
              # @param [Version] version Version that contains the resource
              # @param [Hash] payload payload that contains response from Twilio
              # @param [String] account_sid The SID of the
              #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this History
              #   resource.
              # @param [String] sid The SID of the Call resource to fetch.
              # @return [HistoryInstance] HistoryInstance
              def initialize(version, payload, sid: nil)
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
                @params = { 'sid' => sid || @properties['sid'], }
              end

              ##
              # Generate an instance context for the instance, the context is capable of
              # performing various actions.  All instance actions are proxied to the context
              # @return [HistoryContext] CallContext for this CallInstance
              def context
                unless @instance_context
                  @instance_context = HistoryContext.new(@version, @params['sid'])
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
              # Fetch the HistoryInstance
              # @param [Hash] add_ons_data
              # @return [HistoryInstance] Fetched HistoryInstance
              def fetch(
                add_ons_data: :unset
              )
                context.fetch(
                  add_ons_data: add_ons_data,
                )
              end

              ##
              # Provide a user friendly representation
              def to_s
                values = @params.map { |k, v| "#{k}: #{v}" }.join(" ")
                "<Twilio.FlexApi.V1.HistoryInstance #{values}>"
              end

              ##
              # Provide a detailed, user friendly representation
              def inspect
                values = @properties.map { |k, v| "#{k}: #{v}" }.join(" ")
                "<Twilio.FlexApi.V1.HistoryInstance #{values}>"
              end
            end
          end
        end
      end
    end
  end
end
