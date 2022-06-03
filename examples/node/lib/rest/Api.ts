import Domain = require('../base/Domain');

export default class Api extends Domain {
    constructor(twilio) {
        super(twilio, 'https://api.twilio.com');
    }
}
