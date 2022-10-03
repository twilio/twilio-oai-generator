"use strict";
import nock from "nock";
import { AccountListInstancePageOptions } from "../../lib/rest/api/v2010/account";
import Twilio from "../../lib/rest/Twilio";

describe("account", () => {
  const twilio = new Twilio();

  it("should create an account", () => {
    const scope = nock("https://api.twilio.com")
      .post("/v2010/2010-04-01/Accounts.json")
      .reply(201, { account_sid: "123" });

    return twilio.api.v2010.accounts
      .create({ sid: "123" })
      .then(() => scope.done());
  });

  it("should fetch an account", () => {
    const scope = nock("https://api.twilio.com")
      .get("/v2010/2010-04-01/Accounts/123.json")
      .reply(200, { account_sid: "123" });

    return twilio.api.v2010
      .accounts("123")
      .fetch()
      .then(() => scope.done());
  });

  it("should update an account", () => {
    const scope = nock("https://api.twilio.com")
      .post("/v2010/2010-04-01/Accounts/123.json")
      .query({
        Status: "closed",
      })
      .reply(200, { account_sid: "123", status: "closed" });

    return twilio.api.v2010
      .accounts("123")
      .update({ status: "closed" })
      .then(() => scope.done());
  });

  it("should remove an account", () => {
    const scope = nock("https://api.twilio.com")
      .delete("/v2010/2010-04-01/Accounts/123.json")
      .reply(204);

    return twilio.api.v2010
      .accounts("123")
      .remove()
      .then(() => scope.done());
  });

  it("should get account pages in between dates", () => {
    const scope = nock("https://api.twilio.com")
      .get("/v2010/2010-04-01/Accounts.json")
      .query({
        "DateCreated<": "2022-12-25T00:00:00Z",
        "DateCreated>": "2022-01-01T00:00:00Z",
      })
      .reply(200, {
        first_page_uri:
          "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0",
        end: 0,
        previous_page_uri:
          "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0",
        accounts: [],
        uri: "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0",
        page_size: 50,
        start: 0,
        next_page_uri:
          "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=50",
        page: 0,
      });
    const params: AccountListInstancePageOptions = {
      dateCreatedBefore: new Date("2022-12-25"),
      dateCreatedAfter: new Date("2022-01-01"),
    };
    return twilio.api.v2010.accounts
      .page(params)
      .then((accountPage) =>
        expect(accountPage.nextPageUrl).toEqual(
          "https://api.twilio.com/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=50"
        )
      )
      .then(() => scope.done());
  });
});
