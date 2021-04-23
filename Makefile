.PHONY: build install test-docker

OPENAPI_GENERATOR_VERSION=5.0.1

build:
	mvn clean package -DskipTests

install:
	wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$(OPENAPI_GENERATOR_VERSION)/openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar -O openapi-generator-cli.jar
	mvn clean package -DskipTests

test-docker:
	bash ./prism.sh
