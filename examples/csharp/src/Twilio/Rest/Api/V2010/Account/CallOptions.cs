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



namespace Twilio.Rest.Api.V2010.Account
{

    /// <summary> create </summary>
    public class CreateCallOptions : IOptions<CallResource>
    {
        
        
        public string RequiredStringProperty { get; }

        ///<summary> The HTTP method that we should use to request the `TestArrayOfUri`. </summary> 
        public Twilio.Http.HttpMethod TestMethod { get; }

        
        public string PathAccountSid { get; set; }

        
        public List<string> TestArrayOfStrings { get; set; }

        
        public List<Uri> TestArrayOfUri { get; set; }


        /// <summary> Construct a new CreateCallOptions </summary>
        /// <param name="requiredStringProperty">  </param>
        /// <param name="testMethod"> The HTTP method that we should use to request the `TestArrayOfUri`. </param>
        public CreateCallOptions(string requiredStringProperty, Twilio.Http.HttpMethod testMethod)
        {
            RequiredStringProperty = requiredStringProperty;
            TestMethod = testMethod;
            TestArrayOfStrings = new List<string>();
            TestArrayOfUri = new List<Uri>();
        }

        
        /// <summary> Generate the necessary parameters </summary>
        public List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (RequiredStringProperty != null)
            {
                p.Add(new KeyValuePair<string, string>("RequiredStringProperty", RequiredStringProperty));
            }
            if (TestMethod != null)
            {
                p.Add(new KeyValuePair<string, string>("TestMethod", TestMethod.ToString()));
            }
            if (TestArrayOfStrings != null)
            {
                p.AddRange(TestArrayOfStrings.Select(TestArrayOfStrings => new KeyValuePair<string, string>("TestArrayOfStrings", TestArrayOfStrings)));
            }
            if (TestArrayOfUri != null)
            {
                p.AddRange(TestArrayOfUri.Select(TestArrayOfUri => new KeyValuePair<string, string>("TestArrayOfUri", Serializers.Url(TestArrayOfUri))));
            }
            return p;
        }

        

    }
    /// <summary> delete </summary>
    public class DeleteCallOptions : IOptions<CallResource>
    {
        
        ///<summary> INTEGER ID param!!! </summary> 
        public int? PathTestInteger { get; }

        
        public string PathAccountSid { get; set; }



        /// <summary> Construct a new DeleteCallOptions </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        public DeleteCallOptions(int? pathTestInteger)
        {
            PathTestInteger = pathTestInteger;
        }

        
        /// <summary> Generate the necessary parameters </summary>
        public List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            return p;
        }

    

    }


    /// <summary> fetch </summary>
    public class FetchCallOptions : IOptions<CallResource>
    {
    
        ///<summary> INTEGER ID param!!! </summary> 
        public int? PathTestInteger { get; }

        
        public string PathAccountSid { get; set; }



        /// <summary> Construct a new FetchCallOptions </summary>
        /// <param name="pathTestInteger"> INTEGER ID param!!! </param>
        public FetchCallOptions(int? pathTestInteger)
        {
            PathTestInteger = pathTestInteger;
        }

        
        /// <summary> Generate the necessary parameters </summary>
        public List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            return p;
        }

    

    }


}

