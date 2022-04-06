#!/bin/bash
set -e

cd examples/prism
docker-compose build
docker-compose up -d --force-recreate --remove-orphans
docker-compose up --force-recreate --abort-on-container-exit --remove-orphans
