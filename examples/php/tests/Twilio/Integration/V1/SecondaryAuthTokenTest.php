<?php

/**
 * This code was generated by
 * \ / _    _  _|   _  _
 * | (_)\/(_)(_|\/| |(/_  v1.0.0
 * /       /
 */

namespace Twilio\Tests\Integration\Accounts\V1;

use Twilio\Exceptions\DeserializeException;
use Twilio\Exceptions\TwilioException;
use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;

class SecondaryAuthTokenTest extends HolodeckTestCase {
//     public function testCreateRequest(): void {
//         $this->holodeck->mock(new Response(500, ''));
//
//         try {
//             $this->twilio->accounts->v1->secondaryAuthToken()->create();
//         } catch (DeserializeException $e) {}
//           catch (TwilioException $e) {}
//
//         $this->assertRequest(new Request(
//             'post',
//             'https://accounts.twilio.com/v1/AuthTokens/Secondary'
//         ));
//     }
//
//     public function testCreateResponse(): void {
//         $this->holodeck->mock(new Response(
//             201,
//             '
//             {
//                 "account_sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//                 "date_created": "2015-07-31T04:00:00Z",
//                 "date_updated": "2015-07-31T04:00:00Z",
//                 "secondary_auth_token": "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
//                 "url": "https://accounts.twilio.com/v1/AuthTokens/Secondary"
//             }
//             '
//         ));
//
//         $actual = $this->twilio->accounts->v1->secondaryAuthToken()->create();
//
//         $this->assertNotNull($actual);
//     }
//
//     public function testDeleteRequest(): void {
//         $this->holodeck->mock(new Response(500, ''));
//
//         try {
//             $this->twilio->accounts->v1->secondaryAuthToken()->delete();
//         } catch (DeserializeException $e) {}
//           catch (TwilioException $e) {}
//
//         $this->assertRequest(new Request(
//             'delete',
//             'https://accounts.twilio.com/v1/AuthTokens/Secondary'
//         ));
//     }
//
//     public function testDeleteResponse(): void {
//         $this->holodeck->mock(new Response(
//             204,
//             null
//         ));
//
//         $actual = $this->twilio->accounts->v1->secondaryAuthToken()->delete();
//
//         $this->assertTrue($actual);
//     }

    public function testDummyTest(): void {

        $this->assertTrue(true);
    }


}