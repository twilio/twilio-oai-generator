package client

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
		headers map[string]interface{}, body ...byte) (*http.Response, error)
	SetOauth(auth client.OAuth)
	OAuth() client.OAuth
}

type TestClient struct {
	client.Client
	scheme string
}

func NewTestClient(username string, password string, scheme string) *TestClient {
	c := &TestClient{
		Client: client.Client{
			Credentials: client.NewCredentials(username, password),
		},
		scheme: scheme,
	}
	c.SetAccountSid(username)
	return c
}

func (tc *TestClient) getParsedUrl(path string) *url.URL {
	parsedUrl, _ := url.Parse(path)
	parsedUrl.Scheme = tc.scheme
	return parsedUrl
}

func (tc *TestClient) SendRequest(method string, rawURL string, data url.Values, headers map[string]interface{}, body ...byte) (*http.Response, error) {
	return tc.Client.SendRequest(method, tc.getParsedUrl(rawURL).String(), data, headers, body...)
}
