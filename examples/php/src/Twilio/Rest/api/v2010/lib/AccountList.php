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

namespace Twilio\Rest\Api\V2010;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;


class AccountList extends ListResource {
    /**
     * Construct the AccountList
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
    * Create the AccountInstance
    *
    * @param array|Options $options Optional Arguments
    * @return AccountInstance Created AccountInstance
    * @throws TwilioException When an HTTP error occurs.
    */

    public function create( array $options = []): AccountInstance {
        $options = new Values($options);

        $data = Values::of([
            'X-Twilio-Webhook-Enabled' => $options['X-Twilio-Webhook-Enabled'],
            'RecordingStatusCallback' => $options['RecordingStatusCallback'],
            'RecordingStatusCallbackEvent' => Serialize::map($options['RecordingStatusCallbackEvent'], function($e) { return $e; }),
        ]);

        $payload = $this->version->create('POST', $this->uri, [], $data);

        return new AccountInstance(
            $this->version,
            $payload,
        );
    }

    
    
    
    
    
    
    
    /**
    * Reads AccountInstance records from the API as a list.
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
    * @return AccountInstance[] Array of results
    */

    public function read(int $limit = null, $pageSize = null): array {
        return \iterator_to_array($this->stream($limit, $pageSize), false);
    }

    /**
    * Streams AccountInstance records from the API as a generator stream.
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
    * Retrieve a single page of AccountInstance records from the API.
    * Request is executed immediately
    *
    * @param mixed $pageSize Number of records to return, defaults to 50
    * @param string $pageToken PageToken provided by the API
    * @param mixed $pageNumber Page Number, this value is simply for client state
    * @return AccountPage Page of AccountInstance
    */

    public function page(array $options = [], $pageSize = Values::NONE, string $pageToken = Values::NONE, $pageNumber = Values::NONE): AccountPage {
        $params = Values::of([
            'DateCreated' => Serialize::iso8601DateTime($options['DateCreated']),
            'Date.Test' => Serialize::iso8601Date($options['Date.Test']),
            'DateCreated&lt;' => Serialize::iso8601DateTime($options['DateCreated&lt;']),
            'DateCreated&gt;' => Serialize::iso8601DateTime($options['DateCreated&gt;']),
            'PageSize' => $options['PageSize'],
            'PageToken' => $pageToken,
            'Page' => $pageNumber,
            'PageSize' => $pageSize,
        ]);

        $response = $this->version->page('GET', $this->uri, $params);

        return new AccountPage($this->version, $response, $this->solution);
    }

    /**
    * Retrieve a specific page of AccountInstance records from the API.
    * Request is executed immediately
    *
    * @param string $targetUrl API-generated URL for the requested results page
    * @return AccountPage Page of AccountInstance
    */

    public function getPage(string $targetUrl): AccountPage {
        $response = $this->version->getDomain()->getClient()->request(
            'GET',
            $targetUrl
        );

        return new AccountPage($this->version, $response, $this->solution);
    }


    
    

    /**
    * Constructs a AccountContext
    *
    * @param string $sid The unique string that identifies the resource
    */
    public function getContext(string $sid): AccountContext {
        return new AccountContext($this->version);
    }

    /**
    * Provide a friendly representation
    *
    * @return string Machine friendly representation
    */
    public function __toString(): string {
        return '[Twilio.Api.V2010.AccountList]';
    }
}