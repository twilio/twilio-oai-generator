import { Client, ClientOpts } from "../base/BaseTwilio";
import ApiBase from "./ApiBase";
import FlexApiBase from "./FlexApiBase";
import VersionlessBase from "./VersionlessBase";
import AccountsBase from "./AccountsBase";

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

  get accounts(): AccountsBase {
    return new AccountsBase(this);
  }
}

export = Twilio;
