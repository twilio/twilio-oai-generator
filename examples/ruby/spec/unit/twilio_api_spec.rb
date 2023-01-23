require 'spec_helper.rb'

describe 'Account' do
    it "can read"
        @holodeck.mock(Twilio::Response).new(500,''))

        expect{
            @client.api.v2010.accounts().list()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'get',
                url: 'https://api.twilio.com/2010-04-01/Accounts.json',
            ))).to eq(true)
        )
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

        @expect{
            @client.api.v2010.accounts.create()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: "post",
                url: "https://api.twilio.com/2010-04-01/Accounts.json"
            )).to eq(true)
        )
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

    if "can delete" do
        @holodeck.mock(Twilio::Response.new(500, ''))
        expect {
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').delete()
        }.to raise_exception(Twilio::Rest::TwilioError)

         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'delete',
                url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json'
            ))).to eq(true)
    end

    it "receives delete responses" do
        @holodeck.mock(Twilio::Response.new(
            204,
            nil
        ))

        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').delete()
        expect(actual).to eq(true)
    end

    it "can fetch" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        }.to raise_exception(Twilio::Rest::TwilioError)

        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json'
        ))).to eq(true)
    end

    it "receives fetch responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CR12345678123456781234567812345678",
                "test_string": "Ahoy",
                "test_enum": "completed",
                "a2p_profile_bundle_sid": "BU0987654321abcdefABCDEFABCDEFABCD"
            }
        ]))

        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        expect(actual).to_not eq(nil)
    end

    it "can update" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect {
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        }.to raise_exception(Twilio::Rest::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'post',
                url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json'
            ))).to eq(true)
    end

    it "receives update responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CR12345678123456781234567812345678",
                "test_string": "Ahoy",
                "test_enum": "completed",
                 "a2p_profile_bundle_sid": "BU0987654321abcdefABCDEFABCDEFABCD"
            }
        ]))
        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        expect(actual).to_not eq(nil)
    end
end

describe "Call" do
    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))

        @expect{
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.create(
            required_string_property:"test string", test_method: "get")
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: "post",
                url: "https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls.json"
            )).to eq(true)
        )
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

    if "can delete" do
        @holodeck.mock(Twilio::Response.new(500, ''))
        expect {
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls(123).delete()
        }.to raise_exception(Twilio::Rest::TwilioError)

         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'delete',
                url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls/123.json'
            ))).to eq(true)
    end

    it "receives delete responses" do
        @holodeck.mock(Twilio::Response.new(
            204,
            nil
        ))

        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls(123).delete()
        expect(actual).to eq(true)
    end
    it "can fetch" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls(123).fetch()
        }.to raise_exception(Twilio::Rest::TwilioError)

        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls/123.json'
        ))).to eq(true)
    end

    it "receives fetch responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CR12345678123456781234567812345678",
                "test_string": "Ahoy",
                "test_enum": "completed",
                "a2p_profile_bundle_sid": "BU0987654321abcdefABCDEFABCDEFABCD"
            }
        ]))

        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls(123).fetch()
        expect(actual).to_not eq(nil)
    end
end

describe "FeedBackSummary" do
    it "can update" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect {
            @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.feedback_summaries("TESTSID").update()
        }.to raise_exception(Twilio::Rest::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'post',
                url: 'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Calls/Feedback/Summary/TESTSID.json'
            ))).to eq(true)
    end

    it "receives update responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CR12345678123456781234567812345678",
                "test_string": "Ahoy",
                "test_enum": "completed",
                 "a2p_profile_bundle_sid": "BU0987654321abcdefABCDEFABCDEFABCD"
            }
        ]))
        actual = @client.api.v2010.accounts('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').calls.feedback_summaries("TESTSID").update()
        expect(actual).to_not eq(nil)
    end
end
