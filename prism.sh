#!/bin/bash
set -e

rm -rf codegen && mkdir -p codegen
make install
python3 examples/build_twilio_go.py examples/go/oai.yaml codegen
cd examples/go/prism
docker-compose build
docker-compose up --force-recreate --abort-on-container-exit
