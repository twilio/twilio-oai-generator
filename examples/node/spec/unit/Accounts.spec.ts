'use strict';
import nock from 'nock';
import Twilio from '../../lib/rest/Twilio';

describe('accounts', () => {
    const twilio = new Twilio();

    it('should fetch an account', () => {
        const scope = nock('https://api.twilio.com')
            .get('/v2010/2010-04-01/Accounts/123.json')
            .reply(200, {account_sid: '123'});

        return twilio.api.v2010.accounts('123').fetch().then(() => scope.done());
    });
});
