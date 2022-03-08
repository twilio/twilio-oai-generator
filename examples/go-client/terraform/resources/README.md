
## twilio_api_accounts_calls_recordings_v2010

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**call_sid** | string | **Required** | 
**x_twilio_webhook_enabled** | string | Optional | 
**path_account_sid** | string | Optional | 
**recording_status_callback** | string | Optional | 
**recording_status_callback_event** | list(string) | Optional | 
**sid** | int | *Computed* | INTEGER SID!!!
**status** | string | Optional | 
**pause_behavior** | string | Optional | 

## twilio_api_credentials_aws_v2010

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**credentials** | string | **Required** | A string that contains the AWS access credentials in the format &#x60;&lt;AWS_ACCESS_KEY_ID&gt;:&lt;AWS_SECRET_ACCESS_KEY&gt;&#x60;. For example, &#x60;AKIAIOSFODNN7EXAMPLE:wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY&#x60;
**account_sid** | string | Optional | The SID of the Subaccount that this Credential should be associated with. Must be a valid Subaccount of the account issuing the request.
**friendly_name** | string | Optional | A descriptive string that you create to describe the resource. It can be up to 64 characters long.
**test_number** | float | Optional | test number type transformation
**test_number_float** | float | Optional | test number/float transformation
**test_number_double** | string | Optional | test number/double transformation
**test_number_int32** | float | Optional | test number/int32 transformation
**test_number_int64** | string | Optional | test integer/int64 transformation
**test_object** | string | Optional | test object type transformation
**test_date_time** | string | Optional | test date-time type transformation
**test_date** | string | Optional | test date format transformation
**test_string_array** | list(string) | Optional | The recording status events on which we should call the &#x60;recording_status_callback&#x60; URL. Can be: &#x60;in-progress&#x60;, &#x60;completed&#x60; and &#x60;absent&#x60; and the default is &#x60;completed&#x60;. Separate multiple event values with a space.
**test_enum** | string | Optional | Test enum
**test_object_array** | list(string) | Optional | test array of object transformation
**sid** | string | *Computed* | The Twilio-provided string that uniquely identifies the AWS resource to update.

## twilio_api_accounts_messages_v2010

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**required_string_property** | string | **Required** | 
**path_account_sid** | string | Optional | 
**address_retention** | string | Optional | Determines if the address can be stored or obfuscated based on privacy settings
**int_property** | int | Optional | 
**boolean_property** | bool | Optional | Reserved
**number_property** | float | Optional | 
**array_of_strings_property** | list(string) | Optional | 
**uri_property** | string | Optional | 
**sid** | string | *Computed* | 

