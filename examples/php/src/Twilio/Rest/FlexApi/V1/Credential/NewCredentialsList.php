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



class NewCredentialsList extends ListResource {

    /**
     * Construct the NewCredentialsList
     *
     * @param Version $version Version that contains the resource
     */
    public function __construct(Version $version) {
        parent::__construct($version);

        // Path Solution
        $this->solution = [];

        $this->uri = '/Credentials/AWS';
    }
    
    /**
     * Create the NewCredentialsInstance
     *
     * @param string $testString 
     * @param array|Options $options Optional Arguments
     * @return NewCredentialsInstance Created NewCredentialsInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function create(string $testString, array $options = []): NewCredentialsInstance {
        $options = new Values($options);


        $data = Values::of([
            'TestString' => $testString,
            'TestBoolean' => Serialize::booleanToString($options['testBoolean']),
            'TestInteger' => $options['testInteger'],
            'TestNumber' => $options['testNumber'],
            'TestNumberFloat' => $options['testNumberFloat'],
            'TestNumberDouble' => $options['testNumberDouble'],
            'TestNumberInt32' => $options['testNumberInt32'],
            'TestNumberInt64' => $options['testNumberInt64'],
            'TestObject' => Serialize::jsonObject($options['testObject']),
            'TestDateTime' => Serialize::iso8601DateTime($options['testDateTime']),
            'TestDate' => Serialize::iso8601Date($options['testDate']),
            'TestEnum' => $options['testEnum'],
            'TestObjectArray' => Serialize::map($options['testObjectArray'], function($e) { return $e; }),
            'TestAnyType' => $options['testAnyType'],
            'TestAnyArray' => Serialize::map($options['testAnyArray'], function($e) { return $e; }),
            'Permissions' => Serialize::map($options['permissions'], function($e) { return $e; }),
            'SomeA2PThing' => $options['someA2PThing'],
        ]);

        $payload = $this->version->create('POST', $this->uri, [], $data );

        return new NewCredentialsInstance(
            $this->version,
            $payload
        );
    }

    
    /**
     * Constructs a NewCredentialsContext
     *
     */
    public function getContext(): NewCredentialsContext {
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
