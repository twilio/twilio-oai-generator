'use strict';
import nock from 'nock';
import Twilio from '../../lib/rest/Twilio';

describe('credential', () => {
    const twilio = new Twilio();

    it('should update an aws credential', () => {
        const scope = nock('https://api.twilio.com')
            .post('/v2010/v1/Credentials/AWS/123')
            .reply(200, {sid: '123'});

        return twilio.api.v2010.credential.aws('123').update(() => scope.done());
    });
});
