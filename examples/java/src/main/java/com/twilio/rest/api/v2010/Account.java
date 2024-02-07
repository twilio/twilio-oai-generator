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

package com.twilio.rest.api.v2010;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class Account extends Resource {
    private static final long serialVersionUID = 47015198707523L;

    

    public static AccountCreator creator(){
        return new AccountCreator();
    }

    public static AccountDeleter deleter(){
        return new AccountDeleter();
    }
    public static AccountDeleter deleter(final String pathSid){
        return new AccountDeleter(pathSid);
    }

    public static AccountFetcher fetcher(){
        return new AccountFetcher();
    }
    public static AccountFetcher fetcher(final String pathSid){
        return new AccountFetcher(pathSid);
    }

    public static AccountReader reader(){
        return new AccountReader();
    }

    public static AccountUpdater updater(final Account.Status status){
        return new AccountUpdater(status);
    }
    public static AccountUpdater updater(final String pathSid, final Account.Status status){
        return new AccountUpdater(pathSid, status);
    }

    /**
    * Converts a JSON String into a Account object using the provided ObjectMapper.
    *
    * @param json Raw JSON String
    * @param objectMapper Jackson ObjectMapper
    * @return Account object represented by the provided JSON
    */
    public static Account fromJson(final String json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, Account.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }

    /**
    * Converts a JSON InputStream into a Account object using the provided
    * ObjectMapper.
    *
    * @param json Raw JSON InputStream
    * @param objectMapper Jackson ObjectMapper
    * @return Account object represented by the provided JSON
    */
    public static Account fromJson(final InputStream json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, Account.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }

    public enum XTwilioWebhookEnabled {
        TRUE("true"),
        FALSE("false");

        private final String value;

        private XTwilioWebhookEnabled(final String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }

        @JsonCreator
        public static XTwilioWebhookEnabled forValue(final String value) {
            return Promoter.enumFromString(value, XTwilioWebhookEnabled.values());
        }
    }

    private final String accountSid;
    private final String sid;
    private final String testString;
    private final Integer testInteger;
    private final PhoneNumberCapabilities testObject;
    private final ZonedDateTime testDateTime;
    private final BigDecimal testNumber;
    private final com.twilio.type.PhoneNumber from;
    private final Currency priceUnit;
    private final Float testNumberFloat;
    private final BigDecimal testNumberDecimal;
    private final Account.Status testEnum;
    private final String a2pProfileBundleSid;
    private final List<Integer> testArrayOfIntegers;
    private final List<List<Integer>> testArrayOfArrayOfIntegers;
    private final List<FeedbackIssue> testArrayOfObjects;
    private final List<Account.Status> testArrayOfEnum;

    @JsonCreator
    private Account(
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

        @JsonProperty("from")
        final com.twilio.type.PhoneNumber from,

        @JsonProperty("price_unit")
        @JsonDeserialize(using = com.twilio.converter.CurrencyDeserializer.class)
        final Currency priceUnit,

        @JsonProperty("test_number_float")
        final Float testNumberFloat,

        @JsonProperty("test_number_decimal")
        final BigDecimal testNumberDecimal,

        @JsonProperty("test_enum")
        final Account.Status testEnum,

        @JsonProperty("a2p_profile_bundle_sid")
        final String a2pProfileBundleSid,

        @JsonProperty("test_array_of_integers")
        final List<Integer> testArrayOfIntegers,

        @JsonProperty("test_array_of_array_of_integers")
        final List<List<Integer>> testArrayOfArrayOfIntegers,

        @JsonProperty("test_array_of_objects")
        final List<FeedbackIssue> testArrayOfObjects,

        @JsonProperty("test_array_of_enum")
        final List<Account.Status> testArrayOfEnum
    ) {
        this.accountSid = accountSid;
        this.sid = sid;
        this.testString = testString;
        this.testInteger = testInteger;
        this.testObject = testObject;
        this.testDateTime = DateConverter.rfc2822DateTimeFromString(testDateTime);
        this.testNumber = testNumber;
        this.from = from;
        this.priceUnit = priceUnit;
        this.testNumberFloat = testNumberFloat;
        this.testNumberDecimal = testNumberDecimal;
        this.testEnum = testEnum;
        this.a2pProfileBundleSid = a2pProfileBundleSid;
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
        public final com.twilio.type.PhoneNumber getFrom() {
            return this.from;
        }
        public final Currency getPriceUnit() {
            return this.priceUnit;
        }
        public final Float getTestNumberFloat() {
            return this.testNumberFloat;
        }
        public final BigDecimal getTestNumberDecimal() {
            return this.testNumberDecimal;
        }
        public final Account.Status getTestEnum() {
            return this.testEnum;
        }
        public final String getA2pProfileBundleSid() {
            return this.a2pProfileBundleSid;
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
        public final List<Account.Status> getTestArrayOfEnum() {
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

        Account other = (Account) o;

        return Objects.equals(accountSid, other.accountSid) &&  Objects.equals(sid, other.sid) &&  Objects.equals(testString, other.testString) &&  Objects.equals(testInteger, other.testInteger) &&  Objects.equals(testObject, other.testObject) &&  Objects.equals(testDateTime, other.testDateTime) &&  Objects.equals(testNumber, other.testNumber) &&  Objects.equals(from, other.from) &&  Objects.equals(priceUnit, other.priceUnit) &&  Objects.equals(testNumberFloat, other.testNumberFloat) &&  Objects.equals(testNumberDecimal, other.testNumberDecimal) &&  Objects.equals(testEnum, other.testEnum) &&  Objects.equals(a2pProfileBundleSid, other.a2pProfileBundleSid) &&  Objects.equals(testArrayOfIntegers, other.testArrayOfIntegers) &&  Objects.equals(testArrayOfArrayOfIntegers, other.testArrayOfArrayOfIntegers) &&  Objects.equals(testArrayOfObjects, other.testArrayOfObjects) &&  Objects.equals(testArrayOfEnum, other.testArrayOfEnum)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountSid, sid, testString, testInteger, testObject, testDateTime, testNumber, from, priceUnit, testNumberFloat, testNumberDecimal, testEnum, a2pProfileBundleSid, testArrayOfIntegers, testArrayOfArrayOfIntegers, testArrayOfObjects, testArrayOfEnum);
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

}

