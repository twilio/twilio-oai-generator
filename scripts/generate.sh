#!/bin/bash
set -e

function should_generate() {
  [ "$LANGUAGES" == "" ] || [[ "$LANGUAGES" == *$1* ]]
}

function generate() {
  #find -E "$OUT_DIR"/*/* ! -name "*_test.go" ! -regex "$OUT_DIR/[^/]+/__init__.py" -type f -delete || true
  # Get language name without the twilio- prefix
  lang="${1#twilio-}"
  SPEC_DIR=${2:-examples/spec}

  # Use Python script to generate code
  echo "Using build_twilio_library.py to generate $1 code"
  python3 scripts/build_twilio_library.py "$SPEC_DIR" "$OUT_DIR" -l $lang

  # If additional properties were passed, use them
  if [ -n "$2" ]; then
    echo "Additional properties were passed: $2"
    echo "You may need to add these to build_twilio_library.py"
  fi
}

function docker_run() {
  pushd "$(dirname "$1")"
  docker run \
    -v "${PWD}":/local \
    "$(docker build -f "$(basename "$1")" -q .)"
  popd
}

if should_generate go; then
  OUT_DIR=examples/go/go-client/helper
  generate twilio-go

#  OUT_DIR=examples/go/go-client/terraform/resources
#  generate terraform-provider-twilio
  docker_run examples/go/Dockerfile-goimports
fi

if should_generate csharp; then
  OUT_DIR=examples/csharp/src/Twilio
  generate twilio-csharp
fi

if should_generate java; then
  OUT_DIR=examples/java/src/main/java/com/twilio
  generate twilio-java
fi

if should_generate node; then
  OUT_DIR=examples/node/src
  generate twilio-node
  docker_run examples/node/Dockerfile-prettier
fi

if should_generate php; then
  OUT_DIR=examples/php/src/Twilio
  generate twilio-php
fi

if should_generate python; then
  OUT_DIR=examples/python/twilio
  generate twilio-python
  generate twilio-python examples/test_spec/twilio_oneOf_v1.yaml
  docker_run examples/python/Dockerfile-prettier
fi

if should_generate ruby; then
  OUT_DIR=examples/ruby/lib/twilio-ruby
  generate twilio-ruby
  docker_run examples/ruby/Dockerfile-formatter
fi
