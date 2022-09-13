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

using Twilio.Types;

using Twilio.Types;


namespace Twilio.Rest.Api.V2010.Credential
{
    public class AwsResource : Resource
    {
    
        public sealed class StatusEnum : StringEnum
        {
            private StatusEnum(string value) : base(value) {}
            public StatusEnum() {}
            public static implicit operator StatusEnum(string value)
            {
                return new StatusEnum(value);
            }
            public static readonly StatusEnum InProgress = new StatusEnum("in-progress");
            public static readonly StatusEnum Paused = new StatusEnum("paused");
            public static readonly StatusEnum Stopped = new StatusEnum("stopped");
            public static readonly StatusEnum Processing = new StatusEnum("processing");
            public static readonly StatusEnum Completed = new StatusEnum("completed");
            public static readonly StatusEnum Absent = new StatusEnum("absent");

        }


        
        private static Request BuildDeleteRequest(DeleteAwsOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS/{Sid}";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);


            return new Request(
                HttpMethod.Delete,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        public static bool Delete(DeleteAwsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(DeleteAwsOptions options,
                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }
        #endif

        public static bool Delete(string pathSid, ITwilioRestClient client = null)
        {
            var options = new DeleteAwsOptions(pathSid)     ;
            return Delete(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(string pathSid, ITwilioRestClient client = null)
        {
            var options = new DeleteAwsOptions(pathSid) ;
            return await DeleteAsync(options, client);
        }
        #endif






        

        private static Request BuildFetchRequest(FetchAwsOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS/{Sid}";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);


            return new Request(
                HttpMethod.Get,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        public static AwsResource Fetch(FetchAwsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<AwsResource> FetchAsync(FetchAwsOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        public static AwsResource Fetch(string pathSid, ITwilioRestClient client = null)
        {
            var options = new FetchAwsOptions(pathSid){  };
            return Fetch(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<AwsResource> FetchAsync(string pathSid, ITwilioRestClient client = null)
        {
            var options = new FetchAwsOptions(pathSid){  };
            return await FetchAsync(options, client);
        }
        #endif





        


        private static Request BuildReadRequest(ReadAwsOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS";



            return new Request(
                HttpMethod.Get,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        public static ResourceSet<AwsResource> Read(ReadAwsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildReadRequest(options, client));
            var page = Page<AwsResource>.FromJson("credentials", response.Content);
            return new ResourceSet<AwsResource>(page, options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<ResourceSet<AwsResource>> ReadAsync(ReadAwsOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildReadRequest(options, client));

            var page = Page<AwsResource>.FromJson("credentials", response.Content);
            return new ResourceSet<AwsResource>(page, options, client);
        }
        #endif

        public static ResourceSet<AwsResource> Read(
                                                     int? pageSize = null,
                                                     long? limit = null,
                                                     ITwilioRestClient client = null)
        {
            var options = new ReadAwsOptions(){ PageSize = pageSize, Limit = limit};
            return Read(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<ResourceSet<AwsResource>> ReadAsync(
                                                                                             int? pageSize = null,
                                                                                             long? limit = null,
                                                                                             ITwilioRestClient client = null)
        {
            var options = new ReadAwsOptions(){ PageSize = pageSize, Limit = limit};
            return await ReadAsync(options, client);
        }
        #endif






        
        private static Request BuildUpdateRequest(UpdateAwsOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS/{Sid}";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);


            return new Request(
                HttpMethod.Post,
                Rest.Domain.Api,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }


        public static AwsResource Update(UpdateAwsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<AwsResource> UpdateAsync(UpdateAwsOptions options,
                                                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        public static AwsResource Update(
                                          string pathSid,
                                          string testString = null,
                                          bool? testBoolean = null,
                                          ITwilioRestClient client = null)
        {
            var options = new UpdateAwsOptions(pathSid){ TestString = testString, TestBoolean = testBoolean };
            return Update(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<AwsResource> UpdateAsync(
                                                                              string pathSid,
                                                                              string testString = null,
                                                                              bool? testBoolean = null,
                                                                              ITwilioRestClient client = null)
        {
            var options = new UpdateAwsOptions(pathSid){ TestString = testString, TestBoolean = testBoolean };
            return await UpdateAsync(options, client);
        }
        #endif
    
        public static Page<AwsResource> GetPage(string targetUrl, ITwilioRestClient client)
        {
            client = client ?? TwilioClient.GetRestClient();

            var request = new Request(
                HttpMethod.Get,
                targetUrl
            );

            var response = client.Request(request);
            return Page<AwsResource>.FromJson("credentials", response.Content);
        }

        public static Page<AwsResource> NextPage(Page<AwsResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetNextPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<AwsResource>.FromJson("credentials", response.Content);
        }

        public static Page<AwsResource> PreviousPage(Page<AwsResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetPreviousPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<AwsResource>.FromJson("credentials", response.Content);
        }

        public static AwsResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<AwsResource>(json);
            }
            catch (JsonException e)
            {
                throw new ApiException(e.Message, e);
            }
        }


    
        [JsonProperty("account_sid")]
        public string AccountSid { get; private set; }

        [JsonProperty("sid")]
        public string Sid { get; private set; }

        [JsonProperty("test_string")]
        public string TestString { get; private set; }

        [JsonProperty("test_integer")]
        public int? TestInteger { get; private set; }

        [JsonProperty("test_object")]
        public PhoneNumberCapabilities TestObject { get; private set; }

        [JsonProperty("test_date_time")]
        public DateTime? TestDateTime { get; private set; }

        [JsonProperty("test_number")]
        public decimal? TestNumber { get; private set; }

        [JsonProperty("price_unit")]
        public string PriceUnit { get; private set; }

        [JsonProperty("test_number_float")]
        public float? TestNumberFloat { get; private set; }

        [JsonProperty("test_enum")]
        [JsonConverter(typeof(StringEnumConverter))]
        public AwsResource.StatusEnum TestEnum { get; private set; }

        [JsonProperty("test_array_of_integers")]
        public List<int?> TestArrayOfIntegers { get; private set; }

        [JsonProperty("test_array_of_array_of_integers")]
        public List<List<int?>> TestArrayOfArrayOfIntegers { get; private set; }

        [JsonProperty("test_array_of_objects")]
        public List<FeedbackIssue> TestArrayOfObjects { get; private set; }

        [JsonProperty("test_array_of_enum")]
        [JsonConverter(typeof(StringEnumConverter))]
        public List<AwsResource.StatusEnum> TestArrayOfEnum { get; private set; }



    private AwsResource(){

    }
    } // end of resource class
} // end of namespace

