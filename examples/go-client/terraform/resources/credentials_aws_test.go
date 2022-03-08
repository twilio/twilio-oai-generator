package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var credentialsSid = "CR123"
var friendlyName = "house-keys"
var credential = &AccountsV1CredentialAws{
	AccountSid:   &accountSid,
	Sid:          &credentialsSid,
	FriendlyName: &friendlyName,
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
	for paramName, paramSchema := range resource.Schema {
		required := paramName == "credentials"
		computed := paramName != "credentials"
		optional := paramName != "sid" && paramName != "credentials"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff credentials: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not credentials: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or credentials: %s", paramName))
	}
}
