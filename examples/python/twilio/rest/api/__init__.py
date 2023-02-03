from twilio.base.domain import Domain
from twilio.rest.api.v2010 import V2010


class Api(Domain):
    def __init__(self, twilio):
        super().__init__(twilio)
        self.base_url = 'https://api.twilio.com'
        self._V2010 = None

    @property
    def V2010(self):
        if self._V2010 is None:
            self._V2010 = V2010(self)
        return self._V2010

    @property
    def accounts(self):
        return self.v2010.accounts

    @property
    def account(self):
        return self.v2010.account

    def __repr__(self):
        return '<Twilio.Api>'
