/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Versionless
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
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



namespace Twilio.Rest.Versionless.DeployedDevices
{
    public class FleetResource : Resource
    {
    

        
        private static Request BuildCreateRequest(CreateFleetOptions options, ITwilioRestClient client)
        {
            
            string path = "/DeployedDevices/Fleets";


            return new Request(
                HttpMethod.Post,
                Rest.Domain.Versionless,
                path,
                postParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> create </summary>
        /// <param name="options"> Create Fleet parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Fleet </returns>
        public static FleetResource Create(CreateFleetOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="options"> Create Fleet parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Fleet </returns>
        public static async System.Threading.Tasks.Task<FleetResource> CreateAsync(CreateFleetOptions options,
        ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = await client.RequestAsync(BuildCreateRequest(options, client));
            return FromJson(response.Content);
        }
        #endif

        /// <summary> create </summary>
        /// <param name="name">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Fleet </returns>
        public static FleetResource Create(
                                          string name = null,
                                          ITwilioRestClient client = null)
        {
            var options = new CreateFleetOptions(){  Name = name };
            return Create(options, client);
        }

        #if !NET35
        /// <summary> create </summary>
        /// <param name="name">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Fleet </returns>
        public static async System.Threading.Tasks.Task<FleetResource> CreateAsync(
                                                                                  string name = null,
                                                                                  ITwilioRestClient client = null)
        {
        var options = new CreateFleetOptions(){  Name = name };
            return await CreateAsync(options, client);
        }
        #endif
        
        private static Request BuildFetchRequest(FetchFleetOptions options, ITwilioRestClient client)
        {
            
            string path = "/DeployedDevices/Fleets/{Sid}";

            string PathSid = options.PathSid;
            path = path.Replace("{"+"Sid"+"}", PathSid);

            return new Request(
                HttpMethod.Get,
                Rest.Domain.Versionless,
                path,
                queryParams: options.GetParams(),
                headerParams: null
            );
        }

        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Fleet parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> A single instance of Fleet </returns>
        public static FleetResource Fetch(FetchFleetOptions options, ITwilioRestClient client = null)
        {
            client = client ?? TwilioClient.GetRestClient();
            var response = client.Request(BuildFetchRequest(options, client));
            return FromJson(response.Content);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="options"> Fetch Fleet parameters </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Fleet </returns>
        public static async System.Threading.Tasks.Task<FleetResource> FetchAsync(FetchFleetOptions options,
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
        /// <returns> A single instance of Fleet </returns>
        public static FleetResource Fetch(
                                         string pathSid, 
                                         ITwilioRestClient client = null)
        {
            var options = new FetchFleetOptions(pathSid){  };
            return Fetch(options, client);
        }

        #if !NET35
        /// <summary> fetch </summary>
        /// <param name="pathSid">  </param>
        /// <param name="client"> Client to make requests to Twilio </param>
        /// <returns> Task that resolves to A single instance of Fleet </returns>
        public static async System.Threading.Tasks.Task<FleetResource> FetchAsync(string pathSid, ITwilioRestClient client = null)
        {
            var options = new FetchFleetOptions(pathSid){  };
            return await FetchAsync(options, client);
        }
        #endif
    
        /// <summary>
        /// Converts a JSON string into a FleetResource object
        /// </summary>
        /// <param name="json"> Raw JSON string </param>
        /// <returns> FleetResource object represented by the provided JSON </returns>
        public static FleetResource FromJson(string json)
        {
            try
            {
                return JsonConvert.DeserializeObject<FleetResource>(json);
            }
            catch (JsonException e)
            {
                throw new ApiException(e.Message, e);
            }
        }

    
        ///<summary> The name </summary> 
        [JsonProperty("name")]
        public string Name { get; private set; }

        ///<summary> A string that uniquely identifies this Fleet. </summary> 
        [JsonProperty("sid")]
        public string Sid { get; private set; }

        ///<summary> A human readable description for this Fleet. </summary> 
        [JsonProperty("friendly_name")]
        public string FriendlyName { get; private set; }



        private FleetResource() {

        }
    }
}

