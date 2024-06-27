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

package com.twilio.rest.flexapi.v1.credential;

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

import com.twilio.base.Creator;
import com.twilio.http.Request;
import com.twilio.http.TwilioRestClient;

public class NewCredentialsCreator extends Creator<NewCredentials>{
    private String testString;
    private Boolean testBoolean;
    private Integer testInteger;
    private BigDecimal testNumber;
    private Float testNumberFloat;
    private Double testNumberDouble;
    private BigDecimal testNumberInt32;
    private Long testNumberInt64;
    private Map<String, Object> testObject;
    private ZonedDateTime testDateTime;
    private LocalDate testDate;
    private NewCredentials.Status testEnum;
    private List<Map<String, Object>> testObjectArray;
    private Map<String, Object> testAnyType;
    private List<Map<String, Object>> testAnyArray;
    private List<NewCredentials.Permissions> permissions;
    private String someA2PThing;

    public NewCredentialsCreator(final String testString, final Integer testInteger, final Float testNumberFloat) {
        this.testString = testString;
        this.testInteger = testInteger;
        this.testNumberFloat = testNumberFloat;
    }
    public NewCredentialsCreator(final String testString, final Integer testInteger, final Map<String, Object> testObject) {
        this.testString = testString;
        this.testInteger = testInteger;
        this.testObject = testObject;
    }
    public NewCredentialsCreator(final String testString, final LocalDate testDate, final Float testNumberFloat) {
        this.testString = testString;
        this.testDate = testDate;
        this.testNumberFloat = testNumberFloat;
    }
    public NewCredentialsCreator(final String testString, final LocalDate testDate, final Map<String, Object> testObject) {
        this.testString = testString;
        this.testDate = testDate;
        this.testObject = testObject;
    }

    public NewCredentialsCreator setTestString(final String testString){
        this.testString = testString;
        return this;
    }
    public NewCredentialsCreator setTestBoolean(final Boolean testBoolean){
        this.testBoolean = testBoolean;
        return this;
    }
    public NewCredentialsCreator setTestInteger(final Integer testInteger){
        this.testInteger = testInteger;
        return this;
    }
    public NewCredentialsCreator setTestNumber(final BigDecimal testNumber){
        this.testNumber = testNumber;
        return this;
    }
    public NewCredentialsCreator setTestNumberFloat(final Float testNumberFloat){
        this.testNumberFloat = testNumberFloat;
        return this;
    }
    public NewCredentialsCreator setTestNumberDouble(final Double testNumberDouble){
        this.testNumberDouble = testNumberDouble;
        return this;
    }
    public NewCredentialsCreator setTestNumberInt32(final BigDecimal testNumberInt32){
        this.testNumberInt32 = testNumberInt32;
        return this;
    }
    public NewCredentialsCreator setTestNumberInt64(final Long testNumberInt64){
        this.testNumberInt64 = testNumberInt64;
        return this;
    }
    public NewCredentialsCreator setTestObject(final Map<String, Object> testObject){
        this.testObject = testObject;
        return this;
    }
    public NewCredentialsCreator setTestDateTime(final ZonedDateTime testDateTime){
        this.testDateTime = testDateTime;
        return this;
    }
    public NewCredentialsCreator setTestDate(final LocalDate testDate){
        this.testDate = testDate;
        return this;
    }
    public NewCredentialsCreator setTestEnum(final NewCredentials.Status testEnum){
        this.testEnum = testEnum;
        return this;
    }
    public NewCredentialsCreator setTestObjectArray(final List<Map<String, Object>> testObjectArray){
        this.testObjectArray = testObjectArray;
        return this;
    }
    public NewCredentialsCreator setTestObjectArray(final Map<String, Object> testObjectArray){
        return setTestObjectArray(Promoter.listOfOne(testObjectArray));
    }
    public NewCredentialsCreator setTestAnyType(final Map<String, Object> testAnyType){
        this.testAnyType = testAnyType;
        return this;
    }
    public NewCredentialsCreator setTestAnyArray(final List<Map<String, Object>> testAnyArray){
        this.testAnyArray = testAnyArray;
        return this;
    }
    public NewCredentialsCreator setTestAnyArray(final Map<String, Object> testAnyArray){
        return setTestAnyArray(Promoter.listOfOne(testAnyArray));
    }
    public NewCredentialsCreator setPermissions(final List<NewCredentials.Permissions> permissions){
        this.permissions = permissions;
        return this;
    }
    public NewCredentialsCreator setPermissions(final NewCredentials.Permissions permissions){
        return setPermissions(Promoter.listOfOne(permissions));
    }
    public NewCredentialsCreator setSomeA2PThing(final String someA2PThing){
        this.someA2PThing = someA2PThing;
        return this;
    }

    @Override
    public NewCredentials create(final TwilioRestClient client){
        String path = "/v1/Credentials/AWS";

        path = path.replace("{"+"TestString"+"}", this.testString.toString());

        Request request = new Request(
            HttpMethod.POST,
            Domains.FLEXAPI.toString(),
            path
        );
        request.setContentType(EnumConstants.ContentType.FORM_URLENCODED);
        addPostParams(request);
        Response response = client.request(request);
        if (response == null) {
            throw new ApiConnectionException("NewCredentials creation failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.test(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content", response.getStatusCode());
            }
            throw new ApiException(restException);
        }

        return NewCredentials.fromJson(response.getStream(), client.getObjectMapper());
    }
    private void addPostParams(final Request request) {
        if (testString != null) {
            request.addPostParam("TestString", testString);
    
        }
        if (testBoolean != null) {
            request.addPostParam("TestBoolean", testBoolean.toString());
    
        }
        if (testInteger != null) {
            request.addPostParam("TestInteger", testInteger.toString());
    
        }
        if (testNumber != null) {
            request.addPostParam("TestNumber", testNumber.toString());
    
        }
        if (testNumberFloat != null) {
            request.addPostParam("TestNumberFloat", testNumberFloat.toString());
    
        }
        if (testNumberDouble != null) {
            request.addPostParam("TestNumberDouble", testNumberDouble.toString());
    
        }
        if (testNumberInt32 != null) {
            request.addPostParam("TestNumberInt32", testNumberInt32.toString());
    
        }
        if (testNumberInt64 != null) {
            request.addPostParam("TestNumberInt64", testNumberInt64.toString());
    
        }
        if (testObject != null) {
            request.addPostParam("TestObject",  Converter.mapToJson(testObject));
    
        }
        if (testDateTime != null) {
            request.addPostParam("TestDateTime", testDateTime.toInstant().toString());

        }
        if (testDate != null) {
            request.addPostParam("TestDate", DateConverter.dateStringFromLocalDate(testDate));

        }
        if (testEnum != null) {
            request.addPostParam("TestEnum", testEnum.toString());
    
        }
        if (testObjectArray != null) {
            for (Map<String, Object> prop : testObjectArray) {
                request.addPostParam("TestObjectArray", Converter.mapToJson(prop));
            }
    
        }
        if (testAnyType != null) {
            request.addPostParam("TestAnyType",  Converter.mapToJson(testAnyType));
    
        }
        if (testAnyArray != null) {
            for (Map<String, Object> prop : testAnyArray) {
                request.addPostParam("TestAnyArray", Converter.mapToJson(prop));
            }
    
        }
        if (permissions != null) {
            for (NewCredentials.Permissions prop : permissions) {
                request.addPostParam("Permissions", prop.toString());
            }
    
        }
        if (someA2PThing != null) {
            request.addPostParam("SomeA2PThing", someA2PThing);
    
        }
    }
}
