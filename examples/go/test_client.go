package test_client

import (
	"net/http"
	"net/url"

	"github.com/twilio/twilio-go/client"
)

type TestClient struct {
	client.Client
	BaseURL    string
}

func NewTestClient(username string, password string) *TestClient {
	c := &TestClient{
		Client: client.Client{
			Credentials: client.NewCredentials(username, password),
		},
	}
	c.SetAccountSid(username)
	return c
}

func (testClient TestClient) getParsedUrl(path string) *url.URL {
	parsedUrl, _ := url.Parse(path)
	baseUrl, _ := url.Parse(testClient.BaseURL)
	parsedUrl.Scheme = baseUrl.Scheme
	parsedUrl.Host = baseUrl.Host
	return parsedUrl
}

func (c *TestClient) SendRequest(method string, rawURL string, data url.Values, headers map[string]interface{}) (*http.Response, error) {
	return c.Client.SendRequest(method, c.getParsedUrl(rawURL).String(), data, headers)
}
