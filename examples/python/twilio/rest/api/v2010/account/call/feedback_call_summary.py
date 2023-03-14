"""
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


from datetime import date
from twilio.base import deserialize
from twilio.base import serialize
from twilio.base import values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.version import Version


class FeedbackCallSummaryList(ListResource):
    def __init__(self, version: Version, account_sid: str):
        """
        Initialize the FeedbackCallSummaryList

        :param Version version: Version that contains the resource
        :param account_sid:

        :returns: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryList
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryList
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "account_sid": account_sid,
        }

    def get(self, sid):
        """
        Constructs a FeedbackCallSummaryContext

        :param sid:

        :returns: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        """
        return FeedbackCallSummaryContext(
            self._version, account_sid=self._solution["account_sid"], sid=sid
        )

    def __call__(self, sid):
        """
        Constructs a FeedbackCallSummaryContext

        :param sid:

        :returns: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        """
        return FeedbackCallSummaryContext(
            self._version, account_sid=self._solution["account_sid"], sid=sid
        )

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        return "<Twilio.Api.V2010.FeedbackCallSummaryList>"


class FeedbackCallSummaryInstance(InstanceResource):
    class Status(object):
        IN_PROGRESS = "in-progress"
        PAUSED = "paused"
        STOPPED = "stopped"
        PROCESSING = "processing"
        COMPLETED = "completed"
        ABSENT = "absent"

    def __init__(self, version, payload, account_sid: str, sid: str = None):
        """
        Initialize the FeedbackCallSummaryInstance

        :returns: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryInstance
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryInstance
        """
        super().__init__(version)

        self._properties = {
            "account_sid": payload.get("account_sid"),
            "sid": payload.get("sid"),
            "test_string": payload.get("test_string"),
            "test_integer": deserialize.integer(payload.get("test_integer")),
            "test_object": payload.get("test_object"),
            "test_date_time": deserialize.rfc2822_datetime(
                payload.get("test_date_time")
            ),
            "test_number": deserialize.decimal(payload.get("test_number")),
            "price_unit": payload.get("price_unit"),
            "test_number_float": payload.get("test_number_float"),
            "test_number_decimal": payload.get("test_number_decimal"),
            "test_enum": payload.get("test_enum"),
            "a2p_profile_bundle_sid": payload.get("a2p_profile_bundle_sid"),
            "test_array_of_integers": payload.get("test_array_of_integers"),
            "test_array_of_array_of_integers": payload.get(
                "test_array_of_array_of_integers"
            ),
            "test_array_of_objects": payload.get("test_array_of_objects"),
            "test_array_of_enum": payload.get("test_array_of_enum"),
        }

        self._context = None
        self._solution = {
            "account_sid": account_sid,
            "sid": sid or self._properties["sid"],
        }

    @property
    def _proxy(self):
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions. All instance actions are proxied to the context

        :returns: FeedbackCallSummaryContext for this FeedbackCallSummaryInstance
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        """
        if self._context is None:
            self._context = FeedbackCallSummaryContext(
                self._version,
                account_sid=self._solution["account_sid"],
                sid=self._solution["sid"],
            )
        return self._context

    @property
    def account_sid(self):
        """
        :returns:
        :rtype: str
        """
        return self._properties["account_sid"]

    @property
    def sid(self):
        """
        :returns:
        :rtype: str
        """
        return self._properties["sid"]

    @property
    def test_string(self):
        """
        :returns:
        :rtype: str
        """
        return self._properties["test_string"]

    @property
    def test_integer(self):
        """
        :returns:
        :rtype: int
        """
        return self._properties["test_integer"]

    @property
    def test_object(self):
        """
        :returns:
        :rtype: TestResponseObjectTestObject
        """
        return self._properties["test_object"]

    @property
    def test_date_time(self):
        """
        :returns:
        :rtype: datetime
        """
        return self._properties["test_date_time"]

    @property
    def test_number(self):
        """
        :returns:
        :rtype: float
        """
        return self._properties["test_number"]

    @property
    def price_unit(self):
        """
        :returns:
        :rtype: str
        """
        return self._properties["price_unit"]

    @property
    def test_number_float(self):
        """
        :returns:
        :rtype: float
        """
        return self._properties["test_number_float"]

    @property
    def test_number_decimal(self):
        """
        :returns:
        :rtype: Decimal
        """
        return self._properties["test_number_decimal"]

    @property
    def test_enum(self):
        """
        :returns:
        :rtype: FeedbackCallSummaryInstance.Status
        """
        return self._properties["test_enum"]

    @property
    def a2p_profile_bundle_sid(self):
        """
        :returns: A2P Messaging Profile Bundle BundleSid
        :rtype: str
        """
        return self._properties["a2p_profile_bundle_sid"]

    @property
    def test_array_of_integers(self):
        """
        :returns:
        :rtype: list[int]
        """
        return self._properties["test_array_of_integers"]

    @property
    def test_array_of_array_of_integers(self):
        """
        :returns:
        :rtype: list[list[int]]
        """
        return self._properties["test_array_of_array_of_integers"]

    @property
    def test_array_of_objects(self):
        """
        :returns:
        :rtype: list[TestResponseObjectTestArrayOfObjects]
        """
        return self._properties["test_array_of_objects"]

    @property
    def test_array_of_enum(self):
        """
        :returns: Permissions authorized to the app
        :rtype: list[FeedbackCallSummaryInstance.Status]
        """
        return self._properties["test_array_of_enum"]

    def update(self, end_date, start_date, account_sid=values.unset):
        """
        Update the FeedbackCallSummaryInstance

        :param date end_date:
        :param date start_date:
        :param str account_sid:

        :returns: The updated FeedbackCallSummaryInstance
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryInstance
        """
        return self._proxy.update(
            end_date=end_date,
            start_date=start_date,
            account_sid=account_sid,
        )

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.FeedbackCallSummaryInstance {}>".format(context)


class FeedbackCallSummaryContext(InstanceContext):
    def __init__(self, version: Version, account_sid: str, sid: str):
        """
        Initialize the FeedbackCallSummaryContext

        :param Version version: Version that contains the resource
        :param account_sid:
        :param sid:

        :returns: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryContext
        """
        super().__init__(version)

        # Path Solution
        self._solution = {
            "account_sid": account_sid,
            "sid": sid,
        }
        self._uri = "/Accounts/{account_sid}/Calls/Feedback/Summary/{sid}.json".format(
            **self._solution
        )

    def update(self, end_date, start_date, account_sid=values.unset):
        """
        Update the FeedbackCallSummaryInstance

        :param date end_date:
        :param date start_date:
        :param str account_sid:

        :returns: The updated FeedbackCallSummaryInstance
        :rtype: twilio.rest.api.v2010.account.call.feedback_call_summary.FeedbackCallSummaryInstance
        """
        data = values.of(
            {
                "EndDate": serialize.iso8601_date(end_date),
                "StartDate": serialize.iso8601_date(start_date),
                "AccountSid": account_sid,
            }
        )

        payload = self._version.update(
            method="POST",
            uri=self._uri,
            data=data,
        )

        return FeedbackCallSummaryInstance(
            self._version,
            payload,
            account_sid=self._solution["account_sid"],
            sid=self._solution["sid"],
        )

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        context = " ".join("{}={}".format(k, v) for k, v in self._solution.items())
        return "<Twilio.Api.V2010.FeedbackCallSummaryContext {}>".format(context)
