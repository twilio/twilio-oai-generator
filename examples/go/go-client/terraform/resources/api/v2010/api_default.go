/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package openapi

import (
    "context"
    "github.com/hashicorp/terraform-plugin-sdk/v2/diag"
    "github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"
    "go-client/terraform/client"
    . "github.com/twilio/terraform-provider-twilio/core"
    . "go-client/helper/rest/api/v2010"
)

func ResourceAccounts() *schema.Resource {
    return &schema.Resource{
        CreateContext: createAccounts,
        ReadContext: readAccounts,
        UpdateContext: updateAccounts,
        DeleteContext: deleteAccounts,
        Schema: map[string]*schema.Schema{
            "x_twilio_webhook_enabled": AsString(SchemaForceNewOptional),
            "recording_status_callback": AsString(SchemaForceNewOptional),
            "recording_status_callback_event": AsList(AsString(SchemaForceNewOptional), SchemaForceNewOptional),
            "twiml": AsString(SchemaForceNewOptional),
            "sid": AsString(SchemaComputed),
            "status": AsString(SchemaComputedOptional),
            "pause_behavior": AsString(SchemaComputedOptional),
        },
        Importer: &schema.ResourceImporter{
            StateContext: func(ctx context.Context, d *schema.ResourceData, m interface{}) ([]*schema.ResourceData, error) {
                err := parseAccountsImportId(d.Id(), d)
                if err != nil {
                    return nil, err
                }

                return []*schema.ResourceData{d}, nil
            },
        },
    }
}

func createAccounts(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := CreateAccountParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }


        r, err := m.(*client.Config).Client.Api.CreateAccount(&params)
        if err != nil {
            return diag.FromErr(err)
        }

        idParts := []string{  }
        idParts = append(idParts, (*r.Sid))
        d.SetId(strings.Join(idParts, "/"))
            d.Set("sid", *r.Sid)

            return updateAccounts(ctx, d, m)
}

func deleteAccounts(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {

    sid := d.Get("sid").(string)

        err := m.(*client.Config).Client.Api.DeleteAccount(sid)
        if err != nil {
            return diag.FromErr(err)
        }

        d.SetId("")

        return nil
}

func readAccounts(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {

    sid := d.Get("sid").(string)

        r, err := m.(*client.Config).Client.Api.FetchAccount(sid)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func parseAccountsImportId(importId string, d *schema.ResourceData) error {
    importParts := strings.Split(importId, "/")
    errStr := "invalid import ID (%q), expected sid"

    if len(importParts) != 1 {
        return fmt.Errorf(errStr, importId)
    }

        d.Set("sid", importParts[0])

    return nil
}
func updateAccounts(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := UpdateAccountParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    sid := d.Get("sid").(string)

        r, err := m.(*client.Config).Client.Api.UpdateAccount(sid, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func ResourceAccountsCalls() *schema.Resource {
    return &schema.Resource{
        CreateContext: createAccountsCalls,
        ReadContext: readAccountsCalls,
        DeleteContext: deleteAccountsCalls,
        Schema: map[string]*schema.Schema{
            "required_string_property": AsString(SchemaForceNewRequired),
            "test_method": AsString(SchemaForceNewRequired),
            "path_account_sid": AsString(SchemaForceNewOptional),
            "test_array_of_strings": AsList(AsString(SchemaForceNewOptional), SchemaForceNewOptional),
            "test_array_of_uri": AsList(AsString(SchemaForceNewOptional), SchemaForceNewOptional),
            "test_integer": AsInt(SchemaComputed),
        },
        Importer: &schema.ResourceImporter{
            StateContext: func(ctx context.Context, d *schema.ResourceData, m interface{}) ([]*schema.ResourceData, error) {
                err := parseAccountsCallsImportId(d.Id(), d)
                if err != nil {
                    return nil, err
                }

                return []*schema.ResourceData{d}, nil
            },
        },
    }
}

func createAccountsCalls(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := CreateCallParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }


        r, err := m.(*client.Config).Client.Api.CreateCall(&params)
        if err != nil {
            return diag.FromErr(err)
        }

        idParts := []string{  }
        idParts = append(idParts, IntToString(*r.TestInteger))
        d.SetId(strings.Join(idParts, "/"))

            err = MarshalSchema(d, r)
            if err != nil {
                return diag.FromErr(err)
            }

            return nil
}

func deleteAccountsCalls(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := DeleteCallParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    testInteger := d.Get("test_integer").(int)

        err := m.(*client.Config).Client.Api.DeleteCall(testInteger, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        d.SetId("")

        return nil
}

func readAccountsCalls(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {
    params := FetchCallParams{}
    if err := UnmarshalSchema(&params, d); err != nil {
        return diag.FromErr(err)
    }

    testInteger := d.Get("test_integer").(int)

        r, err := m.(*client.Config).Client.Api.FetchCall(testInteger, &params)
        if err != nil {
            return diag.FromErr(err)
        }

        err = MarshalSchema(d, r)
        if err != nil {
            return diag.FromErr(err)
        }

        return nil
}

func parseAccountsCallsImportId(importId string, d *schema.ResourceData) error {
    importParts := strings.Split(importId, "/")
    errStr := "invalid import ID (%q), expected test_integer"

    if len(importParts) != 1 {
        return fmt.Errorf(errStr, importId)
    }

        testInteger, err := StringToInt(importParts[0])
        if err != nil {
            return nil
        }
        d.Set("test_integer", testInteger)

    return nil
}
