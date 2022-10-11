import { BaseTwilio, RequestOpts, ClientOpts } from "../base/BaseTwilio";
import RequestClient from "../base/RequestClient";
import ApiBase from "./ApiBase";
import FlexApiBase from "./FlexApiBase";

class Twilio extends BaseTwilio {

  constructor(username?: string, password?: string, opts?: ClientOpts) {
    super(username, password, opts);
  }

  get api(): ApiBase {
    return new ApiBase(this);
  }

  get flexApi(): FlexApiBase {
    return new FlexApiBase(this);
  }

}

export = Twilio;
