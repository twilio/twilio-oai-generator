module twilio-oai-generator/terraform

go 1.16

replace twilio-oai-generator/go => ../go

require (
	github.com/golang/mock v1.6.0
	github.com/hashicorp/terraform-plugin-sdk/v2 v2.7.0
	github.com/stretchr/testify v1.7.0
	github.com/twilio/terraform-provider-twilio v0.3.0
	twilio-oai-generator/go v0.0.0
)
