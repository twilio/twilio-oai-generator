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


/**
 * @method \Twilio\Rest\Api\V2010\Account\Call\FeedbackCallSummaryContext feedbackCallSummary(string $sid)
 */

class CallList extends ListResource {
    /**
     * Construct the CallList
     *
     * @param Version $version Version that contains the resource
     * @param string $accountSid 
     */
    public function __construct(Version $version, string $accountSid ) {
        parent::__construct($version);

        // Path Solution
        $this->solution = ['account_sid' => $accountSid, ];

        $this->uri = '/Accounts/' . \rawurlencode($accountSid) . '/Calls.json';
    }
    
    /**
     * Create the CallInstance
     *
     * @param string $requiredStringProperty 
     * @param string $testMethod The HTTP method that we should use to request the &#x60;TestArrayOfUri&#x60;.
     * @param array|Options $options Optional Arguments
     * @return CallInstance Created CallInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function create(string $requiredStringProperty, string $testMethod, array $options = []): CallInstance {
        $options = new Values($options);


        $data = Values::of([
            'RequiredStringProperty' => $requiredStringProperty,
            'TestMethod' => $testMethod,
            'TestArrayOfStrings' => Serialize::map($options['testArrayOfStrings'], function($e) { return $e; }),
            'TestArrayOfUri' => Serialize::map($options['testArrayOfUri'], function($e) { return $e; }),
        ]);

        $payload = $this->version->create('POST', $this->uri, [], $data);

        return new CallInstance(
            $this->version,
            $payload
            , $this->solution['accountSid']
        );
    }

    
    
    
    
    
    /**
     * Constructs a CallContext
     *
     * @param string $sid The unique string that identifies the resource
     */
    public function getContext(string $sid): CallContext {
        return new CallContext($this->version);
    }

    /**
     * Provide a friendly representation
     *
     * @return string Machine friendly representation
     */
    public function __toString(): string {
        return '[Twilio.Api.V2010.CallList]';
    }
}
