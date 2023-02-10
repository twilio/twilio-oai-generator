##
 # This code was generated by
 # ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 #  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 #  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 #
 # NOTE: This class is auto generated by OpenAPI Generator.
 # https://openapi-generator.tech
 # Do not edit the class manually.
# frozen_string_literal: true

module Twilio
  module REST
    class FlexApiBase < Domain

      ##
      # Initialize flexApi domain
      #
      # @param twilio - The twilio client
      #
      def initialize(twilio)
        super(twilio)
        @base_url =  "https://flex-api.twilio.com"
        @host = "flex-api"
        @port = 443

        # Versions
        @v1 = nil
      end

    end
  end
end
