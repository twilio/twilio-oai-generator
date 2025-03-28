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
use Twilio\Base\PhoneNumberCapabilities;
use Twilio\Rest\FlexApi\V1\Credential\Aws\HistoryList;


/**
 * @property HistoryList $history
 * @method \Twilio\Rest\FlexApi\V1\Credential\Aws\HistoryContext history()
 */
class AwsContext extends InstanceContext
    {
    protected $_history;

    /**
     * Initialize the AwsContext
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

        $this->uri = '/Credentials/AWS/' . \rawurlencode($sid)
        .'';
    }

    /**
     * Delete the AwsInstance
     *
     * @return bool True if delete succeeds, false otherwise
     * @throws TwilioException When an HTTP error occurs.
     */
    public function delete(): bool
    {

        $headers = Values::of(['Content-Type' => 'application/x-www-form-urlencoded',  ]);
        return $this->version->delete('DELETE', $this->uri, [], [], $headers);
    }


    /**
     * Fetch the AwsInstance
     *
     * @return AwsInstance Fetched AwsInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function fetch(): AwsInstance
    {

        $headers = Values::of(['Content-Type' => 'application/x-www-form-urlencoded', 'Accept' => 'application/json' ]);
        $payload = $this->version->fetch('GET', $this->uri, [], [], $headers);

        return new AwsInstance(
            $this->version,
            $payload,
            $this->solution['sid']
        );
    }


    /**
     * Update the AwsInstance
     *
     * @param array|Options $options Optional Arguments
     * @return AwsInstance Updated AwsInstance
     * @throws TwilioException When an HTTP error occurs.
     */
    public function update(array $options = []): AwsInstance
    {

        $options = new Values($options);

        $data = Values::of([
            'TestString' =>
                $options['testString'],
            'TestBoolean' =>
                Serialize::booleanToString($options['testBoolean']),
        ]);

        $headers = Values::of(['Content-Type' => 'application/x-www-form-urlencoded', 'Accept' => 'application/json' ]);
        $payload = $this->version->update('POST', $this->uri, [], $data, $headers);

        return new AwsInstance(
            $this->version,
            $payload,
            $this->solution['sid']
        );
    }


    /**
     * Access the history
     */
    protected function getHistory(): HistoryList
    {
        if (!$this->_history) {
            $this->_history = new HistoryList(
                $this->version,
                $this->solution['sid']
            );
        }

        return $this->_history;
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
        return '[Twilio.FlexApi.V1.AwsContext ' . \implode(' ', $context) . ']';
    }
}
