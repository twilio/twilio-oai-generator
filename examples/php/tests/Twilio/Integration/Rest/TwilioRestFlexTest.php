<?php




namespace Twilio\Tests\Integration\Rest;

use Twilio\Exceptions\ConfigurationException;
use Twilio\Exceptions\TwilioException;
use Twilio\Exceptions\DeserializeException;
use Twilio\Http\CurlClient;
use Twilio\Http\Response;
use Twilio\Rest\Client;
use Twilio\Tests\Holodeck;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;
use Twilio\Tests\Unit\UnitTest;



class TwilioFlexRestTest extends HolodeckTestCase {

    public function testShouldSerializePrefixedMap(): void {

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
}
