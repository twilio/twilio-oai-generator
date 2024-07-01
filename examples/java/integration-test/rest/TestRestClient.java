package com.twilio.rest;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;

public class TestRestClient extends TwilioRestClient {
    String scheme;

    TestRestClient(final Builder b, final String scheme) {
        super(b);
        this.scheme = scheme;
    }

    @Override
    public Response request(final Request request) {
        StringBuilder sb = new StringBuilder(request.getUrl());
        // manipulate string for mock server url
        sb.replace(0, 5, scheme);
        Request testRequest = new Request(request.getMethod(), sb.toString());
        testRequest.setContentType(request.getContentType());
        // updating the query and post params
        request.getPostParams().forEach((key, value) -> value.forEach(listValue -> testRequest.addPostParam(key, listValue)));
        request.getQueryParams().forEach((key, value) -> value.forEach(listValue -> testRequest.addPostParam(key, listValue)));
        return super.request(testRequest);
    }
}
