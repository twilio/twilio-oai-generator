#!/bin/sh

while true
do
  echo "Checking if Prism mock server is up... "
  wget -O/dev/null --header="Authorization: Basic `echo -n abc:123 | base64`" http://prism_twilio:4010/v1/Credentials/AWS > /dev/null 2>&1
  status=$?
  echo "Prism Status: $status"
  if [ $status -eq 0 ]; then
    echo "Prism mock server is up now."
    break
  fi
  sleep 0.1
done


>&2 echo "Prism is responding to API calls - executing command"
exec "$@"

