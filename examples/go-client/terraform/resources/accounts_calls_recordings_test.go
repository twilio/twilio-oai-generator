package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var callSid = "CA123"
var recordingSid = 123
var recordingStatusCallback = "completed"
var pauseBehavior = "skip"
var callRecording = &ApiV2010CallRecording{
	CallSid: &callSid,
	Sid:     &recordingSid,
}
var recordingId = fmt.Sprintf("%s/%d", callSid, recordingSid)

func setupCallRecording(t *testing.T) {
	setup(t)
	resource = ResourceAccountsCallsRecordings()
	resourceData = resource.TestResourceData()
}

func TestCreateCallRecording(t *testing.T) {
	setupCallRecording(t)

	// Set required and optional params.
	_ = resourceData.Set("call_sid", callSid)
	_ = resourceData.Set("recording_status_callback", recordingStatusCallback)
	_ = resourceData.Set("pause_behavior", pauseBehavior)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateCallRecording(
		callSid,
		&CreateCallRecordingParams{
			RecordingStatusCallback: &recordingStatusCallback,
		},
	).Return(callRecording, nil)

	testClient.EXPECT().UpdateCallRecording(
		callSid,
		recordingSid,
		&UpdateCallRecordingParams{
			PauseBehavior: &pauseBehavior,
		},
	).Return(callRecording, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, recordingId, resourceData.Id())
	assert.Equal(t, callSid, resourceData.Get("call_sid"))
	assert.Equal(t, recordingSid, resourceData.Get("sid"))
}

func TestImportCallRecording(t *testing.T) {
	setupCallRecording(t)

	resourceData.SetId(recordingId)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, callSid, resourceData.Get("call_sid"))
	assert.Equal(t, recordingSid, resourceData.Get("sid"))
}

func TestImportInvalidCallRecording(t *testing.T) {
	setupCallRecording(t)

	resourceData.SetId(callSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaCallRecording(t *testing.T) {
	assert.Contains(t, resource.Schema, "path_account_sid")

	for paramName, paramSchema := range resource.Schema {
		required := paramName == "call_sid"
		computed := paramName != "call_sid"
		optional := paramName != "sid" && paramName != "call_sid"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff call_sid: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not call_sid: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or call_sid: %s", paramName))
	}
}
