using NSubstitute;
using NSubstitute.ExceptionExtensions;
using NUnit.Framework;
using System;
using System.Collections;
using System.Collections.Generic;
using Twilio.Clients;
using Twilio.Converters;
using Twilio.Exceptions;
using Twilio.Http;
using System.Linq;
using Twilio.Rest.Api.V2010;
using Twilio.Rest.Api.V2010.Credential;
using Twilio.Rest.Api.V2010.Account;
using Twilio.Rest.Api.V2010.Account.Call;
using Twilio.Base;
using Newtonsoft.Json;

namespace Twilio.Tests.Rest
{
    [TestFixture]
    public class TwilioRestTest
    {
        private const string TEST_INTEGER = "TestInteger";
        private const string ACCOUNT_SID = "AC222222222222222222222222222222";

        [Test]
        public void TestCreateRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                ""
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AccountResource.Create(client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestCreateResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.Created,
                                         "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                                     ));

            var response = AccountResource.Create(client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
        public void TestShouldSendArrayTypeParamInRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<string>()
            {
                "http://test1.com/",
                "http://test2.com"
            };
            var postParams = new List<KeyValuePair<string, string>>();
            list.ForEach(i =>
            {
                postParams.Add(new KeyValuePair<string, string>("RecordingStatusCallbackEvent", i));
            });
            postParams.Add(new KeyValuePair<string, string>("RecordingStatusCallback", "https://validurl.com"));
            var headerParams = new List<KeyValuePair<string, string>>();
            headerParams.Add(new KeyValuePair<string, string>("X-Twilio-Webhook-Enabled", "true"));

            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                postParams:postParams,
                headerParams:headerParams
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request)
                .Returns(new Response(
                    System.Net.HttpStatusCode.Created,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\", \"test_array_of_integers\":[1,2,3], \"test_number\":4.0, \"test_integer\":4,  \"test_number_float\":-103.349998, \"test_array_of_enum\":[\"absent\",\"in-progress\",\"paused\"], \"test_array_of_array_of_integers\":[[1,2],[1,4],[3,5]]}"
                ));

            var response = AccountResource.Create(xTwilioWebhookEnabled:"true",recordingStatusCallback:new Uri("https://validurl.com"),recordingStatusCallbackEvent:list,client: twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("https://validurl.com",request.PostParams.Find(i => i.Key == "RecordingStatusCallback").Value);
            Assert.AreEqual("true",request.HeaderParams.Find(i => i.Key == "X-Twilio-Webhook-Enabled").Value);
            Assert.AreEqual(list[0],request.PostParams.Find(i => i.Key == "RecordingStatusCallbackEvent").Value);
            Assert.AreEqual(4, response.TestInteger);
            Assert.AreEqual("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", response.AccountSid);
            Assert.AreEqual(4.0, response.TestNumber);
            Assert.AreEqual(-103.349998f, response.TestNumberFloat);
            Assert.AreEqual(3, response.TestArrayOfIntegers.Count);
            Assert.AreEqual(3, response.TestArrayOfEnum.Count);
            Assert.AreEqual(2, response.TestArrayOfArrayOfIntegers[1].Count);
        }

        [Test]
        public void TestFetchRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json",
                ""
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AccountResource.Fetch(client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestFetchResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                                     ));

            var response = AccountResource.Fetch("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",client: twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",response.AccountSid);
        }

        [Test]
        public void TestDeleteRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Delete,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json",
                ""
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AccountResource.Delete(client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestDeleteResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.NoContent,
                                         "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                                     ));

            var response = AccountResource.Delete("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",client: twilioRestClient);
            Assert.NotNull(response);
            Assert.True(response);
        }

        [Test]
        public void TestReadRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                ""
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AccountResource.Read(client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestReadFullResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"first_page_uri\": \"/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0\",\"end\": 0,\"previous_page_uri\": null,\"accounts\": [{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}],\"uri\": \"/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0\",\"page_size\": 50,\"start\": 0,\"next_page_uri\": null,\"page\": 0}"
                                     ));

            var response = AccountResource.Read(client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
        public void TestShouldQueryParamInRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime currentDateTime = DateTime.Now;
            string formattedDate = Serializers.DateTimeIso8601(currentDateTime);
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("DateCreated", formattedDate));
            list.Add(new KeyValuePair<string, string>("PageSize", "50"));

