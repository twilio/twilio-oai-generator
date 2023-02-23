"use strict";
import Twilio from "../../lib/rest/Twilio";
import { CallListInstanceCreateOptions } from "../../lib/rest/api/v2010/account/call";
import { FeedbackCallSummaryContextUpdateOptions } from "../../lib/rest/api/v2010/account/call/feedbackCallSummary";

describe("Integration tests", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const client = new Twilio(accountSid, authToken);

  it("should get an account", async () => {
    const result = await client.api.v2010.accounts.page();
    expect(result.instances[0].testString).toEqual("Ahoy");
    expect(result.instances[1].testString).toEqual("Matey");
  });

  it("should create an account", async () => {
    const result = await client.api.v2010.accounts.create();
    expect(result.sid).toEqual("CR12345678123456781234567812345678");
    expect(result.testString).toEqual("Ahoy");
  });

  it("should create a call ", async () => {
    const params: CallListInstanceCreateOptions = {
      requiredStringProperty: "str",
      testMethod: "POST",
    };
    const result = await client.api.v2010
      .accounts(accountSid)
      .calls.create(params);
    expect(result.sid).toEqual("CR12345678123456781234567812345678");
    expect(result.testString).toEqual("Ahoy");
  });

  it("should fetch a call", async () => {
    const result = await client.api.v2010
      .accounts(accountSid)
      .calls(123)
      .fetch();
    expect(result.sid).toEqual("CR12345678123456781234567812345678");
    expect(result.testString).toEqual("Ahoy");
  });

  it("should update call feedback summary", async () => {
    const params: FeedbackCallSummaryContextUpdateOptions = {
      endDate: "2020-12-31",
      startDate: "2020-01-01",
    };
    const result = await client.api.v2010
      .accounts(accountSid)
      .calls.feedbackCallSummary("CR12345678123456781234567812345678")
      .update(params);
    expect(result.testArrayOfObjects[0].description).toEqual(
      "issue description"
    );
    expect(result.testArrayOfObjects[0].count).toEqual(4);
  });
});
