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
	"strings"
	"time"

	"github.com/twilio/twilio-go/client"
)

// Optional parameters for the method 'CreateAccount'
type CreateAccountParams struct {
	//
	XTwilioWebhookEnabled *string `json:"X-Twilio-Webhook-Enabled,omitempty"`
	//
	RecordingStatusCallback *string `json:"RecordingStatusCallback,omitempty"`
	//
	RecordingStatusCallbackEvent *[]string `json:"RecordingStatusCallbackEvent,omitempty"`
	//
	Twiml *string `json:"Twiml,omitempty"`
}

func (params *CreateAccountParams) SetXTwilioWebhookEnabled(XTwilioWebhookEnabled string) *CreateAccountParams {
	params.XTwilioWebhookEnabled = &XTwilioWebhookEnabled
	return params
}
func (params *CreateAccountParams) SetRecordingStatusCallback(RecordingStatusCallback string) *CreateAccountParams {
	params.RecordingStatusCallback = &RecordingStatusCallback
	return params
}
func (params *CreateAccountParams) SetRecordingStatusCallbackEvent(RecordingStatusCallbackEvent []string) *CreateAccountParams {
	params.RecordingStatusCallbackEvent = &RecordingStatusCallbackEvent
	return params
}
func (params *CreateAccountParams) SetTwiml(Twiml string) *CreateAccountParams {
	params.Twiml = &Twiml
	return params
}

