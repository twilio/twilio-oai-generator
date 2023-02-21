"""
    This code was generated by
   ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    Twilio - Accounts
    This is the public Twilio REST API.

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



class CallList(ListResource):

    def __init__(self, version: Version):
        """
        Initialize the CallList
        :param Version version: Version that contains the resource
        
        :returns: twilio.flex_api.v1.call..CallList
        :rtype: twilio.flex_api.v1.call..CallList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        
        
        
    

    def get(self, sid):
        """
        Constructs a CallContext
        
        :param sid: 
        
        :returns: twilio.rest.flex_api.v1.call.CallContext
        :rtype: twilio.rest.flex_api.v1.call.CallContext
        """
        return CallContext(self._version, sid=sid)

    def __call__(self, sid):
        """
        Constructs a CallContext
        
        :param sid: 
        
        :returns: twilio.rest.flex_api.v1.call.CallContext
        :rtype: twilio.rest.flex_api.v1.call.CallContext
        """
        return CallContext(self._version, sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.CallList>'


class CallContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'sid': sid,  }
        self._uri = '/Voice/${sid}'
        
    
    def update(self):
        data = values.of({
            
        })

        payload = self._version.update(method='post', uri=self._uri, data=data, )

        return CallInstance(self._version, payload, sid=self._solution['sid'], )
        
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.CallContext>'



class CallInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        super().__init__(version)
        self._properties = { 
            'sid' : payload.get('sid'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = CallContext(
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
        return '<Twilio.FlexApi.V1.CallInstance {}>'.format(context)



