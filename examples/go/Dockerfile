FROM golang:1.21

ENV GOPATH /go

WORKDIR /go/src/twilio-go
COPY go-client .

# Initialize Go modules and download dependencies
RUN go mod download && go mod verify

# Install test dependencies
RUN go get github.com/stretchr/testify@v1.7.0

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "sleep 25 && set -o pipefail && go test -race ./... -coverprofile /local/coverage.out -json | tee /local/test-report.out"]
