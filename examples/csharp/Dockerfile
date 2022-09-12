FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
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

RUN rm -rf src/test/
COPY test /app/twilio-csharp/test

RUN dotnet restore
RUN dotnet build -c Release

# sonar coverage reports

RUN	dotnet build Twilio.sln > buildforsonar.log
#RUN reportgenerator "-reports:./test/lcov.net451.opencover.xml" "-targetdir:test" "-reporttypes:SonarQube" -- enable this to generate better reports
CMD ["/bin/bash", "-c", "dotnet test test/Twilio.Test/Twilio.Test.csproj --no-build /p:CollectCoverage=true /p:CoverletOutputFormat=opencover /p:CoverletOutput=../lcov 2>&1 | tee /local/test-report.out && mv test/lcov.net451.opencover.xml /local/test/Twilio.Test/lcov.net451.opencover.xml"]
# dotnet sonarscanner end /d:sonar.login="${SONAR_TOKEN}" -- enable this when syncing up with sonarcloud

