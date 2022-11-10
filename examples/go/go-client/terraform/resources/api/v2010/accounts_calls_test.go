package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
	. "github.com/twilio/terraform-provider-twilio/core"
)

var callSid = 123
var callId = IntToString(123)
var call = &TestResponseObject{
	AccountSid:  &accountSid,
	TestInteger: &callSid,
}

func setupResource(t *testing.T) {
	setup(t)
	resource = ResourceAccountsCalls()
	resourceData = resource.TestResourceData()
}

func TestCreateCall(t *testing.T) {
	setupResource(t)

	// Set required and optional params.
	_ = resourceData.Set("required_string_property", stringValue)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateCall(
		&CreateCallParams{
			RequiredStringProperty: &stringValue,
		},
	).Return(call, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, callId, resourceData.Id())
}

func TestFetchCall(t *testing.T) {
	setupResource(t)

	// Set required params.
	_ = resourceData.Set("test_integer", callSid)

	testClient.EXPECT().FetchCall(callSid, &FetchCallParams{}).Return(call, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, callSid, resourceData.Get("test_integer"))
}

func TestDeleteCall(t *testing.T) {
	setupResource(t)

	// Set required params.
	_ = resourceData.Set("test_integer", callSid)

	testClient.EXPECT().DeleteCall(callSid, &DeleteCallParams{}).Return(nil)

	resource.DeleteContext(nil, resourceData, config)

	// Assert resource ID is now empty.
	assert.Empty(t, resourceData.Id())
}

func TestImportCall(t *testing.T) {
	setupResource(t)

	resourceData.SetId(callId)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, callSid, resourceData.Get("test_integer"))
}

func TestImportInvalidCall(t *testing.T) {
	setupResource(t)

	resourceData.SetId(fmt.Sprintf("%s/%d", accountSid, callSid))

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaCall(t *testing.T) {
	setupResource(t)

	assert.Contains(t, resource.Schema, "path_account_sid")

	for paramName, paramSchema := range resource.Schema {
		required := paramName == "required_string_property" || paramName == "test_method"
		forceNew := paramName != "test_integer"
		computed := !required
		optional := paramName != "test_integer" && !required

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff required_string_property: %s", paramName))
		assert.Equal(t, forceNew, paramSchema.ForceNew, fmt.Sprintf("schema.ForceNew iff not test_integer: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not to: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not test_integer or required_string_property: %s", paramName))
	}
}
