package twilio

import (
    "fmt"
    "github.com/stretchr/testify/assert"
    twilio "github.com/twilio/twilio-go/client"
    "net/url"
    "net/http"
    "net/http/httptest"
    "testing"
)

func NewTestClient(accountSid string, authToken string) *twilio.Client {
    creds := &twilio.Credentials{
        AccountSID: accountSid,
        AuthToken:  authToken,
    }
    c := &twilio.Client{
        Credentials: creds,
        HTTPClient:  http.DefaultClient,
        BaseURL:     "twilio.com",
    }
    return c
}

func TestQueryParams(t *testing.T) {
    data := url.Values{}
    data.Set("PageSize", fmt.Sprint(int32(15)))
    mockResponseBody := `{
      "uri": "/2010-04-01/Accounts/ACXXXXXX/Messages.json?PageSize=15&Page=0",
      "page_size": 15,
    }`
    mockServer := httptest.NewServer(http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
        resp.WriteHeader(200)
        _, _ = resp.Write([]byte(mockResponseBody))
    }))
    defer mockServer.Close()
    client := NewTestClient("user", "pass")
    resp, err := client.SendRequest("get", mockServer.URL, data, nil, nil)
    assert.NoError(t, err)
    assert.Equal(t, 200, resp.StatusCode)
    assert.Contains(t, resp.Request.URL.String(), "PageSize=15")
}

func TestAddingHeader(t *testing.T) {
    mockResponseBody := `{
     "to": "1234567891"",
     "from": "1987654321",
     "body": "foobar",
   }`
    mockHeader := make(map[string]interface{})
    mockHeader["custom-header"] = "mock value"
    mockServer := httptest.NewServer(http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
        resp.WriteHeader(200)
        _, _ = resp.Write([]byte(mockResponseBody))
    }))
    defer mockServer.Close()
    client := NewTestClient("user", "pass")
    resp, err := client.SendRequest("get", mockServer.URL, nil, nil, mockHeader)
    assert.NoError(t, err)
    assert.Equal(t, 200, resp.StatusCode)
    assert.Equal(t, "mock value", resp.Request.Header.Get("custom-header"))
}

