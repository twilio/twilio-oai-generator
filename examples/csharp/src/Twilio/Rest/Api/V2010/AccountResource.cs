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
using Twilio.Constant;
using Twilio.Converters;
using Twilio.Exceptions;
using Twilio.Http;
using Twilio.Types;


namespace Twilio.Rest.Api.V2010
{
    public class AccountResource : Resource
    {
    

    
        [JsonConverter(typeof(StringEnumConverter))]
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
        public sealed class XTwilioWebhookEnabledEnum : StringEnum
        {
            private XTwilioWebhookEnabledEnum(string value) : base(value) {}
            public XTwilioWebhookEnabledEnum() {}
            public static implicit operator XTwilioWebhookEnabledEnum(string value)
            {
                return new XTwilioWebhookEnabledEnum(value);
            }
            public static readonly XTwilioWebhookEnabledEnum True = new XTwilioWebhookEnabledEnum("true");
            public static readonly XTwilioWebhookEnabledEnum False = new XTwilioWebhookEnabledEnum("false");

        }

        
        private static Request BuildCreateRequest(CreateAccountOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts.json";


            return new Request(
                HttpMethod.Post,
                Rest.Domain.Api,
                path,
                contentType: EnumConstants.ContentTypeEnum.FORM_URLENCODED,
                postParams: options.GetParams(),
                headerParams: options.GetHeaderParams()
            );
        }

        /// <summary> create </summary>
        /// <param name="options"> Create Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Create(CreateAccountOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="options"> Create Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<AccountResource> CreateAsync(CreateAccountOptions options,
        ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        /// <summary> create </summary>
        /// <param name="recordingStatusCallback">  </param>
        /// <param name="recordingStatusCallbackEvent">  </param>
        /// <param name="twiml">  </param>
        /// <param name="xTwilioWebhookEnabled">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Create(
                                          Uri recordingStatusCallback = null,
                                          List<string> recordingStatusCallbackEvent = null,
                                          Types.Twiml twiml = null,
                                          AccountResource.XTwilioWebhookEnabledEnum xTwilioWebhookEnabled = null,
                                          ITwilioRestClient client = null)
        {
            var options = new CreateAccountOptions(){  RecordingStatusCallback = recordingStatusCallback, RecordingStatusCallbackEvent = recordingStatusCallbackEvent, Twiml = twiml, XTwilioWebhookEnabled = xTwilioWebhookEnabled };
            return Create(options, client);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="recordingStatusCallback">  </param>
        /// <param name="recordingStatusCallbackEvent">  </param>
        /// <param name="twiml">  </param>
        /// <param name="xTwilioWebhookEnabled">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<AccountResource> CreateAsync(
                                                                                  Uri recordingStatusCallback = null,
                                                                                  List<string> recordingStatusCallbackEvent = null,
                                                                                  Types.Twiml twiml = null,
                                                                                  AccountResource.XTwilioWebhookEnabledEnum xTwilioWebhookEnabled = null,
                                                                                  ITwilioRestClient client = null)
        {
        var options = new CreateAccountOptions(){  RecordingStatusCallback = recordingStatusCallback, RecordingStatusCallbackEvent = recordingStatusCallbackEvent, Twiml = twiml, XTwilioWebhookEnabled = xTwilioWebhookEnabled };
            return await CreateAsync(options, client);
        }
        #endif
        
        /// <summary> delete </summary>
        /// <param name="options"> Delete Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        private static Request BuildDeleteRequest(DeleteAccountOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{Sid}.json";

