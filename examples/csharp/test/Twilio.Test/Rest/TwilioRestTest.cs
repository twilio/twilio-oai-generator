using NSubstitute;
using NSubstitute.ExceptionExtensions;
using NUnit.Framework;
using System;
using System.Collections.Generic;
using Twilio.Clients;
using Twilio.Converters;
using Twilio.Exceptions;
using Twilio.Http;
using Twilio.Rest.Api.V2010;

namespace Twilio.Tests.Rest
{
    [TestFixture]
    public class TwilioRestTest
    {
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
    }   
}