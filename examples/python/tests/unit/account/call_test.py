import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.http.response import Response

@patch("twilio.http.http_client.TwilioHttpClient.request")
class AccountCallTests(unittest.TestCase):
    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY, "allow_redirects": ANY}

    def test_create_call(self, mock_request):
        mock_request.return_value = Response(201, "{\"account_sid\": \"123\", \"sid\": \"1\"}")

        self.client.api.v2010.accounts("123").calls.create(required_string_property="phone home", test_method="post")
        request_args = self.generic_request_args
        request_args["data"] = {"RequiredStringProperty": "phone home", "TestMethod": "post"}

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts/123/Calls.json", **request_args)

    def test_fetch_call(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\", \"sid\": \"1\"}")

        self.client.api.v2010.accounts("123").calls("1").fetch()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("GET", "http://api.twilio.com/2010-04-01/Accounts/123/Calls/1.json", **request_args)

    def test_remove_call(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        self.client.api.v2010.accounts("123").calls("1").delete()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("DELETE", "http://api.twilio.com/2010-04-01/Accounts/123/Calls/1.json", **request_args)

    def test_create_feedback_summary(self, mock_request):
        mock_request.return_value = Response(201, "{\"account_sid\": \"123\", \"test_array\": [{\"count\": \"4\"}]}")

        self.client.api.v2010.accounts("123").calls.feedback_call_summary("456").update(account_sid="other_account", start_date="2023-02-10", end_date="2023-02-10")
        request_args = self.generic_request_args
        request_args["data"] = {"AccountSid": "other_account", "StartDate": "2023-02-10", "EndDate": "2023-02-10"}

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts/123/Calls/Feedback/Summary/456.json", **request_args)

@patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
class AsyncAccountCallTests(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.http_client = AsyncTwilioHttpClient()
        self.client = Client(self.sid, self.auth, http_client=self.http_client)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY, "allow_redirects": ANY}

    async def asyncTearDown(self) -> None:
        await self.http_client.session.close()

    async def test_async_create_call(self, mock_request):
        mock_request.return_value = Response(201, "{\"account_sid\": \"123\", \"sid\": \"1\"}")

        await self.client.api.v2010.accounts("123").calls.create_async(required_string_property="phone home", test_method="post")
        request_args = self.generic_request_args
        request_args["data"] = {"RequiredStringProperty": "phone home", "TestMethod": "post"}

        mock_request.assert_called_once_with("POST", "http://api.twilio.com/2010-04-01/Accounts/123/Calls.json", **request_args)

    async def test_async_fetch_call(self, mock_request):
        mock_request.return_value = Response(200, "{\"account_sid\": \"123\", \"sid\": \"1\"}")

        await self.client.api.v2010.accounts("123").calls("1").fetch_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("GET", "http://api.twilio.com/2010-04-01/Accounts/123/Calls/1.json",
                                             **request_args)

    async def test_async_remove_call(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        await self.client.api.v2010.accounts("123").calls("1").delete_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with("DELETE", "http://api.twilio.com/2010-04-01/Accounts/123/Calls/1.json", **request_args)

    async def test_async_create_feedback_summary(self, mock_request):
        mock_request.return_value = Response(201, "{\"account_sid\": \"123\", \"test_array\": [{\"count\": \"4\"}]}")

        await self.client.api.v2010.accounts("123").calls.feedback_call_summary("456").update_async(account_sid="other_account",
                                                                                        start_date="2023-02-10",
                                                                                        end_date="2023-02-10")
        request_args = self.generic_request_args
        request_args["data"] = {"AccountSid": "other_account", "StartDate": "2023-02-10", "EndDate": "2023-02-10"}

        mock_request.assert_called_once_with("POST",
                                             "http://api.twilio.com/2010-04-01/Accounts/123/Calls/Feedback/Summary/456.json",
                                             **request_args)
