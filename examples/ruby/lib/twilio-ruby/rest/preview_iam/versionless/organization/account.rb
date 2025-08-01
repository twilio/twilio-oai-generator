##
#    This code was generated by
#    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
#     |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
#     |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
#
#    Organization Public API
#    No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
#
#    NOTE: This class is auto generated by OpenAPI Generator.
#    https://openapi-generator.tech
#    Do not edit the class manually.
#

module Twilio
  module REST
    class PreviewIam < PreviewIamBase
      class Versionless < Version
        class OrganizationContext < InstanceContext
          class AccountList < ListResource
            ##
            # Initialize the AccountList
            # @param [Version] version Version that contains the resource
            # @return [AccountList] AccountList
            def initialize(version, organization_sid: nil)
              super(version)
              # Path Solution
              @solution = { organization_sid: organization_sid }
              @uri = "/#{@solution[:organization_sid]}/Accounts"
            end

            ##
            # Lists AccountInstance records from the API as a list.
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
            # When passed a block, yields AccountInstance records from the API.
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
            # Retrieve a single page of AccountInstance records from the API.
            # Request is executed immediately.
            # @param [String] page_token PageToken provided by the API
            # @param [Integer] page_number Page Number, this value is simply for client state
            # @param [Integer] page_size Number of records to return, defaults to 50
            # @return [Page] Page of AccountInstance
            def page(page_token: :unset, page_number: :unset, page_size: :unset)
              params = Twilio::Values.of({
                                           'PageToken' => page_token,
                                           'Page' => page_number,
                                           'PageSize' => page_size,
                                         })
              headers = Twilio::Values.of({})

              response = @version.page('GET', @uri, params: params, headers: headers)

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

            # Provide a user friendly representation
            def to_s
              '#<Twilio.PreviewIam.Versionless.AccountList>'
            end
          end

          class AccountContext < InstanceContext
            ##
            # Initialize the AccountContext
            # @param [Version] version Version that contains the resource
            # @param [String] organization_sid
            # @param [String] account_sid
            # @return [AccountContext] AccountContext
            def initialize(version, organization_sid, account_sid)
              super(version)

              # Path Solution
              @solution = { organization_sid: organization_sid, account_sid: account_sid, }
              @uri = "/#{@solution[:organization_sid]}/Accounts/#{@solution[:account_sid]}"
            end

            ##
            # Fetch the AccountInstance
            # @return [AccountInstance] Fetched AccountInstance
            def fetch
              headers = Twilio::Values.of({ 'Content-Type' => 'application/x-www-form-urlencoded', })

              payload = @version.fetch('GET', @uri, headers: headers)
              AccountInstance.new(
                @version,
                payload,
                organization_sid: @solution[:organization_sid],
                account_sid: @solution[:account_sid],
              )
            end

            ##
            # Provide a user friendly representation
            def to_s
              context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
              "#<Twilio.PreviewIam.Versionless.AccountContext #{context}>"
            end

            ##
            # Provide a detailed, user friendly representation
            def inspect
              context = @solution.map { |k, v| "#{k}: #{v}" }.join(',')
              "#<Twilio.PreviewIam.Versionless.AccountContext #{context}>"
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
              AccountInstance.new(@version, payload, organization_sid: @solution[:organization_sid])
            end

            ##
            # Provide a user friendly representation
            def to_s
              '<Twilio.PreviewIam.Versionless.AccountPage>'
            end
          end

          class AccountInstance < InstanceResource
            ##
            # Initialize the AccountInstance
            # @param [Version] version Version that contains the resource
            # @param [Hash] payload payload that contains response from Twilio
            # @param [String] account_sid The SID of the
            #   {Account}[https://www.twilio.com/docs/iam/api/account] that created this Account
            #   resource.
            # @param [String] sid The SID of the Call resource to fetch.
            # @return [AccountInstance] AccountInstance
            def initialize(version, payload, organization_sid: nil, account_sid: nil)
              super(version)

              # Marshaled Properties
              @properties = {
                'account_sid' => payload['account_sid'],
                'friendly_name' => payload['friendly_name'],
                'status' => payload['status'],
                'owner_sid' => payload['owner_sid'],
                'date_created' => Twilio.deserialize_iso8601_datetime(payload['date_created']),
              }

              # Context
              @instance_context = nil
              @params = { 'organization_sid' => organization_sid || @properties['organization_sid'],
                          'account_sid' => account_sid, }
            end

            ##
            # Generate an instance context for the instance, the context is capable of
            # performing various actions.  All instance actions are proxied to the context
            # @return [AccountContext] CallContext for this CallInstance
            def context
              unless @instance_context
                @instance_context = AccountContext.new(@version, @params['organization_sid'],
                                                       @params['account_sid'])
              end
              @instance_context
            end

            ##
            # @return [String] Twilio account sid
            def account_sid
              @properties['account_sid']
            end

            ##
            # @return [String] Account friendly name
            def friendly_name
              @properties['friendly_name']
            end

            ##
            # @return [String] Account status
            def status
              @properties['status']
            end

            ##
            # @return [String] Twilio account sid
            def owner_sid
              @properties['owner_sid']
            end

            ##
            # @return [Time] The date and time when the account was created in the system
            def date_created
              @properties['date_created']
            end

            ##
            # Fetch the AccountInstance
            # @return [AccountInstance] Fetched AccountInstance
            def fetch
              context.fetch
            end

            ##
            # Provide a user friendly representation
            def to_s
              values = @params.map { |k, v| "#{k}: #{v}" }.join(" ")
              "<Twilio.PreviewIam.Versionless.AccountInstance #{values}>"
            end

            ##
            # Provide a detailed, user friendly representation
            def inspect
              values = @properties.map { |k, v| "#{k}: #{v}" }.join(" ")
              "<Twilio.PreviewIam.Versionless.AccountInstance #{values}>"
            end
          end
        end
      end
    end
  end
end
