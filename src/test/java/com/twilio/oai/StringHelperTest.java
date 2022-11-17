package com.twilio.oai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringHelperTest {

    @Test
    public void testToSnakeCase() {
        assertEquals("some_a2p_thing", StringHelper.toSnakeCase("SomeA2PThing"));
        assertEquals("psd2_enabled", StringHelper.toSnakeCase("Psd2Enabled"));
        assertEquals("aws_s3_url", StringHelper.toSnakeCase("AwsS3Url"));
        assertEquals("callback_url", StringHelper.toSnakeCase("callbackURL"));
    }

    @Test
    public void testCamelize() {
        assertEquals("SomeA2PThing", StringHelper.camelize("some_a2p_thing"));
        assertEquals("Psd2Enabled", StringHelper.camelize("psd2_enabled"));
        assertEquals("AwsS3Url", StringHelper.camelize("aws_s3_url"));
        assertEquals("CallbackUrl", StringHelper.camelize("callback_url"));
    }
}
