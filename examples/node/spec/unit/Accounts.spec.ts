'use strict';
import nock from 'nock';
import Twilio from '../../lib/rest/Twilio';

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
});

describe('calls', () => {
    const twilio = new Twilio();

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

});
