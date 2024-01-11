##
#    This code was generated by
#    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
#     |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
#     |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
#
#    Twilio - Versionless
#    No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
#
#    NOTE: This class is auto generated by OpenAPI Generator.
#    https://openapi-generator.tech
#    Do not edit the class manually.
#

module Twilio
  module REST
    class Versionless < VersionlessBase
      class Understand < Version
        class AssistantList < ListResource
          ##
          # Initialize the AssistantList
          # @param [Version] version Version that contains the resource
          # @return [AssistantList] AssistantList
          def initialize(version)
            super(version)
            # Path Solution
            @solution = {}
            @uri = "/Assistants"
          end

          ##
          # Lists AssistantInstance records from the API as a list.
          # Unlike stream(), this operation is eager and will load `limit` records into
          # memory before returning.
          # @param [Integer] limit Upper limit for the number of records to return. stream()
          #    guarantees to never return more than limit.  Default is no limit
          # @param [Integer] page_size Number of records to fetch per request, when
          #    not set will use the default value of 50 records.  If no page_size is defined
          #    but a limit is defined, stream() will attempt to read the limit with the most
          #    efficient page size, i.e. min(limit, 1000)
          # @return [Array] Array of up to limit results
          def list(limit: nil, page_size: nil)
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
          def stream(limit: nil, page_size: nil)
            limits = @version.read_limits(limit, page_size)

            page = self.page(
              page_size: limits[:page_size],
            )

            @version.stream(page, limit: limits[:limit], page_limit: limits[:page_limit])
          end

          ##
          # When passed a block, yields AssistantInstance records from the API.
          # This operation lazily loads records as efficiently as possible until the limit
          # is reached.
          def each
            limits = @version.read_limits

            page = self.page(page_size: limits[:page_size],)

            @version.stream(page,
                            limit: limits[:limit],
                            page_limit: limits[:page_limit]).each { |x| yield x }
          end

          ##
          # Retrieve a single page of AssistantInstance records from the API.
          # Request is executed immediately.
          # @param [String] page_token PageToken provided by the API
          # @param [Integer] page_number Page Number, this value is simply for client state
          # @param [Integer] page_size Number of records to return, defaults to 50
          # @return [Page] Page of AssistantInstance
          def page(page_token: :unset, page_number: :unset, page_size: :unset)
            params = Twilio::Values.of({
                                         'PageToken' => page_token,
                                         'Page' => page_number,
                                         'PageSize' => page_size,
                                       })

            response = @version.page('GET', @uri, params: params)

            AssistantPage.new(@version, response, @solution)
          end

          ##
          # Retrieve a single page of AssistantInstance records from the API.
          # Request is executed immediately.
          # @param [String] target_url API-generated URL for the requested results page
          # @return [Page] Page of AssistantInstance
          def get_page(target_url)
            response = @version.domain.request(
              'GET',
              target_url
            )
            AssistantPage.new(@version, response, @solution)
          end

          # Provide a user friendly representation
          def to_s
            '#<Twilio.Versionless.Understand.AssistantList>'
          end
        end

        class AssistantPage < Page
          ##
          # Initialize the AssistantPage
          # @param [Version] version Version that contains the resource
          # @param [Response] response Response from the API
          # @param [Hash] solution Path solution for the resource
          # @return [AssistantPage] AssistantPage
          def initialize(version, response, solution)
            super(version, response)

            # Path Solution
            @solution = solution
          end

          ##
          # Build an instance of AssistantInstance
          # @param [Hash] payload Payload response from the API
          # @return [AssistantInstance] AssistantInstance
          def get_instance(payload)
            AssistantInstance.new(@version, payload)
          end

          ##
          # Provide a user friendly representation
          def to_s
            '<Twilio.Versionless.Understand.AssistantPage>'
          end
        end

        class AssistantInstance < InstanceResource
          ##
          # Initialize the AssistantInstance
          # @param [Version] version Version that contains the resource
          # @param [Hash] payload payload that contains response from Twilio
          # @param [String] account_sid The SID of the
          #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this Assistant
          #   resource.
          # @param [String] sid The SID of the Call resource to fetch.
          # @return [AssistantInstance] AssistantInstance
          def initialize(version, payload)
            super(version)

            # Marshaled Properties
            @properties = {
              'sid' => payload['sid'],
              'friendly_name' => payload['friendly_name'],
            }
          end

          ##
          # @return [String] A string that uniquely identifies this Fleet.
          def sid
            @properties['sid']
          end

          ##
          # @return [String] A human readable description for this Fleet.
          def friendly_name
            @properties['friendly_name']
          end

          ##
          # Provide a user friendly representation
          def to_s
            "<Twilio.Versionless.Understand.AssistantInstance>"
          end

          ##
          # Provide a detailed, user friendly representation
          def inspect
            "<Twilio.Versionless.Understand.AssistantInstance>"
          end
        end
      end
    end
  end
end
