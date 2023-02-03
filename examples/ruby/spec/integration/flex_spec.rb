require 'spec_helper.rb'
describe 'Integration Test' do
   before(:each) do
        @account_sid = 'AC12345678123456781234567812345678'
        @auth_token = 'CR12345678123456781234567812345678'
   end

  it "can reac versionless domain" do
    @holodeck.mock(Twilio::Response.new(200, '
            {
                    "sid": "AC12345678123456781234567812345678",
                    friendlyName: "foo friend"
            }'))
    expect{ @client.versionless.understand.assistants.read()}
    .to raise_exception(Twilio::REST::TwilioError)
    expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://versionless.twilio.com/understand/Assistants',
        ))).to eq(true)
    expect(readCallToVersionless).to_not eq(nil)
    expect(readCallToVersionless.sid).to eq('AC12345678123456781234567812345678')
  end
end
