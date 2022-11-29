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



class ClientTest extends HolodeckTestCase {

    public function testShouldMakeValidCall(): void {
//        $this->holodeck->mock(new Response(500, ''));
//
//        try {
//            $this->twilio->api->v2010->accounts->create();
//        } catch (DeserializeException $e) {}
//        catch (TwilioException $e) {}
//
//        $this->assertRequest(new Request(
//            'post',
//            'https://api.twilio.com/2010-04-01/Accounts.json'
//        ));
    }

}
