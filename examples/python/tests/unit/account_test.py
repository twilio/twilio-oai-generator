import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.response import Response

@patch("twilio.http.http_client.TwilioHttpClient.request")
class AccountTests(unittest.TestCase):

    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY, "allow_redirects": ANY}

    def test_create_account(self, mock_request):
        mock_request.return_value = Response(201, "{}")

        self.client.api.v2010.accounts.create()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts.json", **request_args)

    def test_account_shortcut(self, mock_request):
        mock_request.return_value = Response(201, "{\"sid\": \"123\"}")

        self.client.api.v2010.account.calls.create(required_string_property="phone home", test_method="post")
        request_args = self.generic_request_args
        request_args["data"] = {"RequiredStringProperty": "phone home", "TestMethod": "post"}

        mock_request.assert_called_once_with("POST", f"http://api.twilio.com/2010-04-01/Accounts/{self.sid}/Calls.json", **request_args)

    def test_fetch_account(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\"}")

        self.client.api.v2010.accounts("123").fetch()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("GET", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)

    def test_update_account(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\", \"status\": \"stopped\"}")

        self.client.api.v2010.accounts("123").update(status="stopped")
        request_args = self.generic_request_args
        request_args["data"] = {"Status": "stopped"}

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)

    def test_remove_account(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        self.client.api.v2010.accounts("123").delete()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("DELETE", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)

