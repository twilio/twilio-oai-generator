FROM eclipse-temurin:8-jdk

RUN apt-get update && apt-get install maven git -y

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN rm -rf src/main/java/com/twilio/rest/*/ \
           src/main/java/com/twilio/example/ \
           src/test/

COPY src/main/java/com/twilio src/main/java/com/twilio/
COPY unit-test/rest integration-test/rest src/test/java/com/twilio/

CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test --no-transfer-progress | tee /local/test-report.out && cp -r target /local"]
