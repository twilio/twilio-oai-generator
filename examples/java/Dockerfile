FROM eclipse-temurin:11-jdk

# Install Maven with proper error handling
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
# Install git
RUN apt-get update && \
    apt-get install -y --no-install-recommends git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Use HTTPS instead of SSH for git clone
RUN git clone https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN rm -rf src/main/java/com/twilio/rest/*/ \
           src/main/java/com/twilio/example/ \
           src/test/

COPY src/main/java/com/twilio src/main/java/com/twilio/
COPY unit-test/rest integration-test/rest src/test/java/com/twilio/

RUN sed -i.bak 's/<dependencies>/<dependencies><dependency><groupId>org.json   <\/groupId><artifactId>json       <\/artifactId><version>20220320<\/version><\/dependency>/g' pom.xml ;

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test --no-transfer-progress | tee /local/test-report.out && cp -r target /local"]
