#!/bin/bash
set -e

make install

API_SPEC=examples/twilio_api_v2010.yaml

OUT_DIR=examples/go/rest
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i $API_SPEC \
  -o $OUT_DIR/api/v2010

cd examples/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
