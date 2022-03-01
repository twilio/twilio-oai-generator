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

OUT_DIR=examples/java/rest
rm -rf $OUT_DIR
java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar  \
  org.openapitools.codegen.OpenAPIGenerator \
  generate -g twilio-java \
  -i $API_SPEC \
  -o $OUT_DIR/api  \
  --global-property apiTests=false,apiDocs=false


cd examples/prism
docker-compose build
docker-compose up -d --force-recreate --remove-orphans


while [ "$(docker-compose ps -q golang-test | xargs docker inspect -f "{{.State.Status}}")" != "exited" ] || [ "$(docker-compose ps -q java-test | xargs docker inspect -f "{{.State.Status}}")" != "exited" ]
do
  echo " Waiting for tests to complete"
done

EXIT_CODE=0
function check_status() {
  docker_test_services=("$@")
  for docker_test_service in "${docker_test_services[@]}"
  do
      echo "$docker_test_service"
      if [[ $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}") -ne 0 ]]
        then
          EXIT_CODE=$(($EXIT_CODE || $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}")))
          echo "Failed $docker_test_service with EXIT code $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}")"
        else
          echo "$docker_test_service completed successfully"
        fi
  done
}

testing_services=("golang-test" "java-test")
check_status "${testing_services[@]}"
exit $EXIT_CODE
