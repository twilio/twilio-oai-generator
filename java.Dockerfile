FROM openjdk:8

RUN mkdir /app
WORKDIR /app
RUN git clone https://github.com/twilio/twilio-java.git
WORKDIR /app/twilio-java/

RUN echo $(ls -1 src/main/java/com/twilio)

RUN rm -rf /src/main/java/com/twilio/rest/ \
    rm -rf /src/test/java/com/twilio/rest/ \
    rm -rf /src/main/java/com/twilio/example\
    rm -rf /src/test/java/com/twilio/base/ReaderTest.java 

RUN ls /src/main/java/com/twilio
RUN ls src/main/java/com/twilio

COPY examples/java/rest src/main/java/com/twilio/rest
COPY examples/java/unit-test/rest src/test/java/com/twilio/rest/
COPY examples/java/integration-test/rest src/test/java/com/twilio/rest/
COPY examples/java/pom.xml pom.xml

RUN echo $(ls -1 src/main/java/com/twilio)

RUN apt-get update && apt-get install maven -y

CMD mvn clean test
