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

    public function testReadRequestCredentials(): void
    {
        $this->holodeck->mock(new Response(500, ''));

        try {
            $this->twilio->flexApi->v1->credentials->read();
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'get',
            'https://flex-api.twilio.com/v1/Credentials'
        ));
    }

    public function testReadEmptyResponseCredentials(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
                "first_page_uri": "/https://flex-api.twilio.com/v1/Credentials?Page=0",
                "end": 0,
                "previous_page_uri": null,
                "credentials": [],
                "uri": "https://flex-api.twilio.com/v1/Credentials?Page=0",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
            }
            '
        ));

        $actual = $this->twilio->flexApi->v1->credentials->read();

        $this->assertNotNull($actual);
    }
}
