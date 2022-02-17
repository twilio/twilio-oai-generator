package openapi

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"testing"
	"time"

	. "go-client/helper"

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

	twilio := NewApiServiceWithClient(testClient)
	params := &FetchIncomingPhoneNumberParams{}
	_, _ = twilio.FetchIncomingPhoneNumber("PNXXXXY", params)
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

	twilio := NewApiServiceWithClient(testClient)
	subAccountSid := "AC444444444444444444444444444444"
	params := &FetchIncomingPhoneNumberParams{PathAccountSid: &subAccountSid}
	_, _ = twilio.FetchIncomingPhoneNumber("PNXXXXY", params)
}

func TestAddingHeader(t *testing.T) {
	testHeader := "true"
	testUri := "https://validurl.com"
	params := &CreateCallRecordingParams{}
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
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.CreateCallRecording("CA1234", params)
}

func TestQueryParams(t *testing.T) {
	dateCreated := time.Date(2000, 1, 3, 1, 0, 0, 0, time.UTC)
	dateCreatedBefore := time.Date(2000, 1, 2, 1, 0, 0, 0, time.UTC)
	dateCreatedAfter := time.Date(2000, 1, 4, 1, 0, 0, 0, time.UTC)
	dateTest := "2021-03-31"
	pageSize := 4
	params := ListCallRecordingParams{
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
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.ListCallRecording("12345678", &params)
}

func TestArrayTypeParam(t *testing.T) {
	callbackEvents := []string{"http://test1.com/", "http://test2.com"}
	params := CreateCallRecordingParams{
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
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.CreateCallRecording("CA1234", &params)
}

func getStreamCount(twilio *ApiService, params *ListMessageParams) int {
	messageCount := 0

	channel, _ := twilio.StreamMessage(params)
	for range channel {
		messageCount += 1
	}
	return messageCount
}

func TestList(t *testing.T) {
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
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "inbound",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "accepted",
					},
					{
						"direction": "outbound-reply",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "read",
					},
					{
						"direction": "outbound-call",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetFrom("4444444444")
	params.SetTo("9999999999")
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.ListMessage(params)
	assert.Equal(t, "outbound-api", *resp[0].Direction)
	assert.Equal(t, "4444444444", *resp[0].From)
	assert.Equal(t, "inbound", *resp[1].Direction)
	assert.Equal(t, "read", *resp[2].Status)
	assert.Equal(t, 5, len(resp))
	assert.Nil(t, err)

	resp, _ = twilio.ListMessage(params)
	assert.Equal(t, 5, len(resp))

	params.SetLimit(10)
	resp, _ = twilio.ListMessage(params)
	assert.Equal(t, 10, len(resp))
}

func TestListPaging(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC222222222222222222222222222222"
	}).AnyTimes()

	page0 := testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-call",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi!",
						"status":    "accepted",
					},
					{
						"direction": "outbound-reply",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "canceled",
					},
					{
						"direction": "inbound",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "received",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "queued",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		)

	page1 := testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			response := map[string]interface{}{
				"end":            3,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-call",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi!",
						"status":    "queued",
					},
					{
						"direction": "outbound-reply",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "sent",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=",
				"page_size":     2,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=2&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          1,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		)

	page2 := testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(nil)),
			}, nil
		},
		)

	gomock.InOrder(
		page0,
		page1,
		page2,
	)

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetFrom("from")
	params.SetTo("to")
	params.SetPageSize(5)
	params.SetLimit(10)

	resp, _ := twilio.ListMessage(params)
	assert.Equal(t, "delivered", *resp[0].Status)
	assert.Equal(t, "Hi!", *resp[1].Body)
	assert.Equal(t, 9, len(resp))
}

func TestListError(t *testing.T) {
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
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, errors.New("Listing error")
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetFrom("from")
	params.SetTo("to")
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.ListMessage(params)
	assert.Len(t, resp, 0)
	assert.NotNil(t, err)
	assert.Equal(t, "Listing error", err.Error())
}

func TestStream(t *testing.T) {
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
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-call",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "queued",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hello",
						"status":    "sent",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetPageSize(5)
	params.SetLimit(10)

	messageCount := getStreamCount(twilio, params)
	assert.Equal(t, 10, messageCount)

	params.SetLimit(15)
	messageCount = getStreamCount(twilio, params)
	assert.Equal(t, 15, messageCount)

	params.SetLimit(40)
	messageCount = getStreamCount(twilio, params)
	assert.Equal(t, 40, messageCount)
}

func TestStreamPaging(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC222222222222222222222222222222"
	}).AnyTimes()

	page0 := testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 0",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 1",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 2",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 3",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 4",
						"status":    "delivered",
					},
				},
				"uri":           "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=dummy",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		)

	page1 := testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values,
			headers map[string]interface{}) (*http.Response, error) {
			response := map[string]interface{}{
				"end":            2,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 5",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 6",
						"status":    "delivered",
					},
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Message 7",
						"status":    "delivered",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=",
				"page_size":     2,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=2&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          1,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		)

	gomock.InOrder(
		page0,
		page1,
	)

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetFrom("4444444444")
	params.SetTo("9999999999")
	params.SetPageSize(5)
	params.SetLimit(8)

	messageCount := 0

	channel, _ := twilio.StreamMessage(params)
	for record := range channel {
		text := fmt.Sprintf("Message %d", messageCount)
		assert.Equal(t, text, *record.Body)
		messageCount += 1
	}

	assert.Equal(t, 8, messageCount)
}

func TestStreamError(t *testing.T) {
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
			response := map[string]interface{}{
				"end":            4,
				"first_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0",
				"messages": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
				},
				"uri":           "“/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=0&PageToken=",
				"page_size":     5,
				"start":         0,
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Messages.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
				"page":          0,
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, errors.New("streaming error")
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListMessageParams{}
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.StreamMessage(params)
	assert.Len(t, resp, 0)
	assert.NotNil(t, err)
	assert.Equal(t, "streaming error", err.Error())
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
	params := CreateCredentialAwsParams{}
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
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.CreateCredentialAws(&params)
}
