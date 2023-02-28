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

from twilio.rest.api.v2010.account.call.feedback_call_summary import FeedbackCallSummaryList


class CallList(ListResource):

    def __init__(self, version: Version, account_sid: str):
        """
        Initialize the CallList

        :param Version version: Version that contains the resource
        :param account_sid: 
        
        :returns: twilio.rest.api.v2010.account.call.CallList
        :rtype: twilio.rest.api.v2010.account.call.CallList
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 'account_sid': account_sid,  }
        self._uri = '/Accounts/{account_sid}/Calls.json'.format(**self._solution)
        
        self._feedback_call_summary = None
        
    
    
    
    def create(self, required_string_property, test_method, test_array_of_strings=values.unset, test_array_of_uri=values.unset):
        """
        Create the CallInstance

        :param str required_string_property: 
        :param str test_method: The HTTP method that we should use to request the `TestArrayOfUri`.
        :param list[str] test_array_of_strings: 
        :param list[str] test_array_of_uri: 
        
        :returns: The created CallInstance
        :rtype: twilio.rest.api.v2010.account.call.CallInstance
        """
        data = values.of({ 
            'RequiredStringProperty': required_string_property,
            'TestMethod': test_method,
            'TestArrayOfStrings': serialize.map(test_array_of_strings, lambda e: e),
            'TestArrayOfUri': serialize.map(test_array_of_uri, lambda e: e),
        })
        
        payload = self._version.create(method='POST', uri=self._uri, data=data,)

        return CallInstance(self._version, payload, account_sid=self._solution['account_sid'])
    

    @property
    def feedback_call_summary(self):
        """
        Access the feedback_call_summary

        :returns: twilio.rest.api.v2010.account.call.FeedbackCallSummaryList
        :rtype: twilio.rest.api.v2010.account.call.FeedbackCallSummaryList
        """
        if self._feedback_call_summary is None:
            self._feedback_call_summary = FeedbackCallSummaryList(self._version, account_sid=self._solution['account_sid'])
        return self._feedback_call_summary

    def get(self, test_integer):
        """
        Constructs a CallContext
        
        :param test_integer: INTEGER ID param!!!
        
        :returns: twilio.rest.api.v2010.account.call.CallContext
        :rtype: twilio.rest.api.v2010.account.call.CallContext
        """
        return CallContext(self._version, account_sid=self._solution['account_sid'], test_integer=test_integer)

    def __call__(self, test_integer):
        """
        Constructs a CallContext
        
        :param test_integer: INTEGER ID param!!!
        
        :returns: twilio.rest.api.v2010.account.call.CallContext
        :rtype: twilio.rest.api.v2010.account.call.CallContext
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
        """
        Initialize the CallContext

        :param Version version: Version that contains the resource
        :param account_sid: :param test_integer: INTEGER ID param!!!

        :returns: twilio.rest.api.v2010.account.call.CallContext
        :rtype: twilio.rest.api.v2010.account.call.CallContext
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 
            'account_sid': account_sid,
            'test_integer': test_integer,
        }
        self._uri = '/Accounts/{account_sid}/Calls/{test_integer}.json'.format(**self._solution)
        
    
    def delete(self):
        """
        Deletes the CallInstance

        
        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._version.delete(method='DELETE', uri=self._uri,)
        
    def fetch(self):
        """
        Fetch the CallInstance
        

        :returns: The fetched CallInstance
        :rtype: twilio.rest.api.v2010.account.call.CallInstance
        """
        
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return CallInstance(
            self._version,
            payload,
            account_sid=self._solution['account_sid'],
            test_integer=self._solution['test_integer'],
            
        )
        
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.V2010.CallContext {}>'.format(context)

