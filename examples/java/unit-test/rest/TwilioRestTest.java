package com.twilio.rest;

import com.twilio.base.Page;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.AccountCreator;
import com.twilio.rest.api.v2010.AccountFetcher;
import com.twilio.rest.api.v2010.AccountReader;
import com.twilio.rest.api.v2010.AccountDeleter;
import com.twilio.rest.api.v2010.Call;
import com.twilio.rest.api.v2010.CallFetcher;
import com.twilio.rest.api.v2010.Aws;
import com.twilio.rest.api.v2010.AwsCreator;
import com.twilio.rest.api.v2010.AwsReader;

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
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import java.time.format.DateTimeFormatter;
import com.twilio.converter.DateConverter;

public class TwilioRestTest {
    @Mock
    private TwilioRestClient twilioRestClient;

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

        Account account = new AccountFetcher("AC222222222222222222222222222222").fetch(twilioRestClient);

        assertNotNull(account);
    }

    @Test(expected=Exception.class)
    public void testShouldMakeInValidAPICallReturnsNullForAccountFetcher() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Account account = new AccountFetcher("AC222222222222222222222222222222").fetch(twilioRestClient);
        assertNotNull(account);
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
        Account account = new AccountFetcher("AC222222222222222222222222222222").fetch(twilioRestClient);
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

        assertNotNull(account);
    }

    @Test(expected=Exception.class)
    public void testShouldMakeInValidAPICallReturnsNulForAccountDeleter() {
        Request mockRequest = new Request(
                HttpMethod.DELETE,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(null);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Boolean account = new AccountDeleter("AC222222222222222222222222222222").delete(twilioRestClient);
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
        Boolean account = new AccountDeleter("AC222222222222222222222222222222").delete(twilioRestClient);
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
    }

//    @Test(expected=Exception.class)
//    public void testShouldSendNullResponseForAccountCreator() {
//        List<String> recordingStatusCallbackEvent = Arrays.asList("http://test1.com/", "http://test2.com");
//        Request mockRequest = new Request(
//                HttpMethod.POST,
//                Domains.API.toString(),
//                "/2010-04-01/Accounts.json"
//        );
//        for(String recordingStatusEvent : recordingStatusCallbackEvent){
//            mockRequest.addPostParam("RecordingStatusCallbackEvent", recordingStatusEvent);
//        }
//        mockRequest.addPostParam("RecordingStatusCallback", "https://validurl.com");
//        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        when(twilioRestClient.request(mockRequest)).thenReturn(null);
//        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
//        AccountCreator accountCreator = new AccountCreator();
//        accountCreator.setRecordingStatusCallbackEvent(recordingStatusCallbackEvent);
//        accountCreator.setRecordingStatusCallback(URI.create("https://validurl.com"));
//        accountCreator.setXTwilioWebhookEnabled("true");
//
//        Account account  = accountCreator.create(twilioRestClient);
//    }

//    @Test(expected=Exception.class)
//    public void testShouldSendIncorrectStatusForAccountCreator() {
//        Request mockRequest = new Request(
//                HttpMethod.POST,
//                Domains.API.toString(),
//                "/2010-04-01/Accounts.json"
//        );
//        mockRequest.addPostParam("RecordingStatusCallback", null);
//        mockRequest.addHeaderParam("X-Twilio-Webhook-Enabled", "true");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}]}", 404));
//        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
//        AccountCreator accountCreator = new AccountCreator();
//        accountCreator.setRecordingStatusCallbackEvent(null);
//        accountCreator.setRecordingStatusCallback(null);
//        accountCreator.setXTwilioWebhookEnabled(null);
//
//        Account account  = accountCreator.create(twilioRestClient);
//    }

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
    }

    @Test
    public void testShouldAddAccountSidIfNotPresent() {
        when(twilioRestClient.getAccountSid()).thenReturn("AC222222222222222222222222222222");
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
        assertEquals(call.getAccountSid(), "AC222222222222222222222222222222");
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
        List<Object> testObjectArray = Arrays.asList(item1, item2);
        for(Object testObject : testObjectArray){
            mockRequest.addPostParam("TestObjectArray", testObject.toString());
        }
        mockRequest.addPostParam("TestString", "AC222222222222222222222222222222");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsCreator awsCreator = new AwsCreator("AC222222222222222222222222222222");
        awsCreator.setTestObjectArray(Arrays.asList(item1, item2));
        Aws aws = awsCreator.create(twilioRestClient);
        assertNotNull(aws);
    }

    @Test
    public void testAnyTypeParam() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "api",
                "/v1/Credentials/AWS"
        );

        Map<String, Object> anyMap = new HashMap<>();
        anyMap.put("TestInteger", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockRequest.addPostParam("TestAnyType", "{TestInteger=1}");
        mockRequest.addPostParam("TestString", "AC222222222222222222222222222222");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsCreator awsCreator = new AwsCreator("AC222222222222222222222222222222");
        awsCreator.setTestAnyType(anyMap);
        Aws aws = awsCreator.create(twilioRestClient);

        assertNotNull(aws);
        assertNotNull(awsCreator);
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
        assertEquals("123", account.iterator().next().getSid());
        assertTrue(account.iterator().next().getTestObject().getMms());
        assertFalse(account.iterator().next().getTestObject().getSms());
        assertFalse(account.iterator().next().getTestObject().getVoice());
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
        Request mockRequest = new Request(HttpMethod.GET, Domains.FLEXAPI.toString(), "/v1/uri");
        assertNotNull(mockRequest);
        assertEquals(HttpMethod.GET, mockRequest.getMethod());
        assertEquals("https://flex-api.twilio.com/v1/uri", mockRequest.getUrl());
    }
}
