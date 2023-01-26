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
 * @property CallList $calls
 * @method \Twilio\Rest\Api\V2010\Account\CallContext calls(string $testInteger)
 */
class AccountContext extends InstanceContext
    {
    protected $_calls;

    /**
     * Initialize the AccountContext
     *
     * @param Version $version Version that contains the resource
     * @param string $sid
     */
    public function __construct(
        Version $version,
        $sid
    ) {
        parent::__construct($version);

        // Path Solution
        $this->solution = [
        'sid' =>
            $sid,
        ];

        $this->uri = '/Accounts/' . \rawurlencode($sid)
        .'.json';
    }

    /**
     * Delete the AccountInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool
    {

        return $this->version->delete('DELETE', $this->uri);
    }


    /**
     * Fetch the AccountInstance
     *
     * @return AccountInstance Fetched AccountInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): AccountInstance
    {

        $payload = $this->version->fetch('GET', $this->uri);

        return new AccountInstance(
            $this->version,
            $payload,
            $this->solution['sid']
        );
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

        $options = new Values($options);

        $data = Values::of([
            'Status' =>
                $status,
            'PauseBehavior' =>
                $options['pauseBehavior'],
        ]);

        $payload = $this->version->update('POST', $this->uri, [], $data);

        return new AccountInstance(
            $this->version,
            $payload,
            $this->solution['sid']
        );
    }


    /**
     * Access the calls
     */
    protected function getCalls(): CallList
    {
        if (!$this->_calls) {
            $this->_calls = new CallList(
                $this->version,
                $this->solution['sid']
            );
        }

        return $this->_calls;
    }

    /**
     * Magic getter to lazy load subresources
     *
     * @param string $name Subresource to return
     * @return ListResource The requested subresource
     * @throws TwilioException For unknown subresources
     */
    public function __get(string $name): ListResource
    {
        if (\property_exists($this, '_' . $name)) {
            $method = 'get' . \ucfirst($name);
            return $this->$method();
        }

        throw new TwilioException('Unknown subresource ' . $name);
    }

    /**
     * Magic caller to get resource contexts
     *
     * @param string $name Resource to return
     * @param array $arguments Context parameters
     * @return InstanceContext The requested resource context
     * @throws TwilioException For unknown resource
     */
    public function __call(string $name, array $arguments): InstanceContext
    {
        $property = $this->$name;
        if (\method_exists($property, 'getContext')) {
            return \call_user_func_array(array($property, 'getContext'), $arguments);
        }

        throw new TwilioException('Resource does not have a context');
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
        return '[Twilio.Api.V2010.AccountContext ' . \implode(' ', $context) . ']';
    }
}
