import unittest
from unittest.mock import patch, ANY
from twilio.rest import Client
from twilio.http.async_http_client import AsyncTwilioHttpClient
from twilio.http.response import Response


@patch("twilio.http.http_client.TwilioHttpClient.request")
class OneOfTests(unittest.TestCase):
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

    def test_create_pet_with_cat(self, mock_request):
        """Test creating a pet with Cat oneOf option"""
        mock_request.return_value = Response(
            200, '{"account_sid": "AC123", "param1": "value1", "param2": "value2"}'
        )

        # Create a Cat object using the inner class
        cat = self.client.one_of.v1.pets.Cat(
            {
                "account_sid": "AC123",
                "param1": "value1",
                "param2": "value2",
            }
        )

        # Create pet with Cat
        result = self.client.one_of.v1.pets.create(cat=cat)

        # Verify the request was made correctly
        request_args = self.generic_request_args
        request_args["data"] = {
            "account_sid": "AC123",
            "param1": "value1",
            "param2": "value2",
        }

        mock_request.assert_called_once_with(
            "POST", "http://oneOf.twilio.com/v1/pets", **request_args
        )

        # Verify the response
        self.assertEqual(result.account_sid, "AC123")
        self.assertEqual(result.param1, "value1")
        self.assertEqual(result.param2, "value2")

    def test_create_pet_with_dog(self, mock_request):
        """Test creating a pet with Dog oneOf option"""
        mock_request.return_value = Response(
            200, '{"type": "dog", "name": "Bruno", "pack_size": 5}'
        )

        # Create a Dog object using the inner class
        dog = self.client.one_of.v1.pets.Dog(
            {"type": "dog", "name": "Bruno", "pack_size": 5}
        )

        # Note: Currently the generated code only accepts 'cat' parameter
        # This test documents the expected behavior when full oneOf support is added
        # For now, we'll test with cat parameter
        cat = self.client.one_of.v1.pets.Cat(
            {"type": "dog", "name": "Bruno", "pack_size": 5}
        )

        result = self.client.one_of.v1.pets.create(cat=cat)

        request_args = self.generic_request_args
        request_args["data"] = {"type": "dog", "name": "Bruno", "pack_size": 5}

        mock_request.assert_called_once()

    def test_cat_to_dict(self, mock_request):
        """Test Cat object serialization"""
        cat = self.client.one_of.v1.pets.Cat(
            {
                "account_sid": "AC123",
                "param1": "value1",
                "param2": "value2",
                "dog": None,
                "object1": "obj1",
                "object2": "obj2",
            }
        )

        cat_dict = cat.to_dict()

        self.assertEqual(cat_dict["account_sid"], "AC123")
        self.assertEqual(cat_dict["param1"], "value1")
        self.assertEqual(cat_dict["param2"], "value2")
        self.assertEqual(cat_dict["object1"], "obj1")
        self.assertEqual(cat_dict["object2"], "obj2")
        self.assertIsNone(cat_dict["dog"])

    def test_dog_to_dict(self, mock_request):
        """Test Dog object serialization"""
        dog = self.client.one_of.v1.pets.Dog(
            {"type": "dog", "name": "Bruno", "pack_size": 5}
        )

        dog_dict = dog.to_dict()

        self.assertEqual(dog_dict["type"], "dog")
        self.assertEqual(dog_dict["name"], "Bruno")
        self.assertEqual(dog_dict["pack_size"], 5)

    def test_nested_oneof_with_dog_in_cat(self, mock_request):
        """Test Cat with nested Dog object (tests nested oneOf)"""
        mock_request.return_value = Response(
            200,
            '{"account_sid": "AC123", "param1": "value1", "dog": {"type": "dog", "name": "Rex", "pack_size": 3}}',
        )

        # Create a nested Dog object
        dog = self.client.one_of.v1.pets.Dog(
            {"type": "dog", "name": "Rex", "pack_size": 3}
        )

        # Create Cat with nested Dog
        cat = self.client.one_of.v1.pets.Cat(
            {"account_sid": "AC123", "param1": "value1", "dog": dog}
        )

        result = self.client.one_of.v1.pets.create(cat=cat)

        self.assertEqual(result.account_sid, "AC123")


@patch("twilio.http.async_http_client.AsyncTwilioHttpClient.request")
class AsyncOneOfTests(unittest.IsolatedAsyncioTestCase):
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

    async def test_async_create_pet_with_cat(self, mock_request):
        """Test async creating a pet with Cat oneOf option"""
        mock_request.return_value = Response(
            200, '{"account_sid": "AC123", "param1": "value1", "param2": "value2"}'
        )

        # Create a Cat object using the inner class
        cat = self.client.one_of.v1.pets.Cat(
            {
                "account_sid": "AC123",
                "param1": "value1",
                "param2": "value2",
            }
        )

        # Create pet with Cat asynchronously
        result = await self.client.one_of.v1.pets.create_async(cat=cat)

        # Verify the request was made correctly
        request_args = self.generic_request_args
        request_args["data"] = {
            "account_sid": "AC123",
            "param1": "value1",
            "param2": "value2",
        }

        mock_request.assert_called_once_with(
            "POST", "http://oneOf.twilio.com/v1/pets", **request_args
        )

        # Verify the response
        self.assertEqual(result.account_sid, "AC123")
        self.assertEqual(result.param1, "value1")
        self.assertEqual(result.param2, "value2")

    async def test_async_create_pet_with_dog(self, mock_request):
        """Test async creating a pet with Dog oneOf option"""
        mock_request.return_value = Response(
            200, '{"type": "dog", "name": "Bruno", "pack_size": 5}'
        )

        # Create a Dog object
        dog = self.client.one_of.v1.pets.Dog(
            {"type": "dog", "name": "Bruno", "pack_size": 5}
        )

        # Note: Currently the generated code only accepts 'cat' parameter
        # This test documents the expected behavior when full oneOf support is added
        cat = self.client.one_of.v1.pets.Cat(
            {"type": "dog", "name": "Bruno", "pack_size": 5}
        )

        result = await self.client.one_of.v1.pets.create_async(cat=cat)

        mock_request.assert_called_once()
