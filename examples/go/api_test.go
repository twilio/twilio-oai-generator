package twilio

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCredentialsGet(t *testing.T) {
	accountSid := "AC2C5555A9A7eBBAAACaD0D010eAd2Ec2c"
	authToken := "CR4E2286812c5ad1BC0789C4Be4fF1F629"

	client := NewClient(accountSid, authToken)
	client.BaseURL = "http://prism_twilio:4010"

	resp, err := client.OpenApi.ListCredentialAws(nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, resp.Credentials[0].AccountSid, &accountSid, "AccountSid mismatch")
	assert.Equal(t, resp.Meta.FirstPageUrl, "http://example.com/page1", "FirstPageUrl mismatch")
}
