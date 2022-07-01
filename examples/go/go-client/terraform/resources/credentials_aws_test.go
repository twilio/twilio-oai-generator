package openapi

import (
	"fmt"
	. "go-client/helper/rest/api/v2010"
	"testing"

	"github.com/stretchr/testify/assert"
)

var credentialsSid = "CR123"
var credential = &TestResponseObject{
	Sid:         &credentialsSid,
	TestString:  &stringValue,
	TestInteger: &integerValue,
}

func setupCredential(t *testing.T) {
	setup(t)
	resource = ResourceCredentialsAWS()
	resourceData = resource.TestResourceData()
}

func TestCreateCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required and optional params.
	_ = resourceData.Set("test_string", stringValue)
	_ = resourceData.Set("test_integer", integerValue)

	testClient.EXPECT().CreateCredentialAws(
		&CreateCredentialAwsParams{
			TestString:  &stringValue,
			TestInteger: &integerValue,
			TestBoolean: &booleanValueDefaultValue,
		},
	).Return(credential, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, credentialsSid, resourceData.Id())
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, stringValue, resourceData.Get("test_string"))
	assert.Equal(t, integerValue, resourceData.Get("test_integer"))
}

func TestFetchCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required params.
	_ = resourceData.Set("sid", credentialsSid)

	testClient.EXPECT().FetchCredentialAws(credentialsSid).Return(credential, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, stringValue, resourceData.Get("test_string"))
}

func TestUpdateCredentialAws(t *testing.T) {
	setupCredential(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("test_string", stringValue)

	testClient.EXPECT().UpdateCredentialAws(
		credentialsSid,
		&UpdateCredentialAwsParams{
			TestString: &stringValue,
		},
	).Return(credential, nil)

	resource.UpdateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, stringValue, resourceData.Get("test_string"))
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
		required := paramName == "test_string"
		computed := paramName != "test_string"
		optional := paramName != "sid" && paramName != "test_string"
		forceNew := paramName != "sid" && paramName != "test_string" && paramName != "test_boolean"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff test_string: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not test_string: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or test_string: %s", paramName))
		assert.Equal(t, forceNew, paramSchema.ForceNew, fmt.Sprintf("schema.ForceNew iff not sid or test_string or test_boolean: %s", paramName))
	}
}
