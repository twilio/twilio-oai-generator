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
from twilio.base.page import Page
from twilio.rest.api.v2010.account.calls import CallList


class AccountList(ListResource):

    def __init__(self, version: Version):
        """
        Initialize the AccountList

        :param Version version: Version that contains the resource
        
        :returns: twilio.rest.api.v2010.account.AccountList
        :rtype: twilio.rest.api.v2010.account.AccountList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Accounts.json'.format(**self._solution)
        
        
    
    
    
    
    def create(self, x_twilio_webhook_enabled=values.unset, recording_status_callback=values.unset, recording_status_callback_event=values.unset, twiml=values.unset):
        """
        Create the AccountInstance
        :param str x_twilio_webhook_enabled: 
        :param str recording_status_callback: 
        :param list[str] recording_status_callback_event: 
        :param str twiml: 
        
        :returns: The created AccountInstance
        :rtype: twilio.rest.api.v2010.account.AccountInstance
        """
        data = values.of({ 
            'X-Twilio-Webhook-Enabled': x_twilio_webhook_enabled,
            'RecordingStatusCallback': recording_status_callback,
            'RecordingStatusCallbackEvent': recording_status_callback_event,
            'Twiml': twiml,
        })

        payload = self._version.create(method='POST', uri=self._uri, data=data)
        return AccountInstance(self._version, payload)
    
    
    def stream(self, date_created=values.unset, date_test=values.unset, date_created_before=values.unset, date_created_after=values.unset, limit=None, page_size=None):
        """
        Streams AccountInstance records from the API as a generator stream.
        This operation lazily loads records as efficiently as possible until the limit
        is reached.
        The results are returned as a generator, so this operation is memory efficient.
        
        :param datetime date_created: 
        :param date date_test: 
        :param datetime date_created_before: 
        :param datetime date_created_after: 
        :param int limit: Upper limit for the number of records to return. stream()
                          guarantees to never return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, stream() will attempt to read the
                              limit with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.api.v2010.account.AccountInstance]
        """
        limits = self._version.read_limits(limit, page_size)
        page = self.page(
            date_created=date_created,
            date_test=date_test,
            date_created_before=date_created_before,
            date_created_after=date_created_after,
            page_size=limits['page_size']
        )

        return self._version.stream(page, limits['limit'])

    def list(self, date_created=values.unset, date_test=values.unset, date_created_before=values.unset, date_created_after=values.unset, limit=None, page_size=None):
        """
        Lists AccountInstance records from the API as a list.
        Unlike stream(), this operation is eager and will load `limit` records into
        memory before returning.
        
        :param datetime date_created: 
        :param date date_test: 
        :param datetime date_created_before: 
        :param datetime date_created_after: 
        :param int limit: Upper limit for the number of records to return. list() guarantees
                          never to return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, list() will attempt to read the limit
                              with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.api.v2010.account.AccountInstance]
        """
        return list(self.stream(
            date_created=date_created,
            date_test=date_test,
            date_created_before=date_created_before,
            date_created_after=date_created_after,
            limit=limit,
            page_size=page_size,
        ))

    def page(self, date_created=values.unset, date_test=values.unset, date_created_before=values.unset, date_created_after=values.unset, page_token=values.unset, page_number=values.unset, page_size=values.unset):
        """
        Retrieve a single page of AccountInstance records from the API.
        Request is executed immediately
        
        :param datetime date_created: 
        :param date date_test: 
        :param datetime date_created_before: 
        :param datetime date_created_after: 
        :param str page_token: PageToken provided by the API
        :param int page_number: Page Number, this value is simply for client state
        :param int page_size: Number of records to return, defaults to 50

        :returns: Page of AccountInstance
        :rtype: twilio.rest.api.v2010.account.AccountPage
        """
        data = values.of({ 
            'DateCreated': serialize.iso8601_datetime(date_created),
            'Date.Test': serialize.iso8601_date(date_test),
            'DateCreated<': serialize.iso8601_datetime(date_created_before),
            'DateCreated>': serialize.iso8601_datetime(date_created_after),
            'PageToken': page_token,
            'Page': page_number,
            'PageSize': page_size,
        })

        response = self._version.page(method='GET', uri=self._uri, params=data)
        return AccountPage(self._version, response, self._solution)

    def get_page(self, target_url):
        """
        Retrieve a specific page of AccountInstance records from the API.
        Request is executed immediately

        :param str target_url: API-generated URL for the requested results page

        :returns: Page of AccountInstance
        :rtype: twilio.rest.api.v2010.account.AccountPage
        """
        response = self._version.domain.twilio.request(
            'GET',
            target_url
        )
        return AccountPage(self._version, response, self._solution)


    def get(self, sid):
        """
        Constructs a AccountContext
        
        :param sid: 
        
        :returns: twilio.rest.api.v2010.account.AccountContext
        :rtype: twilio.rest.api.v2010.account.AccountContext
        """
        return AccountContext(self._version, sid=sid)

    def __call__(self, sid):
        """
        Constructs a AccountContext
        
        :param sid: 
        
        :returns: twilio.rest.api.v2010.account.AccountContext
        :rtype: twilio.rest.api.v2010.account.AccountContext
        """
        return AccountContext(self._version, sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.AccountList>'










class AccountPage(Page):

    def __init__(self, version, response, solution):
        """
        Initialize the AccountPage

        :param Version version: Version that contains the resource
        :param Response response: Response from the API

        :returns: twilio.rest.api.v2010.account.AccountPage
        :rtype: twilio.rest.api.v2010.account.AccountPage
        """
        super().__init__(version, response)

        # Path solution
        self._solution = solution

    def get_instance(self, payload):
        """
        Build an instance of AccountInstance

        :param dict payload: Payload response from the API

        :returns: twilio.rest.api.v2010.account.AccountInstance
        :rtype: twilio.rest.api.v2010.account.AccountInstance
        """
        return AccountInstance(self._version, payload)

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.AccountPage>'





class AccountContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'sid': sid,  }
        self._uri = '/Accounts/${sid}.json'
        
        self._calls = None
    
    def delete(self):
        
        

        """
        Deletes the AccountInstance

        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._version.delete(method='DELETE', uri=self._uri, )
    
    def fetch(self):
        
        """
        Fetch the AccountInstance

        :returns: The fetched AccountInstance
        #TODO: add rtype docs
        """
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return AccountInstance(self._version, payload, sid=self._solution['sid'], )
        

        
    
    def update(self, status, pause_behavior):
        data = values.of({
            'status': status,'pause_behavior': pause_behavior,
        })

        payload = self._version.update(method='post', uri=self._uri, data=data, )

        return AccountInstance(self._version, payload, sid=self._solution['sid'], )
        
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.AccountContext>'



class AccountInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
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
            'sid': sid or self._properties['sid'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = AccountContext(
                self._version,
                sid=self._solution['sid'],
            )
        return self._context

    @property
    def calls(self):
        return self._proxy.calls
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Api.V2010.AccountInstance {}>'.format(context)



