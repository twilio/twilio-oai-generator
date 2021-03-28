package client

import (
	"net/http"
	"net/url"
	"time"
)

type BaseClient interface {
	SetTimeout(timeout time.Duration)
	SendRequest(method string, rawURL string, queryParams interface{}, formData url.Values) (*http.Response, error)
	Post(path string, bodyData url.Values, headers interface{}) (*http.Response, error)
	Get(path string, queryData interface{}, headers interface{}) (*http.Response, error)
	Delete(path string, nothing interface{}, headers interface{}) (*http.Response, error)
}
