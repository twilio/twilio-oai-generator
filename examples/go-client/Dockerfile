FROM golang:1.16

ENV GOPATH /go
RUN go install golang.org/x/tools/cmd/goimports@latest

WORKDIR /go/src/twilio-go

RUN go get -u github.com/twilio/twilio-go@main
RUN go get -u github.com/twilio/terraform-provider-twilio@main

CMD goimports -w . && go test ./... -coverprofile coverage.out -json | tee test-report.out
