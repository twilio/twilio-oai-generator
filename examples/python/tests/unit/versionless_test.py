import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
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
