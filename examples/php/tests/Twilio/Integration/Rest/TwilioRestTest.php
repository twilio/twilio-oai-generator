<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Exceptions\TwilioException;
use Twilio\Exceptions\DeserializeException;
use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;


class TwilioRestTest extends HolodeckTestCase
{

    public function testShouldReturnExceptionWhenAccountIsCalledWithInvalidRequest(): void
    {
        $this->holodeck->mock(new Response(500, ''));

        try {
            $response = $this->twilio->api->v2010->accounts->create();
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
            $this->AssertEquals($e->getMessage(), '[HTTP 500] Unable to create record');
        }

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json'
        ));
    }

    public function testShouldCreateAccount(): void
    {
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
            $accountList = $this->twilio->api->v2010->accounts;
            $this->assertEquals("[Twilio.Api.V2010.AccountList]", $accountList->__toString());

            $response = $accountList->create();
            $this->assertEquals("[Twilio.Api.V2010.AccountInstance sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $response->__toString());
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json',
            null,
            []
        ));
        $this->assertEquals($response->accountSid, "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");
    }

    public function testShouldDeleteAccountResource(): void
    {
        $this->holodeck->mock(new Response(204, ''));

        try {
            $accountContext = $this->twilio->api->v2010->accounts("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            $this->assertEquals("[Twilio.Api.V2010.AccountContext sid=ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX]", $accountContext->__toString());

            $actual = $accountContext->delete();
            $this->assertTrue($actual);
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'delete',
            'https://api.twilio.com/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json'
        ));

    }

    public function testShouldMakeAccountsCallWithCustomHeaders(): void
    {
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
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts.json',
            [],
            ['RecordingStatusCallback' => 'https://validurl.com', 'RecordingStatusCallbackEvent' => ''],
            ["X-Twilio-Webhook-Enabled" => true]
        ));
    }

    public function testShouldMakeValidApiCallToCallFetcher(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $callContext = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls(34);
        $this->assertEquals("[Twilio.Api.V2010.CallContext accountSid=AC222222222222222222222222222222 testInteger=34]", $callContext->__toString());

        $response = $callContext->fetch();

        $this->assertNotNull($response);
        $this->assertEquals("[Twilio.Api.V2010.CallInstance accountSid=AC222222222222222222222222222222 testInteger=34]", $response->__toString());
        $this->assertRequest(new Request(
            'get',
            'https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/34.json',
            [],
            []
        ));
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");

        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));
        $instanceResponse = $response->fetch();
        $this->assertNotNull($instanceResponse);
    }

    public function testShouldSerializeDateTime(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String",
                "start_date": "2022-12-01",
                "end_date": "2022-12-04"
            }'));

        $feedbackCallSummaryContext = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls->feedbackCallSummary("FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        $this->assertEquals("[Twilio.Api.V2010.FeedbackCallSummaryContext accountSid=AC222222222222222222222222222222 sid=FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX]", $feedbackCallSummaryContext->__toString());

        $response = $feedbackCallSummaryContext->update(new \DateTime('2022-12-04'), new \DateTime('2022-12-01'));

        $this->assertNotNull($response);
        $this->assertEquals("[Twilio.Api.V2010.FeedbackCallSummaryInstance accountSid=AC222222222222222222222222222222 sid=FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX]", $response->__toString());
        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/Feedback/Summary/FSXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json',
            [],
            ['StartDate' => '2022-12-01', 'EndDate' => '2022-12-04']
        ));
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");

        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String",
                "start_date": "2022-12-01",
                "end_date": "2022-12-04"
            }'));
        $instanceResponse = $response->update(new \DateTime('2022-12-04'), new \DateTime('2022-12-01'));
        $this->assertNotNull($instanceResponse);
        $this->assertEquals($instanceResponse->testString, "Test String");
    }

    public function testReadWhenRequestFails(): void
    {
        $this->holodeck->mock(new Response(500, ''));

        try {
            $this->twilio->api->v2010->accounts->read();
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'get',
            'https://api.twilio.com/2010-04-01/Accounts.json'
        ));
    }

    public function testReadWhenEmptyResponseReceived(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
                "first_page_uri": "/2010-04-01/Accounts.json?Page=0",
                "end": 0,
                "previous_page_uri": null,
                "accounts": [],
                "uri": "/2010-04-01/Accounts.json?Page=0",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
            }
            '
        ));

        $actual = $this->twilio->api->v2010->accounts->read();

        $this->assertNotNull($actual);
    }

    public function testWhenValidReadResponseReceived(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
                "first_page_uri": "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0",
                "end": 0,
                "previous_page_uri": null,
                "accounts": [
                   {
                        "account_sid": "account_sid",
                        "test_date_time": "Thu, 30 Jul 2015 20:00:00 +0000",
                        "test_string": "Test  String",
                        "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    } 
                ],
                "uri": "/2010-04-01/Accounts.json?DateCreated=date_created",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
            }
            '
        ));
        $actual = $this->twilio->api->v2010->accounts->read();
        $this->assertGreaterThan(0, \count($actual));
    }

    public function testReadObjectCreatesValidTestString(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
                "first_page_uri": "/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0",
                "end": 0,
                "previous_page_uri": null,
                "accounts": [
                   {
                        "account_sid": "account_sid",
                        "test_date_time": "Thu, 30 Jul 2015 20:00:00 +0000",
                        "test_string": "Test  String",
                        "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    } 
                ],
                "uri": "/2010-04-01/Accounts.json?DateCreated=date_created",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
            }
            '
        ));
        $actual = $this->twilio->api->v2010->accounts->read();
        $val = array_values($actual)[0];
        $this->assertEquals(
            $val->testString,
            "Test  String"
        );
    }

    public function testShouldValidateDateRangeInQuery(): void
    {

        $this->holodeck->mock(new Response(200,
            '
            {
                "first_page_uri": "/2010-04-01/Accounts.json",
                "end": 0,
                "previous_page_uri": null,
                "accounts": [
                   {
                        "account_sid": "account_sid",
                        "test_date_time": "Thu, 30 Jul 2015 20:00:00 +0000",
                        "test_string": "Test  String",
                        "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    } 
                ],
                "uri": "/2010-04-01/Accounts.json?DateCreated=date_created",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0
            }
            '
        ));

        $actual = $this->twilio->api->v2010->accounts->read(
            [
                'dateCreatedBefore' => "2011-05-21",
                'dateCreatedAfter' => "2012-01-01"
            ]);
        $this->assertNotNull($actual);
        $this->assertRequest(new Request(
            'get',
            "https://api.twilio.com/2010-04-01/Accounts.json",
            ['DateCreated<' => '2011-05-21', 'DateCreated>' => "2012-01-01"]
        ));
    }


}
