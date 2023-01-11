#!/bin/bash
set -e

function should-generate() {
  [ "$LANGUAGES" = "" ] || [[ "$LANGUAGES" == *$1* ]]
}

function generate() {
  find "$OUT_DIR"/*/ ! -name "*_test.go" -type f -delete || true

  for api_spec in examples/spec/*; do
    java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar \
      org.openapitools.codegen.OpenAPIGenerator \
      generate -g "$@" \
      -i "$api_spec" \
      -o "$OUT_DIR" \
      --inline-schema-name-defaults arrayItemSuffix="" \
      --global-property apiTests=false,apiDocs=false
  done
}

function docker-run() {
  pushd "$(dirname "$1")"
  docker run \
    -v "${PWD}":/local \
    "$(docker build -f "$(basename "$1")" -q .)"
  popd
}

if should-generate go; then
  OUT_DIR=examples/go/go-client/helper/rest
  generate twilio-go

  OUT_DIR=examples/go/go-client/terraform/resources
  generate terraform-provider-twilio

  # Replace a couple imports in the generated Terraform resource to use local code.
  for path in api/v2010 flex/v1; do
    sed -i.bak "s/github.com\/twilio\/twilio-go/go-client\/helper/g" "$OUT_DIR/$path/api_default.go"
    sed -i.bak "s/github.com\/twilio\/terraform-provider-twilio\/client/go-client\/terraform\/client/g" "$OUT_DIR/$path/api_default.go"
  done

  docker-run examples/go/Dockerfile-goimports
fi

if should-generate csharp; then
  OUT_DIR=examples/csharp/src/Twilio/Rest
  generate twilio-csharp
fi

if should-generate java; then
  OUT_DIR=examples/java/src/main/java/com/twilio/rest
  generate twilio-java
fi

if should-generate node; then
  OUT_DIR=examples/node/lib/rest
  generate twilio-node --global-property skipFormModel=false
  docker-run examples/node/Dockerfile-prettier
fi

if should-generate php; then
  OUT_DIR=examples/php/src/Twilio/Rest
  generate twilio-php
fi

if should-generate python; then
  OUT_DIR=examples/python/twilio/rest
  generate twilio-python
fi

if should-generate ruby; then
  OUT_DIR=examples/ruby/lib/twilio-ruby/rest
  generate twilio-ruby
fi
