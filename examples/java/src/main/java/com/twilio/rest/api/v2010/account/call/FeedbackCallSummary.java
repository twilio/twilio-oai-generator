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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.base.Resource;
import com.twilio.converter.Converter;
import java.util.Currency;
import com.twilio.converter.DateConverter;
import com.twilio.converter.Promoter;
import com.twilio.converter.PrefixedCollapsibleMap;
import com.twilio.converter.CurrencyDeserializer;
import com.twilio.exception.ApiConnectionException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;

import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import java.util.Map;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.twilio.type.PhoneNumberCapabilities;
import com.twilio.type.FeedbackIssue;
import com.twilio.type.IceServer;
import com.twilio.type.InboundCallPrice;
import com.twilio.type.OutboundPrefixPriceWithOrigin;
import com.twilio.type.OutboundPrefixPrice;
import com.twilio.type.OutboundCallPriceWithOrigin;
import com.twilio.type.PhoneNumberPrice;
import com.twilio.type.InboundSmsPrice;
import com.twilio.type.OutboundSmsPrice;
import com.twilio.type.OutboundCallPrice;
import com.twilio.type.RecordingRule;
import com.twilio.type.SubscribeRule;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FeedbackCallSummary extends Resource {
    private static final long serialVersionUID = 193205363285633L;





    public static FeedbackCallSummaryUpdater updater(final String pathSid, final LocalDate endDate, final LocalDate startDate){
        return new FeedbackCallSummaryUpdater(pathSid, endDate, startDate);
    }
    public static FeedbackCallSummaryUpdater updater(final String pathAccountSid, final String pathSid, final LocalDate endDate, final LocalDate startDate){
        return new FeedbackCallSummaryUpdater(pathAccountSid, pathSid, endDate, startDate);
    }

    /**
    * Converts a JSON String into a FeedbackCallSummary object using the provided ObjectMapper.
    *
    * @param json Raw JSON String
    * @param objectMapper Jackson ObjectMapper
    * @return FeedbackCallSummary object represented by the provided JSON
    */
    public static FeedbackCallSummary fromJson(final String json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, FeedbackCallSummary.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }

    /**
    * Converts a JSON InputStream into a FeedbackCallSummary object using the provided
    * ObjectMapper.
    *
    * @param json Raw JSON InputStream
    * @param objectMapper Jackson ObjectMapper
    * @return FeedbackCallSummary object represented by the provided JSON
    */
    public static FeedbackCallSummary fromJson(final InputStream json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, FeedbackCallSummary.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }
    public enum Status {
        IN_PROGRESS("in-progress"),
        PAUSED("paused"),
        STOPPED("stopped"),
        PROCESSING("processing"),
        COMPLETED("completed"),
        ABSENT("absent");

        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }

        @JsonCreator
        public static Status forValue(final String value) {
            return Promoter.enumFromString(value, Status.values());
        }
    }

    private final String accountSid;
    private final String sid;
    private final String testString;
    private final Integer testInteger;
    private final PhoneNumberCapabilities testObject;
    private final ZonedDateTime testDateTime;
    private final BigDecimal testNumber;
    private final Currency priceUnit;
    private final Float testNumberFloat;
    private final FeedbackCallSummary.Status testEnum;
    private final List<Integer> testArrayOfIntegers;
    private final List<List<Integer>> testArrayOfArrayOfIntegers;
    private final List<FeedbackIssue> testArrayOfObjects;
    private final List<FeedbackCallSummary.Status> testArrayOfEnum;

    @JsonCreator
    private FeedbackCallSummary(
        @JsonProperty("account_sid")
        final String accountSid,

        @JsonProperty("sid")
        final String sid,

        @JsonProperty("test_string")
        final String testString,

        @JsonProperty("test_integer")
        final Integer testInteger,

        @JsonProperty("test_object")
        final PhoneNumberCapabilities testObject,

        @JsonProperty("test_date_time")
        final String testDateTime,

        @JsonProperty("test_number")
        final BigDecimal testNumber,

        @JsonProperty("price_unit")
        @JsonDeserialize(using = com.twilio.converter.CurrencyDeserializer.class)
        final Currency priceUnit,

        @JsonProperty("test_number_float")
        final Float testNumberFloat,

        @JsonProperty("test_enum")
        final FeedbackCallSummary.Status testEnum,

        @JsonProperty("test_array_of_integers")
        final List<Integer> testArrayOfIntegers,

        @JsonProperty("test_array_of_array_of_integers")
        final List<List<Integer>> testArrayOfArrayOfIntegers,

        @JsonProperty("test_array_of_objects")
        final List<FeedbackIssue> testArrayOfObjects,

        @JsonProperty("test_array_of_enum")
        final List<FeedbackCallSummary.Status> testArrayOfEnum
    ) {
        this.accountSid = accountSid;
        this.sid = sid;
        this.testString = testString;
        this.testInteger = testInteger;
        this.testObject = testObject;
        this.testDateTime = DateConverter.rfc2822DateTimeFromString(testDateTime);
        this.testNumber = testNumber;
        this.priceUnit = priceUnit;
        this.testNumberFloat = testNumberFloat;
        this.testEnum = testEnum;
        this.testArrayOfIntegers = testArrayOfIntegers;
        this.testArrayOfArrayOfIntegers = testArrayOfArrayOfIntegers;
        this.testArrayOfObjects = testArrayOfObjects;
        this.testArrayOfEnum = testArrayOfEnum;
    }

        public final String getAccountSid() {
            return this.accountSid;
        }
        public final String getSid() {
            return this.sid;
        }
        public final String getTestString() {
            return this.testString;
        }
        public final Integer getTestInteger() {
            return this.testInteger;
        }
        public final PhoneNumberCapabilities getTestObject() {
            return this.testObject;
        }
        public final ZonedDateTime getTestDateTime() {
            return this.testDateTime;
        }
        public final BigDecimal getTestNumber() {
            return this.testNumber;
        }
        public final Currency getPriceUnit() {
            return this.priceUnit;
        }
        public final Float getTestNumberFloat() {
            return this.testNumberFloat;
        }
        public final FeedbackCallSummary.Status getTestEnum() {
            return this.testEnum;
        }
        public final List<Integer> getTestArrayOfIntegers() {
            return this.testArrayOfIntegers;
        }
        public final List<List<Integer>> getTestArrayOfArrayOfIntegers() {
            return this.testArrayOfArrayOfIntegers;
        }
        public final List<FeedbackIssue> getTestArrayOfObjects() {
            return this.testArrayOfObjects;
        }
        public final List<FeedbackCallSummary.Status> getTestArrayOfEnum() {
            return this.testArrayOfEnum;
        }

    @Override
    public boolean equals(final Object o) {
        if (this==o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeedbackCallSummary other = (FeedbackCallSummary) o;

        return Objects.equals(accountSid, other.accountSid) &&  Objects.equals(sid, other.sid) &&  Objects.equals(testString, other.testString) &&  Objects.equals(testInteger, other.testInteger) &&  Objects.equals(testObject, other.testObject) &&  Objects.equals(testDateTime, other.testDateTime) &&  Objects.equals(testNumber, other.testNumber) &&  Objects.equals(priceUnit, other.priceUnit) &&  Objects.equals(testNumberFloat, other.testNumberFloat) &&  Objects.equals(testEnum, other.testEnum) &&  Objects.equals(testArrayOfIntegers, other.testArrayOfIntegers) &&  Objects.equals(testArrayOfArrayOfIntegers, other.testArrayOfArrayOfIntegers) &&  Objects.equals(testArrayOfObjects, other.testArrayOfObjects) &&  Objects.equals(testArrayOfEnum, other.testArrayOfEnum)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountSid, sid, testString, testInteger, testObject, testDateTime, testNumber, priceUnit, testNumberFloat, testEnum, testArrayOfIntegers, testArrayOfArrayOfIntegers, testArrayOfObjects, testArrayOfEnum);
    }

}

