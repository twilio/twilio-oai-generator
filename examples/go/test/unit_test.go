package test

import (
    "testing"

    "github.com/stretchr/testify/assert"
    "github.com/golang/mock/gomock"
    openapi "github.com/twilio/twilio-go/twilio/rest/oai"
    )

func TestPathIsCorrect(t *testing.T) {
    accountSid := "AC12345678123456781234567812345678"
    //create with mock base-client
    mockCtrl := gomock.NewController(t)
    //we need to create the mock for the get function to be intercepted.
    testClient := NewMockBaseClient(mockCtrl)

    testClient.EXPECT().Get(
        gomock.Any(),
        gomock.Any(),
        gomock.Any()).
        Do(func(path string, query interface{}, headers map[string]interface{}) {
        assert.Equal(t,path,"https://autopilot.twilio.com/2010-04-01/Accounts/AC12345678123456781234567812345678/IncomingPhoneNumbers/PNXXXX.json")
    },
        )
    twilio := openapi.NewDefaultApiService(testClient)
    twilio.FetchIncomingPhoneNumber(accountSid,"PNXXXX")


}






//
//func TestQueryParams(t *testing.T) {
//	data := url.Values{}
//	data.Set("PageSize", fmt.Sprint(int32(15)))
//	mockResponseBody := `{
//    "uri": "/2010-04-01/Accounts/ACXXXXXX/Messages.json?PageSize=15&Page=0",
//    "page_size": 15,
//  }`
//	mockServer := httptest.NewServer(http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
//		resp.WriteHeader(200)
//		_, _ = resp.Write([]byte(mockResponseBody))
//	}))
//	defer mockServer.Close()
//	client := NewTestClient("user", "pass")
//	resp, err := client.SendRequest("get", mockServer.URL, data, nil, nil)
//	assert.NoError(t, err)
//	assert.Equal(t, 200, resp.StatusCode)
//	assert.Contains(t, resp.Request.URL.String(), "PageSize=15")
//}
//
//func TestAddingHeader(t *testing.T) {
//	mockResponseBody := `{
//    "to": "1234567891"",
//    "from": "1987654321",
//    "body": "foobar",
//  }`
//	mockHeader := make(map[string]interface{})
//	mockHeader["custom-header"] = "mock value"
//	mockServer := httptest.NewServer(http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
//		resp.WriteHeader(200)
//		_, _ = resp.Write([]byte(mockResponseBody))
//	}))
//	defer mockServer.Close()
//	client := NewTestClient("user", "pass")
//	resp, err := client.SendRequest("get", mockServer.URL, nil, nil, mockHeader)
//	assert.NoError(t, err)
//	assert.Equal(t, 200, resp.StatusCode)
//	assert.Equal(t, "mock value", resp.Request.Header.Get("custom-header"))
//}
