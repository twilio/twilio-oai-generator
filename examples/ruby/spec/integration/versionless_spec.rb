require 'spec_helper.rb'
describe 'Integration Test' do
   before(:each) do
        @account_sid = 'AC12345678123456781234567812345678'
        @auth_token = 'CR12345678123456781234567812345678'
   end

  it "can read call for versionless domain" do
    @holodeck.mock(Twilio::Response.new(200, '
            {
            "sid": "123",
            "friendlyName": "Friendly Assistant"
            }
            '))

    expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://versionless.twilio.com/understand/Assistants',
        ))).to eq(true)
    readCallToVersionless = @client.versionless.understand.assistants.read()
    expect(readCallToVersionless).to_not eq(nil)
    expect(readCallToVersionless.sid).to eq('AC12345678123456781234567812345678')
  end

  it "can make create call to Deployed Devices" do
  @holodeck.mock(Twilio::Response.new(200,'
            {
           "name":"Fleet Name",
           "sid": "123",
          "friendlyName": "Friendly Fleet "
           }
           '))
  createDeployedDevices = @client.versionless.deployedDevices.fleets.create()
  expect(createDeployedDevices.sid).to eq('123')
  expect(createDeployedDevices.name).to eq('Fleet Name')
  end
end
