require 'spec_helper.rb'
describe 'Integration Test: flex api' do
  before(:each) do
    @account_sid = 'AC12345678123456781234567812345678'
    @auth_token = 'CR12345678123456781234567812345678'
  end
  it "can fetch aws credential" do
    @holodeck.mock(Twilio::Response.new(200, '
               {
                       "sid": "AC12345678123456781234567812345678"
               }'))
    createdFlex = @client.flex_api.v1.credentials.aws(@auth_token).fetch()
    expect(createdFlex).to_not eq(nil)
    expect(createdFlex.sid).to eq(@account_sid)
  end
end
