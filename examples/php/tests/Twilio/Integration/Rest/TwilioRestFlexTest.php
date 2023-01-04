<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;


class TwilioFlexRestTest extends HolodeckTestCase
{

    public function testShouldSerializePrefixedMapWhenFetchCallMadeToHistory(): void
    {

        $this->holodeck->mock(new Response(200,
            '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'
        ));

        $historyContext = $this->twilio->flexApi->v1->credentials->aws("AC222222222222222222222222222222")->history();
        $this->assertEquals("[Twilio.FlexApi.V1.HistoryContext sid=AC222222222222222222222222222222]", $historyContext->__toString());
        $actual = $historyContext->fetch(["addOnsData" => [
            "status" => "successful",
            "message" => "hi"
        ]]);

        $this->assertNotNull($actual);

        $this->assertEquals("[Twilio.FlexApi.V1.HistoryInstance sid=AC222222222222222222222222222222]", $actual->__toString());
        $this->assertRequest(new Request(
            'get',
            'https://flex-api.twilio.com/v1/Credentials/AWS/AC222222222222222222222222222222/History',
            ['AddOns.status' => 'successful', 'AddOns.message' => 'hi']
        ));

        $this->holodeck->mock(new Response(200,
            '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'
        ));
        $instanceResponse = $actual->fetch();
        $this->assertNotNull($instanceResponse);
    }

    public function testShouldMakeDeleteCallToAWS(): void
    {
        $this->holodeck->mock(new Response(
            204,
            ''
        ));

        $awsContext = $this->twilio->flexApi->v1->credentials->aws("123");
        $this->assertEquals("[Twilio.FlexApi.V1.AwsContext sid=123]", $awsContext->__toString());
        $actual = $awsContext->delete();

        $this->assertNotNull($actual);
        $this->assertTrue($actual);
    }

    public function testShouldMakeCreateCallToNewCredentials(): void
    {
        $this->holodeck->mock(new Response(
            200,
            '
            {
             "test_string": "test string",
             "test_integer" : "123",
             "sid": "S1234",
             "account_sid": "AXXXXXXXXXXX"
            }
            '
        ));

        $newCredentialsList = $this->twilio->flexApi->v1->credentials->newCredentials;
        $this->assertEquals("[Twilio.FlexApi.V1.NewCredentialsList]", $newCredentialsList->__toString());
        $actual = $newCredentialsList->create("test String");

        $this->assertNotNull($actual);
        $this->assertEquals("[Twilio.FlexApi.V1.NewCredentialsInstance]", $actual->__toString());
        $this->assertRequest(new Request('post',
            'https://flex-api.twilio.com/v1/Credentials/AWS',
            [],
            ['TestString' => 'test String']));
    }

    public function testValidResponseForAwsCredentialRead(): void
    {
        $this->holodeck->mock(new Response(200,
            '
            {
              "meta":{
                "first_page_uri": "/v1/Credentials/AWS",
                "end": 0,
                "previous_page_uri": null,
                "uri": "/v1/Credentials/AWS",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0 ,
                "key":"credentials"
              },
              "credentials":[{
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Ahoy"
              }]
            }
            '
        ));
        $awsList = $this->twilio->flexApi->v1->credentials->aws;
        $this->assertEquals("[Twilio.FlexApi.V1.AwsList]", $awsList->__toString());

        $actual = $awsList->read();
        $this->assertNotNull($actual);
        $this->assertEquals("Ahoy", $actual[0]->testString);
    }

