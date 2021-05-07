package twilio // Package twilio provides bindings for Twilio's REST APIs.

import (
	"github.com/twilio/twilio-go/client"
	openapi "twilio-oai-generator/go/twilio/rest/oai"
)

// Twilio provides access to Twilio services.
type Twilio struct {
	*client.Credentials
	*TestClient
	common  service
	OpenApi *openapi.DefaultApiService
}

type service struct {
	client *Twilio
}

func NewClient(accountSID string, authToken string) *Twilio {
	credentials := &client.Credentials{AccountSID: accountSID, AuthToken: authToken}
	baseClient := client.Client{
		Credentials: credentials,
		BaseURL:     "twilio.com",
		AccountSid:  accountSid,
	}

	c := &Twilio{
		Credentials: credentials,
		TestClient: &TestClient{
			Credentials: credentials,
			BaseURL:     "twilio.com",
			Client:      baseClient,
		},
	}

	c.common.client = c
	c.OpenApi = openapi.NewDefaultApiService(c.TestClient)

	return c
}

// NewClient provides an initialized Twilio client.
func NewClient(userName string, authToken string) *Twilio {
	return NewClientWithAccountSid(userName, authToken, userName)
}
