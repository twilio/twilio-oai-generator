"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";
import { AwsContext } from "../../lib/rest/flexApi/v1/credential/aws";

describe("credential", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const twilio = new Twilio(accountSid, authToken);

  afterAll(() => {
    nock.cleanAll();
  });

  it("should create a new set of aws credentials", () => {
    const scope = nock("http://flex-api.twilio.com")
      .post("/v1/Credentials/AWS", {
        TestString: "I'm New Here",
        TestEnum: "completed",
        TestAnyType: '{"hear":"me"}',
        SomeA2PThing: "caps",
      })
      .reply(201, { sid: "123" });

    return twilio.flexApi.v1.credentials.newCredentials
      .create({
        testString: "I'm New Here",
        testEnum: "completed",
        testAnyType: { hear: "me" },
        someA2PThing: "caps",
      })
      .then(() => scope.done());
  });

  describe("AwsContext", () => {
    const aws: AwsContext = twilio.flexApi.v1.credentials.aws("123");

    it("should update an aws credential", () => {
      const scope = nock("http://flex-api.twilio.com")
        .post("/v1/Credentials/AWS/123")
        .reply(200, { sid: "123" });

      return aws.update(() => scope.done());
    });

    it("should remove an aws credential", () => {
      const scope = nock("http://flex-api.twilio.com")
        .delete("/v1/Credentials/AWS/123")
        .reply(204);

      return aws.remove((error, response) => {
        expect(error).toBeNull();
        expect(response).toEqual(true);
        scope.done();
      });
    });

    it("should fetch a nested aws credential history instance", () => {
      const scope = nock("http://flex-api.twilio.com")
        .get("/v1/Credentials/AWS/123/History")
        .reply(200, { sid: "123" });

      return aws
        .history()
        .fetch()
        .then(() => scope.done());
    });
  });
});
