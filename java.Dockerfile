FROM openjdk:8

RUN mkdir /app
WORKDIR /app

RUN git clone https://github.com/twilio/twilio-java.git

RUN rm -rf /app/twilio-java/src/main/java/com/twilio/rest
RUN rm -rf /app/twilio-java/src/test/java/com/twilio/rest/
RUN rm -rf /app/twilio-java/src/test/java/com/twilio/compliance/
RUN rm -rf /app/twilio-java/src/main/java/com/twilio/example
RUN rm -rf /app/twilio-java/src/test/java/com/twilio/http/TwilioRestClientTest.java
RUN rm -rf /app/twilio-java/src/test/java/com/twilio/base/ReaderTest.java

COPY examples/java/rest /app/twilio-java/src/main/java/com/twilio/rest
COPY examples/java/unit-test/rest /app/twilio-java/src/test/java/com/twilio/rest/

RUN apt-get update && apt-get install maven -y

WORKDIR /app/twilio-java/

CMD mvn clean test
