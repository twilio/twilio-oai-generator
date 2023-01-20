from twilio.base import ClientBase

class Client(ClientBase):
    """ A client for accessing the Twilio API. """

    def __init__(self, username=None, password=None, account_sid=None, region=None,
                 http_client=None, environment=None, edge=None,
                 user_agent_extensions=None):
        super().__init__(username, password, account_sid, region, http_client, environment, edge, user_agent_extensions)

    @property
    def api(self):
        from twilio.rest.api import Api
        return Api(self)

    @property
    def flex_api(self):
        from twilio.rest.flex_api import FlexApi
        return FlexApi(self)
  