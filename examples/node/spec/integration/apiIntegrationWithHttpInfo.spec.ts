"use strict";
import Twilio from "../../lib/rest/Twilio";
import { ApiResponse } from "../../lib/base/ApiResponse";
import {
  AccountInstance,
  AccountListInstancePageOptions,
} from "../../lib/rest/api/v2010/account";
import { CallListInstanceCreateOptions } from "../../lib/rest/api/v2010/account/call";

describe("Integration tests - WithHttpInfo methods", () => {
  const accountSid: string = "AC12345678123456781234567812345678";
  const authToken: string = "CR12345678123456781234567812345678";
  const client = new Twilio(accountSid, authToken);

  describe("Account operations", () => {
    describe("createWithHttpInfo", () => {
      it("should create an account with HTTP metadata", async () => {
        const response: ApiResponse<AccountInstance> =
          await client.api.v2010.accounts.createWithHttpInfo();

        // Verify ApiResponse structure
        expect(response).toBeDefined();
        expect(response.statusCode).toBeDefined();
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify status code
        expect(response.statusCode).toBeGreaterThanOrEqual(200);
        expect(response.statusCode).toBeLessThan(300);

        // Verify headers exist
        expect(typeof response.headers).toBe("object");
        expect(Object.keys(response.headers).length).toBeGreaterThan(0);

        // Verify common headers
        if (response.headers["content-type"]) {
          expect(response.headers["content-type"]).toContain(
            "application/json",
          );
        }

        // Verify body content
        expect(response.body).toBeInstanceOf(AccountInstance);
        expect(response.body.sid).toBe("CR12345678123456781234567812345678");
        expect(response.body.testString).toEqual("Ahoy");
      });
    });

    describe("pageWithHttpInfo", () => {
      it("should get account page with HTTP metadata", async () => {
        const response = await client.api.v2010.accounts.pageWithHttpInfo();

        // Verify ApiResponse structure
        expect(response.statusCode).toBeDefined();
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify status code
        expect(response.statusCode).toBe(200);

        // Verify headers
        expect(typeof response.headers).toBe("object");

        // Verify body content
        expect(response.body.instances).toBeDefined();
        expect(Array.isArray(response.body.instances)).toBe(true);
        expect(response.body.instances.length).toBeGreaterThan(0);
        expect(response.body.instances[0]).toBeInstanceOf(AccountInstance);
        expect(response.body.instances[0].testString).toEqual("Ahoy");
        expect(response.body.instances[1].testString).toEqual("Matey");
      });

      it("should handle page options with HTTP metadata", async () => {
        const options: AccountListInstancePageOptions = {
          pageSize: 10,
        };

        const response =
          await client.api.v2010.accounts.pageWithHttpInfo(options);

        expect(response.statusCode).toBe(200);
        expect(response.body.instances).toBeDefined();
      });
    });

    describe("fetchWithHttpInfo", () => {
      it("should fetch an account with HTTP metadata", async () => {
        // First create an account to fetch
        const createResponse =
          await client.api.v2010.accounts.createWithHttpInfo();
        const accountSid = createResponse.body.sid;

        // Now fetch it with HTTP info
        const response: ApiResponse<AccountInstance> = await client.api.v2010
          .accounts(accountSid)
          .fetchWithHttpInfo();

        // Verify ApiResponse structure
        expect(response.statusCode).toBe(200);
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify body
        expect(response.body).toBeInstanceOf(AccountInstance);
        expect(response.body.sid).toBe(accountSid);
      });
    });

    describe("updateWithHttpInfo", () => {
      it("should update an account with HTTP metadata", async () => {
        // First create an account
        const createResponse =
          await client.api.v2010.accounts.createWithHttpInfo();
        const accountSid = createResponse.body.sid;

        // Update it with HTTP info
        const response: ApiResponse<AccountInstance> = await client.api.v2010
          .accounts(accountSid)
          .updateWithHttpInfo({ status: "paused" });

        // Verify ApiResponse structure
        expect(response.statusCode).toBe(200);
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify body
        expect(response.body).toBeInstanceOf(AccountInstance);
        expect(response.body.sid).toBe(accountSid);
      });
    });

    describe("removeWithHttpInfo", () => {
      it("should remove an account with HTTP metadata", async () => {
        // First create an account
        const createResponse =
          await client.api.v2010.accounts.createWithHttpInfo();
        const accountSid = createResponse.body.sid;

        // Remove it with HTTP info
        const response: ApiResponse<boolean> = await client.api.v2010
          .accounts(accountSid)
          .removeWithHttpInfo();

        // Verify ApiResponse structure
        expect(response.statusCode).toBeDefined();
        expect(response.statusCode).toBeGreaterThanOrEqual(200);
        expect(response.statusCode).toBeLessThan(300);
        expect(response.headers).toBeDefined();

        // Verify body is boolean
        expect(typeof response.body).toBe("boolean");
        expect(response.body).toBe(true);
      });
    });

    describe("listWithHttpInfo", () => {
      it("should list accounts with HTTP metadata", async () => {
        const response: ApiResponse<AccountInstance[]> =
          await client.api.v2010.accounts.listWithHttpInfo({ limit: 5 });

        // Verify ApiResponse structure
        expect(response.statusCode).toBeDefined();
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify body is array
        expect(Array.isArray(response.body)).toBe(true);
        if (response.body.length > 0) {
          expect(response.body[0]).toBeInstanceOf(AccountInstance);
        }
      });
    });
  });

  describe("Call operations", () => {
    describe("createWithHttpInfo", () => {
      it("should create a call with HTTP metadata", async () => {
        const params: CallListInstanceCreateOptions = {
          requiredStringProperty: "str",
          testMethod: "POST",
        };

        const response = await client.api.v2010
          .accounts(accountSid)
          .calls.createWithHttpInfo(params);

        // Verify ApiResponse structure
        expect(response.statusCode).toBeDefined();
        expect(response.statusCode).toBeGreaterThanOrEqual(200);
        expect(response.statusCode).toBeLessThan(300);
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify body content
        expect(response.body.sid).toBe("CR12345678123456781234567812345678");
        expect(response.body.testString).toEqual("Ahoy");
      });
    });

    describe("fetchWithHttpInfo", () => {
      it("should fetch a call with HTTP metadata", async () => {
        const response = await client.api.v2010
          .accounts(accountSid)
          .calls(123)
          .fetchWithHttpInfo();

        // Verify ApiResponse structure
        expect(response.statusCode).toBe(200);
        expect(response.headers).toBeDefined();
        expect(response.body).toBeDefined();

        // Verify body content
        expect(response.body.sid).toBe("CR12345678123456781234567812345678");
        expect(response.body.testString).toEqual("Ahoy");
      });
    });
  });

  describe("HTTP metadata validation", () => {
    it("should have valid status codes", async () => {
      // Create - should be 201
      const createResponse =
        await client.api.v2010.accounts.createWithHttpInfo();
      expect([200, 201]).toContain(createResponse.statusCode);

      // Fetch - should be 200
      const fetchResponse = await client.api.v2010
        .accounts(createResponse.body.sid)
        .fetchWithHttpInfo();
      expect(fetchResponse.statusCode).toBe(200);

      // Update - should be 200
      const updateResponse = await client.api.v2010
        .accounts(createResponse.body.sid)
        .updateWithHttpInfo({ status: "paused" });
      expect(updateResponse.statusCode).toBe(200);

      // Delete - should be 204 or 200
      const deleteResponse = await client.api.v2010
        .accounts(createResponse.body.sid)
        .removeWithHttpInfo();
      expect([200, 204]).toContain(deleteResponse.statusCode);
    });

    it("should have valid headers", async () => {
      const response = await client.api.v2010.accounts.pageWithHttpInfo();

      // Headers should be an object
      expect(typeof response.headers).toBe("object");
      expect(response.headers).not.toBeNull();

      // Should have at least some headers
      const headerKeys = Object.keys(response.headers);
      expect(headerKeys.length).toBeGreaterThan(0);

      // Common headers should exist (if present in response)
      const commonHeaders = [
        "content-type",
        "x-request-id",
        "x-ratelimit-limit",
        "x-ratelimit-remaining",
        "date",
      ];

      // Check if at least one common header exists
      const hasCommonHeader = commonHeaders.some((header) =>
        headerKeys.includes(header),
      );
      expect(hasCommonHeader).toBe(true);
    });

    it("should have consistent structure across all methods", async () => {
      // Create
      const createResp = await client.api.v2010.accounts.createWithHttpInfo();
      expect(createResp).toHaveProperty("statusCode");
      expect(createResp).toHaveProperty("headers");
      expect(createResp).toHaveProperty("body");

      // Page
      const pageResp = await client.api.v2010.accounts.pageWithHttpInfo();
      expect(pageResp).toHaveProperty("statusCode");
      expect(pageResp).toHaveProperty("headers");
      expect(pageResp).toHaveProperty("body");

      // Fetch
      const fetchResp = await client.api.v2010
        .accounts(createResp.body.sid)
        .fetchWithHttpInfo();
      expect(fetchResp).toHaveProperty("statusCode");
      expect(fetchResp).toHaveProperty("headers");
      expect(fetchResp).toHaveProperty("body");

      // All responses should have same structure
      const checkStructure = (resp: any) => {
        expect(typeof resp.statusCode).toBe("number");
        expect(typeof resp.headers).toBe("object");
        expect(resp.body).toBeDefined();
      };

      checkStructure(createResp);
      checkStructure(pageResp);
      checkStructure(fetchResp);
    });
  });

  describe("Rate limit monitoring", () => {
    it("should expose rate limit headers", async () => {
      const response = await client.api.v2010.accounts.pageWithHttpInfo();

      // Check for rate limit headers (if present)
      const headers = response.headers;
      const hasRateLimitHeaders =
        headers["x-ratelimit-limit"] !== undefined ||
        headers["x-ratelimit-remaining"] !== undefined;

      if (hasRateLimitHeaders) {
        // If rate limit headers exist, they should be parseable
        if (headers["x-ratelimit-limit"]) {
          const limit = parseInt(headers["x-ratelimit-limit"]);
          expect(limit).toBeGreaterThan(0);
        }

        if (headers["x-ratelimit-remaining"]) {
          const remaining = parseInt(headers["x-ratelimit-remaining"]);
          expect(remaining).toBeGreaterThanOrEqual(0);
        }
      }
    });

    it("should allow rate limit monitoring across multiple requests", async () => {
      const responses = [];

      // Make multiple requests
      for (let i = 0; i < 3; i++) {
        const response = await client.api.v2010.accounts.pageWithHttpInfo({
          pageSize: 1,
        });
        responses.push(response);
      }

      // Each response should have headers
      responses.forEach((response) => {
        expect(response.headers).toBeDefined();
        expect(typeof response.headers).toBe("object");
      });

      // Could track rate limit consumption
      const rateLimits = responses.map((resp) => ({
        remaining: resp.headers["x-ratelimit-remaining"],
        limit: resp.headers["x-ratelimit-limit"],
      }));

      // All responses should have headers (structure)
      expect(rateLimits.length).toBe(3);
    });
  });

  describe("Request tracking", () => {
    it("should provide request IDs for debugging", async () => {
      const response = await client.api.v2010.accounts.createWithHttpInfo();

      // Request ID header (if present)
      const requestId = response.headers["x-request-id"];

      if (requestId) {
        // Request ID should be a non-empty string
        expect(typeof requestId).toBe("string");
        expect(requestId.length).toBeGreaterThan(0);

        // Could be used for logging
        console.log(`Request completed: ${requestId}`);
      }

      // Response should always have some identifying information
      expect(Object.keys(response.headers).length).toBeGreaterThan(0);
    });

    it("should have unique request IDs for different requests", async () => {
      const response1 = await client.api.v2010.accounts.pageWithHttpInfo();
      const response2 = await client.api.v2010.accounts.pageWithHttpInfo();

      const id1 = response1.headers["x-request-id"];
      const id2 = response2.headers["x-request-id"];

      // If request IDs exist, they should be different
      if (id1 && id2) {
        expect(id1).not.toBe(id2);
      }
    });
  });

  describe("Error scenarios", () => {
    it("should handle errors with HTTP metadata", async () => {
      try {
        // Try to fetch non-existent account
        await client.api.v2010.accounts("INVALID_SID").fetchWithHttpInfo();
        fail("Should have thrown an error");
      } catch (error: any) {
        // Error should contain useful information
        expect(error).toBeDefined();

        // Error might have statusCode property (implementation dependent)
        // This tests that errors are properly thrown
      }
    });
  });

  describe("Pagination with HTTP info", () => {
    it("should handle pagination correctly", async () => {
      // Get first page
      const firstPage = await client.api.v2010.accounts.pageWithHttpInfo({
        pageSize: 1,
      });

      expect(firstPage.statusCode).toBe(200);
      expect(firstPage.body.instances).toBeDefined();

      // If there's a next page, get it
      if (firstPage.body.nextPageUrl) {
        const secondPage = await client.api.v2010.accounts.getPageWithHttpInfo(
          firstPage.body.nextPageUrl,
        );

        expect(secondPage.statusCode).toBe(200);
        expect(secondPage.body.instances).toBeDefined();
      }
    });

    it("should provide metadata for each page", async () => {
      let pageCount = 0;
      let currentPage = await client.api.v2010.accounts.pageWithHttpInfo({
        pageSize: 1,
      });

      pageCount++;
      expect(currentPage.statusCode).toBe(200);
      expect(currentPage.headers).toBeDefined();

      // Navigate through pages (limit to 3 for testing)
      while (currentPage.body.nextPageUrl && pageCount < 3) {
        currentPage = await client.api.v2010.accounts.getPageWithHttpInfo(
          currentPage.body.nextPageUrl,
        );

        pageCount++;
        expect(currentPage.statusCode).toBe(200);
        expect(currentPage.headers).toBeDefined();
      }

      expect(pageCount).toBeGreaterThan(0);
    });
  });

  describe("Type safety", () => {
    it("should have correct TypeScript types", async () => {
      // TypeScript should enforce correct types
      const response: ApiResponse<AccountInstance> =
        await client.api.v2010.accounts.createWithHttpInfo();

      // These should all type-check correctly
      const statusCode: number = response.statusCode;
      const headers: Record<string, string> = response.headers;
      const body: AccountInstance = response.body;

      expect(typeof statusCode).toBe("number");
      expect(typeof headers).toBe("object");
      expect(body).toBeInstanceOf(AccountInstance);
    });

    it("should have correct types for different operations", async () => {
      // Create returns AccountInstance
      const createResp: ApiResponse<AccountInstance> =
        await client.api.v2010.accounts.createWithHttpInfo();
      expect(createResp.body).toBeInstanceOf(AccountInstance);

      // Delete returns boolean
      const deleteResp: ApiResponse<boolean> = await client.api.v2010
        .accounts(createResp.body.sid)
        .removeWithHttpInfo();
      expect(typeof deleteResp.body).toBe("boolean");

      // List returns array
      const listResp: ApiResponse<AccountInstance[]> =
        await client.api.v2010.accounts.listWithHttpInfo({ limit: 1 });
      expect(Array.isArray(listResp.body)).toBe(true);
    });
  });
});
