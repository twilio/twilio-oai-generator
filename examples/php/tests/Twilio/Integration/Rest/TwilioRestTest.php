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



class TwilioRestTest extends HolodeckTestCase {

    public function testShouldMakeValidCall(): void {
        $this->holodeck->mock(new Response(500, ''));

        try {
            $this->twilio->api->v2010->accounts->create();
        } catch (DeserializeException $e) {}
        catch (TwilioException $e) {}

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json'
        ));
    }

    public function testShouldCreateAccount(): void {
        $this->holodeck->mock(new Response(
            200,
            '
            {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }
            '
        ));

        try {
            $this->twilio->api->v2010->accounts->create();
        } catch (DeserializeException $e) {}
        catch (TwilioException $e) {}

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json',
            null,
            []
        ));
    }

    public function testShouldDeleteAccountResource(): void {
        $this->holodeck->mock(new Response(200, ''));

        try {
            $this->twilio->api->v2010->accounts("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")->delete();
        } catch (DeserializeException $e) {}
        catch (TwilioException $e) {}

        $this->assertRequest(new Request(
            'delete',
            'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json'
        ));
    }


    public function testShouldAddHeader(): void {
        $this->holodeck->mock(new Response(200,
            '
            {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            }
            '
        ));
        try {
            $this->twilio->api->v2010->accounts->create([
                "xTwilioWebhookEnabled" => true,
                "recordingStatusCallback" => "https://validurl.com",
                "recordingStatusCallbackEvent" => ""
            ]);
        } catch (DeserializeException $e) {}
        catch (TwilioException $e) {}

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json',
            [],
            ['RecordingStatusCallback' => 'https://validurl.com', 'RecordingStatusCallbackEvent' => ''],
            ["X-Twilio-Webhook-Enabled" => true]
        ));
    }

    public function testShouldMakeValidApiCallToCallFetcher(): void{
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls(34)->fetch();

        $this->assertNotNull($response);
    }

    public function testShouldSerializeDateTime(): void{
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String",
                "start_date": "2022-12-01",
                "end_date": "2022-12-04"
            }'));

        $response = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls
            ->feedbackCallSummary("FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")->
                update(new \DateTime('2022-12-04'), new \DateTime('2022-12-01'));

        $this->assertNotNull($response);
        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/Feedback/Summary/FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json',
            [],
            ['StartDate' => '2022-12-01', 'EndDate' => '2022-12-04']
        ));
    }

}
