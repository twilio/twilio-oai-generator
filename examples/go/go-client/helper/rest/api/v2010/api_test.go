package openapi

import (
	"os"
	"testing"
	"time"

	test_client "go-client/helper/client"

	"github.com/stretchr/testify/assert"
)

var accountSid string
var authToken string
var testApiService *ApiService

func TestMain(m *testing.M) {
	// Do setup before the tests are run
	accountSid = "AC12345678123456781234567812345678"
	authToken = "CR12345678123456781234567812345678"
	testClient := test_client.NewTestClient(accountSid, authToken, "http")
	testApiService = NewApiServiceWithClient(testClient)

	ret := m.Run()
	os.Exit(ret)
}

func TestDateTimeQueryParams(t *testing.T) {
	params := &ListAccountParams{}
	params.SetDateCreated(time.Now())
	params.SetDateCreatedBefore(time.Now().Add(-2))
	params.SetDateCreatedAfter(time.Now().Add(2))
	params.SetDateTest("2021-03-31")
	params.SetPageSize(4)

	resp, err := testApiService.PageAccount(params, "", "")

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.NotNil(t, resp.Accounts)
	assert.Equal(t, 2, len(resp.Accounts))
	assert.Equal(t, "Ahoy", *resp.Accounts[0].TestString)
	assert.Equal(t, "Matey", *resp.Accounts[1].TestString)
}

func TestNonStringInPath(t *testing.T) {
	err := testApiService.DeleteCall(123, nil)
	assert.Nil(t, err)
}

func TestCustomHeaders(t *testing.T) {
	params := &CreateAccountParams{}
	params.SetXTwilioWebhookEnabled("true")
	params.SetRecordingStatusCallback("https://validurl.com")

	resp, err := testApiService.CreateAccount(params)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, float32(100.22), *resp.TestNumberFloat)
	assert.Equal(t, "completed", *resp.TestEnum)
}

func TestRequiredParameters(t *testing.T) {
	params := &UpdateCallFeedbackSummaryParams{}
	params.SetStartDate("2021-04-04")

	// EndDate is a required parameter.
	resp, err := testApiService.UpdateCallFeedbackSummary("sid", params)
	assert.NotNil(t, err)
	assert.Nil(t, resp)

	params.SetEndDate("2021-04-05")
	resp, err = testApiService.UpdateCallFeedbackSummary("sid", params)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, float32(4), (*resp.TestArrayOfObjects)[0].Count)
	assert.Equal(t, "issue description", (*resp.TestArrayOfObjects)[0].Description)
}

func TestCustomType(t *testing.T) {
	resp, err := testApiService.FetchCall(123, nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, false, resp.TestObject.Fax)
	assert.Equal(t, false, resp.TestObject.Mms)
	assert.Equal(t, true, resp.TestObject.Sms)
	assert.Equal(t, true, resp.TestObject.Voice)
}
