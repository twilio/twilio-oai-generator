package unit

import (
	"testing"
	. "twilio-oai-generator/go/rest/api/v2010"
	. "twilio-oai-generator/terraform/client"
	. "twilio-oai-generator/terraform/resources"

	"github.com/stretchr/testify/assert"

	"github.com/golang/mock/gomock"
)

func TestUpdate(t *testing.T) {
	accountSid := "AC111"
	credentialsSid := "CR123"
	friendlyName := "house-keys"

	testClient := NewMockApiV2010(gomock.NewController(t))
	testClient.EXPECT().UpdateCredentialAws(credentialsSid, gomock.Any()).DoAndReturn(
		func(sid string, params *UpdateCredentialAwsParams) (*AccountsV1CredentialCredentialAws, error) {
			// Assert on the params that we're expecting.
			assert.Equal(t, &friendlyName, params.FriendlyName)

			return &AccountsV1CredentialCredentialAws{
				AccountSid:   &accountSid,
				Sid:          &credentialsSid,
				FriendlyName: &friendlyName,
			}, nil
		})

	config := &Config{Client: &RestClient{ApiV2010: testClient}}

	resource := ResourceCredentialsAWS()
	resourceData := resource.TestResourceData()

	// Set required and optional params.
	_ = resourceData.Set("sid", credentialsSid)
	_ = resourceData.Set("friendly_name", friendlyName)

	resource.UpdateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, accountSid, resourceData.Get("account_sid"))
	assert.Equal(t, credentialsSid, resourceData.Get("sid"))
	assert.Equal(t, friendlyName, resourceData.Get("friendly_name"))
}
