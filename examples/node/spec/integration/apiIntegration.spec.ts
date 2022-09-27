'use strict'
import Twilio from '../../lib/rest/Twilio';
import {AccountListInstancePageOptions} from "../../lib/rest/api/v2010/account";

describe('', () => {
    beforeEach( () => {
        console.log("Before Each called :)");
        });
    it('should page an account', (done) => {

        // TODO: Move all of this to beforeEach
        const accountSid:string = 'AC12345678123456781234567812345678';
        const authToken:string = 'CR12345678123456781234567812345678';
        // TODO: Need to change baseUrl from https://api.twilio.com to http://prism_twilio:4010
        // Options: jasmine spy or create custom Twilio test object
        const client:Twilio = new Twilio(accountSid, authToken);
        // TODO: Determine why spyOn method isn't available
        // const propertySpy = spyOn(client.api, "baseUrl").and.returnValue('http://prism_twilio:4010');

        const params: AccountListInstancePageOptions =  {
            dateCreated: new Date(),
            dateTest: '',
            dateCreatedBefore: new Date(),
            dateCreatedAfter: new Date(),
            pageSize: 1,
        };

        const promise = client.api.v2010.accounts.page(params);
        promise.then((response) => {
            console.log(response);
            expect(response).toBeDefined();
            expect(response.accountSid).toEqual(accountSid);
            expect(response.testString).toEqual('Ahoy');
            done();
        }, () => {
            throw new Error('failed');
        }).done();
    })
})
