"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("credential", () => {
  const twilio = new Twilio();

  it("should update an aws credential", () => {
    const scope = nock("https://flex-api.twilio.com")
      .post("/v1/Credentials/AWS/123")
      .reply(200, { sid: "123" });

    return twilio.flexApi.v1.credential.aws("123").update(() => scope.done());
  });
});
