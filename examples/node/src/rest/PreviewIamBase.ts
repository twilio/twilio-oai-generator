import Domain from "../base/Domain";
import V1 from "./previewIam/V1";

class PreviewIamBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "https://preview-iam.twilio.com");
  }

  get v1(): V1 {
    return new V1(this);
  }
}

export = PreviewIamBase;
