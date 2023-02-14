"""
    This code was generated by
   ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    Twilio - Versionless
    No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)

    NOTE: This class is auto generated by OpenAPI Generator.
    https://openapi-generator.tech
    Do not edit the class manually.
"""


from twilio.base import deserialize
from twilio.base import serialize
from twilio.base import values
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version



class FleetList(ListResource):
    def __init__(self, version: Version):
        """
        Initialize the FleetList
        :param Version version: Version that contains the resource
        
        :returns: twilio.versionless.deployed_devices.fleet..FleetList
        :rtype: twilio.versionless.deployed_devices.fleet..FleetList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Fleets'.format(**self._solution)



    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Versionless.DeployedDevices.FleetList>'


class FleetContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'sid': sid,  }
        self._uri = '/Fleets/${sid}'
        
    
    def fetch(self):
        
        """
        Fetch the FleetInstance

        :returns: The fetched FleetInstance
        #TODO: add rtype docs
        """
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return FleetInstance(self._version, payload, sid=self._solution['sid'], )
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.DeployedDevices.FleetContext>'



class FleetInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        super().__init__(version)
        self._properties = { 
            'name' : payload.get('name'),
            'sid' : payload.get('sid'),
            'friendly_name' : payload.get('friendly_name'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = FleetContext(
                self._version,
                sid=self._solution['sid'],
            )
        return self._context

    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.DeployedDevices.FleetInstance {}>'.format(context)




