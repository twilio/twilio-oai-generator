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


// Optional parameters for the method 'CreateCredentialAws'
type CreateCredentialAwsParams struct {
    // 
    TestString *string `json:"TestString,omitempty"`
    // 
    TestBoolean *bool `json:"TestBoolean,omitempty"`
    // 
    TestInteger *int `json:"TestInteger,omitempty"`
    // 
    TestNumber *float32 `json:"TestNumber,omitempty"`
    // 
    TestNumberFloat *float32 `json:"TestNumberFloat,omitempty"`
    // 
    TestNumberDouble *float64 `json:"TestNumberDouble,omitempty"`
    // 
    TestNumberInt32 *float32 `json:"TestNumberInt32,omitempty"`
    // 
    TestNumberInt64 *int64 `json:"TestNumberInt64,omitempty"`
    // 
    TestObject *map[string]interface{} `json:"TestObject,omitempty"`
    // 
    TestDateTime *time.Time `json:"TestDateTime,omitempty"`
    // 
    TestDate *string `json:"TestDate,omitempty"`
    // 
    TestEnum *string `json:"TestEnum,omitempty"`
    // 
    TestObjectArray *[]map[string]interface{} `json:"TestObjectArray,omitempty"`
    // 
    TestAnyType *interface{} `json:"TestAnyType,omitempty"`
    // 
    TestAnyArray *[]interface{} `json:"TestAnyArray,omitempty"`
    // A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: `get-all` and `post-all`.
    Permissions *[]string `json:"Permissions,omitempty"`
    // 
    SomeA2PThing *string `json:"SomeA2PThing,omitempty"`
}

func (params *CreateCredentialAwsParams) SetTestString(TestString string) (*CreateCredentialAwsParams){
    params.TestString = &TestString
    return params
}
func (params *CreateCredentialAwsParams) SetTestBoolean(TestBoolean bool) (*CreateCredentialAwsParams){
    params.TestBoolean = &TestBoolean
    return params
}
func (params *CreateCredentialAwsParams) SetTestInteger(TestInteger int) (*CreateCredentialAwsParams){
    params.TestInteger = &TestInteger
    return params
}
func (params *CreateCredentialAwsParams) SetTestNumber(TestNumber float32) (*CreateCredentialAwsParams){
    params.TestNumber = &TestNumber
    return params
}
func (params *CreateCredentialAwsParams) SetTestNumberFloat(TestNumberFloat float32) (*CreateCredentialAwsParams){
    params.TestNumberFloat = &TestNumberFloat
    return params
}
func (params *CreateCredentialAwsParams) SetTestNumberDouble(TestNumberDouble float64) (*CreateCredentialAwsParams){
    params.TestNumberDouble = &TestNumberDouble
    return params
}
func (params *CreateCredentialAwsParams) SetTestNumberInt32(TestNumberInt32 float32) (*CreateCredentialAwsParams){
    params.TestNumberInt32 = &TestNumberInt32
    return params
}
func (params *CreateCredentialAwsParams) SetTestNumberInt64(TestNumberInt64 int64) (*CreateCredentialAwsParams){
    params.TestNumberInt64 = &TestNumberInt64
    return params
}
func (params *CreateCredentialAwsParams) SetTestObject(TestObject map[string]interface{}) (*CreateCredentialAwsParams){
    params.TestObject = &TestObject
    return params
}
func (params *CreateCredentialAwsParams) SetTestDateTime(TestDateTime time.Time) (*CreateCredentialAwsParams){
    params.TestDateTime = &TestDateTime
    return params
}
func (params *CreateCredentialAwsParams) SetTestDate(TestDate string) (*CreateCredentialAwsParams){
    params.TestDate = &TestDate
    return params
}
func (params *CreateCredentialAwsParams) SetTestEnum(TestEnum string) (*CreateCredentialAwsParams){
    params.TestEnum = &TestEnum
    return params
}
func (params *CreateCredentialAwsParams) SetTestObjectArray(TestObjectArray []map[string]interface{}) (*CreateCredentialAwsParams){
    params.TestObjectArray = &TestObjectArray
    return params
}
func (params *CreateCredentialAwsParams) SetTestAnyType(TestAnyType interface{}) (*CreateCredentialAwsParams){
    params.TestAnyType = &TestAnyType
    return params
}
func (params *CreateCredentialAwsParams) SetTestAnyArray(TestAnyArray []interface{}) (*CreateCredentialAwsParams){
    params.TestAnyArray = &TestAnyArray
    return params
}
func (params *CreateCredentialAwsParams) SetPermissions(Permissions []string) (*CreateCredentialAwsParams){
    params.Permissions = &Permissions
    return params
}
func (params *CreateCredentialAwsParams) SetSomeA2PThing(SomeA2PThing string) (*CreateCredentialAwsParams){
    params.SomeA2PThing = &SomeA2PThing
    return params
}

