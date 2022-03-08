/*
    * Twilio - Accounts
    *
    * This is the public Twilio REST API.
    *
    * API version: 1.11.0
    * Contact: support@twilio.com
*/

// Code generated by OpenAPI Generator (https://openapi-generator.tech); DO NOT EDIT.

package openapi

import (
    "context"
    "github.com/hashicorp/terraform-plugin-sdk/v2/diag"
    "github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"
    "go-client/terraform/client"
    . "github.com/twilio/terraform-provider-twilio/core"
    . "go-client/helper/rest/api/v2010"
)

func ResourceAccountsCallsRecordings() *schema.Resource {
    return &schema.Resource{
        CreateContext: createAccountsCallsRecordings,
        ReadContext: readAccountsCallsRecordings,
        UpdateContext: updateAccountsCallsRecordings,
        DeleteContext: deleteAccountsCallsRecordings,
        Schema: map[string]*schema.Schema{
            "call_sid": AsString(SchemaRequired),
            "x_twilio_webhook_enabled": AsString(SchemaComputedOptional),
            "path_account_sid": AsString(SchemaComputedOptional),
            "recording_status_callback": AsString(SchemaComputedOptional),
            "recording_status_callback_event": AsList(AsString(SchemaComputedOptional), SchemaComputedOptional),
            "sid": AsInt(SchemaComputed),
            "status": AsString(SchemaComputedOptional),
            "pause_behavior": AsString(SchemaComputedOptional),
        },
        Importer: &schema.ResourceImporter{
            StateContext: func(ctx context.Context, d *schema.ResourceData, m interface{}) ([]*schema.ResourceData, error) {
                err := parseAccountsCallsRecordingsImportId(d.Id(), d)
                if err != nil {
                    return nil, err
                }

                return []*schema.ResourceData{d}, nil
            },
        },
    }
}

func createAccountsCallsRecordings(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := CreateCallRecordingParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    callSid := d.Get("call_sid").(string)

        r, err := m.(*client.Config).Client.ApiV2010.CreateCallRecording(callSid, &params);
        if err != nil {
            return diag.FromErr(err)
        }

        idParts := []string{ callSid,  }
        idParts = append(idParts, IntToString(*r.Sid))
        d.SetId(strings.Join(idParts, "/"))
            d.Set("sid", *r.Sid)

            return updateAccountsCallsRecordings(ctx, d, m)
}

