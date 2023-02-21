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

from twilio.rest.api.v2010.call.feedback_call_summary import FeedbackCallSummaryList


class CallList(ListResource):

    def __init__(self, version: Version, account_sid: str):
        """
        Initialize the CallList
        :param Version version: Version that contains the resource
        :param account_sid: 
        
        :returns: twilio.api.v2010.call..CallList
        :rtype: twilio.api.v2010.call..CallList
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 'account_sid': account_sid,  }
        self._uri = '/Accounts/${account_sid}/Calls.json'.format(**self._solution)
        
        self._feedback_call_summary = None
        
    
    
    
    def create(self, required_string_property, test_array_of_strings=values.unset, test_array_of_uri=values.unset, test_method):
        """
        Create the CallInstance
         :param str required_string_property: 
         :param [str] test_array_of_strings: 
         :param [str] test_array_of_uri: 
         :param str test_method: The HTTP method that we should use to request the `TestArrayOfUri`.
        
        :returns: The created CallInstance
        :rtype: twilio.rest.api.v2010.call.CallInstance
        """
        data = values.of({ 
            'RequiredStringProperty': required_string_property,
            'TestArrayOfStrings': serialize.map(test_array_of_strings, lambda e: e),
            'TestArrayOfUri': serialize.map(test_array_of_uri, lambda e: e),
            'TestMethod': test_method,
        })

        payload = self._version.create(method='POST', uri=self._uri, data=data)
        return CallInstance(self._version, payload, account_sid=self._solution['account_sid'])
    

    @property
    def feedback_call_summary(self):
        """
        Access the feedback_call_summary

        :returns: twilio.rest.api.v2010.call.feedback_call_summary.FeedbackCallSummaryList
        :rtype: twilio.rest.api.v2010.call.feedback_call_summary.FeedbackCallSummaryList
        """
        if self._feedback_call_summary is None:
            self._feedback_call_summary = FeedbackCallSummaryList(self._version, account_sid=self._solution['account_sid'])
        return self.feedback_call_summary
    def get(self, test_integer):
        """
        Constructs a CallContext
        
        :param test_integer: INTEGER ID param!!!
        
        :returns: twilio.rest.api.v2010.call.CallContext
        :rtype: twilio.rest.api.v2010.call.CallContext
        """
        return CallContext(self._version, account_sid=self._solution['account_sid'], test_integer=test_integer)

    def __call__(self, test_integer):
        """
        Constructs a CallContext
        
        :param test_integer: INTEGER ID param!!!
        
        :returns: twilio.rest.api.v2010.call.CallContext
        :rtype: twilio.rest.api.v2010.call.CallContext
        """
        return CallContext(self._version, account_sid=self._solution['account_sid'], test_integer=test_integer)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.CallList>'


class CallContext(InstanceContext):
    def __init__(self, version: Version, account_sid: str, test_integer: int):
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'account_sid': account_sid, 'test_integer': test_integer,  }
        self._uri = '/Accounts/${account_sid}/Calls/${test_integer}.json'
        
    
    def delete(self):
        
        

        """
        Deletes the CallInstance

        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._version.delete(method='DELETE', uri=self._uri, )
    
    def fetch(self):
        
        """
        Fetch the CallInstance

        :returns: The fetched CallInstance
        #TODO: add rtype docs
        """
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return CallInstance(self._version, payload, account_sid=self._solution['account_sid'], test_integer=self._solution['test_integer'], )
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.CallContext>'



class CallInstance(InstanceResource):
    def __init__(self, version, payload, account_sid: str, test_integer: int):
        super().__init__(version)
        self._properties = { 
            'account_sid' : payload.get('account_sid'),
            'sid' : payload.get('sid'),
            'test_string' : payload.get('test_string'),
            'test_integer' : payload.get('test_integer'),
            'test_object' : payload.get('test_object'),
            'test_date_time' : payload.get('test_date_time'),
            'test_number' : payload.get('test_number'),
            'price_unit' : payload.get('price_unit'),
            'test_number_float' : payload.get('test_number_float'),
            'test_number_decimal' : payload.get('test_number_decimal'),
            'test_enum' : payload.get('test_enum'),
            'a2p_profile_bundle_sid' : payload.get('a2p_profile_bundle_sid'),
            'test_array_of_integers' : payload.get('test_array_of_integers'),
            'test_array_of_array_of_integers' : payload.get('test_array_of_array_of_integers'),
            'test_array_of_objects' : payload.get('test_array_of_objects'),
            'test_array_of_enum' : payload.get('test_array_of_enum'),
        }

        self._context = None
        self._solution = {
            'account_sid': account_sid or self._properties['account_sid'],'test_integer': test_integer or self._properties['test_integer'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = CallContext(
                self._version,
                account_sid=self._solution['account_sid'],test_integer=self._solution['test_integer'],
            )
        return self._context

    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.V2010.CallInstance {}>'.format(context)



