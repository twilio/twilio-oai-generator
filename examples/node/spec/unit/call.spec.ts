"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("flex-calls", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const twilio = new Twilio(accountSid, authToken);

  afterAll(() => {
    nock.cleanAll();
  });

  it("should update a voice call", () => {
    const scope = nock("http://flex-api.twilio.com")
      .post("/v1/Voice/123")
      .reply(200, { sid: "123" });

    return twilio.flexApi.v1.calls("123").update(() => scope.done());
  });

  it("should throw given an invalid path param", () => {
    expect(() => twilio.flexApi.v1.calls("?")).toThrow("not valid");
  });
});
