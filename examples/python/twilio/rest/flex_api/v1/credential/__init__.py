r"""
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


from typing import Optional


from twilio.base.list_resource import ListResource
from twilio.base.version import Version

from twilio.rest.flex_api.v1.credential.aws import AwsList
from twilio.rest.flex_api.v1.credential.new_credentials import NewCredentialsList


class CredentialList(ListResource):
    def __init__(self, version: Version):
        """
        Initialize the CredentialList

        :param version: Version that contains the resource

        """
        super().__init__(version)

        self._uri = "/Credentials"

        self._aws: Optional[AwsList] = None
        self._new_credentials: Optional[NewCredentialsList] = None

    @property
    def aws(self) -> AwsList:
        """
        Access the aws
        """
        if self._aws is None:
            self._aws = AwsList(self._version)
        return self._aws

    @property
    def new_credentials(self) -> NewCredentialsList:
        """
        Access the new_credentials
        """
        if self._new_credentials is None:
            self._new_credentials = NewCredentialsList(self._version)
        return self._new_credentials

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.FlexApi.V1.CredentialList>"
