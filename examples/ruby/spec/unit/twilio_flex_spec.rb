require 'spec_helper.rb'

describe "Aws" do
    it "can read" do
        @holodeck.mock(Twilio::Response.new(500,''))

        expect{
            @client.flex_api.v1.credentials.aws.list()
        }.to raise_exception(Twilio::REST::TwilioError)
         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'get',
                url: 'https://flex-api.twilio.com/v1/Credentials/AWS',
            ))).to eq(true)
    end

    it "receives valid full response" do
            @holodeck.mock(Twilio::Response.new(
                200,
                %q[
                 {
                    "meta": {
                        "first_page_uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                        "end": 0,
                        "previous_page_uri": null,
                        "key": "credentials",
                        "uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                        "page_size": 50,
                        "start": 0,
                        "next_page_uri": null,
                        "page": 0
                    },
                    "credentials": [{
                        "sid": "CR12345678123456781234567812345678",
                        "test_string": "Ahoy"
                    }]
                 }
                ]
            ))

            actual = @client.flex_api.v1.credentials.aws.list()
            expect(actual).to_not eq(nil)
    end

    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))

        expect{
            @client.flex_api.v1.credentials.new_credentials.create(test_string: "some string")
        }.to raise_exception(Twilio::REST::TwilioError)
        data = {}
        data["TestString"] = "some string"
       expect(
           @holodeck.has_request?(Holodeck::Request.new(
               method: "POST",
               url: "https://flex-api.twilio.com/v1/Credentials/AWS",
               auth: [@client.username, @client.password],
               data: data
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

            actual = @client.flex_api.v1.credentials.new_credentials.create(test_string: "Test String")
            expect(actual).to_not eq(nil)
    end
    it "can delete" do
        @holodeck.mock(Twilio::Response.new(500, ''))
        expect {
            @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').delete()
        }.to raise_exception(Twilio::REST::TwilioError)

         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'delete',
                url: 'https://flex-api.twilio.com/v1/Credentials/AWS/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'
            ))).to eq(true)
    end

    it "receives delete responses" do
        @holodeck.mock(Twilio::Response.new(
            204,
            nil
        ))

        actual = @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').delete()
        expect(actual).to eq(true)
    end
    it "can fetch" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://flex-api.twilio.com/v1/Credentials/AWS/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'
        ))).to eq(true)
    end

    it "receives fetch responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "test_string": "Ahoy"
            }
        ]))

        actual = @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        expect(actual).to_not eq(nil)
    end
    it "can update" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect {
            @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'post',
                url: 'https://flex-api.twilio.com/v1/Credentials/AWS/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'
            ))).to eq(true)
    end

    it "receives update responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "test_string": "Ahoy"
            }
        ]))
        actual = @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        expect(actual).to_not eq(nil)
    end
end

describe "History" do
    it "can fetch" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').history.fetch()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://flex-api.twilio.com/v1/Credentials/AWS/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/History'
        ))).to eq(true)
    end

    it "receives fetch responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "test_string": "Ahoy"
            }
        ]))

        actual = @client.flex_api.v1.credentials.aws('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').history.fetch()
        expect(actual).to_not eq(nil)
    end
end

describe "Call" do
    it "can update" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect {
            @client.flex_api.v1.calls('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'post',
                url: 'https://flex-api.twilio.com/v1/Voice/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'
            ))).to eq(true)
    end

    it "receives update responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "test_string": "Ahoy"
            }
        ]))
        actual = @client.flex_api.v1.calls('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').update()
        expect(actual).to_not eq(nil)
    end
end