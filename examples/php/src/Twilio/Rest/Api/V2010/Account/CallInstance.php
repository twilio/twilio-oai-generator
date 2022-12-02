<?php

/*
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
use Twilio\Rest\Api\V2010\Account\Call\FeedbackCallSummaryList;


/**
 * @property string $accountSid
 * @property string $sid
 * @property string $testString
 * @property int $testInteger
 * @property string $testObject
 * @property string $testDateTime
 * @property string $testNumber
 * @property string $priceUnit
 * @property string $testNumberFloat
 * @property string $testNumberDecimal
 * @property string $testEnum
 * @property string $a2pProfileBundleSid
 * @property int[] $testArrayOfIntegers
 * @property int[][] $testArrayOfArrayOfIntegers
 * @property string[] $testArrayOfObjects
 * @property string[] $testArrayOfEnum
 */

class CallInstance extends InstanceResource {
    /**
     * Initialize the CallInstance
     *
     * @param Version $version Version that contains the resource
     * @param mixed[] $payload The response payload
     * @param string $accountSid 
     * @param int $testInteger INTEGER ID param!!!
     */
    public function __construct(Version $version, array $payload, string $accountSid, int $testInteger = null) {
        parent::__construct($version);

        // Marshaled Properties
        $this->properties = [
            'accountSid' => Values::array_get($payload, 'account_sid'),
            'sid' => Values::array_get($payload, 'sid'),
            'testString' => Values::array_get($payload, 'test_string'),
            'testInteger' => Values::array_get($payload, 'test_integer'),
            'testObject' => Values::array_get($payload, 'test_object'),
            'testDateTime' => Values::array_get($payload, 'test_date_time'),
            'testNumber' => Values::array_get($payload, 'test_number'),
            'priceUnit' => Values::array_get($payload, 'price_unit'),
            'testNumberFloat' => Values::array_get($payload, 'test_number_float'),
            'testNumberDecimal' => Values::array_get($payload, 'test_number_decimal'),
            'testEnum' => Values::array_get($payload, 'test_enum'),
            'a2pProfileBundleSid' => Values::array_get($payload, 'a2p_profile_bundle_sid'),
            'testArrayOfIntegers' => Values::array_get($payload, 'test_array_of_integers'),
            'testArrayOfArrayOfIntegers' => Values::array_get($payload, 'test_array_of_array_of_integers'),
            'testArrayOfObjects' => Values::array_get($payload, 'test_array_of_objects'),
            'testArrayOfEnum' => Values::array_get($payload, 'test_array_of_enum'),
        ];

        $this->solution = ['accountSid' => $accountSid, 'testInteger' => $testInteger ?: $this->properties['testInteger'], ];
    }

    /**
     * Generate an instance context for the instance, the context is capable of
     * performing various actions.  All instance actions are proxied to the context
     *
     * @return CallContext Context for this CallInstance
     */
    protected function proxy(): CallContext {
        if (!$this->context) {
            $this->context = new CallContext(
                $this->version,
                $this->solution['accountSid'],
                $this->solution['testInteger']
            );
        }

        return $this->context;
    }

    /**
     * Delete the CallInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool {
        return $this->proxy()->delete();
    }

    /**
     * Fetch the CallInstance
     *
     * @return CallInstance Fetched CallInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): CallInstance {
        return $this->proxy()->fetch();
    }

    /**
     * Magic getter to access properties
     *
     * @param string $name Property to access
     * @return mixed The requested property
     * @throws TwilioException For unknown properties
     */
    public function __get(string $name) {
        if (\array_key_exists($name, $this->properties)) {
            return $this->properties[$name];
        }

        if (\property_exists($this, '_' . $name)) {
            $method = 'get' . \ucfirst($name);
            return $this->$method();
        }

        throw new TwilioException('Unknown property: ' . $name);
    }

    /**
     * Provide a friendly representation
     *
     * @return string Machine friendly representation
     */
    public function __toString(): string {
        $context = [];
        foreach ($this->solution as $key => $value) {
            $context[] = "$key=$value";
        }
        return '[Twilio.Api.V2010.CallInstance ' . \implode(' ', $context) . ']';
    }
}

