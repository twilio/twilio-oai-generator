
## twilio_api_accounts

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**x_twilio_webhook_enabled** | string | Optional | 
**recording_status_callback** | string | Optional | 
**recording_status_callback_event** | list(string) | Optional | 
**sid** | string | *Computed* | 
**status** | string | Optional | 
**pause_behavior** | string | Optional | 

## twilio_api_accounts_calls

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**required_string_property** | string | **Required** | 
**path_account_sid** | string | Optional | 
**test_array_of_strings** | list(string) | Optional | 
**test_array_of_uri** | list(string) | Optional | 
**test_integer** | int | *Computed* | INTEGER ID param!!!

## twilio_api_credentials_aws

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**test_string** | string | **Required** | 
**test_boolean** | bool | Optional | 
**test_integer** | int | Optional | 
**test_number** | float | Optional | 
**test_number_float** | float | Optional | 
**test_number_double** | string | Optional | 
**test_number_int32** | float | Optional | 
**test_number_int64** | string | Optional | 
**test_object** | string | Optional | 
**test_date_time** | string | Optional | 
**test_date** | string | Optional | 
**test_enum** | string | Optional | 
**test_object_array** | list(string) | Optional | 
**test_any_type** | string | Optional | 
**permissions** | list(string) | Optional | A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;.
**sid** | string | *Computed* | 

