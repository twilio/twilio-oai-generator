r"""
    This code was generated by
   ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
    |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
    |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    Twilio - Versionless
    No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)

    NOTE: This class is auto generated by OpenAPI Generator.
    https://openapi-generator.tech
    Do not edit the class manually.
"""


from typing import Optional
from twilio.base import values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version


class FleetInstance(InstanceResource):
    def __init__(self, version, payload, sid: Optional[str] = None):
        """
        Initialize the FleetInstance
        """
        super().__init__(version)

        self._name: Optional[str] = payload.get("name")
        self._sid: Optional[str] = payload.get("sid")
        self._friendly_name: Optional[str] = payload.get("friendly_name")

        self._solution = {
            "sid": sid or self._sid,
        }
        self._context: Optional[FleetContext] = None

    @property
    def _proxy(self) -> "FleetContext":
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: FleetContext for this FleetInstance
        """
        if self._context is None:
            self._context = FleetContext(
                self._version,
                sid=self._solution["sid"],
            )
        return self._context

    @property
    def name(self) -> Optional[str]:
        return self._name

    @property
    def sid(self) -> Optional[str]:
        """
        :returns: A string that uniquely identifies this Fleet.
        """
        return self._sid

    @property
    def friendly_name(self) -> Optional[str]:
        """
        :returns: A human readable description for this Fleet.
        """
        return self._friendly_name

    def fetch(self) -> "FleetInstance":
        """
        Fetch the FleetInstance


        :returns: The fetched FleetInstance
        """
        return self._proxy.fetch()

    async def fetch_async(self) -> "FleetInstance":
        """
        Asynchronous coroutine to fetch the FleetInstance


        :returns: The fetched FleetInstance
        """
        return await self._proxy.fetch_async()

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Versionless.DeployedDevices.FleetInstance {}>".format(context)


class FleetContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        """
        Initialize the FleetContext

        :param version: Version that contains the resource
        :param sid:
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "sid": sid,
        }
        self._uri = "/Fleets/{sid}".format(**self._solution)

    def fetch(self) -> FleetInstance:
        """
        Fetch the FleetInstance


        :returns: The fetched FleetInstance
        """

        payload = self._version.fetch(
            method="GET",
            uri=self._uri,
        )

        return FleetInstance(
            self._version,
            payload,
            sid=self._solution["sid"],
        )

    async def fetch_async(self) -> FleetInstance:
        """
        Asynchronous coroutine to fetch the FleetInstance


        :returns: The fetched FleetInstance
        """

        payload = await self._version.fetch_async(
            method="GET",
            uri=self._uri,
        )

        return FleetInstance(
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
        return "<Twilio.Versionless.DeployedDevices.FleetContext {}>".format(context)


class FleetList(ListResource):
    def __init__(self, version: Version):
        """
        Initialize the FleetList

        :param version: Version that contains the resource

        """
        super().__init__(version)

        self._uri = "/Fleets"

    def create(self, name=values.unset) -> FleetInstance:
        """
        Create the FleetInstance

        :param str name:

        :returns: The created FleetInstance
        """
        data = values.of(
            {
                "Name": name,
            }
        )

        payload = self._version.create(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return FleetInstance(self._version, payload)

    async def create_async(self, name=values.unset) -> FleetInstance:
        """
        Asynchronously create the FleetInstance

        :param str name:

        :returns: The created FleetInstance
        """
        data = values.of(
            {
                "Name": name,
            }
        )

        payload = await self._version.create_async(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return FleetInstance(self._version, payload)

    def get(self, sid) -> FleetContext:
        """
        Constructs a FleetContext

        :param sid:
        """
        return FleetContext(self._version, sid=sid)

    def __call__(self, sid) -> FleetContext:
        """
        Constructs a FleetContext

        :param sid:
        """
        return FleetContext(self._version, sid=sid)

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.Versionless.DeployedDevices.FleetList>"
