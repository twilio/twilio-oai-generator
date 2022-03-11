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

	. "go-client/helper/client"

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
			assert.Equal(t, "https://api.twilio.com/2010-04-01/Accounts/AC222222222222222222222222222222/Calls/CAXXXXY.json", rawURL)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.FetchCall("CAXXXXY", nil)
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
			assert.Equal(t, "https://api.twilio.com/2010-04-01/Accounts/AC444444444444444444444444444444/Calls/CAXXXXY.json", rawURL)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		},
		)

	twilio := NewApiServiceWithClient(testClient)
	params := &DeleteCallParams{}
	params.SetPathAccountSid("AC444444444444444444444444444444")
	_ = twilio.DeleteCall("CAXXXXY", params)
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
	params := ListCallRecordingParams{}
	params.SetDateCreated(dateCreated)
	params.SetDateCreatedBefore(dateCreatedBefore)
	params.SetDateCreatedAfter(dateCreatedAfter)
	params.SetDateTest(dateTest)
	params.SetPageSize(pageSize)

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
	items := []string{"https://test1.com/", "https://test2.com"}
	params := CreateCallParams{}
	params.SetTestArrayOfStrings(items)

	expectedData := url.Values{}
	for _, item := range items {
		expectedData.Add("TestArrayOfStrings", item)
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
	_, _ = twilio.CreateCall(&params)
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

func getStreamCount(twilio *ApiService, params *ListCallParams) int {
	callCount := 0

	channel, _ := twilio.StreamCall(params)
	for range channel {
		callCount += 1
	}
	return callCount
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
				"calls": []map[string]interface{}{
					{
						"test_string": "hi",
					},
					{
						"test_string": "there",
					},
					{
						"test_string": "you",
					},
					{
						"test_string": "and",
					},
					{
						"test_string": "others",
					},
				},
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Calls.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListCallParams{}
	params.SetFrom("4444444444")
	params.SetTo("9999999999")
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.ListCall(params)
	assert.Equal(t, "hi", *resp[0].TestString)
	assert.Equal(t, "there", *resp[1].TestString)
	assert.Equal(t, 5, len(resp))
	assert.Nil(t, err)

	resp, _ = twilio.ListCall(params)
	assert.Equal(t, 5, len(resp))

	params.SetLimit(10)
	resp, _ = twilio.ListCall(params)
	assert.Equal(t, 10, len(resp))

	params.SetLimit(3)
	resp, _ = twilio.ListCall(params)
	assert.Equal(t, 3, len(resp))
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
				"recordings": []map[string]interface{}{
					{
						"test_string": "first",
					},
					{
						"test_string": "second",
					},
				},
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Calls.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
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
				"recordings": []map[string]interface{}{
					{
						"test_string": "third",
					},
				},
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Calls.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=2&PageToken=PASMc49f620580b24424bcfa885b1f741130",
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

	params := &ListCallRecordingParams{}
	params.SetPageSize(5)
	params.SetLimit(10)

	resp, _ := twilio.ListCallRecording("CA123", params)
	assert.Equal(t, "first", *resp[0].TestString)
	assert.Equal(t, "second", *resp[1].TestString)
	assert.Equal(t, "third", *resp[2].TestString)
	assert.Equal(t, 3, len(resp))
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
				"credentials": []map[string]interface{}{
					{
						"direction": "outbound-api",
						"from":      "4444444444",
						"to":        "9999999999",
						"body":      "Hi",
						"status":    "delivered",
					},
				},
				"next_page_uri": "/2010-04-01/Accounts/AC12345678123456781234567812345678/Calls.json?From=9999999999&PageNumber=&To=4444444444&PageSize=5&Page=1&PageToken=PASMc49f620580b24424bcfa885b1f741130",
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, errors.New("Listing error")
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListCredentialAwsParams{}
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.ListCredentialAws(params)
	assert.Len(t, resp, 0)
	assert.NotNil(t, err)
	assert.Equal(t, "Listing error", err.Error())
}

func TestListNoNextPage(t *testing.T) {
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
				"credentials": []map[string]interface{}{
					{
						"test_string": "me",
					},
					{
						"test_string": "and",
					},
					{
						"test_string": "you",
					},
				},
			}

			resp, _ := json.Marshal(response)

			return &http.Response{
				Body: ioutil.NopCloser(bytes.NewReader(resp)),
			}, nil
		}).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	resp, err := twilio.ListCredentialAws(nil)
	assert.Equal(t, 3, len(resp))
	assert.Nil(t, err)
}

func TestPageToken(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)

	testClient.EXPECT().AccountSid().DoAndReturn(func() string {
		return "AC222222222222222222222222222222"
	}).AnyTimes()

	pageToken := "token"
	pageNumber := "5"

	expectedData := url.Values{}
	expectedData.Set("PageToken", pageToken)
	expectedData.Set("Page", pageNumber)

	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values, headers map[string]interface{}) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: ioutil.NopCloser(bytes.NewReader(nil))}, nil
		}).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	_, _ = twilio.PageCredentialAws(nil, pageToken, pageNumber)
}
