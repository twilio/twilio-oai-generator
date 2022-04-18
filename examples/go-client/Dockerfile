FROM golang:1.18

ENV GOPATH /go

WORKDIR /go/src/twilio-go
COPY . .

RUN go get -u github.com/twilio/twilio-go@main
RUN go get -u github.com/twilio/terraform-provider-twilio@main

# pipefail prevents errors in a pipeline from being masked.
CMD ["/local/wait-for-prism.sh", "/bin/bash", "-c", "set -o pipefail && go test ./... -coverprofile /local/coverage.out -json | tee /local/test-report.out"]
