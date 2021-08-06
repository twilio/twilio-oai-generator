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
	CreateCredentialAws(params *v2010.CreateCredentialAwsParams) (*v2010.AccountsV1CredentialAws, error)
	DeleteCredentialAws(Sid string) error
	FetchCredentialAws(Sid string) (*v2010.AccountsV1CredentialAws, error)
	UpdateCredentialAws(Sid string, params *v2010.UpdateCredentialAwsParams) (*v2010.AccountsV1CredentialAws, error)

	CreateCallRecording(CallSid string, params *v2010.CreateCallRecordingParams) (*v2010.ApiV2010CallRecording, error)
	DeleteCallRecording(CallSid string, Sid int, params *v2010.DeleteCallRecordingParams) error
	FetchCallRecording(CallSid string, Sid int, params *v2010.FetchCallRecordingParams) (*v2010.ApiV2010CallRecording, error)
	UpdateCallRecording(CallSid string, Sid int, params *v2010.UpdateCallRecordingParams) (*v2010.ApiV2010CallRecording, error)

	CreateMessage(params *v2010.CreateMessageParams) (*v2010.ApiV2010Message, error)
	DeleteMessage(Sid string, params *v2010.DeleteMessageParams) error
	FetchMessage(Sid string, params *v2010.FetchMessageParams) (*v2010.ApiV2010Message, error)
	UpdateMessage(Sid string, params *v2010.UpdateMessageParams) (*v2010.ApiV2010Message, error)
}
