#!/bin/bash
set -e

API_SPEC=examples/twilio_api_v2010.yaml

OUT_DIR=examples/go/go-client/helper/rest
find $OUT_DIR ! -name "*_test.go" -type f -delete
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-go \
  --inline-schema-name-defaults arrayItemSuffix="" \
  -i $API_SPEC \
  -o $OUT_DIR/api/v2010

OUT_DIR=examples/go/go-client/terraform/resources
find $OUT_DIR ! -name "*_test.go" -type f -delete
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g terraform-provider-twilio \
  --inline-schema-name-defaults arrayItemSuffix="" \
  -i $API_SPEC \
  -o $OUT_DIR

# Replace a couple imports in the generated Terraform resource to use local code.
sed -i.bak "s/github.com\/twilio\/twilio-go/go-client\/helper/g" "$OUT_DIR/api_default.go"
sed -i.bak "s/github.com\/twilio\/terraform-provider-twilio\/client/go-client\/terraform\/client/g" "$OUT_DIR/api_default.go"

pushd examples/go
docker run \
  -v "${PWD}":/local \
  "$(docker build -f Dockerfile-goimports -q .)"
popd

OUT_DIR=examples/java/src/main/java/com/twilio/rest
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar  \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-java \
  --inline-schema-name-defaults arrayItemSuffix="" \
  -i $API_SPEC \
  -o $OUT_DIR/api  \
  --global-property apiTests=false,apiDocs=false

OUT_DIR=examples/node/lib/rest/api
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-node \
  --inline-schema-name-defaults arrayItemSuffix="" \
  -i $API_SPEC \
  -o $OUT_DIR

pushd examples/node
docker run \
  -v "${PWD}":/local \
  "$(docker build -f Dockerfile-prettier -q .)"
popd
