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


from datetime import date
from twilio.base import deserialize
from twilio.base import serialize
from twilio.base import values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version
from twilio.base.page import Page
from twilio.rest.flex_api.v1.credential.aws.history import HistoryList


class AwsList(ListResource):

    def __init__(self, version: Version):
        """
        Initialize the AwsList

        :param Version version: Version that contains the resource
        
        :returns: twilio.rest.flex_api.v1.credential.aws.AwsList
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Credentials/AWS'.format(**self._solution)
        
        
    
    
    
    
    def stream(self, limit=None, page_size=None):
        """
        Streams AwsInstance records from the API as a generator stream.
        This operation lazily loads records as efficiently as possible until the limit
        is reached.
        The results are returned as a generator, so this operation is memory efficient.
        
        :param int limit: Upper limit for the number of records to return. stream()
                          guarantees to never return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, stream() will attempt to read the
                              limit with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.flex_api.v1.credential.aws.AwsInstance]
        """
        limits = self._version.read_limits(limit, page_size)
        page = self.page(
            page_size=limits['page_size']
        )

        return self._version.stream(page, limits['limit'])

    def list(self, limit=None, page_size=None):
        """
        Lists AwsInstance records from the API as a list.
        Unlike stream(), this operation is eager and will load `limit` records into
        memory before returning.
        
        :param int limit: Upper limit for the number of records to return. list() guarantees
                          never to return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, list() will attempt to read the limit
                              with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.flex_api.v1.credential.aws.AwsInstance]
        """
        return list(self.stream(
            limit=limit,
            page_size=page_size,
        ))

    def page(self, page_token=values.unset, page_number=values.unset, page_size=values.unset):
        """
        Retrieve a single page of AwsInstance records from the API.
        Request is executed immediately
        
        :param str page_token: PageToken provided by the API
        :param int page_number: Page Number, this value is simply for client state
        :param int page_size: Number of records to return, defaults to 50

        :returns: Page of AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsPage
        """
        data = values.of({ 
            'PageToken': page_token,
            'Page': page_number,
            'PageSize': page_size,
        })

        response = self._version.page(method='GET', uri=self._uri, params=data)
        return AwsPage(self._version, response, self._solution)

    def get_page(self, target_url):
        """
        Retrieve a specific page of AwsInstance records from the API.
        Request is executed immediately

        :param str target_url: API-generated URL for the requested results page

        :returns: Page of AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsPage
        """
        response = self._version.domain.twilio.request(
            'GET',
            target_url
        )
        return AwsPage(self._version, response, self._solution)


    def get(self, sid):
        """
        Constructs a AwsContext
        
        :param sid: 
        
        :returns: twilio.rest.flex_api.v1.credential.aws.AwsContext
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsContext
        """
        return AwsContext(self._version, sid=sid)

    def __call__(self, sid):
        """
        Constructs a AwsContext
        
        :param sid: 
        
        :returns: twilio.rest.flex_api.v1.credential.aws.AwsContext
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsContext
        """
        return AwsContext(self._version, sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.AwsList>'








class AwsPage(Page):

    def __init__(self, version, response, solution):
        """
        Initialize the AwsPage

        :param Version version: Version that contains the resource
        :param Response response: Response from the API

        :returns: twilio.rest.flex_api.v1.credential.aws.AwsPage
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsPage
        """
        super().__init__(version, response)

        # Path solution
        self._solution = solution

    def get_instance(self, payload):
        """
        Build an instance of AwsInstance

        :param dict payload: Payload response from the API

        :returns: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        return AwsInstance(self._version, payload)

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.AwsPage>'




class AwsContext(InstanceContext):

    def __init__(self, version: Version, sid: str):
        """
        Initialize the AwsContext

        :param Version version: Version that contains the resource
        :param sid: 

        :returns: twilio.rest.flex_api.v1.credential.aws.AwsContext
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsContext
        """
        super().__init__(version)

        # Path Solution
        self._solution = { 
            'sid': sid,
        }
        self._uri = '/Credentials/AWS/{sid}'.format(**self._solution)
        
        self._history = None
    
    def delete(self):
        """
        Deletes the AwsInstance

        
        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._version.delete(method='DELETE', uri=self._uri,)
        
    def fetch(self):
        """
        Fetch the AwsInstance
        

        :returns: The fetched AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return AwsInstance(
            self._version,
            payload,
            sid=self._solution['sid'],
            
        )
        
    def update(self, test_string=values.unset, test_boolean=values.unset):
        """
        Update the AwsInstance
        
        :params str test_string: 
        :params bool test_boolean: 

        :returns: The updated AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        data = values.of({ 
            'TestString': test_string,
            'TestBoolean': test_boolean,
        })
        

        payload = self._version.update(method='POST', uri=self._uri, data=data,)

        return AwsInstance(
            self._version,
            payload,
            sid=self._solution['sid']
        )
        
    
    @property
    def history(self):
        """
        Access the history

        :returns: twilio.rest.flex_api.v1.credential.aws.HistoryList
        :rtype: twilio.rest.flex_api.v1.credential.aws.HistoryList
        """
        if self._history is None:
            self._history = HistoryList(self._version, self._solution['sid'],
            )
        return self._history
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.FlexApi.V1.AwsContext {}>'.format(context)

class AwsInstance(InstanceResource):

    def __init__(self, version, payload, sid: str=None):
        """
        Initialize the AwsInstance
        :returns: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        super().__init__(version)

        self._properties = { 
            'account_sid': payload.get('account_sid'),
            'sid': payload.get('sid'),
            'test_string': payload.get('test_string'),
            'test_integer': deserialize.integer(payload.get('test_integer')),
        }

        self._context = None
        self._solution = { 'sid': sid or self._properties['sid'],  }
    
    @property
    def _proxy(self):
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: AwsContext for this AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsContext
        """
        if self._context is None:
            self._context = AwsContext(self._version, sid=self._solution['sid'],)
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
    
    def delete(self):
        """
        Deletes the AwsInstance
        

        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._proxy.delete()
    
    def fetch(self):
        """
        Fetch the AwsInstance
        

        :returns: The fetched AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        return self._proxy.fetch()
    
    def update(self, test_string=values.unset, test_boolean=values.unset):
        """
        Update the AwsInstance
        
        :params str test_string: 
        :params bool test_boolean: 

        :returns: The updated AwsInstance
        :rtype: twilio.rest.flex_api.v1.credential.aws.AwsInstance
        """
        return self._proxy.update(test_string=test_string, test_boolean=test_boolean, )
    
    @property
    def history(self):
        """
        Access the history

        :returns: twilio.rest.flex_api.v1.credential.aws.HistoryList
        :rtype: twilio.rest.flex_api.v1.credential.aws.HistoryList
        """
        return self._proxy.history
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.FlexApi.V1.AwsInstance {}>'.format(context)


