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
public class NumberPool extends Resource {
    private static final long serialVersionUID = 12870424678248L;

    public static NumberPoolCreator creator(final NumberPoolModel.NumberPoolRequest numberPoolRequest){
        return new NumberPoolCreator(numberPoolRequest);
    }

    public static NumberPoolDeleter deleter(final String pathNumberPoolSID){
        return new NumberPoolDeleter(pathNumberPoolSID);
    }

    public static NumberPoolFetcher fetcher(final String pathNumberPoolSID){
        return new NumberPoolFetcher(pathNumberPoolSID);
    }

    public static NumberPoolReader reader(){
        return new NumberPoolReader();
    }

    public static NumberPoolUpdater updater(final String pathNumberPoolSID, final NumberPoolModel.NumberPoolRequest numberPoolRequest){
        return new NumberPoolUpdater(pathNumberPoolSID, numberPoolRequest);
    }

    /**
    * Converts a JSON String into a NumberPool object using the provided ObjectMapper.
    *
    * @param json Raw JSON String
    * @param objectMapper Jackson ObjectMapper
    * @return NumberPool object represented by the provided JSON
    */
    public static NumberPool fromJson(final String json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, NumberPool.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }

    /**
    * Converts a JSON InputStream into a NumberPool object using the provided
    * ObjectMapper.
    *
    * @param json Raw JSON InputStream
    * @param objectMapper Jackson ObjectMapper
    * @return NumberPool object represented by the provided JSON
    */
    public static NumberPool fromJson(final InputStream json, final ObjectMapper objectMapper) {
        // Convert all checked exceptions to Runtime
        try {
            return objectMapper.readValue(json, NumberPool.class);
        } catch (final JsonMappingException | JsonParseException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        }
    }

    private final String id;
    private final String name;
    private final List<String> senders;
    private final String callbackURL;

    @JsonCreator
    private NumberPool(
        @JsonProperty("id")
        final String id,

        @JsonProperty("name")
        final String name,

        @JsonProperty("senders")
        final List<String> senders,

        @JsonProperty("callback_url")
        final String callbackURL
    ) {
        this.id = id;
        this.name = name;
        this.senders = senders;
        this.callbackURL = callbackURL;
    }

        public final String getId() {
            return this.id;
        }
        public final String getName() {
            return this.name;
        }
        public final List<String> getSenders() {
            return this.senders;
        }
        public final String getCallbackURL() {
            return this.callbackURL;
        }

    @Override
    public boolean equals(final Object o) {
        if (this==o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NumberPool other = (NumberPool) o;

        return Objects.equals(id, other.id) &&  Objects.equals(name, other.name) &&  Objects.equals(senders, other.senders) &&  Objects.equals(callbackURL, other.callbackURL)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, senders, callbackURL);
    }

}

