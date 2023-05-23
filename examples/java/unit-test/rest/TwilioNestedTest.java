package com.twilio.rest;

import com.twilio.base.Page;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.twilio.rest.messaging.v1.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TwilioNestedTest {

    @Mock
    private TwilioRestClient twilioRestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(twilioRestClient.getObjectMapper()).thenReturn(objectMapper);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testShouldCreateNumberPool() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "messaging",
                "/v2/NumberPools"
        );
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"id\": \"123\"," +
                " \"name\": \"example\", \"senders\": [\"sender_example\"], " +
                "\"callback_url\":\"http://callback.com\" }", 200));
        NumberPoolModel.NumberPoolRequest numberPoolRequest = new NumberPoolModel.NumberPoolRequest("example",
                Arrays.asList("sender_example"), "http://callback.com");
        NumberPool numberPool = new NumberPoolCreator(numberPoolRequest).create(twilioRestClient);

        assertNotNull(numberPool);
        assertEquals(numberPool.getId(), "123");
    }

    @Test
    public void testShouldGetNumberPoolList() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "messaging",
                "/v2/NumberPools"
        );
        String url = "https://messaging.twilio.com/v2/NumberPools";
        String responseContext = "{\"numberpools\": [ {\"id\": \"123\"," +
                " \"name\": \"example\", \"senders\": [\"sender_example\"], " +
                "\"callback_url\":\"http://callback.com\" }"+ "], " +
                "\"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", " +
                "\"previous_page_url\":\"" + url + "?PageSize=3" + "\", " +
                "\"first_page_url\":\"" + url + "?PageSize=1" +  "\", \"page_size\":4}" +
                "}";
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response(responseContext, 200));
        Page<NumberPool> numberPoolPage = new NumberPoolReader().firstPage(twilioRestClient);

        assertNotNull(numberPoolPage);
        assertEquals(numberPoolPage.getRecords().get(0).getId(), "123");
    }

    @Test
    public void testShouldFetchNumberPool() {
        Request mockRequest = new Request(
                HttpMethod.GET,
                "messaging",
                "/v2/NumberPools/123"
        );
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"id\": \"123\"," +
                " \"name\": \"example\", \"senders\": [\"sender_example\"], " +
                "\"callback_url\":\"http://callback.com\" }", 200));
        NumberPool numberPool = new NumberPoolFetcher("123").fetch(twilioRestClient);

        assertNotNull(numberPool);
        assertEquals(numberPool.getId(), "123");
    }

    @Test
    public void testShouldDeleteNumberPool() {
        Request mockRequest = new Request(
                HttpMethod.DELETE,
                "messaging",
                "/v2/NumberPools/123"
        );
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("Successfully deleted" ,204));
        boolean output = new NumberPoolDeleter("123").delete(twilioRestClient);
        assertTrue(output);
    }

    @Test
    public void testShouldUpdateNumberPool() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "messaging",
                "/v2/NumberPools/123"
        );
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"id\": \"123\"," +
                " \"name\": \"example2\", \"senders\": [\"sender_example2\"], " +
                "\"callback_url\":\"http://callback2.com\" }", 200));
        NumberPoolModel.NumberPoolRequest numberPoolRequest = new NumberPoolModel.NumberPoolRequest("example2",
                Arrays.asList("sender_example2"), "http://callback2.com");
        NumberPool numberPoolUpdated = new NumberPoolUpdater("123", numberPoolRequest).update(twilioRestClient);

        assertEquals(numberPoolUpdated.getId(),  "123");
        assertEquals(numberPoolUpdated.getName(),  "example2");
    }


    @Test
    public void testCreateUseCase() {
        Request mockRequest = new Request(
                HttpMethod.POST,
                "messaging",
                "/v2/UseCases"
        );
        when(twilioRestClient.request(mockRequest)).thenReturn(new Response("{\"name\": \"example\", \"intent\": \"intent_example\", " +
                "\"pipeline\":\"[pipeline_example]\" }", 200));

        UseCaseModel.UseCaseRequestPipeline useCaseRequestPipeline = new UseCaseModel.UseCaseRequestPipeline("type_example",
                true, true, "twilio", true);
        UseCaseModel.UseCaseRequest usePoolRequest = new UseCaseModel.UseCaseRequest("example",
                "intent_example", Arrays.asList(useCaseRequestPipeline));
        assertNotNull(usePoolRequest);
        assertEquals(usePoolRequest.getName(), "example");
    }

}