class CallInstance(InstanceResource):

    def __init__(self, version, payload, account_sid: str, test_integer: int=None):
        """
        Initialize the CallInstance
        :returns: twilio.rest.api.v2010.account.call.CallInstance
        :rtype: twilio.rest.api.v2010.account.call.CallInstance
        """
        super().__init__(version)

        self._properties = { 
            'account_sid': payload.get('account_sid'),
            'sid': payload.get('sid'),
            'test_string': payload.get('test_string'),
            'test_integer': deserialize.integer(payload.get('test_integer')),
            'test_object': payload.get('test_object'),
            'test_date_time': deserialize.rfc2822_datetime(payload.get('test_date_time')),
            'test_number': deserialize.decimal(payload.get('test_number')),
            'price_unit': payload.get('price_unit'),
            'test_number_float': payload.get('test_number_float'),
            'test_number_decimal': payload.get('test_number_decimal'),
            'test_enum': payload.get('test_enum'),
            'a2p_profile_bundle_sid': payload.get('a2p_profile_bundle_sid'),
            'test_array_of_integers': payload.get('test_array_of_integers'),
            'test_array_of_array_of_integers': payload.get('test_array_of_array_of_integers'),
            'test_array_of_objects': payload.get('test_array_of_objects'),
            'test_array_of_enum': payload.get('test_array_of_enum'),
        }

        self._context = None
        self._solution = { 'account_sid': account_sid, 'test_integer': test_integer or self._properties['test_integer'],  }
    
    @property
    def _proxy(self):
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: CallContext for this CallInstance
        :rtype: twilio.rest.api.v2010.account.call.CallContext
        """
        if self._context is None:
            self._context = CallContext(self._version, account_sid=self._solution['account_sid'], test_integer=self._solution['test_integer'],)
        return self._context
    
    @property
    def account_sid(self):
        """
        :returns: 
        :rtype: str
        """
        return self._properties['account_sid']
    
    @property
    def sid(self):
        """
        :returns: 
        :rtype: str
        """
        return self._properties['sid']
    
    @property
    def test_string(self):
        """
        :returns: 
        :rtype: str
        """
        return self._properties['test_string']
    
    @property
    def test_integer(self):
        """
        :returns: 
        :rtype: int
        """
        return self._properties['test_integer']
    
    @property
    def test_object(self):
        """
        :returns: 
        :rtype: TestResponseObjectTestObject
        """
        return self._properties['test_object']
    
    @property
    def test_date_time(self):
        """
        :returns: 
        :rtype: datetime
        """
        return self._properties['test_date_time']
    
    @property
    def test_number(self):
        """
        :returns: 
        :rtype: float
        """
        return self._properties['test_number']
    
    @property
    def price_unit(self):
        """
        :returns: 
        :rtype: str
        """
        return self._properties['price_unit']
    
    @property
    def test_number_float(self):
        """
        :returns: 
        :rtype: float
        """
        return self._properties['test_number_float']
    
    @property
    def test_number_decimal(self):
        """
        :returns: 
        :rtype: Decimal
        """
        return self._properties['test_number_decimal']
    
    @property
    def test_enum(self):
        """
        :returns: 
        :rtype: TestStatus
        """
        return self._properties['test_enum']
    
    @property
    def a2p_profile_bundle_sid(self):
        """
        :returns: A2P Messaging Profile Bundle BundleSid
        :rtype: str
        """
        return self._properties['a2p_profile_bundle_sid']
    
    @property
    def test_array_of_integers(self):
        """
        :returns: 
        :rtype: list[int]
        """
        return self._properties['test_array_of_integers']
    
    @property
    def test_array_of_array_of_integers(self):
        """
        :returns: 
        :rtype: list[list[int]]
        """
        return self._properties['test_array_of_array_of_integers']
    
    @property
    def test_array_of_objects(self):
        """
        :returns: 
        :rtype: list[TestResponseObjectTestArrayOfObjects]
        """
        return self._properties['test_array_of_objects']
    
    @property
    def test_array_of_enum(self):
        """
        :returns: Permissions authorized to the app
        :rtype: list[TestStatus]
        """
        return self._properties['test_array_of_enum']
    
    def delete(self):
        """
        Deletes the CallInstance
        

        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._proxy.delete()
    
    def fetch(self):
        """
        Fetch the CallInstance
        

        :returns: The fetched CallInstance
        :rtype: twilio.rest.api.v2010.account.call.CallInstance
        """
        return self._proxy.fetch()
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.V2010.CallInstance {}>'.format(context)


