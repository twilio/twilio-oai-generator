'use strict';
import nock from 'nock';
import Twilio from '../../lib/rest/Twilio';
import {AccountListInstancePageOptions} from "../../lib/rest/api/v2010/accounts";

describe('accounts', () => {
    const twilio = new Twilio();

    it('should create an account', () => {
        const scope = nock('https://api.twilio.com')
            .post('/v2010/2010-04-01/Accounts.json')
            .reply(201, {account_sid: '123'});

        return twilio.api.v2010.accounts.create({sid: '123'}).then(() => scope.done());
    });

    it('should fetch an account', () => {
        const scope = nock('https://api.twilio.com')
            .get('/v2010/2010-04-01/Accounts/123.json')
            .reply(200, {account_sid: '123'});

        return twilio.api.v2010.accounts('123').fetch().then(() => scope.done());
    });

    it('should update an account', () => {
        const scope = nock('https://api.twilio.com')
            .post('/v2010/2010-04-01/Accounts/123.json')
            .reply(200, {account_sid: '123', status: 'closed'});

        return twilio.api.v2010.accounts('123').update({status: 'closed'}).then(() => scope.done());
    });

    it('should remove an account', () => {
        const scope = nock('https://api.twilio.com')
            .delete('/v2010/2010-04-01/Accounts/123.json')
            .reply(204);

        return twilio.api.v2010.accounts('123').remove().then(() => scope.done());
    });

    it('should get account pages in between dates', () => {
        const scope = nock('https://api.twilio.com')
            .get('/v2010/2010-04-01/Accounts.json', 'DateCreated%3C=2022-12-25T00%3A00%3A00.000Z&DateCreated%3E=2022-01-01T00%3A00%3A00.000Z')
            .reply(200, {});
        const params: AccountListInstancePageOptions = {dateCreatedBefore: new Date('2022-12-25'), dateCreatedAfter: new Date('2022-01-01')}
        return twilio.api.v2010.accounts.page(params).then(() => scope.done());
    })
});


describe('calls', () => {
    const twilio = new Twilio();

    it('should create a call', () => {
        const scope = nock('https://api.twilio.com')
            .post('/v2010/2010-04-01/Accounts/123/Calls.json')
            .reply(201, {requiredStringProperty: 'radda radda', account_sid: '123', sid: 1});

        return twilio.api.v2010.accounts('123').calls.create({requiredStringProperty: 'radda radda',
            account_sid: '123', sid: 1})
            .then(() => scope.done());
    });

    it('should fetch a call', () => {
        const scope = nock('https://api.twilio.com')
            .get('/v2010/2010-04-01/Accounts/123/Calls/1.json')
            .reply(307, {account_sid: '123', sid: 1});

        return twilio.api.v2010.accounts('123').calls('123', 1).fetch()
            .then(() => scope.done());
    });

    it('should remove a call', () => {
        const scope = nock('https://api.twilio.com')
            .delete('/v2010/2010-04-01/Accounts/123/Calls/1.json')
            .reply(204);

        return twilio.api.v2010.accounts('123').calls('123', 1).remove()
            .then(() => scope.done());
    });

    it('should create a feedback summary', () => {
        const scope = nock('https://api.twilio.com')
            .post('/v2010/2010-04-01/Accounts/123/Calls/FeedbackSummary.json')
            .reply(201, {test_array: [{count: 4}]});

        return twilio.api.v2010.accounts('123').calls.feedback_summary
            .create({endDate: '2022-08-01', startDate: '2022-08-01'})
            .then(() => scope.done())
    });

});

