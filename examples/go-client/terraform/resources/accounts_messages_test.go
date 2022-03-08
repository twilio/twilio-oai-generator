package openapi

import (
	"fmt"
	"testing"
	. "go-client/helper/rest/api/v2010"
	"github.com/stretchr/testify/assert"
)

var messageSid = "SM123"
var toNumber = "+12223334444"
var fromNumber = "+15556667777"
var message = &ApiV2010Message{
	AccountSid: &accountSid,
	Sid:        &messageSid,
}
var boolDefault = false

func setupResource(t *testing.T) {
	setup(t)
	resource = ResourceAccountsMessages()
	resourceData = resource.TestResourceData()
}

func TestCreateMessage(t *testing.T) {
	setupResource(t)

	// Set required and optional params.
	_ = resourceData.Set("to", toNumber)
	_ = resourceData.Set("from", fromNumber)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateMessage(
		&CreateMessageParams{
			To:   &toNumber,
			From: &fromNumber,
			ForceDelivery: &boolDefault,
			ProvideFeedback: &boolDefault,
			SmartEncoded: &boolDefault,
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
	for paramName, paramSchema := range resource.Schema {
		required := paramName == "to"
		forceNew := paramName != "sid"
		computed := paramName == "sid"
		optional := paramName != "sid" && paramName != "to"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff to: %s", paramName))
		assert.Equal(t, forceNew, paramSchema.ForceNew, fmt.Sprintf("schema.ForceNew iff not sid: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff sid: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or to: %s", paramName))
	}
}
