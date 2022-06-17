package com.twilio.rest;

import com.twilio.Twilio;
import com.twilio.base.Page;
import com.twilio.http.NetworkHttpClient;
import com.twilio.http.TwilioRestClient.Builder;
import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.AccountReader;
import com.twilio.rest.api.v2010.AccountUpdater;
import com.twilio.rest.api.v2010.Call;
import com.twilio.rest.api.v2010.CallCreator;
import com.twilio.rest.api.v2010.CallDeleter;
import com.twilio.rest.api.v2010.CallFetcher;

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
    public void testGet() {
        Page<Account> resource = new AccountReader().firstPage();
        assertNotNull(resource);
        assertEquals("Ahoy", resource.getRecords().get(0).getTestString());
    }

    @Test
    public void testFetchCall() {
        Call call = new CallFetcher(ACCOUNT_SID, 123).fetch();
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
        assertTrue(new CallDeleter(ACCOUNT_SID, 123).delete());
    }

    @Test
    public void testUpdate() {
        Account account = new AccountUpdater(ACCOUNT_SID, Account.Status.PAUSED).update();
        assertNotNull(account);
    }

    @Test
    public void testDateTimeQueryParam() {
        Page<Account> account = new AccountReader()
                .setDateTest(LocalDate.now())
                .setDateCreatedBefore(ZonedDateTime.now().minusMonths(-2))
                .setDateCreatedAfter(ZonedDateTime.now().minusMonths(2))
                .pageSize(5)
                .firstPage();
        assertEquals(2, account.getRecords().size());
        assertEquals("Ahoy", account.getRecords().get(0).getTestString());
        assertEquals("Matey", account.getRecords().get(1).getTestString());
    }

    @Test
    public void testCustomTypes() {
        Call call = new CallFetcher("CA1234567890123456789012", 123).fetch();
        assertNotNull(call);
        assertEquals(Boolean.FALSE, call.getTestObject().getFax());
        assertEquals(Boolean.FALSE, call.getTestObject().getMms());
        assertEquals(Boolean.TRUE, call.getTestObject().getSms());
        assertEquals(Boolean.TRUE, call.getTestObject().getVoice());
    }
}
