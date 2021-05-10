FROM golang:1.16
COPY examples/go /go/src/twilio-go/codegen
WORKDIR /go/src/twilio-go/codegen
ENV GOPATH ''
RUN go get github.com/twilio/twilio-go@main
CMD go test -v ./...
