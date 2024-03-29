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
    class Versionless
      class Understand < Version
        ##
        # Initialize the Understand version of Versionless
        def initialize(domain)
          super
          @version = 'understand'
          @assistants = nil
        end

        ##
        # @return [Twilio::REST::Versionless::Understand::AssistantList]
        def assistants
          @assistants ||= AssistantList.new self
        end

        ##
        # Provide a user friendly representation
        def to_s
          '<Twilio::REST::Versionless::Understand>';
        end
      end
    end
  end
end
