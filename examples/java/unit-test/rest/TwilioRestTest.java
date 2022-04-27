package com.twilio.rest;

import static org.junit.Assert.*;

import com.twilio.rest.api.v2010.credential.Aws;
import com.twilio.rest.api.v2010.credential.AwsCreator;
import com.twilio.rest.api.v2010.credential.AwsReader;
import org.json.CDL;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.HttpMethod;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.http.TwilioRestClient;

import com.twilio.rest.api.v2010.account.call.RecordingCreator;
import com.twilio.rest.api.v2010.account.call.RecordingReader;
import com.twilio.rest.api.v2010.account.call.RecordingFetcher;
import com.twilio.rest.api.v2010.account.call.Recording;

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
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings/123.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"call_sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        Recording recording = new RecordingFetcher("AC222222222222222222222222222222", "PNXXXXY", 123).fetch(twilioRestClient);

        assertNotNull(recording);
    }

    @Test
    public void testShouldSendAddedHeadersThroughRestClient() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                com.twilio.rest.Domains.API.toString(),
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        mockRequest.addPostParam("XTwilioWebhookEnabled", "true");
        mockRequest.addPostParam("RecordingStatusCallback", "https://validurl.com");
        mockRequest.addPostParam("CallSid", "PNXXXXY");
        mockRequest.addPostParam("AccountSid", "AC222222222222222222222222222222");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"sid\": 123, \"call_sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingCreator recordingCreator = new RecordingCreator("AC222222222222222222222222222222", "PNXXXXY");
        recordingCreator.setXTwilioWebhookEnabled("true");
        recordingCreator.setRecordingStatusCallback(URI.create("https://validurl.com"));

        Recording recording = recordingCreator.create(twilioRestClient);

        assertNotNull(recording);
    }

    @Test
    public void testShouldQueryParamInRequest() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        mockRequest.addQueryParam("DateCreated", currentDateTime.toString());
        mockRequest.addQueryParam("DateTest", localDate.toString());
        mockRequest.addQueryParam("DateCreatedBefore", currentDateTime.toString());
        mockRequest.addQueryParam("DateCreatedAfter", currentDateTime.toString());
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setDateCreated(currentDateTime);
        recordingReader.setDateCreatedBefore(currentDateTime);
        recordingReader.setDateCreatedAfter(currentDateTime);
        recordingReader.setDateTest(localDate);
        recordingReader.setPageSize(4);

        ResourceSet<Recording> recording = recordingReader.read(twilioRestClient);

        assertNotNull(recording);
    }

    @Test
    public void testShouldSendArrayTypeParamInRequest() {
        List<String> recordingStatusCallbackEvent = Arrays.asList("http://test1.com/", "http://test2.com");
        Request mockRequest = new Request(
                HttpMethod.POST,
                com.twilio.rest.Domains.API.toString(),
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        mockRequest.addPostParam("RecordingStatusCallbackEvent", recordingStatusCallbackEvent.toString());
        mockRequest.addPostParam("AccountSid", "AC222222222222222222222222222222");
        mockRequest.addPostParam("CallSid", "PNXXXXY");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123}]}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingCreator recordingCreator = new RecordingCreator("AC222222222222222222222222222222", "PNXXXXY");
        recordingCreator.setRecordingStatusCallbackEvent(recordingStatusCallbackEvent);

        Recording recording = recordingCreator.create(twilioRestClient);

        assertNotNull(recording);
    }

    @Test
    @Ignore
    public void testShouldReadListOfMessages() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String messagesValues =
                "direction,      from,         to,           body,  status    \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-call,  4444444444,   9999999999,   Hi,    queued    \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-api,   4444444444,   9999999999,   Hello, sent      \n";
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("end", 4);
        jsonMap.put("first_page_uri", "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0");
        jsonMap.put("uri", "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=");
        jsonMap.put("page_size", 5);
        jsonMap.put("start", 0);
        jsonMap.put("next_page_uri", "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130");
        jsonMap.put("page", 0);
        jsonMap.put("messages", CDL.toJSONArray(messagesValues));


        JSONObject response = new JSONObject(jsonMap);
        mockRequest.addQueryParam("PageSize", "5");
        mockRequest.addQueryParam("To", "9999999999");
        mockRequest.addQueryParam("From", "4444444444");

        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(response.toString(), 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsReader awsReader = new AwsReader();
        awsReader.setPageSize(5);

        ResourceSet<Aws> awsResourceSet = awsReader.read(twilioRestClient);
        assertNotNull(awsResourceSet);
    }

    @Test
    @Ignore
    public void testShouldReadPageOfMessages() {
        String uri = "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/{CallSid}/Recordings.json";
        Request mockRequestPage0 = new Request(
                HttpMethod.GET,
                "api",
                uri
        );
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);

        String messagesValues =
                "direction,      from,         to,           body,  status    \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-call,  4444444444,   9999999999,   Hi,    queued    \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-api,   4444444444,   9999999999,   Hi,    delivered \n" +
                        "outbound-api,   4444444444,   9999999999,   Hello, sent      \n";
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("end", 4);
        jsonMap.put("first_page_uri", uri + "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0");
        jsonMap.put("uri", "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=");
        jsonMap.put("page_size", 5);
        jsonMap.put("start", 0);
        jsonMap.put("next_page_uri", "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130");
        jsonMap.put("page", 0);
        jsonMap.put("messages", CDL.toJSONArray(messagesValues));
        JSONObject page0 = new JSONObject(jsonMap);
        mockRequestPage0.addQueryParam("PageSize", "5");
        mockRequestPage0.addQueryParam("To", "9999999999");
        mockRequestPage0.addQueryParam("From", "4444444444");
        when(twilioRestClient.request(mockRequestPage0)).thenReturn(new Response(page0.toString(), 200));

        Request mockRequestPage1 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130"
        );
        Map<String, Object> jsonMap1 = new HashMap<String, Object>();
        jsonMap1.put("page", 1);
        jsonMap1.put("uri", "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130");
        jsonMap1.put("next_page_uri", "");
        jsonMap1.put("messages", CDL.toJSONArray(messagesValues));
        JSONObject page1 = new JSONObject(jsonMap1);
        when(twilioRestClient.request(mockRequestPage1)).thenReturn(new Response(page1.toString(), 200));
        AwsReader awsReader = new AwsReader();
        awsReader.setPageSize(5);

        ResourceSet<Aws> aws = awsReader.read(twilioRestClient);
        List<Aws> messages = new ArrayList<>();
        aws.iterator().forEachRemaining(e -> messages.add(e));
        assertNotNull(aws);
        assertSame("total messages from all pages", messages.size(), 10);
    }

    @Test
    @Ignore
    public void testListError() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("code", 20001);
        jsonMap.put("message", "Invalid PageSize.");
        jsonMap.put("more_info", "https://www.twilio.com/docs/errors/20001");
        jsonMap.put("status", 400);
        JSONObject response = new JSONObject(jsonMap);
        mockRequest.addQueryParam("To", "9999999999");
        mockRequest.addQueryParam("From", "4444444444");
        mockRequest.addQueryParam("PageSize", "0");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(response.toString(), 400));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsReader messageReader = new AwsReader();
        messageReader.setPageSize(0);
        assertThrows("Invalid PageSize.", ApiException.class, () -> messageReader.read(twilioRestClient));
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
        item2.put("B", Arrays.asList("Banana"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockRequest.addPostParam("TestObjectArray", "[{A=[Apple, Aces]}, {B=[Banana]}]");
        mockRequest.addPostParam("TestString", "AC222222222222222222222222222222");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsCreator awsCreator = new AwsCreator("AC222222222222222222222222222222");
        awsCreator.setTestObjectArray(Arrays.asList(item1, item2));
        awsCreator.create(twilioRestClient);

        assertNotNull(awsCreator);
    }

    @Test
    public void testShouldSupportNestedPropertiesResponse() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        mockRequest.addQueryParam("DateCreated", currentDateTime.toString());
        mockRequest.addQueryParam("DateTest", localDate.toString());
        mockRequest.addQueryParam("DateCreatedBefore", currentDateTime.toString());
        mockRequest.addQueryParam("DateCreatedAfter", currentDateTime.toString());
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setDateCreated(currentDateTime);
        recordingReader.setDateCreatedBefore(currentDateTime);
        recordingReader.setDateCreatedAfter(currentDateTime);
        recordingReader.setDateTest(localDate);
        recordingReader.setPageSize(4);

        ResourceSet<Recording> recording = recordingReader.read(twilioRestClient);

        assertNotNull(recording);
        assertEquals("123", recording.iterator().next().getSid());
        assertEquals(true, recording.iterator().next().getTestObject().getMms());
        assertEquals(false, recording.iterator().next().getTestObject().getSms());
        assertEquals(false, recording.iterator().next().getTestObject().getVoice());
        assertEquals(true, recording.iterator().next().getTestObject().getFax());
    }

    @Test
    public void testShouldSupportArrayOfObjectResponse() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        mockRequest.addQueryParam("DateCreated", currentDateTime.toString());
        mockRequest.addQueryParam("DateTest", localDate.toString());
        mockRequest.addQueryParam("DateCreatedBefore", currentDateTime.toString());
        mockRequest.addQueryParam("DateCreatedAfter", currentDateTime.toString());
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setDateCreated(currentDateTime);
        recordingReader.setDateCreatedBefore(currentDateTime);
        recordingReader.setDateCreatedAfter(currentDateTime);
        recordingReader.setDateTest(localDate);
        recordingReader.setPageSize(4);

        ResourceSet<Recording> recording = recordingReader.read(twilioRestClient);

        assertNotNull(recording);
        assertEquals("123", recording.iterator().next().getSid());
        assertEquals(1, recording.iterator().next().getTestArrayOfObjects().size());
        assertEquals("Testing Object Array", recording.iterator().next().getTestArrayOfObjects().get(0).getDescription());
        assertEquals(123, recording.iterator().next().getTestArrayOfObjects().get(0).getCount());
    }

    @Test
    public void testShouldSupportDeserializationTypes() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json"
        );
        Request mockRequest2 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json?PageSize=5"
        );
        String url = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        LocalDate localDate = LocalDate.now();
        mockRequest.addQueryParam("DateCreated", currentDateTime.toString());
        mockRequest.addQueryParam("DateTest", localDate.toString());
        mockRequest.addQueryParam("DateCreatedBefore", currentDateTime.toString());
        mockRequest.addQueryParam("DateCreatedAfter", currentDateTime.toString());
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"testDateTime\":\"2021-03-23T16:43:32.010069453-05:00\", \"price_unit\": \"USD\" }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.request(mockRequest2)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setDateCreated(currentDateTime);
        recordingReader.setDateCreatedBefore(currentDateTime);
        recordingReader.setDateCreatedAfter(currentDateTime);
        recordingReader.setDateTest(localDate);
        recordingReader.setPageSize(4);

        ResourceSet<Recording> recording = recordingReader.read(twilioRestClient);

        assertNotNull(recording);
        assertEquals("123",recording.iterator().next().getSid());
        assertEquals("USD", recording.iterator().next().getPriceUnit().toString() );
        assertEquals("2021-03-23T21:43:32.010069453Z[UTC]",recording.iterator().next().getTestDateTime().toString());
    }
}
