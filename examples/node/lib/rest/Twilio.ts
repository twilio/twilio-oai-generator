import {RequestOpts} from "../base/BaseTwilio";
import RequestClient from "../base/RequestClient";
import ApiBase from "./ApiBase";

class Twilio {
    get api(): ApiBase {
        return new ApiBase(this);
    }

    request(opts: RequestOpts) {
        return new RequestClient().request({
            method: opts.method,
            uri: opts.uri,
            params: opts.params,
            data: opts.data,
        });
    }
}

export = Twilio;
