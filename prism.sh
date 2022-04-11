#!/bin/bash
set -e

cd examples/prism
docker-compose build
docker-compose up -d --force-recreate --remove-orphans

echo -n "Waiting for tests to complete"
while [ "$(docker-compose ps -q go-client-test | xargs docker inspect -f "{{.State.Status}}")" != "exited" ]
do
  echo -n "."
  sleep 10
done
echo

EXIT_CODE=0
function check_status() {
  docker_test_services=("$@")
  for docker_test_service in "${docker_test_services[@]}"
  do
      if [[ $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}") -ne 0 ]]
        then
          EXIT_CODE=$(($EXIT_CODE || $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}")))
          echo "Failed $docker_test_service with EXIT code $(docker-compose ps -q $docker_test_service | xargs docker inspect -f "{{.State.ExitCode}}")"
        else
          echo "$docker_test_service completed successfully"
        fi
  done
}

testing_services=("go-client-test")
check_status "${testing_services[@]}"
docker-compose down
exit $EXIT_CODE
