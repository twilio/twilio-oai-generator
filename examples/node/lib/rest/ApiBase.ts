import Domain from "../base/Domain";
import V2010 from "./api/V2010";

// TODO: CHANGE BASE BACK
class ApiBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "http://api.twilio.com");
    // super(twilio, "http://prism_twilio:4010");

  }

  get v2010(): V2010 {
    return new V2010(this);
  }
}

export = ApiBase;
