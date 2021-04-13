# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**CreateAssistant**](DefaultApi.md#CreateAssistant) | **Post** /v1/Assistants | 
[**CreateCallFeedbackSummary**](DefaultApi.md#CreateCallFeedbackSummary) | **Post** /2010-04-01/Accounts/{AccountSid}/Calls/FeedbackSummary.json | 
[**CreateCallRecording**](DefaultApi.md#CreateCallRecording) | **Post** /2010-04-01/Accounts/{AccountSid}/Calls/{CallSid}/Recordings.json | 
[**CreateCredentialAws**](DefaultApi.md#CreateCredentialAws) | **Post** /v1/Credentials/AWS | 
[**DeleteArchivedCall**](DefaultApi.md#DeleteArchivedCall) | **Delete** /v1/Archives/{Date}/Calls/{Sid} | 
[**DeleteCredentialAws**](DefaultApi.md#DeleteCredentialAws) | **Delete** /v1/Credentials/AWS/{Sid} | 
[**DeleteIncomingPhoneNumber**](DefaultApi.md#DeleteIncomingPhoneNumber) | **Delete** /2010-04-01/Accounts/{AccountSid}/IncomingPhoneNumbers/{Sid}.json | 
[**FetchCredentialAws**](DefaultApi.md#FetchCredentialAws) | **Get** /v1/Credentials/AWS/{Sid} | 
[**FetchIncomingPhoneNumber**](DefaultApi.md#FetchIncomingPhoneNumber) | **Get** /2010-04-01/Accounts/{AccountSid}/IncomingPhoneNumbers/{Sid}.json | 
[**ListAssistant**](DefaultApi.md#ListAssistant) | **Get** /v1/Assistants | 
[**ListCallRecording**](DefaultApi.md#ListCallRecording) | **Get** /2010-04-01/Accounts/{AccountSid}/Calls/{CallSid}/Recordings.json | 
[**ListCredentialAws**](DefaultApi.md#ListCredentialAws) | **Get** /v1/Credentials/AWS | 
[**UpdateCredentialAws**](DefaultApi.md#UpdateCredentialAws) | **Post** /v1/Credentials/AWS/{Sid} | 
[**UpdateIncomingPhoneNumber**](DefaultApi.md#UpdateIncomingPhoneNumber) | **Post** /2010-04-01/Accounts/{AccountSid}/IncomingPhoneNumbers/{Sid}.json | 



## CreateAssistant

> AutopilotV1Assistant CreateAssistant(ctx, optional)



### Path Parameters

This endpoint does not need any path parameter.

### Other Parameters

Other parameters are passed through a pointer to a CreateAssistantParams struct


Name | Type | Description
------------- | ------------- | -------------
**CallbackEvents** | **string** | Reserved.
**CallbackUrl** | **string** | Reserved.
**Defaults** | [**map[string]interface{}**](map[string]interface{}.md) | A JSON object that defines the Assistant&#39;s [default tasks](https://www.twilio.com/docs/autopilot/api/assistant/defaults) for various scenarios, including initiation actions and fallback tasks.
**FriendlyName** | **string** | A descriptive string that you create to describe the new resource. It is not unique and can be up to 255 characters long.
**LogQueries** | **bool** | Whether queries should be logged and kept after training. Can be: &#x60;true&#x60; or &#x60;false&#x60; and defaults to &#x60;true&#x60;. If &#x60;true&#x60;, queries are stored for 30 days, and then deleted. If &#x60;false&#x60;, no queries are stored.
**StyleSheet** | [**map[string]interface{}**](map[string]interface{}.md) | The JSON string that defines the Assistant&#39;s [style sheet](https://www.twilio.com/docs/autopilot/api/assistant/stylesheet)
**UniqueName** | **string** | An application-defined string that uniquely identifies the new resource. It can be used as an alternative to the &#x60;sid&#x60; in the URL path to address the resource. The first 64 characters must be unique.

### Return type

[**AutopilotV1Assistant**](AutopilotV1Assistant.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## CreateCallFeedbackSummary

> ApiV2010AccountCallCallFeedbackSummary CreateCallFeedbackSummary(ctx, AccountSidoptional)



Create a FeedbackSummary resource for a call

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The unique id of the [Account](https://www.twilio.com/docs/iam/api/account) responsible for this resource.

### Other Parameters

Other parameters are passed through a pointer to a CreateCallFeedbackSummaryParams struct


Name | Type | Description
------------- | ------------- | -------------
**EndDate** | **string** | Only include feedback given on or before this date. Format is &#x60;YYYY-MM-DD&#x60; and specified in UTC.
**IncludeSubaccounts** | **bool** | Whether to also include Feedback resources from all subaccounts. &#x60;true&#x60; includes feedback from all subaccounts and &#x60;false&#x60;, the default, includes feedback from only the specified account.
**StartDate** | **string** | Only include feedback given on or after this date. Format is &#x60;YYYY-MM-DD&#x60; and specified in UTC.
**StatusCallback** | **string** | The URL that we will request when the feedback summary is complete.
**StatusCallbackMethod** | **string** | The HTTP method (&#x60;GET&#x60; or &#x60;POST&#x60;) we use to make the request to the &#x60;StatusCallback&#x60; URL.

### Return type

[**ApiV2010AccountCallCallFeedbackSummary**](ApiV2010AccountCallCallFeedbackSummary.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## CreateCallRecording

> ApiV2010AccountCallCallRecording CreateCallRecording(ctx, AccountSidCallSidoptional)



Create a recording for the call

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that will create the resource.
**CallSid** | **string** | The SID of the [Call](https://www.twilio.com/docs/voice/api/call-resource) to associate the resource with.

### Other Parameters

Other parameters are passed through a pointer to a CreateCallRecordingParams struct


Name | Type | Description
------------- | ------------- | -------------
**XTwilioWebhookEnabled** | **string** | The X-Twilio-Webhook-Enabled HTTP request header
**RecordingChannels** | **string** | The number of channels used in the recording. Can be: &#x60;mono&#x60; or &#x60;dual&#x60; and the default is &#x60;mono&#x60;. &#x60;mono&#x60; records all parties of the call into one channel. &#x60;dual&#x60; records each party of a 2-party call into separate channels.
**RecordingStatusCallback** | **string** | The URL we should call using the &#x60;recording_status_callback_method&#x60; on each recording event specified in  &#x60;recording_status_callback_event&#x60;. For more information, see [RecordingStatusCallback parameters](https://www.twilio.com/docs/voice/api/recording#recordingstatuscallback).
**RecordingStatusCallbackEvent** | **[]string** | The recording status events on which we should call the &#x60;recording_status_callback&#x60; URL. Can be: &#x60;in-progress&#x60;, &#x60;completed&#x60; and &#x60;absent&#x60; and the default is &#x60;completed&#x60;. Separate multiple event values with a space.
**RecordingStatusCallbackMethod** | **string** | The HTTP method we should use to call &#x60;recording_status_callback&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and the default is &#x60;POST&#x60;.
**RecordingTrack** | **string** | The audio track to record for the call. Can be: &#x60;inbound&#x60;, &#x60;outbound&#x60; or &#x60;both&#x60;. The default is &#x60;both&#x60;. &#x60;inbound&#x60; records the audio that is received by Twilio. &#x60;outbound&#x60; records the audio that is generated from Twilio. &#x60;both&#x60; records the audio that is received and generated by Twilio.
**Trim** | **string** | Whether to trim any leading and trailing silence in the recording. Can be: &#x60;trim-silence&#x60; or &#x60;do-not-trim&#x60; and the default is &#x60;do-not-trim&#x60;. &#x60;trim-silence&#x60; trims the silence from the beginning and end of the recording and &#x60;do-not-trim&#x60; does not.

### Return type

[**ApiV2010AccountCallCallRecording**](ApiV2010AccountCallCallRecording.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## CreateCredentialAws

> AccountsV1CredentialCredentialAws CreateCredentialAws(ctx, optional)



Create a new AWS Credential

### Path Parameters

This endpoint does not need any path parameter.

### Other Parameters

Other parameters are passed through a pointer to a CreateCredentialAwsParams struct


Name | Type | Description
------------- | ------------- | -------------
**AccountSid** | **string** | The SID of the Subaccount that this Credential should be associated with. Must be a valid Subaccount of the account issuing the request.
**Credentials** | **string** | A string that contains the AWS access credentials in the format &#x60;&lt;AWS_ACCESS_KEY_ID&gt;:&lt;AWS_SECRET_ACCESS_KEY&gt;&#x60;. For example, &#x60;AKIAIOSFODNN7EXAMPLE:wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY&#x60;
**FriendlyName** | **string** | A descriptive string that you create to describe the resource. It can be up to 64 characters long.

### Return type

[**AccountsV1CredentialCredentialAws**](AccountsV1CredentialCredentialAws.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## DeleteArchivedCall

> DeleteArchivedCall(ctx, DateSid)



Delete an Archived Call record from your account. This removes the record from your Bulk Export results, but not from API or Account Portal logs.

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**Date** | **string** | The date of the Call in UTC.
**Sid** | **string** | The Twilio-provided Call SID that uniquely identifies the Call resource to delete

### Other Parameters

Other parameters are passed through a pointer to a DeleteArchivedCallParams struct


Name | Type | Description
------------- | ------------- | -------------

### Return type

 (empty response body)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## DeleteCredentialAws

> DeleteCredentialAws(ctx, Sid)



Delete a Credential from your account

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**Sid** | **string** | The Twilio-provided string that uniquely identifies the AWS resource to delete.

### Other Parameters

Other parameters are passed through a pointer to a DeleteCredentialAwsParams struct


Name | Type | Description
------------- | ------------- | -------------

### Return type

 (empty response body)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## DeleteIncomingPhoneNumber

> DeleteIncomingPhoneNumber(ctx, AccountSidSid)



Delete a phone-numbers belonging to the account used to make the request.

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the IncomingPhoneNumber resources to delete.
**Sid** | **string** | The Twilio-provided string that uniquely identifies the IncomingPhoneNumber resource to delete.

### Other Parameters

Other parameters are passed through a pointer to a DeleteIncomingPhoneNumberParams struct


Name | Type | Description
------------- | ------------- | -------------

### Return type

 (empty response body)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## FetchCredentialAws

> AccountsV1CredentialCredentialAws FetchCredentialAws(ctx, Sid)



Fetch the AWS credentials specified by the provided Credential Sid

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**Sid** | **string** | The Twilio-provided string that uniquely identifies the AWS resource to fetch.

### Other Parameters

Other parameters are passed through a pointer to a FetchCredentialAwsParams struct


Name | Type | Description
------------- | ------------- | -------------

### Return type

[**AccountsV1CredentialCredentialAws**](AccountsV1CredentialCredentialAws.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## FetchIncomingPhoneNumber

> ApiV2010AccountIncomingPhoneNumber FetchIncomingPhoneNumber(ctx, AccountSidSid)



Fetch an incoming-phone-number belonging to the account used to make the request.

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the IncomingPhoneNumber resource to fetch.
**Sid** | **string** | The Twilio-provided string that uniquely identifies the IncomingPhoneNumber resource to fetch.

### Other Parameters

Other parameters are passed through a pointer to a FetchIncomingPhoneNumberParams struct


Name | Type | Description
------------- | ------------- | -------------

### Return type

[**ApiV2010AccountIncomingPhoneNumber**](ApiV2010AccountIncomingPhoneNumber.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## ListAssistant

> ListAssistantResponse ListAssistant(ctx, optional)



### Path Parameters

This endpoint does not need any path parameter.

### Other Parameters

Other parameters are passed through a pointer to a ListAssistantParams struct


Name | Type | Description
------------- | ------------- | -------------
**PageSize** | **int32** | How many resources to return in each list page. The default is 50, and the maximum is 1000.

### Return type

[**ListAssistantResponse**](ListAssistantResponse.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## ListCallRecording

> ListCallRecordingResponse ListCallRecording(ctx, AccountSidCallSidoptional)



Retrieve a list of recordings belonging to the call used to make the request

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the Recording resources to read.
**CallSid** | **string** | The [Call](https://www.twilio.com/docs/voice/api/call-resource) SID of the resources to read.

### Other Parameters

Other parameters are passed through a pointer to a ListCallRecordingParams struct


Name | Type | Description
------------- | ------------- | -------------
**DateCreated** | **time.Time** | The &#x60;date_created&#x60; value, specified as &#x60;YYYY-MM-DD&#x60;, of the resources to read. You can also specify inequality: &#x60;DateCreated&lt;&#x3D;YYYY-MM-DD&#x60; will return recordings generated at or before midnight on a given date, and &#x60;DateCreated&gt;&#x3D;YYYY-MM-DD&#x60; returns recordings generated at or after midnight on a date.
**DateTest** | **string** | The &#x60;date_test&#x60; value, specified as &#x60;YYYY-MM-DD&#x60;, of the resources to read.
**DateCreatedBefore** | **time.Time** | The &#x60;date_created&#x60; value, specified as &#x60;YYYY-MM-DD&#x60;, of the resources to read. You can also specify inequality: &#x60;DateCreated&lt;&#x3D;YYYY-MM-DD&#x60; will return recordings generated at or before midnight on a given date, and &#x60;DateCreated&gt;&#x3D;YYYY-MM-DD&#x60; returns recordings generated at or after midnight on a date.
**DateCreatedAfter** | **time.Time** | The &#x60;date_created&#x60; value, specified as &#x60;YYYY-MM-DD&#x60;, of the resources to read. You can also specify inequality: &#x60;DateCreated&lt;&#x3D;YYYY-MM-DD&#x60; will return recordings generated at or before midnight on a given date, and &#x60;DateCreated&gt;&#x3D;YYYY-MM-DD&#x60; returns recordings generated at or after midnight on a date.
**PageSize** | **int32** | How many resources to return in each list page. The default is 50, and the maximum is 1000.

### Return type

[**ListCallRecordingResponse**](ListCallRecordingResponse.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## ListCredentialAws

> ListCredentialAwsResponse ListCredentialAws(ctx, optional)



Retrieves a collection of AWS Credentials belonging to the account used to make the request

### Path Parameters

This endpoint does not need any path parameter.

### Other Parameters

Other parameters are passed through a pointer to a ListCredentialAwsParams struct


Name | Type | Description
------------- | ------------- | -------------
**PageSize** | **int32** | How many resources to return in each list page. The default is 50, and the maximum is 1000.

### Return type

[**ListCredentialAwsResponse**](ListCredentialAwsResponse.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## UpdateCredentialAws

> AccountsV1CredentialCredentialAws UpdateCredentialAws(ctx, Sidoptional)



Modify the properties of a given Account

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**Sid** | **string** | The Twilio-provided string that uniquely identifies the AWS resource to update.

### Other Parameters

Other parameters are passed through a pointer to a UpdateCredentialAwsParams struct


Name | Type | Description
------------- | ------------- | -------------
**FriendlyName** | **string** | A descriptive string that you create to describe the resource. It can be up to 64 characters long.

### Return type

[**AccountsV1CredentialCredentialAws**](AccountsV1CredentialCredentialAws.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)


## UpdateIncomingPhoneNumber

> ApiV2010AccountIncomingPhoneNumber UpdateIncomingPhoneNumber(ctx, AccountSidSidoptional)



Update an incoming-phone-number instance.

### Path Parameters


Name | Type | Description
------------- | ------------- | -------------
**ctx** | **context.Context** | context for authentication, logging, cancellation, deadlines, tracing, etc.
**AccountSid** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the IncomingPhoneNumber resource to update.  For more information, see [Exchanging Numbers Between Subaccounts](https://www.twilio.com/docs/iam/api/subaccounts#exchanging-numbers).
**Sid** | **string** | The Twilio-provided string that uniquely identifies the IncomingPhoneNumber resource to update.

### Other Parameters

Other parameters are passed through a pointer to a UpdateIncomingPhoneNumberParams struct


Name | Type | Description
------------- | ------------- | -------------
**AccountSid2** | **string** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the IncomingPhoneNumber resource to update.  For more information, see [Exchanging Numbers Between Subaccounts](https://www.twilio.com/docs/iam/api/subaccounts#exchanging-numbers).
**AddressSid** | **string** | The SID of the Address resource we should associate with the phone number. Some regions require addresses to meet local regulations.
**ApiVersion** | **string** | The API version to use for incoming calls made to the phone number. The default is &#x60;2010-04-01&#x60;.
**BundleSid** | **string** | The SID of the Bundle resource that you associate with the phone number. Some regions require a Bundle to meet local Regulations.
**EmergencyAddressSid** | **string** | The SID of the emergency address configuration to use for emergency calling from this phone number.
**EmergencyStatus** | **string** | The configuration status parameter that determines whether the phone number is enabled for emergency calling.
**FriendlyName** | **string** | A descriptive string that you created to describe this phone number. It can be up to 64 characters long. By default, this is a formatted version of the phone number.
**IdentitySid** | **string** | The SID of the Identity resource that we should associate with the phone number. Some regions require an identity to meet local regulations.
**SmsApplicationSid** | **string** | The SID of the application that should handle SMS messages sent to the number. If an &#x60;sms_application_sid&#x60; is present, we ignore all of the &#x60;sms_*_url&#x60; urls and use those set on the application.
**SmsFallbackMethod** | **string** | The HTTP method that we should use to call &#x60;sms_fallback_url&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and defaults to &#x60;POST&#x60;.
**SmsFallbackUrl** | **string** | The URL that we should call when an error occurs while requesting or executing the TwiML defined by &#x60;sms_url&#x60;.
**SmsMethod** | **string** | The HTTP method that we should use to call &#x60;sms_url&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and defaults to &#x60;POST&#x60;.
**SmsUrl** | **string** | The URL we should call when the phone number receives an incoming SMS message.
**StatusCallback** | **string** | The URL we should call using the &#x60;status_callback_method&#x60; to send status information to your application.
**StatusCallbackMethod** | **string** | The HTTP method we should use to call &#x60;status_callback&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and defaults to &#x60;POST&#x60;.
**TrunkSid** | **string** | The SID of the Trunk we should use to handle phone calls to the phone number. If a &#x60;trunk_sid&#x60; is present, we ignore all of the voice urls and voice applications and use only those set on the Trunk. Setting a &#x60;trunk_sid&#x60; will automatically delete your &#x60;voice_application_sid&#x60; and vice versa.
**VoiceApplicationSid** | **string** | The SID of the application we should use to handle phone calls to the phone number. If a &#x60;voice_application_sid&#x60; is present, we ignore all of the voice urls and use only those set on the application. Setting a &#x60;voice_application_sid&#x60; will automatically delete your &#x60;trunk_sid&#x60; and vice versa.
**VoiceCallerIdLookup** | **bool** | Whether to lookup the caller&#39;s name from the CNAM database and post it to your app. Can be: &#x60;true&#x60; or &#x60;false&#x60; and defaults to &#x60;false&#x60;.
**VoiceFallbackMethod** | **string** | The HTTP method that we should use to call &#x60;voice_fallback_url&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and defaults to &#x60;POST&#x60;.
**VoiceFallbackUrl** | **string** | The URL that we should call when an error occurs retrieving or executing the TwiML requested by &#x60;url&#x60;.
**VoiceMethod** | **string** | The HTTP method that we should use to call &#x60;voice_url&#x60;. Can be: &#x60;GET&#x60; or &#x60;POST&#x60; and defaults to &#x60;POST&#x60;.
**VoiceReceiveMode** | **string** | The configuration parameter for the phone number to receive incoming voice calls or faxes. Can be: &#x60;fax&#x60; or &#x60;voice&#x60; and defaults to &#x60;voice&#x60;.
**VoiceUrl** | **string** | The URL that we should call to answer a call to the phone number. The &#x60;voice_url&#x60; will not be called if a &#x60;voice_application_sid&#x60; or a &#x60;trunk_sid&#x60; is set.

### Return type

[**ApiV2010AccountIncomingPhoneNumber**](ApiV2010AccountIncomingPhoneNumber.md)

### Authorization

[accountSid_authToken](../README.md#accountSid_authToken)

### HTTP request headers

- **Content-Type**: application/x-www-form-urlencoded, 
- **Accept**: application/json, 

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints)
[[Back to Model list]](../README.md#documentation-for-models)
[[Back to README]](../README.md)

