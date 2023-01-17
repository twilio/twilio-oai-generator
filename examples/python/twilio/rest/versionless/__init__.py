from twilio.base.domain import Domain
from twilio.rest.versionless.deployed_devices import DeployedDevices
from twilio.rest.versionless.understand import Understand


class Versionless(Domain):

    def __init__(self, twilio):
        super(Versionless, self).__init__(twilio)
        self.base_url = 'https://versionless.twilio.com'

        self._deployed_devices = None
        self._understand = None

    @property
    def deployed_devices(self):
        if self._deployed_devices is None:
            self._deployed_devices = DeployedDevices(self)
        return self._deployed_devices

    @property
    def understand(self):
        if self._understand is None:
            self._understand = Understand(self)
        return self._understand

    @property
    def fleets(self):
        return self.deployed_devices.fleets

    @property
    def assistants(self):
        return self.understand.assistants

    def __repr__(self):
        return '<Twilio.Versionless>'
