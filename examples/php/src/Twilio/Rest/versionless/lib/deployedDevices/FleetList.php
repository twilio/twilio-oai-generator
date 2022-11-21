<?php

/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Versionless
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

namespace Twilio\Rest\Versionless\DeployedDevices;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;


class FleetList extends ListResource {
    /**
     * Construct the FleetList
     *
     * @param Version $version Version that contains the resource
     * @param string $sid
     */
    public function __construct(Version $version, string $Sid ) {
        parent::__construct($version);
        $this->solution = ['sid' => $Sid  ];
        $this->uri = '/Sid/' . \rawurlencode($Sid)  . '/.json'  ;
    }

        /**
    * Create the FleetInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AccountInstance Created AccountInstance
    * @throws TwilioException When an HTTP error occurs.
    */

    public function create( array $options = []): FleetInstance {
        $options = new Values($options);

        $data = Values::of([
            'FriendlyName' => $options['FriendlyName'],
        ]);

        $payload = $this->version->create('POST', $this->uri, [], $data);

        return new FleetInstance(
            $this->version,
            $payload,
        );
    }

    
    
    

    /**
    * Constructs a FleetContext
    *
    * @param string $sid The unique string that identifies the resource
    */
    public function getContext(string $sid): FleetContext {
        return new FleetContext($this->version);
    }

    /**
    * Provide a friendly representation
    *
    * @return string Machine friendly representation
    */
    public function __toString(): string {
        return '[Twilio.Versionless.DeployedDevices.FleetList]';
    }
}
