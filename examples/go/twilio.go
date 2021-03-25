package twilio // Package twilio provides bindings for Twilio's REST APIs.

import (
	"github.com/twilio/twilio-go/client"
	openapi "github.com/twilio/twilio-go/twilio/rest/oai"
)

// Twilio provides access to Twilio services.
type Twilio struct {
	*client.Credentials
	*client.Client
	common  service
	OpenApi *openapi.DefaultApiService
}

type service struct {
	client *Twilio
}

// NewClient provides an initialized Twilio client.
func NewClient(accountSID string, authToken string, baseurl string) *Twilio {
	credentials := &client.Credentials{AccountSID: accountSID, AuthToken: authToken}

	c := &Twilio{
		Credentials: credentials,
		Client: &client.Client{
			Credentials: credentials,
			BaseURL:     baseurl,
		},
	}

	c.common.client = c
	c.OpenApi = openapi.NewDefaultApiService(c.Client)
	c.OpenApi.BaseURL = baseurl

	return c
}
