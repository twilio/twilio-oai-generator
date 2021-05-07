#!/bin/bash
set -e

rm -rf examples/go/twilio && mkdir examples/go/twilio
make install
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i examples/go/oai.yaml \
  -o examples/go/twilio/rest/oai/
cd examples/go/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
