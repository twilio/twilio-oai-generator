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

    public function testShouldPrefixedMap(): void {
        $this->markTestSkipped('WIP.');
        $this->holodeck->mock(new Response(500, ''));

        try {
            $this->twilio->flexApi->v1->credentials("1234")->aws("")->history("")->fetch();
        } catch (DeserializeException $e) {}
        catch (TwilioException $e) {}

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json'
        ));
    }
}
