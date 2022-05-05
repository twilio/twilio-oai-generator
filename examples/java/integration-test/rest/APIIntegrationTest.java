package com.twilio.rest;

import com.twilio.Twilio;
import com.twilio.base.Page;
import com.twilio.http.NetworkHttpClient;
import com.twilio.http.TwilioRestClient.Builder;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.rest.api.v2010.account.CallDeleter;
import com.twilio.rest.api.v2010.account.CallFetcher;
import com.twilio.rest.api.v2010.account.call.Recording;
import com.twilio.rest.api.v2010.account.call.RecordingReader;
import com.twilio.rest.api.v2010.account.call.RecordingUpdater;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

public class APIIntegrationTest {
    final String baseURL = "http://prism_twilio:4010";
    final String ACCOUNT_SID = "AC12345678123456781234567812345678";

    @Before
    public void setUp() {
        String authToken = "CR12345678123456781234567812345678";

        TestRestClient testClient = new TestRestClient(new Builder(ACCOUNT_SID, authToken).httpClient(new NetworkHttpClient()), baseURL);
        Twilio.init(ACCOUNT_SID, authToken);
        Twilio.setRestClient(testClient);
    }

    @Test
    public void testGetCall() {
        Page<Recording> resource = new RecordingReader(ACCOUNT_SID, "CR12345678123456781234567812345678").firstPage();
        assertNotNull(resource);
        assertEquals("Ahoy", resource.getRecords().get(0).getTestString());
    }

    @Test
    public void testFetchCall() {
        Call call = new CallFetcher(ACCOUNT_SID, "CA1234567890123456789012").fetch();
        assertNotNull(call);
        assertEquals("CR12345678123456781234567812345678", call.getSid());
        assertEquals("Ahoy", call.getTestString());
    }

    @Test
    public void testPost() {
        Call call = new CallCreator(ACCOUNT_SID, "testString").create();
        assertEquals("Ahoy", call.getTestString());
        assertNotNull(call);
    }

    @Test
    public void testDelete() {
        assertTrue(new CallDeleter(ACCOUNT_SID, "CA1234567890123456789012").delete());
    }

    @Test
    public void testUpdate() {
        Recording recording = new RecordingUpdater(ACCOUNT_SID, "CA1234567890123456789012", 12, "paused").update();
        assertNotNull(recording);
    }

    @Test
    public void testDateTimeQueryParam() {
        Page<Recording> recording = new RecordingReader(ACCOUNT_SID, "CR12345678123456781234567812345678")
            .setDateTest(LocalDate.now())
            .setDateCreatedBefore(ZonedDateTime.now().minusMonths(-2))
            .setDateCreatedAfter(ZonedDateTime.now().minusMonths(2))
            .pageSize(5)
            .firstPage();
        assertEquals(2, recording.getRecords().size());
        assertEquals("Ahoy", recording.getRecords().get(0).getTestString());
        assertEquals("Matey", recording.getRecords().get(1).getTestString());
    }

    @Test
    public void testCustomTypes() {
        Call call = new CallFetcher("CA1234567890123456789012", "CA1234567890123456789012").fetch();
        assertNotNull(call);
        assertEquals(Boolean.FALSE, call.getTestObject().getFax());
        assertEquals(Boolean.FALSE, call.getTestObject().getMms());
        assertEquals(Boolean.TRUE, call.getTestObject().getSms());
        assertEquals(Boolean.TRUE, call.getTestObject().getVoice());
    }
}
