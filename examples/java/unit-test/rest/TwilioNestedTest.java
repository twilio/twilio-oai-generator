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
import org.junit.Ignore;
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

}
