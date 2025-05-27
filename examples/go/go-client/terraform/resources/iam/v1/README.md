
## twilio_iam_keys_v1

### Parameters

Name | Type | Requirement | Description
--- | --- | --- | ---
**account_sid** | string | **Required** | The SID of the [Account](https://www.twilio.com/docs/iam/api/account) that created the Payments resource.
**friendly_name** | string | Optional | A descriptive string that you create to describe the resource. It can be up to 64 characters long.
**key_type** | string | Optional | 
**policy** | string | Optional | The \\\\`Policy\\\\` object is a collection that specifies the allowed Twilio permissions for the restricted key. For more information on the permissions available with restricted API keys, refer to the [Twilio documentation](https://www.twilio.com/docs/iam/api-keys/restricted-api-keys#permissions-available-with-restricted-api-keys).
**sid** | string | *Computed* | The Twilio-provided string that uniquely identifies the Key resource to update.

