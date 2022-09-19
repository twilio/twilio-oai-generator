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


namespace Twilio.Rest.Api.V2010.Credential
{

    public class CreateNewCredentialsOptions : IOptions<NewCredentialsResource>
    {
        // How to decide which has getter and which has setter ?
        
        public string TestString { get; set; }
        public bool? TestBoolean { get; set; }
        public int? TestInteger { get; set; }
        public decimal? TestNumber { get; set; }
        public float? TestNumberFloat { get; set; }
        public double? TestNumberDouble { get; set; }
        public decimal? TestNumberInt32 { get; set; }
        public long? TestNumberInt64 { get; set; }
        public object TestObject { get; set; }
        public DateTime? TestDateTime { get; set; }
        public DateTime? TestDate { get; set; }
        public NewCredentialsResource.StatusEnum TestEnum { get; set; }
        public List<object> TestObjectArray { get; set; }
        public object TestAnyType { get; set; }
        public List<NewCredentialsResource.PermissionsEnum> Permissions { get; set; }

        public CreateNewCredentialsOptions(string testString)
        {
            TestString = testString;
            TestObjectArray = new List<object>();
            Permissions = new List<NewCredentialsResource.PermissionsEnum>();
        }

        
        public  List<KeyValuePair<string, string>> GetParams()
        {
            var p = new List<KeyValuePair<string, string>>();

            if (TestString != null)
            {
                p.Add(new KeyValuePair<string, string>("TestString", TestString));
            }
            if (TestBoolean != null)
            {
                p.Add(new KeyValuePair<string, string>("TestBoolean", TestBoolean.Value.ToString().ToLower()));
            }
            if (TestInteger != null)
            {
                p.Add(new KeyValuePair<string, string>("TestInteger", TestInteger.ToString()));
            }
            if (TestNumber != null)
            {
                p.Add(new KeyValuePair<string, string>("TestNumber", TestNumber.ToString()));
            }
            if (TestNumberFloat != null)
            {
                p.Add(new KeyValuePair<string, string>("TestNumberFloat", TestNumberFloat.ToString()));
            }
            if (TestNumberDouble != null)
            {
                p.Add(new KeyValuePair<string, string>("TestNumberDouble", TestNumberDouble.ToString()));
            }
            if (TestNumberInt32 != null)
            {
                p.Add(new KeyValuePair<string, string>("TestNumberInt32", TestNumberInt32.ToString()));
            }
            if (TestNumberInt64 != null)
            {
                p.Add(new KeyValuePair<string, string>("TestNumberInt64", TestNumberInt64.ToString()));
            }
            if (TestObject != null)
            {
                p.Add(new KeyValuePair<string, string>("TestObject", TestObject.ToString()));
            }
            if (TestDateTime != null)
            {
                p.Add(new KeyValuePair<string, string>("TestDateTime", Serializers.DateTimeIso8601(TestDateTime)));
            }
            if (TestDate != null)
            {
                p.Add(new KeyValuePair<string, string>("TestDate", TestDate.Value.ToString("yyyy-MM-dd")));
            }
            if (TestEnum != null)
            {
                p.Add(new KeyValuePair<string, string>("TestEnum", TestEnum.ToString()));
            }
            if (TestObjectArray != null)
            {
                p.AddRange(TestObjectArray.Select(TestObjectArray => new KeyValuePair<string, string>("TestObjectArray", Serializers.JsonObject(TestObjectArray))));
            }
            if (TestAnyType != null)
            {
                p.Add(new KeyValuePair<string, string>("TestAnyType", Serializers.JsonObject(TestAnyType)));
            }
            if (Permissions != null)
            {
                p.AddRange(Permissions.Select(Permissions => new KeyValuePair<string, string>("Permissions", Permissions.ToString())));
            }
            return p;
        }
        

    }
}
