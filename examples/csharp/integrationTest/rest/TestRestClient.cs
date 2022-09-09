using Twilio.Http;
using System;
using System.Text;
using System.Linq;
using System.Collections.Generic;
using Twilio.Clients;
using Twilio.Converters;

namespace examples.csharp.integrationTest.rest
{
    public class TestRestClient : TwilioRestClient, ITwilioRestClient
    {
        String baseURL;

        public TestRestClient( String username, String password, String url) : base(username,password) {
            this.baseURL = url;
        }
        
        public new Response Request(Request request) {
            StringBuilder sb = new StringBuilder(request.Uri.ToString());
            // manipulate string for mock server url
            sb.Remove(0, request.Uri.ToString().IndexOf("twilio.com") + 10);
            sb.Insert(0, baseURL);
            Request testRequest = new Request(request.Method, sb.ToString());
            // updating the query and post params
            request.PostParams.ForEach(val => testRequest.AddPostParam(val.Key, val.Value));
            request.QueryParams.ForEach(val => testRequest.AddPostParam(val.Key, val.Value));
            return base.Request(testRequest);
        }
    }
}