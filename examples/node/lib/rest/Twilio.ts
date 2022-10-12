import { RequestOpts } from "../base/BaseTwilio";
import RequestClient from "../base/RequestClient";
import ApiBase from "./ApiBase";
import FlexApiBase from "./FlexApiBase";
import VersionlessBase from "./VersionlessBase";

class Twilio {
  get api(): ApiBase {
    return new ApiBase(this);
  }

  get flexApi(): FlexApiBase {
    return new FlexApiBase(this);
  }

  get versionless(): VersionlessBase {
    return new VersionlessBase(this);
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
