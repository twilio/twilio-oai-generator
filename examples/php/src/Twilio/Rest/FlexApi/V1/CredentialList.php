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
use Twilio\InstanceResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;
use Twilio\Rest\FlexApi\V1\Credential\AwsList;
use Twilio\Rest\FlexApi\V1\Credential\NewCredentialsList;
use Twilio\Rest\FlexApi\V1\CredentialPage;


/**
 * @property AwsList $aws
 * @property NewCredentialsList $newCredentials
 * @method \Twilio\Rest\FlexApi\V1\Credential\AwsContext aws(string $sid)
 */

class CredentialList extends ListResource {
    protected $_aws = null;
    protected $_newCredentials = null;

    /**
     * Construct the CredentialList
     *
     * @param Version $version Version that contains the resource
     */
    public function __construct(Version $version) {
        parent::__construct($version);

        // Path Solution
        $this->solution = [];

        $this->uri = '/Credentials';
    }
    
    
    /**
    * Reads CredentialInstance records from the API as a list.
    * Unlike stream(), this operation is eager and will load `limit` records into
    * memory before returning.
    *
    * @param int $limit Upper limit for the number of records to return. read()
    *                   guarantees to never return more than limit.  Default is no
    *                   limit
    * @param mixed $pageSize Number of records to fetch per request, when not set
    *                        will use the default value of 50 records.  If no
    *                        page_size is defined but a limit is defined, read()
    *                        will attempt to read the limit with the most
    *                        efficient page size, i.e. min(limit, 1000)
    * @return CredentialInstance[] Array of results
    */

    public function read(int $limit = null, $pageSize = null): array {
        return \iterator_to_array($this->stream($limit, $pageSize), false);
    }

    /**
    * Streams CredentialInstance records from the API as a generator stream.
    * This operation lazily loads records as efficiently as possible until the
    * limit
    * is reached.
    * The results are returned as a generator, so this operation is memory
    * efficient.
    *
    * @param int $limit Upper limit for the number of records to return. stream()
    *                   guarantees to never return more than limit.  Default is no
    *                   limit
    * @param mixed $pageSize Number of records to fetch per request, when not set
    *                        will use the default value of 50 records.  If no
    *                        page_size is defined but a limit is defined, stream()
    *                        will attempt to read the limit with the most
    *                        efficient page size, i.e. min(limit, 1000)
    * @return Stream stream of results
    */

    public function stream(int $limit = null, $pageSize = null): Stream {
        $limits = $this->version->readLimits($limit, $pageSize);

        $page = $this->page($limits['pageSize']);

        return $this->version->stream($page, $limits['limit'], $limits['pageLimit']);
    }

    /**
    * Retrieve a single page of CredentialInstance records from the API.
    * Request is executed immediately
    *
    * @param mixed $pageSize Number of records to return, defaults to 50
    * @param string $pageToken PageToken provided by the API
    * @param mixed $pageNumber Page Number, this value is simply for client state
    * @return CredentialPage Page of CredentialInstance
    */

    public function page( $pageSize = Values::NONE, string $pageToken = Values::NONE, $pageNumber = Values::NONE): CredentialPage {

        $params = Values::of([
            'PageToken' => $pageToken,
            'Page' => $pageNumber,
            'PageSize' => $pageSize,
        ]);

        $response = $this->version->page('GET', $this->uri, $params);

        return new CredentialPage($this->version, $response, $this->solution);
    }

    /**
    * Retrieve a specific page of CredentialInstance records from the API.
    * Request is executed immediately
    *
    * @param string $targetUrl API-generated URL for the requested results page
    * @return CredentialPage Page of CredentialInstance
    */

    public function getPage(string $targetUrl): CredentialPage {
        $response = $this->version->getDomain()->getClient()->request(
            'GET',
            $targetUrl
        );

        return new CredentialPage($this->version, $response, $this->solution);
    }


    /**
     * Constructs a CredentialContext
     *
     * @param string $sid 
     */
    public function getContext(string $sid): CredentialContext {
        return new CredentialContext($this->version, $sid);
    }

    /**
     * Access the aws
     */
    protected function getAws(): AwsList {
        if (!$this->_aws) {
            $this->_aws = new AwsList(
                $this->version
            );
        }

        return $this->_aws;
    }

    /**
     * Access the newCredentials
     */
    protected function getNewCredentials(): NewCredentialsList {
        if (!$this->_newCredentials) {
            $this->_newCredentials = new NewCredentialsList(
                $this->version
            );
        }

        return $this->_newCredentials;
    }

    /**
     * Magic getter to lazy load subresources
     *
     * @param string $name Subresource to return
     * @return ListResource The requested subresource
     * @throws TwilioException For unknown subresources
     */
    public function __get(string $name): ListResource {
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
    public function __call(string $name, array $arguments): InstanceContext {
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
    public function __toString(): string {
        return '[Twilio.FlexApi.V1.CredentialList]';
    }
}
