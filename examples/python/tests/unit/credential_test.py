import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.http.response import Response


@patch("twilio.http.http_client.TwilioHttpClient.request")
class CredentialTests(unittest.TestCase):
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

    def test_create_credentials(self, mock_request):
        mock_request.return_value = Response(201, "{}")

        self.client.flex_api.V1.credentials.new_credentials.create(
            test_string="abc",
            test_boolean=True,
            test_any_array=['{"any": "thing"}', '{"another": "thing"}'],
            test_any_type={"type": "any"},
        )
        request_args = self.generic_request_args
        request_args["data"] = {
            "TestString": "abc",
            "TestBoolean": True,
            "TestAnyArray": ['{"any": "thing"}', '{"another": "thing"}'],
            "TestAnyType": '{"type": "any"}',
        }

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Credentials/AWS", **request_args
        )

    def test_update_credentials(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123"}')

        self.client.flex_api.V1.credentials.aws("123").update(
            test_boolean=False, test_string="new string"
        )
        request_args = self.generic_request_args
        request_args["data"] = {"TestString": "new string", "TestBoolean": False}

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Credentials/AWS/123", **request_args
        )

    def test_fetch_history_instance(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123", "test_integer": 1}')

        self.client.flex_api.V1.credentials.aws("123").history(1).fetch(
            add_ons_data={"twilio.segment": "engage"}
        )
        request_args = self.generic_request_args
        request_args["params"] = {"AddOns.twilio.segment": "engage"}

        mock_request.assert_called_once_with(
            "GET",
            "http://flex-api.twilio.com/v1/Credentials/AWS/123/History/1",
            **request_args
        )

    def test_remove_credentials(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        self.client.flex_api.V1.credentials.aws("123").delete()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with(
            "DELETE",
            "http://flex-api.twilio.com/v1/Credentials/AWS/123",
            **request_args
        )


@patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
class AsyncCredentialTests(unittest.IsolatedAsyncioTestCase):
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

    async def test_async_create_credentials(self, mock_request):
        mock_request.return_value = Response(201, "{}")

        await self.client.flex_api.V1.credentials.new_credentials.create_async(
            test_string="abc",
            test_boolean=True,
            test_any_array=['{"any": "thing"}', '{"another": "thing"}'],
            test_any_type={"type": "any"},
        )
        request_args = self.generic_request_args
        request_args["data"] = {
            "TestString": "abc",
            "TestBoolean": True,
            "TestAnyArray": ['{"any": "thing"}', '{"another": "thing"}'],
            "TestAnyType": '{"type": "any"}',
        }

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Credentials/AWS", **request_args
        )

    async def test_async_update_credentials(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123"}')

        await self.client.flex_api.V1.credentials.aws("123").update_async(
            test_boolean=False, test_string="new string"
        )
        request_args = self.generic_request_args
        request_args["data"] = {"TestString": "new string", "TestBoolean": False}

        mock_request.assert_called_once_with(
            "POST", "http://flex-api.twilio.com/v1/Credentials/AWS/123", **request_args
        )

    async def test_async_fetch_history_instance(self, mock_request):
        mock_request.return_value = Response(200, '{"sid": "123", "test_integer": 1}')

        await self.client.flex_api.V1.credentials.aws("123").history(1).fetch_async(
            add_ons_data={"twilio.segment": "engage"}
        )
        request_args = self.generic_request_args
        request_args["params"] = {"AddOns.twilio.segment": "engage"}

        mock_request.assert_called_once_with(
            "GET",
            "http://flex-api.twilio.com/v1/Credentials/AWS/123/History/1",
            **request_args
        )

    async def test_async_remove_credentials(self, mock_request):
        mock_request.return_value = Response(204, "{}")

        await self.client.flex_api.V1.credentials.aws("123").delete_async()
        request_args = self.generic_request_args

        mock_request.assert_called_once_with(
            "DELETE",
            "http://flex-api.twilio.com/v1/Credentials/AWS/123",
            **request_args
        )
