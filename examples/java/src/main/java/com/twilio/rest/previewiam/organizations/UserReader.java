/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Organization Public API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.twilio.rest.previewiam.organizations;

import com.twilio.converter.Promoter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.converter.PrefixedCollapsibleMap;
import com.twilio.converter.Converter;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Response;
import com.twilio.rest.Domains;
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

import com.twilio.base.bearertoken.Page;
import com.twilio.base.bearertoken.Reader;
import com.twilio.base.bearertoken.ResourceSet;
import com.twilio.http.bearertoken.BearerTokenRequest;
import com.twilio.http.bearertoken.BearerTokenTwilioRestClient;

public class UserReader extends Reader<User> {
    private String pathOrganizationSid;
    private String filter;

    public UserReader(final String pathOrganizationSid){
        this.pathOrganizationSid = pathOrganizationSid;
    }

    public UserReader setFilter(final String filter){
        this.filter = filter;
        return this;
    }

    @Override
    public ResourceSet<User> read(final BearerTokenTwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage(client));
    }

    public Page<User> firstPage(final BearerTokenTwilioRestClient client) {
        String path = "/Organizations/{organizationSid}/scim/Users";
        path = path.replace("{"+"organizationSid"+"}", this.pathOrganizationSid.toString());

        BearerTokenRequest request = new BearerTokenRequest(
            HttpMethod.GET,
            Domains.PREVIEWIAM.toString(),
            path
        );

        addQueryParams(request);
        return pageForRequest(client, request);
    }

    private Page<User> pageForRequest(final BearerTokenTwilioRestClient client, final BearerTokenRequest request) {
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("User read failed: Unable to connect to server");
        } else if (!BearerTokenTwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content", response.getStatusCode());
            }
            throw new ApiException(restException);
        }

        return Page.fromJson(
            "Resources",
            response.getContent(),
            User.class,
            client.getObjectMapper()
        );
    }

    @Override
    public Page<User> previousPage(final Page<User> page, final BearerTokenTwilioRestClient client) {
        BearerTokenRequest request = new BearerTokenRequest(
            HttpMethod.GET,
            page.getPreviousPageUrl(Domains.PREVIEWIAM.toString())
        );
        return pageForRequest(client, request);
    }


    @Override
    public Page<User> nextPage(final Page<User> page, final BearerTokenTwilioRestClient client) {
        BearerTokenRequest request = new BearerTokenRequest(
            HttpMethod.GET,
            page.getNextPageUrl(Domains.PREVIEWIAM.toString())
        );
        return pageForRequest(client, request);
    }

    @Override
    public Page<User> getPage(final String targetUrl, final BearerTokenTwilioRestClient client) {
        BearerTokenRequest request = new BearerTokenRequest(
            HttpMethod.GET,
            targetUrl
        );

        return pageForRequest(client, request);
    }
    private void addQueryParams(final BearerTokenRequest request) {
        if (filter != null) {
    
            request.addQueryParam("filter", filter);
        }

        if(getPageSize() != null) {
            request.addQueryParam("PageSize", Integer.toString(getPageSize()));
        }
    }
}
