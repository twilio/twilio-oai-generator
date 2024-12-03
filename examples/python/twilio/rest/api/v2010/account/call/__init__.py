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


from datetime import datetime
from decimal import Decimal
from typing import Any, Dict, List, Optional, Union
from twilio.base import deserialize, serialize, values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version

from twilio.rest.api.v2010.account.call.feedback_call_summary import (
    FeedbackCallSummaryList,
)


class CallInstance(InstanceResource):
    class Status(object):
        IN_PROGRESS = "in-progress"
        PAUSED = "paused"
        STOPPED = "stopped"
        PROCESSING = "processing"
        COMPLETED = "completed"
        ABSENT = "absent"

    """
    :ivar account_sid: 
    :ivar sid: 
    :ivar test_string: 
    :ivar test_integer: 
    :ivar test_object: 
    :ivar test_date_time: 
    :ivar test_number: 
    :ivar from_: 
    :ivar price_unit: 
    :ivar test_number_float: 
    :ivar test_number_decimal: 
    :ivar test_enum: 
    :ivar a2p_profile_bundle_sid: A2P Messaging Profile Bundle BundleSid
    :ivar test_array_of_integers: 
    :ivar test_array_of_array_of_integers: 
    :ivar test_array_of_objects: 
    :ivar test_array_of_enum: Permissions authorized to the app
    """

    def __init__(
        self,
        version: Version,
        payload: Dict[str, Any],
        account_sid: str,
        test_integer: Optional[int] = None,
    ):
        super().__init__(version)

        self.account_sid: Optional[str] = payload.get("account_sid")
        self.sid: Optional[str] = payload.get("sid")
        self.test_string: Optional[str] = payload.get("test_string")
        self.test_integer: Optional[int] = deserialize.integer(
            payload.get("test_integer")
        )
        self.test_object: Optional[str] = payload.get("test_object")
        self.test_date_time: Optional[datetime] = deserialize.rfc2822_datetime(
            payload.get("test_date_time")
        )
        self.test_number: Optional[float] = deserialize.decimal(
            payload.get("test_number")
        )
        self.from_: Optional[str] = payload.get("from")
        self.price_unit: Optional[str] = payload.get("price_unit")
        self.test_number_float: Optional[float] = payload.get("test_number_float")
        self.test_number_decimal: Optional[Decimal] = payload.get("test_number_decimal")
        self.test_enum: Optional["CallInstance.Status"] = payload.get("test_enum")
        self.a2p_profile_bundle_sid: Optional[str] = payload.get(
            "a2p_profile_bundle_sid"
        )
        self.test_array_of_integers: Optional[List[int]] = payload.get(
            "test_array_of_integers"
        )
        self.test_array_of_array_of_integers: Optional[List[List[int]]] = payload.get(
            "test_array_of_array_of_integers"
        )
        self.test_array_of_objects: Optional[List[str]] = payload.get(
            "test_array_of_objects"
        )
        self.test_array_of_enum: Optional[List["CallInstance.Status"]] = payload.get(
            "test_array_of_enum"
        )

        self._solution = {
            "account_sid": account_sid,
            "test_integer": test_integer or self.test_integer,
        }
        self._context: Optional[CallContext] = None

    @property
    def _proxy(self) -> "CallContext":
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: CallContext for this CallInstance
        """
        if self._context is None:
            self._context = CallContext(
                self._version,
                account_sid=self._solution["account_sid"],
                test_integer=self._solution["test_integer"],
            )
        return self._context

    def delete(self) -> bool:
        """
        Deletes the CallInstance


        :returns: True if delete succeeds, False otherwise
        """
        return self._proxy.delete()

    async def delete_async(self) -> bool:
        """
        Asynchronous coroutine that deletes the CallInstance


        :returns: True if delete succeeds, False otherwise
        """
        return await self._proxy.delete_async()

    def fetch(self) -> "CallInstance":
        """
        Fetch the CallInstance


        :returns: The fetched CallInstance
        """
        return self._proxy.fetch()

    async def fetch_async(self) -> "CallInstance":
        """
        Asynchronous coroutine to fetch the CallInstance


        :returns: The fetched CallInstance
        """
        return await self._proxy.fetch_async()

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.CallInstance {}>".format(context)


class CallContext(InstanceContext):
    def __init__(self, version: Version, account_sid: str, test_integer: int):
        """
        Initialize the CallContext

        :param version: Version that contains the resource
        :param account_sid:
        :param test_integer: INTEGER ID param!!!
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "account_sid": account_sid,
            "test_integer": test_integer,
        }
        self._uri = "/Accounts/{account_sid}/Calls/{test_integer}.json".format(
            **self._solution
        )

    def delete(self) -> bool:
        """
        Deletes the CallInstance


        :returns: True if delete succeeds, False otherwise
        """
        return self._version.delete(
            method="DELETE",
            uri=self._uri,
        )

    async def delete_async(self) -> bool:
        """
        Asynchronous coroutine that deletes the CallInstance


        :returns: True if delete succeeds, False otherwise
        """
        return await self._version.delete_async(
            method="DELETE",
            uri=self._uri,
        )

    def fetch(self) -> CallInstance:
        """
        Fetch the CallInstance


        :returns: The fetched CallInstance
        """

        payload = self._version.fetch(
            method="GET",
            uri=self._uri,
        )

        return CallInstance(
            self._version,
            payload,
            account_sid=self._solution["account_sid"],
            test_integer=self._solution["test_integer"],
        )

    async def fetch_async(self) -> CallInstance:
        """
        Asynchronous coroutine to fetch the CallInstance


        :returns: The fetched CallInstance
        """

        payload = await self._version.fetch_async(
            method="GET",
            uri=self._uri,
        )

        return CallInstance(
            self._version,
            payload,
            account_sid=self._solution["account_sid"],
            test_integer=self._solution["test_integer"],
        )

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.CallContext {}>".format(context)


