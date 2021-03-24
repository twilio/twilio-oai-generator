FROM golang:1.16
COPY examples/go /go/src/twilio-go/codegen
COPY codegen/ /go/src/twilio-go/codegen
WORKDIR /go/src/twilio-go/codegen
ENV GOPATH ''
CMD go test -v ./...
