"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("versionless", () => {
  const twilio = new Twilio();

  it("should create a deployed devices fleet", () => {
    const scope = nock("https://versionless.twilio.com")
      .post("/DeployedDevices/Fleets")
      .query({
        FriendlyName: "Friend-o",
      })
      .reply(201, { sid: "123" });

    return twilio.versionless.deployed_devices.fleets
      .create({ friendlyName: "Friend-o" })
      .then(() => scope.done());
  });
});
