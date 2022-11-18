FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
ENV PATH="${PATH}:/root/.dotnet/tools"

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
         git \
         build-essential \
         mono-complete \
         openjdk-11-jre

RUN dotnet tool install --global dotnet-sonarscanner \
&& dotnet tool install --global dotnet-reportgenerator-globaltool --version 4.8.12 \
&& dotnet tool list -g

RUN mkdir /app
WORKDIR /app
RUN git clone --depth 1 https://github.com/twilio/twilio-csharp.git
WORKDIR /app/twilio-csharp

RUN rm -rf src/Twilio/Rest/*/
COPY src/Twilio/Rest src/Twilio/Rest

RUN rm -rf test/Twilio.Test/*/ test/Twilio.Test/*Cluster*
COPY unit-test/Rest integrationTest/Rest test/Twilio.Test/

RUN make install

COPY test.sh .
ARG SONAR_TOKEN
ENV SONAR_TOKEN=$SONAR_TOKEN

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && chmod u+r+x /app/twilio-csharp/test.sh && /app/twilio-csharp/test.sh 2>&1 | tee /local/test-report.out"]