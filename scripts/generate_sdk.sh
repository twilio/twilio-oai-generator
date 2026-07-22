#!/bin/bash
set -e

SUPPORTED_LANGUAGES="go csharp java node php python ruby"
SPEC_DIR="examples/spec"
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

usage() {
  echo "Usage: $0 <language>"
  echo ""
  echo "Generate an SDK locally for the given language using the spec in $SPEC_DIR."
  echo ""
  echo "Supported languages: $SUPPORTED_LANGUAGES"
  echo ""
  echo "Examples:"
  echo "  $0 node"
  echo "  $0 python"
  exit 1
}

if [ -z "$1" ]; then
  usage
fi

LANGUAGE="$1"

if ! echo "$SUPPORTED_LANGUAGES" | grep -qw "$LANGUAGE"; then
  echo "Error: unsupported language '$LANGUAGE'"
  echo "Supported languages: $SUPPORTED_LANGUAGES"
  exit 1
fi

cd "$PROJECT_ROOT"

# Build the generator JAR
echo "==> Building openapi-generator JAR..."
mvn clean package -DskipTests -q

# Determine output directory
case "$LANGUAGE" in
  go)      OUT_DIR="examples/go/go-client/helper" ;;
  csharp)  OUT_DIR="examples/csharp/src/Twilio" ;;
  java)    OUT_DIR="examples/java/src/main/java/com/twilio" ;;
  node)    OUT_DIR="examples/node/src" ;;
  php)     OUT_DIR="examples/php/src/Twilio" ;;
  python)  OUT_DIR="examples/python/twilio" ;;
  ruby)    OUT_DIR="examples/ruby/lib/twilio-ruby" ;;
esac

# Run code generation
echo "==> Generating $LANGUAGE SDK from $SPEC_DIR into $OUT_DIR..."
python3 scripts/build_twilio_library.py "$SPEC_DIR" "$OUT_DIR" -l "$LANGUAGE"

# Run language-specific formatters
echo "==> Running post-generation formatters..."
docker_run() {
  pushd "$(dirname "$1")" > /dev/null
  docker run --rm -v "${PWD}":/local "$(docker build -f "$(basename "$1")" -q .)"
  popd > /dev/null
}

case "$LANGUAGE" in
  go)     docker_run examples/go/Dockerfile-goimports ;;
  node)   docker_run examples/node/Dockerfile-prettier ;;
  python) docker_run examples/python/Dockerfile-prettier ;;
  ruby)   docker_run examples/ruby/Dockerfile-formatter ;;
  *)      echo "(no formatter configured for $LANGUAGE)" ;;
esac

echo "==> Done. Generated $LANGUAGE SDK at $OUT_DIR"
