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
using System.Linq;
using Twilio.Types;


namespace Twilio.Rest.Api.V2010
{

    /// <summary> create </summary>
    public class CreateAccountOptions : IOptions<AccountResource>
    {
        
        
        public AccountResource.XTwilioWebhookEnabledEnum XTwilioWebhookEnabled { get; set; }

        
        public Uri RecordingStatusCallback { get; set; }

        
        public List<string> RecordingStatusCallbackEvent { get; set; }

        
        public Types.Twiml Twiml { get; set; }



        
        /// <summary> Generate the necessary parameters </summary>
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (RecordingStatusCallback != null)
            {
                p.Add(new KeyValuePair<string, string>("RecordingStatusCallback", Serializers.Url(RecordingStatusCallback)));
            }
            if (RecordingStatusCallbackEvent != null)
            {
                p.AddRange(RecordingStatusCallbackEvent.Select(RecordingStatusCallbackEvent => new KeyValuePair<string, string>("RecordingStatusCallbackEvent", RecordingStatusCallbackEvent)));
            }
            if (Twiml != null)
            {
                p.Add(new KeyValuePair<string, string>("Twiml", Twiml.ToString()));
            }
            return p;
        }
        
    /// <summary> Generate the necessary header parameters </summary>
    public List<KeyValuePair<string, string>> GetHeaderParams()
    {
        var p = new List<KeyValuePair<string, string>>();
        if (XTwilioWebhookEnabled != null)
        {
            p.Add(new KeyValuePair<string, string>("X-Twilio-Webhook-Enabled", XTwilioWebhookEnabled.ToString()));
        }
        return p;
    }

    }
    /// <summary> delete </summary>
    public class DeleteAccountOptions : IOptions<AccountResource>
    {
        
        
        public string PathSid { get; set; }




        
        /// <summary> Generate the necessary parameters </summary>
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            return p;
        }
        

    }


    /// <summary> fetch </summary>
    public class FetchAccountOptions : IOptions<AccountResource>
    {
    
        
        public string PathSid { get; set; }




        
        /// <summary> Generate the necessary parameters </summary>
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            return p;
        }
        

    }


    /// <summary> This operation's summary has a special character </summary>
    public class ReadAccountOptions : ReadOptions<AccountResource>
    {
    
        
        public DateTime? DateCreated { get; set; }

        
        public DateTime? DateTest { get; set; }

        
        public DateTime? DateCreatedBefore { get; set; }

        
        public DateTime? DateCreatedAfter { get; set; }

        ///<summary> The `date_updated` value, specified as `YYYY-MM-DD`, of the resources to read. To read conferences that were last updated on or before midnight on a date, use `<=YYYY-MM-DD`, and to specify conferences that were last updated on or after midnight on a given date, use  `>=YYYY-MM-DD`. </summary> 
        public DateTime? DateUpdated { get; set; }

        ///<summary> The `date_updated` value, specified as `YYYY-MM-DD`, of the resources to read. To read conferences that were last updated on or before midnight on a date, use `<=YYYY-MM-DD`, and to specify conferences that were last updated on or after midnight on a given date, use  `>=YYYY-MM-DD`. </summary> 
        public DateTime? DateUpdatedBefore { get; set; }

        ///<summary> The `date_updated` value, specified as `YYYY-MM-DD`, of the resources to read. To read conferences that were last updated on or before midnight on a date, use `<=YYYY-MM-DD`, and to specify conferences that were last updated on or after midnight on a given date, use  `>=YYYY-MM-DD`. </summary> 
        public DateTime? DateUpdatedAfter { get; set; }




        
        /// <summary> Generate the necessary parameters </summary>
        public  override List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (DateCreated != null)
            {
                p.Add(new KeyValuePair<string, string>("DateCreated", Serializers.DateTimeIso8601(DateCreated)));
            }
            else
            {
                if (DateCreatedBefore != null)
                {
                    p.Add(new KeyValuePair<string, string>("DateCreated<", Serializers.DateTimeIso8601(DateCreatedBefore)));
                }
                if (DateCreatedAfter != null)
                {
                    p.Add(new KeyValuePair<string, string>("DateCreated>", Serializers.DateTimeIso8601(DateCreatedAfter)));
                }
            }
            if (DateTest != null)
            {
                p.Add(new KeyValuePair<string, string>("Date.Test", DateTest.Value.ToString("yyyy-MM-dd")));
            }
            if (DateUpdated != null)
            {
                p.Add(new KeyValuePair<string, string>("DateUpdated", DateUpdated.Value.ToString("yyyy-MM-dd")));
            }
            else
            {
                if (DateUpdatedBefore != null)
                {
                    p.Add(new KeyValuePair<string, string>("DateUpdated<", DateUpdatedBefore.Value.ToString("yyyy-MM-dd")));
                }
                if (DateUpdatedAfter != null)
                {
                    p.Add(new KeyValuePair<string, string>("DateUpdated>", DateUpdatedAfter.Value.ToString("yyyy-MM-dd")));
                }
            }
            if (PageSize != null)
            {
                p.Add(new KeyValuePair<string, string>("PageSize", PageSize.ToString()));
            }
            return p;
        }
        

    }

    /// <summary> update </summary>
    public class UpdateAccountOptions : IOptions<AccountResource>
    {
    
        
        public AccountResource.StatusEnum Status { get; }

        
        public string PathSid { get; set; }

        
        public string PauseBehavior { get; set; }



        /// <summary> Construct a new UpdateAccountOptions </summary>
        /// <param name="status">  </param>
        public UpdateAccountOptions(AccountResource.StatusEnum status)
        {
            Status = status;
        }

        
        /// <summary> Generate the necessary parameters </summary>
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (Status != null)
            {
                p.Add(new KeyValuePair<string, string>("Status", Status.ToString()));
            }
            if (PauseBehavior != null)
            {
                p.Add(new KeyValuePair<string, string>("PauseBehavior", PauseBehavior));
            }
            return p;
        }
        

    }


}

