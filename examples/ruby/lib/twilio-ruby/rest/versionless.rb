require 'twilio-ruby/rest'

module Twilio
    module REST
      class Versionless < VersionlessBase
          def deployed_devices
            @deployed_devices ||= DeployedDevices.new self
          end
          def understand
            @understand ||= Understand.new self
          end
      end
    end
end
