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


/**
 * @property string $sid
 * @property string $friendlyName
 */

class AssistantInstance extends InstanceResource {
    /**
     * Initialize the AssistantInstance
     *
     * @param Version $version Version that contains the resource
     * @param mixed[] $payload The response payload
     */
    public function __construct(Version $version, array $payload) {
        parent::__construct($version);

        // Marshaled Properties
        $this->properties = [
            'sid' => Values::array_get($payload, 'sid'),
            'friendlyName' => Values::array_get($payload, 'friendly_name'),
        ];

        $this->solution = [];
    }

    /**
    * Generate an instance context for the instance, the context is capable of
    * performing various actions.  All instance actions are proxied to the context
    *
    * @return FeedbackContext Context for this FeedbackInstance
    */
    protected function proxy(): AssistantContext {
        if (!$this->context) {
            $this->context = new AssistantContext($this->version );
        }
        return $this->context;
    }

    /**
    * Fetch the AssistantInstance
    *
    * @return AssistantInstance Fetched AssistantInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function fetch(): AssistantInstance {
        return $this->proxy()->fetch();
    }

    /**
    * Create the AssistantInstance
    *
    * @param int $qualityScore The call quality expressed as an integer from 1 to 5
    * @param array|Options $options Optional Arguments
    * @return AssistantInstance Created AssistantInstance
    * @throws TwilioException When an HTTP error occurs.
    */
    public function create(int $qualityScore, array $options = []): AssistantInstance {
        return $this->proxy()->create($qualityScore, $options);
    }

    /**
    * Update the AssistantInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AssistantInstance Updated AssistantInstance
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
        return '[Twilio.Versionless.Understand.AssistantInstance ' . \implode(' ', $context) . ']';
    }
}

