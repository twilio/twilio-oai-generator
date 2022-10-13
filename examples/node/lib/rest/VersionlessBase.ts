import Domain from "../base/Domain";
import DeployedDevices from "./versionless/DeployedDevices";

class VersionlessBase extends Domain {
  constructor(twilio: any) {
    super(twilio, "http://versionless.twilio.com");
  }

  get deployed_devices(): DeployedDevices {
    return new DeployedDevices(this);
  }
}

export = VersionlessBase;
