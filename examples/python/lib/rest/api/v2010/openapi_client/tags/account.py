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

from twilio.base.page import Page

from twilio.rest.account.call import CallListInstance


class AccountContext(InstanceContext):
    def __init__(self, version: V2010, sid: str):
        # TODO: needs autogenerated docs
        super(AccountContextList, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
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
            return AccountInstance(
                self._version,
                payload,
                sid=self._solution['sid'],
            )
            
            
        
        def update(self, body):
            data = values.of({
                'body': body,
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
        super(AccountInstance, self).__init__(version)
        self._properties = { 
            'account_sid' = payload.get('account_sid'),
            'sid' = payload.get('sid'),
            'test_string' = payload.get('test_string'),
            'test_integer' = payload.get('test_integer'),
            'test_object' = payload.get('test_object'),
            'test_date_time' = payload.get('test_date_time'),
            'test_number' = payload.get('test_number'),
            'price_unit' = payload.get('price_unit'),
            'test_number_float' = payload.get('test_number_float'),
            'test_number_decimal' = payload.get('test_number_decimal'),
            'test_enum' = payload.get('test_enum'),
            'a2p_profile_bundle_sid' = payload.get('a2p_profile_bundle_sid'),
            'test_array_of_integers' = payload.get('test_array_of_integers'),
            'test_array_of_array_of_integers' = payload.get('test_array_of_array_of_integers'),
            'test_array_of_objects' = payload.get('test_array_of_objects'),
            'test_array_of_enum' = payload.get('test_array_of_enum'),
        }

        self._context = None
        self._solution = {
            'sid': sid or self._properties['sid']
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



class AccountListInstance(ListResource):
    def __init__(self, version: V2010):
        # TODO: needs autogenerated docs
        super(AccountListInstanceList, self).__init__(version)

        # Path Solution
        self._solution = {  }
        self._uri = '/Accounts.json'
        
        
        def create(self, x_twilio_webhook_enabled, body):
            data = values.of({
                'x_twilio_webhook_enabled': x_twilio_webhook_enabled,'body': body,
            })

            payload = self._version.create(method='post', uri=self._uri, data=data, )

            return AccountInstance(self._version, payload, )
            
        
        def page(self, date_created, date_test, date_created, date_created, page_size):
            
            data = values.of({
                'date_created': date_created,'date_test': date_test,'date_created': date_created,'date_created': date_created,'page_size': page_size,
            })

            payload = self._version.create(method='get', uri=self._uri, data=data, )

            return AccountPage(self._version, payload, )
        

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Api.V2010.AccountListInstance>'

