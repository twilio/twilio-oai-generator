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

namespace Twilio\Rest\FlexApi\V1\Credential;

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
use Twilio\Rest\FlexApi\V1\Credential\Aws\HistoryList;

abstract class AwsOptions
{



    /**
     * @param string $testString
     * @param bool $testBoolean
     * @return UpdateAwsOptions Options builder
     */
    public static function update(
        
        string $testString = Values::NONE,
        bool $testBoolean = Values::NONE

    ): UpdateAwsOptions
    {
        return new UpdateAwsOptions(
            $testString,
            $testBoolean
        );
    }

}




class UpdateAwsOptions extends Options
    {
    /**
     * @param string $testString
     * @param bool $testBoolean
     */
    public function __construct(
        
        string $testString = Values::NONE,
        bool $testBoolean = Values::NONE

    ) {
        $this->options['testString'] = $testString;
        $this->options['testBoolean'] = $testBoolean;
    }

    /**
     * @param string $testString
     * @return $this Fluent Builder
     */
    public function setTestString(string $testString): self
    {
        $this->options['testString'] = $testString;
        return $this;
    }

    /**
     * @param bool $testBoolean
     * @return $this Fluent Builder
     */
    public function setTestBoolean(bool $testBoolean): self
    {
        $this->options['testBoolean'] = $testBoolean;
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
        return '[Twilio.FlexApi.V1.UpdateAwsOptions ' . $options . ']';
    }
}