func (c *ApiService) CreateCredentialAws(params *CreateCredentialAwsParams) (*TestResponseObject, error) {
    path := "/v1/Credentials/AWS"
    
    data := url.Values{}
    headers := make(map[string]interface{})
if params != nil && params.TestString != nil {
    data.Set("TestString", *params.TestString)
}
if params != nil && params.TestBoolean != nil {
    data.Set("TestBoolean", fmt.Sprint(*params.TestBoolean))
}
if params != nil && params.TestInteger != nil {
    data.Set("TestInteger", fmt.Sprint(*params.TestInteger))
}
if params != nil && params.TestNumber != nil {
    data.Set("TestNumber", fmt.Sprint(*params.TestNumber))
}
if params != nil && params.TestNumberFloat != nil {
    data.Set("TestNumberFloat", fmt.Sprint(*params.TestNumberFloat))
}
if params != nil && params.TestNumberDouble != nil {
    data.Set("TestNumberDouble", fmt.Sprint(*params.TestNumberDouble))
}
if params != nil && params.TestNumberInt32 != nil {
    data.Set("TestNumberInt32", fmt.Sprint(*params.TestNumberInt32))
}
if params != nil && params.TestNumberInt64 != nil {
    data.Set("TestNumberInt64", fmt.Sprint(*params.TestNumberInt64))
}
if params != nil && params.TestObject != nil {
    v, err := json.Marshal(params.TestObject)

    if err != nil {
        return nil, err
    }

    data.Set("TestObject", string(v))
}
if params != nil && params.TestDateTime != nil {
    data.Set("TestDateTime", fmt.Sprint((*params.TestDateTime).Format(time.RFC3339)))
}
if params != nil && params.TestDate != nil {
    data.Set("TestDate", fmt.Sprint(*params.TestDate))
}
if params != nil && params.TestEnum != nil {
    data.Set("TestEnum", *params.TestEnum)
}
if params != nil && params.TestObjectArray != nil {
    for _, item  := range *params.TestObjectArray {
        v, err := json.Marshal(item)

        if err != nil {
            return nil, err
        }

        data.Add("TestObjectArray", string(v))
    }
}
if params != nil && params.TestAnyType != nil {
    v, err := json.Marshal(params.TestAnyType)

    if err != nil {
        return nil, err
    }

    data.Set("TestAnyType", string(v))
}
if params != nil && params.TestAnyArray != nil {
    for _, item  := range *params.TestAnyArray {
        v, err := json.Marshal(item)

        if err != nil {
            return nil, err
        }

        data.Add("TestAnyArray", string(v))
    }
}
if params != nil && params.Permissions != nil {
    for _, item  := range *params.Permissions {
        data.Add("Permissions", item)
    }
}
if params != nil && params.SomeA2PThing != nil {
    data.Set("SomeA2PThing", *params.SomeA2PThing)
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

func (c *ApiService) DeleteCredentialAws(Sid string, ) (error) {
    path := "/v1/Credentials/AWS/{Sid}"
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

func (c *ApiService) FetchCredentialAws(Sid string, ) (*TestResponseObject, error) {
    path := "/v1/Credentials/AWS/{Sid}"
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

// Optional parameters for the method 'ListCredentialAws'
type ListCredentialAwsParams struct {
    // 
    PageSize *int `json:"PageSize,omitempty"`
    // Max number of records to return.
    Limit *int `json:"limit,omitempty"`
}

func (params *ListCredentialAwsParams) SetPageSize(PageSize int) (*ListCredentialAwsParams){
    params.PageSize = &PageSize
    return params
}
func (params *ListCredentialAwsParams) SetLimit(Limit int) (*ListCredentialAwsParams){
    params.Limit = &Limit
    return params
}

// Retrieve a single page of CredentialAws records from the API. Request is executed immediately.
func (c *ApiService) PageCredentialAws(params *ListCredentialAwsParams, pageToken, pageNumber string) (*ListCredentialAwsResponse, error) {
    path := "/v1/Credentials/AWS"

    
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

    ps := &ListCredentialAwsResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }

    return ps, err
}

// Lists CredentialAws records from the API as a list. Unlike stream, this operation is eager and loads 'limit' records into memory before returning.
func (c *ApiService) ListCredentialAws(params *ListCredentialAwsParams) ([]TestResponseObject, error) {
	response, errors := c.StreamCredentialAws(params)

	records := make([]TestResponseObject, 0)
	for record := range response {
		records = append(records, record)
	}

	if err := <-errors; err != nil {
		return nil, err
	}

	return records, nil
}

// Streams CredentialAws records from the API as a channel stream. This operation lazily loads records as efficiently as possible until the limit is reached.
func (c *ApiService) StreamCredentialAws(params *ListCredentialAwsParams) (chan TestResponseObject, chan error) {
	if params == nil {
		params = &ListCredentialAwsParams{}
	}
	params.SetPageSize(client.ReadLimits(params.PageSize, params.Limit))

	recordChannel := make(chan TestResponseObject, 1)
	errorChannel := make(chan error, 1)

	response, err := c.PageCredentialAws(params, "", "")
	if err != nil {
		errorChannel <- err
		close(recordChannel)
		close(errorChannel)
	} else {
		go c.streamCredentialAws(response, params, recordChannel, errorChannel)
	}

	return recordChannel, errorChannel
}


func (c *ApiService) streamCredentialAws(response *ListCredentialAwsResponse, params *ListCredentialAwsParams, recordChannel chan TestResponseObject, errorChannel chan error) {
	curRecord := 1

	for response != nil {
		responseRecords := response.Credentials
		for item := range responseRecords {
			recordChannel <- responseRecords[item]
			curRecord += 1
			if params.Limit != nil && *params.Limit < curRecord {
				close(recordChannel)
				close(errorChannel)
				return
			}
		}

		record, err := client.GetNext(c.baseURL, response, c.getNextListCredentialAwsResponse)
		if err != nil {
			errorChannel <- err
			break
		} else if record == nil {
			break
		}

		response = record.(*ListCredentialAwsResponse)
	}

	close(recordChannel)
	close(errorChannel)
}

func (c *ApiService) getNextListCredentialAwsResponse(nextPageUrl string) (interface{}, error) {
    if nextPageUrl == "" {
        return nil, nil
    }
    resp, err := c.requestHandler.Get(nextPageUrl, nil, nil)
    if err != nil {
        return nil, err
    }

    defer resp.Body.Close()

    ps := &ListCredentialAwsResponse{}
    if err := json.NewDecoder(resp.Body).Decode(ps); err != nil {
        return nil, err
    }
    return ps, nil
}


// Optional parameters for the method 'UpdateCredentialAws'
type UpdateCredentialAwsParams struct {
    // 
    TestString *string `json:"TestString,omitempty"`
    // 
    TestBoolean *bool `json:"TestBoolean,omitempty"`
}

func (params *UpdateCredentialAwsParams) SetTestString(TestString string) (*UpdateCredentialAwsParams){
    params.TestString = &TestString
    return params
}
func (params *UpdateCredentialAwsParams) SetTestBoolean(TestBoolean bool) (*UpdateCredentialAwsParams){
    params.TestBoolean = &TestBoolean
    return params
}

func (c *ApiService) UpdateCredentialAws(Sid string, params *UpdateCredentialAwsParams) (*TestResponseObject, error) {
    path := "/v1/Credentials/AWS/{Sid}"
        path = strings.Replace(path, "{"+"Sid"+"}", Sid, -1)

    data := url.Values{}
    headers := make(map[string]interface{})
if params != nil && params.TestString != nil {
    data.Set("TestString", *params.TestString)
}
if params != nil && params.TestBoolean != nil {
    data.Set("TestBoolean", fmt.Sprint(*params.TestBoolean))
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
