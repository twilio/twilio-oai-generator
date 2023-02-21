"""
    This code was generated by
   ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    Twilio - Versionless
    No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)

    NOTE: This class is auto generated by OpenAPI Generator.
    https://openapi-generator.tech
    Do not edit the class manually.
"""

from twilio.base.version import Version
from twilio.base.domain import Domain
from twilio.rest.versionless.understand.assistant import AssistantList


class Understand(Version):

    def __init__(self, domain: Domain):
        """
        Initialize the Understand version of Versionless

        :param domain: The Twilio.versionless domain
        """
        super().__init__(domain)
        self.version = 'understand'
        self._assistants = None
        
    @property
    def assistants(self) -> AssistantList:
        if self._assistants is None:
            self._assistants = AssistantList(self)
        return self._assistants

    def __repr__(self) -> str:
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Versionless.Understand>'
