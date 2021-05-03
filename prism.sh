#!/bin/bash
set -e

rm -rf codegen && mkdir -p codegen
docker system prune -af
make install
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator-1.0.0.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i examples/go/oai.yaml \
  -o codegen/twilio/rest/oai/
cp -R codegen/ examples/go
cd examples/go/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
