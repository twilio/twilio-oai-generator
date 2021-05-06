package test

import (
	"os"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"twilio-oai-generator/go"
	openapi "twilio-oai-generator/go/twilio/rest/oai"
)

var accountSid string
var authToken string
var testClient *twilio.Twilio

func TestMain(m *testing.M) {
	// Do setup before the tests are run
	accountSid = "AC12345678123456781234567812345678"
	authToken = "CR12345678123456781234567812345678"
	testClient = twilio.NewClient(accountSid, authToken)
	testClient.BaseURL = "http://prism_twilio:4010"

	ret := m.Run()
	os.Exit(ret)
}

func TestGet(t *testing.T) {
	resp, err := testClient.OpenApi.ListCredentialAws(nil)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, resp.Credentials[0].AccountSid, &accountSid, "AccountSid mismatch")
	assert.Equal(t, resp.Meta.FirstPageUrl, "http://example.com/page1", "FirstPageUrl mismatch")
}

func TestPost(t *testing.T) {
	friendlyName := "MockCreds"
	credentials := "credentials"

	params := &openapi.CreateCredentialAwsParams{}

	params.AccountSid = &accountSid
	params.FriendlyName = &friendlyName
	params.Credentials = &credentials

	resp, err := testClient.OpenApi.CreateCredentialAws(params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}

func TestDelete(t *testing.T) {
	err := testClient.OpenApi.DeleteCredentialAws("CR12345678123456781234567812345678")
	assert.Nil(t, err)
}

func TestFetch(t *testing.T) {
	expectedFriendlyName := "Ahoy"
	expectedAWSSid := "CR12345678123456781234567812345678"
	expectedUrl := "http://example.com"

	resp, err := testClient.OpenApi.FetchCredentialAws("CR12345678123456781234567812345678")
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, resp.AccountSid, &accountSid)
	assert.Equal(t, resp.FriendlyName, &expectedFriendlyName)
	assert.Equal(t, resp.Sid, &expectedAWSSid)
	assert.Equal(t, resp.Url, &expectedUrl)
}

func TestUpdate(t *testing.T) {
	params := &openapi.UpdateCredentialAwsParams{}
	friendlyName := "MockCreds"
	params.FriendlyName = &friendlyName

	resp, err := testClient.OpenApi.UpdateCredentialAws(authToken, params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}

func TestDateTimeQueryParams(t *testing.T) {
	dateCreated := time.Now()
	dateCreatedBefore := time.Now().Add(-2)
	dateCreatedAfter := time.Now().Add(2)
	dateTest := "2021-03-31"

	pageSize := int32(4)

	params := openapi.ListCallRecordingParams{
		DateCreated:       &dateCreated,
		DateCreatedBefore: &dateCreatedBefore,
		DateCreatedAfter:  &dateCreatedAfter,
		DateTest:          &dateTest,
		PageSize:          &pageSize,
	}

	resp, err := testClient.OpenApi.ListCallRecording(accountSid, "CA12345678123456781234567812345678", &params)

	expectedTrack := "DialVerb"
	expectedStatus := "completed"
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.NotNil(t, resp.Recordings)
	assert.Equal(t, len(resp.Recordings), 2)
	assert.Equal(t, resp.Recordings[0].Source, &expectedTrack)
	assert.Equal(t, resp.Recordings[1].Status, &expectedStatus)
}

func TestDateInPath(t *testing.T) {
	date := "2021-01-04"
	err := testClient.OpenApi.DeleteArchivedCall(date, "CA12345678123456781234567812345678")
	assert.Nil(t, err)
}

func TestCustomHeaders(t *testing.T) {
	testHeader := "true"
	testUri := "https://validurl.com"
	params := &openapi.CreateCallRecordingParams{}
	params.XTwilioWebhookEnabled = &testHeader
	params.RecordingStatusCallback = &testUri

	resp, err := testClient.OpenApi.CreateCallRecording(accountSid, "CA12345678123456781234567812345678", params)

	expectedSource := "Trunking"
	expectedPrice := float32(100.22)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, resp.Price, &expectedPrice)
	assert.Equal(t, resp.Source, &expectedSource)
}

func TestRequiredParameters(t *testing.T) {
	startDate := "2021-04-04"
	endDate := "2021-04-05"

	params := &openapi.CreateCallFeedbackSummaryParams{
		EndDate:   &endDate,
		StartDate: &startDate,
	}

	// StartDate and EndDate are required parameters
	resp, err := testClient.OpenApi.CreateCallFeedbackSummary(accountSid, nil)
	assert.NotNil(t, err)
	assert.Nil(t, resp)

	resp, err = testClient.OpenApi.CreateCallFeedbackSummary(accountSid, params)

	expectedCount := float32(4)

	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, (*resp.Issues)[0].Count, expectedCount)
	assert.Equal(t, (*resp.Issues)[0].Description, "issue description")
}

func TestCustomType(t *testing.T) {
	resp, err := testClient.OpenApi.FetchIncomingPhoneNumber(accountSid, "PNFB2fe4c709Af4C1c658b25cE7DDCEbC7")
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, resp.Capabilities.Fax, false)
	assert.Equal(t, resp.Capabilities.Mms, false)
	assert.Equal(t, resp.Capabilities.Sms, true)
	assert.Equal(t, resp.Capabilities.Voice, true)
}
