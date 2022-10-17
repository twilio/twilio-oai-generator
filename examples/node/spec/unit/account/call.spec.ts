"use strict";
import nock from "nock";
import Twilio from "../../../lib/rest/Twilio";

describe("call", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const twilio = new Twilio(accountSid, authToken);

  afterAll(() => {
    nock.cleanAll();
  });

  it("should create a call", () => {
    const scope = nock("http://api.twilio.com")
      .post("/2010-04-01/Accounts/123/Calls.json")
      .query({
        RequiredStringProperty: "radda radda",
      })
      .reply(201, {
        requiredStringProperty: "radda radda",
        account_sid: "123",
        sid: 1,
      });

    return twilio.api.v2010
      .accounts("123")
      .calls.create({
        requiredStringProperty: "radda radda",
        account_sid: "123",
        sid: 1,
      })
      .then(() => scope.done());
  });

  it("should fetch a call", () => {
    const scope = nock("http://api.twilio.com")
      .get("/2010-04-01/Accounts/123/Calls/1.json")
      .reply(307, { account_sid: "123", sid: 1 });

    return twilio.api.v2010
      .accounts("123")
      .calls(1)
      .fetch()
      .then(() => scope.done());
  });

  it("should remove a call", () => {
    const scope = nock("http://api.twilio.com")
      .delete("/2010-04-01/Accounts/123/Calls/1.json")
      .reply(204);

    return twilio.api.v2010
      .accounts("123")
      .calls(1)
      .remove()
      .then(() => scope.done());
  });

  it("should create a feedback summary", () => {
    const scope = nock("http://api.twilio.com")
      .post("/2010-04-01/Accounts/123/Calls/Feedback/Summary/456.json")
      .query({
        EndDate: "2022-08-01",
        StartDate: "2022-08-01",
      })
      .reply(201, { test_array: [{ count: 4 }] });

    return twilio.api.v2010
      .accounts("123")
      .calls.feedbackCallSummary("456")
      .update({ endDate: "2022-08-01", startDate: "2022-08-01" })
      .then(() => scope.done());
  });
});
