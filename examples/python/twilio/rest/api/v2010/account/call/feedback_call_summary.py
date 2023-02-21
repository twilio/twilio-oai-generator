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



class FeedbackCallSummaryList(ListResource):

    def __init__(self, version: Version, account_sid: str):
        """
        Initialize the FeedbackCallSummaryList
        :param Version version: Version that contains the resource
        :param account_sid: 
        
        :returns: twilio.api.v2010.feedback_call_summary..FeedbackCallSummaryList
        :rtype: twilio.api.v2010.feedback_call_summary..FeedbackCallSummaryList
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 'account_sid': account_sid,  }
        
        
        
    

    def get(self, sid):
        """
        Constructs a FeedbackCallSummaryContext
        
        :param sid: 
        
        :returns: twilio.rest.api.v2010.feedback_call_summary.FeedbackCallSummaryContext
        :rtype: twilio.rest.api.v2010.feedback_call_summary.FeedbackCallSummaryContext
        """
        return FeedbackCallSummaryContext(self._version, account_sid=self._solution['account_sid'], sid=sid)

    def __call__(self, sid):
        """
        Constructs a FeedbackCallSummaryContext
        
        :param sid: 
        
        :returns: twilio.rest.api.v2010.feedback_call_summary.FeedbackCallSummaryContext
        :rtype: twilio.rest.api.v2010.feedback_call_summary.FeedbackCallSummaryContext
        """
        return FeedbackCallSummaryContext(self._version, account_sid=self._solution['account_sid'], sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.FeedbackCallSummaryList>'


class FeedbackCallSummaryContext(InstanceContext):
    def __init__(self, version: Version, account_sid: str, sid: str):
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'account_sid': account_sid, 'sid': sid,  }
        self._uri = '/Accounts/${account_sid}/Calls/Feedback/Summary/${sid}.json'
        
    
    def update(self, account_sid, end_date, start_date):
        data = values.of({
            'account_sid': account_sid,'end_date': end_date,'start_date': start_date,
        })

        payload = self._version.update(method='post', uri=self._uri, data=data, )

        return FeedbackCallSummaryInstance(self._version, payload, account_sid=self._solution['account_sid'], sid=self._solution['sid'], )
        
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.FeedbackCallSummaryContext>'



class FeedbackCallSummaryInstance(InstanceResource):
    def __init__(self, version, payload, account_sid: str, sid: str):
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
            'account_sid': account_sid or self._properties['account_sid'],'sid': sid or self._properties['sid'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = FeedbackCallSummaryContext(
                self._version,
                account_sid=self._solution['account_sid'],sid=self._solution['sid'],
            )
        return self._context

    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.V2010.FeedbackCallSummaryInstance {}>'.format(context)



