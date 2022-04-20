package com.twilio;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;

public class TestRestClient extends TwilioRestClient{
    String username;
    String password;
    String baseURL;
    TestRestClient(Builder b, String url) {
        super(b);  
        this.baseURL = url;
    }
    @Override
    public Response request(final Request request) {
        StringBuilder sb = new StringBuilder(request.getUrl());
        // manipulate string for mock server url
        sb.replace(0, (request.getUrl().indexOf("twilio.com")+10), baseURL);
        Request testRequest = new Request(request.getMethod(), sb.toString());
        // updating the query and post params
        request.getPostParams().forEach((key, value) -> value.forEach(listValue -> testRequest.addPostParam(key, listValue)));
        request.getQueryParams().forEach((key, value) -> value.forEach(listValue -> testRequest.addPostParam(key, listValue)));
        return super.request(testRequest);
    }
}
