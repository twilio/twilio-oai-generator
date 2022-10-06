package openapi

import (
	. "go-client/terraform/client"
	"testing"

	"github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"

	"github.com/golang/mock/gomock"
)

var accountSid = "AC111"
var stringValue = "someString"

var testClient *MockApi
var config *Config
var resource *schema.Resource
var resourceData *schema.ResourceData

func setup(t *testing.T) {
	testClient = NewMockApi(gomock.NewController(t))
	config = &Config{Client: &RestClient{Api: testClient}}
}
