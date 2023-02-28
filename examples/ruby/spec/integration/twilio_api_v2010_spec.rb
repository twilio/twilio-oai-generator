describe 'Integration Test' do
  before(:each) do
    @account_sid = 'AC12345678123456781234567812345678'
    @auth_token = 'CR12345678123456781234567812345678'
  end

  it "can create" do
    @holodeck.mock(Twilio::Response.new(200, '
            {
                    "sid": "AC12345678123456781234567812345678"
            }'))
    createdAccount = @client.api.v2010.accounts.create()
    expect(createdAccount).to_not eq(nil)
    expect(createdAccount.sid).to eq('AC12345678123456781234567812345678')
  end

  it "can fetch call" do
    @holodeck.mock(Twilio::Response.new(200, '
                {
                        "sid": "AC12345678123456781234567812345678"
                }'))
    fetchedCall = @client.api.v2010.accounts(@account_sid).calls(123).fetch()
    expect(fetchedCall).to_not eq(nil)
    expect(fetchedCall.sid).to eq('AC12345678123456781234567812345678')
  end

  it "can create call" do
    @holodeck.mock(Twilio::Response.new(
                     201,
                     %q[
        {
            "account_sid": "AC12345678123456781234567812345678",
            "from": "+14158675308",
            "from_formatted": "(415) 867-5308",
            "group_sid": null,
            "parent_call_sid": null,
            "phone_number_sid": "PNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        }
        ]
                   ))

    createdCall = @client.api.v2010.accounts('AC12345678123456781234567812345678') \
                         .calls.create(required_string_property: 'GET')

    expect(createdCall).to_not eq(nil)
  end

  it "can update" do
    @holodeck.mock(Twilio::Response.new(200, '{
              "account_sid": "AC12345678123456781234567812345678",
              "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
              "test_string": "Test String"
             }'))

    result = @client.api.v2010.accounts(@account_sid).update()
    expect(result).to_not eq(nil)
    expect(result.test_string).to eq("Test String")
  end

  it "can delete call " do
    @holodeck.mock(Twilio::Response.new(204, nil))
    deleteCall = @client.api.v2010.account.calls(123).delete()
    expect(deleteCall).to eq(true)
  end
end
