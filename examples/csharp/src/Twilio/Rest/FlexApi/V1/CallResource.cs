/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using Twilio.Base;
using Twilio.Clients;
using Twilio.Converters;
using Twilio.Exceptions;
using Twilio.Http;



namespace Twilio.Rest.FlexApi.V1
{
    public class CallResource : Resource
    {
    





        
        private static Request BuildUpdateRequest(UpdateCallOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Voice/{Sid}";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);


            return new Request(
                HttpMethod.Post,
                Rest.Domain.FlexApi,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }


        public static CallResource Update(UpdateCallOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<CallResource> UpdateAsync(UpdateCallOptions options,
                                                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        public static CallResource Update(
                                          string pathSid,
                                          ITwilioRestClient client = null)
        {
            var options = new UpdateCallOptions(pathSid){  };
            return Update(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<CallResource> UpdateAsync(
                                                                              string pathSid,
                                                                              ITwilioRestClient client = null)
        {
            var options = new UpdateCallOptions(pathSid){  };
            return await UpdateAsync(options, client);
        }
        #endif
    
        public static Page<CallResource> GetPage(string targetUrl, ITwilioRestClient client)
        {
            client = client ?? TwilioClient.GetRestClient();

            var request = new Request(
                HttpMethod.Get,
                targetUrl
            );

            var response = client.Request(request);
            return Page<CallResource>.FromJson("", response.Content);
        }

        public static Page<CallResource> NextPage(Page<CallResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetNextPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<CallResource>.FromJson("", response.Content);
        }

        public static Page<CallResource> PreviousPage(Page<CallResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetPreviousPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<CallResource>.FromJson("", response.Content);
        }

        public static CallResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<CallResource>(json);
            }
            catch (JsonException e)
            {
                throw new ApiException(e.Message, e);
            }
        }


    
        [JsonProperty("sid")]
        public string Sid { get; private set; }



        private CallResource() {

        }
    } // end of resource class
} // end of namespace

