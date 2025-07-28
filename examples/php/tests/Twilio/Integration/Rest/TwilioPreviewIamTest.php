<?php


namespace Twilio\Tests\Integration\Rest;

use Twilio\Exceptions\TwilioException;
use Twilio\Exceptions\DeserializeException;
use Twilio\Http\Response;
use Twilio\Tests\HolodeckTestCase;
use Twilio\Tests\Request;
use Twilio\Rest\PreviewIam\Versionless\Organization\UserModels;


class TwilioPreviewIamTest extends HolodeckTestCase
{

    public function testShouldCreateOrgUser()
    {
        $this->holodeck->mock(new Response(200, '
        {
        "id": "US123",
        "userName": "test@example.com"
        }
        '));
        try {
            $userName = "test@example.com";
            $orgSid = "ORXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
            $scimUser = UserModels::createScimUser([
                'userName' => $userName
            ]);

            $user = $this->twilio->previewIam->organization($orgSid)->users->create($scimUser);
            $this->assertEquals("[Twilio.PreviewIam.Versionless.UserInstance]", $user->__toString());
            $this->assertNotNull($user);
            $this->assertEquals($user->userName, $userName);
        } catch (DeserializeException $e) {
            print "DeserializeException: " . $e->getMessage();
        } catch (TwilioException $e) {
            print "TwilioException: " . $e->getMessage();
        }

        $this->assertRequest(new Request(
            'post',
            'https://preview-iam.twilio.com/Organizations/ORfc9e0b49972d3b552499767926a1f4f6/scim/Users',
            [],
            ['userName' => 'test@example.com']
        ));

    }
}
