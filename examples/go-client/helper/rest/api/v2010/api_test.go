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
	testClient := test_client.NewTestClient(accountSid, authToken)
	testClient.BaseURL = "http://prism_twilio:4010"
	testApiService = NewApiServiceWithClient(testClient)

	ret := m.Run()
	os.Exit(ret)
}

func TestGet(t *testing.T) {
	resp, err := testApiService.PageCredentialAws(nil, "", "")
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, accountSid, *resp.Credentials[0].AccountSid, "AccountSid mismatch")
	assert.Equal(t, "http://example.com/page1", resp.Meta.FirstPageUrl, "FirstPageUrl mismatch")
}

func TestPost(t *testing.T) {
	params := &CreateCredentialAwsParams{}

	params.SetAccountSid(accountSid)
	params.SetFriendlyName("MockCreds")
	params.SetCredentials("credentials")

	resp, err := testApiService.CreateCredentialAws(params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}

func TestDelete(t *testing.T) {
	err := testApiService.DeleteCredentialAws("CR12345678123456781234567812345678")
	assert.Nil(t, err)
}

func TestFetch(t *testing.T) {
	resp, err := testApiService.FetchCredentialAws("CR12345678123456781234567812345678")
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, accountSid, *resp.AccountSid)
	assert.Equal(t, "Ahoy", *resp.FriendlyName)
	assert.Equal(t, "CR12345678123456781234567812345678", *resp.Sid)
	assert.Equal(t, "http://example.com", *resp.Url)
}

func TestUpdate(t *testing.T) {
	params := &UpdateCredentialAwsParams{}
	params.SetFriendlyName("MockCreds")

	resp, err := testApiService.UpdateCredentialAws(authToken, params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}

func TestDateTimeQueryParams(t *testing.T) {
	params := &ListCallRecordingParams{}
	params.SetDateCreated(time.Now())
	params.SetDateCreatedBefore(time.Now().Add(-2))
	params.SetDateCreatedAfter(time.Now().Add(2))
	params.SetDateTest("2021-03-31")
	params.SetPageSize(4)

	resp, err := testApiService.PageCallRecording("12345678", params, "", "")

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.NotNil(t, resp.Recordings)
	assert.Equal(t, 2, len(resp.Recordings))
	assert.Equal(t, "DialVerb", *resp.Recordings[0].Source)
	assert.Equal(t, "completed", *resp.Recordings[1].Status)
}

func TestDateInPath(t *testing.T) {
	date := "2021-01-04"
	err := testApiService.DeleteArchivedCall(date, "CA12345678123456781234567812345678")
	assert.Nil(t, err)
}

func TestCustomHeaders(t *testing.T) {
	params := &CreateCallRecordingParams{}
	params.SetXTwilioWebhookEnabled("true")
	params.SetRecordingStatusCallback("https://validurl.com")

	resp, err := testApiService.CreateCallRecording("CA12345678123456781234567812345678", params)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, float32(100.22), *resp.Price)
	assert.Equal(t, "Trunking", *resp.Source)
}

func TestRequiredParameters(t *testing.T) {
	params := &CreateCallFeedbackSummaryParams{}
	params.SetStartDate("2021-04-04")
	params.SetEndDate("2021-04-05")

	// StartDate and EndDate are required parameters
	resp, err := testApiService.CreateCallFeedbackSummary(nil)
	assert.NotNil(t, err)
	assert.Nil(t, resp)

	resp, err = testApiService.CreateCallFeedbackSummary(params)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, float32(4), (*resp.Issues)[0].Count)
	assert.Equal(t, "issue description", (*resp.Issues)[0].Description)
}

func TestCustomType(t *testing.T) {
	resp, err := testApiService.FetchIncomingPhoneNumber("PNFB2fe4c709Af4C1c658b25cE7DDCEbC7", nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, false, resp.Capabilities.Fax)
	assert.Equal(t, false, resp.Capabilities.Mms)
	assert.Equal(t, true, resp.Capabilities.Sms)
	assert.Equal(t, true, resp.Capabilities.Voice)
}
