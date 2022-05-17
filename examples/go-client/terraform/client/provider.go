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

	CreateAccount(params *v2010.CreateAccountParams) (*v2010.TestResponseObject, error)
	DeleteAccount(Sid string) error
	FetchAccount(Sid string) (*v2010.TestResponseObject, error)
	UpdateAccount(Sid string, params *v2010.UpdateAccountParams) (*v2010.TestResponseObject, error)

	CreateCall(params *v2010.CreateCallParams) (*v2010.TestResponseObject, error)
	DeleteCall(Sid int, params *v2010.DeleteCallParams) error
	FetchCall(Sid int, params *v2010.FetchCallParams) (*v2010.TestResponseObject, error)
}
