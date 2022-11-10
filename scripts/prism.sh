#!/bin/bash
set -e

LANGUAGES=${LANGUAGES:-go java node csharp php}

for library in ${LANGUAGES}; do
  testing_services+=("${library}-test")
done

cd examples/prism
docker-compose build --pull "${testing_services[@]}"
docker-compose up -d --force-recreate --remove-orphans "${testing_services[@]}"

function wait_for() {
  echo -n "Waiting for tests to complete"
  for docker_test_service in "$@"; do
    while true; do
      if [[ "$(docker-compose ps -q "$docker_test_service" | xargs docker inspect -f "{{.State.Status}}")" != "exited" ]]; then
        echo -n "."
        sleep 10
      else
        break
      fi
    done
  done
  echo
}

EXIT_CODE=0
function check_status() {
  for docker_test_service in "$@"; do
    if [[ $(docker-compose ps -q "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}") -ne 0 ]]; then
      EXIT_CODE=$(($EXIT_CODE || $(docker-compose ps -q "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}")))
      echo "Failed $docker_test_service with EXIT code $(docker-compose ps -q "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}")"
    else
      echo "$docker_test_service completed successfully"
    fi
  done
}

wait_for "${testing_services[@]}"
check_status "${testing_services[@]}"
docker-compose down
exit $EXIT_CODE
