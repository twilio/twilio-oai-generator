FROM golang:1.16

ENV GOPATH '/go'
RUN go install golang.org/x/tools/cmd/goimports@latest

COPY examples /go/src/twilio-go

WORKDIR /go/src/twilio-go/go
RUN go get github.com/twilio/twilio-go@main

WORKDIR /go/src/twilio-go/terraform
RUN go get github.com/twilio/terraform-provider-twilio@main

WORKDIR /go/src/twilio-go
RUN goimports -w .

CMD (cd go        && go test -v ./...) \
 && (cd terraform && go test -v ./...)