class CallList(ListResource):
    def __init__(self, version: Version, account_sid: str):
        """
        Initialize the CallList

        :param version: Version that contains the resource
        :param account_sid:

        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "account_sid": account_sid,
        }
        self._uri = "/Accounts/{account_sid}/Calls.json".format(**self._solution)

        self._feedback_call_summary: Optional[FeedbackCallSummaryList] = None

    def create(
        self,
        required_string_property: str,
        test_method: str,
        test_array_of_strings: Union[List[str], object] = values.unset,
        test_array_of_uri: Union[List[str], object] = values.unset,
    ) -> CallInstance:
        """
        Create the CallInstance

        :param required_string_property:
        :param test_method: The HTTP method that we should use to request the `TestArrayOfUri`.
        :param test_array_of_strings:
        :param test_array_of_uri:

        :returns: The created CallInstance
        """

        data = values.of(
            {
                "RequiredStringProperty": required_string_property,
                "TestMethod": test_method,
                "TestArrayOfStrings": serialize.map(test_array_of_strings, lambda e: e),
                "TestArrayOfUri": serialize.map(test_array_of_uri, lambda e: e),
            }
        )
        headers = values.of({"Content-Type": "application/x-www-form-urlencoded"})

        headers["Content-Type"] = "application/x-www-form-urlencoded"

        headers["Accept"] = "application/json"

        payload = self._version.create(
            method="POST", uri=self._uri, data=data, headers=headers
        )

        return CallInstance(
            self._version, payload, account_sid=self._solution["account_sid"]
        )

    async def create_async(
        self,
        required_string_property: str,
        test_method: str,
        test_array_of_strings: Union[List[str], object] = values.unset,
        test_array_of_uri: Union[List[str], object] = values.unset,
    ) -> CallInstance:
        """
        Asynchronously create the CallInstance

        :param required_string_property:
        :param test_method: The HTTP method that we should use to request the `TestArrayOfUri`.
        :param test_array_of_strings:
        :param test_array_of_uri:

        :returns: The created CallInstance
        """

        data = values.of(
            {
                "RequiredStringProperty": required_string_property,
                "TestMethod": test_method,
                "TestArrayOfStrings": serialize.map(test_array_of_strings, lambda e: e),
                "TestArrayOfUri": serialize.map(test_array_of_uri, lambda e: e),
            }
        )
        headers = values.of({"Content-Type": "application/x-www-form-urlencoded"})

        headers["Content-Type"] = "application/x-www-form-urlencoded"

        headers["Accept"] = "application/json"

        payload = await self._version.create_async(
            method="POST", uri=self._uri, data=data, headers=headers
        )

        return CallInstance(
            self._version, payload, account_sid=self._solution["account_sid"]
        )

    @property
    def feedback_call_summary(self) -> FeedbackCallSummaryList:
        """
        Access the feedback_call_summary
        """
        if self._feedback_call_summary is None:
            self._feedback_call_summary = FeedbackCallSummaryList(
                self._version, account_sid=self._solution["account_sid"]
            )
        return self._feedback_call_summary

    def get(self, test_integer: int) -> CallContext:
        """
        Constructs a CallContext

        :param test_integer: INTEGER ID param!!!
        """
        return CallContext(
            self._version,
            account_sid=self._solution["account_sid"],
            test_integer=test_integer,
        )

    def __call__(self, test_integer: int) -> CallContext:
        """
        Constructs a CallContext

        :param test_integer: INTEGER ID param!!!
        """
        return CallContext(
            self._version,
            account_sid=self._solution["account_sid"],
            test_integer=test_integer,
        )

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.Api.V2010.CallList>"
