<?php

namespace Twilio\Tests\Integration\Rest;

use Twilio\Rest\Client;
use PHPUnit\Framework\TestCase;

class TwilioRestIntegrationTest extends TestCase
{
    var $accountSid = "AC12345678123456781234567812345678";
    var $authToken = "CR12345678123456781234567812345678";

    public function testCreateAccount(): void
    {
        $client = new Client($this->accountSid, $this->authToken);
        $account = $client->api->v2010->accounts->create();
        $this->assertEquals("CR12345678123456781234567812345678", $account->sid);
    }

    public function testFetchCall(): void
    {
        $client = new Client($this->accountSid, $this->authToken);
        $result = $client->api->v2010->accounts($this->accountSid)->calls(123)->fetch();
        $this->assertEquals("CR12345678123456781234567812345678", $result->sid);
        $this->assertEquals("Ahoy", $result->testString);
    }

    public function testUpdateCallFeedbackSummary(): void
    {
        $client = new Client($this->accountSid, $this->authToken);
        $endDate = date_create_from_format("Y-m-d", "2020-12-31");
        $startDate = date_create_from_format("Y-m-d", "2020-01-01");
        $result = $client->api->v2010->
        accounts($this->accountSid)->calls->feedbackCallSummary("CR12345678123456781234567812345678")
            ->update($endDate, $startDate);
        $this->assertEquals("issue description", $result->testArrayOfObjects[0]["description"]);
        $this->assertEquals(4, $result->testArrayOfObjects[0]["count"]);
    }

    public function testCreateCall(): void
    {
        $client = new Client($this->accountSid, $this->authToken);
        $call = $client->api->v2010->accounts($this->accountSid)->calls->create("requiredStringProperty", "GET");
        $this->assertEquals("CR12345678123456781234567812345678", $call->sid);
        $this->assertEquals("Ahoy", $call->testString);

    }
}