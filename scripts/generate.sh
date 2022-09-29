#!/bin/bash
set -e

function generate() {
  find "$OUT_DIR" ! -name "*_test.go" -type f -delete
  java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
    org.openapitools.codegen.OpenAPIGenerator \
    generate -g "$@" \
    -i "$API_SPEC" \
    -o "$OUT_DIR" \
    --inline-schema-name-defaults arrayItemSuffix="" \
    --global-property apiTests=false,apiDocs=false
}

function docker-run() {
  pushd "$(dirname "$1")"
  docker run \
    -v "${PWD}":/local \
    "$(docker build -f "$(basename "$1")" -q .)"
  popd
}

API_SPEC=examples/twilio_api_v2010.yaml

OUT_DIR=examples/go/go-client/helper/rest/api/v2010
generate twilio-go

OUT_DIR=examples/go/go-client/terraform/resources
generate terraform-provider-twilio

# Replace a couple imports in the generated Terraform resource to use local code.
sed -i.bak "s/github.com\/twilio\/twilio-go/go-client\/helper/g" "$OUT_DIR/api_default.go"
sed -i.bak "s/github.com\/twilio\/terraform-provider-twilio\/client/go-client\/terraform\/client/g" "$OUT_DIR/api_default.go"

docker-run examples/go/Dockerfile-goimports

OUT_DIR=examples/java/src/main/java/com/twilio/rest/api
generate twilio-java

OUT_DIR=examples/node/lib/rest/api
generate twilio-node
docker-run examples/node/Dockerfile-prettier
