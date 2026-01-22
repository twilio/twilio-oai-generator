package openapi

import (
	"bytes"
	"encoding/json"
	"errors"
	"io"
	"net/http"
	"net/url"
	"testing"

	. "go-client/helper/client"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
)

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
			headers map[string]interface{}, body ...byte) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: getEmptyBody()}, nil
		},
		)
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.CreateCredentialAws(&params)
}

func TestUpdateOperation(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	testClient := NewMockBaseClient(mockCtrl)
	testClient.EXPECT().SendRequest(
		gomock.Any(),
		gomock.Any(),
		gomock.Any(),
		gomock.Any()).
		DoAndReturn(func(method string, rawURL string, data url.Values, headers map[string]interface{}, body ...byte) (*http.Response, error) {
			assert.Equal(t, "POST", method)
			assert.Equal(t, "https://flex-api.twilio.com/v1/Voice/VO123", rawURL)
			return &http.Response{Body: getEmptyBody()}, nil
		},
		)
	twilio := NewApiServiceWithClient(testClient)
	_, _ = twilio.UpdateCall("VO123")
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
			headers map[string]interface{}, body ...byte) (*http.Response, error) {
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

			return &http.Response{Body: io.NopCloser(bytes.NewReader(resp))}, errors.New("listing error")
		},
		).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	params := &ListCredentialAwsParams{}
	params.SetPageSize(5)
	params.SetLimit(5)

	resp, err := twilio.ListCredentialAws(params)
	assert.Len(t, resp, 0)
	assert.NotNil(t, err)
	assert.Equal(t, "listing error", err.Error())
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
			headers map[string]interface{}, body ...byte) (*http.Response, error) {
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

			return &http.Response{Body: io.NopCloser(bytes.NewReader(resp))}, nil
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
		DoAndReturn(func(method string, rawURL string, data url.Values, headers map[string]interface{}, body ...byte) (*http.Response, error) {
			assert.Equal(t, expectedData, data)
			return &http.Response{Body: getEmptyBody()}, nil
		}).AnyTimes()

	twilio := NewApiServiceWithClient(testClient)

	_, _ = twilio.PageCredentialAws(nil, pageToken, pageNumber)
}

func getEmptyBody() io.ReadCloser {
	return io.NopCloser(bytes.NewReader([]byte("{}")))
}
