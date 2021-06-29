#!/bin/bash
set -e

REST_DIR=examples/go/rest/oai

rm -rf $REST_DIR
make install
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i examples/oai.yaml \
  -o $REST_DIR
cd examples/go/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
