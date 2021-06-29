FROM golang:1.16
COPY examples/go /go/src/twilio-go/codegen
WORKDIR /go/src/twilio-go/codegen
ENV GOPATH '/go'
RUN go get github.com/twilio/twilio-go/client@main \
 && go get golang.org/x/tools/cmd/goimports \
 && goimports -w .
CMD go test -v ./...
