package twilio

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestAWSCredentials(t *testing.T) {
	url := "http://prism_twilio:4010"
	client := NewClient("accountSid", "authToken", url)
	client.BaseURL = url
	resp, err := client.OpenApi.ListCredentialAws(nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}
