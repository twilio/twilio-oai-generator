/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package openapi

import (
	"context"
	"encoding/json"
	"fmt"
	"net/url"
	"strings"
)

// Optional parameters for the method 'CreateCall'
type CreateCallParams struct {
	//
	PathAccountSid *string `json:"PathAccountSid,omitempty"`
	//
	RequiredStringProperty *string `json:"RequiredStringProperty,omitempty"`
	//
	TestArrayOfStrings *[]string `json:"TestArrayOfStrings,omitempty"`
	//
	TestArrayOfUri *[]string `json:"TestArrayOfUri,omitempty"`
	// The HTTP method that we should use to request the `TestArrayOfUri`.
	TestMethod *string `json:"TestMethod,omitempty"`
}

func (params *CreateCallParams) SetPathAccountSid(PathAccountSid string) *CreateCallParams {
	params.PathAccountSid = &PathAccountSid
	return params
}
func (params *CreateCallParams) SetRequiredStringProperty(RequiredStringProperty string) *CreateCallParams {
	params.RequiredStringProperty = &RequiredStringProperty
	return params
}
func (params *CreateCallParams) SetTestArrayOfStrings(TestArrayOfStrings []string) *CreateCallParams {
	params.TestArrayOfStrings = &TestArrayOfStrings
	return params
}
func (params *CreateCallParams) SetTestArrayOfUri(TestArrayOfUri []string) *CreateCallParams {
	params.TestArrayOfUri = &TestArrayOfUri
	return params
}
func (params *CreateCallParams) SetTestMethod(TestMethod string) *CreateCallParams {
	params.TestMethod = &TestMethod
	return params
}

func (c *ApiService) CreateCall(params *CreateCallParams) (*TestResponseObject, error) {
	return c.CreateCallWithCtx(context.TODO(), params)
}

func (c *ApiService) CreateCallWithCtx(ctx context.Context, params *CreateCallParams) (*TestResponseObject, error) {
	path := "/2010-04-01/Accounts/{AccountSid}/Calls.json"
	if params != nil && params.PathAccountSid != nil {
		path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
	} else {
		path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
	}

	data := url.Values{}
	headers := make(map[string]interface{})

	if params != nil && params.RequiredStringProperty != nil {
		data.Set("RequiredStringProperty", *params.RequiredStringProperty)
	}
	if params != nil && params.TestArrayOfStrings != nil {
		for _, item := range *params.TestArrayOfStrings {
			data.Add("TestArrayOfStrings", item)
		}
	}
	if params != nil && params.TestArrayOfUri != nil {
		for _, item := range *params.TestArrayOfUri {
			data.Add("TestArrayOfUri", item)
		}
	}
	if params != nil && params.TestMethod != nil {
		data.Set("TestMethod", *params.TestMethod)
	}

	resp, err := c.requestHandler.Post(ctx, c.baseURL+path, data, headers)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()

	ps := &TestResponseObject{}
	if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
		return nil, err
	}

	return ps, err
}

// Optional parameters for the method 'DeleteCall'
type DeleteCallParams struct {
	//
	PathAccountSid *string `json:"PathAccountSid,omitempty"`
}

func (params *DeleteCallParams) SetPathAccountSid(PathAccountSid string) *DeleteCallParams {
	params.PathAccountSid = &PathAccountSid
	return params
}

func (c *ApiService) DeleteCall(TestInteger int, params *DeleteCallParams) error {
	return c.DeleteCallWithCtx(context.TODO(), TestInteger, params)
}

func (c *ApiService) DeleteCallWithCtx(ctx context.Context, TestInteger int, params *DeleteCallParams) error {
	path := "/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json"
	if params != nil && params.PathAccountSid != nil {
		path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
	} else {
		path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
	}
	path = strings.Replace(path, "{"+"TestInteger"+"}", fmt.Sprint(TestInteger), -1)

	data := url.Values{}
	headers := make(map[string]interface{})

	resp, err := c.requestHandler.Delete(ctx, c.baseURL+path, data, headers)
	if err != nil {
		return err
	}

	defer resp.Body.Close()

	return nil
}

// Optional parameters for the method 'FetchCall'
type FetchCallParams struct {
	//
	PathAccountSid *string `json:"PathAccountSid,omitempty"`
}

func (params *FetchCallParams) SetPathAccountSid(PathAccountSid string) *FetchCallParams {
	params.PathAccountSid = &PathAccountSid
	return params
}

func (c *ApiService) FetchCall(TestInteger int, params *FetchCallParams) (*TestResponseObject, error) {
	return c.FetchCallWithCtx(context.TODO(), TestInteger, params)
}

func (c *ApiService) FetchCallWithCtx(ctx context.Context, TestInteger int, params *FetchCallParams) (*TestResponseObject, error) {
	path := "/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json"
	if params != nil && params.PathAccountSid != nil {
		path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
	} else {
		path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
	}
	path = strings.Replace(path, "{"+"TestInteger"+"}", fmt.Sprint(TestInteger), -1)

	data := url.Values{}
	headers := make(map[string]interface{})

	resp, err := c.requestHandler.Get(ctx, c.baseURL+path, data, headers)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()

	ps := &TestResponseObject{}
	if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
		return nil, err
	}

	return ps, err
}
