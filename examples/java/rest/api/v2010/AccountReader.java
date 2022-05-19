/*
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * The version of the OpenAPI document: 1.11.0
 * Contact: support@twilio.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.twilio.rest.api.v2010;

import com.twilio.base.Reader;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import com.twilio.base.Page;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import com.twilio.converter.DateConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;



/*
    * Twilio - Accounts
    *
    * This is the public Twilio REST API.
    *
    * API version: 1.11.0
    * Contact: support@twilio.com
*/

// Code generated by OpenAPI Generator (https://openapi-generator.tech); DO NOT EDIT.


public class AccountReader extends Reader<Account> {
    private ZonedDateTime dateCreated;
    private LocalDate dateTest;
    private ZonedDateTime dateCreatedBefore;
    private ZonedDateTime dateCreatedAfter;
    private Integer pageSize;

    public AccountReader(){
    }

    public AccountReader setDateCreated(final ZonedDateTime dateCreated){
        this.dateCreated = dateCreated;
        return this;
    }
    public AccountReader setDateTest(final LocalDate dateTest){
        this.dateTest = dateTest;
        return this;
    }
    public AccountReader setDateCreatedBefore(final ZonedDateTime dateCreatedBefore){
        this.dateCreatedBefore = dateCreatedBefore;
        return this;
    }
    public AccountReader setDateCreatedAfter(final ZonedDateTime dateCreatedAfter){
        this.dateCreatedAfter = dateCreatedAfter;
        return this;
    }
    public AccountReader setPageSize(final Integer pageSize){
        this.pageSize = pageSize;
        return this;
    }


    @Override
    public ResourceSet<Account> read(final TwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage(client));
    }

    public Page<Account> firstPage(final TwilioRestClient client) {
        String path = "/2010-04-01/Accounts.json";

        Request request = new Request(
            HttpMethod.GET,
            Domains.API.toString(),
            path
        );

        addQueryParams(request);
        return pageForRequest(client, request);
    }

    private Page<Account> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("Account read failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Page.fromJson(
            "accounts",
            response.getContent(),
            Account.class,
            client.getObjectMapper()
        );
    }

    @Override
    public Page<Account> previousPage(final Page<Account> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getPreviousPageUrl(Domains.API.toString())
        );
        return pageForRequest(client, request);
    }


    @Override
    public Page<Account> nextPage(final Page<Account> page, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getNextPageUrl(Domains.API.toString())
        );
        return pageForRequest(client, request);
    }

    @Override
    public Page<Account> getPage(final String targetUrl, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            targetUrl
        );

        return pageForRequest(client, request);
    }
    private void addQueryParams(final Request request) {
        if (dateCreated != null) {
            request.addQueryParam("DateCreated", dateCreated.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT)));
        }
        else if (dateCreatedAfter != null || dateCreatedBefore != null) {
            request.addQueryDateTimeRange("DateCreated", dateCreatedAfter, dateCreatedBefore);
        }
        if (dateTest != null) {
            request.addQueryParam("Date.Test", DateConverter.dateStringFromLocalDate(dateTest));
        }

        if (pageSize != null) {
            
            request.addQueryParam("PageSize", pageSize.toString());
        }
    }
}

