.PHONY: install test generate test-docker

OPENAPI_GENERATOR_VERSION=6.0.1

install:
	wget -N https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$(OPENAPI_GENERATOR_VERSION)/openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar
	ln -sf openapi-generator-cli-$(OPENAPI_GENERATOR_VERSION).jar openapi-generator-cli.jar
	mvn clean package -DskipTests

test:
	mvn clean test

generate: install
	bash scripts/generate.sh

test-docker: generate
	bash ./prism.sh

cover:
	dotnet sonarscanner begin /k:"twilio_twilio-oai-generator-csharp" /o:"twilio" /d:sonar.host.url=https://sonarcloud.io /d:sonar.login="${SONAR_TOKEN}"  /d:sonar.language="cs" /d:sonar.cs.opencover.reportsPaths="test/lcov.net451.opencover.xml"
	# Write to a log file since the logs for build with sonar analyzer are pretty beefy and travis has a limit on the number of log lines
	dotnet build Twilio.sln > buildsonar.log
	dotnet test test/Twilio.Test/Twilio.Test.csproj --no-build /p:CollectCoverage=true /p:CoverletOutputFormat=opencover /p:CoverletOutput=../lcov
	dotnet sonarscanner end /d:sonar.login="${SONAR_TOKEN}"
