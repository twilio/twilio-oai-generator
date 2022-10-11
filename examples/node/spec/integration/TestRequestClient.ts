import RequestClient from '../../lib/base/RequestClient';

export default class TestRequestClient extends RequestClient {
    constructor() {
        super();
    }

    request(opts: object) {
        // Replace https with http in the uri
        console.log("Current opts " + opts);
        opts.uri = opts.uri.replace("https", "http");
        console.log("Utilizing TestRequestClient with uri = " + opts.uri);
        return super.request(opts);
    }
}
