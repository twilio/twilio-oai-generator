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


namespace Twilio\Rest\Api\V2010;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;
use Twilio\Rest\FlexApi\V1\AwsPage;


/**
 * @property string $accountSid
 * @property string $sid
 * @property string $testString
 * @property int $testInteger
 */

class AwsInstance extends InstanceResource {
    /**
     * Initialize the AwsInstance
     *
     * @param Version $version Version that contains the resource
     * @param mixed[] $payload The response payload
     */
    public function __construct(Version $version, array $payload) {
        parent::__construct($version);

        // Marshaled Properties
        $this->properties = [
            'accountSid' => Values::array_get($payload, 'account_sid'),
            'sid' => Values::array_get($payload, 'sid'),
            'testString' => Values::array_get($payload, 'test_string'),
            'testInteger' => Values::array_get($payload, 'test_integer'),
        ];

        $this->solution = [];
    }

    /**
    * Generate an instance context for the instance, the context is capable of
    * performing various actions.  All instance actions are proxied to the context
    *
    * @return FeedbackContext Context for this FeedbackInstance
    */
    protected function proxy(): AwsContext {
        if (!$this->context) {
            $this->context = new AwsContext($this->version );
        }
        return $this->context;
    }

    /**
    * Fetch the AwsInstance
    *
    * @return AwsInstance Fetched AwsInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function fetch(): AwsInstance {
        return $this->proxy()->fetch();
    }

    /**
    * Create the AwsInstance
    *
    * @param int $qualityScore The call quality expressed as an integer from 1 to 5
    * @param array|Options $options Optional Arguments
    * @return AwsInstance Created AwsInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function create(int $qualityScore, array $options = []): AwsInstance {
        return $this->proxy()->create($qualityScore, $options);
    }

    /**
    * Update the AwsInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AwsInstance Updated AwsInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function update(array $options = []): FeedbackInstance {
        return $this->proxy()->update($options);
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
        return '[Twilio.FlexApi.V1.AwsInstance ' . \implode(' ', $context) . ']';
    }
}

