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

# TODO: needs dependent imports



class CallContext(ListResource):
    def __init__(self, version: V1, sid: str):
        # TODO: needs autogenerated docs
        super(CallContext, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = '/Voice/${sid}'

        # Components
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
    context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
    return '<Twilio.Api.V1.CallContext {}>'.format(context)



class CallListInstance(ListResource):
    def __init__(self, version: V1):
        # TODO: needs autogenerated docs
        super(CallListInstance, self).__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = ''

        # Components
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
    context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
    return '<Twilio.Api.V1.CallListInstance {}>'.format(context)


