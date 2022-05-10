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

package com.twilio.rest.api.v2010.account.call;

import com.twilio.base.Creator;
import com.twilio.converter.Promoter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import com.twilio.converter.Converter;
import java.time.ZonedDateTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;


import java.net.URI;

/*
    * Twilio - Accounts
    *
    * This is the public Twilio REST API.
    *
    * API version: 1.11.0
    * Contact: support@twilio.com
*/

// Code generated by OpenAPI Generator (https://openapi-generator.tech); DO NOT EDIT.


public class RecordingCreator extends Creator<Recording>{
    private String CallSid;
    private String XTwilioWebhookEnabled;
    private String AccountSid;
    private URI RecordingStatusCallback;
    private List<String> RecordingStatusCallbackEvent;

    public RecordingCreator(final String CallSid) {
        this.CallSid = CallSid;
    }
    public RecordingCreator(final String AccountSid, final String CallSid) {
        this.AccountSid = AccountSid;
        this.CallSid = CallSid;
    }

    public RecordingCreator setXTwilioWebhookEnabled(final String XTwilioWebhookEnabled){
        this.XTwilioWebhookEnabled = XTwilioWebhookEnabled;
        return this;
    }
    public RecordingCreator setRecordingStatusCallback(final URI RecordingStatusCallback){
        this.RecordingStatusCallback = RecordingStatusCallback;
        return this;
    }
    public RecordingCreator setRecordingStatusCallbackEvent(final List<String> RecordingStatusCallbackEvent){
        this.RecordingStatusCallbackEvent = RecordingStatusCallbackEvent;
        return this;
    }

    @Override
    public Recording create(final TwilioRestClient client){
        String path = "/2010-04-01/Accounts/{AccountSid}/Calls/{CallSid}/Recordings.json";
        this.AccountSid = this.AccountSid == null ? client.getAccountSid() : this.AccountSid;
        path = path.replace("{"+"AccountSid"+"}", this.AccountSid.toString());
        path = path.replace("{"+"CallSid"+"}", this.CallSid.toString());

        Request request = new Request(
            HttpMethod.POST,
            Domains.API.toString(),
            path
        );
        addPostParams(request);
        addHeaderParams(request);
        Response response = client.request(request);
        if (response == null) {
            throw new ApiConnectionException("Recording creation failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return Recording.fromJson(response.getStream(), client.getObjectMapper());
    }
    private void addPostParams(final Request request) {
        if (RecordingStatusCallback != null){
        request.addPostParam("RecordingStatusCallback", RecordingStatusCallback.toString());
        }
        if (RecordingStatusCallbackEvent != null){
        request.addPostParam("RecordingStatusCallbackEvent", RecordingStatusCallbackEvent.toString());
        }
    }
    private void addHeaderParams(final Request request) {
        if (XTwilioWebhookEnabled != null) {
           request.addHeaderParam("X-Twilio-Webhook-Enabled", XTwilioWebhookEnabled.toString());
        }
    }
}