    public function testValidResponseForAwsCredentialFetcher()
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $awsContext = $this->twilio->flexApi->v1->credentials->aws("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals("[Twilio.FlexApi.V1.AwsContext sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $awsContext->__toString());
        $response = $awsContext->fetch();

        $this->assertNotNull($response);
        $this->assertEquals("[Twilio.FlexApi.V1.AwsInstance sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $response->__toString());
        $this->assertRequest(new Request(
            'get',
            'https://flex-api.twilio.com/v1/Credentials/AWS/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
            [],
            []
        ));
        $this->assertEquals("AC222222222222222222222222222222", $response->accountSid);
        $this->assertEquals("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", $response->sid);
        $this->assertEquals("Test String", $response->testString);

        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));
        $instanceResponse = $response->fetch();
        $this->assertNotNull($instanceResponse);
        $this->assertEquals("Test String", $instanceResponse->testString);
    }

    public function testForValidAwsCredentialUpdate(): void
    {
        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $response = $this->twilio->flexApi->v1->credentials->aws("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            ->update([
                'testString' => "Test String",
                'testBoolean' => true
            ]);

        $this->assertNotNull($response);
        $this->assertRequest(new Request(
            'post',
            'https://flex-api.twilio.com/v1/Credentials/AWS/ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
            [],
            ['TestBoolean' => true, 'TestString' => "Test String"]
        ));
        $this->assertEquals($response->accountSid, "AC222222222222222222222222222222");
        $this->assertEquals($response->sid, "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals($response->testString, "Test String");

        $this->holodeck->mock(new Response(200, '{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));
        $instanceResponse = $response->update();
        $this->assertNotNull($instanceResponse);
        $this->assertEquals($instanceResponse->accountSid, "AC222222222222222222222222222222");
    }

    public function testLimitsInReadGivesCorrectNumberOfResponse(): void
    {
        $this->holodeck->mock(new Response(200,
            '
            {
              "meta":{
                "first_page_uri": "/v1/Credentials/AWS",
                "end": 0,
                "previous_page_uri": null,
                "uri": "/v1/Credentials/AWS",
                "page_size": 50,
                "start": 0,
                "next_page_uri": null,
                "page": 0 ,
                "key":"credentials"
              },
              "credentials":[{
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Ahoy"
              },
              {
               "sid": "CR123456781234567812345678321456789",
               "test_string": "Second Response"
              },
              {
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Third Response"
              },
              {
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Fourth Response"
              }]
            }
            '
        ));
        $actual = $this->twilio->flexApi->v1->credentials->aws->read($limit = 3);
        $this->assertNotNull($actual);
        $this->assertEquals(3, count($actual));
        $this->assertEquals("Ahoy", $actual[0]->testString);
        $this->assertEquals("Second Response", $actual[1]->testString);
        $this->assertEquals("Third Response", $actual[2]->testString);
    }

    public function testPageReturnsValidPageUrls(): void
    {
        $this->holodeck->mock(new Response(200,
            '
            {
              "meta":{
                "first_page_url": "/v1/Credentials/AWS",
                "end": 0,
                "previous_page_url": "https://flex-api.twilio.com/v1/Credentials/AWS/page=1",
                "uri": "/v1/Credentials/AWS",
                "page_size": 3,
                "start": 0,
                "next_page_url": "https://flex-api.twilio.com/v1/Credentials/AWS/page=3",
                "page": 0 ,
                "key":"credentials"
              },
              "credentials":[{
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Ahoy"
              }]
            }
            '
        ));
        $actual = $this->twilio->flexApi->v1->credentials->aws->page($pageSize = 3);
        $this->assertNotNull($actual);
        $this->assertEquals("https://flex-api.twilio.com/v1/Credentials/AWS/page=3", $actual->getNextPageUrl());
        $this->assertEquals("https://flex-api.twilio.com/v1/Credentials/AWS/page=1", $actual->getPreviousPageUrl());
    }

    public function testValidPageSizeInResponse(): void
    {
        $this->holodeck->mock(new Response(200,
            '
            {
              "meta":{
                "first_page_uri": "/v1/Credentials/AWS",
                "end": 0,
                "previous_page_uri": null,
                "uri": "/v1/Credentials/AWS",
                "page_size": 3,
                "start": 0,
                "next_page_uri": null,
                "page": 0 ,
                "key":"credentials"
              },
              "credentials":[{
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Ahoy"
              },
              {
               "sid": "CR123456781234567812345678321456789",
               "test_string": "Second Response"
              },
              {
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Third Response"
              },
              {
               "sid": "CR12345678123456781234567812345678",
               "test_string": "Fourth Response"
              }]
            }
            '
        ));
        $actual = $this->twilio->flexApi->v1->credentials->aws->read($pageSize = 3);
        $this->assertNotNull($actual);
        $this->assertEquals(3, count($actual));
        $this->assertEquals("Ahoy", $actual[0]->testString);
        $this->assertEquals("Second Response", $actual[1]->testString);
        $this->assertEquals("Third Response", $actual[2]->testString);
    }

    public function testShouldMakeUpdateCallToCredentialCall(): void
    {
        $this->holodeck->mock(new Response(204,'{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));

        $callContext = $this->twilio->flexApi->v1->calls("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        $this->assertEquals("[Twilio.FlexApi.V1.CallContext sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $callContext->__toString());

        $response = $callContext->update();
        $this->assertNotNull($response);
        $this->assertEquals("[Twilio.FlexApi.V1.CallInstance sid=ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]", $response->__toString());

        $this->holodeck->mock(new Response(204,'{
                "account_sid": "AC222222222222222222222222222222",
                "sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "test_string": "Test String"
            }'));
        $instanceResponse = $response->update();
        $this->assertNotNull($instanceResponse);
    }

}
