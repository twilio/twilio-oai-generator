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



namespace Twilio.Rest.FlexApi.V1.Credential.Aws
{
    public class HistoryResource : Resource
    {
    

    

        
        private static Request BuildFetchRequest(FetchHistoryOptions options, ITwilioRestClient client)
        {
            
            string path = "/v1/Credentials/AWS/{Sid}/History";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);

            return new Request(
                HttpMethod.Get,
                Rest.Domain.FlexApi,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> fetch </summary>
        /// <param name="options"> Fetch History parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of History </returns>
        public static HistoryResource Fetch(FetchHistoryOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="options"> Fetch History parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of History </returns>
        public static async System.Threading.Tasks.Task<HistoryResource> FetchAsync(FetchHistoryOptions options,
                                                                                             ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }
        #endif
        /// <summary> fetch </summary>
        /// <param name="pathSid">  </param>
        /// <param name="addOnsData">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of History </returns>
        public static HistoryResource Fetch(
                                         string pathSid, 
                                         Dictionary<string, object> addOnsData = null, 
                                         ITwilioRestClient client = null)
        {
            var options = new FetchHistoryOptions(pathSid){ AddOnsData = addOnsData };
            return Fetch(options, client);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="pathSid">  </param>
        /// <param name="addOnsData">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of History </returns>
        public static async System.Threading.Tasks.Task<HistoryResource> FetchAsync(string pathSid, Dictionary<string, object> addOnsData = null, ITwilioRestClient client = null)
        {
            var options = new FetchHistoryOptions(pathSid){ AddOnsData = addOnsData };
            return await FetchAsync(options, client);
        }
        #endif
    
        /// <summary>
        /// Converts a JSON string into a HistoryResource object
        /// </summary>
        /// <param name="json"> Raw JSON string </param>
        /// <returns> HistoryResource object represented by the provided JSON </returns>
        public static HistoryResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<HistoryResource>(json);
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



        private HistoryResource() {

        }
    }
}

