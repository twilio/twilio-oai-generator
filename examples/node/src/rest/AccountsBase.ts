import Domain from "../base/Domain";
import V1 from "./accounts/V1";

class AccountsBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "http://accounts.twilio.com");
  }

  get v1(): V1 {
    return new V1(this);
  }
}

export = AccountsBase;
