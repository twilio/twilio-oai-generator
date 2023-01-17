from twilio.base.domain import Domain
from twilio.rest.FlexApi.v1 import V1


class FlexApi(Domain):
    def __init__(self, twilio):
        """
        Initialize the FlexApi Domain

        :returns: Domain for FlexApi
        :rtype: twilio.rest.v1.FlexApi
        """
        super(FlexApi, self).__init__(twilio)
        self.base_url = 'https://FlexApi.twilio.com'
        self._V1 = None

    @property
    def V1(self):
        """
        :returns: Versions v1 of FlexApi
        :rtype: twilio.rest.FlexApi.v1
        """
        if self._V1 is None:
            self._V1 = V1(self)
        return self._V1

    @property
    def calls(self):
        """
        :rtype: twilio.rest.v1.calls
        """
        return self.v1.calls

    @property
    def credentials(self):
        """
        :rtype: twilio.rest.v1.credentials
        """
        return self.v1.credentials

    def __repr__(self):
        """
        Provide a friendly representation
        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.FlexApi>'
