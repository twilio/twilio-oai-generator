<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;


class TwilioFlexRestTest extends HolodeckTestCase
{

    public function testShouldSerializePrefixedMapWhenFetchCallMadeToHistory(): void
    {

        $this->holodeck->mock(new Response(200,
            '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'
        ));

        $actual = $this->twilio->flexApi->v1->credentials->aws("AC222222222222222222222222222222")->history()->
        fetch(["addOnsData" => [
            "status" => "successful",
            "message" => "hi"
        ]]);

        $this->assertNotNull($actual);

        $this->assertRequest(new Request(
            'get',
            'https://flex-api.twilio.com/v1/Credentials/AWS/AC222222222222222222222222222222/History',
            ['AddOns.status' => 'successful', 'AddOns.message' => 'hi']
        ));
    }

    public function testShouldMakeDeleteCallToAWS(): void
    {
        $this->holodeck->mock(new Response(
            204,
            ''
        ));

        $actual = $this->twilio->flexApi->v1->credentials->aws("123")->delete();

        $this->assertNotNull($actual);
        $this->assertTrue($actual);
    }

    public function testShouldMakeCreateCallToNewCredentials(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
             "test_string": "test string",
             "test_integer" : "123",
             "sid": "S1234",
             "account_sid": "AXXXXXXXXXXX"
            }
            '
        ));

        $actual = $this->twilio->flexApi->v1->credentials->newCredentials->create("test String");

        $this->assertNotNull($actual);
        $this->assertRequest(new Request('post',
            'https://flex-api.twilio.com/v1/Credentials/AWS',
            [],
            ['TestString' => 'test String']));
    }

    public function testValidResponseForAwsCredentialRead(): void
    {
        $this->holodeck->mock(new Response(200,
            '
            {
              "meta":{
                "first_page_uri": "/v1/Credentials/AWS",
                "end": 0,
                "previous_page_uri": null,
                "uri": "/v1/Credentials/AWS",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0 ,
                "key":"credentials"
              },
              "credentials":[{
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Ahoy"
              }]
            }
            '
        ));
        $actual = $this->twilio->flexApi->v1->credentials->aws->read();
        $this->assertNotNull($actual);
        $this->assertEquals("Ahoy", $actual[0]->testString);
    }

    public function testValidResponseForAwsCredentialFetcher()
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->flexApi->v1->credentials->aws("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")->fetch();

        $this->assertNotNull($response);
        $this->assertRequest(new Request(
            'get',
            'https://flex-api.twilio.com/v1/Credentials/AWS/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
            [],
            []
        ));
        $this->assertEquals("AC222222222222222222222222222222", $response->accountSid);
        $this->assertEquals("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", $response->sid);
        $this->assertEquals("Test String", $response->testString);
    }

    public function testForValidAwsCredentialUpdate(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->flexApi->v1->credentials->aws("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            ->update([
                'testString' => "Test String",
                'testBoolean' => true
            ]);

        $this->assertNotNull($response);
        $this->assertRequest(new Request(
            'post',
            'https://flex-api.twilio.com/v1/Credentials/AWS/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
            [],
            ['TestBoolean' => true, 'TestString' => "Test String"]
        ));
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");
    }


}
