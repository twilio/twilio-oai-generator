import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.http.response import Response


class FlexCallTests(unittest.TestCase):
    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)
        self.generic_request_args = {
            "params": ANY,
            "data": ANY,
            "headers": ANY,
            "auth": ANY,
            "timeout": ANY,
            "allow_redirects": ANY,
        }

    @patch("twilio.http.http_client.TwilioHttpClient.request")
    def test_update_call(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123"}')

        self.client.flex_api.V1.calls("123").update()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Voice/123", **request_args
        )

    def test_invalid_path_param(self):
        self.assertRaises(BaseException, self.client.flex_api.V1.calls("?"))


class AsyncFlexCallTests(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.http_client = AsyncTwilioHttpClient()
        self.client = Client(self.sid, self.auth, http_client=self.http_client)
        self.generic_request_args = {
            "params": ANY,
            "data": ANY,
            "headers": ANY,
            "auth": ANY,
            "timeout": ANY,
            "allow_redirects": ANY,
        }

    async def asyncTearDown(self) -> None:
        await self.http_client.session.close()

    @patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
    async def test_async_update_call(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123"}')

        await self.client.flex_api.V1.calls("123").update_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Voice/123", **request_args
        )
