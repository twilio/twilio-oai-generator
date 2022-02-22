FROM openjdk:11

RUN mkdir /app
WORKDIR /app

RUN git clone https://github.com/twilio/twilio-java.git

RUN rm -rf /app/twilio-java/src/main/java/com/twilio/rest
RUN rm -rf /app/twilio-java/src/test/java/com/twilio/rest/

COPY examples/java/rest /app/twilio-java/src/main/java/com/twilio/
COPY examples/java/unit-test/rest /app/twilio-java/src/test/java/com/twilio/rest/

RUN apt update
RUN apt install -y maven

WORKDIR /app/twilio-java/

CMD mvn test
