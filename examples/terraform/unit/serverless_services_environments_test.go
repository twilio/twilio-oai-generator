package unit

import (
	"fmt"
	"testing"
	. "twilio-oai-generator/go/rest/api/v2010"
	. "twilio-oai-generator/terraform/resources"

	"github.com/stretchr/testify/assert"
)

var serviceSid = "ZS123"
var environmentSid = "ZE123"
var uniqueName = "unique"
var domainSuffix = "org"
var serviceEnvironment = &ServerlessV1Environment{
	ServiceSid: &serviceSid,
	Sid:     &environmentSid,
}

var environmentId = fmt.Sprintf("%s/%s", serviceSid, environmentSid)

func setupServiceEnvironment(t *testing.T) {
	setup(t)
	resource = ResourceServicesEnvironments()
	resourceData = resource.TestResourceData()
}

func TestCreateServiceEnvironment(t *testing.T) {
	setupServiceEnvironment(t)

	// Set required and optional params.
	_ = resourceData.Set("service_sid", serviceSid)
	_ = resourceData.Set("unique_name", uniqueName)
	_ = resourceData.Set("domain_suffix", domainSuffix)

	// Expect calls to create _and_ update the recording.
	testClient.EXPECT().CreateEnvironment(
		serviceSid,
		&CreateEnvironmentParams{
			UniqueName: &uniqueName,
			DomainSuffix: &domainSuffix,
		},
	).Return(serviceEnvironment, nil)

	resource.CreateContext(nil, resourceData, config)

	// Assert API response was successfully marshaled.
	assert.Equal(t, environmentId, resourceData.Id())
	assert.Equal(t, serviceSid, resourceData.Get("service_sid"))
	assert.Equal(t, environmentSid, resourceData.Get("sid"))
}

func TestImportServiceEnvironment(t *testing.T) {
	setupServiceEnvironment(t)

	resourceData.SetId(environmentId)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert no errors and the ID was properly parsed.
	assert.Nil(t, err)
	assert.Equal(t, serviceSid, resourceData.Get("service_sid"))
	assert.Equal(t, environmentSid, resourceData.Get("sid"))
}

func TestImportInvalidServiceEnvironment(t *testing.T) {
	setupServiceEnvironment(t)

	resourceData.SetId(serviceSid)

	_, err := resource.Importer.StateContext(nil, resourceData, nil)

	// Assert invalid error is present.
	assert.NotNil(t, err)
	assert.Regexp(t, "invalid", err.Error())
}

type ExpectedParamSchema struct {
	Required bool
	ForceNew bool
	Computed bool
	Optional bool
}

func TestSchemaServiceEnvironment(t *testing.T) {
	testCases := map[string]ExpectedParamSchema{
		"service_sid": {true, true, false, false},
		"unique_name": {true, true, false, false},
		"sid": {false, false, true, false},
		"domain_suffix": {false, true, true, true},
		"date_created": {false, false, true, false},
	}

	assert.Contains(t, resource.Schema, "date_created")
	for paramName, paramSchema := range resource.Schema {
		if expectedParams, ok := testCases[paramName]; ok {
			assert.Equal(t, expectedParams.Required, paramSchema.Required, fmt.Sprintf("schema.Required iff service_sid or unique_name: %s", paramName))
			assert.Equal(t, expectedParams.ForceNew, paramSchema.ForceNew, fmt.Sprintf("schema.ForceNew iff service_sid or unique_name or domain_suffix: %s", paramName))
			assert.Equal(t, expectedParams.Computed, paramSchema.Computed, fmt.Sprintf("schema.Computed iff not service_sid or unique_name: %s", paramName))
			assert.Equal(t, expectedParams.Optional, paramSchema.Optional, fmt.Sprintf("schema.Optional iff param and not sid or service_sid or unique_name: %s", paramName))
		}
	}
}
