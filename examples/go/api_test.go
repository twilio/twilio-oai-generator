package _go

import (
	"testing"

	"github.com/stretchr/testify/assert"
	twilio "github.com/twilio/twilio-go/client"
)

func TestAWSCredentials(t *testing.T) {
	client := twilio.NewClient("accountSid", "authToken")
	client.BaseURL = "http://prism_twilio_accounts_v1:4010"
	url := "/v1/Credentials/AWS/CRc8A889a81A65bfDD0021E4BC4be5d508"
	resp, err := client.SendRequest("GET", client.BaseURL+url, nil, nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.NotNil(t, resp.Body)
	assert.Equal(t, 200, resp.StatusCode, "Wrong status code returned")
}

func TestPublicKeyCreds(t *testing.T) {
	client := twilio.NewClient("accountSid", "authToken")
	client.BaseURL = "http://prism_twilio_accounts_v1:4010"
	url := "/v1/AuthTokens/Promote"
	resp, err := client.SendRequest("POST", client.BaseURL+url, nil, nil)
	assert.Nil(t, err)
	assert.Equal(t, 200, resp.StatusCode, "Wrong status code returned")
}