func (c *ApiService) CreateAccount(params *CreateAccountParams) (*TestResponseObject, error) {
	path := "/2010-04-01/Accounts.json"

	data := url.Values{}
	headers := make(map[string]interface{})

	if params != nil && params.RecordingStatusCallback != nil {
		data.Set("RecordingStatusCallback", *params.RecordingStatusCallback)
	}
	if params != nil && params.RecordingStatusCallbackEvent != nil {
		for _, item := range *params.RecordingStatusCallbackEvent {
			data.Add("RecordingStatusCallbackEvent", item)
		}
	}
	if params != nil && params.Twiml != nil {
		data.Set("Twiml", *params.Twiml)
	}

	if params != nil && params.XTwilioWebhookEnabled != nil {
		headers["X-Twilio-Webhook-Enabled"] = *params.XTwilioWebhookEnabled
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

func (c *ApiService) DeleteAccount(Sid string) error {
	path := "/2010-04-01/Accounts/{Sid}.json"
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

func (c *ApiService) FetchAccount(Sid string) (*TestResponseObject, error) {
	path := "/2010-04-01/Accounts/{Sid}.json"
	path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

	data := url.Values{}
	headers := make(map[string]interface{})

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

// Optional parameters for the method 'ListAccount'
type ListAccountParams struct {
	//
	DateCreated *time.Time `json:"DateCreated,omitempty"`
	//
	DateTest *string `json:"Date.Test,omitempty"`
	//
	DateCreatedBefore *time.Time `json:"DateCreated&lt;,omitempty"`
	//
	DateCreatedAfter *time.Time `json:"DateCreated&gt;,omitempty"`
	//
	PageSize *int `json:"PageSize,omitempty"`
	// Max number of records to return.
	Limit *int `json:"limit,omitempty"`
}

func (params *ListAccountParams) SetDateCreated(DateCreated time.Time) *ListAccountParams {
	params.DateCreated = &DateCreated
	return params
}
func (params *ListAccountParams) SetDateTest(DateTest string) *ListAccountParams {
	params.DateTest = &DateTest
	return params
}
func (params *ListAccountParams) SetDateCreatedBefore(DateCreatedBefore time.Time) *ListAccountParams {
	params.DateCreatedBefore = &DateCreatedBefore
	return params
}
func (params *ListAccountParams) SetDateCreatedAfter(DateCreatedAfter time.Time) *ListAccountParams {
	params.DateCreatedAfter = &DateCreatedAfter
	return params
}
func (params *ListAccountParams) SetPageSize(PageSize int) *ListAccountParams {
	params.PageSize = &PageSize
	return params
}
func (params *ListAccountParams) SetLimit(Limit int) *ListAccountParams {
	params.Limit = &Limit
	return params
}

// Retrieve a single page of Account records from the API. Request is executed immediately.
func (c *ApiService) PageAccount(params *ListAccountParams, pageToken, pageNumber string) (*ListAccountResponse, error) {
	path := "/2010-04-01/Accounts.json"

	data := url.Values{}
	headers := make(map[string]interface{})

	if params != nil && params.DateCreated != nil {
		data.Set("DateCreated", fmt.Sprint((*params.DateCreated).Format(time.RFC3339)))
	}
	if params != nil && params.DateTest != nil {
		data.Set("Date.Test", fmt.Sprint(*params.DateTest))
	}
	if params != nil && params.DateCreatedBefore != nil {
		data.Set("DateCreated<", fmt.Sprint((*params.DateCreatedBefore).Format(time.RFC3339)))
	}
	if params != nil && params.DateCreatedAfter != nil {
		data.Set("DateCreated>", fmt.Sprint((*params.DateCreatedAfter).Format(time.RFC3339)))
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

	ps := &ListAccountResponse{}
	if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
		return nil, err
	}

	return ps, err
}

// Lists Account records from the API as a list. Unlike stream, this operation is eager and loads 'limit' records into memory before returning.
func (c *ApiService) ListAccount(params *ListAccountParams) ([]TestResponseObject, error) {
	response, errors := c.StreamAccount(params)

	records := make([]TestResponseObject, 0)
	for record := range response {
		records = append(records, record)
	}

	if err := <-errors; err != nil {
		return nil, err
	}

	return records, nil
}

// Streams Account records from the API as a channel stream. This operation lazily loads records as efficiently as possible until the limit is reached.
func (c *ApiService) StreamAccount(params *ListAccountParams) (chan TestResponseObject, chan error) {
	if params == nil {
		params = &ListAccountParams{}
	}
	params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

	recordChannel := make(chan TestResponseObject, 1)
	errorChannel := make(chan error, 1)

	response, err := c.PageAccount(params, "", "")
	if err != nil {
		errorChannel <- err
		close(recordChannel)
		close(errorChannel)
	} else {
		go c.streamAccount(response, params, recordChannel, errorChannel)
	}

	return recordChannel, errorChannel
}

func (c *ApiService) streamAccount(response *ListAccountResponse, params *ListAccountParams, recordChannel chan TestResponseObject, errorChannel chan error) {
	curRecord := 1

	for response != nil {
		responseRecords := response.Accounts
		for item := range responseRecords {
			recordChannel <- responseRecords[item]
			curRecord += 1
			if params.Limit != nil && *params.Limit < curRecord {
				close(recordChannel)
				close(errorChannel)
				return
			}
		}

		record, err := client.GetNext(c.baseURL, response, c.getNextListAccountResponse)
		if err != nil {
			errorChannel <- err
			break
		} else if record == nil {
			break
		}

		response = record.(*ListAccountResponse)
	}

	close(recordChannel)
	close(errorChannel)
}

func (c *ApiService) getNextListAccountResponse(nextPageUrl string) (interface{}, error) {
	if nextPageUrl == "" {
		return nil, nil
	}
	resp, err := c.requestHandler.Get(nextPageUrl, nil, nil)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()

	ps := &ListAccountResponse{}
	if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
		return nil, err
	}
	return ps, nil
}

// Optional parameters for the method 'UpdateAccount'
type UpdateAccountParams struct {
	//
	PauseBehavior *string `json:"PauseBehavior,omitempty"`
	//
	Status *string `json:"Status,omitempty"`
}

func (params *UpdateAccountParams) SetPauseBehavior(PauseBehavior string) *UpdateAccountParams {
	params.PauseBehavior = &PauseBehavior
	return params
}
func (params *UpdateAccountParams) SetStatus(Status string) *UpdateAccountParams {
	params.Status = &Status
	return params
}

func (c *ApiService) UpdateAccount(Sid string, params *UpdateAccountParams) (*TestResponseObject, error) {
	path := "/2010-04-01/Accounts/{Sid}.json"
	path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

	data := url.Values{}
	headers := make(map[string]interface{})

	if params != nil && params.PauseBehavior != nil {
		data.Set("PauseBehavior", *params.PauseBehavior)
	}
	if params != nil && params.Status != nil {
		data.Set("Status", *params.Status)
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
