To generate `twilio-go`, execute the following from the root of this repo:

# Setup

`make install`

# Code Generation

First, please update `<path to>`.

`mvn clean package -DskipTests && python3 examples/build_twilio_go.py <path to>/twilio-oai/spec/yaml <path to>/twilio-go`
