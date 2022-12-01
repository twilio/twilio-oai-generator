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




class FleetContext(InstanceContext):
    def __init__(self, version: DeployedDevices, sid: str):
        # TODO: needs autogenerated docs
        super(FleetContextList, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = '/Fleets/${sid}'
        
        
        def fetch(self):
            
            
            fetch = True
            
            
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        # TODO: update so that contexts aren't returned for page or list resources
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.DeployedDevices.FleetContext {}>'.format(context)


class FleetInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        super(FleetInstance, self).__init__(version)
        self._properties = {
            'account_sid' = payload.get('account_sid'),'friendly_name' = payload.get('friendly_name'),'sid' = payload.get('sid'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid']
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




class FleetListInstance(ListResource):
    def __init__(self, version: DeployedDevices):
        # TODO: needs autogenerated docs
        super(FleetListInstanceList, self).__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Fleets'
        
        
        def create(self, body):
            data = values.of({
                'body': body,
            })

            payload = self._version.create(method='post', uri=self._uri, data=data, )

            return FleetInstance(self._version, payload, )
            
            
            
            
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        # TODO: update so that contexts aren't returned for page or list resources
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.DeployedDevices.FleetListInstance {}>'.format(context)



