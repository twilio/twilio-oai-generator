package test_client

import (
	"net/http"
	"net/url"
	"time"

	"github.com/twilio/twilio-go/client"
)

type BaseClient interface {
	AccountSid() string
	SetTimeout(timeout time.Duration)
	SendRequest(method string, rawURL string, data url.Values,
		headers map[string]interface{}) (*http.Response, error)
}

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

func (tc *TestClient) getParsedUrl(path string) *url.URL {
	parsedUrl, _ := url.Parse(path)
	baseUrl, _ := url.Parse(tc.BaseURL)
	parsedUrl.Scheme = baseUrl.Scheme
	parsedUrl.Host = baseUrl.Host
	return parsedUrl
}

func (tc *TestClient) SendRequest(method string, rawURL string, data url.Values, headers map[string]interface{}) (*http.Response, error) {
	return tc.Client.SendRequest(method, tc.getParsedUrl(rawURL).String(), data, headers)
}
