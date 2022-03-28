package com.twilio.rest;

import static org.junit.Assert.*;

import com.twilio.rest.api.v2010.Account.Message;
import com.twilio.rest.api.v2010.Account.MessageReader;
import com.twilio.rest.api.v2010.Credential.AwsCreator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.URI;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.HttpMethod;
import com.twilio.base.ResourceSet;
import com.twilio.http.TwilioRestClient;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import com.twilio.rest.api.v2010.Account.Call.RecordingCreator;
import com.twilio.rest.api.v2010.Account.Call.RecordingReader;
import com.twilio.rest.api.v2010.Account.Call.RecordingFetcher;

import com.twilio.rest.api.v2010.Account.Call.Recording;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//TODO: Ignore Test cases will covered in future stories
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
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"account_sid\":\"AC222222222222222222222222222222\", \"callSid\":\"PNXXXXY\"}", 200));
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
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"sid\": 123, \"callSid\":\"PNXXXXY\"}", 200));
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
        LocalDate localDate  = LocalDate.now();
        mockRequest.addQueryParam("DateCreated", currentDateTime.toString());
        mockRequest.addQueryParam("DateTest", localDate.toString());
        mockRequest.addQueryParam("DateCreatedLessThan", currentDateTime.toString());
        mockRequest.addQueryParam("DateCreatedGreaterThan", currentDateTime.toString());
        mockRequest.addQueryParam("PageSize", "4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = "{\"recordings\":[{\"callSid\":\"PNXXXXY\", \"sid\":123}], \"meta\": {\"url\":\""+url+"\", \"next_page_url\":\""+url+"?PageSize=5"+"\", \"previous_page_url\":\""+url+"?PageSize=3"+"\", \"first_page_url\":\""+url+"?PageSize=1"+"\", \"page_size\":4}}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContent, 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        RecordingReader recordingReader = new RecordingReader("AC222222222222222222222222222222", "PNXXXXY");
        recordingReader.setDateCreated(currentDateTime);
        recordingReader.setDateCreatedLessThan(currentDateTime);
        recordingReader.setDateCreatedGreaterThan(currentDateTime);
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
        mockRequest.addPostParam("AccountSid","AC222222222222222222222222222222");
        mockRequest.addPostParam("CallSid","PNXXXXY");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"recordings\":[{\"callSid\":\"PNXXXXY\", \"sid\":123}]}", 200));
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
        objectMapper.registerModule(new JavaTimeModule());
        mockRequest.addQueryParam("PageSize", "5");
        mockRequest.addQueryParam("To", "9999999999");
        mockRequest.addQueryParam("From", "4444444444");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"messages\":[{\"from\":\"4444444444\", \"to\":\"9999999999\", \"PageSize\":\"5\"}], \"meta\": {\"url\":\"https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json\", \"next_page_url\":\"\", \"previous_page_url\":\"\", \"first_page_url\":\"\", \"page_size\":5}}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        MessageReader messageReader = new MessageReader("AC222222222222222222222222222222");
        messageReader.setFrom("4444444444");
        messageReader.setTo("9999999999");
        messageReader.setPageSize(5);

        ResourceSet<Message> message = messageReader.read(twilioRestClient);

        assertNotNull(message);
    }

    @Test
    @Ignore
    public void testShouldReadPageOfMessages() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"messages\":[{\"from\":\"\", \"to\":\"\"}], \"meta\": {\"url\":\"ad\", \"next_page_url\":\"ad\", \"previous_page_url\":\"ad\", \"first_page_url\":\"\", \"page_size\":4}}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        MessageReader messageReader = new MessageReader("AC222222222222222222222222222222");
        messageReader.setFrom("from");
        messageReader.setTo("to");
        messageReader.setPageSize(5);

        messageReader.read(twilioRestClient);

        assertNotNull(messageReader);
    }

    @Test
    @Ignore
    public void testShouldListErrorOnPageLimitCross() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "api",
                "/2010-04-01/Accounts/AC222222222222222222222222222222/Messages.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"messages\":[{\"from\":\"\", \"to\":\"\"}], \"meta\": {\"url\":\"ad\", \"next_page_url\":\"ad\", \"previous_page_url\":\"ad\", \"first_page_url\":\"\", \"page_size\":4}}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        MessageReader messageReader = new MessageReader("AC222222222222222222222222222222");
        messageReader.setFrom("from");
        messageReader.setTo("to");
        messageReader.setPageSize(5);

        messageReader.read(twilioRestClient);

        assertNotNull(messageReader);
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
        mockRequest.addPostParam("Credentials","AC222222222222222222222222222222");
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"accountSid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}", 200));
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        AwsCreator awsCreator = new AwsCreator("AC222222222222222222222222222222");
        awsCreator.setTestObjectArray(Arrays.asList(item1, item2));
        awsCreator.create(twilioRestClient);

        assertNotNull(awsCreator);
    }
}


