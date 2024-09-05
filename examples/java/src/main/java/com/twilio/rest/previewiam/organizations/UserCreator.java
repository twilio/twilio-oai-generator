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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.constant.EnumConstants;
import com.twilio.converter.Promoter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.converter.PrefixedCollapsibleMap;
import com.twilio.converter.Converter;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Response;
import com.twilio.rest.Domains;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import com.twilio.converter.Converter;
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
import java.util.UUID;

import lombok.ToString;

import java.net.URI;

import com.twilio.base.bearertoken.Creator;
import com.twilio.http.bearertoken.BearerTokenRequest;
import com.twilio.http.bearertoken.BearerTokenTwilioRestClient;

public class UserCreator extends Creator<User>{
    private String pathOrganizationSid;
    private User.ScimUser scimUser;

    public UserCreator(final String pathOrganizationSid, final User.ScimUser scimUser) {
        this.pathOrganizationSid = pathOrganizationSid;
        this.scimUser = scimUser;
    }

    public UserCreator setScimUser(final User.ScimUser scimUser){
        this.scimUser = scimUser;
        return this;
    }

    @Override
    public User create(final BearerTokenTwilioRestClient client){
        String path = "/Organizations/{organizationSid}/scim/Users";

        path = path.replace("{"+"organizationSid"+"}", this.pathOrganizationSid.toString());
        path = path.replace("{"+"ScimUser"+"}", this.scimUser.toString());

        BearerTokenRequest request = new BearerTokenRequest(
            HttpMethod.POST,
            Domains.PREVIEWIAM.toString(),
            path
        );
        request.setContentType(EnumConstants.ContentType.JSON);
        addPostParams(request, client);
        Response response = client.request(request);
        if (response == null) {
            throw new ApiConnectionException("User creation failed: Unable to connect to server");
        } else if (!BearerTokenTwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content", response.getStatusCode());
            }
            throw new ApiException(restException);
        }

        return User.fromJson(response.getStream(), client.getObjectMapper());
    }
    private void addPostParams(final BearerTokenRequest request, BearerTokenTwilioRestClient client) {
        ObjectMapper objectMapper = client.getObjectMapper();
        if (scimUser != null) {
            request.setBody(User.toJson(scimUser, objectMapper));
        }
    }
}
