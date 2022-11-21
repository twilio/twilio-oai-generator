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

namespace Twilio\Rest\FlexApi\V1;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;


class NewCredentialsList extends ListResource {
    /**
     * Construct the NewCredentialsList
     *
     * @param Version $version Version that contains the resource
     */
    public function __construct(Version $version) {
        parent::__construct($version);
        $this->solution = [];
    }

        /**
    * Create the NewCredentialsInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AccountInstance Created AccountInstance
    * @throws TwilioException When an HTTP error occurs.
    */

    public function create(string $test_string , array $options = []): NewCredentialsInstance {
        $options = new Values($options);

        $data = Values::of([
            'TestString' => $test_string,
            'TestBoolean' => Serialize::booleanToString($options['TestBoolean']),
            'TestInteger' => $options['TestInteger'],
            'TestNumber' => $options['TestNumber'],
            'TestNumberFloat' => $options['TestNumberFloat'],
            'TestNumberDouble' => $options['TestNumberDouble'],
            'TestNumberInt32' => $options['TestNumberInt32'],
            'TestNumberInt64' => $options['TestNumberInt64'],
            'TestObject' => Serialize::jsonObject($options['TestObject']),
            'TestDateTime' => Serialize::iso8601DateTime($options['TestDateTime']),
            'TestDate' => Serialize::iso8601Date($options['TestDate']),
            'TestEnum' => $options['TestEnum'],
            'TestObjectArray' => Serialize::map($options['TestObjectArray'], function($e) { return $e; }),
            'TestAnyType' => $options['TestAnyType'],
            'TestAnyArray' => Serialize::map($options['TestAnyArray'], function($e) { return $e; }),
            'Permissions' => Serialize::map($options['Permissions'], function($e) { return $e; }),
            'SomeA2PThing' => $options['SomeA2PThing'],
        ]);

        $payload = $this->version->create('POST', $this->uri, [], $data);

        return new NewCredentialsInstance(
            $this->version,
            $payload,
            $this->solution['test_string'],
        );
    }

    

    /**
    * Constructs a NewCredentialsContext
    *
    * @param string $sid The unique string that identifies the resource
    */
    public function getContext(string $sid): NewCredentialsContext {
        return new NewCredentialsContext($this->version);
    }

    /**
    * Provide a friendly representation
    *
    * @return string Machine friendly representation
    */
    public function __toString(): string {
        return '[Twilio.FlexApi.V1.NewCredentialsList]';
    }
}