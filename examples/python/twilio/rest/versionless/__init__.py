from twilio.base.domain import Domain
from twilio.rest.versionless.deployed_devices import DeployedDevices


class Preview(Domain):
    def __init__(self, twilio):
        super().__init__(twilio)
        self.base_url = "http://preview.twilio.com"
        self._deployed_devices = None

    @property
    def deployed_devices(self):
        if self._deployed_devices is None:
            self._deployed_devices = DeployedDevices(self)
        return self._deployed_devices
