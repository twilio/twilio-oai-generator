<?php




namespace Twilio\Tests\Integration\Rest;

use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;



class TwilioFlexRestTest extends HolodeckTestCase {

    public function testShouldSerializePrefixedMapWhenFetchCallMadeToHistory(): void {

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

    public function testShouldMakeDeleteCallToAWS(): void {
        $this->holodeck->mock(new Response(
            204,
            ''
        ));

        $actual = $this->twilio->flexApi->v1->credentials->aws("123")->delete();

        $this->assertNotNull($actual);
        $this->assertTrue($actual);
    }

    public function testShouldMakeCreateCallToNewCredentials(): void {
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
        $this->assertRequest(new Request( 'post',
            'https://flex-api.twilio.com/v1/Credentials/AWS',
            [],
            ['TestString' => 'test String']));
    }
}
