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


// Optional parameters for the method 'CreateMessage'
type CreateMessageParams struct {
    // 
    PathAccountSid *string `json:"PathAccountSid,omitempty"`
    // Determines if the address can be stored or obfuscated based on privacy settings
    AddressRetention *string `json:"AddressRetention,omitempty"`
    // 
    ApplicationSid *string `json:"ApplicationSid,omitempty"`
    // 
    Attempt *int `json:"Attempt,omitempty"`
    // 
    Body *string `json:"Body,omitempty"`
    // 
    ContentRetention *string `json:"ContentRetention,omitempty"`
    // Reserved
    ForceDelivery *bool `json:"ForceDelivery,omitempty"`
    // 
    From *string `json:"From,omitempty"`
    // 
    MaxPrice *float32 `json:"MaxPrice,omitempty"`
    // 
    MediaUrl *[]string `json:"MediaUrl,omitempty"`
    // 
    MessagingServiceSid *string `json:"MessagingServiceSid,omitempty"`
    // Rich actions for Channels Messages.
    PersistentAction *[]string `json:"PersistentAction,omitempty"`
    // 
    ProvideFeedback *bool `json:"ProvideFeedback,omitempty"`
    // 
    SmartEncoded *bool `json:"SmartEncoded,omitempty"`
    // 
    StatusCallback *string `json:"StatusCallback,omitempty"`
    // 
    To *string `json:"To,omitempty"`
    // 
    ValidityPeriod *int `json:"ValidityPeriod,omitempty"`
}

func (params *CreateMessageParams) SetPathAccountSid(PathAccountSid string) (*CreateMessageParams){
    params.PathAccountSid = &PathAccountSid
    return params
}
func (params *CreateMessageParams) SetAddressRetention(AddressRetention string) (*CreateMessageParams){
    params.AddressRetention = &AddressRetention
    return params
}
func (params *CreateMessageParams) SetApplicationSid(ApplicationSid string) (*CreateMessageParams){
    params.ApplicationSid = &ApplicationSid
    return params
}
func (params *CreateMessageParams) SetAttempt(Attempt int) (*CreateMessageParams){
    params.Attempt = &Attempt
    return params
}
func (params *CreateMessageParams) SetBody(Body string) (*CreateMessageParams){
    params.Body = &Body
    return params
}
func (params *CreateMessageParams) SetContentRetention(ContentRetention string) (*CreateMessageParams){
    params.ContentRetention = &ContentRetention
    return params
}
func (params *CreateMessageParams) SetForceDelivery(ForceDelivery bool) (*CreateMessageParams){
    params.ForceDelivery = &ForceDelivery
    return params
}
func (params *CreateMessageParams) SetFrom(From string) (*CreateMessageParams){
    params.From = &From
    return params
}
func (params *CreateMessageParams) SetMaxPrice(MaxPrice float32) (*CreateMessageParams){
    params.MaxPrice = &MaxPrice
    return params
}
func (params *CreateMessageParams) SetMediaUrl(MediaUrl []string) (*CreateMessageParams){
    params.MediaUrl = &MediaUrl
    return params
}
func (params *CreateMessageParams) SetMessagingServiceSid(MessagingServiceSid string) (*CreateMessageParams){
    params.MessagingServiceSid = &MessagingServiceSid
    return params
}
func (params *CreateMessageParams) SetPersistentAction(PersistentAction []string) (*CreateMessageParams){
    params.PersistentAction = &PersistentAction
    return params
}
func (params *CreateMessageParams) SetProvideFeedback(ProvideFeedback bool) (*CreateMessageParams){
    params.ProvideFeedback = &ProvideFeedback
    return params
}
func (params *CreateMessageParams) SetSmartEncoded(SmartEncoded bool) (*CreateMessageParams){
    params.SmartEncoded = &SmartEncoded
    return params
}
func (params *CreateMessageParams) SetStatusCallback(StatusCallback string) (*CreateMessageParams){
    params.StatusCallback = &StatusCallback
    return params
}
func (params *CreateMessageParams) SetTo(To string) (*CreateMessageParams){
    params.To = &To
    return params
}
func (params *CreateMessageParams) SetValidityPeriod(ValidityPeriod int) (*CreateMessageParams){
    params.ValidityPeriod = &ValidityPeriod
    return params
}

