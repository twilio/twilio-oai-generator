from twilio.base.domain import Domain
from twilio.rest.flex_api.v1 import V1


class FlexApi(Domain):
    def __init__(self, twilio):
        super().__init__(twilio)
        self.base_url = "http://flex-api.twilio.com"
        self._V1 = None

    @property
    def V1(self):
        if self._V1 is None:
            self._V1 = V1(self)
        return self._V1

    @property
    def calls(self):
        return self.v1.calls

    @property
    def credentials(self):
        return self.v1.credentials

    def __repr__(self):
        return "<Twilio.FlexApi>"
