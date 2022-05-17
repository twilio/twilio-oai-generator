package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var recordingStatusCallback = "completed"
var pauseBehavior = "skip"
var account = &TestResponseObject{
	Sid: &accountSid,
}

func setupAccount(t *testing.T) {
	setup(t)
	resource = ResourceAccounts()
	resourceData = resource.TestResourceData()
}

func TestCreateAccount(t *testing.T) {
	setupAccount(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", accountSid)
	_ = resourceData.Set("recording_status_callback", recordingStatusCallback)
	_ = resourceData.Set("pause_behavior", pauseBehavior)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateAccount(
		&CreateAccountParams{
			RecordingStatusCallback: &recordingStatusCallback,
		},
	).Return(account, nil)

	testClient.EXPECT().UpdateAccount(
		accountSid,
		&UpdateAccountParams{
			PauseBehavior: &pauseBehavior,
		},
	).Return(account, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Id())
}

func TestFetchAccount(t *testing.T) {
	setupAccount(t)

	// Set required params.
	_ = resourceData.Set("sid", accountSid)

	testClient.EXPECT().FetchAccount(accountSid).Return(account, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("sid"))
}

func TestDeleteAccount(t *testing.T) {
	setupAccount(t)

	// Set required params.
	_ = resourceData.Set("sid", accountSid)

	testClient.EXPECT().DeleteAccount(accountSid).Return(nil)

	resource.DeleteContext(nil, resourceData, config)

	// Assert resource ID is now empty.
	assert.Empty(t, resourceData.Id())
}

func TestImportAccount(t *testing.T) {
	setupAccount(t)

	resourceData.SetId(accountSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, accountSid, resourceData.Id())
}

func TestImportInvalidAccount(t *testing.T) {
	setupAccount(t)

	resourceData.SetId(fmt.Sprintf("%s/%d", accountSid, callSid))

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaAccount(t *testing.T) {
	for paramName, paramSchema := range resource.Schema {
		optional := paramName != "sid"

		assert.False(t, paramSchema.Required, fmt.Sprintf("expected not schema.Required: %s", paramName))
		assert.True(t, paramSchema.Computed, fmt.Sprintf("expected schema.Computed: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid: %s", paramName))
	}
}
