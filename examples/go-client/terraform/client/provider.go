package client

import (
	v2010 "go-client/helper/rest/api/v2010"
)

// Config is provided as context to the underlying resources.
type Config struct {
	Client *RestClient
}

type RestClient struct {
	ApiV2010
}

type ApiV2010 interface {
	CreateCredentialAws(params *v2010.CreateCredentialAwsParams) (*v2010.TestResponseObject, error)
	DeleteCredentialAws(Sid string) error
	FetchCredentialAws(Sid string) (*v2010.TestResponseObject, error)
	UpdateCredentialAws(Sid string, params *v2010.UpdateCredentialAwsParams) (*v2010.TestResponseObject, error)

	CreateCallRecording(CallSid string, params *v2010.CreateCallRecordingParams) (*v2010.TestResponseObject, error)
	DeleteCallRecording(CallSid string, Sid int, params *v2010.DeleteCallRecordingParams) error
	FetchCallRecording(CallSid string, Sid int, params *v2010.FetchCallRecordingParams) (*v2010.TestResponseObject, error)
	UpdateCallRecording(CallSid string, Sid int, params *v2010.UpdateCallRecordingParams) (*v2010.TestResponseObject, error)

	CreateMessage(params *v2010.CreateMessageParams) (*v2010.TestResponseObject, error)
	DeleteMessage(Sid string, params *v2010.DeleteMessageParams) error
	FetchMessage(Sid string, params *v2010.FetchMessageParams) (*v2010.TestResponseObject, error)
}
