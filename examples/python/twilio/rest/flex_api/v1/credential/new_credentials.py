r"""
    This code was generated by
   ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    Twilio - Accounts
    This is the public Twilio REST API.

    NOTE: This class is auto generated by OpenAPI Generator.
    https://openapi-generator.tech
    Do not edit the class manually.
"""


from typing import Optional
from twilio.base import deserialize, serialize, values

from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version


class NewCredentialsInstance(InstanceResource):
    def __init__(self, version, payload):
        """
        Initialize the NewCredentialsInstance
        """
        super().__init__(version)

        self._account_sid: Optional[str] = payload.get("account_sid")
        self._sid: Optional[str] = payload.get("sid")
        self._test_string: Optional[str] = payload.get("test_string")
        self._test_integer: Optional[int] = deserialize.integer(
            payload.get("test_integer")
        )

        self._solution = {}

    @property
    def account_sid(self) -> Optional[str]:
        return self._account_sid

    @property
    def sid(self) -> Optional[str]:
        return self._sid

    @property
    def test_string(self) -> Optional[str]:
        return self._test_string

    @property
    def test_integer(self) -> Optional[int]:
        return self._test_integer

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.FlexApi.V1.NewCredentialsInstance {}>".format(context)


class NewCredentialsList(ListResource):
    def __init__(self, version: Version):
        """
        Initialize the NewCredentialsList

        :param version: Version that contains the resource

        """
        super().__init__(version)

        self._uri = "/Credentials/AWS"

    def create(
        self,
        test_string,
        test_boolean=values.unset,
        test_integer=values.unset,
        test_number=values.unset,
        test_number_float=values.unset,
        test_number_double=values.unset,
        test_number_int32=values.unset,
        test_number_int64=values.unset,
        test_object=values.unset,
        test_date_time=values.unset,
        test_date=values.unset,
        test_enum=values.unset,
        test_object_array=values.unset,
        test_any_type=values.unset,
        test_any_array=values.unset,
        permissions=values.unset,
        some_a2p_thing=values.unset,
    ) -> NewCredentialsInstance:
        """
        Create the NewCredentialsInstance

        :param str test_string:
        :param bool test_boolean:
        :param int test_integer:
        :param float test_number:
        :param float test_number_float:
        :param float test_number_double:
        :param float test_number_int32:
        :param int test_number_int64:
        :param Dict[str, object] test_object:
        :param datetime test_date_time:
        :param date test_date:
        :param &quot;NewCredentialsInstance.Status&quot; test_enum:
        :param List[object] test_object_array:
        :param object test_any_type:
        :param List[object] test_any_array:
        :param List[str] permissions: A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`.
        :param str some_a2p_thing:

        :returns: The created NewCredentialsInstance
        """
        data = values.of(
            {
                "TestString": test_string,
                "TestBoolean": test_boolean,
                "TestInteger": test_integer,
                "TestNumber": test_number,
                "TestNumberFloat": test_number_float,
                "TestNumberDouble": test_number_double,
                "TestNumberInt32": test_number_int32,
                "TestNumberInt64": test_number_int64,
                "TestObject": serialize.object(test_object),
                "TestDateTime": serialize.iso8601_datetime(test_date_time),
                "TestDate": serialize.iso8601_date(test_date),
                "TestEnum": test_enum,
                "TestObjectArray": serialize.map(
                    test_object_array, lambda e: serialize.object(e)
                ),
                "TestAnyType": serialize.object(test_any_type),
                "TestAnyArray": serialize.map(
                    test_any_array, lambda e: serialize.object(e)
                ),
                "Permissions": serialize.map(permissions, lambda e: e),
                "SomeA2PThing": some_a2p_thing,
            }
        )

        payload = self._version.create(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return NewCredentialsInstance(self._version, payload)

    async def create_async(
        self,
        test_string,
        test_boolean=values.unset,
        test_integer=values.unset,
        test_number=values.unset,
        test_number_float=values.unset,
        test_number_double=values.unset,
        test_number_int32=values.unset,
        test_number_int64=values.unset,
        test_object=values.unset,
        test_date_time=values.unset,
        test_date=values.unset,
        test_enum=values.unset,
        test_object_array=values.unset,
        test_any_type=values.unset,
        test_any_array=values.unset,
        permissions=values.unset,
        some_a2p_thing=values.unset,
    ) -> NewCredentialsInstance:
        """
        Asynchronously create the NewCredentialsInstance

        :param str test_string:
        :param bool test_boolean:
        :param int test_integer:
        :param float test_number:
        :param float test_number_float:
        :param float test_number_double:
        :param float test_number_int32:
        :param int test_number_int64:
        :param Dict[str, object] test_object:
        :param datetime test_date_time:
        :param date test_date:
        :param &quot;NewCredentialsInstance.Status&quot; test_enum:
        :param List[object] test_object_array:
        :param object test_any_type:
        :param List[object] test_any_array:
        :param List[str] permissions: A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`.
        :param str some_a2p_thing:

        :returns: The created NewCredentialsInstance
        """
        data = values.of(
            {
                "TestString": test_string,
                "TestBoolean": test_boolean,
                "TestInteger": test_integer,
                "TestNumber": test_number,
                "TestNumberFloat": test_number_float,
                "TestNumberDouble": test_number_double,
                "TestNumberInt32": test_number_int32,
                "TestNumberInt64": test_number_int64,
                "TestObject": serialize.object(test_object),
                "TestDateTime": serialize.iso8601_datetime(test_date_time),
                "TestDate": serialize.iso8601_date(test_date),
                "TestEnum": test_enum,
                "TestObjectArray": serialize.map(
                    test_object_array, lambda e: serialize.object(e)
                ),
                "TestAnyType": serialize.object(test_any_type),
                "TestAnyArray": serialize.map(
                    test_any_array, lambda e: serialize.object(e)
                ),
                "Permissions": serialize.map(permissions, lambda e: e),
                "SomeA2PThing": some_a2p_thing,
            }
        )

        payload = await self._version.create_async(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return NewCredentialsInstance(self._version, payload)

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.FlexApi.V1.NewCredentialsList>"
