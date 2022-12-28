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




class HistoryContext(InstanceContext):
    def __init__(self, version: V1, sid: str):
        # TODO: needs autogenerated docs
        super(HistoryContextList, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = '/Credentials/AWS/${sid}/History'
        
        
        def fetch(self, add_ons_data):
            
            """
            Fetch the HistoryInstance

            :returns: The fetched HistoryInstance
            #TODO: add rtype docs
            """
            payload = self._version.fetch(method='GET', uri=self._uri, )
            return HistoryInstance(
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
        return '<Twilio.Api.V1.HistoryContext>'



class HistoryInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        super(HistoryInstance, self).__init__(version)
        self._properties = { 
            'account_sid' = payload.get('account_sid'),
            'sid' = payload.get('sid'),
            'test_string' = payload.get('test_string'),
            'test_integer' = payload.get('test_integer'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid']
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = HistoryContext(
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
        return '<Twilio.Api.V1.HistoryInstance {}>'.format(context)



class HistoryListInstance(ListResource):
    def __init__(self, version: V1, sid: str):
        # TODO: needs autogenerated docs
        super(HistoryListInstanceList, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = ''
        
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V1.HistoryListInstance>'

