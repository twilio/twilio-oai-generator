.PHONY: install test generate test-docker

OPENAPI_GENERATOR_VERSION=6.2.1

install:
	wget -N https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$(OPENAPI_GENERATOR_VERSION)/openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar
	ln -sf openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar openapi-generator-cli.jar
	mvn clean package -DskipTests

test:
	mvn clean test

generate: install
	bash scripts/generate.sh

test-docker: generate
	bash scripts/prism.sh $(SONAR_TOKEN)
