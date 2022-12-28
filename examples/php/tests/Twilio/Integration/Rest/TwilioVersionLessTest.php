<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Exceptions\TwilioException;
use Twilio\Exceptions\DeserializeException;
use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;


class TwilioVersionLessTest extends HolodeckTestCase
{

    public function testShouldMakeReadCallToVersionlessDomain()
    {
        $this->holodeck->mock(new Response(200, '
        {
        "assistants": [{
        "sid": "123",
        "friendly_name": "Friendly Assistant"
        }]
        }
        '));
        try {
            $response = $this->twilio->versionless->understand->assistants->read();
            $this->assertNotNull($response);
            $this->assertEquals($response[0]->sid, "123");
            $this->assertEquals($response[0]->friendlyName, "Friendly Assistant");
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'get',
            'https://versionless.twilio.com/understand/Assistants'
        ));

    }

    public function testShouldMakeCreateCallToDeployedDevices()
    {
        $this->holodeck->mock(new Response(200, '
        {
        "name":"Fleet Name",
        "sid": "123",
        "friendly_name": "Friendly Fleet"
        }
        '));
        try {
            $response = $this->twilio->versionless->deployedDevices->fleets->create();
            $this->assertEquals($response->sid, "123");
            $this->assertEquals($response->friendlyName, "Friendly Fleet");
//            $this->assertEquals($response->sid, "123");
//            $this->assertEquals($response->name, "Fleet Name");


        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'post',
            'https://versionless.twilio.com/DeployedDevices/Fleets'
        ));
    }

    public function testShouldMakeFetchCallToDeployedDevices()
    {
        $this->holodeck->mock(new Response(200, '
        {
        "sid": "123",
        "friendly_name": "Friendly Assistant"
        }
        '));
        try {
            $response = $this->twilio->versionless->deployedDevices->fleets("123")->fetch();
            $this->assertNotNull($response);
            $this->assertEquals($response->friendlyName, "Friendly Assistant");
            $this->assertEquals($response->sid, "123");
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'get',
            'https://versionless.twilio.com/DeployedDevices/Fleets/123'
        ));
    }
}
