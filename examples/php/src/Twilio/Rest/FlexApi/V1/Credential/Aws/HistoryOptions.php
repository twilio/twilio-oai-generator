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

namespace Twilio\Rest\FlexApi\V1\Credential\Aws;

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

abstract class HistoryOptions
{
    /**
     * @param string $addOnsData
     * @return FetchHistoryOptions Options builder
     */
    public static function fetch(
        string $addOnsData = Values::NONE
    

    ): FetchHistoryOptions
    {
        return new FetchHistoryOptions(
            $addOnsData);
    }

}

class FetchHistoryOptions extends Options
    {
    /**
     * @param string $addOnsData
     */
    public function __construct(
        string $addOnsData = Values::NONE
    

    )
    {
        $this->options['addOnsData'] = $addOnsData;
    }

    /**
     * @param string $addOnsData
     * @return $this Fluent Builder
     */
    public function setAddOnsData(string $addOnsData): self
    {
        $this->options['addOnsData'] = $addOnsData;
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
        return '[Twilio.FlexApi.V1.FetchHistoryOptions ' . $options . ']';
    }
}

