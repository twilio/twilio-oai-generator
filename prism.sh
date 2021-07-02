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

OUT_DIR=examples/terraform/resources
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g terraform-provider-twilio \
  -i $API_SPEC \
  -o $OUT_DIR

# Replace a couple imports in the generated Terraform resource to use local code.
sed -i.bak "s/github.com\/twilio\/twilio-go/twilio-oai-generator\/go/g" "$OUT_DIR/api_default.go"
sed -i.bak "s/github.com\/twilio\/terraform-provider-twilio\/client/twilio-oai-generator\/terraform\/client/g" "$OUT_DIR/api_default.go"

cd examples/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
