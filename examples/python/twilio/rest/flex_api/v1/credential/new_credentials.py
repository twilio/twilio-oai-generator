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

from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version



class NewCredentialsList(ListResource):

    def __init__(self, version: Version):
        """
        Initialize the NewCredentialsList

        :param Version version: Version that contains the resource
        
        :returns: twilio.rest.flex_api.v1.credential.new_credentials.NewCredentialsList
        :rtype: twilio.rest.flex_api.v1.credential.new_credentials.NewCredentialsList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Credentials/AWS'.format(**self._solution)
        
        
    
    def create(self, test_string, test_boolean=values.unset, test_integer=values.unset, test_number=values.unset, test_number_float=values.unset, test_number_double=values.unset, test_number_int32=values.unset, test_number_int64=values.unset, test_object=values.unset, test_date_time=values.unset, test_date=values.unset, test_enum=values.unset, test_object_array=values.unset, test_any_type=values.unset, test_any_array=values.unset, permissions=values.unset, some_a2_p_thing=values.unset):
        """
        Create the NewCredentialsInstance

        :param str test_string: 
        :param bool test_boolean: 
        :param int test_integer: 
        :param float test_number: 
        :param float test_number_float: 
        :param float test_number_double: 
        :param float test_number_int32: 
        :param int test_number_int64: 
        :param dict test_object: 
        :param datetime test_date_time: 
        :param date test_date: 
        :param Status test_enum: 
        :param list[object] test_object_array: 
        :param object test_any_type: 
        :param list[object] test_any_array: 
        :param list[str] permissions: A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`.
        :param str some_a2_p_thing: 
        
        :returns: The created NewCredentialsInstance
        :rtype: twilio.rest.flex_api.v1.credential.new_credentials.NewCredentialsInstance
        """
        data = values.of({ 
            'TestString': test_string,
            'TestBoolean': test_boolean,
            'TestInteger': test_integer,
            'TestNumber': test_number,
            'TestNumberFloat': test_number_float,
            'TestNumberDouble': test_number_double,
            'TestNumberInt32': test_number_int32,
            'TestNumberInt64': test_number_int64,
            'TestObject': serialize.object(test_object),
            'TestDateTime': serialize.iso8601_datetime(test_date_time),
            'TestDate': serialize.iso8601_date(test_date),
            'TestEnum': test_enum,
            'TestObjectArray': serialize.map(test_object_array, lambda e: e),
            'TestAnyType': serialize.object(test_any_type),
            'TestAnyArray': serialize.map(test_any_array, lambda e: e),
            'Permissions': serialize.map(permissions, lambda e: e),
            'SomeA2PThing': some_a2_p_thing,
        })
        
        payload = self._version.create(method='POST', uri=self._uri, data=data,)

        return NewCredentialsInstance(self._version, payload)
    


    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1.NewCredentialsList>'

class NewCredentialsInstance(InstanceResource):

    def __init__(self, version, payload):
        """
        Initialize the NewCredentialsInstance
        :returns: twilio.rest.flex_api.v1.credential.new_credentials.NewCredentialsInstance
        :rtype: twilio.rest.flex_api.v1.credential.new_credentials.NewCredentialsInstance
        """
        super().__init__(version)

        self._properties = { 
            'account_sid': payload.get('account_sid'),
            'sid': payload.get('sid'),
            'test_string': payload.get('test_string'),
            'test_integer': deserialize.integer(payload.get('test_integer')),
        }

        self._context = None
        self._solution = {  }
    
    
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
    
    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.FlexApi.V1.NewCredentialsInstance {}>'.format(context)



