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

package com.twilio.rest.api.v2010.account.call;

import com.twilio.base.Updater;
import com.twilio.constant.EnumConstants;
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
import java.time.format.DateTimeFormatter;
import com.twilio.converter.DateConverter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;

public class FeedbackCallSummaryUpdater extends Updater<FeedbackCallSummary>{
    private String pathSid;
    private LocalDate endDate;
    private LocalDate startDate;
    private String pathAccountSid;
    private String accountSid;

    public FeedbackCallSummaryUpdater(final String pathSid, final LocalDate endDate, final LocalDate startDate){
        this.pathSid = pathSid;
        this.endDate = endDate;
        this.startDate = startDate;
    }
    public FeedbackCallSummaryUpdater(final String pathAccountSid, final String pathSid, final LocalDate endDate, final LocalDate startDate){
        this.pathAccountSid = pathAccountSid;
        this.pathSid = pathSid;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public FeedbackCallSummaryUpdater setEndDate(final LocalDate endDate){
        this.endDate = endDate;
        return this;
    }
    public FeedbackCallSummaryUpdater setStartDate(final LocalDate startDate){
        this.startDate = startDate;
        return this;
    }
    public FeedbackCallSummaryUpdater setAccountSid(final String accountSid){
        this.accountSid = accountSid;
        return this;
    }

    @Override
    public FeedbackCallSummary update(final TwilioRestClient client){
        String path = "/2010-04-01/Accounts/{AccountSid}/Calls/Feedback/Summary/{Sid}.json";

        this.pathAccountSid = this.pathAccountSid == null ? client.getAccountSid() : this.pathAccountSid;
        path = path.replace("{"+"AccountSid"+"}", this.pathAccountSid.toString());
        path = path.replace("{"+"Sid"+"}", this.pathSid.toString());
        path = path.replace("{"+"EndDate"+"}", this.endDate.toString());
        path = path.replace("{"+"StartDate"+"}", this.startDate.toString());

        Request request = new Request(
            HttpMethod.POST,
            Domains.API.toString(),
            path
        );
        request.setContentType(EnumConstants.ContentType.FORM_URLENCODED);
        addPostParams(request);
        Response response = client.request(request);
        if (response == null) {
            throw new ApiConnectionException("FeedbackCallSummary update failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
            throw new ApiException(restException);
        }

        return FeedbackCallSummary.fromJson(response.getStream(), client.getObjectMapper());
    }
    private void addPostParams(final Request request) {
        if (accountSid != null) {
            request.addPostParam("AccountSid", accountSid);
    
        }
        if (endDate != null) {
            request.addPostParam("EndDate", DateConverter.dateStringFromLocalDate(endDate));

        }
        if (startDate != null) {
            request.addPostParam("StartDate", DateConverter.dateStringFromLocalDate(startDate));

        }
    }
}
