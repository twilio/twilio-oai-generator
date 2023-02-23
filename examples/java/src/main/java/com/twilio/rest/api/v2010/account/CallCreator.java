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

package com.twilio.rest.api.v2010.account;

import com.twilio.base.Creator;
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
import java.time.LocalDate;
import com.twilio.converter.Converter;
import java.time.ZonedDateTime;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import com.twilio.converter.DateConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;
import java.net.URI;

public class CallCreator extends Creator<Call>{
    private String requiredStringProperty;
    private HttpMethod testMethod;
    private String pathAccountSid;
    private List<String> testArrayOfStrings;
    private List<URI> testArrayOfUri;

    public CallCreator(final String requiredStringProperty, final HttpMethod testMethod) {
        this.requiredStringProperty = requiredStringProperty;
        this.testMethod = testMethod;
    }
    public CallCreator(final String pathAccountSid, final String requiredStringProperty, final HttpMethod testMethod) {
        this.pathAccountSid = pathAccountSid;
        this.requiredStringProperty = requiredStringProperty;
        this.testMethod = testMethod;
    }

    public CallCreator setRequiredStringProperty(final String requiredStringProperty){
        this.requiredStringProperty = requiredStringProperty;
        return this;
    }
    public CallCreator setTestMethod(final HttpMethod testMethod){
        this.testMethod = testMethod;
        return this;
    }
    public CallCreator setTestArrayOfStrings(final List<String> testArrayOfStrings){
        this.testArrayOfStrings = testArrayOfStrings;
        return this;
    }
    public CallCreator setTestArrayOfStrings(final String testArrayOfStrings){
        return setTestArrayOfStrings(Promoter.listOfOne(testArrayOfStrings));
    }
    public CallCreator setTestArrayOfUri(final List<URI> testArrayOfUri){
        this.testArrayOfUri = testArrayOfUri;
        return this;
    }
    public CallCreator setTestArrayOfUri(final URI testArrayOfUri){
        return setTestArrayOfUri(Promoter.listOfOne(testArrayOfUri));
    }

    public CallCreator setTestArrayOfUri(final String testArrayOfUri){
        return setTestArrayOfUri(Promoter.uriFromString(testArrayOfUri));
    }

    @Override
    public Call create(final TwilioRestClient client){
        String path = String.format("%s", "/2010-04-01/Accounts/{AccountSid}/Calls.json");

        this.pathAccountSid = this.pathAccountSid == null ? client.getAccountSid() : this.pathAccountSid;
        path = path.replace("{"+"AccountSid"+"}", this.pathAccountSid);
        path = path.replace("{"+"RequiredStringProperty"+"}", this.requiredStringProperty);

        path = path.replace("{"+"TestMethod"+"}", this.testMethod.toString());


        Request request = new Request(
            HttpMethod.POST,
            Domains.API.toString(),
            path
        );
        addPostParams(request);
        Response response = client.request(request);
        if (response == null) {
            throw new ApiConnectionException("Call creation failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Call.fromJson(response.getStream(), client.getObjectMapper());
    }
    private void addPostParams(final Request request) {
        request.addPostParam("RequiredStringProperty", String.valueOf(requiredStringProperty));
    
        if (testArrayOfStrings != null) {
            for (String prop : testArrayOfStrings) {
                request.addPostParam("TestArrayOfStrings", prop);
            }
        }
    
        if (testArrayOfUri != null) {
            for (URI prop : testArrayOfUri) {
                request.addPostParam("TestArrayOfUri", String.valueOf(prop));
            }
        }
    
        request.addPostParam("TestMethod", String.valueOf(testMethod));
    
    }

}
