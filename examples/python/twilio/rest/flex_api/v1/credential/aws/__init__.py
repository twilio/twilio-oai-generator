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
from twilio.rest.flex_api.v1.aws.history import HistoryList


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
        # TODO: needs autogenerated docs
        super().__init__(version)

        # Path Solution
        self._solution = { 'sid': sid,  }
        self._uri = '/Credentials/AWS/${sid}'
        
        self._history = None
    
    def delete(self):
        
        

        """
        Deletes the AwsInstance

        :returns: True if delete succeeds, False otherwise
        :rtype: bool
        """
        return self._version.delete(method='DELETE', uri=self._uri, )
    
    def fetch(self):
        
        """
        Fetch the AwsInstance

        :returns: The fetched AwsInstance
        #TODO: add rtype docs
        """
        payload = self._version.fetch(method='GET', uri=self._uri, )

        return AwsInstance(self._version, payload, sid=self._solution['sid'], )
        

        
    
    def update(self, test_string, test_boolean):
        data = values.of({
            'test_string': test_string,'test_boolean': test_boolean,
        })

        payload = self._version.update(method='post', uri=self._uri, data=data, )

        return AwsInstance(self._version, payload, sid=self._solution['sid'], )
        
        

        
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.AwsContext>'



class AwsInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        super().__init__(version)
        self._properties = { 
            'account_sid' : payload.get('account_sid'),
            'sid' : payload.get('sid'),
            'test_string' : payload.get('test_string'),
            'test_integer' : payload.get('test_integer'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid'],
        }

    @property
    def _proxy(self):
        if self._context is None:
            self._context = AwsContext(
                self._version,
                sid=self._solution['sid'],
            )
        return self._context

    @property
    def history(self):
        return self._proxy.history
    

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.FlexApi.V1.AwsInstance {}>'.format(context)



