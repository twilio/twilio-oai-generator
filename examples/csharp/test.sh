#!/bin/bash
set -e

sleep 10

if [ "$SONAR_TOKEN" = "" ]; then
  make test
else
  make cover PROJECT_NAME=twilio_twilio-oai-generator-csharp \
             SONAR_SOURCES="/d:sonar.inclusions=src/Twilio/Rest/**/* /d:sonar.cs.analyzeGeneratedCode=true /d:sonar.exclusions=test/Twilio.Test/**/*"
fi