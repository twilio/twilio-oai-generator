import { Client, ClientOpts } from "../base/BaseTwilio";
import ApiBase from "./ApiBase";
import FlexApiBase from "./FlexApiBase";
import VersionlessBase from "./VersionlessBase";
import PreviewIamBase = require("./PreviewIamBase");
import OauthBase = require("./OauthBase");

class Twilio extends Client {
  constructor(username?: string, password?: string, opts?: ClientOpts) {
    super(username, password, opts);
  }

  get api(): ApiBase {
    return new ApiBase(this);
  }

  get flexApi(): FlexApiBase {
    return new FlexApiBase(this);
  }

  get versionless(): VersionlessBase {
    return new VersionlessBase(this);
  }

  get previewIam(): PreviewIamBase {
    return new PreviewIamBase(this);
  }

  get oauth(): OauthBase {
    return new OauthBase(this);
  }
}

export = Twilio;
