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
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;
use Twilio\Rest\Api\V2010\Account\Call\FeedbackCallSummaryList;



class CallContext extends InstanceContext {

    /**
     * Initialize the CallContext
     *
     * @param Version $version Version that contains the resource
     * @param string $accountSid 
     * @param int $testInteger INTEGER ID param!!!
     */
    public function __construct(Version $version, $accountSid , $testInteger ) {
        parent::__construct($version);

        // Path Solution
        $this->solution = ['account_sid' => $accountSid,  'test_integer' => $testInteger,  ];

        $this->uri = '/Accounts/' . \rawurlencode($accountSid) . '/Calls/' . \rawurlencode($testInteger) . '.json';
    }

    /**
     * Delete the CallInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool {
        return $this->version->delete('DELETE', $this->uri);
    }

    /**
     * Fetch the CallInstance
     *
     * @return CallInstance Fetched CallInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): CallInstance {

        $payload = $this->version->fetch('GET', $this->uri);

        return new CallInstance($this->version, $payload, $this->solution['accountSid'], $this->solution['testInteger']);
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
        return '[Twilio.Api.V2010.CallContext ' . \implode(' ', $context) . ']';
    }
}
