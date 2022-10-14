import Domain from "../base/Domain";
import V1 from "./flexApi/V1";

class FlexApiBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "http://flex-api.twilio.com");
  }

  get v1(): V1 {
    return new V1(this);
  }
}

export = FlexApiBase;
