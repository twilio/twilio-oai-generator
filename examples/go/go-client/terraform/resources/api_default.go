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
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"go-client/terraform/client"
	"strings"

	"github.com/hashicorp/terraform-plugin-sdk/v2/diag"
	"github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"
	. "github.com/twilio/terraform-provider-twilio/core"
)

func ResourceAccounts() *schema.Resource {
	return &schema.Resource{
		CreateContext: createAccounts,
		ReadContext:   readAccounts,
		UpdateContext: updateAccounts,
		DeleteContext: deleteAccounts,
		Schema: map[string]*schema.Schema{
			"x_twilio_webhook_enabled":        AsString(SchemaComputedOptional),
			"recording_status_callback":       AsString(SchemaComputedOptional),
			"recording_status_callback_event": AsList(AsString(SchemaComputedOptional), SchemaComputedOptional),
			"sid":                             AsString(SchemaComputed),
			"status":                          AsString(SchemaComputedOptional),
			"pause_behavior":                  AsString(SchemaComputedOptional),
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

	idParts := []string{}
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
		ReadContext:   readAccountsCalls,
		DeleteContext: deleteAccountsCalls,
		Schema: map[string]*schema.Schema{
			"required_string_property": AsString(SchemaForceNewRequired),
			"path_account_sid":         AsString(SchemaForceNewOptional),
			"test_array_of_strings":    AsList(AsString(SchemaForceNewOptional), SchemaForceNewOptional),
			"test_integer":             AsInt(SchemaComputed),
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

	idParts := []string{}
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
func ResourceCredentialsAWS() *schema.Resource {
	return &schema.Resource{
		CreateContext: createCredentialsAWS,
		ReadContext:   readCredentialsAWS,
		UpdateContext: updateCredentialsAWS,
		DeleteContext: deleteCredentialsAWS,
		Schema: map[string]*schema.Schema{
			"test_string":        AsString(SchemaRequired),
			"test_boolean":       AsBool(SchemaComputedOptional),
			"test_integer":       AsInt(SchemaComputedOptional),
			"test_number":        AsFloat(SchemaComputedOptional),
			"test_number_float":  AsFloat(SchemaComputedOptional),
			"test_number_double": AsString(SchemaComputedOptional),
			"test_number_int32":  AsFloat(SchemaComputedOptional),
			"test_number_int64":  AsString(SchemaComputedOptional),
			"test_object":        AsString(SchemaComputedOptional),
			"test_date_time":     AsString(SchemaComputedOptional),
			"test_date":          AsString(SchemaComputedOptional),
			"test_enum":          AsString(SchemaComputedOptional),
			"test_object_array":  AsList(AsString(SchemaComputedOptional), SchemaComputedOptional),
			"test_any_type":      AsString(SchemaComputedOptional),
			"sid":                AsString(SchemaComputed),
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

	r, err := m.(*client.Config).Client.Api.CreateCredentialAws(&params)
	if err != nil {
		return diag.FromErr(err)
	}

	idParts := []string{}
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

	err := m.(*client.Config).Client.Api.DeleteCredentialAws(sid)
	if err != nil {
		return diag.FromErr(err)
	}

	d.SetId("")

	return nil
}

func readCredentialsAWS(ctx context.Context, d *schema.ResourceData, m interface{}) diag.Diagnostics {

	sid := d.Get("sid").(string)

	r, err := m.(*client.Config).Client.Api.FetchCredentialAws(sid)
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

	r, err := m.(*client.Config).Client.Api.UpdateCredentialAws(sid, &params)
	if err != nil {
		return diag.FromErr(err)
	}

	err = MarshalSchema(d, r)
	if err != nil {
		return diag.FromErr(err)
	}

	return nil
}
