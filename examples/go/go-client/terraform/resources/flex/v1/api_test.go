package openapi

import (
	. "go-client/terraform/client"
	"testing"

	"github.com/hashicorp/terraform-plugin-sdk/v2/helper/schema"

	"github.com/golang/mock/gomock"
)

var accountSid = "AC111"
var stringValue = "someString"
var integerValue = 123
var booleanValueDefaultValue = false

var testClient *MockFlexV1
var config *Config
var resource *schema.Resource
var resourceData *schema.ResourceData

func setup(t *testing.T) {
	testClient = NewMockFlexV1(gomock.NewController(t))
	config = &Config{Client: &RestClient{FlexV1: testClient}}
}
