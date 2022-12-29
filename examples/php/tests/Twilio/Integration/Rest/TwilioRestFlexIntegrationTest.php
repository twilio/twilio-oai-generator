<?php

namespace Twilio\Tests\Integration\Rest;

use Twilio\Rest\Client;
use PHPUnit\Framework\TestCase;

class TwilioRestFlexIntegrationTest extends TestCase
{
    var $accountSid = "AC12345678123456781234567812345678";
    var $authToken = "CR12345678123456781234567812345678";

    public function testFecthAwsCredentials(): void
    {
        $client = new Client($this->accountSid, $this->authToken);
        $response = $client->flexApi->v1->credentials->aws($this->authToken)->fetch();
        $this->assertNotNull($response);
        $this->assertEquals("CR12345678123456781234567812345678", $response->sid);
        $this->assertEquals("Ahoy", $response->testString);
    }
}