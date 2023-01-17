require 'spec_helper.rb'

describe "Aws" do
    it "can read" do
        @holodeck.mock(Twilio::Response).new(500,''))

        expect{
            @client.flex_api.v1.credential.aws.list()
        }.to raise_exception(Twilio::REST::TwilioError)
         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'get',
                url: 'https://flex-api.twilio.com/v1/Credentials/AWS',
            ))).to eq(true)
        )
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
                        "sid": "CR12345678123456781234567812345678"
                        "test_string": "Ahoy"
                    }]
                 }
                ]
            ))

            actual = @client.flex_api.v1.credential.aws.list()
            expect(actual).to_not eq(nil)
    end

    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))

        @expect{
            @client.flex_api.v1.credential.aws.create("example","get")
        }.to raise_exception(Twilio::REST::TwilioError)

       expect(
           @holodeck.has_request?(Holodeck::Request.new(
               method: "post",
               url: "https://flex-api.twilio.com/v1/Credentials/AWS"
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

            actual = @client.flex_api.v1.credential.aws.create("example","post")
            expect(actual).to_not eq(nil)
    end
end