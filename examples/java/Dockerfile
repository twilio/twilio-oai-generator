FROM openjdk:8

RUN apt-get update && apt-get install maven -y

WORKDIR /app
RUN git clone --depth 1 https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN rm -rf src/main/java/com/twilio/rest/*/ \
           src/main/java/com/twilio/example/ \
           src/test/

COPY src/main/java/com/twilio/rest src/main/java/com/twilio/
COPY unit-test/rest integration-test/rest src/test/java/com/twilio/

RUN sed -i.bak 's/<dependencies>/<dependencies><dependency><groupId>org.mockito<\/groupId><artifactId>mockito-all<\/artifactId><version>1.10.18 <\/version><\/dependency>/g' pom.xml \
  ; sed -i.bak 's/<dependencies>/<dependencies><dependency><groupId>org.json   <\/groupId><artifactId>json       <\/artifactId><version>20220320<\/version><\/dependency>/g' pom.xml

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test --no-transfer-progress | tee /local/test-report.out && cp -r target /local"]
