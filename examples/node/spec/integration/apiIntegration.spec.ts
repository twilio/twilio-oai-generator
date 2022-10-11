'use strict'
import Twilio from '../../lib/rest/Twilio';
import {AccountListInstanceCreateOptions, AccountListInstancePageOptions} from "../../lib/rest/api/v2010/account";
import TestRequestClient from './TestRequestClient';
import {CallListInstanceCreateOptions} from "../../lib/rest/api/v2010/account/call";
import {FeedbackCallSummaryContextUpdateOptions} from "../../lib/rest/api/v2010/account/call/feedbackCallSummary";

describe('Integration tests', () => {
    const accountSid:string = 'AC12345678123456781234567812345678';
    const authToken:string = 'CR12345678123456781234567812345678';
    const testRequestClient: TestRequestClient = new TestRequestClient();
    const client = new Twilio(accountSid, authToken, {httpClient: testRequestClient});

    // Throws error: "Page Records can not be deserialized"
    xit('should get an account', async () => {
        const params = {};
        const result = await client.api.v2010.accounts.page();
        expect(result.end).toEqual(5);
        expect(result.first_page_uri).toEqual("http://example.com/page1");
    })

    it('should create an account', async () => {
        const result = await client.api.v2010.accounts.create();
        expect(result.sid).toEqual("CR12345678123456781234567812345678");
        expect(result.testString).toEqual("Ahoy");
    })

    it('should create a call ', async () => {
        const params: CallListInstanceCreateOptions = {requiredStringProperty: "str"};
        const result = await client.api.v2010.accounts(accountSid).calls.create(params);
        expect(result.sid).toEqual("CR12345678123456781234567812345678");
        expect(result.testString).toEqual("Ahoy");
    })

    it('should fetch a call', async () => {
        const result = await client.api.v2010.accounts(accountSid).calls(123).fetch();
        expect(result.sid).toEqual("CR12345678123456781234567812345678");
        expect(result.testString).toEqual("Ahoy");
    })

    // Not working
    it('should update call feedback summary', async () => {
        const params: FeedbackCallSummaryContextUpdateOptions = {endDate: "strDate" , startDate: "strDate"};
        const result = await client.api.v2010.accounts(accountSid).calls.feedback_call_summary("CR12345678123456781234567812345678").update(params);
        expect(result.testArrayOfObjects[0].description).toEqual("issue description");
        expect(result.testArrayOfObjects[0].count).toEqual(4);
    })

})
