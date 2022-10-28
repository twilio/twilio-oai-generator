"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("versionless", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const twilio = new Twilio(accountSid, authToken);

  afterAll(() => {
    nock.cleanAll();
  });

  it("should create a deployed devices fleet", () => {
    const scope = nock("http://versionless.twilio.com")
      .post("/DeployedDevices/Fleets", {
        FriendlyName: "Friend-o",
      })
      .reply(201, { sid: "123" });

    return twilio.versionless.deployed_devices.fleets
      .create({ friendlyName: "Friend-o" })
      .then(() => scope.done());
  });
});
