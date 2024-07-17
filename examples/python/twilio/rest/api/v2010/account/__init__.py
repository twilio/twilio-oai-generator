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


from datetime import date, datetime
from decimal import Decimal
from typing import Any, Dict, List, Optional, Union, Iterator, AsyncIterator
from twilio.base import deserialize, serialize, values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version
from twilio.base.page import Page
from twilio.rest.api.v2010.account.call import CallList


class AccountInstance(InstanceResource):
    class XTwilioWebhookEnabled(object):
        TRUE = "true"
        FALSE = "false"

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
        self, version: Version, payload: Dict[str, Any], sid: Optional[str] = None
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
        self.test_enum: Optional["AccountInstance.Status"] = payload.get("test_enum")
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
        self.test_array_of_enum: Optional[List["AccountInstance.Status"]] = payload.get(
            "test_array_of_enum"
        )

        self._solution = {
            "sid": sid or self.sid,
        }
        self._context: Optional[AccountContext] = None

    @property
    def _proxy(self) -> "AccountContext":
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: AccountContext for this AccountInstance
        """
        if self._context is None:
            self._context = AccountContext(
                self._version,
                sid=self._solution["sid"],
            )
        return self._context

    def delete(self) -> bool:
        """
        Deletes the AccountInstance


        :returns: True if delete succeeds, False otherwise
        """
        return self._proxy.delete()

    async def delete_async(self) -> bool:
        """
        Asynchronous coroutine that deletes the AccountInstance


        :returns: True if delete succeeds, False otherwise
        """
        return await self._proxy.delete_async()

    def fetch(self) -> "AccountInstance":
        """
        Fetch the AccountInstance


        :returns: The fetched AccountInstance
        """
        return self._proxy.fetch()

    async def fetch_async(self) -> "AccountInstance":
        """
        Asynchronous coroutine to fetch the AccountInstance


        :returns: The fetched AccountInstance
        """
        return await self._proxy.fetch_async()

    def update(
        self,
        status: "AccountInstance.Status",
        pause_behavior: Union[str, object] = values.unset,
    ) -> "AccountInstance":
        """
        Update the AccountInstance

        :param status:
        :param pause_behavior:

        :returns: The updated AccountInstance
        """
        return self._proxy.update(
            status=status,
            pause_behavior=pause_behavior,
        )

    async def update_async(
        self,
        status: "AccountInstance.Status",
        pause_behavior: Union[str, object] = values.unset,
    ) -> "AccountInstance":
        """
        Asynchronous coroutine to update the AccountInstance

        :param status:
        :param pause_behavior:

        :returns: The updated AccountInstance
        """
        return await self._proxy.update_async(
            status=status,
            pause_behavior=pause_behavior,
        )

    @property
    def calls(self) -> CallList:
        """
        Access the calls
        """
        return self._proxy.calls

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.AccountInstance {}>".format(context)


class AccountContext(InstanceContext):
    def __init__(self, version: Version, sid: str):
        """
        Initialize the AccountContext

        :param version: Version that contains the resource
        :param sid:
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "sid": sid,
        }
        self._uri = "/Accounts/{sid}.json".format(**self._solution)

        self._calls: Optional[CallList] = None

    def delete(self) -> bool:
        """
        Deletes the AccountInstance


        :returns: True if delete succeeds, False otherwise
        """
        return self._version.delete(
            method="DELETE",
            uri=self._uri
        )

    async def delete_async(self) -> bool:
        """
        Asynchronous coroutine that deletes the AccountInstance


        :returns: True if delete succeeds, False otherwise
        """
        return await self._version.delete_async(
            method="DELETE",
            uri=self._uri
        )

    def fetch(self) -> AccountInstance:
        """
        Fetch the AccountInstance


        :returns: The fetched AccountInstance
        """

        payload = self._version.fetch(
            method="GET",
            uri=self._uri
        )

        return AccountInstance(
            self._version,
            payload,
            sid=self._solution["sid"],
        )

    async def fetch_async(self) -> AccountInstance:
        """
        Asynchronous coroutine to fetch the AccountInstance


        :returns: The fetched AccountInstance
        """

        payload = await self._version.fetch_async(
            method="GET",
            uri=self._uri
        )

        return AccountInstance(
            self._version,
            payload,
            sid=self._solution["sid"],
        )

    def update(
        self,
        status: "AccountInstance.Status",
        pause_behavior: Union[str, object] = values.unset,
    ) -> AccountInstance:
        """
        Update the AccountInstance

        :param status:
        :param pause_behavior:

        :returns: The updated AccountInstance
        """
        data = values.of(
            {
                "Status": status,
                "PauseBehavior": pause_behavior,
            }
        )

        payload = self._version.update(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return AccountInstance(self._version, payload, sid=self._solution["sid"])

    async def update_async(
        self,
        status: "AccountInstance.Status",
        pause_behavior: Union[str, object] = values.unset,
    ) -> AccountInstance:
        """
        Asynchronous coroutine to update the AccountInstance

        :param status:
        :param pause_behavior:

        :returns: The updated AccountInstance
        """
        data = values.of(
            {
                "Status": status,
                "PauseBehavior": pause_behavior,
            }
        )

        payload = await self._version.update_async(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return AccountInstance(self._version, payload, sid=self._solution["sid"])

    @property
    def calls(self) -> CallList:
        """
        Access the calls
        """
        if self._calls is None:
            self._calls = CallList(
                self._version,
                self._solution["sid"],
            )
        return self._calls

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.AccountContext {}>".format(context)


class AccountPage(Page):
    def get_instance(self, payload: Dict[str, Any]) -> AccountInstance:
        """
        Build an instance of AccountInstance

        :param payload: Payload response from the API
        """
        return AccountInstance(self._version, payload)

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.Api.V2010.AccountPage>"


class AccountList(ListResource):
    def __init__(self, version: Version):
        """
        Initialize the AccountList

        :param version: Version that contains the resource

        """
        super().__init__(version)

        self._uri = "/Accounts.json"

    def create(
        self,
        x_twilio_webhook_enabled: Union[
            "AccountInstance.XTwilioWebhookEnabled", object
        ] = values.unset,
        recording_status_callback: Union[str, object] = values.unset,
        recording_status_callback_event: Union[List[str], object] = values.unset,
        twiml: Union[str, object] = values.unset,
    ) -> AccountInstance:
        """
        Create the AccountInstance

        :param x_twilio_webhook_enabled:
        :param recording_status_callback:
        :param recording_status_callback_event:
        :param twiml:

        :returns: The created AccountInstance
        """

        data = values.of(
            {
                "RecordingStatusCallback": recording_status_callback,
                "RecordingStatusCallbackEvent": serialize.map(
                    recording_status_callback_event, lambda e: e
                ),
                "Twiml": twiml,
            }
        )
        headers = values.of(
            {
                "X-Twilio-Webhook-Enabled": x_twilio_webhook_enabled,
                "Content-Type": "application/x-www-form-urlencoded",
            }
        )

        payload = self._version.create(
            method="POST", uri=self._uri, data=data, headers=headers
        )

        return AccountInstance(self._version, payload)

    async def create_async(
        self,
        x_twilio_webhook_enabled: Union[
            "AccountInstance.XTwilioWebhookEnabled", object
        ] = values.unset,
        recording_status_callback: Union[str, object] = values.unset,
        recording_status_callback_event: Union[List[str], object] = values.unset,
        twiml: Union[str, object] = values.unset,
    ) -> AccountInstance:
        """
        Asynchronously create the AccountInstance

        :param x_twilio_webhook_enabled:
        :param recording_status_callback:
        :param recording_status_callback_event:
        :param twiml:

        :returns: The created AccountInstance
        """

        data = values.of(
            {
                "RecordingStatusCallback": recording_status_callback,
                "RecordingStatusCallbackEvent": serialize.map(
                    recording_status_callback_event, lambda e: e
                ),
                "Twiml": twiml,
            }
        )
        headers = values.of(
            {
                "X-Twilio-Webhook-Enabled": x_twilio_webhook_enabled,
                "Content-Type": "application/x-www-form-urlencoded",
            }
        )

        payload = await self._version.create_async(
            method="POST", uri=self._uri, data=data, headers=headers
        )

        return AccountInstance(self._version, payload)

    def stream(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        limit: Optional[int] = None,
        page_size: Optional[int] = None,
    ) -> Iterator[AccountInstance]:
        """
        Streams AccountInstance records from the API as a generator stream.
        This operation lazily loads records as efficiently as possible until the limit
        is reached.
        The results are returned as a generator, so this operation is memory efficient.

        :param datetime date_created:
        :param date date_test:
        :param datetime date_created_before:
        :param datetime date_created_after:
        :param limit: Upper limit for the number of records to return. stream()
                      guarantees to never return more than limit.  Default is no limit
        :param page_size: Number of records to fetch per request, when not set will use
                          the default value of 50 records.  If no page_size is defined
                          but a limit is defined, stream() will attempt to read the
                          limit with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        """
        limits = self._version.read_limits(limit, page_size)
        page = self.page(
            date_created=date_created,
            date_test=date_test,
            date_created_before=date_created_before,
            date_created_after=date_created_after,
            page_size=limits["page_size"],
        )

        return self._version.stream(page, limits["limit"])

    async def stream_async(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        limit: Optional[int] = None,
        page_size: Optional[int] = None,
    ) -> AsyncIterator[AccountInstance]:
        """
        Asynchronously streams AccountInstance records from the API as a generator stream.
        This operation lazily loads records as efficiently as possible until the limit
        is reached.
        The results are returned as a generator, so this operation is memory efficient.

        :param datetime date_created:
        :param date date_test:
        :param datetime date_created_before:
        :param datetime date_created_after:
        :param limit: Upper limit for the number of records to return. stream()
                      guarantees to never return more than limit.  Default is no limit
        :param page_size: Number of records to fetch per request, when not set will use
                          the default value of 50 records.  If no page_size is defined
                          but a limit is defined, stream() will attempt to read the
                          limit with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        """
        limits = self._version.read_limits(limit, page_size)
        page = await self.page_async(
            date_created=date_created,
            date_test=date_test,
            date_created_before=date_created_before,
            date_created_after=date_created_after,
            page_size=limits["page_size"],
        )

        return self._version.stream_async(page, limits["limit"])

    def list(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        limit: Optional[int] = None,
        page_size: Optional[int] = None,
    ) -> List[AccountInstance]:
        """
        Lists AccountInstance records from the API as a list.
        Unlike stream(), this operation is eager and will load `limit` records into
        memory before returning.

        :param datetime date_created:
        :param date date_test:
        :param datetime date_created_before:
        :param datetime date_created_after:
        :param limit: Upper limit for the number of records to return. list() guarantees
                      never to return more than limit.  Default is no limit
        :param page_size: Number of records to fetch per request, when not set will use
                          the default value of 50 records.  If no page_size is defined
                          but a limit is defined, list() will attempt to read the limit
                          with the most efficient page size, i.e. min(limit, 1000)

        :returns: list that will contain up to limit results
        """
        return list(
            self.stream(
                date_created=date_created,
                date_test=date_test,
                date_created_before=date_created_before,
                date_created_after=date_created_after,
                limit=limit,
                page_size=page_size,
            )
        )

    async def list_async(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        limit: Optional[int] = None,
        page_size: Optional[int] = None,
    ) -> List[AccountInstance]:
        """
        Asynchronously lists AccountInstance records from the API as a list.
        Unlike stream(), this operation is eager and will load `limit` records into
        memory before returning.

        :param datetime date_created:
        :param date date_test:
        :param datetime date_created_before:
        :param datetime date_created_after:
        :param limit: Upper limit for the number of records to return. list() guarantees
                      never to return more than limit.  Default is no limit
        :param page_size: Number of records to fetch per request, when not set will use
                          the default value of 50 records.  If no page_size is defined
                          but a limit is defined, list() will attempt to read the limit
                          with the most efficient page size, i.e. min(limit, 1000)

        :returns: list that will contain up to limit results
        """
        return [
            record
            async for record in await self.stream_async(
                date_created=date_created,
                date_test=date_test,
                date_created_before=date_created_before,
                date_created_after=date_created_after,
                limit=limit,
                page_size=page_size,
            )
        ]

    def page(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        page_token: Union[str, object] = values.unset,
        page_number: Union[int, object] = values.unset,
        page_size: Union[int, object] = values.unset,
    ) -> AccountPage:
        """
        Retrieve a single page of AccountInstance records from the API.
        Request is executed immediately

        :param date_created:
        :param date_test:
        :param date_created_before:
        :param date_created_after:
        :param page_token: PageToken provided by the API
        :param page_number: Page Number, this value is simply for client state
        :param page_size: Number of records to return, defaults to 50

        :returns: Page of AccountInstance
        """
        data = values.of(
            {
                "DateCreated": serialize.iso8601_datetime(date_created),
                "Date.Test": serialize.iso8601_date(date_test),
                "DateCreated<": serialize.iso8601_datetime(date_created_before),
                "DateCreated>": serialize.iso8601_datetime(date_created_after),
                "PageToken": page_token,
                "Page": page_number,
                "PageSize": page_size,
            }
        )

        response = self._version.page(method="GET", uri=self._uri, params=data)
        return AccountPage(self._version, response)

    async def page_async(
        self,
        date_created: Union[datetime, object] = values.unset,
        date_test: Union[date, object] = values.unset,
        date_created_before: Union[datetime, object] = values.unset,
        date_created_after: Union[datetime, object] = values.unset,
        page_token: Union[str, object] = values.unset,
        page_number: Union[int, object] = values.unset,
        page_size: Union[int, object] = values.unset,
    ) -> AccountPage:
        """
        Asynchronously retrieve a single page of AccountInstance records from the API.
        Request is executed immediately

        :param date_created:
        :param date_test:
        :param date_created_before:
        :param date_created_after:
        :param page_token: PageToken provided by the API
        :param page_number: Page Number, this value is simply for client state
        :param page_size: Number of records to return, defaults to 50

        :returns: Page of AccountInstance
        """
        data = values.of(
            {
                "DateCreated": serialize.iso8601_datetime(date_created),
                "Date.Test": serialize.iso8601_date(date_test),
                "DateCreated<": serialize.iso8601_datetime(date_created_before),
                "DateCreated>": serialize.iso8601_datetime(date_created_after),
                "PageToken": page_token,
                "Page": page_number,
                "PageSize": page_size,
            }
        )

        response = await self._version.page_async(
            method="GET", uri=self._uri, params=data
        )
        return AccountPage(self._version, response)

    def get_page(self, target_url: str) -> AccountPage:
        """
        Retrieve a specific page of AccountInstance records from the API.
        Request is executed immediately

        :param target_url: API-generated URL for the requested results page

        :returns: Page of AccountInstance
        """
        response = self._version.domain.twilio.request("GET", target_url)
        return AccountPage(self._version, response)

    async def get_page_async(self, target_url: str) -> AccountPage:
        """
        Asynchronously retrieve a specific page of AccountInstance records from the API.
        Request is executed immediately

        :param target_url: API-generated URL for the requested results page

        :returns: Page of AccountInstance
        """
        response = await self._version.domain.twilio.request_async("GET", target_url)
        return AccountPage(self._version, response)

    def get(self, sid: str) -> AccountContext:
        """
        Constructs a AccountContext

        :param sid:
        """
        return AccountContext(self._version, sid=sid)

    def __call__(self, sid: str) -> AccountContext:
        """
        Constructs a AccountContext

        :param sid:
        """
        return AccountContext(self._version, sid=sid)

    def __repr__(self) -> str:
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        """
        return "<Twilio.Api.V2010.AccountList>"
