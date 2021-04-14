package test

import (
	"bytes"
	"fmt"
	"net/url"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	openapi "github.com/twilio/twilio-go/twilio/rest/oai"
	"io/ioutil"
	"net/http"
	"os"
	"testing"
	"time"
)

var accountSid string
var authToken string

func TestMain(m *testing.M) {
	accountSid = "AC12345678123456781234567812345678"
	authToken = "CR12345678123456781234567812345678"

	ret := m.Run()
	os.Exit(ret)
}

func TestPathIsCorrect(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().Get(
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(path string, query interface{}, headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, path, "https://autopilot.twilio.com/2010-04-01/Accounts/AC12345678123456781234567812345678/IncomingPhoneNumbers/PNXXXXY.json")
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewDefaultApiService(testClient)
	twilio.FetchIncomingPhoneNumber(accountSid, "PNXXXXY")
}

func TestAddingHeader(t *testing.T) {
	testHeader := "Custom Header"
	testUri := "https://validurl.com"
	params := &openapi.CreateCallRecordingParams{}
	params.XTwilioWebhookEnabled = &testHeader
	params.RecordingStatusCallback = &testUri
	mockCtrl := gomock.NewController(t)

	expectedHeader := make(map[string]interface{})
	expectedHeader["XTwilioWebhookEnabled"] = "Custom Header"
	testClient := NewMockBaseClient(mockCtrl)

	testClient.EXPECT().Post(
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(path string, query interface{}, headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, headers, expectedHeader)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewDefaultApiService(testClient)
	twilio.CreateCallRecording(accountSid, "CA1234", params)
}

func TestQueryParams(t *testing.T) {
	dateCreated := time.Date(2000, 1, 3, 1, 0, 0, 0, time.UTC)
	dateCreatedBefore := time.Date(2000, 1, 2, 1, 0, 0, 0, time.UTC)
	dateCreatedAfter := time.Date(2000, 1, 4, 1, 0, 0, 0, time.UTC)
	dateTest := "2021-03-31"
	pageSize := int32(4)

	expectedData := url.Values{}
	expectedData.Set("DateCreated", fmt.Sprint(dateCreated.Format(time.RFC3339)))
	expectedData.Set("DateTest", fmt.Sprint(dateTest))
	expectedData.Set("DateCreatedBefore", fmt.Sprint(dateCreatedBefore.Format(time.RFC3339)))
	expectedData.Set("DateCreatedAfter", fmt.Sprint(dateCreatedAfter.Format(time.RFC3339)))
	expectedData.Set("PageSize", fmt.Sprint(pageSize))

	params := openapi.ListCallRecordingParams{
		DateCreated:       &dateCreated,
		DateCreatedBefore: &dateCreatedBefore,
		DateCreatedAfter:  &dateCreatedAfter,
		DateTest:          &dateTest,
		PageSize:          &pageSize,
	}
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().Get(
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(path string, query interface{}, headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, query, expectedData)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewDefaultApiService(testClient)
	twilio.ListCallRecording(accountSid, "CA1234", &params)
}
