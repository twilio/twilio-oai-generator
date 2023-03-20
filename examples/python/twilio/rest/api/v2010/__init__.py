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
from twilio.base.version import Version
from twilio.base.domain import Domain
from twilio.rest.api.v2010.account import AccountList
from twilio.rest.api.v2010.account import AccountContext


class V2010(Version):
    def __init__(self, domain: Domain):
        """
        Initialize the V2010 version of Api

        :param domain: The Twilio.api domain
        """
        super().__init__(domain, "2010-04-01")
        self._accounts: Optional[AccountList] = None
        self._account: Optional[AccountContext] = None

    @property
    def accounts(self) -> AccountList:
        if self._accounts is None:
            self._accounts = AccountList(self)
        return self._accounts

    @property
    def account(self) -> AccountContext:
        if self._account is None:
            self._account = AccountContext(self, self.domain.twilio.account_sid)
        return self._account

    @account.setter
    def account(self, value: AccountContext):
        """
        Setter to override account
        :param value: value to use as account
        """
        self._account = value

    def __repr__(self) -> str:
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        """
        return "<Twilio.Api.V2010>"