            var request = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list,
                headerParams: null
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    "{\"first_page_uri\": \"/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0\",\"end\": 0,\"previous_page_uri\": null,\"accounts\": [{\"auth_token\": \"auth_token\",\"date_created\": \""+formattedDate+"\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}],\"uri\": \"/2010-04-01/Accounts.json?FriendlyName=friendly_name&Status=active&PageSize=50&Page=0\",\"page_size\": 50,\"start\": 0,\"next_page_uri\": null,\"page\": 0}"
                ));
            var response = AccountResource.Read(dateCreated: currentDateTime,pageSize: 50 ,client: twilioRestClient);
            Assert.NotNull(response);
            Assert.NotNull(request.QueryParams.Find(i => i.Key == "DateCreated").Value);
            Assert.NotNull(request.QueryParams.Find(i => i.Key == "PageSize").Value);
        }

        [Test]
        public void TestShouldReadPageOfMessages()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("PageSize", "2"));

            var request1 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list
            );
            String url = "https://api.twilio.com/2010-04-01/Accounts.json";
            String nextPageURL = "https://api.twilio.com/2010-04-01/Accounts.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130";
            String responseContent1 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Ahoy\"}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + nextPageURL + "?PageSize=2" + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + url + "?PageSize=2" + "\", \"page_size\":2}}";

            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request1)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent1
                ));
            var request2 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.jsonFrom=9999999999&PageNumber=&To=4444444444&PageSize=2&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130?PageSize=2"
            );
            String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_string\":\"Matey\"}], \"meta\": {\"url\":\"" + nextPageURL + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "?PageSize=2" + "\", \"first_page_url\":\"" + nextPageURL + "?PageSize=2" + "\", \"page_size\":2}}";
            twilioRestClient.Request(request2)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent2
                ));
            var response = AccountResource.Read(pageSize: 2 ,client: twilioRestClient);
            Assert.NotNull(response);
            List<String> testStringValues = new List<String>();
            var account = response.GetEnumerator();
            using (account)
            {
                while (account.MoveNext())
                {
                    var val = account.Current;
                    Assert.NotNull(val);
                    testStringValues.Add(val.TestString);
                }
            }

            Assert.AreEqual(testStringValues[0], "Ahoy");
            Assert.AreEqual(testStringValues[1], "Matey");
        }

        [Test]
        public void TestShouldSupportNestedPropertiesResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime currentDateTime = DateTime.Now;
            DateTime testDate = new DateTime(2022, 8, 15);
            string formattedDate = testDate.ToString("yyyy-MM-dd");
            string formattedDateTime = Serializers.DateTimeIso8601(currentDateTime);
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("DateCreated<",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateCreated>",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateTest", formattedDate));
            list.Add(new KeyValuePair<string, string>("DateCreatedBefore",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateCreatedAfter",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("PageSize", "4"));

            var request1 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list
            );
            String url = "https://api.twilio.com/2010-04-01/Accounts.json";
            String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
            String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";

            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request1)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent
                ));
            var request2 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json?PageSize=5"
            );
            twilioRestClient.Request(request2)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent2
                ));
            var response = AccountResource.Read(dateCreatedBefore: currentDateTime,dateCreatedAfter: currentDateTime,dateTest: testDate,pageSize: 4 ,client: twilioRestClient);
            Assert.NotNull(response);
            var account = response.GetEnumerator();

            account.MoveNext();
            var val = account.Current;
            Assert.False(val.TestObject.Sms);
            Assert.False(val.TestObject.Voice);
            Assert.True(val.TestObject.Mms);
            Assert.True(val.TestObject.Fax);
            Assert.AreEqual(val.Sid, "123");
        }

        [Test]
        public void TestShouldSupportArrayOfObjectResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime currentDateTime = DateTime.Now;
            DateTime testDate = new DateTime(2022, 8, 15);
            string formattedDate = testDate.ToString("yyyy-MM-dd");
            string formattedDateTime = Serializers.DateTimeIso8601(currentDateTime);
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("DateCreated",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateTest", formattedDate));
            list.Add(new KeyValuePair<string, string>("PageSize", "4"));

            var request1 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list
            );
            String url = "https://api.twilio.com/2010-04-01/Accounts.json";
            String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
            String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";

            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request1)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent
                ));
            var request2 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json?PageSize=5"
            );
            twilioRestClient.Request(request2)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent2
                ));
            var response = AccountResource.Read(dateCreated: currentDateTime,dateTest: testDate,pageSize: 4 ,client: twilioRestClient);
            Assert.NotNull(response);
            var account = response.GetEnumerator();
            account.MoveNext();
            var val = account.Current;
            Assert.AreEqual(val.TestArrayOfObjects.Count,1);
            Assert.AreEqual(val.TestArrayOfObjects[0].Description,"Testing Object Array");
            Assert.AreEqual(val.TestArrayOfObjects[0].Count,123);
            Assert.AreEqual(val.Sid, "123");
        }

        [Test]
        public void TestShouldSupportDeserializationTypes()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime currentDateTime = DateTime.Now;
            DateTime testDate = new DateTime(2022, 8, 15);
            string formattedDate = testDate.ToString("yyyy-MM-dd");
            string formattedDateTime = Serializers.DateTimeIso8601(currentDateTime);
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("DateCreated",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateTest", formattedDate));
            list.Add(new KeyValuePair<string, string>("PageSize", "4"));

            var request1 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list
            );
            String url = "https://api.twilio.com/2010-04-01/Accounts.json";
            String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_date_time\":\"2021-03-23T21:43:32Z\", \"price_unit\": \"USD\" }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
            String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}],\"test_date_time\":\"2021-03-23T16:43:32.010069453-05:00\", \"price_unit\": \"USD\" }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";

            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request1)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent
                ));
            var request2 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json?PageSize=5"
            );
            twilioRestClient.Request(request2)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent2
                ));
            var response = AccountResource.Read(dateCreated: currentDateTime,dateTest: testDate,pageSize: 4 ,client: twilioRestClient);
            Assert.NotNull(response);
            var account = response.GetEnumerator();
            account.MoveNext();
            var val = account.Current;
            Assert.AreEqual(val.PriceUnit,"USD");
            Assert.AreEqual(Serializers.DateTimeIso8601(val.TestDateTime),"2021-03-23T21:43:32Z");
            Assert.AreEqual(val.Sid, "123");
        }

        [Test]
        public void TestUpdateRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("Status",null ));
            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json",
                postParams:list
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AccountResource.Update(new AccountResource.StatusEnum(),client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestUpdateResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                                     ));

            var response = AccountResource.Update(AccountResource.StatusEnum.Stopped,client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
        public void TestUpdateWithNumericStatusResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                                     ));

            var response = AccountResource.Update(AccountResource.StatusEnum.Processing,client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
        public void TestUpdateWithParameters()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("Status","completed" ));
            list.Add(new KeyValuePair<string, string>("PauseBehavior", "test"));

            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json",
                postParams:list
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\",\"test_enum\": \"completed\"}"
                ));
            var response = AccountResource.Update(AccountResource.StatusEnum.Completed,"ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX","test",twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("completed",response.TestEnum.ToString());
        }

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAccountCreateAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<string>()
            {
                "http://test1.com/",
                "http://test2.com"
            };
            var postParams = new List<KeyValuePair<string, string>>();
            list.ForEach(i =>
            {
                postParams.Add(new KeyValuePair<string, string>("RecordingStatusCallbackEvent", i));
            });
            postParams.Add(new KeyValuePair<string, string>("RecordingStatusCallback", "https://validurl.com"));
            var headerParams = new List<KeyValuePair<string, string>>();
            headerParams.Add(new KeyValuePair<string, string>("X-Twilio-Webhook-Enabled", "true"));

            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                postParams:postParams,
                headerParams:headerParams
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request)
                .Returns(new Response(
                    System.Net.HttpStatusCode.Created,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\", \"test_string\": \"example\", \"test_array_of_integers\":[1,2,3], \"test_number\":4.0,  \"test_number_float\":-103.349998, \"test_array_of_enum\":[\"absent\",\"in-progress\",\"paused\"], \"test_array_of_array_of_integers\":[[1,2],[1,4],[3,5]]}"
                ));

            var response = AccountResource.Create(xTwilioWebhookEnabled:"true",recordingStatusCallback:new Uri("https://validurl.com"),recordingStatusCallbackEvent:list,client: twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("example", response.TestString);
        }

        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAccountFetchAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                ));

            var response = AccountResource.Fetch("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",client: twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",response.AccountSid);
        }

        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAccountDeleteAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                .Returns(new Response(
                    System.Net.HttpStatusCode.NoContent,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\"}"
                ));

            var response = AccountResource.Delete("ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",client: twilioRestClient);
            Assert.NotNull(response);
            Assert.True(response);
        }

        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAccountReadAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime currentDateTime = DateTime.Now;
            DateTime testDate = new DateTime(2022, 8, 15);
            string formattedDate = testDate.ToString("yyyy-MM-dd");
            string formattedDateTime = Serializers.DateTimeIso8601(currentDateTime);
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("DateCreated",formattedDateTime ));
            list.Add(new KeyValuePair<string, string>("DateTest", formattedDate));
            list.Add(new KeyValuePair<string, string>("PageSize", "4"));

            var request1 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json",
                queryParams:list
            );
            String url = "https://api.twilio.com/2010-04-01/Accounts.json";
            String responseContent = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
            String responseContent2 = "{\"accounts\":[{\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_array_of_objects\": [{\"count\": 123, \"description\":\"Testing Object Array\"}] }], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + ""  + "\", \"previous_page_url\":\"" + url + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";

            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request1)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent
                ));
            var request2 = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts.json?PageSize=5"
            );
            twilioRestClient.Request(request2)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    responseContent2
                ));
            var response = AccountResource.Read(dateCreated: currentDateTime,dateTest: testDate,pageSize: 4 ,client: twilioRestClient);
            Assert.NotNull(response);
            var account = response.GetEnumerator();
            account.MoveNext();
            var val = account.Current;
            Assert.AreEqual(val.TestArrayOfObjects.Count,1);
            Assert.AreEqual(val.TestArrayOfObjects[0].Description,"Testing Object Array");
            Assert.AreEqual(val.TestArrayOfObjects[0].Count,123);
            Assert.AreEqual(val.Sid, "123");
        }

        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAccountUpdateAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var list = new List<KeyValuePair<string, string>>();
            list.Add(new KeyValuePair<string, string>("Status","completed" ));
            list.Add(new KeyValuePair<string, string>("PauseBehavior", "test"));

            var request = new Request(
                HttpMethod.Post,
                Twilio.Rest.Domain.Api,
                "/2010-04-01/Accounts/ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.json",
                postParams:list
            );
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(request)
                .Returns(new Response(
                    System.Net.HttpStatusCode.OK,
                    "{\"auth_token\": \"auth_token\",\"date_created\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"date_updated\": \"Thu, 30 Jul 2015 20:00:00 +0000\",\"friendly_name\": \"friendly_name\",\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"status\": \"active\",\"test_enum\": \"completed\"}"
                ));
            var response = AccountResource.Update(AccountResource.StatusEnum.Completed,"ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX","test",twilioRestClient);
            Assert.NotNull(response);
            Assert.AreEqual("completed",response.TestEnum.ToString());
        }
        #endif

        [Test]
        public void TestShouldMakeValidAPICallAWSFetch()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
                    Request request = new Request(
                            HttpMethod.Get,
                            Twilio.Rest.Domain.Api,
                            "/v1/Credentials/AWS"
                    );
                    string url = "https://api.twilio.com/v1/Credentials/AWS";
                    String testResponse = "{\"credentials\":[{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Ahoy\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}, {\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Hello\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
                    twilioRestClient.Request(request).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
                    var response = AwsResource.Read(client: twilioRestClient);
                    Assert.IsNotNull(response);
                    var awsEnumerator = response.GetEnumerator();
                    Assert.IsNotNull(awsEnumerator);
                    awsEnumerator.MoveNext();
                    var firstResult = awsEnumerator.Current;
                    Assert.IsNotNull(firstResult);
                    Assert.AreEqual("CR12345678123456781234567812345678", firstResult.Sid);
                    Assert.AreEqual("Ahoy",firstResult.TestString);
                    awsEnumerator.MoveNext();
                    var secondResult = awsEnumerator.Current;
                    Assert.IsNotNull(secondResult);
                    Assert.AreEqual("CR12345678123456781234567812345678", secondResult.Sid);
                    Assert.AreEqual("Hello",secondResult.TestString);
        }


        [Test]
        public void TestRequestForAWSFetch()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                ""
            );
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AwsResource.Fetch("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);

        }

        [Test]
        public void TestAwsResourceDeleteRequest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Delete,
                Twilio.Rest.Domain.Api,
                "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                ""
            );
            twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                AwsResource.Delete("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().Request(request);
        }

        [Test]
        public void TestAwsResourceDeleteResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.NoContent,
                                         "null"
                                     ));

            var response = AwsResource.Delete("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
        public void TestAwsResourceUpdateResponse()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.Request(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"date_created\": \"2015-07-31T04:00:00Z\",\"date_updated\": \"2015-07-31T04:00:00Z\",\"friendly_name\": \"friendly_name\",\"sid\": \"CRaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"url\": \"https://accounts.twilio.com/v1/Credentials/AWS/CRaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"}"
                                     ));

            var response = AwsResource.Update("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
            Assert.NotNull(response);
        }

        [Test]
       public void TestUpdateAwsResourceRequest()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           var request = new Request(
               HttpMethod.Post,
               Twilio.Rest.Domain.Api,
               "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
               ""
           );
           twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));
           try
           {
               AwsResource.Update("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
               Assert.Fail("Expected TwilioException to be thrown for 500");
           }
           catch (ApiException) {}
           twilioRestClient.Received().Request(request);
       }

       [Test]
       public void TestAwsResourceReadRequest()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           var request = new Request(
               HttpMethod.Get,
               Twilio.Rest.Domain.Api,
               "/v1/Credentials/AWS",
               ""
           );
           twilioRestClient.Request(request).Throws(new ApiException("Server Error, no content"));
           try
           {
               AwsResource.Read(client: twilioRestClient);
               Assert.Fail("Expected TwilioException to be thrown for 500");
           }
           catch (ApiException) {}
           twilioRestClient.Received().Request(request);
       }

       [Test]
       public void TestAwsResourceReadEmptyResponse()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
           twilioRestClient.Request(Arg.Any<Request>())
                           .Returns(new Response(
                                        System.Net.HttpStatusCode.OK,
                                        "{\"credentials\": [],\"meta\": {\"first_page_url\": \"https://accounts.twilio.com/v1/Credentials/AWS?PageSize=50&Page=0\",\"key\": \"credentials\",\"next_page_url\": null,\"page\": 0,\"page_size\": 50,\"previous_page_url\": null,\"url\": \"https://accounts.twilio.com/v1/Credentials/AWS?PageSize=50&Page=0\"}}"
                                    ));
           var response = AwsResource.Read(client: twilioRestClient);
           Assert.NotNull(response);
       }

        [Test]
        public void TestAwsResourceObjectCreationFromJsonIsNotNull()
        {
            string json = "{\"sid\": \"ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\",\"test_string\":\"AwsResourceTestString\",\"test_integer\":123}";
            var awsResource = AwsResource.FromJson(json);
            Assert.IsNotNull(awsResource);
            Assert.AreEqual("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",awsResource.Sid);
            Assert.AreEqual("AwsResourceTestString",awsResource.TestString);
            Assert.AreEqual(123,awsResource.TestInteger);
        }

        [Test]
        public void TestAwsResourcePagination()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            string firstPageURI = "/v1/Credentials/AWS";
            string secondPageURI = "/v1/Credentials/AWSN";

            string responseContentFirstPage = "{\"credentials\":[" +
                "{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Ahoy\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}" +
                "],\"meta\": {\"url\":\"" + firstPageURI + "\", \"next_page_url\":\"" + secondPageURI + "?PageSize=2" + "\", \"previous_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"first_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"page_size\":2}}";

            string responseContentSecondPage = "{\"credentials\":[" +
                "{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Matey\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}" +
                "],\"meta\": {\"url\":\"" + firstPageURI + "\", \"next_page_url\":\"" + "" + "?PageSize=2" + "\", \"previous_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"first_page_url\":\"" + firstPageURI + "?PageSize=2" + "\", \"page_size\":2}}";

            Page<AwsResource> secondPage = Page<AwsResource>.FromJson("credentials", responseContentSecondPage);
            Assert.IsNotNull(secondPage);

            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, responseContentFirstPage));
            Page<AwsResource> previousPage = AwsResource.PreviousPage(secondPage, client: twilioRestClient); //Get's First Page
            Assert.IsNotNull(previousPage);

            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, responseContentSecondPage));
            Page<AwsResource> page = AwsResource.GetPage(secondPageURI,twilioRestClient);//Get's second page
            Assert.IsNotNull(page);
            Page<AwsResource> nextPage = AwsResource.NextPage(previousPage, twilioRestClient); // Get's second page
            Assert.IsNotNull(nextPage);

            Assert.AreEqual("Matey", page.Records[0].TestString);
            Assert.AreEqual("Matey", secondPage.Records[0].TestString);
            Assert.AreEqual("Ahoy", previousPage.Records[0].TestString);
            Assert.AreEqual("Matey", nextPage.Records[0].TestString);
        }

        [Test]
        public void TestValidAPICallAwsResourceDeleteWhenResourceNotDeleted()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            string testResponse = "{\"accountSid\": \"sid\"}";
            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
            bool resource = AwsResource.Delete(ACCOUNT_SID,client:twilioRestClient);
            Assert.False(resource);
        }

        [Test]
        public void TestAwsResourceObjectCreationExceptionForBrokenJson()
        {
            string json = "{broken_json_key:value";
            Assert.Throws<ApiException>(() => AwsResource.FromJson(json));
        }

        [Test]
        public void TestAwsResourceObjectCreationValues()
        {
            String json = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                "\"test_enum\": \"paused\"}";
            String jsonDuplicate = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                 "\"test_enum\": \"paused\"}";

            AwsResource aws = AwsResource.FromJson(json);
            AwsResource awsDuplicate = AwsResource.FromJson(jsonDuplicate);

            Assert.IsNotNull(aws);
            Assert.IsNotNull(awsDuplicate);
            Assert.IsNotNull(aws.AccountSid);
            Assert.IsNotNull(aws.Sid);
            Assert.IsNotNull(aws.TestEnum);
            Assert.AreEqual(aws.AccountSid,awsDuplicate.AccountSid);
            Assert.AreEqual(aws.Sid,awsDuplicate.Sid);
            Assert.AreEqual(aws.TestInteger,awsDuplicate.TestInteger);
            Assert.AreEqual(aws.TestNumber,awsDuplicate.TestNumber);
            Assert.AreEqual(aws.TestNumberFloat,awsDuplicate.TestNumberFloat);
            Assert.AreEqual(aws.TestEnum,awsDuplicate.TestEnum);

            Assert.AreEqual("a123",aws.AccountSid);
            Assert.AreEqual("123",aws.Sid);
            Assert.AreEqual(123,aws.TestInteger);
        }

        [Test]
        public void TestAwsResourceUpdate()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            string testResponse = "{\"account_sid\":\"" + ACCOUNT_SID + "\",\"test_string\":\"hello\" }";
            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
            AwsResource aws = AwsResource.Update(ACCOUNT_SID, client: twilioRestClient);
            Assert.IsNotNull(aws);
            Assert.IsNotNull(aws.TestString);
            Assert.AreEqual("hello", aws.TestString);
        }

        [Test]
        public void TestShouldAddAccountSidIfNotPresent()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns(ACCOUNT_SID);
            Request request = new Request(
                    HttpMethod.Get,
                    Twilio.Rest.Domain.Api,
                    "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/123.json"
            );

            twilioRestClient.Request(request).Returns(new Response(System.Net.HttpStatusCode.OK, "{\"account_sid\":\"AC222222222222222222222222222222\",\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}"));
            CallResource call = CallResource.Fetch(123,client:twilioRestClient);
            Assert.IsNotNull(call);
            Assert.AreEqual("123", call.Sid);
            Assert.AreEqual(ACCOUNT_SID, call.AccountSid);
        }

        [Test]
        public void TestCallObjectCreationFromJson()
        {
            String json = "{\"test_integer\": 123}";
            CallResource call = CallResource.FromJson(json);
            Assert.IsNotNull(call);
            Assert.IsNotNull(call.TestInteger);
            Assert.AreEqual(123, call.TestInteger);
        }

        [Test]
        public void TestCallResourceObjectCreationValues()
        {
            String json = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                "\"test_enum\": \"paused\"}";
            String jsonDuplicate = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                 "\"test_enum\": \"paused\"}";

            CallResource call = CallResource.FromJson(json);
            CallResource callDuplicate = CallResource.FromJson(jsonDuplicate);

            Assert.IsNotNull(call);
            Assert.IsNotNull(callDuplicate);
            Assert.IsNotNull(call.AccountSid);
            Assert.IsNotNull(call.Sid);
            Assert.IsNotNull(call.TestEnum);
            Assert.AreEqual(call.AccountSid,callDuplicate.AccountSid);
            Assert.AreEqual(call.Sid,callDuplicate.Sid);
            Assert.AreEqual(call.TestInteger,callDuplicate.TestInteger);
            Assert.AreEqual(call.TestNumber,callDuplicate.TestNumber);
            Assert.AreEqual(call.TestNumberFloat,callDuplicate.TestNumberFloat);
            Assert.AreEqual(call.TestEnum,callDuplicate.TestEnum);

            Assert.AreEqual("a123",call.AccountSid);
            Assert.AreEqual("123",call.Sid);
            Assert.AreEqual(123,call.TestInteger);
        }

        [Test]
        public void TestFeedbackCallSummaryObjectUpdateResponseValid()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime startDate = DateTime.Parse("2009-01-27");
            DateTime endDate = DateTime.Parse("2022-01-27");

            twilioRestClient.AccountSid.Returns("sid");
            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, "{ \"account_sid\":\"AXCCCCC1234567891234567\",\"sid\":\"123\"}"));
            FeedbackCallSummaryResource feedbackSummary = FeedbackCallSummaryResource.Update(ACCOUNT_SID, endDate,startDate,client:twilioRestClient);
            Assert.IsNotNull(feedbackSummary);
        }


        [Test]
        public void TestFeedbackCallSummaryResourceObjectCreationValues()
        {
            String json = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                "\"test_enum\": \"paused\"}";
            String jsonDuplicate = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, " +
                 "\"test_enum\": \"paused\"}";
            FeedbackCallSummaryResource feedback = FeedbackCallSummaryResource.FromJson(json);
            FeedbackCallSummaryResource feedbackDuplicate = FeedbackCallSummaryResource.FromJson(jsonDuplicate);
            Assert.IsNotNull(feedback);
            Assert.IsNotNull(feedbackDuplicate);
            Assert.IsNotNull(feedback.AccountSid);
            Assert.IsNotNull(feedback.Sid);
            Assert.IsNotNull(feedback.TestEnum);
            Assert.AreEqual(feedback.AccountSid,feedbackDuplicate.AccountSid);
            Assert.AreEqual(feedback.Sid,feedbackDuplicate.Sid);
            Assert.AreEqual(feedback.TestInteger,feedbackDuplicate.TestInteger);
            Assert.AreEqual(feedback.TestNumber,feedbackDuplicate.TestNumber);
            Assert.AreEqual(feedback.TestNumberFloat,feedbackDuplicate.TestNumberFloat);
            Assert.AreEqual(feedback.TestEnum,feedbackDuplicate.TestEnum);
            Assert.AreEqual("a123",feedback.AccountSid);
            Assert.AreEqual("123",feedback.Sid);
            Assert.AreEqual(123,feedback.TestInteger);
        }

        [Test]
        public void TestFeedbackCallSummaryObjectCreationFromInvalidString() {
            string invalidJson = "invalid";
            Assert.Throws<ApiException>(() => {var feedback = FeedbackCallSummaryResource.FromJson(invalidJson);});
        }

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAWSFetchAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
                    Request request = new Request(
                            HttpMethod.Get,
                            Twilio.Rest.Domain.Api,
                            "/v1/Credentials/AWS"
                    );
                    string url = "https://api.twilio.com/v1/Credentials/AWS";
                    String testResponse = "{\"credentials\":[{\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Ahoy\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}, {\"sid\":\"CR12345678123456781234567812345678\", \"test_string\":\"Hello\", \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}], \"meta\": {\"url\":\"" + url + "\", \"next_page_url\":\"" + url + "?PageSize=5" + "\", \"previous_page_url\":\"" + url + "?PageSize=3" + "\", \"first_page_url\":\"" + url + "?PageSize=1" + "\", \"page_size\":4}}";
                    twilioRestClient.RequestAsync(request).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
                    var response = await AwsResource.ReadAsync(client: twilioRestClient);
                    Assert.IsNotNull(response);
                    var awsEnumerator = response.GetEnumerator();
                    Assert.IsNotNull(awsEnumerator);
                    awsEnumerator.MoveNext();
                    var firstResult = awsEnumerator.Current;
                    Assert.IsNotNull(firstResult);
                    Assert.AreEqual("CR12345678123456781234567812345678", firstResult.Sid);
                    Assert.AreEqual("Ahoy",firstResult.TestString);
                    awsEnumerator.MoveNext();
                    var secondResult = awsEnumerator.Current;
                    Assert.IsNotNull(secondResult);
                    Assert.AreEqual("CR12345678123456781234567812345678", secondResult.Sid);
                    Assert.AreEqual("Hello",secondResult.TestString);
        }
        #endif

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestShouldGetInValidAPICallResponseAWSFetchAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Get,
                Twilio.Rest.Domain.Api,
                "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                ""
            );
            twilioRestClient.RequestAsync(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                await AwsResource.FetchAsync("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().RequestAsync(request);

        }
        #endif

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestAwsResourceDeleteRequestAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            var request = new Request(
                HttpMethod.Delete,
                Twilio.Rest.Domain.Api,
                "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                ""
            );
            twilioRestClient.RequestAsync(request).Throws(new ApiException("Server Error, no content"));

            try
            {
                await AwsResource.DeleteAsync("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
                Assert.Fail("Expected TwilioException to be thrown for 500");
            }
            catch (ApiException) {}
            twilioRestClient.Received().RequestAsync(request);
        }
        #endif

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestAwsResourceDeleteResponseAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.RequestAsync(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.NoContent,
                                         "null"
                                     ));

            var response = await AwsResource.DeleteAsync("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
            Assert.NotNull(response);
        }
        #endif

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestAwsResourceUpdateResponseAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            twilioRestClient.RequestAsync(Arg.Any<Request>())
                            .Returns(new Response(
                                         System.Net.HttpStatusCode.OK,
                                         "{\"account_sid\": \"ACaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"date_created\": \"2015-07-31T04:00:00Z\",\"date_updated\": \"2015-07-31T04:00:00Z\",\"friendly_name\": \"friendly_name\",\"sid\": \"CRaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"url\": \"https://accounts.twilio.com/v1/Credentials/AWS/CRaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"}"
                                     ));

            var response = await AwsResource.UpdateAsync("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
            Assert.NotNull(response);
        }
        #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestUpdateAwsResourceRequestAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           var request = new Request(
               HttpMethod.Post,
               Twilio.Rest.Domain.Api,
               "/v1/Credentials/AWS/CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
               ""
           );
           twilioRestClient.RequestAsync(request).Throws(new ApiException("Server Error, no content"));
           try
           {
               await AwsResource.UpdateAsync("CRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", client: twilioRestClient);
               Assert.Fail("Expected TwilioException to be thrown for 500");
           }
           catch (ApiException) {}
           twilioRestClient.Received().RequestAsync(request);
       }
       #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestAwsResourceReadRequestAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           var request = new Request(
               HttpMethod.Get,
               Twilio.Rest.Domain.Api,
               "/v1/Credentials/AWS",
               ""
           );
           twilioRestClient.RequestAsync(request).Throws(new ApiException("Server Error, no content"));
           try
           {
               await AwsResource.ReadAsync(client: twilioRestClient);
               Assert.Fail("Expected TwilioException to be thrown for 500");
           }
           catch (ApiException) {}
           twilioRestClient.Received().RequestAsync(request);
       }
       #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestAwsResourceReadEmptyResponseAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           twilioRestClient.AccountSid.Returns("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
           twilioRestClient.RequestAsync(Arg.Any<Request>())
                           .Returns(new Response(
                                        System.Net.HttpStatusCode.OK,
                                        "{\"credentials\": [],\"meta\": {\"first_page_url\": \"https://accounts.twilio.com/v1/Credentials/AWS?PageSize=50&Page=0\",\"key\": \"credentials\",\"next_page_url\": null,\"page\": 0,\"page_size\": 50,\"previous_page_url\": null,\"url\": \"https://accounts.twilio.com/v1/Credentials/AWS?PageSize=50&Page=0\"}}"
                                    ));
           var response = await AwsResource.ReadAsync(client: twilioRestClient);
           Assert.NotNull(response);
       }
       #endif

        #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestShouldMakeValidAPICallAwsResourceDeleteAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           string testResponse = "{\"accountSid\": \"sid\"}";
           twilioRestClient.RequestAsync(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
           bool resource = await AwsResource.DeleteAsync(ACCOUNT_SID,client:twilioRestClient);
           Assert.False(resource);
       }
       #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestAwsResourceUpdateAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           string testResponse = "{\"account_sid\":\"" + ACCOUNT_SID + "\",\"test_string\":\"hello\" }";
           twilioRestClient.RequestAsync(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, testResponse));
           AwsResource aws = await AwsResource.UpdateAsync(ACCOUNT_SID, client: twilioRestClient);
           Assert.IsNotNull(aws);
           Assert.IsNotNull(aws.TestString);
           Assert.AreEqual("hello", aws.TestString);
       }
       #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestShouldAddAccountSidIfNotPresentAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           twilioRestClient.AccountSid.Returns(ACCOUNT_SID);
           Request request = new Request(
                   HttpMethod.Get,
                   Twilio.Rest.Domain.Api,
                   "/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/123.json"
           );
           twilioRestClient.RequestAsync(request).Returns(new Response(System.Net.HttpStatusCode.OK, "{\"account_sid\":\"AC222222222222222222222222222222\",\"call_sid\":\"PNXXXXY\", \"sid\":123, \"test_object\":{\"mms\": true, \"sms\":false, \"voice\": false, \"fax\":true}}"));
           CallResource call = await CallResource.FetchAsync(123,client:twilioRestClient);
           Assert.IsNotNull(call);
           Assert.AreEqual("123", call.Sid);
           Assert.AreEqual(ACCOUNT_SID, call.AccountSid);
       }
       #endif

       #if !NET35
       [Test]
       public async System.Threading.Tasks.Task TestFeedbackCallSummaryObjectUpdationResponseValidAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           DateTime startDate = DateTime.Parse("2009-01-27");
           DateTime endDate = DateTime.Parse("2022-01-27");
           twilioRestClient.AccountSid.Returns("sid");
           twilioRestClient.RequestAsync(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, "{ \"account_sid\":\"AXCCCCC1234567891234567\",\"sid\":\"123\"}"));
           FeedbackCallSummaryResource feedbackSummary = await FeedbackCallSummaryResource.UpdateAsync(ACCOUNT_SID,startDate, endDate,client:twilioRestClient);
           Assert.IsNotNull(feedbackSummary);
       }
       #endif

        [Test]
        public void TestAnyTypeParam()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            Request request = new Request(
                    HttpMethod.Post,
                    Twilio.Rest.Domain.Api,
                    "/v1/Credentials/AWS"
            );

            Dictionary<string, Object> anyMap = new Dictionary<string, Object>();
            anyMap.Add(TEST_INTEGER, 1);
            request.AddPostParam("TestAnyType", "{\"TestInteger\":1}");
            request.AddPostParam("TestNumberFloat", "1.4");
            request.AddPostParam(TEST_INTEGER, "1");
            request.AddPostParam("TestString", ACCOUNT_SID);
            twilioRestClient.Request(request).Returns(new Response(System.Net.HttpStatusCode.OK, "{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}"));

            NewCredentialsResource credentials = NewCredentialsResource.Create("AC222222222222222222222222222222", testInteger:1, testNumberFloat:1.4F,testAnyType:anyMap, client: twilioRestClient);

            Assert.IsNotNull(credentials);
            Assert.AreEqual("AC222222222222222222222222222222", request.PostParams.Single(kvp => kvp.Key == "TestString").Value);
            Assert.AreEqual("{\"TestInteger\":1}", request.PostParams.Single(kvp => kvp.Key == "TestAnyType").Value);
        }

        [Test]
        public void TestNewCredentialResourceObjectTest()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, "{}"));
            NewCredentialsResource credentials1 = NewCredentialsResource.Create(ACCOUNT_SID, testInteger: 1, testNumberFloat:1F,client:twilioRestClient);
            NewCredentialsResource credentials2 = NewCredentialsResource.Create(ACCOUNT_SID, testInteger: 1, testAnyType: new Dictionary<string, Object>(), client: twilioRestClient);
            NewCredentialsResource credentials3 = NewCredentialsResource.Create(ACCOUNT_SID, testDateTime: DateTime.Now, testAnyType: new Dictionary<string, Object>(),client: twilioRestClient);
            NewCredentialsResource credentials4 = NewCredentialsResource.Create(ACCOUNT_SID, testDateTime: DateTime.Now, testNumberFloat: 1F, client: twilioRestClient);
            Assert.IsNotNull(credentials1);
            Assert.IsNotNull(credentials2);
            Assert.IsNotNull(credentials3);
            Assert.IsNotNull(credentials4);
        }

        [Test]
        public void TestNewCredentialsGetters()
        {

            string json = "{\"account_sid\": \"a123\", \"sid\": \"123\", \"test_integer\": 123, \"test_number\": 123.1, \"test_number_float\": 123.2, \"test_enum\": \"paused\", " +
                    "\"test_enum\": \"paused\"}";
            NewCredentialsResource credentials = NewCredentialsResource.FromJson(json);
            NewCredentialsResource credentialsDuplicate = NewCredentialsResource.FromJson(json);

            Assert.IsNotNull(credentials);
            Assert.IsNotNull(credentialsDuplicate);
            Assert.AreEqual("a123", credentials.AccountSid);
            Assert.AreEqual("123", credentials.Sid);
            Assert.IsNull(credentials.TestString);
            Assert.IsNull(credentials.TestObject);
            Assert.AreEqual(123, credentials.TestInteger);
            Assert.IsNull(credentials.TestDateTime);

            Assert.IsNull(credentials.PriceUnit);

            Assert.IsNull(credentials.TestArrayOfIntegers);
            Assert.IsNull(credentials.TestArrayOfArrayOfIntegers);
            Assert.IsNull(credentials.TestArrayOfObjects);

            Assert.AreEqual(credentials.AccountSid, credentialsDuplicate.AccountSid);
            Assert.AreEqual(credentials.Sid, credentialsDuplicate.Sid);
            Assert.AreEqual(credentials.TestInteger, credentialsDuplicate.TestInteger);
            Assert.AreEqual(credentials.TestNumber, credentialsDuplicate.TestNumber);
        }

        #if !NET35
        [Test]
        public async System.Threading.Tasks.Task TestAnyTypeParamAsync()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            Request request = new Request(
                    HttpMethod.Post,
                    Twilio.Rest.Domain.Api,
                    "/v1/Credentials/AWS"
            );

            Dictionary<string, Object> anyMap = new Dictionary<string, Object>();
            anyMap.Add(TEST_INTEGER, 1);
            request.AddPostParam("TestAnyType", "{\"TestInteger\":1}");
            request.AddPostParam("TestNumberFloat", "1.4");
            request.AddPostParam(TEST_INTEGER, "1");
            request.AddPostParam("TestString", ACCOUNT_SID);
            twilioRestClient.RequestAsync(request).Returns(new Response(System.Net.HttpStatusCode.OK, "{\"account_sid\":\"AC222222222222222222222222222222\", \"sid\":\"PNXXXXY\"}"));

            NewCredentialsResource credentials = await NewCredentialsResource.CreateAsync("AC222222222222222222222222222222", testInteger:1, testNumberFloat:1.4F,testAnyType:anyMap, client: twilioRestClient);

            Assert.IsNotNull(credentials);
            Console.WriteLine(request.PostParams);

            Assert.AreEqual("AC222222222222222222222222222222", request.PostParams.Single(kvp => kvp.Key == "TestString").Value);
            Assert.AreEqual("{\"TestInteger\":1}", request.PostParams.Single(kvp => kvp.Key == "TestAnyType").Value);
        }
        #endif

        [Test]
        public void TestDeleteAwsOptionsCreation()
        {
            var deleteAwsOptions = new DeleteAwsOptions(pathSid: "PATHSID");
            Assert.IsNotNull(deleteAwsOptions);
            Assert.IsNotNull(deleteAwsOptions.PathSid);
            Assert.AreEqual("PATHSID", deleteAwsOptions.PathSid);
        }

        [Test]
        public void TestFetchAwsOptionsCreation()
        {
            var fetchAwsOptions = new FetchAwsOptions(pathSid: "PATHSID");
            Assert.IsNotNull(fetchAwsOptions);
            Assert.IsNotNull(fetchAwsOptions.PathSid);
            Assert.AreEqual("PATHSID", fetchAwsOptions.PathSid);
        }

        [Test]
        public void TestReadAwsOptionsCreation()
        {
            var readAwsOptions = new ReadAwsOptions() { PageSize = 3 };
            Assert.IsNotNull(readAwsOptions);
            Assert.IsNotNull(readAwsOptions.PageSize);
            Assert.AreEqual(3, readAwsOptions.PageSize);
            var param = readAwsOptions.GetParams();
            Assert.IsNotNull(param);
            Assert.AreEqual(3.ToString(), param.Single((x) => x.Key == "PageSize").Value);
        }

        [Test]
        public void TestUpdateAwsOptionsCreation()
        {
            string pathSid = "PathSid";
            bool testBoolean = true;
            string testString = "TestString";
            var updateAwsOptions = new UpdateAwsOptions(pathSid) { TestString = testString, TestBoolean = testBoolean };
            Assert.IsNotNull(updateAwsOptions);
            Assert.IsNotNull(updateAwsOptions.PathSid);
            Assert.AreEqual(pathSid, updateAwsOptions.PathSid);
            Assert.IsNotNull(updateAwsOptions.TestString);
            Assert.AreEqual(testString, updateAwsOptions.TestString);
            Assert.IsTrue(updateAwsOptions.TestBoolean);
            var param = updateAwsOptions.GetParams();
            Assert.IsNotNull(param);
            Assert.AreEqual(testString, param.Single((x) => x.Key == "TestString").Value);
            Assert.AreEqual(testBoolean.ToString().ToLower(), param.Single((x) => x.Key == "TestBoolean").Value);
        }

        [Test]
        public void TestCreateCallOptionsCreation()
        {
            string requiredStringProperty = "RequiredStringProperty";
            string pathAccountSid = "PathAccountSid";
            List<string> testArrayOfStrings = new List<string>() { "testElement1", "testElement2" };

            var createCall = new CreateCallOptions(requiredStringProperty: requiredStringProperty) {PathAccountSid = pathAccountSid,TestArrayOfStrings = testArrayOfStrings };
            Assert.IsNotNull(createCall);
            Assert.IsNotNull(createCall.PathAccountSid);
            Assert.AreEqual(pathAccountSid, createCall.PathAccountSid);
            Assert.IsNotNull(createCall.RequiredStringProperty);
            Assert.AreEqual(requiredStringProperty, createCall.RequiredStringProperty);
            Assert.IsNotNull(createCall.TestArrayOfStrings);
            Assert.AreEqual(testArrayOfStrings, createCall.TestArrayOfStrings);

        }

        [Test]
        public void TestCreateCallOptionsParamsCreation()
        {
            string requiredStringProperty = "RequiredStringProperty";
            string pathAccountSid = "PathAccountSid";
            List<string> testArrayOfStrings = new List<string>() { "testElement1", "testElement2" };

            var createCall = new CreateCallOptions(requiredStringProperty: requiredStringProperty) { PathAccountSid = pathAccountSid, TestArrayOfStrings = testArrayOfStrings };
            Assert.IsNotNull(createCall);

            var param = createCall.GetParams();
            Assert.IsNotNull(param);
            Assert.AreEqual(requiredStringProperty,param.Single(x => x.Key == "RequiredStringProperty").Value);
            List<string> testArrayOfStringsRetrieved = param.Where(x => x.Key == "TestArrayOfStrings").Select(x => x.Value).ToList();
            CollectionAssert.AreEquivalent(testArrayOfStrings, testArrayOfStringsRetrieved);
        }

        [Test]
        public void TestDeleteCallOptionsCreation()
        {

            string pathAccountSid = "PathAccountSid";
            int pathTestInteger = 10;

            var deleteCall = new DeleteCallOptions(pathTestInteger) { PathAccountSid = pathAccountSid};
            Assert.IsNotNull(deleteCall);
            Assert.IsNotNull(deleteCall.PathAccountSid);
            Assert.AreEqual(pathAccountSid, deleteCall.PathAccountSid);
            Assert.AreEqual(pathTestInteger, deleteCall.PathTestInteger);

        }

        [Test]
        public void TestFetchCallOptionsCreation()
        {

            string pathAccountSid = "PathAccountSid";
            int pathTestInteger = 10;

            var fetchCall = new FetchCallOptions(pathTestInteger) { PathAccountSid = pathAccountSid};
            Assert.IsNotNull(fetchCall);
            Assert.IsNotNull(fetchCall.PathAccountSid);
            Assert.AreEqual(pathAccountSid, fetchCall.PathAccountSid);
            Assert.AreEqual(pathTestInteger, fetchCall.PathTestInteger);

        }

        [Test]
        public void TestCreateFeedbackCallSummaryOptionsCreation()
        {

            string pathSid = "PathAccountSid";
            DateTime startDate = DateTime.Parse("2010-3-12");
            DateTime endDate = DateTime.Parse("2011-5-12");

            var updateCall = new UpdateFeedbackCallSummaryOptions(pathSid,endDate,startDate);
            Assert.IsNotNull(updateCall);
            Assert.IsNotNull(updateCall.PathSid);
            Assert.AreEqual(pathSid, updateCall.PathSid);
        }

       [Test]
       public void TestCreateFeedbackCallSummaryOptionsParamsCreation()
       {

           string pathSid = "PathAccountSid";
           DateTime startDate = DateTime.Parse("2010-3-12");
           DateTime endDate = DateTime.Parse("2011-5-12");

           var updateCall = new UpdateFeedbackCallSummaryOptions(pathSid,endDate,startDate);
           Assert.IsNotNull(updateCall);
           var param = updateCall.GetParams();
           Assert.IsNotNull(param);
           Assert.AreEqual(startDate.ToString("yyyy-MM-dd"), param.Single((x) => x.Key == "StartDate").Value);
           Assert.AreEqual(endDate.ToString("yyyy-MM-dd"), param.Single((x) => x.Key == "EndDate").Value);
       }

       [Test]
          public void TestCreateNewCredentialsOptionsCreation()
       {

            string testString = "testString";
            bool testBoolean = true;
            int testInteger = 5;
            decimal testNumber = Decimal.Parse("5");
            float testNumberFloat = 12.0F;
            double testNumberDouble = 19.0;
            decimal testNumberInt32 = Decimal.Parse("100");
            long testNumberInt64 = 100L;
            Dictionary<string, Object> testObjectHelper = new Dictionary<string, Object>();
            testObjectHelper.Add("test1", DateTime.Parse("1999-12-01"));
            testObjectHelper.Add("test2", DateTime.Now);
            DateTime testDateTime = DateTime.Now;
            DateTime testDate = DateTime.Parse("2011-5-12");
            NewCredentialsResource.StatusEnum testEnum = NewCredentialsResource.StatusEnum.InProgress;
            List<Dictionary<string, Object>> testObjectArrayHelper = new List<Dictionary<string, Object>>()
                {
                    new Dictionary<string, object>(){{"key11","value11"},{"key12","value12"}},
                    new Dictionary<string, object>(){{"key21","value21"},{ "key22", "value22" }}
                };

            Object testObject = (Object)testObjectHelper;
            List<Object> testObjectArray = testObjectArrayHelper.Select(x => (Object)x).ToList();
            List<NewCredentialsResource.PermissionsEnum> permissions = new List<NewCredentialsResource.PermissionsEnum>()
               {
                   NewCredentialsResource.PermissionsEnum.GetAll,
                   NewCredentialsResource.PermissionsEnum.PostAll
               };

            var credentials = new CreateNewCredentialsOptions(testString)
            {
                TestBoolean = testBoolean,
                TestInteger = testInteger,
                TestNumber = testNumber,
                TestNumberFloat = testNumberFloat,
                TestNumberDouble = testNumberDouble,
                TestNumberInt32 = testNumberInt32,
                TestNumberInt64 = testNumberInt64,
                TestDate = testDate,
                TestDateTime = testDateTime,
                TestEnum = testEnum,
                Permissions = permissions,
                TestObject = testObject,
                TestObjectArray = testObjectArray
            };
            Assert.IsNotNull(credentials);
            Assert.IsNotNull(credentials.TestString);
            Assert.IsNotNull(credentials.Permissions);
            Assert.IsNotNull(credentials.TestObject);
            Assert.IsNotNull(credentials.TestObjectArray);
            Assert.AreEqual(testString, credentials.TestString);
            Assert.IsTrue(credentials.TestBoolean);
            Assert.AreEqual(testInteger, credentials.TestInteger);
            Assert.AreEqual(testNumber, credentials.TestNumber);
            Assert.AreEqual(testNumberFloat, credentials.TestNumberFloat);
            Assert.AreEqual(testNumberDouble, credentials.TestNumberDouble);
            Assert.AreEqual(testNumberInt32, credentials.TestNumberInt32);
            Assert.AreEqual(testNumberInt64, credentials.TestNumberInt64);
            Assert.AreEqual(testDate, credentials.TestDate);
            Assert.AreEqual(testDateTime, credentials.TestDateTime);
            Assert.AreEqual(testEnum, credentials.TestEnum);
            CollectionAssert.AreEquivalent(permissions, credentials.Permissions);
            Assert.AreEqual(testObject, credentials.TestObject);
            CollectionAssert.AreEquivalent(testObjectArray, credentials.TestObjectArray);

       }

       [Test]
       public void TestCreateNewCredentialsOptionsParamsCreation()
       {

            string testString = "testString";
            bool testBoolean = true;
            int testInteger = 5;
            decimal testNumber = Decimal.Parse("5");
            float testNumberFloat = 12.0F;
            double testNumberDouble = 19.0;
            decimal testNumberInt32 = Decimal.Parse("100");
            long testNumberInt64 = 100L;
            Dictionary<string, Object> testObjectHelper = new Dictionary<string, Object>();
            testObjectHelper.Add("test1", DateTime.Parse("1999-12-01"));
            testObjectHelper.Add("test2", DateTime.Now);
            DateTime testDateTime = DateTime.Now;
            DateTime testDate = DateTime.Parse("2011-5-12");
            NewCredentialsResource.StatusEnum testEnum = NewCredentialsResource.StatusEnum.InProgress;
            List<Dictionary<string, Object>> testObjectArrayHelper = new List<Dictionary<string, Object>>()
            {
                new Dictionary<string, Object>(){{"key11","value11"},{"key12","value12"}},
                new Dictionary<string, Object>(){{"key21","value21"},{ "key22", "value22" }}
            };

            List<NewCredentialsResource.PermissionsEnum> permissions = new List<NewCredentialsResource.PermissionsEnum>()
            {
                NewCredentialsResource.PermissionsEnum.GetAll,
                NewCredentialsResource.PermissionsEnum.PostAll
            };
            Object testObject = (Object)testObjectHelper;
            List<Object> testObjectArray = testObjectArrayHelper.Select(x => (Object)x).ToList();
            var credentials = new CreateNewCredentialsOptions(testString)
            {
                TestBoolean = testBoolean,
                TestInteger = testInteger,
                TestNumber = testNumber,
                TestNumberFloat = testNumberFloat,
                TestNumberDouble = testNumberDouble,
                TestNumberInt32 = testNumberInt32,
                TestNumberInt64 = testNumberInt64,
                TestDate = testDate,
                TestDateTime = testDateTime,
                TestEnum = testEnum,
                Permissions = permissions,
                TestObject = testObject,
                TestObjectArray = testObjectArray
            };
            Assert.IsNotNull(credentials);
            var param = credentials.GetParams();
            Assert.IsNotNull(param);
            Assert.AreEqual(testString, param.Single((x) => x.Key == "TestString").Value);
            Assert.AreEqual(testBoolean.ToString().ToLower(), param.Single((x) => x.Key == "TestBoolean").Value);
            Assert.AreEqual(testInteger.ToString(), param.Single((x) => x.Key == "TestInteger").Value);
            Assert.AreEqual(testNumber.ToString(), param.Single((x) => x.Key == "TestNumber").Value);
            Assert.AreEqual(testNumberFloat.ToString(), param.Single((x) => x.Key == "TestNumberFloat").Value);
            Assert.AreEqual(testNumberDouble.ToString(), param.Single((x) => x.Key == "TestNumberDouble").Value);
            Assert.AreEqual(testNumberInt32.ToString(), param.Single((x) => x.Key == "TestNumberInt32").Value);
            Assert.AreEqual(testNumberInt64.ToString(), param.Single((x) => x.Key == "TestNumberInt64").Value);
            Assert.AreEqual(testDate.ToString("yyyy-MM-dd"), param.Single((x) => x.Key == "TestDate").Value);
            Assert.AreEqual(Serializers.DateTimeIso8601(testDateTime), param.Single((x) => x.Key == "TestDateTime").Value);
            Assert.AreEqual(testEnum.ToString(), param.Single((x) => x.Key == "TestEnum").Value);

            var testObjectData = testObject.ToString();
            var testObjectDataRetieved = param.Single((x) => x.Key == "TestObject").Value;
            Assert.AreEqual(testObjectData, testObjectDataRetieved);

            var permissionsString = permissions.Select(x => x.ToString());
            var permissionsStringRetrieved = param.Where(x => x.Key == "Permissions").Select(x => x.Value);
            CollectionAssert.AreEquivalent(permissionsString, permissionsStringRetrieved);

            var testObjectArrayData = testObjectArray.Select(TestObjectArray => new KeyValuePair<string, string>("TestObjectArray", Serializers.JsonObject(TestObjectArray)));
            var testObjectArrayDataRetrieved = param.Where(x => x.Key == "TestObjectArray").ToList();
            CollectionAssert.AreEquivalent(testObjectArrayData, testObjectArrayDataRetrieved);

       }

       [Test]
       public void TestEnumSerialization()
       {
          var testJson = "{\"sid\":\"AC123456789123456789\",\"test_enum\":\"in-progress\"}";
          var testEnum = AwsResource.StatusEnum.InProgress;
          var testEnumString = "in-progress";
          var resource = AwsResource.FromJson(testJson);
          Assert.AreEqual(testEnum,resource.TestEnum);

          var serializedObject = JsonConvert.SerializeObject(resource);
          Assert.True(serializedObject.Contains(testEnumString));
       }

       [Test]
       public void TestEnumArraySerialization()
       {
          var testJson = "{\"sid\":\"AC123456789123456789\",\"test_array_of_enum\":[\"in-progress\",\"paused\",\"stopped\"]}";
          var testArrayOfEnum = new List<AwsResource.StatusEnum>(){AwsResource.StatusEnum.InProgress,AwsResource.StatusEnum.Paused,AwsResource.StatusEnum.Stopped};
          var testArrayOfEnumString = "[\"in-progress\",\"paused\",\"stopped\"]";
          var resource = AwsResource.FromJson(testJson);
          CollectionAssert.AreEquivalent(testArrayOfEnum,resource.TestArrayOfEnum);

          var serializedObject = JsonConvert.SerializeObject(resource);
          Assert.True(serializedObject.Contains(testArrayOfEnumString));
       }
    }
}