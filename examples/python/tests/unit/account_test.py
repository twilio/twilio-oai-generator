import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
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

@patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
class AsyncAccountTests(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.http_client = AsyncTwilioHttpClient()
        self.client = Client(self.sid, self.auth, http_client=self.http_client)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY,
                                     "allow_redirects": ANY}

    async def asyncTearDown(self) -> None:
        await self.http_client.session.close()

    async def test_async_create_account(self, mock_request):
        mock_request.return_value = Response(201, "{}")

        await self.client.api.v2010.accounts.create_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts.json", **request_args)

    async def test_async_account_shortcut(self, mock_request):
        mock_request.return_value = Response(201, "{\"sid\": \"123\"}")

        await self.client.api.v2010.account.calls.create_async(required_string_property="phone home", test_method="post")
        request_args = self.generic_request_args
        request_args["data"] = {"RequiredStringProperty": "phone home", "TestMethod": "post"}

        mock_request.assert_called_once_with("POST", f"http://api.twilio.com/2010-04-01/Accounts/{self.sid}/Calls.json", **request_args)

    async def test_async_fetch_account(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\"}")

        await self.client.api.v2010.accounts("123").fetch_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("GET", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)

    async def test_async_update_account(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\", \"status\": \"stopped\"}")

        await self.client.api.v2010.accounts("123").update_async(status="stopped")
        request_args = self.generic_request_args
        request_args["data"] = {"Status": "stopped"}

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)

    async def test_async_remove_account(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        await self.client.api.v2010.accounts("123").delete_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("DELETE", "http://api.twilio.com/2010-04-01/Accounts/123.json", **request_args)
