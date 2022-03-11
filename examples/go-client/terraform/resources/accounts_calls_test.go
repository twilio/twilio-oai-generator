package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var call = &TestResponseObject{
	AccountSid: &accountSid,
	Sid:        &callSid,
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
	assert.Equal(t, callSid, resourceData.Id())
}

func TestFetchCall(t *testing.T) {
	setupResource(t)

	// Set required params.
	_ = resourceData.Set("sid", callSid)

	testClient.EXPECT().FetchCall(callSid, &FetchCallParams{}).Return(call, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, callSid, resourceData.Get("sid"))
}

func TestDeleteCall(t *testing.T) {
	setupResource(t)

	// Set required params.
	_ = resourceData.Set("sid", callSid)

	testClient.EXPECT().DeleteCall(callSid, &DeleteCallParams{}).Return(nil)

	resource.DeleteContext(nil, resourceData, config)

	// Assert resource ID is now empty.
	assert.Empty(t, resourceData.Id())
}

func TestImportCall(t *testing.T) {
	setupResource(t)

	resourceData.SetId(callSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, callSid, resourceData.Get("sid"))
}

func TestImportInvalidCall(t *testing.T) {
	setupResource(t)

	resourceData.SetId(fmt.Sprintf("%s/%s", accountSid, callSid))

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaCall(t *testing.T) {
	setupResource(t)

	for paramName, paramSchema := range resource.Schema {
		required := paramName == "required_string_property"
		forceNew := paramName != "sid"
		computed := paramName != "required_string_property"
		optional := paramName != "sid" && paramName != "required_string_property"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff required_string_property: %s", paramName))
		assert.Equal(t, forceNew, paramSchema.ForceNew, fmt.Sprintf("schema.ForceNew iff not sid: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not to: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or required_string_property: %s", paramName))
	}
}
