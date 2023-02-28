import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.response import Response

@patch("twilio.http.http_client.TwilioHttpClient.request")
class CredentialTests(unittest.TestCase):

    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY, "allow_redirects": ANY}

    def test_create_credentials(self, mock_request):
        mock_request.return_value = Response(201, "{}")

        self.client.flex_api.V1.credentials.new_credentials.create(
            test_string="abc", test_boolean=True, test_any_array=['{"any": "thing"}', '{"another": "thing"}'], test_any_type={"type": "any"})
        request_args = self.generic_request_args
        request_args["data"] = {"TestString": "abc", "TestBoolean": True, "TestAnyArray": ['{"any": "thing"}', '{"another": "thing"}'], "TestAnyType": '{"type": "any"}'}

        mock_request.assert_called_once_with("POST", "https://flex-api.twilio.com/v1/Credentials/AWS", **request_args)

    def test_update_credentials(self, mock_request):
        mock_request.return_value = Response(200, "{\"sid\": \"123\"}")

        self.client.flex_api.V1.credentials.aws("123").update(test_boolean=False, test_string="new string")
        request_args = self.generic_request_args
        request_args["data"] = {"TestString": "new string", "TestBoolean": False}

        mock_request.assert_called_once_with("POST", "https://flex-api.twilio.com/v1/Credentials/AWS/123", **request_args)

    def test_fetch_history_instance(self, mock_request):
        mock_request.return_value = Response(200, "{\"sid\": \"123\"}")

        self.client.flex_api.V1.credentials.aws("123").history().fetch(add_ons_data={"twilio.segment": "engage"})
        request_args = self.generic_request_args
        request_args["params"] = {"AddOns.twilio.segment": "engage"}

        mock_request.assert_called_once_with("GET", "https://flex-api.twilio.com/v1/Credentials/AWS/123/History", **request_args)
   
    def test_remove_credentials(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        self.client.flex_api.V1.credentials.aws("123").delete()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("DELETE", "https://flex-api.twilio.com/v1/Credentials/AWS/123", **request_args)
