import unittest
from twilio.rest import Client

class IntegrationTest(unittest.TestCase):

    def setUp(self) -> None:
        self.sid = "AC12345678123456781234567812345678"
        self.auth = "CR12345678123456781234567812345678"
        self.client = Client(self.sid, self.auth)

    def test_get_account(self):
        result = list(self.client.api.v2010.accounts.page()._records)
        self.assertEqual(result[0]["test_string"], "Ahoy")
        self.assertEqual(result[1]["test_string"], "Matey")

    def test_create_account(self):
        result = self.client.api.v2010.accounts.create()
        print(result._properties)
        self.assertEqual(result._properties["sid"], self.auth)
        self.assertEqual(result._properties["test_string"], "Ahoy")

    def test_create_call(self):
        result = self.client.api.v2010.accounts(self.sid).calls.create("str", "POST")
        self.assertEqual(result._properties["sid"], self.auth)
        self.assertEqual(result._properties["test_string"], "Ahoy")

    def test_fetch_call(self):
        result = self.client.api.v2010.accounts(self.sid).calls(123).fetch()
        self.assertEqual(result._properties["sid"], self.auth)
        self.assertEqual(result._properties["test_string"], "Ahoy")

    def test_update_call_feedback_summary(self):
        result = self.client.api.v2010.accounts(self.sid).calls\
            .feedback_call_summary(self.auth)\
            .update(end_date="2020-12-31", start_date="2020-01-01")
        self.assertEqual(result._properties['test_array_of_objects'][0]['description'], "issue description")
        self.assertEqual(result._properties['test_array_of_objects'][0]['count'], 4)

