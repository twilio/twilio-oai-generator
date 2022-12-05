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
        Name: "Otis",
      })
      .reply(201, { name: "Otis" });

    return twilio.versionless.deployed_devices.fleets
      .create({ name: "Otis" })
      .then((fleetInstance) => {
        expect(fleetInstance.name).toEqual("Otis");
        scope.done();
      });
  });

  it("should fetch a deployed devices fleet", () => {
    const scope = nock("http://versionless.twilio.com")
      .get("/DeployedDevices/Fleets/123")
      .reply(200, { sid: "123", friendly_name: "Friend-o" });

    return twilio.versionless.deployed_devices
      .fleets("123")
      .fetch()
      .then((fleetInstance) => {
        expect(fleetInstance.sid).toEqual("123");
        expect(fleetInstance.friendlyName).toEqual("Friend-o");
        scope.done();
      });
  });

  it("should page the understand assistants", () => {
    const scope = nock("http://versionless.twilio.com")
      .get("/understand/Assistants")
      .reply(200, {
        assistants: [
          {
            friendly_name: "flea",
          },
        ],
        meta: {
          key: "assistants",
        },
      });

    return twilio.versionless.understand.assistants
      .page()
      .then((assistantPage) => {
        expect(assistantPage.instances[0].friendlyName).toEqual("flea");
      })
      .then(() => scope.done());
  });
});
