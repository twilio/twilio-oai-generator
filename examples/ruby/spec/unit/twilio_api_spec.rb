require 'spec_helper.rb'

describe 'Account' do
    it "can read" do
        @holodeck.mock(Twilio::Response.new(500,''))

        expect{
            @client.api.v2010.accounts.list()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'get',
                url: 'https://api.twilio.com/2010-04-01/Accounts.json',
            ))).to eq(true)
    end

    it "receives read_empty responses" do
        @holodeck.mock(Twilio::Response.new(
            200,
            %q[
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
            ]
        ))

        actual = @client.api.v2010.accounts.list()
        expect(actual).to_not eq(nil)
    end

    it "receives valid full response" do
        @holodeck.mock(Twilio::Response.new(
            200,
            %q[
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
            ]
        ))

        actual = @client.api.v2010.accounts.list()
        expect(actual).to_not eq(nil)
    end

    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))

        expect{
            @client.api.v2010.accounts.create()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: "post",
                url: "https://api.twilio.com/2010-04-01/Accounts.json"
            ))).to eq(true)
    end

    it "recieves create responses" do
        @holodeck.mock(Twilio::Response.new(
        201,
        %q[
            {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }
        ]
        ))

        actual = @client.api.v2010.accounts.create()
        expect(actual).to_not eq(nil)
    end
end

describe "Call" do
    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.create(
            required_string_property:"test string", test_method: "get")
        }.to raise_exception(Twilio::REST::TwilioError)

        data = {}
        data["RequiredStringProperty"] = "test string"
        data["TestMethod"] = "get"
        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'POST',
                url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls.json',
                auth: [@client.username, @client.password],
                headers: @client.generate_headers('POST'),
                data: data

            ))).to eq(true)

    end

    it "recieves create responses" do
    #params = ['RequiredStringProperty' => 'example', 'TestMethod' => 'example']
            @holodeck.mock(Twilio::Response.new(
            201,
            %q[
                {
                    "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    "test_string": "Test String"
                }
            ]
            ))

            actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.create(
                                 required_string_property:"test string", test_method: "get")
            expect(actual).to_not eq(nil)
    end
=begin
    it "should serialize date time" do
        @holodeck.mock(Twilio::Response.new(500, ''))
        expect {
          @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX') \
                           .calls('FSaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa') \
                           .feedback_call_summary() \
                           .update(start_date: Date.new(2008, 1, 2), end_date: Date.new(2008, 1, 2))
        }.to raise_exception(Twilio::REST::TwilioError)

        values = {
            'StartDate' => Twilio.serialize_iso8601_date(Date.new(2008, 1, 2)),
            'EndDate' => Twilio.serialize_iso8601_date(Date.new(2008, 1, 2)),
        }
        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'post',
            url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls/Feedback/Summary/FSaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.json',
            data: values,
        ))).to eq(true)
    end
=end
end
