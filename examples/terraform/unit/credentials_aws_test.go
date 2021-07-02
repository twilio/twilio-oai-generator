package unit

import (
	"fmt"
	"testing"
	. "twilio-oai-generator/go/rest/api/v2010"
	. "twilio-oai-generator/terraform/client"
	. "twilio-oai-generator/terraform/resources"

	"github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"

	"github.com/stretchr/testify/assert"

	"github.com/golang/mock/gomock"
)

var accountSid = "AC111"
var credentialsSid = "CR123"
var friendlyName = "house-keys"
var credential = &AccountsV1CredentialCredentialAws{
	AccountSid:   &accountSid,
	Sid:          &credentialsSid,
	FriendlyName: &friendlyName,
}

var testClient *MockApiV2010
var config *Config
var resource *schema.Resource
var resourceData *schema.ResourceData

func setup(t *testing.T) {
	testClient = NewMockApiV2010(gomock.NewController(t))
	config = &Config{Client: &RestClient{ApiV2010: testClient}}
	resource = ResourceCredentialsAWS()
	resourceData = resource.TestResourceData()
}

func TestCreate(t *testing.T) {
	setup(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("friendly_name", friendlyName)

	testClient.EXPECT().CreateCredentialAws(gomock.Any()).DoAndReturn(
		func(params *CreateCredentialAwsParams) (*AccountsV1CredentialCredentialAws, error) {
			// Assert on the params that we're expecting.
			assert.Equal(t, &friendlyName, params.FriendlyName)

			return credential, nil
		})

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, credentialsSid, resourceData.Id())
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}

func TestRead(t *testing.T) {
	setup(t)

	// Set required params.
	_ = resourceData.Set("sid", credentialsSid)

	testClient.EXPECT().FetchCredentialAws(credentialsSid).Return(credential, nil)

	resource.ReadContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}

func TestUpdate(t *testing.T) {
	setup(t)

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("friendly_name", friendlyName)

	testClient.EXPECT().UpdateCredentialAws(credentialsSid, gomock.Any()).DoAndReturn(
		func(sid string, params *UpdateCredentialAwsParams) (*AccountsV1CredentialCredentialAws, error) {
			// Assert on the params that we're expecting.
			assert.Equal(t, &friendlyName, params.FriendlyName)

			return credential, nil
		})

	resource.UpdateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}

func TestDelete(t *testing.T) {
	setup(t)

	// Set required params.
	_ = resourceData.Set("sid", credentialsSid)

	testClient.EXPECT().DeleteCredentialAws(credentialsSid).Return(nil)

	resource.DeleteContext(nil, resourceData, config)

	// Assert resource ID is now empty.
	assert.Empty(t, resourceData.Id())
}

func TestImport(t *testing.T) {
	resourceData.SetId(credentialsSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors parsing the ID.
	assert.Nil(t, err)
}

func TestImportInvalid(t *testing.T) {
	resourceData.SetId(accountSid + "/" + credentialsSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

func TestSchema(t *testing.T) {
	for paramName, paramSchema := range resource.Schema {
		required := paramName == "credentials"
		computed := paramName != "credentials"
		optional := paramName != "sid" && paramName != "credentials"

		assert.Equal(t, required, paramSchema.Required, fmt.Sprintf("schema.Required iff credentials: %s", paramName))
		assert.Equal(t, computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not credentials: %s", paramName))
		assert.Equal(t, optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff not sid or credentials: %s", paramName))
	}
}
