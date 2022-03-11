FROM golang:1.16

ENV GOPATH /go
RUN go install golang.org/x/tools/cmd/goimports@latest

WORKDIR /go/src/twilio-go
COPY . .

RUN go get -u github.com/twilio/twilio-go@main
RUN go get -u github.com/twilio/terraform-provider-twilio@main
RUN goimports -w .

CMD go test ./... -coverprofile /local/coverage.out -json | tee /local/test-report.out
