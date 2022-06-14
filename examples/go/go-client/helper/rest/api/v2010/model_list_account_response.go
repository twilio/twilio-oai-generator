/*
This code was generated by
    ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
     |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
     |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \

    * Twilio - Accounts
    *
    * This is the public Twilio REST API.
    *
    * API version: 1.11.0
    * Contact: support@twilio.com
    *
    * NOTE: This class is auto generated by OpenAPI Generator.
    * https://openapi-generator.tech
    * Do not edit the class manually.
*/

package openapi
import (
	"encoding/json"
	"github.com/twilio/twilio-go/client"
)
// ListAccountResponse struct for ListAccountResponse
type ListAccountResponse struct {
	End int `json:"end,omitempty"`
	FirstPageUri string `json:"first_page_uri,omitempty"`
	NextPageUri string `json:"next_page_uri,omitempty"`
	Page int `json:"page,omitempty"`
	PageSize int `json:"page_size,omitempty"`
	PreviousPageUri string `json:"previous_page_uri,omitempty"`
	Accounts []TestResponseObject `json:"accounts,omitempty"`
	Start int `json:"start,omitempty"`
	Uri string `json:"uri,omitempty"`
}


