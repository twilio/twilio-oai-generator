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

class AuthTokenPromotionTest extends HolodeckTestCase {
//     public function testUpdateRequest(): void {
//         $this->holodeck->mock(new Response(500, ''));
//
//         try {
//             $this->twilio->accounts->v1->authTokenPromotion()->update();
//         } catch (DeserializeException $e) {}
//           catch (TwilioException $e) {}
//
//         $this->assertRequest(new Request(
//             'post',
//             'https://accounts.twilio.com/v1/AuthTokens/Promote'
//         ));
//     }

//     public function testUpdateResponse(): void {
//         $this->holodeck->mock(new Response(
//             200,
//             '
//             {
//                 "account_sid": "ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//                 "auth_token": "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
//                 "date_created": "2015-07-31T04:00:00Z",
//                 "date_updated": "2015-07-31T04:00:00Z",
//                 "url": "https://accounts.twilio.com/v1/AuthTokens/Promote"
//             }
//             '
//         ));
//
//         $actual = $this->twilio->accounts->v1->authTokenPromotion()->update();
//
//         $this->assertNotNull($actual);
//     }

    public function testDummyTest(): void {

        $this->assertTrue(true);
    }
}