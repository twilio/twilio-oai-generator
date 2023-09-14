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


namespace Twilio.Rest.FlexApi.V1.Credential
{
    public class NewCredentialsResource : Resource
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
                Rest.Domain.FlexApi,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> create </summary>
        /// <param name="options"> Create NewCredentials parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of NewCredentials </returns>
        public static NewCredentialsResource Create(CreateNewCredentialsOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="options"> Create NewCredentials parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of NewCredentials </returns>
        public static async System.Threading.Tasks.Task<NewCredentialsResource> CreateAsync(CreateNewCredentialsOptions options,
        ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        /// <summary> create </summary>
        /// <param name="testString">  </param>
        /// <param name="testInteger">  </param>
        /// <param name="testDate">  </param>
        /// <param name="testNumberFloat">  </param>
        /// <param name="testObject">  </param>
        /// <param name="testBoolean">  </param>
        /// <param name="testNumber">  </param>
        /// <param name="testNumberDouble">  </param>
        /// <param name="testNumberInt32">  </param>
        /// <param name="testNumberInt64">  </param>
        /// <param name="testDateTime">  </param>
        /// <param name="testEnum">  </param>
        /// <param name="testObjectArray">  </param>
        /// <param name="testAnyType">  </param>
        /// <param name="testAnyArray">  </param>
        /// <param name="permissions"> A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`. </param>
        /// <param name="someA2PThing">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of NewCredentials </returns>
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
                                          List<object> testAnyArray = null,
                                          List<NewCredentialsResource.PermissionsEnum> permissions = null,
                                          string someA2PThing = null,
                                          ITwilioRestClient client = null)
        {
            var options = new CreateNewCredentialsOptions(testString){  TestInteger = testInteger, TestDate = testDate, TestNumberFloat = testNumberFloat, TestObject = testObject, TestBoolean = testBoolean, TestNumber = testNumber, TestNumberDouble = testNumberDouble, TestNumberInt32 = testNumberInt32, TestNumberInt64 = testNumberInt64, TestDateTime = testDateTime, TestEnum = testEnum, TestObjectArray = testObjectArray, TestAnyType = testAnyType, TestAnyArray = testAnyArray, Permissions = permissions, SomeA2PThing = someA2PThing };
            return Create(options, client);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="testString">  </param>
        /// <param name="testInteger">  </param>
        /// <param name="testDate">  </param>
        /// <param name="testNumberFloat">  </param>
        /// <param name="testObject">  </param>
        /// <param name="testBoolean">  </param>
        /// <param name="testNumber">  </param>
        /// <param name="testNumberDouble">  </param>
        /// <param name="testNumberInt32">  </param>
        /// <param name="testNumberInt64">  </param>
        /// <param name="testDateTime">  </param>
        /// <param name="testEnum">  </param>
        /// <param name="testObjectArray">  </param>
        /// <param name="testAnyType">  </param>
        /// <param name="testAnyArray">  </param>
        /// <param name="permissions"> A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`. </param>
        /// <param name="someA2PThing">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of NewCredentials </returns>
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
                                                                                  List<object> testAnyArray = null,
                                                                                  List<NewCredentialsResource.PermissionsEnum> permissions = null,
                                                                                  string someA2PThing = null,
                                                                                  ITwilioRestClient client = null)
        {
        var options = new CreateNewCredentialsOptions(testString){  TestInteger = testInteger, TestDate = testDate, TestNumberFloat = testNumberFloat, TestObject = testObject, TestBoolean = testBoolean, TestNumber = testNumber, TestNumberDouble = testNumberDouble, TestNumberInt32 = testNumberInt32, TestNumberInt64 = testNumberInt64, TestDateTime = testDateTime, TestEnum = testEnum, TestObjectArray = testObjectArray, TestAnyType = testAnyType, TestAnyArray = testAnyArray, Permissions = permissions, SomeA2PThing = someA2PThing };
            return await CreateAsync(options, client);
        }
        #endif
    
        /// <summary>
        /// Converts a JSON string into a NewCredentialsResource object
        /// </summary>
        /// <param name="json"> Raw JSON string </param>
        /// <returns> NewCredentialsResource object represented by the provided JSON </returns>
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



        private NewCredentialsResource() {

        }
    }
}

