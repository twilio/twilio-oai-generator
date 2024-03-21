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

func TestGet(t *testing.T) {
	resp, err := testApiService.PageCredentialAws(nil, "", "")
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, "Ahoy", *resp.Credentials[0].TestString)
	assert.Equal(t, "http://example.com/page1", resp.Meta.FirstPageUrl, "FirstPageUrl mismatch")
}

func TestPost(t *testing.T) {
	params := &CreateCredentialAwsParams{}

	params.SetTestString("string")
	params.SetTestBoolean(true)
	params.SetTestInteger(123)
	params.SetTestNumber(1.23)
	params.SetTestNumberFloat(4.56)
	params.SetTestNumberDouble(7.89)
	params.SetTestNumberInt32(111)
	params.SetTestNumberInt64(222)
	params.SetTestDateTime(time.Now())
	params.SetTestDate("2022-01-01")
	params.SetTestEnum("completed")

	// "Any" type should expect any type of value. We'll test with an array of maps, but any type should work.
	params.SetTestAnyType([]map[string]interface{}{{
		"type": "include",
		"all":  true,
	}})

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
	assert.Equal(t, "CR12345678123456781234567812345678", *resp.Sid)
	assert.Equal(t, "Ahoy", *resp.TestString)
}

func TestUpdate(t *testing.T) {
	params := &UpdateCredentialAwsParams{}
	params.SetTestString("MockCreds")
	params.SetTestBoolean(true)

	resp, err := testApiService.UpdateCredentialAws(authToken, params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
}
