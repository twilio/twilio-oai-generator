using NSubstitute;
using NSubstitute.ExceptionExtensions;
using NUnit.Framework;
using System;
using System.Collections.Generic;
using Twilio.Clients;
using Twilio.Converters;
using Twilio.Exceptions;
using Twilio.Http;
using System.Collections;
using System.Linq;
using Twilio.Rest.Api.V2010;
using Twilio.Rest.Api.V2010.Credential;
using Twilio.Base;
using Twilio.Rest.Api.V2010.Account;
using Twilio.Rest.Api.V2010.Account.Call;
using Twilio.Converters;
using Newtonsoft.Json;

namespace Twilio.Test.Rest
{
    [TestFixture]
    public class TwilioRestTest
    {
        private const string TEST_INTEGER = "TestInteger";
        private const string ACCOUNT_SID = "AC222222222222222222222222222222";

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
        public void TestFeedbackCallSummaryObjectCreationResponseValid()
        {
            var twilioRestClient = Substitute.For<ITwilioRestClient>();
            DateTime startDate = DateTime.Parse("2009-01-27");
            DateTime endDate = DateTime.Parse("2022-01-27");

            twilioRestClient.AccountSid.Returns("sid");
            twilioRestClient.Request(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, "{ \"account_sid\":\"AXCCCCC1234567891234567\",\"sid\":\"123\"}"));
            FeedbackCallSummaryResource feedbackSummary = FeedbackCallSummaryResource.Create(startDate, endDate,pathAccountSid:ACCOUNT_SID,client:twilioRestClient);
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
       public async System.Threading.Tasks.Task TestFeedbackCallSummaryObjectCreationResponseValidAsync()
       {
           var twilioRestClient = Substitute.For<ITwilioRestClient>();
           DateTime startDate = DateTime.Parse("2009-01-27");
           DateTime endDate = DateTime.Parse("2022-01-27");
           twilioRestClient.AccountSid.Returns("sid");
           twilioRestClient.RequestAsync(Arg.Any<Request>()).Returns(new Response(System.Net.HttpStatusCode.OK, "{ \"account_sid\":\"AXCCCCC1234567891234567\",\"sid\":\"123\"}"));
           FeedbackCallSummaryResource feedbackSummary = await FeedbackCallSummaryResource.CreateAsync(startDate, endDate,pathAccountSid:ACCOUNT_SID,client:twilioRestClient);
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

            string pathAccountSid = "PathAccountSid";
            DateTime startDate = DateTime.Parse("2010-3-12");
            DateTime endDate = DateTime.Parse("2011-5-12");

            var createCall = new CreateFeedbackCallSummaryOptions(endDate,startDate) { PathAccountSid = pathAccountSid};
            Assert.IsNotNull(createCall);
            Assert.IsNotNull(createCall.PathAccountSid);
            Assert.AreEqual(pathAccountSid, createCall.PathAccountSid);
        }

       [Test]
       public void TestCreateFeedbackCallSummaryOptionsParamsCreation()
       {

           string pathAccountSid = "PathAccountSid";
           DateTime startDate = DateTime.Parse("2010-3-12");
           DateTime endDate = DateTime.Parse("2011-5-12");

           var createCall = new CreateFeedbackCallSummaryOptions(endDate,startDate) { PathAccountSid = pathAccountSid};
           Assert.IsNotNull(createCall);
           var param = createCall.GetParams();
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
    }
}