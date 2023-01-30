##
#    This code was generated by
#    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
#    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
#    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
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
        class Versionless < Domain
            class DeployedDevices < Version
                class FleetList < ListResource
                    ##
                    # Initialize the FleetList
                    # @param [Version] version Version that contains the resource
                    # @return [FleetList] FleetList
                    def initialize(version)
                        super(version)
                        # Path Solution
                        @solution = {  }
                        @uri = "/Fleets"
                        
                    end
                
                    ##
                    # Create the FleetInstance
                    # @param [String] name 
                    # @return [FleetInstance] Created FleetInstance
                    def create(name: :unset
					)
                        data = Twilio::Values.of(
                            
                            'Name' => name,
                                                    })

                        payload = @version.create('POST', @uri, data: data)

                        FleetInstance.new(@version, payload, )
                    end
                    
                    
                    ##

                    # Provide a user friendly representation
                    def to_s
                        '#<Twilio.Versionless.DeployedDevices.FleetList>'
                    end
                end
                ##
                # PLEASE NOTE that this class contains preview products that are subject to change. Use them with caution. If you currently do not have developer preview access, please contact help@twilio.com.
                class FleetContext < InstanceContext
                    ##
                    # Initialize the FleetContext
                    # @param [Version] version Version that contains the resource
                    # @param [String] sid 
                    # @return [FleetContext] FleetContext
                    def initialize(version, sid)
                        super(version)

                        # Path Solution
                        @solution = { sid: sid,  }
                        @uri = "/Fleets/#{@solution[:sid]}"

                        # Dependents
                    end
                    ##
                    # Fetch the FleetInstance
                    # @return [FleetInstance]
                    Fetched FleetInstance
                    def fetch

                        payload = @version.fetch('GET',@uri )
                        FleetInstance.new(@version, payload, )
                    end


                    ##
                    # Provide a user friendly representation
                    def to_s
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Versionless.DeployedDevices.FleetContext #{context}>"
                    end

                    ##
                    # Provide a detailed, user friendly representation
                    def inspect
                        context = @solution.map {|k, v| "#{k}: #{v}"}.join(',')
                        "#<Twilio.Versionless.DeployedDevices.FleetContext #{context}>"
                    end
                end

                class FleetPage < Page
                    ##
                    # Initialize the FleetPage
                    # @param [Version] version Version that contains the resource
                    # @param [Response] response Response from the API
                    # @param [Hash] solution Path solution for the resource
                    # @return [FleetPage] FleetPage
                    def initialize(version, response, solution)
                        super(version, response)

                        # Path Solution
                        @solution = solution
                    end

                    ##
                    # Build an instance of FleetInstance
                    # @param [Hash] payload Payload response from the API
                    # @return [FleetInstance] FleetInstance
                    def get_instance(payload)
                        FleetInstance.new(@version, payload)
                    end

                    ##
                    # Provide a user friendly representation
                    def to_s
                        '<Twilio.Versionless.DeployedDevices.FleetPage>'
                    end
                end

            end
        end
    end
end
