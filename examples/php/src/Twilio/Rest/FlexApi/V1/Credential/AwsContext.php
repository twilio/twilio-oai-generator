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
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;
use Twilio\Rest\FlexApi\V1\AwsPage;


/**
 * @method \Twilio\Rest\FlexApi\V1\Credential\Aws\HistoryContext history()
 */

class AwsContext extends InstanceContext {

    /**
     * Initialize the AwsContext
     *
     * @param Version $version Version that contains the resource
     * @param string $sid 
     */
    public function __construct(Version $version, $sid ) {
        parent::__construct($version);

        // Path Solution
        $this->solution = ['sid' => $sid,  ];

        $this->uri = '/Credentials/AWS/' . \rawurlencode($sid) . '';
    }

    /**
     * Delete the AwsInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool {
        return $this->version->delete('DELETE', $this->uri);
    }

    /**
     * Fetch the AwsInstance
     *
     * @return AwsInstance Fetched AwsInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): AwsInstance {

        $payload = $this->version->fetch('GET', $this->uri);

        return new AwsInstance($this->version, $payload, $this->solution['sid']);
    }

    /**
    * Update the AwsInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AwsInstance Updated AwsInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function update(array $options = []): AwsInstance {
        $options = new Values($options);

        $data = Values::of([
            'TestString' => $options['testString'],
            'TestBoolean' => Serialize::booleanToString($options['testBoolean']),
        ]);

        $payload = $this->version->update('POST', $this->uri, [], $data);

        return new AwsInstance($this->version, $payload, $this->solution['sid']);
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
        return '[Twilio.FlexApi.V1.AwsContext ' . \implode(' ', $context) . ']';
    }
}
