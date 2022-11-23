import Domain from "../base/Domain";
import DeployedDevices from "./versionless/DeployedDevices";
import Understand from "./versionless/Understand";

class VersionlessBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "http://versionless.twilio.com");
  }

  get deployed_devices(): DeployedDevices {
    return new DeployedDevices(this);
  }

  get understand(): Understand {
    return new Understand(this);
  }
}

export = VersionlessBase;
