
## twilio_api_accounts

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**x_twilio_webhook_enabled** | string | Optional | 
**recording_status_callback** | string | Optional | 
**recording_status_callback_event** | list(string) | Optional | 
**twiml** | string | Optional | 
**sid** | string | *Computed* | 
**status** | string | Optional | 
**pause_behavior** | string | Optional | 

## twilio_api_accounts_calls

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**required_string_property** | string | **Required** | 
**test_method** | string | **Required** | The HTTP method that we should use to request the `TestArrayOfUri`.
**path_account_sid** | string | Optional | 
**test_array_of_strings** | list(string) | Optional | 
**test_array_of_uri** | list(string) | Optional | 
**test_integer** | int | *Computed* | INTEGER ID param!!!

