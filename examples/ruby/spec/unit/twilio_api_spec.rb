require 'spec_helper'

describe 'Account' do
  it 'can read' do
    @holodeck.mock(Twilio::Response.new(500, ''))

    expect do
      @client.api.v2010.accounts.list
    end.to raise_exception(Twilio::REST::TwilioError)

    expect(
      @holodeck.has_request?(Holodeck::Request.new(
                               method: 'get',
                               url: 'https://api.twilio.com/2010-04-01/Accounts.json'
                             ))
    ).to eq(true)
  end

  it 'receives read_empty responses' do
    @holodeck.mock(Twilio::Response.new(
                     200,
                     '
                  {
                      "first_page_uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                      "end": 0,
                      "previous_page_uri": null,
                      "accounts": [],
                      "uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                      "page_size": 50,
                      "start": 0,
                      "next_page_uri": null,
                      "page": 0
                  }
            '
                   ))

    actual = @client.api.v2010.accounts.list
    expect(actual).to_not eq(nil)
  end

  it 'receives valid full response' do
    @holodeck.mock(Twilio::Response.new(
                     200,
                     '
             {
                "first_page_uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                "end": 0,
                "previous_page_uri": null,
                "accounts": [
                    {
                        "account_sid": "AXC123456789123456789",
                        "test_string": "Ahoy"
                    },
                    {
                        "account_sid": "AXC321456789123456789",
                        "test_string": "Matey"
                    }
                ],
                "uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
             }
            '
                   ))

    actual = @client.api.v2010.accounts.list
    expect(actual).to_not eq(nil)
  end

  it 'can create' do
    @holodeck.mock(Twilio::Response.new(500, ''))

    expect do
      @client.api.v2010.accounts.create
    end.to raise_exception(Twilio::REST::TwilioError)

    expect(
      @holodeck.has_request?(Holodeck::Request.new(
                               method: 'post',
                               url: 'https://api.twilio.com/2010-04-01/Accounts.json'
                             ))
    ).to eq(true)
  end

  it 'use dependents to test' do
    @holodeck.mock(Twilio::Response.new(
                     200,
                     %q[
          {
              "calls": [
                  {
                      "account_sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                      "annotation": "billingreferencetag1",
                      "answered_by": "machine_start",
                      "api_version": "2010-04-01",
                      "caller_name": "callerid1",
                      "date_created": "Fri, 18 Oct 2019 17:00:00 +0000",
                      "date_updated": "Fri, 18 Oct 2019 17:01:00 +0000",
                      "direction": "outbound-api",
                      "duration": "4",
                      "end_time": "Fri, 18 Oct 2019 17:03:00 +0000",
                      "forwarded_from": "calledvia1",
                      "from": "+13051416799",
                      "from_formatted": "(305) 141-6799",
                      "group_sid": "GPdeadbeefdeadbeefdeadbeefdeadbeef",
                      "parent_call_sid": "CAdeadbeefdeadbeefdeadbeefdeadbeef",
                      "phone_number_sid": "PNdeadbeefdeadbeefdeadbeefdeadbeef",
                      "price": "-0.200",
                      "price_unit": "USD",
                      "sid": "CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                      "start_time": "Fri, 18 Oct 2019 17:02:00 +0000",
                      "status": "completed",
                      "subresource_uris": {
                          "feedback": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Feedback.json",
                          "feedback_summaries": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/FeedbackSummary.json",
                          "notifications": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Notifications.json",
                          "recordings": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Recordings.json",
                          "payments": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Payments.json",
                          "events": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Events.json",
                          "siprec": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Siprec.json",
                          "streams": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Streams.json",
                          "user_defined_message_subscriptions": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/UserDefinedMessageSubscriptions.json",
                          "user_defined_messages": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/UserDefinedMessages.json"
                      },
                      "to": "+13051913581",
                      "to_formatted": "(305) 191-3581",
                      "trunk_sid": "TKdeadbeefdeadbeefdeadbeefdeadbeef",
                      "uri": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.json",
                      "queue_time": "1000"
                  },
                  {
                      "account_sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                      "annotation": "billingreferencetag2",
                      "answered_by": "human",
                      "api_version": "2010-04-01",
                      "caller_name": "callerid2",
                      "date_created": "Fri, 18 Oct 2019 16:00:00 +0000",
                      "date_updated": "Fri, 18 Oct 2019 16:01:00 +0000",
                      "direction": "inbound",
                      "duration": "3",
                      "end_time": "Fri, 18 Oct 2019 16:03:00 +0000",
                      "forwarded_from": "calledvia2",
                      "from": "+13051416798",
                      "from_formatted": "(305) 141-6798",
                      "group_sid": "GPdeadbeefdeadbeefdeadbeefdeadbeee",
                      "parent_call_sid": "CAdeadbeefdeadbeefdeadbeefdeadbeee",
                      "phone_number_sid": "PNdeadbeefdeadbeefdeadbeefdeadbeee",
                      "price": "-0.100",
                      "price_unit": "JPY",
                      "sid": "CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0",
                      "start_time": "Fri, 18 Oct 2019 16:02:00 +0000",
                      "status": "completed",
                      "subresource_uris": {
                          "feedback": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Feedback.json",
                          "feedback_summaries": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/FeedbackSummary.json",
                          "notifications": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Notifications.json",
                          "recordings": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Recordings.json",
                          "payments": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Payments.json",
                          "events": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Events.json",
                          "siprec": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Siprec.json",
                          "streams": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/Streams.json",
                          "user_defined_message_subscriptions": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/UserDefinedMessageSubscriptions.json",
                          "user_defined_messages": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0/UserDefinedMessages.json"
                      },
                      "to": "+13051913580",
                      "to_formatted": "(305) 191-3580",
                      "trunk_sid": "TKdeadbeefdeadbeefdeadbeefdeadbeef",
                      "uri": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls/CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0.json",
                      "queue_time": "1000"
                  }
              ],
              "end": 1,
              "first_page_uri": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls.json?Status=completed&To=%2B123456789&From=%2B987654321&StartTime=2008-01-02&ParentCallSid=CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa&EndTime=2009-01-02&PageSize=2&Page=0",
              "next_page_uri": null,
              "page": 0,
              "page_size": 2,
              "previous_page_uri": null,
              "start": 0,
              "uri": "/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls.json?Status=completed&To=%2B123456789&From=%2B987654321&StartTime=2008-01-02&ParentCallSid=CAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa&EndTime=2009-01-02&PageSize=2&Page=0"
          }
          ]
                   ))

    request = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX') \
                     .calls()
    expect(request).to_not eq(nil)
  end

  it 'recieves create responses' do
    @holodeck.mock(Twilio::Response.new(
                     201,
                     '
            {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }
        '
                   ))

    actual = @client.api.v2010.accounts.create
    expect(actual).to_not eq(nil)
  end
