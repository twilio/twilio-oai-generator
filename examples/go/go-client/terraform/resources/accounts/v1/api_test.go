package openapi

import (
	"testing"

	"github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"

	"github.com/golang/mock/gomock"
)

var accountSid = "AC111"
var stringValue = "someString"
var integerValue = 123
var booleanValueDefaultValue = false

var testClient *MockAccountsV1
var config *Config
var resource *schema.Resource
var resourceData *schema.ResourceData

func setup(t *testing.T) {
	testClient = NewMockAccountsV1(gomock.NewController(t))
	config = &Config{Client: &RestClient{AccountsV1: testClient}}
}
