require 'spec_helper.rb'
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

  it "can update Call Feedback summary" do
    endDate = "2020-12-31"
    startDate = "2020-01-01"
      @holodeck.mock(Twilio::Response.new(
            201,
          %q[
          {
              "include_subaccounts": false,
              "call_feedback_count": 729,
              "quality_score_standard_deviation": "1.0",
              "end_date": "2014-01-01",
              "quality_score_median": "4.0",
              "quality_score_average": "4.5",
              "date_updated": "Tue, 1 Feb 2023 20:36:28 +0000",
              "account_sid": "AC12345678123456781234567812345678",
              "status": "completed",
              "call_count": 10200,
              "sid": "FSaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
              "date_created": "Tue, 1 Jan 2023 20:36:28 +0000",
              "start_date": "2023-01-01",
              "issues": [
                  {
                      "count": 4,
                      "description": "issue description"
                  }
              ]
          }
          ]
        ))
    fetchedCallFeedback = @client.api.v2010.accounts(@account_sid).calls.feedback_summaries().create(start_date: Date.new(2022, 11, 2),end_date: Date.new(2023, 1, 2)
                                                                                                                        )
    expect(fetchedCallFeedback.issues[0]["description"]).to eq('issue description')
    expect(fetchedCallFeedback.issues[0]["count"]).to eq(4)
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
                                .calls.create(to: '+15558675310', from: '+15017122661')

      expect(createdCall).to_not eq(nil)
    end
end
