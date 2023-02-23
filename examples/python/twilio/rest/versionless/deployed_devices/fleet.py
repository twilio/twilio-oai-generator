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
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version



class FleetList(ListResource):

    def __init__(self, version: Version):
        """
        Initialize the FleetList

        :param Version version: Version that contains the resource
        
        :returns: twilio.rest.versionless.deployed_devices.fleet.FleetList
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Fleets'.format(**self._solution)
        
        
    
    
    def create(self, name=values.unset):
        """
        Create the FleetInstance

        :param str name: 
        
        :returns: The created FleetInstance
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetInstance
        """
        data = values.of({ 
            'Name': name,
        })
        )
        payload = self._version.create(method='POST', uri=self._uri, data=data,)

        return FleetInstance(self._version, payload)
    

    def get(self, sid):
        """
        Constructs a FleetContext
        
        :param sid: 
        
        :returns: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        """
        return FleetContext(self._version, sid=sid)

    def __call__(self, sid):
        """
        Constructs a FleetContext
        
        :param sid: 
        
        :returns: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        """
        return FleetContext(self._version, sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Versionless.DeployedDevices.FleetList>'

class FleetContext(InstanceContext):

    def __init__(self, version: Version, sid: str):
        """
        Initialize the FleetContext

        :param Version version: Version that contains the resource
        :param sid: 

        :returns: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 
            'sid': sid,
        }
        self._uri = '/Fleets/{sid}'.format(**self._solution)
        
    
    def fetch(self):
        """
        Fetch the FleetInstance
        

        :returns: The fetched FleetInstance
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetInstance
        """
        
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return FleetInstance(
            self._version,
            payload,
            sid=self._solution['sid'],
            
        )
        
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Versionless.DeployedDevices.FleetContext {}>'.format(context)

class FleetInstance(InstanceResource):

    def __init__(self, version, payload, sid: str=None):
        """
        Initialize the FleetInstance
        :returns: twilio.rest.versionless.deployed_devices.fleet.FleetInstance
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetInstance
        """
        super().__init__(version)

        self._properties = { 
            'name': payload.get('name'),
            'sid': payload.get('sid'),
            'friendly_name': payload.get('friendly_name'),
        }

        self._context = None
        self._solution = { 'sid': sid or self._properties['sid'],  }
    
    @property
    def _proxy(self):
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: FleetContext for this FleetInstance
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetContext
        """
        if self._context is None:
            self._context = FleetContext(self._version, sid=self._solution['sid'],)
        return self._context
    
    @property
    def name(self):
        """
        :returns: 
        :rtype: str
        """
        return self._properties['name']
    
    @property
    def sid(self):
        """
        :returns: A string that uniquely identifies this Fleet.
        :rtype: str
        """
        return self._properties['sid']
    
    @property
    def friendly_name(self):
        """
        :returns: A human readable description for this Fleet.
        :rtype: str
        """
        return self._properties['friendly_name']
    
    def fetch(self):
        """
        Fetch the FleetInstance
        

        :returns: The fetched FleetInstance
        :rtype: twilio.rest.versionless.deployed_devices.fleet.FleetInstance
        """
        return self._proxy.fetch()
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Versionless.DeployedDevices.FleetInstance {}>'.format(context)


