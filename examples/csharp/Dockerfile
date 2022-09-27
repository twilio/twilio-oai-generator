FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
ARG SONAR_CSHARP_TOKEN=""
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
         git \
         mono-complete \
         openjdk-11-jre

RUN dotnet tool install --global dotnet-sonarscanner \
    && dotnet tool install --global dotnet-reportgenerator-globaltool --version 4.8.12

RUN dotnet tool list -g
ENV PATH="${PATH}:/root/.dotnet/tools"

RUN mkdir /app
WORKDIR /app
RUN git clone --depth 1 https://github.com/twilio/twilio-csharp.git
WORKDIR /app/twilio-csharp

RUN rm -rf src/Twilio/Rest/*/
COPY src/Twilio/Rest src/Twilio/Rest

RUN rm -rf test/Twilio.Test/*/
COPY unit-test/Rest integrationTest/Rest test/Twilio.Test/

RUN dotnet restore
RUN dotnet build -c Release
RUN echo ${SONAR_CSHARP_TOKEN} SONAR_CSHARP_TOKEN

RUN dotnet sonarscanner begin /k:"twilio_twilio-oai-generator-csharp" /o:"twilio" /d:sonar.host.url=https://sonarcloud.io /d:sonar.language="cs" /d:sonar.login=$SONAR_CSHARP_TOKEN /d:sonar.exclusions="src/Twilio/Rest/**/*.*,test/Twilio.Test/**/*.*" /d:sonar.cs.opencover.reportsPaths="test/lcov.net451.opencover.xml"
RUN	dotnet build Twilio.sln > buildforsonar.log
CMD ["/bin/bash", "-c", "dotnet test test/Twilio.Test/Twilio.Test.csproj --no-build /p:CollectCoverage=true /p:CoverletOutputFormat=opencover /p:CoverletOutput=../lcov| tee /local/test-report.out && cp test/lcov.net451.opencover.xml /local"]
RUN dotnet sonarscanner end /d:sonar.login=$SONAR_CSHARP_TOKEN
