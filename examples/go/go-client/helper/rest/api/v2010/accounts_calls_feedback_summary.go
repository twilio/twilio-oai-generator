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
	"fmt"
	"net/url"

    "github.com/twilio/twilio-go/client"
)


// Optional parameters for the method 'UpdateCallFeedbackSummary'
type UpdateCallFeedbackSummaryParams struct {
    // 
    PathAccountSid *string `json:"PathAccountSid,omitempty"`
    // 
    AccountSid *string `json:"AccountSid,omitempty"`
    // 
    EndDate *string `json:"EndDate,omitempty"`
    // 
    StartDate *string `json:"StartDate,omitempty"`
}

func (params *UpdateCallFeedbackSummaryParams) SetPathAccountSid(PathAccountSid string) (*UpdateCallFeedbackSummaryParams){
    params.PathAccountSid = &PathAccountSid
    return params
}
func (params *UpdateCallFeedbackSummaryParams) SetAccountSid(AccountSid string) (*UpdateCallFeedbackSummaryParams){
    params.AccountSid = &AccountSid
    return params
}
func (params *UpdateCallFeedbackSummaryParams) SetEndDate(EndDate string) (*UpdateCallFeedbackSummaryParams){
    params.EndDate = &EndDate
    return params
}
func (params *UpdateCallFeedbackSummaryParams) SetStartDate(StartDate string) (*UpdateCallFeedbackSummaryParams){
    params.StartDate = &StartDate
    return params
}

func (c *ApiService) UpdateCallFeedbackSummary(Sid string, params *UpdateCallFeedbackSummaryParams) (*TestResponseObject, error) {
    path := "/2010-04-01/Accounts/{AccountSid}/Calls/Feedback/Summary/{Sid}.json"
    if params != nil && params.PathAccountSid != nil {
    path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
} else {
    path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
}
    path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

    data := url.Values{}
    headers := make(map[string]interface{})
if params != nil && params.AccountSid != nil {
    data.Set("AccountSid", *params.AccountSid)
}
if params != nil && params.EndDate != nil {
    data.Set("EndDate", fmt.Sprint(*params.EndDate))
}
if params != nil && params.StartDate != nil {
    data.Set("StartDate", fmt.Sprint(*params.StartDate))
}



    resp, err := c.requestHandler.Post(c.baseURL+path, data, headers)
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