func deleteAccountsCallsRecordings(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := DeleteCallRecordingParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    callSid := d.Get("call_sid").(string)
    sid := d.Get("sid").(int)

        err := m.(*client.Config).Client.ApiV2010.DeleteCallRecording(callSid, sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        d.SetId("")

        return nil
}

func readAccountsCallsRecordings(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := FetchCallRecordingParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    callSid := d.Get("call_sid").(string)
    sid := d.Get("sid").(int)

        r, err := m.(*client.Config).Client.ApiV2010.FetchCallRecording(callSid, sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func parseAccountsCallsRecordingsImportId(importId string, d *schema.ResourceData) error {
    importParts := strings.Split(importId, "/")
    errStr := "invalid import ID (%q), expected call_sid/sid"

    if len(importParts) != 2 {
        return fmt.Errorf(errStr, importId)
    }

        d.Set("call_sid", importParts[0])
        sid, err := StringToInt(importParts[1])
        if err != nil {
            return nil
        }
        d.Set("sid", sid)

    return nil
}
func updateAccountsCallsRecordings(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := UpdateCallRecordingParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    callSid := d.Get("call_sid").(string)
    sid := d.Get("sid").(int)

        r, err := m.(*client.Config).Client.ApiV2010.UpdateCallRecording(callSid, sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func ResourceCredentialsAWS() *schema.Resource {
    return &schema.Resource{
        CreateContext: createCredentialsAWS,
        ReadContext: readCredentialsAWS,
        UpdateContext: updateCredentialsAWS,
        DeleteContext: deleteCredentialsAWS,
        Schema: map[string]*schema.Schema{
            "credentials": AsString(SchemaRequired),
            "account_sid": AsString(SchemaComputedOptional),
            "friendly_name": AsString(SchemaComputedOptional),
            "test_number": AsFloat(SchemaComputedOptional),
            "test_number_float": AsFloat(SchemaComputedOptional),
            "test_number_double": AsString(SchemaComputedOptional),
            "test_number_int32": AsFloat(SchemaComputedOptional),
            "test_number_int64": AsString(SchemaComputedOptional),
            "test_object": AsString(SchemaComputedOptional),
            "test_date_time": AsString(SchemaComputedOptional),
            "test_date": AsString(SchemaComputedOptional),
            "test_string_array": AsList(AsString(SchemaComputedOptional), SchemaComputedOptional),
            "test_enum": AsString(SchemaComputedOptional),
            "test_object_array": AsList(AsString(SchemaComputedOptional), SchemaComputedOptional),
            "sid": AsString(SchemaComputed),
        },
        Importer: &schema.ResourceImporter{
            StateContext: func(ctx context.Context, d *schema.ResourceData, m interface{}) ([]*schema.ResourceData, error) {
                err := parseCredentialsAWSImportId(d.Id(), d)
                if err != nil {
                    return nil, err
                }

                return []*schema.ResourceData{d}, nil
            },
        },
    }
}

func createCredentialsAWS(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := CreateCredentialAwsParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }


        r, err := m.(*client.Config).Client.ApiV2010.CreateCredentialAws(&params);
        if err != nil {
            return diag.FromErr(err)
        }

        idParts := []string{  }
        idParts = append(idParts, (*r.Sid))
        d.SetId(strings.Join(idParts, "/"))

            err = MarshalSchema(d, r)
            if err != nil {
                return diag.FromErr(err)
            }

            return nil
}

func deleteCredentialsAWS(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {

    sid := d.Get("sid").(string)

        err := m.(*client.Config).Client.ApiV2010.DeleteCredentialAws(sid)
        if err != nil {
            return diag.FromErr(err)
        }

        d.SetId("")

        return nil
}

func readCredentialsAWS(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {

    sid := d.Get("sid").(string)

        r, err := m.(*client.Config).Client.ApiV2010.FetchCredentialAws(sid)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func parseCredentialsAWSImportId(importId string, d *schema.ResourceData) error {
    importParts := strings.Split(importId, "/")
    errStr := "invalid import ID (%q), expected sid"

    if len(importParts) != 1 {
        return fmt.Errorf(errStr, importId)
    }

        d.Set("sid", importParts[0])

    return nil
}
func updateCredentialsAWS(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := UpdateCredentialAwsParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    sid := d.Get("sid").(string)

        r, err := m.(*client.Config).Client.ApiV2010.UpdateCredentialAws(sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func ResourceAccountsMessages() *schema.Resource {
    return &schema.Resource{
        CreateContext: createAccountsMessages,
        ReadContext: readAccountsMessages,
        DeleteContext: deleteAccountsMessages,
        Schema: map[string]*schema.Schema{
            "required_string_property": AsString(SchemaForceNewRequired),
            "path_account_sid": AsString(SchemaForceNewOptional),
            "address_retention": AsString(SchemaForceNewOptional),
            "int_property": AsInt(SchemaForceNewOptional),
            "boolean_property": AsBool(SchemaForceNewOptional),
            "number_property": AsFloat(SchemaForceNewOptional),
            "array_of_strings_property": AsList(AsString(SchemaForceNewOptional), SchemaForceNewOptional),
            "uri_property": AsString(SchemaForceNewOptional),
            "sid": AsString(SchemaComputed),
        },
        Importer: &schema.ResourceImporter{
            StateContext: func(ctx context.Context, d *schema.ResourceData, m interface{}) ([]*schema.ResourceData, error) {
                err := parseAccountsMessagesImportId(d.Id(), d)
                if err != nil {
                    return nil, err
                }

                return []*schema.ResourceData{d}, nil
            },
        },
    }
}

func createAccountsMessages(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := CreateMessageParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }


        r, err := m.(*client.Config).Client.ApiV2010.CreateMessage(&params);
        if err != nil {
            return diag.FromErr(err)
        }

        idParts := []string{  }
        idParts = append(idParts, (*r.Sid))
        d.SetId(strings.Join(idParts, "/"))

            err = MarshalSchema(d, r)
            if err != nil {
                return diag.FromErr(err)
            }

            return nil
}

func deleteAccountsMessages(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := DeleteMessageParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    sid := d.Get("sid").(string)

        err := m.(*client.Config).Client.ApiV2010.DeleteMessage(sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        d.SetId("")

        return nil
}

func readAccountsMessages(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := FetchMessageParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    sid := d.Get("sid").(string)

        r, err := m.(*client.Config).Client.ApiV2010.FetchMessage(sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func parseAccountsMessagesImportId(importId string, d *schema.ResourceData) error {
    importParts := strings.Split(importId, "/")
    errStr := "invalid import ID (%q), expected sid"

    if len(importParts) != 1 {
        return fmt.Errorf(errStr, importId)
    }

        d.Set("sid", importParts[0])

    return nil
}
