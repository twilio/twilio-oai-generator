/*
    * Twilio - Accounts
    *
    * This is the public Twilio REST API.
    *
    * API version: 1.11.0
    * Contact: support@twilio.com
*/

// Code generated by OpenAPI Generator (https://openapi-generator.tech); DO NOT EDIT.

package openapi

import (
	"encoding/json"
	"fmt"
	"net/url"

    "github.com/twilio/twilio-go/client"
)


// Optional parameters for the method 'CreateEnvironment'
type CreateEnvironmentParams struct {
    // 
    DomainSuffix *string `json:"DomainSuffix,omitempty"`
    // 
    UniqueName *string `json:"UniqueName,omitempty"`
}

func (params *CreateEnvironmentParams) SetDomainSuffix(DomainSuffix string) (*CreateEnvironmentParams){
    params.DomainSuffix = &DomainSuffix
    return params
}
func (params *CreateEnvironmentParams) SetUniqueName(UniqueName string) (*CreateEnvironmentParams){
    params.UniqueName = &UniqueName
    return params
}

// Create a new environment.
func (c *ApiService) CreateEnvironment(ServiceSid string, params *CreateEnvironmentParams) (*ServerlessV1Environment, error) {
    path := "/v1/Services/{ServiceSid}/Environments"
        path = strings.Replace(path, "{"+"ServiceSid"+"}", ServiceSid, -1)

data := url.Values{}
headers := make(map[string]interface{})

if params != nil && params.DomainSuffix != nil {
    data.Set("DomainSuffix", *params.DomainSuffix)
}
if params != nil && params.UniqueName != nil {
    data.Set("UniqueName", *params.UniqueName)
}



    resp, err := c.requestHandler.Post(c.baseURL+path, data, headers)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ServerlessV1Environment{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Delete a specific environment.
func (c *ApiService) DeleteEnvironment(ServiceSid string, Sid string, ) (error) {
    path := "/v1/Services/{ServiceSid}/Environments/{Sid}"
        path = strings.Replace(path, "{"+"ServiceSid"+"}", ServiceSid, -1)
    path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

data := url.Values{}
headers := make(map[string]interface{})




    resp, err := c.requestHandler.Delete(c.baseURL+path, data, headers)
    if err != nil {
        return err
    }

    defer resp.Body.Close()

    return nil
}

// Retrieve a specific environment.
func (c *ApiService) FetchEnvironment(ServiceSid string, Sid string, ) (*ServerlessV1Environment, error) {
    path := "/v1/Services/{ServiceSid}/Environments/{Sid}"
        path = strings.Replace(path, "{"+"ServiceSid"+"}", ServiceSid, -1)
    path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

data := url.Values{}
headers := make(map[string]interface{})




    resp, err := c.requestHandler.Get(c.baseURL+path, data, headers)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ServerlessV1Environment{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Optional parameters for the method 'ListEnvironment'
type ListEnvironmentParams struct {
    // How many resources to return in each list page. The default is 50, and the maximum is 1000.
    PageSize *int `json:"PageSize,omitempty"`
    // Max number of records to return.
    Limit *int `json:"limit,omitempty"`
}

func (params *ListEnvironmentParams) SetPageSize(PageSize int) (*ListEnvironmentParams){
    params.PageSize = &PageSize
    return params
}
func (params *ListEnvironmentParams) SetLimit(Limit int) (*ListEnvironmentParams){
    params.Limit = &Limit
    return params
}

// Retrieve a single page of Environment records from the API. Request is executed immediately.
func (c *ApiService) PageEnvironment(ServiceSid string, params *ListEnvironmentParams, pageToken, pageNumber string) (*ListEnvironmentResponse, error) {
    path := "/v1/Services/{ServiceSid}/Environments"

        path = strings.Replace(path, "{"+"ServiceSid"+"}", ServiceSid, -1)

data := url.Values{}
headers := make(map[string]interface{})

if params != nil && params.PageSize != nil {
    data.Set("PageSize", fmt.Sprint(*params.PageSize))
}

    if pageToken != "" {
        data.Set("PageToken", pageToken)
    }
    if pageNumber != "" {
        data.Set("Page", pageNumber)
    }

    resp, err := c.requestHandler.Get(c.baseURL+path, data, headers)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ListEnvironmentResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Lists Environment records from the API as a list. Unlike stream, this operation is eager and loads 'limit' records into memory before returning.
func (c *ApiService) ListEnvironment(ServiceSid string, params *ListEnvironmentParams) ([]ServerlessV1Environment, error) {
    if params == nil {
        params = &ListEnvironmentParams{}
    }
    params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

    response, err := c.PageEnvironment(ServiceSid, params, "", "")
    if err != nil {
        return nil, err
    }

    curRecord := 0
    var records []ServerlessV1Environment

    for response != nil {
        records = append(records, response.Environments...)

        var record interface{}
        if record, err = client.GetNext(c.baseURL, response, &curRecord, params.Limit, c.getNextListEnvironmentResponse); record == nil || err != nil {
            return records, err
        }

        response = record.(*ListEnvironmentResponse)
    }

    return records, err
}

// Streams Environment records from the API as a channel stream. This operation lazily loads records as efficiently as possible until the limit is reached.
func (c *ApiService) StreamEnvironment(ServiceSid string, params *ListEnvironmentParams) (chan ServerlessV1Environment, error) {
    if params == nil {
        params = &ListEnvironmentParams{}
    }
    params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

    response, err := c.PageEnvironment(ServiceSid, params, "", "")
    if err != nil {
        return nil, err
    }

    curRecord := 0
    //set buffer size of the channel to 1
    channel := make(chan ServerlessV1Environment, 1)

    go func() {
        for response != nil {
            for item := range response.Environments {
                channel <- response.Environments[item]
            }


            var record interface{}
            if record, err = client.GetNext(c.baseURL, response, &curRecord, params.Limit, c.getNextListEnvironmentResponse); record == nil || err != nil {
                close(channel)
                return
            }

            response = record.(*ListEnvironmentResponse)
        }
        close(channel)
    }()

    return channel, err
}

func (c *ApiService) getNextListEnvironmentResponse(nextPageUrl string) (interface{}, error) {
    if nextPageUrl == "" {
        return nil, nil
    }
    resp, err := c.requestHandler.Get(nextPageUrl, nil, nil)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ListEnvironmentResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }
    return ps, nil
}

