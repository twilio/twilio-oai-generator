/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Number Pool Service
 * This service is an entry point for all Number Pool CRUD requests.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.twilio.rest.messaging.v1;

import com.twilio.base.Reader;
import com.twilio.base.ResourceSet;
import com.twilio.converter.Promoter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.converter.PrefixedCollapsibleMap;
import com.twilio.converter.Converter;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import com.twilio.base.Page;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import com.twilio.converter.DateConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;

public class NumberPoolReader extends Reader<NumberPool> {
    private String pathNumberPoolSID;

    public NumberPoolReader(final String pathNumberPoolSID){
        this.pathNumberPoolSID = pathNumberPoolSID;
    }


    @Override
    public ResourceSet<NumberPool> read(final TwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage(client));
    }

    public Page<NumberPool> firstPage(final TwilioRestClient client) {
        String path = "/v2/NumberPools/{numberPoolSID}";
        path = path.replace("{"+"NumberPoolSID"+"}", this.pathNumberPoolSID.toString());

        Request request = new Request(
            HttpMethod.GET,
            Domains.MESSAGING.toString(),
            path
        );

        return pageForRequest(client, request);
    }

    private Page<NumberPool> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("NumberPool read failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Page.fromJson(
            "",
            response.getContent(),
            NumberPool.class,
            client.getObjectMapper()
        );
    }

    @Override
    public Page<NumberPool> previousPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getPreviousPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }


    @Override
    public Page<NumberPool> nextPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getNextPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }

    @Override
    public Page<NumberPool> getPage(final String targetUrl, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            targetUrl
        );

        return pageForRequest(client, request);
    }
}
public class NumberPoolReader extends Reader<NumberPool> {

    public NumberPoolReader(){
    }


    @Override
    public ResourceSet<NumberPool> read(final TwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage(client));
    }

    public Page<NumberPool> firstPage(final TwilioRestClient client) {
        String path = "/v2/NumberPools";

        Request request = new Request(
            HttpMethod.GET,
            Domains.MESSAGING.toString(),
            path
        );

        return pageForRequest(client, request);
    }

    private Page<NumberPool> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("NumberPool read failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Page.fromJson(
            "",
            response.getContent(),
            NumberPool.class,
            client.getObjectMapper()
        );
    }

    @Override
    public Page<NumberPool> previousPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getPreviousPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }


    @Override
    public Page<NumberPool> nextPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getNextPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }

    @Override
    public Page<NumberPool> getPage(final String targetUrl, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            targetUrl
        );

        return pageForRequest(client, request);
    }
}
public class NumberPoolReader extends Reader<NumberPool> {
    private String pathNumberPoolSID;
    private NumberPoolModel.NumberPoolRequest numberPoolRequest;

    public NumberPoolReader(final String pathNumberPoolSID, final NumberPoolModel.NumberPoolRequest numberPoolRequest){
        this.pathNumberPoolSID = pathNumberPoolSID;
        this.numberPoolRequest = numberPoolRequest;
    }

    public NumberPoolReader setNumberPoolRequest(final NumberPoolModel.NumberPoolRequest numberPoolRequest){
        this.numberPoolRequest = numberPoolRequest;
        return this;
    }

    @Override
    public ResourceSet<NumberPool> read(final TwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage(client));
    }

    public Page<NumberPool> firstPage(final TwilioRestClient client) {
        String path = "/v2/NumberPools/{numberPoolSID}";
        path = path.replace("{"+"NumberPoolSID"+"}", this.pathNumberPoolSID.toString());
        path = path.replace("{"+"NumberPoolRequest"+"}", this.numberPoolRequest.toString());

        Request request = new Request(
            HttpMethod.GET,
            Domains.MESSAGING.toString(),
            path
        );

        return pageForRequest(client, request);
    }

    private Page<NumberPool> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("NumberPool read failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Page.fromJson(
            "",
            response.getContent(),
            NumberPool.class,
            client.getObjectMapper()
        );
    }

    @Override
    public Page<NumberPool> previousPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getPreviousPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }


    @Override
    public Page<NumberPool> nextPage(final Page<NumberPool> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getNextPageUrl(Domains.MESSAGING.toString())
        );
        return pageForRequest(client, request);
    }

    @Override
    public Page<NumberPool> getPage(final String targetUrl, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            targetUrl
        );

        return pageForRequest(client, request);
    }
}
