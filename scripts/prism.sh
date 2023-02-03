#!/bin/bash
set -e

LANGUAGES=${LANGUAGES:-go java node csharp php ruby}

for language in ${LANGUAGES}; do
  testing_services+=("${language}-test")
done

cd examples/prism
docker-compose build --pull "${testing_services[@]}"
docker-compose up -d --force-recreate --remove-orphans "${testing_services[@]}"

function wait_for() {
  echo -n "Waiting for tests to complete"
  for language in ${LANGUAGES}; do
    while true; do
      if [[ "$(docker-compose ps -q -a "${language}-test" | xargs docker inspect -f "{{.State.Status}}")" != "exited" ]]; then
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
  for language in ${LANGUAGES}; do
    docker_test_service="${language}-test"
    if [[ $(docker-compose ps -q -a "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}") -ne 0 ]]; then
      EXIT_CODE=$(($EXIT_CODE || $(docker-compose ps -q -a "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}")))
      echo "Failed $language with EXIT code $(docker-compose ps -q -a "$docker_test_service" | xargs docker inspect -f "{{.State.ExitCode}}")"
      cat "../${language}/test-report.out"
    else
      echo "$language completed successfully"
    fi
  done
}

wait_for
check_status
docker-compose down
exit $EXIT_CODE
