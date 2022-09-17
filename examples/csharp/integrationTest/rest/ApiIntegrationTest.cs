using NUnit.Framework;
using System;
using System.Collections.Generic;
using Twilio;
using Twilio.Rest.Api.V2010;
using Twilio.Rest.Api.V2010.Account;
using Twilio.Rest.Api.V2010.Account.Call;
using Twilio.Rest.Api.V2010.Credential;

namespace Twilio.Tests.Rest
{
    [TestFixture]
    public class ApiIntegrationTest
    {
        readonly string baseURL = "http://prism_twilio:4010";
        readonly string ACCOUNT_SID = "AC12345678123456781234567812345678";

        [SetUp]
        public void SetUp()
        {
            string authToken = "CR12345678123456781234567812345678";
            TestRestClient testClient = new TestRestClient(ACCOUNT_SID, authToken,baseURL);
            TwilioClient.Init(ACCOUNT_SID, authToken);
            TwilioClient.SetRestClient(testClient);
        }

        [Test]
        public void TestEnum()
        {
            var response = AccountResource.Create("true",new Uri("https://example.com"));
            Assert.NotNull(response);
            Assert.AreEqual(response.TestEnum.ToString(), "completed");
        }
        
        [Test]
        public void TestDelete() {
            Assert.True(CallResource.Delete(123,ACCOUNT_SID));
        }
        
        [Test]
        public void TestFetchCall()
        {
            var call = CallResource.Fetch(123,ACCOUNT_SID);
            Assert.NotNull(call);
            Assert.AreEqual("CR12345678123456781234567812345678", call.Sid);
            Assert.AreEqual("Ahoy", call.TestString);
        }
        
        [Test]
        public void TestCustomTypes()
        {
            var call = CallResource.Fetch(123, "ACD93d1A6d2c5F0aE3A9C29E6cDd9BdeB9");
            Assert.NotNull(call);
            Assert.False(call.TestObject.Fax);
            Assert.False(call.TestObject.Mms);
            Assert.True(call.TestObject.Sms);
            Assert.True(call.TestObject.Voice);
        }
        
        [Test]
        public void TestPost()
        {
            var call = CallResource.Create("testString", ACCOUNT_SID);
            Assert.NotNull(call);
            Assert.AreEqual("Ahoy", call.TestString);
        }
        
        [Test]
        public void TestDateTimeQueryParam()
        {
            var response = AccountResource.Read(dateTest:DateTime.Now,dateCreatedBefore: DateTime.Now.AddMonths(-2),dateCreatedAfter: DateTime.Now.AddMonths(2),pageSize:5);
            Assert.NotNull(response);
            var enumerator = response.GetEnumerator();
            Assert.NotNull(enumerator);
            enumerator.MoveNext();
            var firstResult = enumerator.Current;
            Assert.NotNull(firstResult);
            Assert.AreEqual("Ahoy", firstResult.TestString);
            enumerator.MoveNext();
            var secondResult = enumerator.Current;
            Assert.NotNull(secondResult);
            Assert.AreEqual("Matey", secondResult.TestString);
        }
        
        [Test]
        public void TestUpdate() {
            
            var response = AccountResource.Update(AccountResource.StatusEnum.Completed,ACCOUNT_SID);
            Assert.NotNull(response);
        }

        [Test]
        public void TestArrayOfObjects()
        {
            var startDate = new DateTime(2022,6,15);
            var endDate = new DateTime(2022,9,15);
            FeedbackCallSummaryResource response = FeedbackCallSummaryResource.Create(startDate,endDate,ACCOUNT_SID);
            Assert.NotNull(response);
            Assert.AreEqual(4, response.TestArrayOfObjects[0].Count);
            Assert.AreEqual("issue description", response.TestArrayOfObjects[0].Description);
        }

        [Test]
        public void TestCreateNewcredentials()
        {
            NewCredentialsResource response = NewCredentialsResource.Create("test",testEnum:NewCredentialsResource.StatusEnum.Paused);
            Assert.NotNull(response);
        }

        [Test]
        public void TestPageLimitEqualToZero()
        {
            var response = AwsResource.Read(1,0);
            Assert.NotNull(response);
            List<string> testStringValues = new List<string>();
            foreach (AwsResource res in response)
            {
                Assert.NotNull(res);
                testStringValues.Add(res.TestString);
            }

            Assert.AreEqual(testStringValues.Count, 1);
            Assert.AreEqual(testStringValues[0], "Ahoy");
        }
    }
}