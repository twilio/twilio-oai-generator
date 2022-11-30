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


namespace Twilio\Rest\FlexApi\V1\Credential\Aws;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;



class HistoryContext extends InstanceContext {

    /**
     * Initialize the HistoryContext
     *
     * @param Version $version Version that contains the resource
     * @param string $sid 
     */
    public function __construct(Version $version, $sid ) {
        parent::__construct($version);

        // Path Solution
        $this->solution = ['sid' => $sid,  ];

        $this->uri = '/Credentials/AWS/' . \rawurlencode($sid) . '/History';
    }

    /**
     * Fetch the HistoryInstance
     *
     * @param array|Options $options Optional Arguments
     * @return HistoryInstance Fetched HistoryInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(array $options = []): HistoryInstance {
        $options = new Values($options);

        $params = Values::of([
        ]);
        $params = \array_merge($params, Serialize::prefixedCollapsibleMap($options['addOnsData'], 'AddOns'));

        $payload = $this->version->fetch('GET', $this->uri, $params);

        return new HistoryInstance($this->version, $payload, $this->solution['sid']);
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
        return '[Twilio.FlexApi.V1.HistoryContext ' . \implode(' ', $context) . ']';
    }
}
