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
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version


class HistoryInstance(InstanceResource):
    def __init__(self, version, payload, sid: str):
        """
        Initialize the HistoryInstance
        """
        super().__init__(version)

        self._properties = {
            "account_sid": payload.get("account_sid"),
            "sid": payload.get("sid"),
            "test_string": payload.get("test_string"),
            "test_integer": deserialize.integer(payload.get("test_integer")),
        }

        self._solution = {
            "sid": sid,
        }
        self._context: Optional[HistoryContext] = None

    @property
    def _proxy(self) -> "HistoryContext":
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: HistoryContext for this HistoryInstance
        """
        if self._context is None:
            self._context = HistoryContext(
                self._version,
                sid=self._solution["sid"],
            )
        return self._context

    @property
    def account_sid(self) -> str:
        """
        :returns:
        """
        return self._properties["account_sid"]

    @property
    def sid(self) -> str:
        """
        :returns:
        """
        return self._properties["sid"]

    @property
    def test_string(self) -> str:
        """
        :returns:
        """
        return self._properties["test_string"]

    @property
    def test_integer(self) -> int:
        """
        :returns:
        """
        return self._properties["test_integer"]

    def fetch(self, add_ons_data=values.unset) -> "HistoryInstance":
        """
        Fetch the HistoryInstance

        :param Dict[str, object] add_ons_data:

        :returns: The fetched HistoryInstance
        """
        return self._proxy.fetch(
            add_ons_data=add_ons_data,
        )

    async def fetch_async(self, add_ons_data=values.unset) -> "HistoryInstance":
        """
        Asynchronous coroutine to fetch the HistoryInstance

        :param Dict[str, object] add_ons_data:

        :returns: The fetched HistoryInstance
        """
        return await self._proxy.fetch_async(
            add_ons_data=add_ons_data,
        )

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.FlexApi.V1.HistoryInstance {}>".format(context)


class HistoryContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        """
        Initialize the HistoryContext

        :param version: Version that contains the resource
        :param sid:
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "sid": sid,
        }
        self._uri = "/Credentials/AWS/{sid}/History".format(**self._solution)

    def fetch(self, add_ons_data=values.unset) -> HistoryInstance:
        """
        Fetch the HistoryInstance

        :param Dict[str, object] add_ons_data:

        :returns: The fetched HistoryInstance
        """

        data = values.of({})
        data.update(serialize.prefixed_collapsible_map(add_ons_data, "AddOns"))

        payload = self._version.fetch(method="GET", uri=self._uri, params=data)

        return HistoryInstance(
            self._version,
            payload,
            sid=self._solution["sid"],
        )

    async def fetch_async(self, add_ons_data=values.unset) -> HistoryInstance:
        """
        Asynchronous coroutine to fetch the HistoryInstance

        :param Dict[str, object] add_ons_data:

        :returns: The fetched HistoryInstance
        """

        data = values.of({})
        data.update(serialize.prefixed_collapsible_map(add_ons_data, "AddOns"))

        payload = await self._version.fetch_async(
            method="GET", uri=self._uri, params=data
        )

        return HistoryInstance(
            self._version,
            payload,
            sid=self._solution["sid"],
        )

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.FlexApi.V1.HistoryContext {}>".format(context)


class HistoryList(ListResource):
    def __init__(self, version: Version, sid: str):
        """
        Initialize the HistoryList

        :param version: Version that contains the resource
        :param sid:

        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "sid": sid,
        }

    def get(self) -> HistoryContext:
        """
        Constructs a HistoryContext

        """
        return HistoryContext(self._version, sid=self._solution["sid"])

    def __call__(self) -> HistoryContext:
        """
        Constructs a HistoryContext

        """
        return HistoryContext(self._version, sid=self._solution["sid"])

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.FlexApi.V1.HistoryList>"
