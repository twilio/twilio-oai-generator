require 'spec_helper.rb'

describe "Fleets" do
    it "can create" do
        @holodeck.mock(Twilio::Response.new(500,''))

        expect{
            @client.versionless.deployed_devices.fleets.create(name: "example")
        }.to raise_exception(Twilio::REST::TwilioError)
        data = {}
        data["Name"] = "example"
        expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: "POST",
                url: "https://versionless.twilio.com/DeployedDevices/Fleets",
                auth: [@client.username, @client.password],
                data: data
            ))).to eq(true)
    end
    it "recieves create responses" do
        @holodeck.mock(Twilio::Response.new(
        201,
        %q[
            {
                "name" : "test Name"
            }
        ]
        ))
        actual = @client.versionless.deployed_devices.fleets.create(name: "example")
        expect(actual).to_not eq(nil)
    end
    it "can fetch" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.versionless.deployed_devices.fleets('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        }.to raise_exception(Twilio::REST::TwilioError)

        expect(
        @holodeck.has_request?(Holodeck::Request.new(
            method: 'get',
            url: 'https://versionless.twilio.com/DeployedDevices/Fleets/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'
        ))).to eq(true)
    end

    it "receives fetch responses" do
        @holodeck.mock(Twilio::Response.new(
        200,
        %q[
            {
                "sid": "CXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "friendly_name": "Ahoy"
            }
        ]))

        actual = @client.versionless.deployed_devices.fleets('ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX').fetch()
        expect(actual).to_not eq(nil)
    end
end
=begin
describe "Assistant" do
    it "can read" do
        @holodeck.mock(Twilio::Response.new(500,''))
        expect{
            @client.versionless.understand.assistants.list()
        }.to raise_exception(Twilio::REST::TwilioError)
         expect(
            @holodeck.has_request?(Holodeck::Request.new(
                method: 'get',
                url: 'https://versionless.twilio.com/understand/Assistants',
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
                    "key": "assistants",
                    "uri": "/2010-04-01/Accounts.json?PageSize=50&Page=0",
                    "page_size": 50,
                    "start": 0,
                    "next_page_uri": null,
                    "page": 0
                },
                "assistants": [{
                    "sid": "CR12345678123456781234567812345678",
                    "friendly_name": "Test Name"
                }]
             }
            ]
        ))

        actual = @client.versionless.understand.assistants.list()
        expect(actual).to_not eq(nil)
    end
end
=end
