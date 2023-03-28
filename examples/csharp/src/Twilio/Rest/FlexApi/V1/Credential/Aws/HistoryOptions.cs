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


using System;
using System.Collections.Generic;
using Twilio.Base;
using Twilio.Converters;




namespace Twilio.Rest.FlexApi.V1.Credential.Aws
{
    /// <summary> fetch </summary>
    public class FetchHistoryOptions : IOptions<HistoryResource>
    {
    
        
        public string PathSid { get; }

        ///<summary> History INTEGER ID param!!! </summary> 
        public int? PathTestInteger { get; }

        
        public Dictionary<string, object> AddOnsData { get; set; }



        /// <summary> Construct a new FetchCredentialHistoryOptions </summary>
        /// <param name="pathSid">  </param>
        /// <param name="pathTestInteger"> History INTEGER ID param!!! </param>
        public FetchHistoryOptions(string pathSid, int? pathTestInteger)
        {
            PathSid = pathSid;
            PathTestInteger = pathTestInteger;
        }

        
        /// <summary> Generate the necessary parameters </summary>
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (AddOnsData != null)
            {
                p.AddRange(PrefixedCollapsibleMap.Serialize(AddOnsData, "AddOns"));
            }
            return p;
        }
        

    }


}

