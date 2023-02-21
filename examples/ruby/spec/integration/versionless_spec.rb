require 'spec_helper.rb'
describe 'Integration Test' do
  before(:each) do
    @account_sid = 'AC12345678123456781234567812345678'
    @auth_token = 'CR12345678123456781234567812345678'
  end

  it "can make create call to Deployed Devices" do
    @holodeck.mock(Twilio::Response.new(200, '
            {
           "name":"Fleet Name",
           "sid": "AC12345678123456781234567812345678",
          "friendlyName": "Friendly Fleet "
           }
           '))
    createDeployedDevices = @client.versionless.deployed_devices.fleets.create()
    expect(createDeployedDevices.sid).to eq(@account_sid)
    expect(createDeployedDevices.name).to eq('Fleet Name')
  end
end
