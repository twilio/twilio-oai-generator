import Domain from '../base/Domain';
import V2010 from './api/V2010';

class ApiBase extends Domain {
    constructor(twilio: any) {
        super(twilio, 'https://api.twilio.com');
    }

    get v2010(): V2010 {
        return new V2010(this);
    }
}

export = ApiBase;
