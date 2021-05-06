package twilio

import (
	"net/http"
	"net/url"

	"github.com/twilio/twilio-go/client"
)

type TestClient struct {
	*client.Credentials
	HTTPClient *http.Client
	BaseURL    string
	client.Client
}

func (testClient TestClient) getParsedUrl(path string) *url.URL {
	parsedUrl, _ := url.Parse(path)
	baseUrl, _ := url.Parse(testClient.BaseURL)
	parsedUrl.Scheme = baseUrl.Scheme
	parsedUrl.Host = baseUrl.Host
	return parsedUrl
}

// Post performs a POST request on the object at the provided URI in the context of the Request's BaseURL
// with the provided data as parameters.
func (testClient TestClient) Post(path string, bodyData url.Values, headers map[string]interface{}) (*http.Response, error) {
	parsedUrl := testClient.getParsedUrl(path)
	return testClient.Client.Post(parsedUrl.String(), bodyData, headers)
}

// Get performs a GET request on the object at the provided URI in the context of the Request's BaseURL
// with the provided data as parameters.
func (testClient TestClient) Get(path string, queryData interface{}, headers map[string]interface{}) (*http.Response, error) {
	parsedUrl := testClient.getParsedUrl(path)
	return testClient.Client.Get(parsedUrl.String(), queryData, headers)
}

// Delete performs a DELETE request on the object at the provided URI in the context of the Request's BaseURL
// with the provided data as parameters.
func (testClient TestClient) Delete(path string, data interface{}, headers map[string]interface{}) (*http.Response, error) {
	parsedUrl := testClient.getParsedUrl(path)
	return testClient.Client.Delete(parsedUrl.String(), data, headers)
}
