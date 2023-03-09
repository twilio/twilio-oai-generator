import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.http.response import Response

class VersionlessTests(unittest.TestCase):

    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY, "allow_redirects": ANY}

    def test_create_deployed_devices_fleet(self):
        # TODO: Add test
        pass

    def test_fetch_deployed_devices_fleet(self):
        # TODO: Add test
        pass

@patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
class AsyncVersionlessTests(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.http_client = AsyncTwilioHttpClient()
        self.client = Client(self.sid, self.auth, http_client=self.http_client)
        self.generic_request_args = {"params": ANY, "data": ANY, "headers": ANY, "auth": ANY, "timeout": ANY,
                                     "allow_redirects": ANY}

    async def asyncTearDown(self) -> None:
        await self.http_client.session.close()

    async def test_async_create_deployed_devices_fleet(self, mock_request):
        mock_request.return_value = Response(201, "{\"account_sid\": \"123\", \"sid\": \"1\"}")

        await self.client.preview.deployed_devices.fleets.create_async(name="test fleet")
        request_args = self.generic_request_args
        request_args["data"] = {"Name": "test fleet"}

        print(request_args)

        mock_request.assert_called_once_with("POST", "http://preview.twilio.com/DeployedDevices/Fleets", **request_args)

    async def test_async_fetch_deployed_devices_fleet(self):
        # TODO: Add test
        pass