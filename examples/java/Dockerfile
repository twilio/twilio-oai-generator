FROM openjdk:8

RUN apt-get update && apt-get install maven -y

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN rm -rf /src/main/java/com/twilio/rest/ \
    rm -rf /src/test/java/com/twilio/rest/ \
    rm -rf /src/main/java/com/twilio/example\
    rm -rf /src/test/java/com/twilio/base/ReaderTest.java

COPY rest                  /src/main/java/com/twilio/rest/
COPY unit-test/rest        /src/test/java/com/twilio/rest/
COPY integration-test/rest /src/test/java/com/twilio/rest/
COPY pom.xml /pom.xml

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test | tee /local/test-report.out"]
