package com.twilio.rest;
import static org.junit.Assert.*;

import com.twilio.rest.api.v2010.credential.Aws;
import com.twilio.rest.api.v2010.credential.AwsCreator;
import com.twilio.rest.api.v2010.credential.AwsReader;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;



import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.HttpMethod;
import com.twilio.base.Page;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.http.TwilioRestClient;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import com.twilio.rest.api.v2010.account.call.RecordingCreator;
import com.twilio.rest.api.v2010.account.call.RecordingReader;
import com.twilio.rest.api.v2010.account.call.RecordingFetcher;
import com.twilio.rest.api.v2010.account.call.Recording;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    public void testShouldAddAccountSidIfNotPresent() {
        when(twilioRestClient.getAccountSid()).thenReturn("AC222222222222222222222222222222");
        Request mockRequest = new Request(
                HttpMethod.GET,
                com.twilio.rest.Domains.API.toString(),
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings/123.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\",\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}", 200));
        Recording recording = new RecordingFetcher( "PNXXXXY", 123).fetch(twilioRestClient);
        assertNotNull(recording);
        assertEquals("123", recording.getSid());
        assertEquals("AC222222222222222222222222222222", recording.getAccountSid());
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
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(testResponse.toString(), 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        Page<Aws> awsPage = new AwsReader().firstPage(twilioRestClient);
        assertEquals("Ahoy",  awsPage.getRecords().get(0).getTestString());
        assertEquals("Hello",  awsPage.getRecords().get(1).getTestString());
    }

    @Test
    public void testShouldReadPageOfMessages() {
        String uri = "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        Request mockRequestPage0 = new Request(
                HttpMethod.GET,
                "api",
                uri
        );
        ObjectMapper objectMapper = new ObjectMapper();
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        mockRequestPage0.addQueryParam("PageSize", "2");
        String url = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.json";
        String nextPageURL = "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130";
        String responseContent = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Ahoy\"}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + nextPageURL + "?PageSize=2" + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + url + "?PageSize=2" + "\", \"page_size\":2}}";
        when(twilioRestClient.request(mockRequestPage0)).thenReturn(new Response(responseContent, 200));

        Request mockRequestPage1 = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/PNXXXXY/Recordings.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130?PageSize=2"
        );
        String responseContent2 = "{\"recordings\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Matey\"}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + url + "?PageSize=2" + "\", \"page_size\":2}}";
        when(twilioRestClient.request(mockRequestPage1)).thenReturn(new Response(responseContent2, 200));
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setPageSize(2);
        ResourceSet<Recording> recording = recordingReader.read(twilioRestClient);
        List<String> testStringValues = new ArrayList<>();
        recording.forEach(recordingVal -> {
                assertNotNull(recordingVal);
                testStringValues.add(recordingVal.getTestString());
        });
        assertEquals("Ahoy", testStringValues.get(0));
        assertEquals("Matey", testStringValues.get(1));
    }

    @Test
    public void testListError() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/v1/Credentials/AWS"
        );
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> content = new HashMap<String, Object>();
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
