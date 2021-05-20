package test

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"testing"
	"time"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	openapi "twilio-oai-generator/go/rest/oai"
)

func TestPathIsCorrect(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC222222222222222222222222222222"
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
		headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, rawURL, "https://autopilot.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/IncomingPhoneNumbers/PNXXXXY.json")
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewDefaultApiServiceWithClient(testClient)
	params := &openapi.FetchIncomingPhoneNumberParams{}
	twilio.FetchIncomingPhoneNumber("PNXXXXY", params)
}

func TestAccountSidAsOptionalParam(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC222222222222222222222222222222"
	}).AnyTimes()
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
		headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, rawURL, "https://autopilot.twilio.com/2010-04-01/Accounts/AC444444444444444444444444444444/IncomingPhoneNumbers/PNXXXXY.json")
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewDefaultApiServiceWithClient(testClient)
	subAccountSid := "AC444444444444444444444444444444"
	params := &openapi.FetchIncomingPhoneNumberParams{PathAccountSid: &subAccountSid}
	twilio.FetchIncomingPhoneNumber("PNXXXXY", params)
}

func TestAddingHeader(t *testing.T) {
	testHeader := "true"
	testUri := "https://validurl.com"
	params := &openapi.CreateCallRecordingParams{}
	params.XTwilioWebhookEnabled = &testHeader
	params.RecordingStatusCallback = &testUri

	expectedHeader := make(map[string]interface{})
	expectedHeader["X-Twilio-Webhook-Enabled"] = "true"

	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return accountSid
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
		headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, headers, expectedHeader)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewDefaultApiServiceWithClient(testClient)
	twilio.CreateCallRecording("CA1234", params)
}

func TestQueryParams(t *testing.T) {
	dateCreated := time.Date(2000, 1, 3, 1, 0, 0, 0, time.UTC)
	dateCreatedBefore := time.Date(2000, 1, 2, 1, 0, 0, 0, time.UTC)
	dateCreatedAfter := time.Date(2000, 1, 4, 1, 0, 0, 0, time.UTC)
	dateTest := "2021-03-31"
	pageSize := int32(4)
	params := openapi.ListCallRecordingParams{
		DateCreated:       &dateCreated,
		DateCreatedBefore: &dateCreatedBefore,
		DateCreatedAfter:  &dateCreatedAfter,
		DateTest:          &dateTest,
		PageSize:          &pageSize,
	}

	expectedData := url.Values{}
	expectedData.Set("DateCreated", fmt.Sprint(dateCreated.Format(time.RFC3339)))
	expectedData.Set("Date.Test", fmt.Sprint(dateTest))
	expectedData.Set("DateCreated<", fmt.Sprint(dateCreatedBefore.Format(time.RFC3339)))
	expectedData.Set("DateCreated>", fmt.Sprint(dateCreatedAfter.Format(time.RFC3339)))
	expectedData.Set("PageSize", fmt.Sprint(pageSize))

	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return accountSid
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
		headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, data, expectedData)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewDefaultApiServiceWithClient(testClient)
	twilio.ListCallRecording("CA1234", &params)
}
