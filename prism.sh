#!/bin/bash
set -e

cd examples/go && rm -rf twilio && mkdir -p twilio && cd ../../
make install
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator-1.0.0.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i examples/go/oai.yaml \
  -o examples/go/twilio/rest/oai/
cd examples/go/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
