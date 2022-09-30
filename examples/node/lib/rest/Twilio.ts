import { RequestOpts } from "../base/BaseTwilio";
import RequestClient from "../base/RequestClient";
import ApiBase from "./ApiBase";
import FlexApiBase from "./FlexApiBase";

class Twilio {
  get api(): ApiBase {
    return new ApiBase(this);
  }

  get flexApi(): FlexApiBase {
    return new FlexApiBase(this);
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
