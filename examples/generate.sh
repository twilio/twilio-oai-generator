#!/bin/bash
set -e

API_SPEC=examples/twilio_api_v2010.yaml

OUT_DIR=examples/go-client/helper/rest
find $OUT_DIR ! -name "*_test.go" -type f -delete
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  -i $API_SPEC \
  -o $OUT_DIR/api/v2010

OUT_DIR=examples/go-client/terraform/resources
find $OUT_DIR ! -name "*_test.go" -type f -delete
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g terraform-provider-twilio \
  -i $API_SPEC \
  -o $OUT_DIR

# Replace a couple imports in the generated Terraform resource to use local code.
sed -i.bak "s/github.com\/twilio\/twilio-go/go-client\/helper/g" "$OUT_DIR/api_default.go"
sed -i.bak "s/github.com\/twilio\/terraform-provider-twilio\/client/go-client\/terraform\/client/g" "$OUT_DIR/api_default.go"

OUT_DIR=examples/java/rest
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar  \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-java \
  -i $API_SPEC \
  -o $OUT_DIR/api  \
  --global-property apiTests=false,apiDocs=false

OUT_DIR=examples/node/rest
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-node \
  -i $API_SPEC \
  -o $OUT_DIR


pushd examples/go-client
docker run \
  -v "${PWD}":/local \
  "$(docker build -f Dockerfile-goimports -q .)"
popd
