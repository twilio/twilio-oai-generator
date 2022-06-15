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

import com.twilio.base.Deleter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.converter.PrefixedCollapsibleMap;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import java.time.LocalDate;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.ZonedDateTime;
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


public class RecordingDeleter extends Deleter<Recording> {
    private String conferenceSid;
    private String sid;
    private String accountSid;

    public RecordingDeleter(final String conferenceSid, final String sid){
        this.conferenceSid = conferenceSid;
        this.sid = sid;
    }
    public RecordingDeleter(final String accountSid, final String conferenceSid, final String sid){
        this.accountSid = accountSid;
        this.conferenceSid = conferenceSid;
        this.sid = sid;
    }


    @Override
    public boolean delete(final TwilioRestClient client) {
        String path = "/2010-04-01/Accounts/{AccountSid}/Conferences/{ConferenceSid}/Recordings/{Sid}.json";
        this.accountSid = this.accountSid == null ? client.getAccountSid() : this.accountSid;
        path = path.replace("{"+"AccountSid"+"}", this.accountSid.toString());
        path = path.replace("{"+"ConferenceSid"+"}", this.conferenceSid.toString());
        path = path.replace("{"+"Sid"+"}", this.sid.toString());

        Request request = new Request(
            HttpMethod.DELETE,
            Domains.API.toString(),
            path
        );
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("Recording delete failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }
        return response.getStatusCode() == 204;
    }
}