// Send a message from the account used to make the request
func (c *ApiService) CreateMessage(params *CreateMessageParams) (*ApiV2010Message, error) {
    path := "/2010-04-01/Accounts/{AccountSid}/Messages.json"
    if params != nil && params.PathAccountSid != nil {
    path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
} else {
    path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
}

data := url.Values{}
headers := make(map[string]interface{})

if params != nil && params.AddressRetention != nil {
    data.Set("AddressRetention", *params.AddressRetention)
}
if params != nil && params.ApplicationSid != nil {
    data.Set("ApplicationSid", *params.ApplicationSid)
}
if params != nil && params.Attempt != nil {
    data.Set("Attempt", fmt.Sprint(*params.Attempt))
}
if params != nil && params.Body != nil {
    data.Set("Body", *params.Body)
}
if params != nil && params.ContentRetention != nil {
    data.Set("ContentRetention", *params.ContentRetention)
}
if params != nil && params.ForceDelivery != nil {
    data.Set("ForceDelivery", fmt.Sprint(*params.ForceDelivery))
}
if params != nil && params.From != nil {
    data.Set("From", *params.From)
}
if params != nil && params.MaxPrice != nil {
    data.Set("MaxPrice", fmt.Sprint(*params.MaxPrice))
}
if params != nil && params.MediaUrl != nil {
    for _, item  := range *params.MediaUrl {
        data.Add("MediaUrl", item)
    }
}
if params != nil && params.MessagingServiceSid != nil {
    data.Set("MessagingServiceSid", *params.MessagingServiceSid)
}
if params != nil && params.PersistentAction != nil {
    for _, item  := range *params.PersistentAction {
        data.Add("PersistentAction", item)
    }
}
if params != nil && params.ProvideFeedback != nil {
    data.Set("ProvideFeedback", fmt.Sprint(*params.ProvideFeedback))
}
if params != nil && params.SmartEncoded != nil {
    data.Set("SmartEncoded", fmt.Sprint(*params.SmartEncoded))
}
if params != nil && params.StatusCallback != nil {
    data.Set("StatusCallback", *params.StatusCallback)
}
if params != nil && params.To != nil {
    data.Set("To", *params.To)
}
if params != nil && params.ValidityPeriod != nil {
    data.Set("ValidityPeriod", fmt.Sprint(*params.ValidityPeriod))
}



    resp, err := c.requestHandler.Post(c.baseURL+path, data, headers)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ApiV2010Message{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Optional parameters for the method 'DeleteMessage'
type DeleteMessageParams struct {
    // 
    PathAccountSid *string `json:"PathAccountSid,omitempty"`
}

func (params *DeleteMessageParams) SetPathAccountSid(PathAccountSid string) (*DeleteMessageParams){
    params.PathAccountSid = &PathAccountSid
    return params
}

// Deletes a message record from your account
func (c *ApiService) DeleteMessage(Sid string, params *DeleteMessageParams) (error) {
    path := "/2010-04-01/Accounts/{AccountSid}/Messages/{Sid}.json"
    if params != nil && params.PathAccountSid != nil {
    path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
} else {
    path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
}
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

// Optional parameters for the method 'FetchMessage'
type FetchMessageParams struct {
    // 
    PathAccountSid *string `json:"PathAccountSid,omitempty"`
}

func (params *FetchMessageParams) SetPathAccountSid(PathAccountSid string) (*FetchMessageParams){
    params.PathAccountSid = &PathAccountSid
    return params
}

// Fetch a message belonging to the account used to make the request
func (c *ApiService) FetchMessage(Sid string, params *FetchMessageParams) (*ApiV2010Message, error) {
    path := "/2010-04-01/Accounts/{AccountSid}/Messages/{Sid}.json"
    if params != nil && params.PathAccountSid != nil {
    path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
} else {
    path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
}
    path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

data := url.Values{}
headers := make(map[string]interface{})




    resp, err := c.requestHandler.Get(c.baseURL+path, data, headers)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ApiV2010Message{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Optional parameters for the method 'ListMessage'
type ListMessageParams struct {
    // 
    PathAccountSid *string `json:"PathAccountSid,omitempty"`
    // Read messages sent to only this phone number.
    To *string `json:"To,omitempty"`
    // 
    From *string `json:"From,omitempty"`
    // 
    DateSent *time.Time `json:"DateSent,omitempty"`
    // 
    DateSentBefore *time.Time `json:"DateSent&lt;,omitempty"`
    // 
    DateSentAfter *time.Time `json:"DateSent&gt;,omitempty"`
    // 
    PageSize *int `json:"PageSize,omitempty"`
    // Max number of records to return.
    Limit *int `json:"limit,omitempty"`
}

func (params *ListMessageParams) SetPathAccountSid(PathAccountSid string) (*ListMessageParams){
    params.PathAccountSid = &PathAccountSid
    return params
}
func (params *ListMessageParams) SetTo(To string) (*ListMessageParams){
    params.To = &To
    return params
}
func (params *ListMessageParams) SetFrom(From string) (*ListMessageParams){
    params.From = &From
    return params
}
func (params *ListMessageParams) SetDateSent(DateSent time.Time) (*ListMessageParams){
    params.DateSent = &DateSent
    return params
}
func (params *ListMessageParams) SetDateSentBefore(DateSentBefore time.Time) (*ListMessageParams){
    params.DateSentBefore = &DateSentBefore
    return params
}
func (params *ListMessageParams) SetDateSentAfter(DateSentAfter time.Time) (*ListMessageParams){
    params.DateSentAfter = &DateSentAfter
    return params
}
func (params *ListMessageParams) SetPageSize(PageSize int) (*ListMessageParams){
    params.PageSize = &PageSize
    return params
}
func (params *ListMessageParams) SetLimit(Limit int) (*ListMessageParams){
    params.Limit = &Limit
    return params
}

// Retrieve a single page of Message records from the API. Request is executed immediately.
func (c *ApiService) PageMessage(params *ListMessageParams, pageToken, pageNumber string) (*ListMessageResponse, error) {
    path := "/2010-04-01/Accounts/{AccountSid}/Messages.json"

    if params != nil && params.PathAccountSid != nil {
    path = strings.Replace(path, "{"+"AccountSid"+"}", *params.PathAccountSid, -1)
} else {
    path = strings.Replace(path, "{"+"AccountSid"+"}", c.requestHandler.Client.AccountSid(), -1)
}

data := url.Values{}
headers := make(map[string]interface{})

if params != nil && params.To != nil {
    data.Set("To", *params.To)
}
if params != nil && params.From != nil {
    data.Set("From", *params.From)
}
if params != nil && params.DateSent != nil {
    data.Set("DateSent", fmt.Sprint((*params.DateSent).Format(time.RFC3339)))
}
if params != nil && params.DateSentBefore != nil {
    data.Set("DateSent<", fmt.Sprint((*params.DateSentBefore).Format(time.RFC3339)))
}
if params != nil && params.DateSentAfter != nil {
    data.Set("DateSent>", fmt.Sprint((*params.DateSentAfter).Format(time.RFC3339)))
}
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

    ps := &ListMessageResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Lists Message records from the API as a list. Unlike stream, this operation is eager and loads 'limit' records into memory before returning.
func (c *ApiService) ListMessage(params *ListMessageParams) ([]ApiV2010Message, error) {
    if params == nil {
        params = &ListMessageParams{}
    }
    params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

    response, err := c.PageMessage(params, "", "")
    if err != nil {
        return nil, err
    }

    curRecord := 0
    var records []ApiV2010Message

    for response != nil {
        records = append(records, response.Messages...)

        var record interface{}
        if record, err = client.GetNext(c.baseURL, response, &curRecord, params.Limit, c.getNextListMessageResponse); record == nil || err != nil {
            return records, err
        }

        response = record.(*ListMessageResponse)
    }

    return records, err
}

// Streams Message records from the API as a channel stream. This operation lazily loads records as efficiently as possible until the limit is reached.
func (c *ApiService) StreamMessage(params *ListMessageParams) (chan ApiV2010Message, error) {
    if params == nil {
        params = &ListMessageParams{}
    }
    params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

    response, err := c.PageMessage(params, "", "")
    if err != nil {
        return nil, err
    }

    curRecord := 0
    //set buffer size of the channel to 1
    channel := make(chan ApiV2010Message, 1)

    go func() {
        for response != nil {
            for item := range response.Messages {
                channel <- response.Messages[item]
            }


            var record interface{}
            if record, err = client.GetNext(c.baseURL, response, &curRecord, params.Limit, c.getNextListMessageResponse); record == nil || err != nil {
                close(channel)
                return
            }

            response = record.(*ListMessageResponse)
        }
        close(channel)
    }()

    return channel, err
}

func (c *ApiService) getNextListMessageResponse(nextPageUrl string) (interface{}, error) {
    if nextPageUrl == "" {
        return nil, nil
    }
    resp, err := c.requestHandler.Get(nextPageUrl, nil, nil)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ListMessageResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }
    return ps, nil
}

