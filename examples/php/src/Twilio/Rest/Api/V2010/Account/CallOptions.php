<?php
/**
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

namespace Twilio\Rest\Api\V2010\Account;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\InstanceResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;
use Twilio\Base\PhoneNumberCapabilities;
use Twilio\Rest\Api\V2010\Account\Call\FeedbackCallSummaryList;

abstract class CallOptions
{
    /**
     * @param string[] $testArrayOfStrings
     * @param string[] $testArrayOfUri
     * @return CreateCallOptions Options builder
     */
    public static function create(
        
        array $testArrayOfStrings = Values::ARRAY_NONE,
        array $testArrayOfUri = Values::ARRAY_NONE

    ): CreateCallOptions
    {
        return new CreateCallOptions(
            $testArrayOfStrings,
            $testArrayOfUri
        );
    }



}

class CreateCallOptions extends Options
    {
    /**
     * @param string[] $testArrayOfStrings
     * @param string[] $testArrayOfUri
     */
    public function __construct(
        
        array $testArrayOfStrings = Values::ARRAY_NONE,
        array $testArrayOfUri = Values::ARRAY_NONE

    )
    {
        $this->options['testArrayOfStrings'] = $testArrayOfStrings;
        $this->options['testArrayOfUri'] = $testArrayOfUri;
    }

    /**
     * @param string[] $testArrayOfStrings
     * @return $this Fluent Builder
     */
    public function setTestArrayOfStrings(array $testArrayOfStrings): self
    {
        $this->options['testArrayOfStrings'] = $testArrayOfStrings;
        return $this;
    }

    /**
     * @param string[] $testArrayOfUri
     * @return $this Fluent Builder
     */
    public function setTestArrayOfUri(array $testArrayOfUri): self
    {
        $this->options['testArrayOfUri'] = $testArrayOfUri;
        return $this;
    }

    /**
     * Provide a friendly representation
     *
     * @return string Machine friendly representation
     */
    public function __toString(): string
    {
        $options = \http_build_query(Values::of($this->options), '', ' ');
        return '[Twilio.Api.V2010.CreateCallOptions ' . $options . ']';
    }
}



