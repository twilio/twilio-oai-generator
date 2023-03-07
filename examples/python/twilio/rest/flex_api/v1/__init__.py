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

from twilio.base.version import Version
from twilio.base.domain import Domain
from twilio.rest.flex_api.v1.call import CallList
from twilio.rest.flex_api.v1.credential import CredentialList


class V1(Version):

    def __init__(self, domain: Domain):
        """
        Initialize the V1 version of FlexApi

        :param domain: The Twilio.flex_api domain
        """
        super().__init__(domain)
        self.version = 'v1'
        self._calls = None
        self._credentials = None
        
    @property
    def calls(self) -> CallList:
        """
        :rtype: twilio.rest.flex_api.v1.call.CallList
        """
        if self._calls is None:
            self._calls = CallList(self)
        return self._calls

    @property
    def credentials(self) -> CredentialList:
        """
        :rtype: twilio.rest.flex_api.v1.credential.CredentialList
        """
        if self._credentials is None:
            self._credentials = CredentialList(self)
        return self._credentials

    def __repr__(self) -> str:
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi.V1>'
