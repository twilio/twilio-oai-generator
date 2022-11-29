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



class HistoryContext(ListResource):
    def __init__(self, version: V1, sid: str):
        # TODO: needs comments
        super(HistoryContext, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = '/Credentials/AWS/${sid}/History'

        # Components
        



class HistoryListInstance(ListResource):
    def __init__(self, version: V1, sid: str):
        # TODO: needs comments
        super(HistoryListInstance, self).__init__(version)

        # Path Solution
        self._solution = { sid,  }
        self._uri = ''

        # Components
        