end

describe 'Call' do
  it 'can create' do
    @holodeck.mock(Twilio::Response.new(500, ''))
    expect do
      @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.create(
        required_string_property: 'test string', test_method: 'get'
      )
    end.to raise_exception(Twilio::REST::TwilioError)

    data = {}
    data['RequiredStringProperty'] = 'test string'
    data['TestMethod'] = 'POST'
    request = Holodeck::Request.new(
      method: 'POST',
      url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls.json',
      auth: [@client.username, @client.password],
      headers: @client.generate_headers('POST'),
      data: data
    )
    response = expect(@holodeck.has_request?(request)).to_not eq(nil)
  end

  it 'recieves create responses' do
    # params = ['RequiredStringProperty' => 'example', 'TestMethod' => 'example']
    @holodeck.mock(Twilio::Response.new(
                     201,
                     '
                {
                    "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    "test_string": "Test String"
                }
            '
                   ))

    actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.create(
      required_string_property: 'test string', test_method: 'get'
    )
    expect(actual).to_not eq(nil)
  end
  #     it "should serialize date time" do
  #         @holodeck.mock(Twilio::Response.new(500, ''))
  #         expect {
  #           @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX') \
  #                            .calls('FSaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa') \
  #                            .feedback_call_summary() \
  #                            .update(start_date: Date.new(2008, 1, 2), end_date: Date.new(2008, 1, 2))
  #         }.to raise_exception(Twilio::REST::TwilioError)
  #
  #         values = {
  #             'StartDate' => Twilio.serialize_iso8601_date(Date.new(2008, 1, 2)),
  #             'EndDate' => Twilio.serialize_iso8601_date(Date.new(2008, 1, 2)),
  #         }
  #         expect(
  #         @holodeck.has_request?(Holodeck::Request.new(
  #             method: 'post',
  #             url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls/Feedback/Summary/FSaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.json',
  #             data: values,
  #         ))).to eq(true)
  #     end
end
