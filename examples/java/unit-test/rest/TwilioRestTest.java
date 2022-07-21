package com.twilio.rest;

import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twilio.base.Page;
import com.twilio.base.ResourceSet;
import com.twilio.converter.Converter;
import com.twilio.converter.DateConverter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.AccountCreator;
import com.twilio.rest.api.v2010.AccountDeleter;
import com.twilio.rest.api.v2010.AccountFetcher;
import com.twilio.rest.api.v2010.AccountReader;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.rest.api.v2010.account.CallDeleter;
import com.twilio.rest.api.v2010.account.CallFetcher;
import com.twilio.rest.api.v2010.account.call.FeedbackCallSummary;
import com.twilio.rest.api.v2010.account.call.FeedbackCallSummaryCreator;
import com.twilio.rest.api.v2010.credential.Aws;
import com.twilio.rest.api.v2010.credential.AwsDeleter;
import com.twilio.rest.api.v2010.credential.AwsFetcher;
import com.twilio.rest.api.v2010.credential.AwsReader;
import com.twilio.rest.api.v2010.credential.AwsUpdater;
import com.twilio.rest.api.v2010.credential.NewCredentials;
import com.twilio.rest.api.v2010.credential.NewCredentialsCreator;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TwilioRestTest {
    @Mock
    private TwilioRestClient twilioRestClient;
    private static final String TEST_INTEGER = "TestInteger";
    private static final String ACCOUNT_SID = "AC222222222222222222222222222222";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldMakeValidAPICall() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Account account = new AccountFetcher(ACCOUNT_SID).fetch(twilioRestClient);
        assertNotNull(account);
        assertEquals(account.getAccountSid(), "AC222222222222222222222222222222");
    }

    @Test(expected=ApiException.class)
    public void testShouldMakeInValidAPICallWithExceptionForAccountFetcher() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        new AccountFetcher("AC222222222222222222222222222222").fetch(twilioRestClient);
    }

    @Test
    public void testShouldMakeValidAPICallAccountDeleter() {
        Request mockRequest = new Request(
                HttpMethod.DELETE,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        Boolean account = new AccountDeleter("AC222222222222222222222222222222").delete(twilioRestClient);

        assertFalse(account);
    }

    @Test(expected=ApiException.class)
    public void testShouldMakeInValidAPICallWithExceptionForAccountDeleter() {
        Request mockRequest = new Request(
                HttpMethod.DELETE,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        new AccountDeleter("AC222222222222222222222222222222").delete(twilioRestClient);
    }

    @Test
    public void testShouldSendAddedHeadersThroughRestClient() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                Domains.API.toString(),
                "/2010-04-01/Accounts.json"
        );
        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
        mockRequest.addPostParam("RecordingStatusCallback", "https://validurl.com");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"sid\": 123, \"call_sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountCreator accountCreator = new AccountCreator();
        accountCreator.setXTwilioWebhookEnabled(Account.XTwilioWebhookEnabled.TRUE);
        accountCreator.setRecordingStatusCallback(URI.create("https://validurl.com"));

        Account account  = accountCreator.create(twilioRestClient);
        assertNotNull(account);
        assertEquals("123",account.getSid());
        assertEquals("true", mockRequest.getHeaderParams().get("X-Twilio-Webhook-Enabled").get(0));
        assertEquals("https://validurl.com", mockRequest.getPostParams().get("RecordingStatusCallback").get(0));
    }

    @Test(expected=ApiConnectionException.class)
    public void testShouldSendNullResponseForAccountCreator() {
        List<String> recordingStatusCallbackEvent = Arrays.asList("http://test1.com/", "http://test2.com");
        Request mockRequest = new Request(
                HttpMethod.POST,
                Domains.API.toString(),
                "/2010-04-01/Accounts.json"
        );
        for(String recordingStatusEvent : recordingStatusCallbackEvent){
            mockRequest.addPostParam("RecordingStatusCallbackEvent", recordingStatusEvent);
        }
        mockRequest.addPostParam("RecordingStatusCallback", "https://validurl.com");
        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountCreator accountCreator = new AccountCreator();
        accountCreator.setRecordingStatusCallbackEvent(recordingStatusCallbackEvent);
        accountCreator.setRecordingStatusCallback(URI.create("https://validurl.com"));
        accountCreator.setXTwilioWebhookEnabled(Account.XTwilioWebhookEnabled.TRUE);

        accountCreator.create(twilioRestClient);
    }

    @Test(expected=ApiConnectionException.class)
    public void testShouldSendIncorrectStatusForAccountCreator() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                Domains.API.toString(),
                "/2010-04-01/Accounts.json"
        );
        mockRequest.addPostParam("RecordingStatusCallback", null);
        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}]}", 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountCreator accountCreator = new AccountCreator();
        accountCreator.setRecordingStatusCallbackEvent(null);
        accountCreator.setXTwilioWebhookEnabled(null);

        accountCreator.create(twilioRestClient);
    }

    @Test
    public void testShouldQueryParamInRequest() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        String formattedCurrentDate =  currentDateTime.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT));
        mockRequest.addQueryParam("DateCreated", formattedCurrentDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreated(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);

        ResourceSet<Account> account  = accountReader.read(twilioRestClient);
        assertNotNull(account);
        assertNotNull(mockRequest.getQueryParams().get("DateCreated"));
        assertNotNull(mockRequest.getQueryParams().get("PageSize"));
    }

    @Test
    public void testShouldSendArrayTypeParamInRequest() {
        List<String> recordingStatusCallbackEvent = Arrays.asList("http://test1.com/", "http://test2.com");
        Request mockRequest = new Request(
                HttpMethod.POST,
                Domains.API.toString(),
                "/2010-04-01/Accounts.json"
        );
        for(String recordingStatusEvent : recordingStatusCallbackEvent){
            mockRequest.addPostParam("RecordingStatusCallbackEvent", recordingStatusEvent);
        }
        mockRequest.addPostParam("RecordingStatusCallback", "https://validurl.com");
        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}]}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountCreator accountCreator = new AccountCreator();
        accountCreator.setRecordingStatusCallbackEvent(recordingStatusCallbackEvent);
        accountCreator.setRecordingStatusCallback(URI.create("https://validurl.com"));
        accountCreator.setXTwilioWebhookEnabled(Account.XTwilioWebhookEnabled.TRUE);
        Account account  = accountCreator.create(twilioRestClient);
        assertNotNull(account);
        assertEquals("true", mockRequest.getHeaderParams().get("X-Twilio-Webhook-Enabled").get(0));
        assertEquals(recordingStatusCallbackEvent.get(0), mockRequest.getPostParams().get("RecordingStatusCallbackEvent").get(0));
        assertEquals("https://validurl.com", mockRequest.getPostParams().get("RecordingStatusCallback").get(0));

    }

    @Test
    public void testShouldAddAccountSidIfNotPresent() {
        when(twilioRestClient.getAccountSid()).thenReturn(ACCOUNT_SID);
        Request mockRequest = new Request(
                HttpMethod.GET,
                Domains.API.toString(),
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/123.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\",\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}", 200));
        Call call = new CallFetcher(123).fetch(twilioRestClient);
        assertNotNull(call);
        assertEquals(call.getSid(), "123");
        assertEquals(call.getAccountSid(), ACCOUNT_SID);
    }


    @Test
    public void testShouldReadListOfMessages() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/v1/Credentials/AWS"
        );
        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        String testResponse =  "{\"credentials\":[{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Ahoy\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}, {\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Hello\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(testResponse, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Page<Aws> awsPage = new AwsReader().firstPage(twilioRestClient);
        assertEquals(awsPage.getRecords().get(0).getTestString(), "Ahoy"  );
        assertEquals(awsPage.getRecords().get(1).getTestString(), "Hello" );
    }

    @Test
    public void testShouldReadPageOfMessages() {
        String uri = "/2010-04-01/Accounts.json";
        Request mockRequestPage0 = new Request(
                HttpMethod.GET,
                "api",
                uri
        );
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        mockRequestPage0.addQueryParam("PageSize", "2");
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        String nextPageURL = "https://api.twilio.com/2010-04-01/Accounts.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130";
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Ahoy\"}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + nextPageURL + "?PageSize=2" + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + url + "?PageSize=2" + "\", \"page_size\":2}}";
        when(twilioRestClient.request(mockRequestPage0)).thenReturn(new Response(responseContent, 200));

        Request mockRequestPage1 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130?PageSize=2"
        );
        String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Matey\"}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + url + "?PageSize=2" + "\", \"page_size\":2}}";
        when(twilioRestClient.request(mockRequestPage1)).thenReturn(new Response(responseContent2, 200));
        AccountReader accountReader = new AccountReader();
        accountReader.setPageSize(2);
        ResourceSet<Account> account = accountReader.read(twilioRestClient);
        List<String> testStringValues = new ArrayList<>();
        account.forEach(accountVal -> {
            assertNotNull(accountVal);
            testStringValues.add(accountVal.getTestString());
        });
        assertEquals(testStringValues.get(0), "Ahoy");
        assertEquals(testStringValues.get(1), "Matey");
    }

    @Test
    public void testListError() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/v1/Credentials/AWS"
        );
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> content = new HashMap<>();
        content.put("code", 20001);
        content.put("message", "Invalid PageSize.");
        content.put("more_info", "https://www.twilio.com/docs/errors/20001");
        content.put("status", 400);
        JSONObject response = new JSONObject(content);
        mockRequest.addQueryParam("PageSize", "0");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(response.toString(), 400));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsReader reader = new AwsReader();
        reader.setPageSize(0);
        assertThrows("Invalid PageSize.", ApiException.class, () -> reader.read(twilioRestClient));
    }

    @Test
    public void testObjectArrayTypeParam() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "api",
                "/v1/Credentials/AWS"
        );
        Map<String, Object> item1 = new HashMap<>();
        Map<String, Object> item2 = new HashMap<>();
        item1.put("A", Arrays.asList("Apple", "Aces"));
        item2.put("B", Collections.singletonList("Banana"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Map<String, Object>> testObjectArray = Arrays.asList(item1, item2);
        for(Map<String, Object> testObject : testObjectArray){
            mockRequest.addPostParam("TestObjectArray", Converter.mapToJson(testObject));
        }
        mockRequest.addPostParam("TestString", ACCOUNT_SID);
        mockRequest.addPostParam("TestNumberFloat", "1.4");
        mockRequest.addPostParam(TEST_INTEGER, "1");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, 1, 1.4F);
        credentialsCreator.setTestObjectArray(Arrays.asList(item1, item2));
        NewCredentials credentials = credentialsCreator.create(twilioRestClient);
        assertNotNull(credentials);
        assertEquals("{\"A\":[\"Apple\",\"Aces\"]}", mockRequest.getPostParams().get("TestObjectArray").get(0));
        assertEquals("AC222222222222222222222222222222", mockRequest.getPostParams().get("TestString").get(0));
    }

    @Test(expected =  ApiConnectionException.class)
    public void testObjectArrayTypeParamNullResponseNewCredentialsCreator() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "api",
                "/v1/Credentials/AWS"
        );
        Map<String, Object> item1 = new HashMap<>();
        Map<String, Object> item2 = new HashMap<>();
        item1.put("A", Arrays.asList("Apple", "Aces"));
        item2.put("B", Collections.singletonList("Banana"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Object> testObjectArray = Arrays.asList(item1, item2);
        for(Object testObject : testObjectArray){
            mockRequest.addPostParam("TestObjectArray", testObject.toString());
        }
        mockRequest.addPostParam("TestString", ACCOUNT_SID);
        mockRequest.addPostParam("TestNumberFloat", "1.4");
        mockRequest.addPostParam(TEST_INTEGER, "1");
        mockRequest.addPostParam("Permissions", "test");
        mockRequest.addPostParam("TestEnum", "test");
        mockRequest.addPostParam("TestDate", "test");
        mockRequest.addPostParam("TestDateTime", "test");
        mockRequest.addPostParam("TestObject", "test");
        mockRequest.addPostParam("TestDateTime", "test");
        mockRequest.addPostParam("TestNumberInt64", "test");
        mockRequest.addPostParam("TestNumberInt32", "test");
        mockRequest.addPostParam("TestNumberDouble", "test");
        mockRequest.addPostParam("TestNumber", "test");
        mockRequest.addPostParam("TestBoolean", "test");
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, 1, 1.4F);
        credentialsCreator.setTestObjectArray(Arrays.asList(item1, item2));
        List<NewCredentials.Permissions> permissions = new ArrayList<>();
        permissions.add(NewCredentials.Permissions.GET_ALL);
        permissions.add(NewCredentials.Permissions.POST_ALL);
        credentialsCreator.setPermissions( permissions);
        credentialsCreator.setTestEnum(NewCredentials.Status.PAUSED);
        LocalDate localDate = LocalDate.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        credentialsCreator.setTestDate(localDate);
        credentialsCreator.setTestDateTime(zonedDateTime);
        credentialsCreator.setTestObject(new HashMap<>());
        credentialsCreator.setTestNumberInt64(new Long(1));
        credentialsCreator.setTestNumberInt32(new BigDecimal(1));
        credentialsCreator.setTestNumberDouble(new Double(1));
        credentialsCreator.setTestNumberFloat(new Float(1));
        credentialsCreator.setTestNumber(new BigDecimal(1));
        credentialsCreator.setTestInteger(new Integer(1));
        credentialsCreator.setTestBoolean(true);
        credentialsCreator.setTestString("test");
        credentialsCreator.create(twilioRestClient);
    }

    @Test(expected =  ApiException.class)
    public void testObjectArrayTypeParamInvalidStatus() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, Object> content = new HashMap<>();
        content.put("code", 20001);
        content.put("message", "Invalid PageSize.");
        content.put("more_info", "https://www.twilio.com/docs/errors/20001");
        content.put("status", 400);
        JSONObject response = new JSONObject(content);
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(response.toString(), 400));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, LocalDate.now(), 1.4F);
        credentialsCreator.create(twilioRestClient);
    }

    @Test(expected =  ApiException.class)
    public void testNewCredentialsCreatorConstructorWithLocalDateAndMap() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, Object> content = new HashMap<>();
        content.put("code", 20001);
        content.put("message", "Invalid PageSize.");
        content.put("more_info", "https://www.twilio.com/docs/errors/20001");
        content.put("status", 400);
        JSONObject response = new JSONObject(content);
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(response.toString(), 400));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, LocalDate.now(), new HashMap<>());
        credentialsCreator.create(twilioRestClient);
    }

    @Test(expected =  ApiException.class)
    public void testNewCredentialsCreatorConstructorWithIntAndMap() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, Object> content = new HashMap<>();
        content.put("code", 20001);
        content.put("message", "Invalid PageSize.");
        content.put("more_info", "https://www.twilio.com/docs/errors/20001");
        content.put("status", 400);
        JSONObject response = new JSONObject(content);
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(response.toString(), 400));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, 1, new HashMap<>());
        credentialsCreator.create(twilioRestClient);
    }

    @Test
    public void testAnyTypeParam() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "api",
                "/v1/Credentials/AWS"
        );

        Map<String, Object> anyMap = new HashMap<>();
        anyMap.put(TEST_INTEGER, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockRequest.addPostParam("TestAnyType", "{\"TestInteger\":1}");
        mockRequest.addPostParam("TestNumberFloat", "1.4");
        mockRequest.addPostParam(TEST_INTEGER, "1");
        mockRequest.addPostParam("TestString", ACCOUNT_SID);
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        NewCredentialsCreator credentialsCreator = new NewCredentialsCreator(ACCOUNT_SID, 1, 1.4F);
        credentialsCreator.setTestAnyType(anyMap);
        NewCredentials credentials = credentialsCreator.create(twilioRestClient);

        assertNotNull(credentials);
        assertNotNull(credentialsCreator);
        assertEquals("AC222222222222222222222222222222", mockRequest.getPostParams().get("TestString").get(0));
        assertEquals("{\"TestInteger\":1}", mockRequest.getPostParams().get("TestAnyType").get(0));

    }

    @Test
    public void testShouldSupportNestedPropertiesResponse() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryDateTimeRange("DateCreated", currentDateTime, currentDateTime);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreatedBefore(currentDateTime);
        accountReader.setDateCreatedAfter(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);
        ResourceSet<Account> account = accountReader.read(twilioRestClient);
        assertNotNull(account);
        assertFalse(account.iterator().next().getTestObject().getSms());
        assertFalse(account.iterator().next().getTestObject().getVoice());
        assertTrue(account.iterator().next().getTestObject().getMms());
        assertEquals("123", account.iterator().next().getSid());
        assertTrue(account.iterator().next().getTestObject().getFax());
    }

    @Test
    public void testShouldSupportArrayOfObjectResponse() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        String formattedCurrentDate =  currentDateTime.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT));
        mockRequest.addQueryParam("DateCreated", formattedCurrentDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreated(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);

        ResourceSet<Account> account = accountReader.read(twilioRestClient);

        assertNotNull(account);
        assertEquals("123", account.iterator().next().getSid());
        assertEquals(1, account.iterator().next().getTestArrayOfObjects().size());
        assertEquals("Testing Object Array", account.iterator().next().getTestArrayOfObjects().get(0).getDescription());
        assertEquals(123, account.iterator().next().getTestArrayOfObjects().get(0).getCount());
    }

    @Test
    public void testShouldSupportDeserializationTypes() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        String formattedCurrentDate =  currentDateTime.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT));
        mockRequest.addQueryParam("DateCreated", formattedCurrentDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"testDateTime\":\"2021-03-23T16:43:32.010069453-05:00\", \"price_unit\": \"USD\" }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreated(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);
        ResourceSet<Account> account = accountReader.read(twilioRestClient);
        assertNotNull(account);
        assertEquals("123",account.iterator().next().getSid());
        assertEquals("USD", account.iterator().next().getPriceUnit().toString() );
        assertEquals("2021-03-23T21:43:32.010069453Z[UTC]",account.iterator().next().getTestDateTime().toString());
    }

    @Test
    public void testRequestNonStandardDomainName() {
        Request mockRequest = new Request(HttpMethod.GET,
                Domains.FLEXAPI.toString(),
                "/2010-04-01/v1/FlexFlows");
        mockRequest.addHeaderParam("Accept", "application/json");
        mockRequest.addPostParam("FriendlyName", "friendly_name");
        mockRequest.addPostParam("ChatServiceSid", "ISXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        mockRequest.addPostParam("ChannelType", "web");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"sid\": \"FOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"date_created\": \"2016-08-01T22:10:40Z\",\"date_updated\": \"2016-08-01T22:10:40Z\",\"friendly_name\": \"friendly_name\",\"chat_service_sid\": \"ISaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"channel_type\": \"sms\",\"contact_identity\": \"12345\",\"enabled\": true,\"integration_type\": \"studio\",\"integration\": {\"flow_sid\": \"FWaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"retry_count\": 1},\"long_lived\": true,\"janitor_enabled\": true,\"url\": \"https://flex-api.twilio.com/v1/FlexFlows/FOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        assertNotNull(mockRequest);
        assertEquals(HttpMethod.GET, mockRequest.getMethod());
        assertEquals("https://flex-api.twilio.com/2010-04-01/v1/FlexFlows", mockRequest.getUrl());
    }

    @Test(expected=ApiException.class)
    public void testShouldQueryParamInRequestNullResponse() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        String formattedCurrentDate =  currentDateTime.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT));
        mockRequest.addQueryParam("DateCreated", formattedCurrentDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreated(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);

        accountReader.read(twilioRestClient);

    }

    @Test(expected=ApiConnectionException.class)
    public void testShouldQueryParamInRequestwithIncorrectResponseCode() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts.json"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        String formattedLocalDate = DateConverter.dateStringFromLocalDate(localDate);
        String formattedCurrentDate =  currentDateTime.format(DateTimeFormatter.ofPattern(Request.QUERY_STRING_DATE_TIME_FORMAT));
        mockRequest.addQueryParam("DateCreated", formattedCurrentDate);
        mockRequest.addQueryParam("Date.Test", formattedLocalDate);
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AccountReader accountReader = new AccountReader();
        accountReader.setDateCreated(currentDateTime);
        accountReader.setDateTest(localDate);
        accountReader.setPageSize(4);

        accountReader.read(twilioRestClient);

    }

    @Test(expected = ApiConnectionException.class)
    public void testShouldMakeInValidAPICallReturnsNullForAccountUpdater() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Account.Status status = Account.Status.IN_PROGRESS;
        new AccountUpdater(ACCOUNT_SID,status).update(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testShouldMakeInValidAPICallReturnsWrongStatusForAccountUpdater() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                Domains.API.toString(),
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        mockRequest.addPostParam("PauseBehavior", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Account.Status status = Account.Status.IN_PROGRESS;
        AccountUpdater accountUpdater = new AccountUpdater(ACCOUNT_SID,status);
        accountUpdater.setPauseBehavior("test");
        accountUpdater.update(twilioRestClient);
    }

    @Test
    public void testShouldMakeValidAPICallAWSFetcher() {
        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse =  "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(new Response(testResponse, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        Aws resource = new AwsFetcher(ACCOUNT_SID).fetch(twilioRestClient);

        assertNotNull(resource);
        assertEquals(resource.getAccountSid(),"sid");
    }

    @Test(expected = ApiException.class)
    public void testShouldGetInValidAPICallResponseAWSFetcher() {

        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse = "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(new Response(testResponse, 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        new AwsFetcher(ACCOUNT_SID).fetch(twilioRestClient);
    }

    @Test(expected = ApiConnectionException.class)
    public void testShouldGetNullAPICallResponseAWSFetcher() {

        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse =  "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        new AwsFetcher(ACCOUNT_SID).fetch(twilioRestClient);
    }

    @Test
    public void testShouldMakeValidAPICallAwsDeleter() {

        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse = "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(new Response(testResponse, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        Boolean resource = new AwsDeleter(ACCOUNT_SID).delete(twilioRestClient);

        assertFalse(resource);
    }

    @Test(expected = ApiException.class)
    public void testShouldGetInValidAPICallResponseAwsDeleter() {

        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse =  "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(new Response(testResponse, 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        new AwsDeleter(ACCOUNT_SID).delete(twilioRestClient);
    }

    @Test(expected = ApiConnectionException.class)
    public void testShouldGetNullAPICallResponseAwsDeleter() {

        String url = "https://api.twilio.com/v1/Credentials/AWS";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String testResponse =  "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any() )).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        new AwsDeleter(ACCOUNT_SID).delete(twilioRestClient);
    }

    public void testCallFetcherResponseNull() {
        when(twilioRestClient.request(Mockito.any())).thenReturn(null);
        new CallFetcher(ACCOUNT_SID, 123).fetch(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testCallFetcherResponseNotSuccess() {
        Response response = new Response("{\"account_sid\":\"SID\", \"call_sid\":\"PNXXXXY\"}", 404);
        when(twilioRestClient.request(Mockito.any())).thenReturn(response);
        when(twilioRestClient.getAccountSid()).thenReturn(ACCOUNT_SID);
        when(twilioRestClient.getObjectMapper()).thenReturn(new ObjectMapper());
        new CallFetcher(1234).fetch(twilioRestClient);
    }

    @Test(expected = ApiConnectionException.class)
    public void testCallDeleterResponseNull() {
        when(twilioRestClient.request(Mockito.any())).thenReturn(null);
        new CallDeleter(ACCOUNT_SID, 123).delete(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testCallDeleterResponseNotSuccess() {
        Response response = new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 404);
        when(twilioRestClient.request(Mockito.any())).thenReturn(response);
        when(twilioRestClient.getAccountSid()).thenReturn(ACCOUNT_SID);
        when(twilioRestClient.getObjectMapper()).thenReturn(new ObjectMapper());
        new CallDeleter(1234).delete(twilioRestClient);
    }

    @Test(expected = ApiConnectionException.class)
    public void testCallCreatorResponseNull() {
        when(twilioRestClient.request(Mockito.any())).thenReturn(null);
        new CallCreator(ACCOUNT_SID, "123").create(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testCallCreatorResponseNotSuccess() {
        Response response = new Response("{\"account_sid\":\"SID\", \"call_sid\":\"PNXXXXY\"}", 404);
        when(twilioRestClient.request(Mockito.any())).thenReturn(response);
        when(twilioRestClient.getAccountSid()).thenReturn(ACCOUNT_SID);
        when(twilioRestClient.getObjectMapper()).thenReturn(new ObjectMapper());
        new CallCreator("123").setTestArrayOfStrings(Arrays.asList("value1", "value2")).create(twilioRestClient);
    }

    @Test
    public void testCallCrud() {
        CallCreator callCreator = Call.creator("123");
        CallCreator callCreatorAccountSid = Call.creator(ACCOUNT_SID,"123");
        assertNotNull(callCreator);
        assertNotNull(callCreatorAccountSid);

        CallFetcher callFetcher = Call.fetcher(123);
        CallFetcher callFetcherAccountSid =  Call.fetcher(ACCOUNT_SID,123);
        assertNotNull(callFetcher);
        assertNotNull(callFetcherAccountSid);

        CallDeleter callDeleter = Call.deleter(123);
        CallDeleter callDeleterAccountSid = Call.deleter(ACCOUNT_SID,123);
        assertNotNull(callDeleter);
        assertNotNull(callDeleterAccountSid);
    }

    @Test
    public void testAWSCrud() {
        NewCredentialsCreator credentialsCreator1 = NewCredentials.creator(ACCOUNT_SID,1,1F);
        NewCredentialsCreator credentialsCreator2 = NewCredentials.creator(ACCOUNT_SID,1,new HashMap<>());
        NewCredentialsCreator credentialsCreator3 = NewCredentials.creator(ACCOUNT_SID,LocalDate.now(),new HashMap<>());
        NewCredentialsCreator credentialsCreator4 = NewCredentials.creator(ACCOUNT_SID,LocalDate.now(),1F);
        assertNotNull(credentialsCreator1);
        assertNotNull(credentialsCreator2);
        assertNotNull(credentialsCreator3);
        assertNotNull(credentialsCreator4);

        AwsFetcher awsFetcher = Aws.fetcher(ACCOUNT_SID);
        assertNotNull(awsFetcher);

        AwsDeleter awsDeleter = Aws.deleter(ACCOUNT_SID);
        assertNotNull(awsDeleter);

        AwsReader awsReader = Aws.reader();
        assertNotNull(awsReader);

        AwsUpdater awsUpdater = Aws.updater(ACCOUNT_SID);
        assertNotNull(awsUpdater);
    }

    @Test
    public void testAccountCrud() {
        AccountCreator accountCreator = Account.creator();
        assertNotNull(accountCreator);

        AccountFetcher accountFetcherSid = Account.fetcher(ACCOUNT_SID);
        AccountFetcher accountFetcher = Account.fetcher();
        assertNotNull(accountFetcherSid);
        assertNotNull(accountFetcher);

        AccountDeleter accountDeleter = Account.deleter();
        assertNotNull(accountDeleter);
        AccountDeleter accountDeleterSid = Account.deleter(ACCOUNT_SID);
        assertNotNull(accountDeleterSid);

        AccountReader accountReader = Account.reader();
        assertNotNull(accountReader);

        AccountUpdater accountUpdaterSid = Account.updater(ACCOUNT_SID,Account.Status.COMPLETED);
        assertNotNull(accountUpdaterSid);
        AccountUpdater accountUpdater = Account.updater(Account.Status.COMPLETED);
        assertNotNull(accountUpdater);
    }

    @Test
    public void testAccountVariables() {
        Account.XTwilioWebhookEnabled xTwilioWebhookEnabled = Account.XTwilioWebhookEnabled.forValue("true");
        Account.Status status = Account.Status.forValue("paused");

        assertEquals("true", xTwilioWebhookEnabled.toString());
        assertEquals("paused", status.toString());
    }

    @Test
    public void testCallObjectCreation() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"testInteger\": 123}";
        Call callString = Call.fromJson(json, objectMapper);

        String initialString = "{\"testInteger\": 123}";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        Call callInputStream = Call.fromJson(targetStream, objectMapper);

        assertEquals((Integer)123, callString.getTestInteger());
        assertEquals((Integer)123, callInputStream.getTestInteger());
    }

    @Test
    public void testAwsObjectCreation() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"testInteger\": 123}";
        Aws awsString = Aws.fromJson(json, objectMapper);

        String initialString = "{\"testInteger\": 123}";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        Aws awsInputStream = Aws.fromJson(targetStream, objectMapper);

        assertEquals((Integer)123, awsString.getTestInteger());
        assertEquals((Integer)123, awsInputStream.getTestInteger());
    }

    @Test
    public void testAccountObjectCreation() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"testInteger\": 123}";
        Account accountString = Account.fromJson(json, objectMapper);

        String initialString = "{\"testInteger\": 123}";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        Account accountInputStream = Account.fromJson(targetStream, objectMapper);

        assertEquals((Integer)123, accountString.getTestInteger());
        assertEquals((Integer)123, accountInputStream.getTestInteger());
    }


    @Test(expected = ApiException.class)
    public void testCallObjectCreationInvalidJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        Call.fromJson("json", objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testAwsObjectCreationInvalidJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        Aws.fromJson("json", objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testAccountObjectCreationInvalidJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        Account.fromJson("json", objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testCallObjectCreationInvalidInputStream() {
        ObjectMapper objectMapper = new ObjectMapper();
        Call.fromJson(new ByteArrayInputStream("initialString".getBytes()), objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testAwsObjectCreationInvalidInputStream() {
        ObjectMapper objectMapper = new ObjectMapper();
        Aws.fromJson(new ByteArrayInputStream("initialString".getBytes()), objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testAccountObjectCreationInvalidInputStream() {
        ObjectMapper objectMapper = new ObjectMapper();
        Account.fromJson(new ByteArrayInputStream("initialString".getBytes()), objectMapper);
    }

    @Test
    public void testCallGetters() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        String jsonDuplicate = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        Call call = Call.fromJson(json, objectMapper);
        Call callDuplicate = Call.fromJson(jsonDuplicate, objectMapper);

        assertEquals("a123", call.getAccountSid());
        assertEquals("123", call.getSid());
        assertNull(call.getTestObject());
        assertEquals(Integer.valueOf("123"), call.getTestInteger());
        assertNull(call.getTestDateTime());
        assertEquals(BigDecimal.valueOf(123.1), call.getTestNumber());
        assertNull(call.getPriceUnit());
        assertEquals(Float.valueOf("123.2"), call.getTestNumberFloat());
        assertEquals("paused", call.getTestEnum().toString());
        assertNull(call.getTestArrayOfIntegers());
        assertNull(call.getTestArrayOfArrayOfIntegers());
        assertNull(call.getTestArrayOfObjects());
        assertNull(call.getTestString());

        assertTrue(call.equals(callDuplicate));
        assertTrue(call.equals(call));
        // If two objects are equal they must have same hashcode
        assertEquals(callDuplicate.hashCode(), call.hashCode());
        assertFalse(call.equals(null));
    }

    @Test
    public void testAwsGetters() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, " +
                "\"testEnum\": \"paused\"}";
        String jsonDuplicate = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, " +
                "\"testEnum\": \"paused\"}";
        Aws aws = Aws.fromJson(json, objectMapper);
        Aws awsDuplicate = Aws.fromJson(jsonDuplicate, objectMapper);

        assertEquals("a123", aws.getAccountSid());
        assertEquals("123", aws.getSid());
        assertNull(aws.getTestObject());
        assertEquals(Integer.valueOf("123"), aws.getTestInteger());
        assertNull(aws.getTestDateTime());
        assertEquals(BigDecimal.valueOf(123.1), aws.getTestNumber());
        assertNull(aws.getPriceUnit());
        assertEquals(Float.valueOf("123.2"), aws.getTestNumberFloat());
        assertEquals(Aws.Status.PAUSED.toString(), aws.getTestEnum().toString());
        assertNull(aws.getTestArrayOfIntegers());
        assertNull(aws.getTestArrayOfArrayOfIntegers());
        assertNull(aws.getTestArrayOfObjects());
        assertNull(aws.getTestString());

        assertTrue(aws.equals(awsDuplicate));
        assertTrue(aws.equals(aws));
        // If two objects are equal they must have same hashcode
        assertEquals(awsDuplicate.hashCode(), aws.hashCode());
        assertFalse(aws.equals(null));
    }

    @Test
    public void testNewCredentialsGetters() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\", " +
                "\"testEnum\": \"paused\"}";
        final NewCredentials credentials = NewCredentials.fromJson(json, objectMapper);
        final NewCredentials credentialsDuplicate = NewCredentials.fromJson(json, objectMapper);

        assertEquals("a123", credentials.getAccountSid());
        assertEquals("123", credentials.getSid());
        assertNull(credentials.getTestString());
        assertNull(credentials.getTestObject());
        assertEquals(Integer.valueOf("123"), credentials.getTestInteger());
        assertNull(credentials.getTestDateTime());
        assertEquals(BigDecimal.valueOf(123.1), credentials.getTestNumber());
        assertNull(credentials.getPriceUnit());
        assertEquals(Float.valueOf("123.2"), credentials.getTestNumberFloat());
        assertEquals("paused", credentials.getTestEnum().toString());
        assertNull(credentials.getTestArrayOfIntegers());
        assertNull(credentials.getTestArrayOfArrayOfIntegers());
        assertNull(credentials.getTestArrayOfObjects());

        assertTrue(credentials.equals(credentialsDuplicate));
        assertTrue(credentialsDuplicate.equals(credentials));
        assertEquals(credentialsDuplicate.hashCode(), credentials.hashCode());
        assertFalse(credentials.equals(null));
    }

    @Test
    public void testAccountGetters() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        String jsonDuplicate = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        Account account = Account.fromJson(json, objectMapper);
        Account accountDuplicate = Account.fromJson(jsonDuplicate, objectMapper);

        assertEquals("a123", account.getAccountSid());
        assertEquals("123", account.getSid());
        assertNull(account.getTestObject());
        assertEquals(Integer.valueOf("123"), account.getTestInteger());
        assertNull(account.getTestDateTime());
        assertEquals(BigDecimal.valueOf(123.1), account.getTestNumber());
        assertNull(account.getPriceUnit());
        assertEquals(Float.valueOf("123.2"), account.getTestNumberFloat());
        assertEquals(Account.Status.PAUSED.toString(), account.getTestEnum().toString());
        assertNull(account.getTestArrayOfIntegers());
        assertNull(account.getTestArrayOfArrayOfIntegers());
        assertNull(account.getTestArrayOfObjects());
        assertNull(account.getTestString());

        assertTrue(account.equals(accountDuplicate));
        assertTrue(account.equals(account));
        // If two objects are equal they must have same hashcode
        assertEquals(accountDuplicate.hashCode(), account.hashCode());
        assertFalse(account.equals(null));
    }

    @Test
    public void testAwsReaderPagination() {
        String firstPageURI = "/v1/Credentials/AWS";
        String nextPageURI = "/v1/Credentials/AWSN";
        Request mockRequestFirstPage = new Request(
                HttpMethod.GET,
                "api",
                firstPageURI
        );

        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        mockRequestFirstPage.addQueryParam("PageSize", "2");
        String responseContent = "{\"credentials\":[" +
                "{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Ahoy\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}" +
                "],\"meta\": {\"url\":\"" + firstPageURI + "\", \"next_page_url\":\"" + nextPageURI + "?PageSize=2" + "\", \"previous_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"first_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"page_size\":2}}";
        when(twilioRestClient.request(mockRequestFirstPage)).thenReturn(new Response(responseContent, 200));

        String responseContentNextPage = "{\"credentials\":[" +
                "{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Matey\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}" +
                "],\"meta\": {\"url\":\"" + firstPageURI + "\", \"next_page_url\":\"" + "" + "?PageSize=2" + "\", \"previous_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"first_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"page_size\":2}}";

        AwsReader awsReader = new AwsReader();
        awsReader.setPageSize(2);
        Page<Aws> firstPage = awsReader.firstPage(twilioRestClient);

        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(responseContentNextPage, 200));
        Page<Aws> nextPage = awsReader.nextPage(firstPage, twilioRestClient);
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(responseContent, 200));
        Page<Aws> previousPage = awsReader.previousPage(nextPage, twilioRestClient);
        Page<Aws> page = awsReader.getPage(firstPageURI, twilioRestClient);

        assertEquals("Ahoy", firstPage.getRecords().get(0).getTestString());
        assertEquals("Matey", nextPage.getRecords().get(0).getTestString());
        assertEquals("Ahoy", previousPage.getRecords().get(0).getTestString());
        assertEquals("Ahoy", page.getRecords().get(0).getTestString());
    }

    @Test(expected = ApiConnectionException.class)
    public void testAwsReaderResponseNull() {
        AwsReader awsReader = new AwsReader();
        awsReader.firstPage(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testAwsReaderResponseNotSuccess() {
        ObjectMapper objectMapper = new ObjectMapper();
        String testResponse =  "{\"credentials\":[], \"meta\": {\"url\":\"" + "url" + "\", \"next_page_url\":\"" + "url" + "?PageSize=5" + "\", \"previous_page_url\":\"" + "url" + "?PageSize=3" + "\", \"first_page_url\":\"" + "url" + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(testResponse, 404));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsReader awsReader = new AwsReader();
        awsReader.firstPage(twilioRestClient);
    }

    @Test
    public void testAwsUpdater() {
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        String testResponse = "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(testResponse, 200));
        AwsUpdater awsUpdater = Aws.updater("sidUpdated").setTestString("testString");
        Aws awsUpdated = awsUpdater.update(twilioRestClient);

        assertEquals("sid", awsUpdated.getAccountSid());
    }
    @Test(expected = ApiConnectionException.class)
    public void testAwsUpdaterResponseNull() {
        AwsUpdater awsUpdater = Aws.updater("123");
        awsUpdater.update(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testAwsUpdaterResponseNotSuccess() {
        AwsUpdater awsUpdater = Aws.updater("123");
        String testResponse = "{\"accountSid\": \"sid\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(testResponse, 404));
        awsUpdater.update(twilioRestClient);
    }

    @Test
    public void testFeedbackCallSummaryObjectCreation() {
        LocalDate startDate = LocalDate.parse("2009-01-27");
        LocalDate endDate = LocalDate.parse("2022-01-27");

        String response = "{\"accountSid\": \"123\"}";
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(response, 200));
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        FeedbackCallSummaryCreator creatorSid = FeedbackCallSummary.creator("sid",startDate, endDate);
        creatorSid.setStartDate(LocalDate.parse("2009-01-17"));
        creatorSid.setEndDate(LocalDate.parse("2021-01-17"));
        FeedbackCallSummary feedbackCallSummary = creatorSid.create(twilioRestClient);

        assertEquals("123", feedbackCallSummary.getAccountSid());
    }

    @Test(expected = ApiConnectionException.class)
    public void testFeedbackCallSummaryObjectCreationResponseNull() {
        LocalDate startDate = LocalDate.parse("2009-01-27");
        LocalDate endDate = LocalDate.parse("2022-01-27");

        when(twilioRestClient.getAccountSid()).thenReturn("sid");
        when(twilioRestClient.request(Mockito.any())).thenReturn(null);

        FeedbackCallSummaryCreator creator = FeedbackCallSummary.creator(startDate, endDate);
        creator.create(twilioRestClient);
    }

    @Test(expected = ApiException.class)
    public void testFeedbackCallSummaryObjectCreationResponseNotSuccess() {
        LocalDate startDate = LocalDate.parse("2009-01-27");
        LocalDate endDate = LocalDate.parse("2022-01-27");

        when(twilioRestClient.getAccountSid()).thenReturn("sid");
        String response = "{\"accountSid\": \"sid\"}";
        when(twilioRestClient.request(Mockito.any())).thenReturn(new Response(response, 404));
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        FeedbackCallSummaryCreator creator = FeedbackCallSummary.creator(startDate, endDate);
        creator.create(twilioRestClient);
    }


    @Test
    public void testFeedbackCallSummaryObjectCreationFromString() {
        String json = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        String jsonDuplicate = "{\"accountSid\": \"a123\", \"sid\": \"123\", \"testInteger\": 123, \"testNumber\": 123.1, \"testNumberFloat\": 123.2, \"testEnum\": \"paused\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        FeedbackCallSummary feedbackCallSummary = FeedbackCallSummary.fromJson(json, objectMapper);
        FeedbackCallSummary feedbackCallSummaryDuplicate = FeedbackCallSummary.fromJson(new ByteArrayInputStream(jsonDuplicate.getBytes()), objectMapper);

        assertEquals("a123", feedbackCallSummary.getAccountSid());
        assertEquals("123", feedbackCallSummary.getSid());
        assertNull(feedbackCallSummary.getTestObject());
        assertEquals(Integer.valueOf("123"), feedbackCallSummary.getTestInteger());
        assertNull(feedbackCallSummary.getTestDateTime());
        assertEquals(BigDecimal.valueOf(123.1), feedbackCallSummary.getTestNumber());
        assertNull(feedbackCallSummary.getPriceUnit());
        assertEquals(Float.valueOf("123.2"), feedbackCallSummary.getTestNumberFloat());
        assertEquals(FeedbackCallSummary.Status.PAUSED.toString(), feedbackCallSummary.getTestEnum().toString());
        assertNull(feedbackCallSummary.getTestArrayOfIntegers());
        assertNull(feedbackCallSummary.getTestArrayOfArrayOfIntegers());
        assertNull(feedbackCallSummary.getTestArrayOfObjects());
        assertNull(feedbackCallSummary.getTestString());

        assertTrue(feedbackCallSummary.equals(feedbackCallSummaryDuplicate));
        // If two objects are equal they must have same hashcode
        assertEquals(feedbackCallSummaryDuplicate.hashCode(), feedbackCallSummary.hashCode());
        assertFalse(feedbackCallSummary.equals(null));
        assertFalse(feedbackCallSummary.equals(new Object()));
    }

    @Test(expected = ApiException.class)
    public void testFeedbackCallSummaryObjectCreationFromInvalidString() {
        String invalidJson = "invalid";

        ObjectMapper objectMapper = new ObjectMapper();
        FeedbackCallSummary.fromJson(invalidJson, objectMapper);
    }

    @Test(expected = ApiException.class)
    public void testFeedbackCallSummaryObjectCreationFromInvalidInputString() {
        String invalidJson = "invalid";

        ObjectMapper objectMapper = new ObjectMapper();
        FeedbackCallSummary.fromJson(new ByteArrayInputStream(invalidJson.getBytes()), objectMapper);
    }
}
