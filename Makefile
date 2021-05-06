.PHONY: install test-docker

OPENAPI_GENERATOR_VERSION=5.0.1

install:
	cd examples/go && go get github.com/twilio/twilio-go@main
	wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$(OPENAPI_GENERATOR_VERSION)/openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar -O openapi-generator-cli.jar
	mvn clean package -DskipTests

test-docker:
	bash ./prism.sh