            string PathSid = options.PathSid ?? client.AccountSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);

            return new Request(
                HttpMethod.Delete,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> delete </summary>
        /// <param name="options"> Delete Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static bool Delete(DeleteAccountOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }

        #if !NET35
        /// <summary> delete </summary>
        /// <param name="options"> Delete Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(DeleteAccountOptions options,
                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildDeleteRequest(options, client));
            return response.StatusCode == System.Net.HttpStatusCode.NoContent;
        }
        #endif

        /// <summary> delete </summary>
        /// <param name="pathSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static bool Delete(string pathSid = null, ITwilioRestClient client = null)
        {
            var options = new DeleteAccountOptions()   { PathSid = pathSid }   ;
            return Delete(options, client);
        }

        #if !NET35
        /// <summary> delete </summary>
        /// <param name="pathSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<bool> DeleteAsync(string pathSid = null, ITwilioRestClient client = null)
        {
            var options = new DeleteAccountOptions()  { PathSid = pathSid };
            return await DeleteAsync(options, client);
        }
        #endif
        
        private static Request BuildFetchRequest(FetchAccountOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{Sid}.json";

            string PathSid = options.PathSid ?? client.AccountSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);

            return new Request(
                HttpMethod.Get,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Fetch(FetchAccountOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<AccountResource> FetchAsync(FetchAccountOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }
        #endif
        /// <summary> fetch </summary>
        /// <param name="pathSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Fetch(
                                         string pathSid = null, 
                                         ITwilioRestClient client = null)
        {
            var options = new FetchAccountOptions(){ PathSid = pathSid };
            return Fetch(options, client);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="pathSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<AccountResource> FetchAsync(string pathSid = null, ITwilioRestClient client = null)
        {
            var options = new FetchAccountOptions(){ PathSid = pathSid };
            return await FetchAsync(options, client);
        }
        #endif
        
        private static Request BuildReadRequest(ReadAccountOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts.json";


            return new Request(
                HttpMethod.Get,
                Rest.Domain.Api,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }
        /// <summary> This operation's summary has a special character </summary>
        /// <param name="options"> Read Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static ResourceSet<AccountResource> Read(ReadAccountOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildReadRequest(options, client));
            var page = Page<AccountResource>.FromJson("accounts", response.Content);
            return new ResourceSet<AccountResource>(page, options, client);
        }

        #if !NET35
        /// <summary> This operation's summary has a special character </summary>
        /// <param name="options"> Read Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<ResourceSet<AccountResource>> ReadAsync(ReadAccountOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildReadRequest(options, client));

            var page = Page<AccountResource>.FromJson("accounts", response.Content);
            return new ResourceSet<AccountResource>(page, options, client);
        }
        #endif
        /// <summary> This operation's summary has a special character </summary>
        /// <param name="dateCreated">  </param>
        /// <param name="dateTest">  </param>
        /// <param name="dateCreatedBefore">  </param>
        /// <param name="dateCreatedAfter">  </param>
        /// <param name="pageSize">  </param>
        /// <param name="limit"> Record limit </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static ResourceSet<AccountResource> Read(
                                                     DateTime? dateCreated = null,
                                                     DateTime? dateTest = null,
                                                     DateTime? dateCreatedBefore = null,
                                                     DateTime? dateCreatedAfter = null,
                                                     int? pageSize = null,
                                                     long? limit = null,
                                                     ITwilioRestClient client = null)
        {
            var options = new ReadAccountOptions(){ DateCreated = dateCreated, DateTest = dateTest, DateCreatedBefore = dateCreatedBefore, DateCreatedAfter = dateCreatedAfter, PageSize = pageSize, Limit = limit};
            return Read(options, client);
        }

        #if !NET35
        /// <summary> This operation's summary has a special character </summary>
        /// <param name="dateCreated">  </param>
        /// <param name="dateTest">  </param>
        /// <param name="dateCreatedBefore">  </param>
        /// <param name="dateCreatedAfter">  </param>
        /// <param name="pageSize">  </param>
        /// <param name="limit"> Record limit </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<ResourceSet<AccountResource>> ReadAsync(
                                                                                             DateTime? dateCreated = null,
                                                                                             DateTime? dateTest = null,
                                                                                             DateTime? dateCreatedBefore = null,
                                                                                             DateTime? dateCreatedAfter = null,
                                                                                             int? pageSize = null,
                                                                                             long? limit = null,
                                                                                             ITwilioRestClient client = null)
        {
            var options = new ReadAccountOptions(){ DateCreated = dateCreated, DateTest = dateTest, DateCreatedBefore = dateCreatedBefore, DateCreatedAfter = dateCreatedAfter, PageSize = pageSize, Limit = limit};
            return await ReadAsync(options, client);
        }
        #endif

        
        /// <summary> Fetch the target page of records </summary>
        /// <param name="targetUrl"> API-generated URL for the requested results page </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The target page of records </returns>
        public static Page<AccountResource> GetPage(string targetUrl, ITwilioRestClient client)
        {
            client = client ?? TwilioClient.GetRestClient();

            var request = new Request(
                HttpMethod.Get,
                targetUrl
            );

            var response = client.Request(request);
            return Page<AccountResource>.FromJson("accounts", response.Content);
        }

        /// <summary> Fetch the next page of records </summary>
        /// <param name="page"> current page of records </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The next page of records </returns>
        public static Page<AccountResource> NextPage(Page<AccountResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetNextPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<AccountResource>.FromJson("accounts", response.Content);
        }

        /// <summary> Fetch the previous page of records </summary>
        /// <param name="page"> current page of records </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> The previous page of records </returns>
        public static Page<AccountResource> PreviousPage(Page<AccountResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetPreviousPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<AccountResource>.FromJson("accounts", response.Content);
        }

        
        private static Request BuildUpdateRequest(UpdateAccountOptions options, ITwilioRestClient client)
        {
            
            string path = "/2010-04-01/Accounts/{Sid}.json";

            string PathSid = options.PathSid ?? client.AccountSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);

            return new Request(
                HttpMethod.Post,
                Rest.Domain.Api,
                path,
                contentType: EnumConstants.ContentTypeEnum.FORM_URLENCODED,
                postParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> update </summary>
        /// <param name="options"> Update Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Update(UpdateAccountOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }

        /// <summary> update </summary>
        /// <param name="options"> Update Account parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        #if !NET35
        public static async System.Threading.Tasks.Task<AccountResource> UpdateAsync(UpdateAccountOptions options,
                                                                                                          ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildUpdateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        /// <summary> update </summary>
        /// <param name="status">  </param>
        /// <param name="pathSid">  </param>
        /// <param name="pauseBehavior">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Account </returns>
        public static AccountResource Update(
                                          AccountResource.StatusEnum status,
                                          string pathSid = null,
                                          string pauseBehavior = null,
                                          ITwilioRestClient client = null)
        {
            var options = new UpdateAccountOptions(status){ PathSid = pathSid, PauseBehavior = pauseBehavior };
            return Update(options, client);
        }

        #if !NET35
        /// <summary> update </summary>
        /// <param name="status">  </param>
        /// <param name="pathSid">  </param>
        /// <param name="pauseBehavior">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Account </returns>
        public static async System.Threading.Tasks.Task<AccountResource> UpdateAsync(
                                                                              AccountResource.StatusEnum status,
                                                                              string pathSid = null,
                                                                              string pauseBehavior = null,
                                                                              ITwilioRestClient client = null)
        {
            var options = new UpdateAccountOptions(status){ PathSid = pathSid, PauseBehavior = pauseBehavior };
            return await UpdateAsync(options, client);
        }
        #endif
    
        /// <summary>
        /// Converts a JSON string into a AccountResource object
        /// </summary>
        /// <param name="json"> Raw JSON string </param>
        /// <returns> AccountResource object represented by the provided JSON </returns>
        public static AccountResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<AccountResource>(json);
            }
            catch (JsonException e)
            {
                throw new ApiException(e.Message, e);
            }
        }
        /// <summary>
    /// Converts an object into a json string
    /// </summary>
    /// <param name="model"> C# model </param>
    /// <returns> JSON string </returns>
    public static string ToJson(object model)
    {
        try
        {
            return JsonConvert.SerializeObject(model);
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

        ///<summary> The from </summary> 
        [JsonProperty("from")]
        [JsonConverter(typeof(PhoneNumberConverter))]
        public Types.PhoneNumber From { get; private set; }

        ///<summary> The price_unit </summary> 
        [JsonProperty("price_unit")]
        public string PriceUnit { get; private set; }

        ///<summary> The test_number_float </summary> 
        [JsonProperty("test_number_float")]
        public float? TestNumberFloat { get; private set; }

        ///<summary> The test_number_decimal </summary> 
        [JsonProperty("test_number_decimal")]
        public decimal? TestNumberDecimal { get; private set; }

        
        [JsonProperty("test_enum")]
        public AccountResource.StatusEnum TestEnum { get; private set; }

        ///<summary> A2P Messaging Profile Bundle BundleSid </summary> 
        [JsonProperty("a2p_profile_bundle_sid")]
        public string A2PProfileBundleSid { get; private set; }

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
        public List<AccountResource.StatusEnum> TestArrayOfEnum { get; private set; }



        private AccountResource() {

        }
    }
}

