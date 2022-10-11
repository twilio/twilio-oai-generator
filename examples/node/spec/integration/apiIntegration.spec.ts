'use strict'
import Twilio from '../../lib/rest/Twilio';
import {AccountListInstanceCreateOptions, AccountListInstancePageOptions} from "../../lib/rest/api/v2010/account";

// // TODO: Need to change baseUrl from https://api.twilio.com to http://api.twilio.com (change scheme to http)
// //  Options: jasmine spy or create custom Twilio test object
describe('Integration tests', () => {
    const accountSid:string = 'AC12345678123456781234567812345678';
    const authToken:string = 'CR12345678123456781234567812345678';
    const client = new Twilio(accountSid, authToken);

    // Not working
    xit('should get an account', async () => {
        const params = {};
        const result = await client.api.v2010.accounts.page();
    })

    it('should create an account', async () => {
        const result = await client.api.v2010.accounts.create();
        expect(result.sid).toEqual("CR12345678123456781234567812345678");
        expect(result.testString).toEqual("Ahoy");
    })

    // Not working
    it('should create a call ', async () => {
        const params = {};
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
        const params = {};
        const result = await client.api.v2010.accounts(accountSid).calls.feedback_call_summary("CR12345678123456781234567812345678").update(params);
        expect(result.testArrayOfObjects[0].description).toEqual("issue description")
    })

})
