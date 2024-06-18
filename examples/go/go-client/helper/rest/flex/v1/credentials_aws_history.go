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
	"encoding/json"
	"net/url"
	"strings"
)

// Optional parameters for the method 'FetchCredentialHistory'
type FetchCredentialHistoryParams struct {
	//
	AddOnsData *map[string]interface{} `json:"AddOnsData,omitempty"`
}

func (params *FetchCredentialHistoryParams) SetAddOnsData(AddOnsData map[string]interface{}) *FetchCredentialHistoryParams {
	params.AddOnsData = &AddOnsData
	return params
}

func (c *ApiService) FetchCredentialHistory(Sid string, params *FetchCredentialHistoryParams) (*TestResponseObject, error) {
	path := "/v1/Credentials/AWS/{Sid}/History"
	path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

	data := url.Values{}
	headers := map[string]interface{}{
		"Content-Type": "application/x-www-form-urlencoded",
	}

	if params != nil && params.AddOnsData != nil {
		v, err := json.Marshal(params.AddOnsData)

		if err != nil {
			return nil, err
		}

		data.Set("AddOnsData", string(v))
	}

	resp, err := c.requestHandler.Get(c.baseURL+path, data, headers)
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
