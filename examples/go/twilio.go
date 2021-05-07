package twilio // Package twilio provides bindings for Twilio's REST APIs.

import (
	"github.com/twilio/twilio-go/client"
	openapi "twilio-oai-generator/go/rest/oai"
)

// Twilio provides access to Twilio services.
type RestClient struct {
	*client.Credentials
	*TestClient
	common  service
	OpenApi *openapi.DefaultApiService
}

type service struct {
	client *RestClient
}

type RestClientParams struct {
	AccountSid string
}

func NewRestClientWithParams(username string, password string, params RestClientParams) *RestClient {
	credentials := &client.Credentials{Username: username, Password: password}
	baseClient := client.Client{
		Credentials: credentials,
		BaseURL:     "twilio.com",
		AccountSid:  params.AccountSid,
	}

	c := &RestClient{
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

// NewRestClient provides an initialized Twilio RestClient.
func NewRestClient(username string, password string) *RestClient {
	return NewRestClientWithParams(username, password, RestClientParams{
		AccountSid: username,
	})
}
