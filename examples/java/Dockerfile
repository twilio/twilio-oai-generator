FROM eclipse-temurin:8-jdk

# Install Maven with proper error handling
RUN apt-get update && \
    apt-get install -y --no-install-recommends git maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
# Install git
RUN apt-get update && \
    apt-get install -y --no-install-recommends git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* 

RUN apt-get update && apt-get install git -y

# Use HTTPS instead of SSH for git clone
RUN git clone https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN rm -rf src/main/java/com/twilio/rest/*/ \
           src/main/java/com/twilio/example/ \
           src/test/

COPY src/main/java/com/twilio src/main/java/com/twilio/
COPY unit-test/rest integration-test/rest src/test/java/com/twilio/

CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test --no-transfer-progress | tee /local/test-report.out && cp -r target /local"]
