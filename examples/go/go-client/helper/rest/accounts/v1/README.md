# Go API client for openapi

This is the public Twilio REST API.

## Overview
This API client was generated by the [OpenAPI Generator](https://openapi-generator.tech) project from the OpenAPI specs located at [twilio/twilio-oai](https://github.com/twilio/twilio-oai/tree/main/spec).  By using the [OpenAPI-spec](https://www.openapis.org/) from a remote server, you can easily generate an API client.

- API version: 1.0.0
- Package version: 1.0.0
- Build package: com.twilio.oai.TwilioGoGenerator
For more information, please visit [https://support.twilio.com](https://support.twilio.com)

## Installation

Install the following dependencies:

```shell
go get github.com/stretchr/testify/assert
go get golang.org/x/net/context
```

Put the package under your project folder and add the following in import:

```golang
import "./openapi"
```

## Documentation for API Endpoints

All URIs are relative to *https://accounts.twilio.com*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*SafeListNumbersApi* | [**CreateSafelist**](docs/SafeListNumbersApi.md#createsafelist) | **Post** /v1/SafeList/Numbers | 
*SafeListNumbersApi* | [**DeleteSafelist**](docs/SafeListNumbersApi.md#deletesafelist) | **Delete** /v1/SafeList/Numbers | 
*SafeListNumbersApi* | [**FetchSafelist**](docs/SafeListNumbersApi.md#fetchsafelist) | **Get** /v1/SafeList/Numbers | 


## Documentation For Models

 - [AccountsV1Safelist](docs/AccountsV1Safelist.md)


## Documentation For Authorization

 Endpoints do not require authorization.
