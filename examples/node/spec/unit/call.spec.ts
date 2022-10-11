"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("credential", () => {
  const accountSid:string = 'AC12345678123456781234567812345678';
  const authToken:string = 'CR12345678123456781234567812345678';
  const twilio = new Twilio(accountSid, authToken);

  it("should update an aws credential", () => {
    const scope = nock("https://flex-api.twilio.com")
      .post("/v1/Voice/123")
      .reply(200, { sid: "123" });

    return twilio.flexApi.v1.call("123").update(() => scope.done());
  });
});
