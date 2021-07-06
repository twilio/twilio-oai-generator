package unit

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"testing"
	"time"

	openapi "twilio-oai-generator/go/rest/api/v2010"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
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
			assert.Equal(t, "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/IncomingPhoneNumbers/PNXXXXY.json", rawURL)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewApiServiceWithClient(testClient)
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
			assert.Equal(t, "https://api.twilio.com/2010-04-01/Accounts/AC444444444444444444444444444444/IncomingPhoneNumbers/PNXXXXY.json", rawURL)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := openapi.NewApiServiceWithClient(testClient)
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
		return "AC123"
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, expectedHeader, headers)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewApiServiceWithClient(testClient)
	twilio.CreateCallRecording("CA1234", params)
}

func TestQueryParams(t *testing.T) {
	dateCreated := time.Date(2000, 1, 3, 1, 0, 0, 0, time.UTC)
	dateCreatedBefore := time.Date(2000, 1, 2, 1, 0, 0, 0, time.UTC)
	dateCreatedAfter := time.Date(2000, 1, 4, 1, 0, 0, 0, time.UTC)
	dateTest := "2021-03-31"
	pageSize := 4
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
		return "AC123"
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewApiServiceWithClient(testClient)
	twilio.ListCallRecording("CA1234", &params)
}

func TestArrayTypeParam(t *testing.T) {
	callbackEvents := []string{"http://test1.com/", "http://test2.com"}
	params := openapi.CreateCallRecordingParams{
		RecordingStatusCallbackEvent: &callbackEvents,
	}

	expectedData := url.Values{}
	for _, item := range callbackEvents {
		expectedData.Add("RecordingStatusCallbackEvent", item)
	}

	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC123"
	})
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewApiServiceWithClient(testClient)
	twilio.CreateCallRecording("CA1234", &params)
}

func TestObjectArrayTypeParam(t *testing.T) {
	item1 := map[string]interface{}{
		"key1": "value1",
		"key2": 2,
	}
	item2 := map[string]interface{}{
		"key1": "value3",
		"key2": 4,
	}
	testObjectArrayParam := []map[string]interface{}{item1, item2}
	params := openapi.CreateCredentialAwsParams{}
	params.SetTestObjectArray(testObjectArrayParam)

	expectedData := url.Values{}
	for _, item := range testObjectArrayParam {
		obj, _ := json.Marshal(item)
		expectedData.Add("TestObjectArray", string(obj))
	}

	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)
	twilio := openapi.NewApiServiceWithClient(testClient)
	twilio.CreateCredentialAws(&params)
}
