#!/bin/bash
set -e

cd examples/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
