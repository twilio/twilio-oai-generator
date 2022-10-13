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


namespace Twilio.Rest.Api.V2010.Account
{
    public class CallResource : Resource
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

        
        private static Request BuildCreateRequest(CreateCallOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{AccountSid}/Calls.json";

            string PathAccountSid = options.PathAccountSid ?? client.AccountSid;
            path = path.Replace("{"+"AccountSid"+"}", PathAccountSid);


            return new Request(
                HttpMethod.Post,
                Rest.Domain.Api,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> create </summary>
        /// <param name="options"> Create Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static CallResource Create(CreateCallOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="options"> Create Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<CallResource> CreateAsync(CreateCallOptions options,
        ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        /// <summary> create </summary>
        /// <param name="requiredStringProperty">  </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="testArrayOfStrings">  </param>
        /// <param name="testArrayOfUri">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static CallResource Create(
                                          string requiredStringProperty,
                                          string pathAccountSid = null,
                                          List<string> testArrayOfStrings = null,
                                          List<Uri> testArrayOfUri = null,
                                          ITwilioRestClient client = null)
        {
            var options = new CreateCallOptions(requiredStringProperty){  PathAccountSid = pathAccountSid, TestArrayOfStrings = testArrayOfStrings, TestArrayOfUri = testArrayOfUri };
            return Create(options, client);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="requiredStringProperty">  </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="testArrayOfStrings">  </param>
        /// <param name="testArrayOfUri">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<CallResource> CreateAsync(
                                                                                  string requiredStringProperty,
                                                                                  string pathAccountSid = null,
                                                                                  List<string> testArrayOfStrings = null,
                                                                                  List<Uri> testArrayOfUri = null,
                                                                                  ITwilioRestClient client = null)
        {
        var options = new CreateCallOptions(requiredStringProperty){  PathAccountSid = pathAccountSid, TestArrayOfStrings = testArrayOfStrings, TestArrayOfUri = testArrayOfUri };
            return await CreateAsync(options, client);
        }
        #endif




        
        /// <summary> delete </summary>
        /// <param name="options"> Delete Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        private static Request BuildDeleteRequest(DeleteCallOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json";

            string PathAccountSid = options.PathAccountSid ?? client.AccountSid;
            path = path.Replace("{"+"AccountSid"+"}", PathAccountSid);
            string PathTestInteger = options.PathTestInteger.ToString();
            path = path.Replace("{"+"TestInteger"+"}", PathTestInteger);


            return new Request(
                HttpMethod.Delete,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> delete </summary>
        /// <param name="options"> Delete Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static bool Delete(DeleteCallOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }

        #if !NET35
        /// <summary> delete </summary>
        /// <param name="options"> Delete Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(DeleteCallOptions options,
                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }
        #endif

        /// <summary> delete </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static bool Delete(int? pathTestInteger, string pathAccountSid = null, ITwilioRestClient client = null)
        {
            var options = new DeleteCallOptions(pathTestInteger)      { PathAccountSid = pathAccountSid }   ;
            return Delete(options, client);
        }

        #if !NET35
        /// <summary> delete </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(int? pathTestInteger, string pathAccountSid = null, ITwilioRestClient client = null)
        {
            var options = new DeleteCallOptions(pathTestInteger)  { PathAccountSid = pathAccountSid };
            return await DeleteAsync(options, client);
        }
        #endif
        
        private static Request BuildFetchRequest(FetchCallOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json";

            string PathAccountSid = options.PathAccountSid ?? client.AccountSid;
            path = path.Replace("{"+"AccountSid"+"}", PathAccountSid);
            string PathTestInteger = options.PathTestInteger.ToString();
            path = path.Replace("{"+"TestInteger"+"}", PathTestInteger);


            return new Request(
                HttpMethod.Get,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static CallResource Fetch(FetchCallOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Call parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<CallResource> FetchAsync(FetchCallOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }
        #endif
        /// <summary> fetch </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Call </returns>
        public static CallResource Fetch(
                                         int? pathTestInteger, 
                                         string pathAccountSid = null, 
                                         ITwilioRestClient client = null)
        {
            var options = new FetchCallOptions(pathTestInteger){ PathAccountSid = pathAccountSid };
            return Fetch(options, client);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        /// <param name="pathAccountSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Call </returns>
        public static async System.Threading.Tasks.Task<CallResource> FetchAsync(int? pathTestInteger, string pathAccountSid = null, ITwilioRestClient client = null)
        {
            var options = new FetchCallOptions(pathTestInteger){ PathAccountSid = pathAccountSid };
            return await FetchAsync(options, client);
        }
        #endif
    
        /// <summary> Fetch the target page of records </summary>
        /// <param name="targetUrl"> API-generated URL for the requested results page </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The target page of records </returns>
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

        /// <summary> Fetch the next page of records </summary>
        /// <param name="page"> current page of records </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The next page of records </returns>
        public static Page<CallResource> NextPage(Page<CallResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetNextPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<CallResource>.FromJson("", response.Content);
        }

        /// <summary> Fetch the previous page of records </summary>
        /// <param name="page"> current page of records </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The previous page of records </returns>
        public static Page<CallResource> PreviousPage(Page<CallResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetPreviousPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<CallResource>.FromJson("", response.Content);
        }

        /// <summary>
        /// Converts a JSON string into a CallResource object
        /// </summary>
        /// <param name="json"> Raw JSON string </param>
        /// <returns> CallResource object represented by the provided JSON </returns>
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

    
        ///<summary> The account_sid </summary> 
        [JsonProperty("account_sid")]
        public string AccountSid { get; private set; }

        ///<summary> The sid </summary> 
        [JsonProperty("sid")]
        public string Sid { get; private set; }

        ///<summary> The test_string </summary> 
        [JsonProperty("test_string")]
        public string TestString { get; private set; }

        ///<summary> The test_integer </summary> 
        [JsonProperty("test_integer")]
        public int? TestInteger { get; private set; }

        ///<summary> The test_object </summary> 
        [JsonProperty("test_object")]
        public PhoneNumberCapabilities TestObject { get; private set; }

        ///<summary> The test_date_time </summary> 
        [JsonProperty("test_date_time")]
        public DateTime? TestDateTime { get; private set; }

        ///<summary> The test_number </summary> 
        [JsonProperty("test_number")]
        public decimal? TestNumber { get; private set; }

        ///<summary> The price_unit </summary> 
        [JsonProperty("price_unit")]
        public string PriceUnit { get; private set; }

        ///<summary> The test_number_float </summary> 
        [JsonProperty("test_number_float")]
        public float? TestNumberFloat { get; private set; }

        
        [JsonProperty("test_enum")]
        [JsonConverter(typeof(StringEnumConverter))]
        public CallResource.StatusEnum TestEnum { get; private set; }

        ///<summary> The test_array_of_integers </summary> 
        [JsonProperty("test_array_of_integers")]
        public List<int?> TestArrayOfIntegers { get; private set; }

        ///<summary> The test_array_of_array_of_integers </summary> 
        [JsonProperty("test_array_of_array_of_integers")]
        public List<List<int?>> TestArrayOfArrayOfIntegers { get; private set; }

        ///<summary> The test_array_of_objects </summary> 
        [JsonProperty("test_array_of_objects")]
        public List<FeedbackIssue> TestArrayOfObjects { get; private set; }

        ///<summary> Permissions authorized to the app </summary> 
        [JsonProperty("test_array_of_enum")]
        [JsonConverter(typeof(StringEnumConverter))]
        public List<CallResource.StatusEnum> TestArrayOfEnum { get; private set; }



        private CallResource() {

        }
    }
}

