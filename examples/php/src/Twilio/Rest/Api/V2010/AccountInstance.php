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


namespace Twilio\Rest\Api\V2010;

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
use Twilio\Rest\Api\V2010\Account\CallList;


/**
 * @property string $accountSid
 * @property string $sid
 * @property string $testString
 * @property int $testInteger
 * @property PhoneNumberCapabilities $testObject
 * @property \DateTime $testDateTime
 * @property string $testNumber
 * @property string $priceUnit
 * @property string $testNumberFloat
 * @property string $testNumberDecimal
 * @property string $testEnum
 * @property string $a2PProfileBundleSid
 * @property int[] $testArrayOfIntegers
 * @property int[][] $testArrayOfArrayOfIntegers
 * @property string[] $testArrayOfObjects
 * @property string[] $testArrayOfEnum
 */
class AccountInstance extends InstanceResource
{
    protected $_calls;

    /**
     * Initialize the AccountInstance
     *
     * @param Version $version Version that contains the resource
     * @param mixed[] $payload The response payload
     * @param string $sid
     */
    public function __construct(Version $version, array $payload, string $sid = null)
    {
        parent::__construct($version);

        // Marshaled Properties
        $this->properties = [
            'accountSid' => Values::array_get($payload, 'account_sid'),
            'sid' => Values::array_get($payload, 'sid'),
            'testString' => Values::array_get($payload, 'test_string'),
            'testInteger' => Values::array_get($payload, 'test_integer'),
            'testObject' => Deserialize::phoneNumberCapabilities(Values::array_get($payload, 'test_object')),
            'testDateTime' => Deserialize::dateTime(Values::array_get($payload, 'test_date_time')),
            'testNumber' => Values::array_get($payload, 'test_number'),
            'priceUnit' => Values::array_get($payload, 'price_unit'),
            'testNumberFloat' => Values::array_get($payload, 'test_number_float'),
            'testNumberDecimal' => Values::array_get($payload, 'test_number_decimal'),
            'testEnum' => Values::array_get($payload, 'test_enum'),
            'a2PProfileBundleSid' => Values::array_get($payload, 'a2p_profile_bundle_sid'),
            'testArrayOfIntegers' => Values::array_get($payload, 'test_array_of_integers'),
            'testArrayOfArrayOfIntegers' => Values::array_get($payload, 'test_array_of_array_of_integers'),
            'testArrayOfObjects' => Values::array_get($payload, 'test_array_of_objects'),
            'testArrayOfEnum' => Values::array_get($payload, 'test_array_of_enum'),
        ];

        $this->solution = ['sid' => $sid ?: $this->properties['sid'], ];
    }

    /**
     * Generate an instance context for the instance, the context is capable of
     * performing various actions.  All instance actions are proxied to the context
     *
     * @return AccountContext Context for this AccountInstance
     */
    protected function proxy(): AccountContext
    {
        if (!$this->context) {
            $this->context = new AccountContext(
                $this->version,
                $this->solution['sid']
            );
        }

        return $this->context;
    }

    /**
     * Delete the AccountInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool
    {

        return $this->proxy()->delete();
    }

    /**
     * Fetch the AccountInstance
     *
     * @return AccountInstance Fetched AccountInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): AccountInstance
    {

        return $this->proxy()->fetch();
    }

    /**
     * Update the AccountInstance
     *
     * @param string $status
     * @param array|Options $options Optional Arguments
     * @return AccountInstance Updated AccountInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function update(string $status, array $options = []): AccountInstance
    {

        return $this->proxy()->update($status, $options);
    }

    /**
     * Access the calls
     */
    protected function getCalls(): CallList
    {
        return $this->proxy()->calls;
    }

    /**
     * Magic getter to access properties
     *
     * @param string $name Property to access
     * @return mixed The requested property
     * @throws TwilioException For unknown properties
     */
    public function __get(string $name)
    {
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
    public function __toString(): string
    {
        $context = [];
        foreach ($this->solution as $key => $value) {
            $context[] = "$key=$value";
        }
        return '[Twilio.Api.V2010.AccountInstance ' . \implode(' ', $context) . ']';
    }
}

