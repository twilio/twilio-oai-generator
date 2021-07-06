package client

import (
	v2010 "twilio-oai-generator/go/rest/api/v2010"
)

// Config is provided as context to the underlying resources.
type Config struct {
	Client *RestClient
}

type RestClient struct {
	ApiV2010
}

type ApiV2010 interface {
	CreateCredentialAws(params *v2010.CreateCredentialAwsParams) (*v2010.AccountsV1CredentialCredentialAws, error)
	DeleteCredentialAws(Sid string) error
	FetchCredentialAws(Sid string) (*v2010.AccountsV1CredentialCredentialAws, error)
	UpdateCredentialAws(Sid string, params *v2010.UpdateCredentialAwsParams) (*v2010.AccountsV1CredentialCredentialAws, error)

	CreateCallRecording(CallSid string, params *v2010.CreateCallRecordingParams) (*v2010.ApiV2010AccountCallCallRecording, error)
	DeleteCallRecording(CallSid string, Sid int, params *v2010.DeleteCallRecordingParams) error
	FetchCallRecording(CallSid string, Sid int, params *v2010.FetchCallRecordingParams) (*v2010.ApiV2010AccountCallCallRecording, error)
	UpdateCallRecording(CallSid string, Sid int, params *v2010.UpdateCallRecordingParams) (*v2010.ApiV2010AccountCallCallRecording, error)
}
