// Code generated by MockGen. DO NOT EDIT.
// Source: client/provider.go

// Package client is a generated GoMock package.
package client

import (
	openapi "go-client/helper/rest/api/v2010"
	openapi0 "go-client/helper/rest/flex/v1"
	reflect "reflect"

	gomock "github.com/golang/mock/gomock"
)

// MockApi is a mock of Api interface.
type MockApi struct {
	ctrl     *gomock.Controller
	recorder *MockApiMockRecorder
}

// MockApiMockRecorder is the mock recorder for MockApi.
type MockApiMockRecorder struct {
	mock *MockApi
}

// NewMockApi creates a new mock instance.
func NewMockApi(ctrl *gomock.Controller) *MockApi {
	mock := &MockApi{ctrl: ctrl}
	mock.recorder = &MockApiMockRecorder{mock}
	return mock
}

// EXPECT returns an object that allows the caller to indicate expected use.
func (m *MockApi) EXPECT() *MockApiMockRecorder {
	return m.recorder
}

// CreateAccount mocks base method.
func (m *MockApi) CreateAccount(params *openapi.CreateAccountParams) (*openapi.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "CreateAccount", params)
	ret0, _ := ret[0].(*openapi.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// CreateAccount indicates an expected call of CreateAccount.
func (mr *MockApiMockRecorder) CreateAccount(params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "CreateAccount", reflect.TypeOf((*MockApi)(nil).CreateAccount), params)
}

// CreateCall mocks base method.
func (m *MockApi) CreateCall(params *openapi.CreateCallParams) (*openapi.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "CreateCall", params)
	ret0, _ := ret[0].(*openapi.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// CreateCall indicates an expected call of CreateCall.
func (mr *MockApiMockRecorder) CreateCall(params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "CreateCall", reflect.TypeOf((*MockApi)(nil).CreateCall), params)
}

// DeleteAccount mocks base method.
func (m *MockApi) DeleteAccount(Sid string) error {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "DeleteAccount", Sid)
	ret0, _ := ret[0].(error)
	return ret0
}

// DeleteAccount indicates an expected call of DeleteAccount.
func (mr *MockApiMockRecorder) DeleteAccount(Sid interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "DeleteAccount", reflect.TypeOf((*MockApi)(nil).DeleteAccount), Sid)
}

// DeleteCall mocks base method.
func (m *MockApi) DeleteCall(Sid int, params *openapi.DeleteCallParams) error {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "DeleteCall", Sid, params)
	ret0, _ := ret[0].(error)
	return ret0
}

// DeleteCall indicates an expected call of DeleteCall.
func (mr *MockApiMockRecorder) DeleteCall(Sid, params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "DeleteCall", reflect.TypeOf((*MockApi)(nil).DeleteCall), Sid, params)
}

// FetchAccount mocks base method.
func (m *MockApi) FetchAccount(Sid string) (*openapi.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "FetchAccount", Sid)
	ret0, _ := ret[0].(*openapi.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// FetchAccount indicates an expected call of FetchAccount.
func (mr *MockApiMockRecorder) FetchAccount(Sid interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "FetchAccount", reflect.TypeOf((*MockApi)(nil).FetchAccount), Sid)
}

// FetchCall mocks base method.
func (m *MockApi) FetchCall(Sid int, params *openapi.FetchCallParams) (*openapi.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "FetchCall", Sid, params)
	ret0, _ := ret[0].(*openapi.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// FetchCall indicates an expected call of FetchCall.
func (mr *MockApiMockRecorder) FetchCall(Sid, params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "FetchCall", reflect.TypeOf((*MockApi)(nil).FetchCall), Sid, params)
}

// UpdateAccount mocks base method.
func (m *MockApi) UpdateAccount(Sid string, params *openapi.UpdateAccountParams) (*openapi.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "UpdateAccount", Sid, params)
	ret0, _ := ret[0].(*openapi.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// UpdateAccount indicates an expected call of UpdateAccount.
func (mr *MockApiMockRecorder) UpdateAccount(Sid, params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "UpdateAccount", reflect.TypeOf((*MockApi)(nil).UpdateAccount), Sid, params)
}

// MockFlexV1 is a mock of FlexV1 interface.
type MockFlexV1 struct {
	ctrl     *gomock.Controller
	recorder *MockFlexV1MockRecorder
}

// MockFlexV1MockRecorder is the mock recorder for MockFlexV1.
type MockFlexV1MockRecorder struct {
	mock *MockFlexV1
}

// NewMockFlexV1 creates a new mock instance.
func NewMockFlexV1(ctrl *gomock.Controller) *MockFlexV1 {
	mock := &MockFlexV1{ctrl: ctrl}
	mock.recorder = &MockFlexV1MockRecorder{mock}
	return mock
}

// EXPECT returns an object that allows the caller to indicate expected use.
func (m *MockFlexV1) EXPECT() *MockFlexV1MockRecorder {
	return m.recorder
}

// CreateCredentialAws mocks base method.
func (m *MockFlexV1) CreateCredentialAws(params *openapi0.CreateCredentialAwsParams) (*openapi0.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "CreateCredentialAws", params)
	ret0, _ := ret[0].(*openapi0.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// CreateCredentialAws indicates an expected call of CreateCredentialAws.
func (mr *MockFlexV1MockRecorder) CreateCredentialAws(params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "CreateCredentialAws", reflect.TypeOf((*MockFlexV1)(nil).CreateCredentialAws), params)
}

// DeleteCredentialAws mocks base method.
func (m *MockFlexV1) DeleteCredentialAws(Sid string) error {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "DeleteCredentialAws", Sid)
	ret0, _ := ret[0].(error)
	return ret0
}

// DeleteCredentialAws indicates an expected call of DeleteCredentialAws.
func (mr *MockFlexV1MockRecorder) DeleteCredentialAws(Sid interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "DeleteCredentialAws", reflect.TypeOf((*MockFlexV1)(nil).DeleteCredentialAws), Sid)
}

// FetchCredentialAws mocks base method.
func (m *MockFlexV1) FetchCredentialAws(Sid string) (*openapi0.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "FetchCredentialAws", Sid)
	ret0, _ := ret[0].(*openapi0.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// FetchCredentialAws indicates an expected call of FetchCredentialAws.
func (mr *MockFlexV1MockRecorder) FetchCredentialAws(Sid interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "FetchCredentialAws", reflect.TypeOf((*MockFlexV1)(nil).FetchCredentialAws), Sid)
}

// UpdateCredentialAws mocks base method.
func (m *MockFlexV1) UpdateCredentialAws(Sid string, params *openapi0.UpdateCredentialAwsParams) (*openapi0.TestResponseObject, error) {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "UpdateCredentialAws", Sid, params)
	ret0, _ := ret[0].(*openapi0.TestResponseObject)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

// UpdateCredentialAws indicates an expected call of UpdateCredentialAws.
func (mr *MockFlexV1MockRecorder) UpdateCredentialAws(Sid, params interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "UpdateCredentialAws", reflect.TypeOf((*MockFlexV1)(nil).UpdateCredentialAws), Sid, params)
}
