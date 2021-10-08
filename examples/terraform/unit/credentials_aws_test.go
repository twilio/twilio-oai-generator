package unit

import (
	"fmt"
	"testing"
	. "twilio-oai-generator/go/rest/api/v2010"

	"github.com/stretchr/testify/assert"

	. "twilio-oai-generator/terraform/resources"
)

var credentialsSid = "CR123"
var friendlyName = "house-keys"
var responseArray = []int{1, 2, 3}
var credential = &AccountsV1CredentialAws{
	AccountSid:   &accountSid,
	Sid:          &credentialsSid,
	FriendlyName: &friendlyName,
	ResponseArray: responseArray,
}

func setupCredential(t *testing.T) {
	setup(t)
	resource = ResourceCredentialsAWS()
	resourceData = resource.TestResourceData()
}

func TestCreateCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("friendly_name", friendlyName)

	testClient.EXPECT().CreateCredentialAws(
		&CreateCredentialAwsParams{
			FriendlyName: &friendlyName,
		},
	).Return(credential, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, credentialsSid, resourceData.Id())
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
	for _, item := range responseArray {
		assert.Contains(t, resourceData.Get("response_array"), item)
	}
}

func TestFetchCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required params.
	_ = resourceData.Set("sid", credentialsSid)

	testClient.EXPECT().FetchCredentialAws(credentialsSid).Return(credential, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}

func TestUpdateCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("friendly_name", friendlyName)

	testClient.EXPECT().UpdateCredentialAws(
		credentialsSid,
		&UpdateCredentialAwsParams{
			FriendlyName: &friendlyName,
		},
	).Return(credential, nil)

	resource.UpdateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}

func TestDeleteCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required params.
	_ = resourceData.Set("sid", credentialsSid)

	testClient.EXPECT().DeleteCredentialAws(credentialsSid).Return(nil)

	resource.DeleteContext(nil, resourceData, config)

	// Assert resource ID is now empty.
	assert.Empty(t, resourceData.Id())
}

func TestImportCredentialAws(t *testing.T) {
	setupCredential(t)

	resourceData.SetId(credentialsSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
}

func TestImportInvalidCredentialAws(t *testing.T) {
	setupCredential(t)

	resourceData.SetId(accountSid + "/" + credentialsSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchemaCredentialAws(t *testing.T) {
	testCases := map[string]ExpectedParamSchema {
		"credential": {true, false, false, false},
		"sid": {false, false, true, false},
		"account_sid": {false, false, true, true},
		"friendly_name": {false, false, true, true},
		"test_number": {false, false, true, true},
		"url": {false, false, true, false},
		"response_array": {false, false, true, false},
	}

	for paramName, paramSchema := range resource.Schema {
		if expectedSchema, ok := testCases[paramName]; ok {
			assert.Equal(t, expectedSchema.Required, paramSchema.Required, fmt.Sprintf("schema.Required iff credentials: %s", paramName))
			assert.Equal(t, expectedSchema.Computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not credentials: %s", paramName))
			assert.Equal(t, expectedSchema.Optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff param and not sid or credentials: %s", paramName))

		}
	}
}
