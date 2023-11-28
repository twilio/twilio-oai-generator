package openapi

import (
	test_client "go-client/helper/client"
	"os"
	"testing"

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

func TestCreateSafelistParams_SetPhoneNumber(t *testing.T) {
	params := &CreateSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	assert.Equal(t, "+12345678910", *params.PhoneNumber)
}

func TestFetchSafelistParams_SetPhoneNumber(t *testing.T) {
	params := &FetchSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	assert.Equal(t, "+12345678910", *params.PhoneNumber)
}

func TestDeleteSafelistParams_SetPhoneNumber(t *testing.T) {
	params := &DeleteSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	assert.Equal(t, "+12345678910", *params.PhoneNumber)
}

func TestApiService_CreateSafelist(t *testing.T) {
	params := &CreateSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	resp, err := testApiService.CreateSafelist(params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, "+12345678910", *resp.PhoneNumber)
}

func TestApiService_FetchSafelist(t *testing.T) {
	params := &FetchSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	resp, err := testApiService.FetchSafelist(params)
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, "+12345678910", *resp.PhoneNumber)
}

func TestApiService_DeleteSafelist(t *testing.T) {
	params := &DeleteSafelistParams{}
	params.SetPhoneNumber("+12345678910")
	err := testApiService.DeleteSafelist(params)
	assert.Nil(t, err)
}
