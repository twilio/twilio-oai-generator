import RequestClient from '../../lib/base/RequestClient';

export default class TestRequestClient extends RequestClient {
    constructor() {
        super();
    }

    request(opts: object) {
        opts.uri = opts.uri.replace("https", "http");
        return super.request(opts);
    }
}
