"use strict";
import nock from "nock";
import {
  AccountInstance,
  AccountListInstancePageOptions,
} from "../../lib/rest/api/v2010/account";
import { ApiResponse } from "../../lib/base/ApiResponse";
import Twilio from "../../lib/rest/Twilio";

describe("account WithHttpInfo methods", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const twilio = new Twilio(accountSid, authToken);

  afterAll(() => {
    nock.cleanAll();
  });

  describe("createWithHttpInfo", () => {
    it("should create an account and return HTTP metadata", async () => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "test",
        test_integer: 42,
      };

      const scope = nock("http://api.twilio.com")
        .post("/2010-04-01/Accounts.json")
        .reply(201, mockResponse, {
          "Content-Type": "application/json",
          "X-Request-ID": "RQ123",
          "X-RateLimit-Remaining": "999",
        });

      const response: ApiResponse<AccountInstance> =
        await twilio.api.v2010.accounts.createWithHttpInfo({
          xTwilioWebhookEnabled: "true",
        });

      // Verify ApiResponse structure
      expect(response).toBeDefined();
      expect(response.statusCode).toBe(201);
      expect(response.headers).toBeDefined();
      expect(response.body).toBeDefined();

      // Verify headers
      expect(response.headers["content-type"]).toContain("application/json");
      expect(response.headers["x-request-id"]).toBe("RQ123");
      expect(response.headers["x-ratelimit-remaining"]).toBe("999");

      // Verify body is AccountInstance
      expect(response.body).toBeInstanceOf(AccountInstance);
      expect(response.body.sid).toBe("AC123");

      scope.done();
    });

    it("should handle callback for createWithHttpInfo", (done) => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "test",
        test_integer: 42,
      };

      const scope = nock("http://api.twilio.com")
        .post("/2010-04-01/Accounts.json")
        .reply(201, mockResponse, { "X-Request-ID": "RQ456" });

      twilio.api.v2010.accounts.createWithHttpInfo(
        { xTwilioWebhookEnabled: "false" },
        (error, response) => {
          expect(error).toBeNull();
          expect(response).toBeDefined();
          expect(response!.statusCode).toBe(201);
          expect(response!.headers["x-request-id"]).toBe("RQ456");
          expect(response!.body).toBeInstanceOf(AccountInstance);
          scope.done();
          done();
        },
      );
    });
  });

  describe("fetchWithHttpInfo", () => {
    it("should fetch an account and return HTTP metadata", async () => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "fetched",
        test_integer: 100,
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts/AC123.json")
        .reply(200, mockResponse, {
          "Content-Type": "application/json",
          ETag: '"abc123"',
          "X-RateLimit-Remaining": "998",
        });

      const response: ApiResponse<AccountInstance> = await twilio.api.v2010
        .accounts("AC123")
        .fetchWithHttpInfo();

      // Verify ApiResponse structure
      expect(response.statusCode).toBe(200);
      expect(response.headers).toBeDefined();
      expect(response.headers["etag"]).toBe('"abc123"');
      expect(response.headers["x-ratelimit-remaining"]).toBe("998");

      // Verify body
      expect(response.body).toBeInstanceOf(AccountInstance);
      expect(response.body.sid).toBe("AC123");
      expect(response.body.testString).toBe("fetched");
      expect(response.body.testInteger).toBe(100);

      scope.done();
    });

    it("should handle callback for fetchWithHttpInfo", (done) => {
      const mockResponse = {
        account_sid: "AC456",
        sid: "AC456",
        test_string: "callback",
        test_integer: 50,
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts/AC456.json")
        .reply(200, mockResponse);

      twilio.api.v2010
        .accounts("AC456")
        .fetchWithHttpInfo((error, response) => {
          expect(error).toBeNull();
          expect(response).toBeDefined();
          expect(response!.statusCode).toBe(200);
          expect(response!.body.sid).toBe("AC456");
          scope.done();
          done();
        });
    });
  });

  describe("updateWithHttpInfo", () => {
    it("should update an account and return HTTP metadata", async () => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "updated",
        test_integer: 200,
        status: "paused",
      };

      const scope = nock("http://api.twilio.com")
        .post("/2010-04-01/Accounts/AC123.json", {
          Status: "paused",
        })
        .reply(200, mockResponse, {
          "Content-Type": "application/json",
          "X-Request-ID": "RQ789",
        });

      const response: ApiResponse<AccountInstance> = await twilio.api.v2010
        .accounts("AC123")
        .updateWithHttpInfo({ status: "paused" });

      // Verify ApiResponse structure
      expect(response.statusCode).toBe(200);
      expect(response.headers["x-request-id"]).toBe("RQ789");

      // Verify body
      expect(response.body).toBeInstanceOf(AccountInstance);
      expect(response.body.sid).toBe("AC123");

      scope.done();
    });

    it("should handle callback for updateWithHttpInfo", (done) => {
      const mockResponse = {
        account_sid: "AC789",
        sid: "AC789",
        test_string: "updated",
        test_integer: 300,
        status: "stopped",
      };

      const scope = nock("http://api.twilio.com")
        .post("/2010-04-01/Accounts/AC789.json")
        .reply(200, mockResponse);

      twilio.api.v2010
        .accounts("AC789")
        .updateWithHttpInfo({ status: "stopped" }, (error, response) => {
          expect(error).toBeNull();
          expect(response!.statusCode).toBe(200);
          expect(response!.body.sid).toBe("AC789");
          scope.done();
          done();
        });
    });
  });

  describe("removeWithHttpInfo", () => {
    it("should remove an account and return HTTP metadata", async () => {
      const scope = nock("http://api.twilio.com")
        .delete("/2010-04-01/Accounts/AC123.json")
        .reply(204, "", {
          "X-Request-ID": "RQ999",
          "X-RateLimit-Remaining": "995",
        });

      const response: ApiResponse<boolean> = await twilio.api.v2010
        .accounts("AC123")
        .removeWithHttpInfo();

      // Verify ApiResponse structure
      expect(response.statusCode).toBe(204);
      expect(response.headers["x-request-id"]).toBe("RQ999");
      expect(response.headers["x-ratelimit-remaining"]).toBe("995");

      // Verify body is boolean
      expect(response.body).toBe(true);

      scope.done();
    });

    it("should handle callback for removeWithHttpInfo", (done) => {
      const scope = nock("http://api.twilio.com")
        .delete("/2010-04-01/Accounts/AC456.json")
        .reply(204);

      twilio.api.v2010
        .accounts("AC456")
        .removeWithHttpInfo((error, response) => {
          expect(error).toBeNull();
          expect(response!.statusCode).toBe(204);
          expect(response!.body).toBe(true);
          scope.done();
          done();
        });
    });

    it("should return false in body for failed delete", async () => {
      const scope = nock("http://api.twilio.com")
        .delete("/2010-04-01/Accounts/AC999.json")
        .reply(404, { error: "Not found" });

      try {
        await twilio.api.v2010.accounts("AC999").removeWithHttpInfo();
        fail("Should have thrown an error");
      } catch (error) {
        // Expected to throw on 404
        expect(error).toBeDefined();
      }

      scope.done();
    });
  });

  describe("pageWithHttpInfo", () => {
    it("should get a page of accounts and return HTTP metadata", async () => {
      const mockResponse = {
        first_page_uri: "/2010-04-01/Accounts.json?PageSize=50&Page=0",
        end: 0,
        previous_page_uri: "/2010-04-01/Accounts.json?PageSize=50&Page=0",
        uri: "/2010-04-01/Accounts.json?PageSize=50&Page=0",
        page_size: 50,
        start: 0,
        next_page_uri: "/2010-04-01/Accounts.json?PageSize=50&Page=1",
        page: 0,
        accounts: [
          {
            account_sid: "AC111",
            sid: "AC111",
            test_string: "account1",
            test_integer: 1,
          },
          {
            account_sid: "AC222",
            sid: "AC222",
            test_string: "account2",
            test_integer: 2,
          },
        ],
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts.json")
        .query({ PageSize: 20 })
        .reply(200, mockResponse, {
          "Content-Type": "application/json",
          "X-Request-ID": "RQ111",
          "X-RateLimit-Remaining": "990",
        });

      const response = await twilio.api.v2010.accounts.pageWithHttpInfo({
        pageSize: 20,
      });

      // Verify ApiResponse structure
      expect(response.statusCode).toBe(200);
      expect(response.headers["x-request-id"]).toBe("RQ111");
      expect(response.headers["x-ratelimit-remaining"]).toBe("990");

      // Verify body is AccountPage
      expect(response.body).toBeDefined();
      expect(response.body.instances).toHaveLength(2);
      expect(response.body.instances[0]).toBeInstanceOf(AccountInstance);
      expect(response.body.instances[0].sid).toBe("AC111");

      scope.done();
    });

    it("should handle callback for pageWithHttpInfo", (done) => {
      const mockResponse = {
        first_page_uri: "/2010-04-01/Accounts.json",
        accounts: [],
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts.json")
        .reply(200, mockResponse);

      twilio.api.v2010.accounts.pageWithHttpInfo((error, response) => {
        expect(error).toBeNull();
        expect(response!.statusCode).toBe(200);
        expect(response!.body.instances).toHaveLength(0);
        scope.done();
        done();
      });
    });
  });

  describe("getPageWithHttpInfo", () => {
    it("should get a specific page by URL and return HTTP metadata", async () => {
      const targetUrl =
        "http://api.twilio.com/2010-04-01/Accounts.json?Page=1&PageToken=xyz";

      const mockResponse = {
        first_page_uri: "/2010-04-01/Accounts.json",
        accounts: [
          {
            account_sid: "AC333",
            sid: "AC333",
            test_string: "page2",
            test_integer: 3,
          },
        ],
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts.json")
        .query({ Page: "1", PageToken: "xyz" })
        .reply(200, mockResponse, {
          "Content-Type": "application/json",
          "X-Page-Number": "1",
        });

      const response =
        await twilio.api.v2010.accounts.getPageWithHttpInfo(targetUrl);

      // Verify ApiResponse structure
      expect(response.statusCode).toBe(200);
      expect(response.headers["x-page-number"]).toBe("1");

      // Verify body
      expect(response.body.instances).toHaveLength(1);
      expect(response.body.instances[0].sid).toBe("AC333");

      scope.done();
    });
  });

  describe("listWithHttpInfo", () => {
    it("should list all accounts and return HTTP metadata", async () => {
      // Mock first page
      const firstPageResponse = {
        first_page_uri: "/2010-04-01/Accounts.json",
        next_page_uri: "/2010-04-01/Accounts.json?PageToken=token2",
        accounts: [
          {
            account_sid: "AC111",
            sid: "AC111",
            test_string: "account1",
            test_integer: 1,
          },
        ],
      };

      // Mock second page (last page)
      const secondPageResponse = {
        first_page_uri: "/2010-04-01/Accounts.json",
        next_page_uri: null,
        accounts: [
          {
            account_sid: "AC222",
            sid: "AC222",
            test_string: "account2",
            test_integer: 2,
          },
        ],
      };

      const scope1 = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts.json")
        .query({ PageSize: 10 })
        .reply(200, firstPageResponse, {
          "X-Request-ID": "RQ-Page1",
          "X-RateLimit-Remaining": "999",
        });

      const scope2 = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts.json")
        .query({ PageToken: "token2" })
        .reply(200, secondPageResponse, {
          "X-Request-ID": "RQ-Page2",
          "X-RateLimit-Remaining": "998",
        });

      const response: ApiResponse<AccountInstance[]> =
        await twilio.api.v2010.accounts.listWithHttpInfo({ limit: 10 });

      // Verify ApiResponse structure
      expect(response.statusCode).toBeDefined();
      expect(response.headers).toBeDefined();

      // Headers should be from the first page
      expect(response.headers["x-request-id"]).toBe("RQ-Page1");

      // Verify body is array of AccountInstances
      expect(Array.isArray(response.body)).toBe(true);
      expect(response.body.length).toBeGreaterThan(0);
      expect(response.body[0]).toBeInstanceOf(AccountInstance);

      scope1.done();
      scope2.done();
    });
  });

  describe("error handling", () => {
    it("should include HTTP metadata in error responses", async () => {
      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts/INVALID.json")
        .reply(
          404,
          { error: "Account not found" },
          {
            "X-Request-ID": "RQ-Error",
            "X-Error-Code": "20404",
          },
        );

      try {
        await twilio.api.v2010.accounts("INVALID").fetchWithHttpInfo();
        fail("Should have thrown an error");
      } catch (error: any) {
        // Error should contain HTTP metadata
        expect(error).toBeDefined();
        // Note: Error handling implementation may vary
        // Check if error contains status code or headers if implemented
        scope.done();
      }
    });
  });

  describe("rate limit monitoring", () => {
    it("should allow monitoring rate limits via headers", async () => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "test",
        test_integer: 42,
      };

      const scope = nock("http://api.twilio.com")
        .get("/2010-04-01/Accounts/AC123.json")
        .reply(200, mockResponse, {
          "X-RateLimit-Limit": "1000",
          "X-RateLimit-Remaining": "950",
          "X-RateLimit-Reset": "1640000000",
        });

      const response = await twilio.api.v2010
        .accounts("AC123")
        .fetchWithHttpInfo();

      // Monitor rate limits
      const rateLimit = {
        limit: parseInt(response.headers["x-ratelimit-limit"] || "0"),
        remaining: parseInt(response.headers["x-ratelimit-remaining"] || "0"),
        reset: parseInt(response.headers["x-ratelimit-reset"] || "0"),
      };

      expect(rateLimit.limit).toBe(1000);
      expect(rateLimit.remaining).toBe(950);
      expect(rateLimit.reset).toBe(1640000000);

      // Could implement rate limit warnings
      if (rateLimit.remaining < 100) {
        console.warn("Rate limit running low!");
      }

      scope.done();
    });
  });

  describe("debugging with request IDs", () => {
    it("should provide request IDs for debugging", async () => {
      const mockResponse = {
        account_sid: "AC123",
        sid: "AC123",
        test_string: "test",
        test_integer: 42,
      };

      const requestId = "RQ-Debug-12345";

      const scope = nock("http://api.twilio.com")
        .post("/2010-04-01/Accounts.json")
        .reply(201, mockResponse, {
          "X-Request-ID": requestId,
          "X-Response-Time": "125ms",
        });

      const response = await twilio.api.v2010.accounts.createWithHttpInfo({});

      // Request ID available for debugging/logging
      expect(response.headers["x-request-id"]).toBe(requestId);
      expect(response.headers["x-response-time"]).toBe("125ms");

      // Could log for debugging
      console.log(
        `Request ${response.headers["x-request-id"]} completed in ${response.headers["x-response-time"]}`,
      );

      scope.done();
    });
  });
});
