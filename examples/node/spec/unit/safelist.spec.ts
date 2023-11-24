"use strict";
import nock from "nock";
import Twilio from "../../lib/rest/Twilio";

describe("safelist", () => {
    const accountSid: string = "AC12345678123456781234567812345678";
    const authToken: string = "CR12345678123456781234567812345678";
    const twilio = new Twilio(accountSid, authToken);

    beforeEach(() => {
        // Clean all nock interceptors before each test
        nock.cleanAll();
    });

    afterEach(() => {
        // Ensure that all expected requests were made after each test
        nock.isDone();
    });

    it("should create a new safelist", () => {
        const scope = nock("http://accounts.twilio.com")
            .post("/v1/SafeList/Numbers", {
                PhoneNumber: "+12345678910"
            })
            .reply(201, { phoneNumber: "+12345678910" });

        return twilio.accounts.v1.safelist
            .create({
                phoneNumber: "+12345678910"
            })
            .then(() => scope.done());
    });

    it("should fetch a safelist", () => {
        const scope = nock("http://accounts.twilio.com")
            .get("/v1/SafeList/Numbers")
            .query({
                phoneNumber: "+12345678910"
            })
            .reply(200, { phoneNumber: "+12345678910" });

        return twilio.accounts.v1.safelist
            .fetch({
                phoneNumber: "+12345678910"
            })
            .then(() => scope.done());
    });

    it("should delete a safelist", () => {
        const scope = nock("http://accounts.twilio.com")
            .delete("/v1/SafeList/Numbers")
            .query({
                phoneNumber: "+12345678910"
            })
            .reply(204, { phoneNumber: "+12345678910" });

        return twilio.accounts.v1.safelist
            .remove({
                phoneNumber: "+12345678910"
            })
            .then(() => scope.done());
    });
});
