import unittest

from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.rest import Client


class IntegrationTest(unittest.TestCase):
    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)

    def test_page_accounts(self):
        result = list(self.client.api.v2010.accounts.page()._records)
        self.assertEqual(result[0]["test_string"], "Ahoy")
        self.assertEqual(result[1]["test_string"], "Matey")

    def test_list_accounts(self):
        result = self.client.api.v2010.accounts.list(limit=2)
        self.assertEqual(result[0].test_string, "Ahoy")
        self.assertEqual(result[1].test_string, "Matey")

    def test_create_account(self):
        result = self.client.api.v2010.accounts.create()
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    def test_create_call(self):
        result = self.client.api.v2010.accounts(self.sid).calls.create("str", "POST")
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    def test_fetch_call(self):
        result = self.client.api.v2010.accounts(self.sid).calls(123).fetch()
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    def test_update_call_feedback_summary(self):
        result = (
            self.client.api.v2010.accounts(self.sid)
            .calls.feedback_call_summary(self.auth)
            .update(end_date="2020-12-31", start_date="2020-01-01")
        )
        self.assertEqual(
            result.test_array_of_objects[0]["description"],
            "issue description",
        )
        self.assertEqual(result.test_array_of_objects[0]["count"], 4)


class AsyncIntegrationTests(unittest.IsolatedAsyncioTestCase):
    async def asyncSetUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.http_client = AsyncTwilioHttpClient()
        self.client = Client(self.sid, self.auth, http_client=self.http_client)

    async def asyncTearDown(self) -> None:
        await self.http_client.session.close()

    async def test_page_accounts(self):
        result = list((await self.client.api.v2010.accounts.page_async())._records)
        self.assertEqual(result[0]["test_string"], "Ahoy")
        self.assertEqual(result[1]["test_string"], "Matey")

    async def test_list_accounts(self):
        result = await self.client.api.v2010.accounts.list_async(limit=2)
        self.assertEqual(result[0].test_string, "Ahoy")
        self.assertEqual(result[1].test_string, "Matey")

    async def test_create_account(self):
        result = await self.client.api.v2010.accounts.create_async()
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    async def test_create_call(self):
        result = await self.client.api.v2010.accounts(self.sid).calls.create_async(
            "str", "POST"
        )
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    async def test_fetch_call(self):
        result = await self.client.api.v2010.accounts(self.sid).calls(123).fetch_async()
        self.assertEqual(result.sid, self.auth)
        self.assertEqual(result.test_string, "Ahoy")

    async def test_update_call_feedback_summary(self):
        result = (
            await self.client.api.v2010.accounts(self.sid)
            .calls.feedback_call_summary(self.auth)
            .update_async(end_date="2020-12-31", start_date="2020-01-01")
        )
        self.assertEqual(
            result.test_array_of_objects[0]["description"],
            "issue description",
        )
        self.assertEqual(result.test_array_of_objects[0]["count"], 4)
