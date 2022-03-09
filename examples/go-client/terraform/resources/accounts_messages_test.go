package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var messageSid = "SM123"
var message = &TestResponseObject{
	AccountSid: &accountSid,
	Sid:        &messageSid,
}

func setupResource(t *testing.T) {
	setup(t)
	resource = ResourceAccountsMessages()
	resourceData = resource.TestResourceData()
}

func TestCreateMessage(t *testing.T) {
	setupResource(t)

	// Set required and optional params.
	_ = resourceData.Set("required_string_property", stringValue)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateMessage(
		&CreateMessageParams{
			RequiredStringProperty: &stringValue,
		},
	).Return(message, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, messageSid, resourceData.Id())
}

func TestImportMessage(t *testing.T) {
	setupResource(t)

	resourceData.SetId(messageSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, messageSid, resourceData.Get("sid"))
}

func TestImportInvalidMessage(t *testing.T) {
	setupResource(t)

	resourceData.SetId(fmt.Sprintf("%s/%s", accountSid, messageSid))

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaMessage(t *testing.T) {
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
