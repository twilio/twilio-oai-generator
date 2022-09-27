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


namespace Twilio.Rest.Api.V2010.Credential
{
    public class NewCredentialsResource : Resource
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
        public sealed class PermissionsEnum : StringEnum
        {
            private PermissionsEnum(string value) : base(value) {}
            public PermissionsEnum() {}
            public static implicit operator PermissionsEnum(string value)
            {
                return new PermissionsEnum(value);
            }

            public static readonly PermissionsEnum GetAll = new PermissionsEnum("get-all");
            public static readonly PermissionsEnum PostAll = new PermissionsEnum("post-all");
        }

        
        private static Request BuildCreateRequest(CreateNewCredentialsOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS";



            return new Request(
                HttpMethod.Post,
                Rest.Domain.Api,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }

        public static NewCredentialsResource Create(CreateNewCredentialsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<NewCredentialsResource> CreateAsync(CreateNewCredentialsOptions options,
        ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif


        public static NewCredentialsResource Create(
                                          string testString,
                                          int? testInteger = null,
                                          DateTime? testDate = null,
                                          float? testNumberFloat = null,
                                          object testObject = null,
                                          bool? testBoolean = null,
                                          decimal? testNumber = null,
                                          double? testNumberDouble = null,
                                          decimal? testNumberInt32 = null,
                                          long? testNumberInt64 = null,
                                          DateTime? testDateTime = null,
                                          NewCredentialsResource.StatusEnum testEnum = null,
                                          List<object> testObjectArray = null,
                                          object testAnyType = null,
                                          List<NewCredentialsResource.PermissionsEnum> permissions = null,
                                          ITwilioRestClient client = null)
        {
            var options = new CreateNewCredentialsOptions(testString){  TestInteger = testInteger, TestDate = testDate, TestNumberFloat = testNumberFloat, TestObject = testObject, TestBoolean = testBoolean, TestNumber = testNumber, TestNumberDouble = testNumberDouble, TestNumberInt32 = testNumberInt32, TestNumberInt64 = testNumberInt64, TestDateTime = testDateTime, TestEnum = testEnum, TestObjectArray = testObjectArray, TestAnyType = testAnyType, Permissions = permissions };
            return Create(options, client);
        }

        #if !NET35
        public static async System.Threading.Tasks.Task<NewCredentialsResource> CreateAsync(
                                                                                  string testString,
                                                                                  int? testInteger = null,
                                                                                  DateTime? testDate = null,
                                                                                  float? testNumberFloat = null,
                                                                                  object testObject = null,
                                                                                  bool? testBoolean = null,
                                                                                  decimal? testNumber = null,
                                                                                  double? testNumberDouble = null,
                                                                                  decimal? testNumberInt32 = null,
                                                                                  long? testNumberInt64 = null,
                                                                                  DateTime? testDateTime = null,
                                                                                  NewCredentialsResource.StatusEnum testEnum = null,
                                                                                  List<object> testObjectArray = null,
                                                                                  object testAnyType = null,
                                                                                  List<NewCredentialsResource.PermissionsEnum> permissions = null,
                                                                                  ITwilioRestClient client = null)
        {
        var options = new CreateNewCredentialsOptions(testString){  TestInteger = testInteger, TestDate = testDate, TestNumberFloat = testNumberFloat, TestObject = testObject, TestBoolean = testBoolean, TestNumber = testNumber, TestNumberDouble = testNumberDouble, TestNumberInt32 = testNumberInt32, TestNumberInt64 = testNumberInt64, TestDateTime = testDateTime, TestEnum = testEnum, TestObjectArray = testObjectArray, TestAnyType = testAnyType, Permissions = permissions };
            return await CreateAsync(options, client);
        }
        #endif








    
        public static Page<NewCredentialsResource> GetPage(string targetUrl, ITwilioRestClient client)
        {
            client = client ?? TwilioClient.GetRestClient();

            var request = new Request(
                HttpMethod.Get,
                targetUrl
            );

            var response = client.Request(request);
            return Page<NewCredentialsResource>.FromJson("", response.Content);
        }

        public static Page<NewCredentialsResource> NextPage(Page<NewCredentialsResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetNextPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<NewCredentialsResource>.FromJson("", response.Content);
        }

        public static Page<NewCredentialsResource> PreviousPage(Page<NewCredentialsResource> page, ITwilioRestClient client)
        {
            var request = new Request(
                HttpMethod.Get,
                page.GetPreviousPageUrl(Rest.Domain.Api)
            );

            var response = client.Request(request);
            return Page<NewCredentialsResource>.FromJson("", response.Content);
        }

        public static NewCredentialsResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<NewCredentialsResource>(json);
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
        public NewCredentialsResource.StatusEnum TestEnum { get; private set; }

        [JsonProperty("test_array_of_integers")]
        public List<int?> TestArrayOfIntegers { get; private set; }

        [JsonProperty("test_array_of_array_of_integers")]
        public List<List<int?>> TestArrayOfArrayOfIntegers { get; private set; }

        [JsonProperty("test_array_of_objects")]
        public List<FeedbackIssue> TestArrayOfObjects { get; private set; }

        [JsonProperty("test_array_of_enum")]
        public List<NewCredentialsResource.StatusEnum> TestArrayOfEnum { get; private set; }



        private NewCredentialsResource() {

        }
    } // end of resource class
} // end of namespace

