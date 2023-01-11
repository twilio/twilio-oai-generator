<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Exceptions\TwilioException;
use Twilio\Exceptions\DeserializeException;
use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;

class TwilioRestExtTest extends HolodeckTestCase
{

    public function testShouldMakeAccountFetchCall(): void
    {
        $this->holodeck->mock(new Response(200, '
        {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
        }
        '));

        try {
            $accountContext = $this->twilio->api->v2010->accounts("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            $this->assertEquals("[Twilio.Api.V2010.AccountContext sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $accountContext->__toString());

            $actual = $accountContext->fetch();
            $this->assertEquals("[Twilio.Api.V2010.AccountInstance sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $actual->__toString());
        } catch (DeserializeException $e) {
        } catch (TwilioException $e) {
        }

        $this->assertRequest(new Request(
            'get',
            'https://api.twilio.com/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.json'
        ));
        $this->assertEquals($actual->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($actual->accountSid, "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        $this->assertEquals($actual->testString, "Test String");

        $this->holodeck->mock(new Response(200, '
        {
                "account_sid": "ACXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
        }
        '));
        $instanceResponse = $actual->fetch();
        $this->assertNotNull($instanceResponse);
    }

    public function testShouldMakeFetchCallToAccountCall(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String",
                "test_integer": 2
            }'));

        $callList = $this->twilio->api->v2010->accounts("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")->calls;
        $this->assertEquals("[Twilio.Api.V2010.CallList]", $callList->__toString());

        $response = $callList->create("example", "example");

        $this->assertNotNull($response);
        $this->assertEquals("[Twilio.Api.V2010.CallInstance accountSid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa testInteger=2]", $response->__toString());
        $this->assertRequest(new Request(
            'post',
            'https://api.twilio.com/2010-04-01/Accounts/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/Calls.json',
            [],
            ['RequiredStringProperty' => 'example', 'TestMethod' => 'example']
        ));
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");
    }

    public function testShouldMakeDeleteCallToAccountCall(): void
    {
        $this->holodeck->mock(new Response(204, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls("123")->delete();

        $this->assertRequest(new Request(
            'delete',
            'https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/123.json'
        ));
        $this->assertNotNull($response);
        $this->assertTrue($response);
    }

    public function testShouldMakeUpdateCallToFeedbackCallSummary(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->calls
            ->feedbackCallSummary("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")->update(new \DateTime(), new \DateTime());

        $this->assertNotNull($response);
        $this->assertEquals($response->testString, "Test String");
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    }

    public function testShouldMakeUpdateAccountCall(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->api->v2010->accounts("AC222222222222222222222222222222")->update("stopped");

        $this->assertNotNull($response);
        $this->assertEquals($response->testString, "Test String");
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));
        $instanceResponse = $response->update("stopped");
        $this->assertNotNull($instanceResponse);

    }
}
